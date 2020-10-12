package de.uulm.team020.logging;

/**
 * Offers the Logging-Interface for game-data and can be used by any other
 * including module.
 * <p>
 * It is the interface to be used with {@link Magpie}
 * 
 * @author Florian Sihler
 * @version 1.0, 07/08/2020
 */
public interface IMagpie {
    public void trace(String sender);

    public void writeDebug(String text, String sender);

    public void writeInfo(String text, String sender);

    public void writeWarning(String text, String sender);

    public void writeError(String text, String sender);

    public void writeException(Throwable ex, String sender);

    public void writeExceptionShort(Exception ex, String sender);

    public void mirrorThisToConsole(boolean mirror);

    public void useRainbow(boolean rainbow);
}