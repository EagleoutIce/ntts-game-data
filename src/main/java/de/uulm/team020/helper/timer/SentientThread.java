package de.uulm.team020.helper.timer;

/**
 * This class is basically a Thread as it derives by one. The goal of a
 * Sentient-Thread is to offer a simpel way to handle Exceptions that occur
 * inside the thread and don't get caught. This implementation wants you to
 * implement the handler yourself (you may pass a fitting function-reference
 * as-well), you may use one of the handlers already present in
 * {@link Sentients}.
 * 
 * @author Florian Sihler
 * @version 1.0, 06/06/2020
 */
public class SentientThread extends Thread {

    private final iSentientHandler handler;

    /**
     * Build a new Sentient Thread
     * 
     * @param handler The method to be called in case of any uncaught exception
     */
    public SentientThread(iSentientHandler handler) {
        super();
        this.handler = handler;
        this.setUncaughtExceptionHandler(this.handler::handle);
    }

    /**
     * Build a new Sentient Thread
     * 
     * @param target  The target to be run on {@link #run()}
     * @param handler The method to be called in case of any uncaught exception
     */
    public SentientThread(Runnable target, iSentientHandler handler) {
        super(target);
        this.handler = handler;
        this.setUncaughtExceptionHandler(this.handler::handle);
    }

    /**
     * Build a new named Sentient Thread
     * 
     * @param name    The name of the thread
     * @param handler The method to be called in case of any uncaught exception
     */
    public SentientThread(String name, iSentientHandler handler) {
        super(name);
        this.handler = handler;
        this.setUncaughtExceptionHandler(this.handler::handle);
    }

    /**
     * Build a new named Sentient Thread
     * 
     * @param target  The target to be run on {@link #run()}
     * @param name    The name of the thread
     * @param handler The method to be called in case of any uncaught exception
     */
    public SentientThread(Runnable target, String name, iSentientHandler handler) {
        super(target, name);
        this.handler = handler;
        this.setUncaughtExceptionHandler(this.handler::handle);
    }

    /**
     * The Handler set on construction.
     * 
     * @return the handler
     */
    public iSentientHandler getHandler() {
        return handler;
    }
}