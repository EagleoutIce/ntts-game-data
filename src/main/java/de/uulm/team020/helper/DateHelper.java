package de.uulm.team020.helper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Just a helper, to get current times with java8 time and correct timezone but
 * returning them as casual date. The other variant worked too but this seams to
 * be more clean.
 *
 *
 * @author Florian Sihler
 * @version 1.0, 06/07/2020
 */
public class DateHelper {

    private static ZoneId zone = ZoneId.of("UTC+01:00");

    /* Hide the public one */
    private DateHelper() {
    }

    /**
     * Set the Timezone
     * 
     * @param zone the zone to set
     */
    public static void setZone(ZoneId zone) {
        DateHelper.zone = zone;
    }

    /**
     * Get Timezone
     * 
     * @return the zone
     */
    public static ZoneId getZone() {
        return zone;
    }

    /**
     * Get the date-time <i>right now</i> in the configured time zone
     * 
     * @return Date-Object representing the current timepoint in the configured
     *         timezone
     */
    public static Date now() {
        ZonedDateTime zdt = ZonedDateTime.now(zone);
        return new GregorianCalendar(zdt.getYear(), zdt.getMonthValue(), zdt.getDayOfMonth(), zdt.getHour(),
                zdt.getMinute(), zdt.getSecond()).getTime();
    }
}
