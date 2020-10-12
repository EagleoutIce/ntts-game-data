package de.uulm.team020.helper.timer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;

/**
 * The goal of this class is to test the Timer-feature, including the resume
 * mechanism, which is important if there is a disconnect whilst a regular
 * game-pause
 *
 * <p>
 * Moved from game-server.
 * 
 * @author Florian Sihler
 * @version 1.2, 06/06/2020
 */
class TimeoutScheduleTest {

    // >= 1 !! (otherwise it will not wait the minimum amount of time needed)
    private static final int TIMEOUT_TOLERANCE_MULT = 4;

    @RepeatedTest(4)
    @Tag("Util")
    @Order(1)
    @DisplayName("[Timer] Test Timer run")
    public void test_timerRun() throws InterruptedException {

        AtomicBoolean flag = new AtomicBoolean(false);
        AtomicBoolean workedDiff = new AtomicBoolean(false);

        final long now = System.currentTimeMillis();

        TimeoutSchedule schedule = new TimeoutSchedule();
        schedule.runIn(() -> {
            synchronized (flag) {
                flag.set(true);
                flag.notifyAll();
            }
            final long inThread = System.currentTimeMillis();
            final long diff = inThread - now;
            synchronized (workedDiff) {
                workedDiff.set(diff > 40 && diff < 150);
                workedDiff.notifyAll();
            }
            Assertions.assertTrue(workedDiff.get(), "diff should be in (40, 150) but was: " + diff);
        }, 50);

        Assertions.assertTrue(schedule.hasTask(), "Task has been scheduled");
        long remTime = schedule.getRemainingTime();
        Assertions.assertTrue(remTime <= 50,
                "Remaining time should be never higher than requested (5) but was: " + remTime);

        synchronized (flag) {
            flag.wait(TIMEOUT_TOLERANCE_MULT * 50);
        }

        Assertions.assertTrue(flag.get(), "Thread should have run.");

        synchronized (workedDiff) {
            workedDiff.wait(TIMEOUT_TOLERANCE_MULT * 25); // just to compensate
        }

        Assertions.assertTrue(workedDiff.get(), "Thread should have run in the expected scope.");
        schedule.assureHealthy();
    }

    @RepeatedTest(2)
    @Tag("Util")
    @Order(2)
    @DisplayName("[Timer] Test Timer cancel")
    public void test_timerCancel() throws InterruptedException {

        AtomicBoolean flag = new AtomicBoolean(false);

        TimeoutSchedule schedule = new TimeoutSchedule();
        schedule.runIn(() -> {
            flag.set(true);
            Assertions.fail("We should not be here!");
        }, 50);

        Thread.sleep(20);

        schedule.cancelTimeout();

        Assertions.assertFalse(flag.get(), "Thread should not run immediately.");

        Thread.sleep(60);

        Assertions.assertFalse(flag.get(), "Thread should never run.");
        schedule.assureHealthy();
    }

    @RepeatedTest(2)
    @Tag("Util")
    @Order(3)
    @DisplayName("[Timer] Test Timer pause without resuming")
    public void test_timerPauseNoResume() throws InterruptedException {

        AtomicBoolean flag = new AtomicBoolean(false);

        TimeoutSchedule schedule = new TimeoutSchedule();
        schedule.runIn(() -> {
            flag.set(true);
            Assertions.fail("We should not be here!");
        }, 50);

        Thread.sleep(20);

        schedule.pause();

        Assertions.assertFalse(flag.get(), "Thread should not run immediately.");

        Thread.sleep(60);

        Assertions.assertFalse(flag.get(), "Thread should never run.");
        schedule.assureHealthy();
    }

    @RepeatedTest(5)
    @Tag("Util")
    @Order(4)
    @DisplayName("[Timer] Test Timer pause with resuming")
    public void test_timerPauseWithResume() throws InterruptedException {

        AtomicBoolean flag = new AtomicBoolean(false);
        AtomicBoolean workedDiff = new AtomicBoolean(false);

        final long now = System.currentTimeMillis();

        TimeoutSchedule schedule = new TimeoutSchedule();
        schedule.runIn(() -> {
            flag.set(true);
            final long inThread = System.currentTimeMillis();
            final long diff = inThread - now;
            synchronized (workedDiff) {
                workedDiff.set(diff > 135 && diff < 280);
                workedDiff.notifyAll();
            }
            Assertions.assertTrue(workedDiff.get(), "diff should be in (95+40, 130+150) but was: " + diff);
        }, 100);

        Thread.sleep(60);

        Assertions.assertTrue(schedule.pause(), "Pause should work");

        Assertions.assertFalse(flag.get(), "Thread should not run immediately.");

        Thread.sleep(60);

        Assertions.assertFalse(flag.get(), "Thread should still not run.");

        Assertions.assertTrue(schedule.resume(), "Resume should work");

        synchronized (workedDiff) {
            workedDiff.wait(TIMEOUT_TOLERANCE_MULT * 55);
        }
        Assertions.assertTrue(flag.get(), "Thread should now have run.");
        Assertions.assertTrue(workedDiff.get(), "Thread should now have run.");

        Assertions.assertFalse(schedule.resume(), "Resume should not work anymore as completed");
        Thread.sleep(50);
        Assertions.assertTrue(schedule.getRemainingTime() < 0, "No remaining pause time, as done already...");
        schedule.assureHealthy();
    }

    @RepeatedTest(5)
    @Tag("Util")
    @Order(5)
    @DisplayName("[Timer] Test Timer pause with resuming on executor thread")
    public void test_timerPauseWithResumeOnExecutorThread() throws InterruptedException {

        AtomicBoolean flag = new AtomicBoolean(false);
        AtomicBoolean workedDiff = new AtomicBoolean(false);

        final long now = System.currentTimeMillis();
        ExecutorService service = Executors.newFixedThreadPool(1);
        Assertions.assertNotNull(service, "Service has to be present to test");
        TimeoutSchedule schedule = new TimeoutSchedule(service);
        schedule.runIn(() -> {
            flag.set(true);
            final long inThread = System.currentTimeMillis();
            final long diff = inThread - now;
            synchronized (workedDiff) {
                workedDiff.set(diff > 135 && diff < 280);
                workedDiff.notifyAll();
            }
            Assertions.assertTrue(workedDiff.get(), "diff should be in (95+40, 130+150) but was: " + diff);
        }, 100);

        Thread.sleep(60);

        Assertions.assertTrue(schedule.pause(), "Pause should work");
        long remTime = schedule.getRemainingTime();
        Assertions.assertTrue(remTime >= 20, "Should be paused and therefore higher than 20 now, but: " + remTime);
        Assertions.assertFalse(flag.get(), "Thread should not run immediately.");

        Thread.sleep(40);

        Assertions.assertFalse(flag.get(), "Thread should still not run.");

        Assertions.assertTrue(schedule.resume(), "Resume should work");

        synchronized (workedDiff) {
            workedDiff.wait(TIMEOUT_TOLERANCE_MULT * 55);
        }

        Assertions.assertTrue(flag.get(), "Thread should now have run.");
        Assertions.assertTrue(workedDiff.get(), "Thread should now have run.");
        schedule.assureHealthy();

        Assertions.assertFalse(schedule.resume(), "Resume should not work anymore as completed");
        Thread.sleep(50);
        Assertions.assertTrue(schedule.getRemainingTime() < 0, "No remaining pause time, as done already...");
        Assertions.assertFalse(schedule.cancelTimeout(), "Cancel should not work as there is no task anymore");
        schedule.assureHealthy();
    }

    @RepeatedTest(2)
    @Tag("Util")
    @Order(6)
    @DisplayName("[Timer] Test Timer assert on reschedule")
    public void test_timerAssertThrowOnReschedule() throws InterruptedException {

        AtomicBoolean flag = new AtomicBoolean(false);

        TimeoutSchedule schedule = new TimeoutSchedule();
        schedule.runIn(() -> {
            flag.set(true);
            Assertions.fail("We should not be here!");
        }, 50);

        Thread.sleep(20);

        Assertions.assertThrows(TimeoutAlreadyRegisteredException.class, () -> schedule.runIn(() -> {
            flag.set(true);
            Assertions.fail("We should not be here!");
        }, 50));

        schedule.pause();

        Assertions.assertFalse(flag.get(), "Thread should not run immediately.");

        Thread.sleep(60);

        Assertions.assertFalse(flag.get(), "Thread should never run.");
        schedule.assureHealthy();
    }

    @RepeatedTest(4)
    @Tag("Util")
    @Order(6)
    @DisplayName("[Timer] Test Timer is healthy with exception")
    public void test_timerIsHealthyWithException() throws InterruptedException {

        AtomicBoolean flag = new AtomicBoolean(false);

        TimeoutSchedule schedule = new TimeoutSchedule();

        Assertions.assertTrue(schedule.isHealthy(), "Should be healthy as nothing run");
        Assertions.assertFalse(schedule.didRun(), "Task did not run (wasn't even scheduled)");

        schedule.runIn(() -> {
            synchronized (flag) {
                flag.set(true);
                flag.notifyAll();
            }
            throw new RuntimeException("This is an outrage.");
        }, 50);

        Assertions.assertTrue(schedule.isHealthy(), "Should be healthy as nothing run");
        Assertions.assertFalse(schedule.didRun(), "Task did not run (wasn't even scheduled)");

        synchronized (flag) {
            flag.wait(TIMEOUT_TOLERANCE_MULT * 50);
        }

        Thread.sleep(25);

        Assertions.assertTrue(flag.get(), "Thread should have run.");

        Assertions.assertFalse(schedule.isHealthy(), "Should not be healthy as crashed");
        Assertions.assertTrue(schedule.didRun(), "Task should have executed now");
        Assertions.assertTrue(schedule.getException().isPresent(), "Has exception");
        Throwable ex = schedule.getException().get();
        Assertions.assertEquals(RuntimeException.class, ex.getClass(), "Should be as thrown (class).");
        Assertions.assertEquals("This is an outrage.", ex.getMessage(), "Should be as thrown (text).");
    }

    @RepeatedTest(4)
    @Tag("Util")
    @Order(6)
    @DisplayName("[Timer] Test Timer is healthy with no exception")
    public void test_timerIsHealthyWithNoException() throws InterruptedException {

        AtomicBoolean flag = new AtomicBoolean(false);

        TimeoutSchedule schedule = new TimeoutSchedule();

        Assertions.assertTrue(schedule.isHealthy(), "Should be healthy as nothing run");
        Assertions.assertFalse(schedule.didRun(), "Task did not run (wasn't even scheduled)");

        schedule.runIn(() -> {
            synchronized (flag) {
                flag.set(true);
                flag.notifyAll();
            }
        }, 50);

        Assertions.assertTrue(schedule.isHealthy(), "Should be healthy as nothing run");
        Assertions.assertFalse(schedule.didRun(), "Task did not run (wasn't even scheduled)");

        synchronized (flag) {
            flag.wait(TIMEOUT_TOLERANCE_MULT * 50);
        }

        Thread.sleep(25);

        Assertions.assertTrue(flag.get(), "Thread should have run.");

        Assertions.assertTrue(schedule.isHealthy(), "Should be healthy as crashed");
        Assertions.assertTrue(schedule.didRun(), "Task should have executed now");
        Assertions.assertFalse(schedule.getException().isPresent(), "Has no exception");

        schedule.assureHealthy();
    }
}