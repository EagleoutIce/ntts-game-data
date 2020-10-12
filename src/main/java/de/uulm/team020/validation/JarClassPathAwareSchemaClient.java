package de.uulm.team020.validation;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;

import org.everit.json.schema.loader.SchemaClient;

import de.uulm.team020.GameData;
import de.uulm.team020.logging.Magpie;

/**
 * Based on the Schema-Client implementation, this variant will work even if the
 * module is included inside of another, as it preserves the path format which
 * seems to be 'different' (for some reason) if used with the new json parser...
 * <p>
 * This client is - even if different in it's core behavior - based on the
 * ClassPathAwareSchemaClient.
 * <p>
 * The Author-declaration marks the author of all changes made to the default
 * implementation. The 'fallback'-behavior has been kept as it doesn't harm in
 * any way - even though there should be no reason to call it (but there could
 * be one).
 * 
 * 
 * @author Florian Sihler
 * @version 1.0, 03/27/2020
 */
class JarClassPathAwareSchemaClient implements SchemaClient {

    private static Magpie magpie = Magpie.createMagpieSafe("Validator");

    private static final List<String> HANDLED_PREFIXES = List.of("classpath://", "classpath:/", "classpath:");

    private final SchemaClient fallbackClient;

    JarClassPathAwareSchemaClient(SchemaClient fallbackClient) {
        this.fallbackClient = requireNonNull(fallbackClient, "fallbackClient cannot be null");
    }

    @Override
    public InputStream get(String url) {
        Optional<String> maybeString = handleProtocol(url);
        if (maybeString.isPresent()) {
            InputStream stream = this.loadFromClasspath(maybeString.get());
            if (stream != null) {
                return stream;
            } else {
                throw new UncheckedIOException(new IOException(String.format("Could not find %s", url)));
            }
        } else {
            return fallbackClient.get(url);
        }
    }

    private InputStream loadFromClasspath(String str) {
        if (str.endsWith("/"))
            str = str.substring(0, str.length() - 1);
        magpie.writeDebug("Loading cleansed: " + str, "Schema-Loader");

        return GameData.class.getResourceAsStream(str);
    }

    private Optional<String> handleProtocol(String url) {
        return HANDLED_PREFIXES.stream().filter(url::startsWith).map(prefix -> "/" + url.substring(prefix.length()))
                .findFirst();
    }

}