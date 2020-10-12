package de.uulm.team020.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import de.uulm.team020.logging.Magpie;

/**
 * This class has the goal to provide a simple interface to acquire internal
 * resources.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/03/2020
 * @since 1.2
 */
public class InternalResources {

    private static Magpie magpie = Magpie.createMagpieSafe("GameData");

    // Hide the default one
    private InternalResources() {
    }

    /**
     * Just a simple one-liner for accessing internal resources. You can
     * theoretically extend this class to exchange meaning.
     * 
     * @param path The wanted internal Path
     * 
     * @return input stream if 'gettable'
     */
    public static InputStream getFileInputStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    /**
     * This will load a File from resources and return the Stream for you to handle
     * 
     * @param path  the path you desire
     * @param eater will be the function capable of transforming the file-data to a
     *              Type you desire. Please note, that you cannot return the
     *              file-resource as we will do everything to close it - enlighten
     *              your fire
     * 
     * @param <T>   Type for conversion
     * 
     * @return the Stream you admire
     * 
     * @throws IOException if the acquire conspire'
     */
    public static <T> T getFile(String path, Function<Stream<String>, T> eater) throws IOException {
        try (InputStream is = getFileInputStream(path)) {
            // We want to get one String without nasty newlines :D
            InputStreamReader isReader = new InputStreamReader(Objects.requireNonNull(is));
            BufferedReader bufferedReader = new BufferedReader(isReader);
            Stream<String> dataStream = bufferedReader.lines();
            T dataT = eater.apply(dataStream);
            isReader.close();
            bufferedReader.close();
            return dataT;
        } catch (IOException ex) {
            magpie.writeException(ex, "Load");
            throw ex;
        }
    }

    /**
     * Returns array of lines in a (internal) file
     * 
     * @param path the path to the file you desire
     * 
     * @return All lines in a File as array
     * 
     * @throws IOException if the acquire conspire'
     */
    public static String[] getFileLines(String path) throws IOException {
        return InternalResources.getFile(path, s -> s.toArray(String[]::new));
    }

}