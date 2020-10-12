package de.uulm.team020.validation;

import java.util.LinkedHashSet;
import java.util.Set;

import org.everit.json.schema.Schema;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Small equals implementation that violates the equals-contract These
 * implementations are <i>order-independent</i> and written to provide more
 * possibilities in tests.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/27/2020
 */
public class SchemaHelper {

    // Hide the default one
    private SchemaHelper() {

    }

    /**
     * Somewhat of a dirty workaround right now to compare to schemas, as toString
     * does invalidate direction. To find this out was somewhat of a pain.
     * 
     * @param a schema a
     * @param b schema
     * @return if a should be equal to b ^^
     */
    public static boolean jsonSchemaEquals(Schema a, Schema b) {
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;

        // maybe add strict-mode with id and or location check?

        // compare title and description, if they differ we do
        // not have to continue and can save some time

        if (a.getTitle() != null && !a.getTitle().equals(b.getTitle()) || a.getTitle() == null && b.getTitle() != null)
            return false;
        if (a.getDescription() != null && !a.getDescription().equals(b.getDescription())
                || a.getDescription() == null && b.getDescription() != null)
            return false;

        // Load to JSON objects:
        JSONObject aJs = new JSONObject(new JSONTokener(a.toString()));
        JSONObject bJs = new JSONObject(new JSONTokener(b.toString()));

        return jsonObjEquals(aJs, bJs);
    }

    /**
     * Compare two json objects independent of their size
     * 
     * @param a obj a
     * @param b obj b
     * @return true if they are equal (ignoring order)
     */
    public static boolean jsonObjEquals(JSONObject a, JSONObject b) {
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;
        Set<String> aKeys = a.keySet();
        Set<String> bKeys = b.keySet();

        if (aKeys.size() != bKeys.size())
            return false; // sizes differ
        if (!aKeys.containsAll(bKeys))
            return false; // b is no subset of a
        if (!bKeys.containsAll(aKeys))
            return false; // a is no subset of b

        for (String key : aKeys) {
            Object aval = a.get(key);
            Object bval = b.get(key);
            if (!objectCompareHelper(aval, bval))
                return false;
        }
        return true;
    }

    private static boolean objectCompareHelper(Object a, Object b) {
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;
        if (a.getClass() != b.getClass()) {
            return false; // different types
        }

        if (a.getClass() == JSONObject.class) {
            return jsonObjEquals((JSONObject) a, (JSONObject) b);
        } else if (a.getClass() == JSONArray.class) {
            return jsonArrayEquals((JSONArray) a, (JSONArray) b);
        } else {
            return a.equals(b); // probably primary type
        }
    }

    /**
     * Compare two json objects independent of their size
     * 
     * @param a obj a
     * @param b obj b
     * @return true if they are equal (ignoring order)
     */
    public static boolean jsonArrayEquals(JSONArray a, JSONArray b) {
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;

        int aLen = a.length();
        int bLen = b.length();

        if (aLen != bLen)
            return false; // sizes differ

        // As we have to be independent from size
        // and we cannot rely on the hashcode as it will
        // be different on different suborders
        // sorting would be as complex as trivial search
        // as this method is for testing purposes mostly
        // we will accept O(n^2) wc complexity

        // We have another problem.
        // If a = [1, 1] and b = [1, 2]
        // every key in a will find a match in 'b'
        // even if their not equal, therefore we will
        // lock the index to avoid matching multiple times

        Set<Integer> founds = new LinkedHashSet<>(bLen);
        for (int i = 0; i < aLen; i++) {
            Object aval = a.get(i);
            boolean found = false; // maybe method subscope?
            for (int j = 0; j < bLen; j++) {
                if (founds.contains(j)) // already mapped
                    continue;
                Object bval = b.get(j);
                if (objectCompareHelper(aval, bval)) {
                    founds.add(j);
                    found = true;
                    break;
                }
            }
            if (!found)
                return false; // early backoff :D
        }

        return true;

    }

}