package de.uulm.team020.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * Write Data to stdout.
 * 
 * @author Florian Sihler
 * @version 1.0, 06/02/2020
 */
public class MagpieConsoleHandler extends StreamHandler {

    private Formatter formatter;

    public MagpieConsoleHandler(Formatter formatter) {
        super();
        this.formatter = formatter;
    }

    @Override
    public synchronized void publish(LogRecord record) {
        System.out.print(formatter.format(record));
    }

}