package de.uulm.team020.parser.expander;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.uulm.team020.logging.Magpie;

/**
 * Contains the lookups for the {@link Expander}.
 * 
 * @author Florian Sihler
 * @version 1.1, 05/07/2020
 */
public class Expandables {

    private static final String REGISTER_TXT = "Register";
    private static final String EXPANSION_TXT = "Expansion";

    private static final int INITIAL_CAPACITY = 10;

    private static Magpie magpie = Magpie.createMagpieSafe(EXPANSION_TXT);

    private Map<String, iExpansionServant> servants;

    /**
     * Provide a constant expander for a key
     * 
     * @param val      Gets returned
     * @param key      ignored
     * @param option   ignored
     * @param level    ignored
     * @param expander ignored
     * @return The value
     */
    public String constantExpander(final String val, final String key, final String option, final int level,
            final Expander expander) {
        return val;
    }

    /**
     * Construct a new expandables instance you can register expansions for
     */
    public Expandables() {
        servants = new HashMap<>(INITIAL_CAPACITY);
    }

    /**
     * Register an expansion to a constant, if there is not already an expansion
     *
     * @param key      the key to expands for
     * @param constant the constant the key should expand to
     *
     * @return True if the key-constant was registered, false if there already has
     *         been an expansion for this key
     */
    public boolean registerConstant(final String key, final String constant) {
        magpie.writeInfo("Registering Constant for: " + key + " (" + constant + ")", REGISTER_TXT);
        return servants.putIfAbsent(key, (final String k, final String o, final int l,
                final Expander e) -> constantExpander(constant, k, o, l, e)) == null;
    }

    /**
     * Register an expansion to a constant. This will overwrite existing values! Use
     * {@link #registerConstant(String, String)} if you do not want this
     * overwriting. Please note, that constant-registration is the only expansion,
     * that shall be overwritten. Any other change in behaviour can be achieved by
     * modifying the expansion target.
     *
     * @param key      the key to expands for
     * @param constant the constant the key should expand to
     *
     * @return True if the key-constant was registered as a new one, false if there
     *         already has been an expansion for this key
     */
    public boolean assignConstant(final String key, final String constant) {
        magpie.writeInfo("Assigning Constant for: " + key + " (" + constant + ")", REGISTER_TXT);
        return servants.put(key, (final String k, final String o, final int l,
                final Expander e) -> constantExpander(constant, k, o, l, e)) == null;
    }

    /**
     * Register an expansion to a function call, if there is not already an
     * expansion
     *
     * @param key      the key to expands for
     * @param expander the function to be called if the key is found
     *
     * @return True if the key-expansion was registered, false if there already has
     *         been an expansion for this key
     */
    public boolean registerExpansion(final String key, final iExpansionServant expander) {
        magpie.writeInfo("Registering Expansion for: " + key + " (" + expander + ")", REGISTER_TXT);
        return servants.putIfAbsent(key, expander) == null;
    }

    /**
     * Update an expansion to a function call. Will register it, if there was none.
     *
     * @param key      the key to expands for
     * @param expander the function to be called if the key is found
     *
     * @return True if the key-expansion was overwritten, false if it was placed as
     *         new
     */
    public boolean updateExpansion(final String key, final iExpansionServant expander) {
        magpie.writeInfo("Registering/Updating Expansion for: " + key + " (" + expander + ")", REGISTER_TXT);
        return servants.put(key, expander) == null;
    }

    /**
     * Register an expansion to a Class-Field, can be used to access static
     * attributes of a class
     *
     * @param key         the key to expands for
     * @param targetClass the class to use
     *
     * @return True if the key-expansion was registered, false if there already has
     *         been an expansion for this key
     */
    public boolean registerStaticClassReflection(final String key, final Class<?> targetClass) {
        magpie.writeInfo("Registering static Class-Expansion for: " + key + " (" + targetClass + ")", REGISTER_TXT);
        return servants.putIfAbsent(key, (final String k, final String o, final int l, final Expander e) -> Expandables
                .reflectionStaticExpander(targetClass, k, o, l, e)) == null;
    }

    /**
     * Update an expansion to a Class-Field, can be used to access static attributes
     * of a class. Will register it, if there was none.
     *
     * @param key         the key to expands for
     * @param targetClass the class to use
     *
     * @return True if the key-expansion was overwritten, false if it was placed as
     *         new
     */
    public boolean updateStaticClassReflection(final String key, final Class<?> targetClass) {
        magpie.writeInfo("Registering/Updating static Class-Expansion for: " + key + " (" + targetClass + ")",
                REGISTER_TXT);
        return servants.put(key, (final String k, final String o, final int l, final Expander e) -> Expandables
                .reflectionStaticExpander(targetClass, k, o, l, e)) == null;
    }

    /**
     * Register an expansion to a Class-Field, can be used to access static
     * attributes of an object
     *
     * @param key The key to expands for
     * @param obj The object to perform the expansion for
     *
     * @return True if the key-expansion was registered, false if there already has
     *         been an expansion for this key
     */
    public boolean registerReflection(final String key, final Object obj) {
        magpie.writeInfo("Registering Class-Expansion for: " + key + " (" + obj.getClass() + ")", REGISTER_TXT);
        return servants.putIfAbsent(key, (final String k, final String o, final int l, final Expander e) -> Expandables
                .reflectionExpander(obj.getClass(), obj, k, o, l, e)) == null;
    }

    /**
     * Update an expansion to a Class-Field, can be used to access static attributes
     * of an object. Will register a new one if not registered before
     *
     * @param key The key to expands for
     * @param obj The object to perform the expansion for
     *
     * @return True if the key-expansion was overwritten, false if it was registered
     */
    public boolean updateReflection(final String key, final Object obj) {
        magpie.writeInfo("Registering/Updating Class-Expansion for: " + key + " (" + obj.getClass() + ")",
                REGISTER_TXT);
        return servants.put(key, (final String k, final String o, final int l, final Expander e) -> Expandables
                .reflectionExpander(obj.getClass(), obj, k, o, l, e)) == null;
    }

    /**
     * Finds the expansion for a given key, if there is any
     *
     * @param key      the key that was found
     * @param option   the option supplied with the key, null if none
     * @param level    the level the expansion occurred on
     * @param expander the expander
     *
     * @return the string found by the expansion
     */
    public String findExpansion(final String key, final String option, final int level, final Expander expander) {
        iExpansionServant servant;
        synchronized (this) {
            servant = servants.get(key);
            if (servant == null)
                return null;
        }
        return servant.produce(key, option, level, expander);
    }

    /**
     * Use with {@link #registerExpansion(String, iExpansionServant)} to get a
     * date-expander
     *
     * @param key      ignored
     * @param option   used to format the date
     * @param level    ignored
     * @param expander ignored
     * 
     * @return the current date
     */
    public static String expandDate(final String key, final String option, final int level, final Expander expander) {
        return new SimpleDateFormat(Objects.requireNonNullElse(option, "dd. MMM yyyy")).format(new Date());
    }

    /**
     * Use with {@link #registerExpansion(String, iExpansionServant)} to get a
     * time-expander
     * 
     * @param key      ignored
     * @param option   used to format the date
     * @param level    ignored
     * @param expander ignored
     * 
     * @return the current time
     */
    public static String expandTime(final String key, final String option, final int level, final Expander expander) {
        return new SimpleDateFormat(Objects.requireNonNullElse(option, "HH:mm:ss")).format(new Date());
    }

    /**
     * Use with {@link #registerExpansion(String, iExpansionServant)} to expand to
     * the current level, can be used for debugging.
     *
     * @param key      ignored
     * @param option   ignored
     * @param level    will be returned
     * @param expander ignored
     * 
     * @return the current time
     */
    public static String expandLevel(final String key, final String option, final int level, final Expander expander) {
        return String.valueOf(level);
    }

    /**
     * Use with {@link #registerExpansion(String, iExpansionServant)} to expand the
     * given option full(y)
     *
     * @param key      ignored
     * @param option   will be (fully) expanded
     * @param level    ignored
     * @param expander ignored
     * 
     * @return the current time
     */
    public static String expandFull(final String key, String option, final int level, final Expander expander) {
        option = option.startsWith("${") && option.endsWith("}") ? option : "${" + option + "}";
        return expander.expandFull(option);
    }

    /**
     * Use with {@link #registerExpansion(String, iExpansionServant)} to expand the
     * system-set environment variable, given in the option, see
     * {@link System#getenv(String)}.
     * 
     * @param key      ignored
     * @param option   will be used as environment key
     * @param level    will be returned
     * @param expander ignored
     * 
     * @return The environment variable value hearing by this name
     */
    public static String expandSystem(final String key, final String option, final int level, final Expander expander) {
        return System.getenv(Objects.requireNonNull(option, "The system expander needs an option!"));
    }

    /**
     * Expander to be used on reflection target
     * 
     * @param target   the target class to listen on
     * @param key      ignored
     * @param option   the field of interest
     * @param level    ignored
     * @param expander Expander to use for access
     * 
     * @return The value of the desired field (or null in case of an error as well)
     */
    @SuppressWarnings("java:S3011")
    public static String reflectionStaticExpander(final Class<?> target, final String key, final String option,
            final int level, final Expander expander) {
        try {
            Field f = target.getDeclaredField(option);
            f.setAccessible(true); // we want it :D
            return f.get(expander).toString();
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            magpie.writeException(ex, EXPANSION_TXT);
            return null;
        }
    }

    /**
     * Expander to be used on reflection target
     * 
     * @param target   The target class to listen on
     * @param obj      The object to perform the expansion on
     * @param key      Ignored
     * @param option   the field of interest
     * @param level    ignored
     * @param expander Expander to use for access
     * 
     * @return The value of the desired field (or null in case of an error as well)
     */
    @SuppressWarnings("java:S3011")
    public static String reflectionExpander(final Class<?> target, final Object obj, final String key,
            final String option, final int level, final Expander expander) {
        try {
            Field f = target.getDeclaredField(option);
            f.setAccessible(true); // we want it :D
            return f.get(obj).toString();
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            magpie.writeException(ex, EXPANSION_TXT);
            return null;
        }
    }

    /**
     * Remove a given key from the set of expandables
     * 
     * @param key The key to remove the linked expansion for
     * 
     * @return True if the key was removed, false if there was no expandable
     *         registered for this key
     */
    public boolean removeExpandable(String key) {
        return this.servants.remove(key) != null;
    }

    public Map<String, iExpansionServant> getServants() {
        return servants;
    }

    /**
     * Add a servant to the mix
     * 
     * @param other the servant to add (expander)
     */
    public void appendServants(Expandables other) {
        this.servants.putAll(other.getServants());
    }

}
