package de.uulm.team020.helper.timer;

import java.util.Objects;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.networking.messages.RequestMetaInformationMessage;

/**
 * Used by the server to schedule timeouts for players. It doesn't care if it is
 * a forced pause timeout or a reconnect timeout. This may be used for the
 * client too (but only to guess when the server will terminate) and will only
 * generate valid time-stamps IF the server pauses the timer correctly (which
 * depends on the implementation) -- as there is no setting revealing this
 * information, clients still may rely on {@link RequestMetaInformationMessage}
 * with {@link MetaKeyEnum#GAME_REMAINING_PAUSE_TIME} (which must not be
 * implemented correctly as well).
 * <p>
 * Moved from game-server. Was updated with 1.3 to allow support for
 * {@link iSentientHandler} and to check for the life-cycle of the supplied
 * thread.
 * 
 * @author Florian Sihler
 * @version 1.3, 06/06/2020
 * 
 * @since 1.2
 */
public class TimeoutSchedule {

    private static final Magpie magpie = Magpie.createMagpieSafe("Timeout");

    private final Timer timer;
    private TimerTask task;
    private Runnable runnable;

    private ExecutorService service;

    private long remainingTime;

    private AtomicBoolean paused = new AtomicBoolean();
    private AtomicBoolean didRun = new AtomicBoolean();

    // exception which may be thrown, access will be synchronized
    private Throwable throwable = null;

    /**
     * Construct a new timeout schedule to be used with a controller to be paused
     * and resumed
     */
    public TimeoutSchedule() {
        this(null);
    }

    /**
     * Construct a new timeout schedule to be used with a controller to be paused
     * and resumed
     * 
     * @param service Service to be used to execute the tasks. Please note, that if
     *                used, this will mean the exact execution-time can vary.
     */
    public TimeoutSchedule(ExecutorService service) {
        timer = new Timer(true);
        task = null;
        clearBuffer();
        this.service = service;
    }

    private void clearBuffer() {
        remainingTime = -1;
        paused.set(false);
        didRun.set(false);
        this.throwable = null;
    }

    /**
     * Select the target time to run the given Thread in. Please not that in case of
     * any exception this will cause the exception to be logged silently. This will
     * clear the {@link #isHealthy()}-status so any previously stored exception will
     * be lost.
     * 
     * @param task        The task to run
     * @param timeoutInMs The time in millisecond to wait until the task shall be
     *                    run (note, that this time is <i>not</i> exact and may vary
     *                    slightly).
     */
    public void runIn(final Runnable task, long timeoutInMs) {
        if (this.task != null)
            throw new TimeoutAlreadyRegisteredException("There is a timeout currently running");
        clearBuffer();
        this.runnable = task;
        // There is no reason to guard as all other tasks have been killed for this
        this.task = buildTT();
        timer.schedule(this.task, timeoutInMs);
    }

    // Allowed as we will just use it to store!
    @SuppressWarnings("squid:S1181")
    private TimerTask buildTT() {
        return new TimerTask() {// Schedule it :D
            @Override
            public void run() {
                if (Objects.isNull(TimeoutSchedule.this.service)) {
                    runTTWithoutService();
                } else {
                    runTTWithService();
                }
            }

            private void runTTWithService() {
                TimeoutSchedule.this.service.execute(() -> {
                    try {
                        runnable.run();
                    } catch (TimerScheduleExecutionException tsEx) {
                        magpie.writeException(tsEx, "Schedule");
                        synchronized (TimeoutSchedule.this) {
                            throwable = tsEx;
                        }
                    } catch (Throwable tEx) {
                        synchronized (TimeoutSchedule.this) {
                            throwable = tEx;
                        }
                    } finally {
                        didRun.set(true);
                    }
                    TimeoutSchedule.cleanup(TimeoutSchedule.this, this);
                });
            }

            private void runTTWithoutService() {
                try {
                    runnable.run();
                } catch (Throwable tEx) {
                    synchronized (TimeoutSchedule.this) {
                        throwable = tEx;
                    }
                } finally {
                    didRun.set(true);
                }
                TimeoutSchedule.cleanup(TimeoutSchedule.this, this);
            }
        };
    }

    /**
     * Will be called when the task has completed.
     * 
     * @param schedule  the schedule to be called on cleanup
     * @param timerTask The task to cleanup
     */
    private static synchronized void cleanup(TimeoutSchedule schedule, TimerTask timerTask) {
        if (schedule.task == timerTask) {
            // Still the one we started with?
            schedule.task = null;
        }
    }

    /**
     * Cancels the currently running timer
     * 
     * @return True if the task was cancelled, false if not applicable
     */
    public boolean cancelTimeout() {
        if (task == null)
            return false;
        boolean feedback = this.task.cancel();
        this.task = null;
        this.timer.purge();
        return feedback;
    }

    /**
     * Pause the timer if it is currently scheduled
     *
     * @return True if the Timer was paused, false otherwise
     */
    public boolean pause() {
        if (task == null)
            return false;
        long calcRemainingTime = task.scheduledExecutionTime() - System.currentTimeMillis();
        if (calcRemainingTime < 0)
            return false; // already in execution/time over
        this.remainingTime = calcRemainingTime;
        this.task.cancel();
        this.timer.purge();
        this.paused.set(true);
        return true;
    }

    /**
     * If the timer is paused via {@link #pause()} it will return the remaining
     * time, otherwise it will calculate the remaining time
     *
     * @return the remaining time in {@link #remainingTime} (ms). Will return -1 if
     *         nothing is scheduled
     */
    public synchronized long getRemainingTime() {
        if (!hasTask())
            return -1;
        if (paused.get())
            return this.remainingTime;
        else
            return task.scheduledExecutionTime() - System.currentTimeMillis();
    }

    /**
     * Resumes the timer if it is currently paused
     *
     * @return True if the timer was to be resumed
     */
    public boolean resume() {
        if (!paused.get())
            return false;
        this.task = buildTT();
        timer.schedule(this.task, remainingTime);
        paused.set(false);
        return true;
    }

    /**
     * @return True if task is already embedded, false otherwise
     */
    public boolean hasTask() {
        return this.task != null;
    }

    /**
     * Will return true if the scheduled task was executed -- this will be reset if
     * you call {@link #runIn(Runnable, long)} again. This will also be true
     * <i>if</i> the task failed with an exception. See {@link #isHealthy()} for
     * that.
     * 
     * @return True if the task was executed, false otherwise.
     */
    public boolean didRun() {
        return this.didRun.get();
    }

    /**
     * This method will check if the timer task has thrown any exception. If so, you
     * may receive it by {@link #getException()}.
     * <p>
     * Note that this will return true if the task wasn't even run (check
     * {@link #getRemainingTime()})
     * 
     * @return True if there was no exception so far, false otherwise.
     */
    public boolean isHealthy() {
        synchronized (this) {
            return throwable == null;
        }
    }

    /**
     * Instead of {@link #isHealthy()} this will throw a
     * {@link TimerScheduleExecutionException} if the task is not healthy.
     * 
     * @see #isHealthy()
     */
    public void assureHealthy() {
        synchronized (this) {
            if (throwable != null) {
                throw new TimerScheduleExecutionException(throwable);
            }
        }
    }

    /**
     * Returns the exception if there is one.
     * 
     * @return Exception thrown by the task if there is one.
     */
    public Optional<Throwable> getException() {
        synchronized (this) {
            return Optional.ofNullable(throwable);
        }
    }
}