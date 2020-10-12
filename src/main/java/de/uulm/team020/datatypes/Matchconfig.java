package de.uulm.team020.datatypes;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a match-configuration as standardized. To avoid any problems with
 * 'legacy'-configuration, this instance should support any Iterations of the
 * standard-committee.
 * <p>
 * This means the Matchconfig-Container supports
 * <ul>
 * <li>"moledieRange"</li>
 * <li>"moledie_Range"</li>
 * <li>"Moledie_Range"</li>
 * </ul>
 * as field-names in json. They will get mapped on the scheme of the current
 * standard ("moledieRange").
 * 
 * @author Florian Sihler
 * @version 1.5, 04/26/2020
 */
public class Matchconfig implements IAmJson {

    private static final long serialVersionUID = 1373388200120744023L;

    /* Gadget: Moledie */
    @SerializedName(value = "moledieRange", alternate = { "moledie_Range", "Moledie_Range" })
    private int moledieRange;
    /* Gadget: BowlerBlade */
    @SerializedName(value = "bowlerBladeRange", alternate = { "bowlerBlade_Range", "BowlerBlade_Range" })
    private int bowlerBladeRange;
    @SerializedName(value = "bowlerBladeHitChance", alternate = { "bowlerBlade_HitChance", "BowlerBlade_HitChance" })
    private double bowlerBladeHitChance;
    @SerializedName(value = "bowlerBladeDamage", alternate = { "bowlerBlade_Damage", "BowlerBlade_Damage" })
    private int bowlerBladeDamage;
    /* Gadget: LaserCompact */
    @SerializedName(value = "laserCompactHitChance", alternate = { "laserCompact_HitChance", "LaserCompact_HitChance" })
    private double laserCompactHitChance;
    /* Gadget: RocketPen */
    @SerializedName(value = "rocketPenDamage", alternate = { "rocketPen_Damage", "RocketPen_Damage" })
    private int rocketPenDamage;
    /* Gadget: GasGloss */
    @SerializedName(value = "gasGlossDamage", alternate = { "gasGloss_Damage", "GasGloss_Damage" })
    private int gasGlossDamage;
    /* Gadget: MothballPouch */
    @SerializedName(value = "mothballPouchRange", alternate = { "mothballPouch_Range", "MothballPouch_Range" })
    private int mothballPouchRange;
    @SerializedName(value = "mothballPouchDamage", alternate = { "mothballPouch_Damage", "MothballPouch_Damage" })
    private int mothballPouchDamage;
    /* Gadget: FogTin */
    @SerializedName(value = "fogTinRange", alternate = { "fogTin_Range", "FogTin_Range" })
    private int fogTinRange;
    /* Gadget: Grapple */
    @SerializedName(value = "grappleRange", alternate = { "grapple_Range", "Grapple_Range" })
    private int grappleRange;
    @SerializedName(value = "grappleHitChance", alternate = { "grapple_HitChance", "Grapple_HitChance" })
    private double grappleHitChance;
    /* Gadget: WiretapWithEarplugs */
    @SerializedName(value = "wiretapWithEarplugsFailChance", alternate = { "wiretapWithEarplugs_FailChance",
            "WiretapWithEarplugs_FailChance" })
    private double wiretapWithEarplugsFailChance;
    /* Gadget: Mirror */
    @SerializedName(value = "mirrorSwapChance", alternate = { "mirror_SwapChance", "Mirror_SwapChance" })
    private double mirrorSwapChance;
    /* Gadget: Cocktail */
    @SerializedName(value = "cocktailDodgeChance", alternate = { "cocktail_DodgeChance", "Cocktail_DodgeChance" })
    private double cocktailDodgeChance;
    @SerializedName(value = "cocktailHp", alternate = { "cocktail_Hp", "Cocktail_Hp" })
    private int cocktailHp;
    /* Aktionen */
    @SerializedName(value = "spySuccessChance", alternate = { "spy_SuccessChance", "Spy_SuccessChance" })
    private double spySuccessChance;
    @SerializedName(value = "babysitterSuccessChance", alternate = { "babysitter_SuccessChance",
            "Babysitter_SuccessChance" })
    private double babysitterSuccessChance;
    @SerializedName(value = "honeyTrapSuccessChance", alternate = { "honeyTrap_SuccessChance",
            "HoneyTrap_SuccessChance" })
    private double honeyTrapSuccessChance;
    @SerializedName(value = "observationSuccessChance", alternate = { "observation_SuccessChance",
            "Observation_SuccessChance" })
    private double observationSuccessChance;
    /* Spielfaktoren */
    @SerializedName(value = "chipsToIpFactor", alternate = { "chipsToIpFaktor" })
    private int chipsToIpFactor;
    @SerializedName("secretToIpFactor")
    private int secretToIpFactor;
    @SerializedName("minChipsRoulette")
    private int minChipsRoulette;
    @SerializedName("maxChipsRoulette")
    private int maxChipsRoulette;
    @SerializedName("roundLimit")
    private int roundLimit;
    @SerializedName("turnPhaseLimit")
    private int turnPhaseLimit;
    @SerializedName("catIp")
    private int catIp;
    @SerializedName("strikeMaximum")
    private int strikeMaximum;
    @SerializedName("pauseLimit")
    private int pauseLimit;
    @SerializedName("reconnectLimit")
    private int reconnectLimit;

    public int getMoledieRange() {
        return moledieRange;
    }

    public int getBowlerBladeRange() {
        return bowlerBladeRange;
    }

    public double getBowlerBladeHitChance() {
        return bowlerBladeHitChance;
    }

    public int getBowlerBladeDamage() {
        return bowlerBladeDamage;
    }

    public double getLaserCompactHitChance() {
        return laserCompactHitChance;
    }

    public int getRocketPenDamage() {
        return rocketPenDamage;
    }

    public int getGasGlossDamage() {
        return gasGlossDamage;
    }

    public int getMothballPouchRange() {
        return mothballPouchRange;
    }

    public int getMothballPouchDamage() {
        return mothballPouchDamage;
    }

    public int getFogTinRange() {
        return fogTinRange;
    }

    public int getGrappleRange() {
        return grappleRange;
    }

    public double getGrappleHitChance() {
        return grappleHitChance;
    }

    public double getWiretapWithEarplugsFailChance() {
        return wiretapWithEarplugsFailChance;
    }

    public double getMirrorSwapChance() {
        return mirrorSwapChance;
    }

    public double getCocktailDodgeChance() {
        return cocktailDodgeChance;
    }

    public int getCocktailHp() {
        return cocktailHp;
    }

    public double getSpySuccessChance() {
        return spySuccessChance;
    }

    public double getBabysitterSuccessChance() {
        return babysitterSuccessChance;
    }

    public double getHoneyTrapSuccessChance() {
        return honeyTrapSuccessChance;
    }

    public double getObservationSuccessChance() {
        return observationSuccessChance;
    }

    public int getChipsToIpFactor() {
        return chipsToIpFactor;
    }

    public int getRoundLimit() {
        return roundLimit;
    }

    public int getTurnPhaseLimit() {
        return turnPhaseLimit;
    }

    public int getCatIp() {
        return catIp;
    }

    public int getStrikeMaximum() {
        return strikeMaximum;
    }

    public int getPauseLimit() {
        return pauseLimit;
    }

    public int getReconnectLimit() {
        return reconnectLimit;
    }

    public void setMaxStrikes(int max) {
        this.strikeMaximum = max;
    }

    /**
     * @return the secretToIpFactor
     */
    public int getSecretToIpFactor() {
        return secretToIpFactor;
    }

    /**
     * @return the minChipsRoulette
     */
    public int getMinChipsRoulette() {
        return minChipsRoulette;
    }

    /**
     * @return the maxChipsRoulette
     */
    public int getMaxChipsRoulette() {
        return maxChipsRoulette;
    }

    /**
     * Creates a new Setting-instance, should be used for <i>Testing</i>-Purposes
     * only (or to do editorial work).
     * <p>
     * It is <b>not</b> designed to offer 'changing' capabilities
     * 
     * @param moledieRange                  range of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#MOLEDIE}-Gadget
     * @param bowlerBladeRange              range of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#BOWLER_BLADE}-Gadget
     * @param bowlerBladeHitChance          hit chance of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#BOWLER_BLADE}-Gadget
     * @param bowlerBladeDamage             damage of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#BOWLER_BLADE}-Gadget
     * @param laserCompactHitChance         hit chance of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#LASER_COMPACT}-Gadget
     * @param rocketPenDamage               damage of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#ROCKET_PEN}-Gadget
     * @param gasGlossDamage                damage of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#GAS_GLOSS}-Gadget
     * @param mothballPouchRange            range of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#MOTHBALL_POUCH}-Gadget
     * @param mothballPouchDamage           damage of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#MOTHBALL_POUCH}-Gadget
     * @param fogTinRange                   range of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#FOG_TIN}-Gadget
     * @param grappleRange                  range of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#GRAPPLE}-Gadget
     * @param grappleHitChance              hit chance of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#GRAPPLE}-Gadget
     * @param wiretapWithEarplugsFailChance hit chance of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#WIRETAP_WITH_EARPLUGS}-Gadget
     * @param mirrorSwapChance              swap chance of the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#MIRROR_OF_WILDERNESS}-Gadget
     * @param cocktailDodgeChance           dodge-chance, when attacked by the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#COCKTAIL}-Gadget
     * @param cocktailHp                    the hp linked to the
     *                                      {@link de.uulm.team020.datatypes.enumerations.GadgetEnum#COCKTAIL}-Gadget
     * @param spySuccessChance              chance, that a spy action succeeds
     * @param babysitterSuccessChance       chance, that the babysitter action
     *                                      succeeds
     * @param honeyTrapSuccessChance        chance, that the honey trap succeeds
     * @param observationSuccessChance      chance, that the observation succeeds
     * @param chipsToIpFactor               how many ip is one chip worth?
     * @param secretToIpFactor              How many ip for one secret discovery?
     * @param minChipsRoulette              Minimum chips for roulette
     * @param maxChipsRoulette              Maximum chips for roulette
     * @param roundLimit                    maximum number of rounds to be played
     *                                      (=&gt; janitor)
     * @param turnPhaseLimit                maximum number of seconds for a turn
     * @param catIp                         number of ip to be gained on pleasing
     *                                      the cat
     * @param strikeMaximum                 maximum of strikes allowed
     * @param pauseLimit                    maximum seconds of pause, -1 means
     *                                      infinite
     * @param reconnectLimit                maximum number of seconds for reconnect,
     *                                      -1 means infinite
     */
    @SuppressWarnings("java:S107")
    public Matchconfig(int moledieRange, int bowlerBladeRange, double bowlerBladeHitChance, int bowlerBladeDamage,
            double laserCompactHitChance, int rocketPenDamage, int gasGlossDamage, int mothballPouchRange,
            int mothballPouchDamage, int fogTinRange, int grappleRange, double grappleHitChance,
            double wiretapWithEarplugsFailChance, double mirrorSwapChance, double cocktailDodgeChance, int cocktailHp,
            double spySuccessChance, double babysitterSuccessChance, double honeyTrapSuccessChance,
            double observationSuccessChance, int chipsToIpFactor, int secretToIpFactor, int minChipsRoulette,
            int maxChipsRoulette, int roundLimit, int turnPhaseLimit, int catIp, int strikeMaximum, int pauseLimit,
            int reconnectLimit) {
        this.moledieRange = moledieRange;
        this.bowlerBladeRange = bowlerBladeRange;
        this.bowlerBladeHitChance = bowlerBladeHitChance;
        this.bowlerBladeDamage = bowlerBladeDamage;
        this.laserCompactHitChance = laserCompactHitChance;
        this.rocketPenDamage = rocketPenDamage;
        this.gasGlossDamage = gasGlossDamage;
        this.mothballPouchRange = mothballPouchRange;
        this.mothballPouchDamage = mothballPouchDamage;
        this.fogTinRange = fogTinRange;
        this.grappleRange = grappleRange;
        this.grappleHitChance = grappleHitChance;
        this.wiretapWithEarplugsFailChance = wiretapWithEarplugsFailChance;
        this.mirrorSwapChance = mirrorSwapChance;
        this.cocktailDodgeChance = cocktailDodgeChance;
        this.cocktailHp = cocktailHp;
        this.spySuccessChance = spySuccessChance;
        this.babysitterSuccessChance = babysitterSuccessChance;
        this.honeyTrapSuccessChance = honeyTrapSuccessChance;
        this.observationSuccessChance = observationSuccessChance;
        this.chipsToIpFactor = chipsToIpFactor;
        this.secretToIpFactor = secretToIpFactor;
        this.minChipsRoulette = minChipsRoulette;
        this.maxChipsRoulette = maxChipsRoulette;
        this.roundLimit = roundLimit;
        this.turnPhaseLimit = turnPhaseLimit;
        this.catIp = catIp;
        this.strikeMaximum = strikeMaximum;
        this.pauseLimit = pauseLimit;
        this.reconnectLimit = reconnectLimit;
    }

    /**
     * Convenient copy-constructor which performs a copy of this matchconfig
     * 
     * @param matchconfig The config you want to copy
     */
    public Matchconfig(final Matchconfig matchconfig) {
        this.moledieRange = matchconfig.moledieRange;
        this.bowlerBladeRange = matchconfig.bowlerBladeRange;
        this.bowlerBladeHitChance = matchconfig.bowlerBladeHitChance;
        this.bowlerBladeDamage = matchconfig.bowlerBladeDamage;
        this.laserCompactHitChance = matchconfig.laserCompactHitChance;
        this.rocketPenDamage = matchconfig.rocketPenDamage;
        this.gasGlossDamage = matchconfig.gasGlossDamage;
        this.mothballPouchRange = matchconfig.mothballPouchRange;
        this.mothballPouchDamage = matchconfig.mothballPouchDamage;
        this.fogTinRange = matchconfig.fogTinRange;
        this.grappleRange = matchconfig.grappleRange;
        this.grappleHitChance = matchconfig.grappleHitChance;
        this.wiretapWithEarplugsFailChance = matchconfig.wiretapWithEarplugsFailChance;
        this.mirrorSwapChance = matchconfig.mirrorSwapChance;
        this.cocktailDodgeChance = matchconfig.cocktailDodgeChance;
        this.cocktailHp = matchconfig.cocktailHp;
        this.spySuccessChance = matchconfig.spySuccessChance;
        this.babysitterSuccessChance = matchconfig.babysitterSuccessChance;
        this.honeyTrapSuccessChance = matchconfig.honeyTrapSuccessChance;
        this.observationSuccessChance = matchconfig.observationSuccessChance;
        this.chipsToIpFactor = matchconfig.chipsToIpFactor;
        this.roundLimit = matchconfig.roundLimit;
        this.turnPhaseLimit = matchconfig.turnPhaseLimit;
        this.catIp = matchconfig.catIp;
        this.strikeMaximum = matchconfig.strikeMaximum;
        this.pauseLimit = matchconfig.pauseLimit;
        this.reconnectLimit = matchconfig.reconnectLimit;
    }

    @Override
    public String toString() {
        return "Matchconfig [\n" + "  babysitterSuccessChance=" + babysitterSuccessChance + "%,\n"
                + "  bowlerBladeDamage=" + bowlerBladeDamage + ",\n" + "  bowlerBladeHitChance=" + bowlerBladeHitChance
                + "%,\n" + "  bowlerBladeRange=" + bowlerBladeRange + ",\n" + "  catIp=" + catIp + ",\n"
                + "  chipsToIpFactor=" + chipsToIpFactor + ",\n" + "  cocktailDodgeChance=" + cocktailDodgeChance
                + "%,\n" + "  cocktailHp=" + cocktailHp + ",\n" + "  fogTinRange=" + fogTinRange + ",\n"
                + "  gasGlossDamage=" + gasGlossDamage + ",\n" + "  grappleHitChance=" + grappleHitChance + "%,\n"
                + "  grappleRange=" + grappleRange + ",\n" + "  honeyTrapSuccessChance=" + honeyTrapSuccessChance
                + "%,\n" + "  laserCompactHitChance=" + laserCompactHitChance + "%,\n" + "  maxChipsRoulette="
                + maxChipsRoulette + ",\n" + "  mirrorSwapChance=" + mirrorSwapChance + "%,\n" + "  minChipsRoulette="
                + minChipsRoulette + ",\n" + "  moledieRange=" + moledieRange + ",\n" + "  mothballPouchDamage="
                + mothballPouchDamage + ",\n" + "  mothballPouchRange=" + mothballPouchRange + ",\n"
                + "  observationSuccessChance=" + observationSuccessChance + "%,\n" + "  pauseLimit=" + pauseLimit
                + "s,\n" + "  reconnectLimit=" + reconnectLimit + "s,\n" + "  rocketPenDamage=" + rocketPenDamage
                + ",\n" + "  roundLimit=" + roundLimit + ",\n" + "  secretToIpFactor=" + secretToIpFactor + ",\n"
                + "  spySuccessChance=" + spySuccessChance + "%,\n" + "  strikeMaximum=" + strikeMaximum + ",\n"
                + "  turnPhaseLimit=" + turnPhaseLimit + "s,\n" + "  wiretapWithEarplugsFailChance="
                + wiretapWithEarplugsFailChance + "%\n" + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(babysitterSuccessChance, bowlerBladeDamage, bowlerBladeHitChance, bowlerBladeRange, catIp,
                chipsToIpFactor, cocktailDodgeChance, cocktailHp, fogTinRange, gasGlossDamage, grappleHitChance,
                grappleRange, honeyTrapSuccessChance, laserCompactHitChance, maxChipsRoulette, minChipsRoulette,
                mirrorSwapChance, moledieRange, mothballPouchDamage, mothballPouchRange, observationSuccessChance,
                pauseLimit, reconnectLimit, rocketPenDamage, roundLimit, secretToIpFactor, spySuccessChance,
                strikeMaximum, turnPhaseLimit, wiretapWithEarplugsFailChance);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Matchconfig)) {
            return false;
        }
        Matchconfig other = (Matchconfig) obj;
        return Double.doubleToLongBits(babysitterSuccessChance) == Double
                .doubleToLongBits(other.babysitterSuccessChance) && bowlerBladeDamage == other.bowlerBladeDamage
                && Double.doubleToLongBits(bowlerBladeHitChance) == Double.doubleToLongBits(other.bowlerBladeHitChance)
                && bowlerBladeRange == other.bowlerBladeRange && catIp == other.catIp
                && chipsToIpFactor == other.chipsToIpFactor
                && Double.doubleToLongBits(cocktailDodgeChance) == Double.doubleToLongBits(other.cocktailDodgeChance)
                && cocktailHp == other.cocktailHp && fogTinRange == other.fogTinRange
                && gasGlossDamage == other.gasGlossDamage
                && Double.doubleToLongBits(grappleHitChance) == Double.doubleToLongBits(other.grappleHitChance)
                && grappleRange == other.grappleRange
                && Double.doubleToLongBits(honeyTrapSuccessChance) == Double
                        .doubleToLongBits(other.honeyTrapSuccessChance)
                && Double.doubleToLongBits(laserCompactHitChance) == Double
                        .doubleToLongBits(other.laserCompactHitChance)
                && maxChipsRoulette == other.maxChipsRoulette && minChipsRoulette == other.minChipsRoulette
                && Double.doubleToLongBits(mirrorSwapChance) == Double.doubleToLongBits(other.mirrorSwapChance)
                && moledieRange == other.moledieRange && mothballPouchDamage == other.mothballPouchDamage
                && mothballPouchRange == other.mothballPouchRange
                && Double.doubleToLongBits(observationSuccessChance) == Double
                        .doubleToLongBits(other.observationSuccessChance)
                && pauseLimit == other.pauseLimit && reconnectLimit == other.reconnectLimit
                && rocketPenDamage == other.rocketPenDamage && roundLimit == other.roundLimit
                && secretToIpFactor == other.secretToIpFactor
                && Double.doubleToLongBits(spySuccessChance) == Double.doubleToLongBits(other.spySuccessChance)
                && strikeMaximum == other.strikeMaximum && turnPhaseLimit == other.turnPhaseLimit
                && Double.doubleToLongBits(wiretapWithEarplugsFailChance) == Double
                        .doubleToLongBits(other.wiretapWithEarplugsFailChance);
    }

}
