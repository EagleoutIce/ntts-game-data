package de.uulm.team020.logging;

import java.io.IOException;
import java.util.logging.FileHandler;

/**
 * Basically works like the parent-class {@link FileHandler},
 * but in difference to the father/mother this instance will hold
 * the pattern-name the File was created with!
 * 
 * @author Florian Sihler
 * @version 1.0, 03/17/2020
 */
public class MagpieFileHandler extends FileHandler {

    private String pattern;

    /**
     * Constructs a FileHandler
     * 
     * @param pattern the target file-pattern
     * @throws IOException If {@link FileHandler#FileHandler(String)} throws
     */
    public MagpieFileHandler(String pattern) throws IOException {
        super(pattern);
        this.pattern = pattern;
    }

    /**
     * @return the pattern used to construct the handler
     */
    public String getPattern() {
        return this.pattern;
    }
    
}