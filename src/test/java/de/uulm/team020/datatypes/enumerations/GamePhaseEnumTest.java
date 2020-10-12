package de.uulm.team020.datatypes.enumerations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


/**
 * Validates the Behaviour of {@link GamePhaseEnum}
 * especially {@link GamePhaseEnum#getNextClient()} and
 * {@link GamePhaseEnum#getNextServer()}.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/22/2020
 */
public class GamePhaseEnumTest {

    @Test @Tag("Util") @Order(1)
    @DisplayName("[GamePhase] Test the unfiltered next.")
    public void test_general_next() {
        GamePhaseEnum phase = GamePhaseEnum.getFirst();
        
        // pre check to avoid constant linking failure
        Assertions.assertEquals(GamePhaseEnum.INITIALIZATION, phase, "First phase should be init");

        // obtain values
        GamePhaseEnum[] values = GamePhaseEnum.values();

        for (GamePhaseEnum gamePhaseEnum : values) {
            Assertions.assertEquals(gamePhaseEnum, phase, "Phase should be as stated.");
            if(phase.equals(GamePhaseEnum.getLast()))
                break;
            phase = phase.getNext();
        }
        
        // Assert that end points to itself
        Assertions.assertEquals(GamePhaseEnum.END, phase.getNext(), "End should point to itself.");
    }

    @Test @Tag("Util") @Order(2)
    @DisplayName("[GamePhase] Test the next for client-side.")
    public void test_general_next_client() {
        GamePhaseEnum phase = GamePhaseEnum.getFirst();
        
        // pre check to avoid constant linking failure
        // we do this for better error message
        Assertions.assertEquals(GamePhaseEnum.INITIALIZATION, phase, "First phase should be init");

        // obtain values
        GamePhaseEnum[] values = new GamePhaseEnum[] {
            GamePhaseEnum.INITIALIZATION, 
            GamePhaseEnum.COLLECT_RESOURCES,
            GamePhaseEnum.INIT_NETWORK,
            GamePhaseEnum.CONNECT_TO_SERVER,
            GamePhaseEnum.WAIT_FOR_GAME_START,
            GamePhaseEnum.GAME_START,
            GamePhaseEnum.SELECT_ITEMS,
            GamePhaseEnum.EQUIP_ITEMS,
            GamePhaseEnum.MAIN_GAME_READY,
            GamePhaseEnum.MAIN_GAME_PLAY,
            GamePhaseEnum.MAIN_GAME_END,
            GamePhaseEnum.END,
        };

        for (GamePhaseEnum gamePhaseEnum : values) {
            Assertions.assertEquals(gamePhaseEnum, phase, "Phase should be as stated.");
            Assertions.assertTrue(phase.isClient(), "Phase should be a client-phase.");
            phase = phase.getNextClient();
        }

        // Assert that end points to itself
        Assertions.assertEquals(GamePhaseEnum.END, phase.getNextClient(), "End should point to itself.");
    }

    @Test @Tag("Util") @Order(3)
    @DisplayName("[GamePhase] Test the next for server-side.")
    public void test_general_next_server() {
        GamePhaseEnum phase = GamePhaseEnum.getFirst();
        
        // pre check to avoid constant linking failure
        // we do this for better error message
        Assertions.assertEquals(GamePhaseEnum.INITIALIZATION, phase, "First phase should be init");

        // obtain values
        GamePhaseEnum[] values = new GamePhaseEnum[] {
            GamePhaseEnum.INITIALIZATION, 
            GamePhaseEnum.COLLECT_RESOURCES,
            GamePhaseEnum.INIT_NETWORK,
            GamePhaseEnum.WAIT_FOR_PLAYERS,
            GamePhaseEnum.WAIT_FOR_GAME_START,
            GamePhaseEnum.GAME_START,
            GamePhaseEnum.ITEM_ASSIGNMENT,
            GamePhaseEnum.MAIN_GAME_READY,
            GamePhaseEnum.MAIN_GAME_PLAY,
            GamePhaseEnum.MAIN_GAME_END,
            GamePhaseEnum.END,
        };

        for (GamePhaseEnum gamePhaseEnum : values) {
            Assertions.assertEquals(gamePhaseEnum, phase, "Phase should be as stated.");
            Assertions.assertTrue(phase.isServer(), "Phase should be a server-phase.");
            phase = phase.getNextServer();
        }

        // Assert that end points to itself
        Assertions.assertEquals(GamePhaseEnum.END, phase.getNextServer(), "End should point to itself.");
    }
}