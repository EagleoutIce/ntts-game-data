package de.uulm.team020.datatypes;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.util.Point;

/**
 * This class test the basic mechanisms of the character class
 */
@Tag("Core")
public class CharacterTest {

    @Test
    @Order(1)
    @DisplayName("[Character] Test basic getter after full construction")
    public void test_getterAfterConstruction() {
        UUID characterId = UUID.randomUUID();
        List<PropertyEnum> properties = new LinkedList<>(List.of(PropertyEnum.AGILITY));
        List<Gadget> gadgets = new LinkedList<>(
                List.of(new Gadget(GadgetEnum.BOWLER_BLADE), new Gadget(GadgetEnum.NUGGET)));

        Character character = new Character(characterId, "Jens", new Point(12, 42), 2, 1, 70, 14, 13, properties,
                gadgets);

        Assertions.assertEquals(characterId, character.getCharacterId(), "Id should be as set");
        Assertions.assertEquals("Jens", character.getName(), "Name should be as set");
        Assertions.assertEquals(new Point(12, 42), character.getCoordinates(), "Pos should be as set");
        Assertions.assertEquals(2, character.getMp(), "mp should be as set");
        Assertions.assertEquals(1, character.getAp(), "ap should be as set");
        Assertions.assertEquals(70, character.getHp(), "hp should be as set");
        Assertions.assertEquals(14, character.getIp(), "ip should be as set");
        Assertions.assertEquals(13, character.getChips(), "chips should be as set");
        Assertions.assertEquals(properties, character.getProperties(), "properties should be as set");
        Assertions.assertEquals(gadgets, character.getGadgets(), "gadgets should be as set");
        Assertions.assertFalse(character.isExfiltrated(), "Should not be exfiltrated");
        Assertions.assertNull(character.getMoledieBuffer(),
                "Moledie-buffer should be null, but: " + character.getMoledieBuffer());
    }

    @Test
    @Order(2)
    @DisplayName("[Character] Test basic setter after full construction")
    public void test_setterAfterConstruction() {
        UUID characterId = UUID.randomUUID();
        List<PropertyEnum> properties = new LinkedList<>(
                List.of(PropertyEnum.FLAPS_AND_SEALS, PropertyEnum.TRADECRAFT));
        List<Gadget> gadgets = new LinkedList<>(
                List.of(new Gadget(GadgetEnum.COCKTAIL), new Gadget(GadgetEnum.NUGGET)));

        Character character = new Character(characterId, "Detlef", new Point(-12, 0), 0, 1, 120, 4, 0, properties,
                gadgets);

        // Test again
        Assertions.assertEquals(characterId, character.getCharacterId(), "Id should be as set");
        Assertions.assertEquals("Detlef", character.getName(), "Name should be as set");
        Assertions.assertEquals(new Point(-12, 0), character.getCoordinates(), "Pos should be as set");
        Assertions.assertEquals(0, character.getMp(), "mp should be as set");
        Assertions.assertEquals(1, character.getAp(), "ap should be as set");
        Assertions.assertEquals(100, character.getHp(), "hp should be 100 as it gets capped");
        Assertions.assertEquals(4, character.getIp(), "ip should be as set");
        Assertions.assertEquals(0, character.getChips(), "chips should be as set");
        Assertions.assertEquals(properties, character.getProperties(), "properties should be as set");
        Assertions.assertEquals(gadgets, character.getGadgets(), "gadgets should be as set");
        Assertions.assertFalse(character.isExfiltrated(), "Should not be exfiltrated");
        Assertions.assertNull(character.getMoledieBuffer(),
                "Moledie-buffer should be null, but: " + character.getMoledieBuffer());

        // change stuff
        character.setChips(34);
        Assertions.assertEquals(34, character.getChips(), "chips should be as changed");
        character.setAp(9);
        Assertions.assertEquals(9, character.getAp(), "ap should be as changed");
        character.setMp(-4);
        Assertions.assertEquals(-4, character.getMp(), "Mp should be as changed");
        Assertions.assertFalse(character.hasMpLeft(), "Has negative mp, this means no :P");
        character.setIp(13);
        Assertions.assertEquals(13, character.getIp(), "chips should be as changed");
        character.setHp(-42);
        Assertions.assertEquals(0, character.getHp(), "hp should be as changed, they should be capped to 0");
        character.setCoordinates(new Point(13, 3));
        Assertions.assertEquals(new Point(13, 3), character.getCoordinates(), "coordinates should be as changed");

        // removers
        character.removeAp();
        Assertions.assertEquals(8, character.getAp(), "ap should be one less after removed");
        character.removeAp(4);
        Assertions.assertEquals(4, character.getAp(), "ap should be 4 times reduced");

        character.setHp(150);
        character.removeHp(12);
        Assertions.assertEquals(88, character.getHp(), "hp should be 100 - 12");

        // add ip
        character.addIp(14);
        Assertions.assertEquals(27, character.getIp(), "Ip should be incremented by 14");

        // active mp
        character.setMp(13);
        Assertions.assertEquals(13, character.getMp(), "Mp should be as changed on 13");
        Assertions.assertTrue(character.hasMpLeft(), "Should have enough mp left");
    }

    @Test
    @Order(2)
    @DisplayName("[Character] Test basic semantic character control")
    public void test_semanticCharacterControl() {
        UUID characterId = UUID.randomUUID();
        List<PropertyEnum> properties = new LinkedList<>(
                List.of(PropertyEnum.FLAPS_AND_SEALS, PropertyEnum.TRADECRAFT));
        List<Gadget> gadgets = new LinkedList<>(
                List.of(new Gadget(GadgetEnum.COCKTAIL), new Gadget(GadgetEnum.NUGGET)));

        Character character = new Character(characterId, "Jupyter", new Point(-12, 0), 3, 1, 30, 4, 0, properties,
                gadgets);

        // Test again
        Assertions.assertEquals(characterId, character.getCharacterId(), "Id should be as set");
        Assertions.assertEquals("Jupyter", character.getName(), "Name should be as set");
        Assertions.assertEquals(30, character.getHp(), "Hp should be as set");
        Assertions.assertTrue(character.hasMpLeft(), "Should have enough mp left with 3");

        // Heal max
        character.healMax();
        Assertions.assertEquals(100, character.getHp(), "Character should be maximum healed");

        // retire
        Assertions.assertFalse(character.isRetired(), "Character should not be retired now");
        character.retire();
        Assertions.assertTrue(character.isRetired(), "Character should be retired now");
        Assertions.assertFalse(character.hasMpLeft(), "Should have no mp left, as character is retired");

        // exfiltrate
        Assertions.assertFalse(character.isExfiltrated(), "Should not be exfiltrated");
        character.exfiltrate();
        Assertions.assertTrue(character.isExfiltrated(), "Should be exfiltrated now");
        Assertions.assertEquals(1, character.getHp(), "Hp should have been locked to 1 on exfiltration");
        Assertions.assertFalse(character.hasMpLeft(),
                "Should have no mp left, as character is retired and exfiltrated");
    }

    @Test
    @Order(2)
    @DisplayName("[Character] Test basic semantic character control with exfiltration only")
    public void test_semanticCharacterControl2() {
        UUID characterId = UUID.randomUUID();
        List<PropertyEnum> properties = new LinkedList<>(
                List.of(PropertyEnum.FLAPS_AND_SEALS, PropertyEnum.TRADECRAFT));
        List<Gadget> gadgets = new LinkedList<>(
                List.of(new Gadget(GadgetEnum.COCKTAIL), new Gadget(GadgetEnum.NUGGET)));

        Character character = new Character(characterId, "Jupyter", new Point(-12, 0), 3, 1, 30, 4, 0, properties,
                gadgets);

        // Test again
        Assertions.assertEquals(characterId, character.getCharacterId(), "Id should be as set");
        Assertions.assertEquals("Jupyter", character.getName(), "Name should be as set");
        Assertions.assertEquals(30, character.getHp(), "Hp should be as set");
        Assertions.assertTrue(character.hasMpLeft(), "Should have enough mp left with 3");

        // Heal max
        character.healMax();
        Assertions.assertEquals(100, character.getHp(), "Character should be maximum healed");

        // exfiltrate
        Assertions.assertFalse(character.isExfiltrated(), "Should not be exfiltrated");
        character.exfiltrate();
        Assertions.assertTrue(character.isExfiltrated(), "Should be exfiltrated now");
        Assertions.assertEquals(1, character.getHp(), "Hp should have been locked to 1 on exfiltration");
        Assertions.assertFalse(character.hasMpLeft(), "Should have no mp left, as character is exfiltrated");
    }

    @Test
    @Order(3)
    @DisplayName("[Character] Test moledie handling")
    public void test_moledieHandling() {
        UUID characterId = UUID.randomUUID();
        List<PropertyEnum> properties = new LinkedList<>(
                List.of(PropertyEnum.JINX, PropertyEnum.TRADECRAFT, PropertyEnum.FLAPS_AND_SEALS));
        List<Gadget> gadgets = new LinkedList<>(
                List.of(new Gadget(GadgetEnum.COCKTAIL), new Gadget(GadgetEnum.NUGGET)));

        Character character = new Character(characterId, "Jenssons Jens", new Point(-12, 0), 3, 1, 30, 4, 0, properties,
                gadgets);

        // Test again
        Assertions.assertEquals(characterId, character.getCharacterId(), "Id should be as set");
        Assertions.assertEquals("Jenssons Jens", character.getName(), "Name should be as set");
        Assertions.assertEquals(30, character.getHp(), "Hp should be as set");
        Assertions.assertTrue(character.hasMpLeft(), "Should have enough mp left with 3");

        // Assure no moledie
        Assertions.assertNull(character.getMoledieBuffer(),
                "Moledie-buffer should be null, but: " + character.getMoledieBuffer());
        Assertions.assertEquals(3, character.getProperties().size(), "Three properties as no moledie");
        Assertions.assertEquals(2, character.getGadgets().size(), "No moledie"); // + moledie

        // give moledie
        character.getMoledie(new Gadget(GadgetEnum.MOLEDIE));
        Assertions.assertNotNull(character.getMoledieBuffer(), "Moledie-buffer should be active, as moledie given");
        Assertions.assertEquals(Set.of(PropertyEnum.TRADECRAFT, PropertyEnum.FLAPS_AND_SEALS),
                character.getMoledieBuffer(), "Buffered correct properties");

        Assertions.assertFalse(character.getMoledie(new Gadget(GadgetEnum.MOLEDIE)),
                "reget of a moledie should be blocked while active");

        // should have removed the properties
        Assertions.assertEquals(1, character.getProperties().size(), "Three properties as moledie");
        Assertions.assertEquals(3, character.getGadgets().size(),
                "Should have gotten moledie on option set, but: " + character.getGadgets()); // + moledie

        // take moledie
        character.removeMoledie();

        Assertions.assertNull(character.getMoledieBuffer(),
                "Moledie-buffer should be null, but: " + character.getMoledieBuffer());
        Assertions.assertEquals(3, character.getProperties().size(), "Three properties as no moledie");
        Assertions.assertEquals(2, character.getGadgets().size(), "No moledie after removal"); // + moledie

        // give moledie direct (without arg)
        character.getMoledie();
        Assertions.assertNotNull(character.getMoledieBuffer(), "Moledie-buffer should be active, as moledie given");
        Assertions.assertEquals(Set.of(PropertyEnum.TRADECRAFT, PropertyEnum.FLAPS_AND_SEALS),
                character.getMoledieBuffer(), "Buffered correct properties");

        // should have removed the properties
        Assertions.assertEquals(1, character.getProperties().size(), "Three properties as moledie");
        Assertions.assertEquals(2, character.getGadgets().size(),
                "Should have NOT gotten moledie on no option set, but: " + character.getGadgets()); // + moledie

        // no return if non given
        Assertions.assertNull(character.removeMoledie(), "Should be null as no moledie given on: " + character);
    }

    @RepeatedTest(25)
    @Order(4)
    @DisplayName("[Character] Test moledie handling")
    public void test_resetMpAp() {
        UUID characterId = UUID.randomUUID();
        List<PropertyEnum> properties = new LinkedList<>(
                List.of(PropertyEnum.JINX, PropertyEnum.TRADECRAFT, PropertyEnum.FLAPS_AND_SEALS));
        List<Gadget> gadgets = new LinkedList<>(
                List.of(new Gadget(GadgetEnum.COCKTAIL), new Gadget(GadgetEnum.NUGGET)));

        Character character = new Character(characterId, "Peter", new Point(-12, 0), 3, 1, 42, 4, 0, properties,
                gadgets);

        // Test again
        Assertions.assertEquals(characterId, character.getCharacterId(), "Id should be as set");
        Assertions.assertEquals("Peter", character.getName(), "Name should be as set");
        Assertions.assertEquals(42, character.getHp(), "Hp should be as set");
        Assertions.assertTrue(character.hasMpLeft(), "Should have enough mp left with 3");

        // simple call for reset - not exfiltrated
        Assertions.assertFalse(character.isExfiltrated(), "Should not be exfiltrated");
        character.resetMpAp();

        // should have two and one now
        Assertions.assertEquals(2, character.getMp(), "Mp should be two by reset");
        Assertions.assertEquals(1, character.getAp(), "Ap should be one by reset");

        // give him sluggishness
        character.addProperty(PropertyEnum.SLUGGISHNESS);
        character.resetMpAp();

        // should have one and one now
        Assertions.assertEquals(1, character.getMp(), "Mp should be one by reset on sluggish");
        Assertions.assertEquals(1, character.getAp(), "Ap should be one by reset");

        // swap sluggishness with nimbleness
        character.removeProperty(PropertyEnum.SLUGGISHNESS);
        character.addProperty(PropertyEnum.NIMBLENESS);
        character.resetMpAp();

        // should have three and one now
        Assertions.assertEquals(3, character.getMp(), "Mp should be three by reset on nimble");
        Assertions.assertEquals(1, character.getAp(), "Ap should be one by reset");

        // give him spryness
        character.addProperty(PropertyEnum.SPRYNESS);
        character.resetMpAp();

        // should have three and two now
        Assertions.assertEquals(3, character.getMp(), "Mp should be three by reset on nimble");
        Assertions.assertEquals(2, character.getAp(), "Ap should be two by reset on spry");

        // take nimble
        character.removeProperty(PropertyEnum.NIMBLENESS);
        character.resetMpAp();

        // should have two and two now
        Assertions.assertEquals(2, character.getMp(), "Mp should be two by reset");
        Assertions.assertEquals(2, character.getAp(), "Ap should be two by reset on spry");

        // give Agility
        character.addProperty(PropertyEnum.AGILITY);
        character.resetMpAp();

        // should have two+1 and two+1 now
        Assertions.assertTrue(2 == character.getMp() || 3 == character.getMp(),
                "Mp should be two or three by reset on agility");
        Assertions.assertTrue(2 == character.getAp() || 3 == character.getAp(),
                "Ap should be two or three by reset on agility");
        Assertions.assertNotEquals(character.getAp(), character.getMp(),
                "Character should have gotton either one ap or one mp");

        // give ponderousness
        character.addProperty(PropertyEnum.PONDEROUSNESS);
        character.resetMpAp();

        Assertions.assertTrue(1 == character.getMp() || 2 == character.getMp() || 3 == character.getMp(),
                "Mp should be one to three by reset on agility and pond");
        Assertions.assertTrue(1 == character.getAp() || 2 == character.getAp() || 3 == character.getAp(),
                "Ap should be one to three by reset on agility and pond");

        // take agility
        character.removeProperty(PropertyEnum.AGILITY);
        character.resetMpAp();

        // should have two-1 and two-1 now
        Assertions.assertTrue(1 == character.getMp() || 2 == character.getMp(),
                "Mp should be two or three by reset on pond");
        Assertions.assertTrue(1 == character.getAp() || 2 == character.getAp(),
                "Ap should be two or three by reset on pond");
        Assertions.assertNotEquals(character.getAp(), character.getMp(),
                "Character should have gotton either one ap or one mp");

        // exfiltrate
        character.exfiltrate();
        Assertions.assertTrue(character.isExfiltrated(), "Should be exfiltrated now");
        Assertions.assertEquals(1, character.getHp(), "Hp should have been locked to 1 on exfiltration");
        Assertions.assertFalse(character.hasMpLeft(), "Should have no mp left, as character is exfiltrated");
        character.setAp(-1);
        Assertions.assertEquals(-1, character.getAp(), "Ap should be set negative");
        character.setMp(-1);
        Assertions.assertEquals(-1, character.getMp(), "Mp should be set negative");
        character.resetMpAp();
        character.setAp(-1);
        Assertions.assertEquals(-1, character.getAp(),
                "Ap should be set negative even after reset as blocked (exfiltration)");
        character.setMp(-1);
        Assertions.assertEquals(-1, character.getMp(),
                "Mp should be set negative even after reset as blocked (exfiltration)");

    }

}