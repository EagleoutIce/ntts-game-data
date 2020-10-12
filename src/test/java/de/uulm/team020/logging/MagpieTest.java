package de.uulm.team020.logging;

import org.junit.jupiter.api.*;

import java.io.File;

/**
 * We can't "really" test the logger as this would mean recreating 
 * everything the logger does - which is nasty. Therefore we will
 * check if the logger is able to acquire a target file, to handle
 * writing-requests etc. 
 * 
 * @author Florian Sihler
 * @version 1.0, 03/18/2020
 */
public class MagpieTest {

    @Test @Tag("Core") @Order(1)
    @DisplayName("[LOG] Path-Validity")
    public void test_path_validity() throws Exception {
        // Acquire a temporary file:
        File tmpfile = File.createTempFile("ntts-testing-magpie-", ".log");
        String filepath = tmpfile.getAbsolutePath();
        // Construct the FileHandler for it
        MagpieFileHandler magpieFileHandler = new MagpieFileHandler(filepath);

        Assertions.assertEquals(filepath, magpieFileHandler.getPattern(), "The reason to write this extensions, is this use-case... it should work ^^");

        // Somewhat of a cleanup :D
        magpieFileHandler.close();
        Assertions.assertTrue(tmpfile.delete(), "The file should be delete-able");
    }

    
    // Here it would be theoretically possible to test the other writers,
    // but this doesn't seem to be necessary at the time of writing this :D
    
}