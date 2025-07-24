package com.tavloo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Game class.
 */
class GameTest {
    
    private Game game;
    
    @BeforeEach
    void setUp() {
        game = new Game();
    }
    
    @Test
    void testGameInitialization() {
        assertFalse(game.gameOver, "Game should not be over when initialized");
        assertNotNull(game.player1, "Player 1 array should be initialized");
        assertNotNull(game.player2, "Player 2 array should be initialized");
        assertEquals(24, game.player1.length, "Player 1 should have 24 positions");
        assertEquals(24, game.player2.length, "Player 2 should have 24 positions");
        assertEquals(0, game.player1_out_of_play, "Player 1 should have no pieces out of play initially");
        assertEquals(0, game.player2_out_of_play, "Player 2 should have no pieces out of play initially");
    }
    
    @Test
    void testPlayerSetup() {
        // Test initial piece setup according to the player_setup array
        assertEquals(5, game.player1[5], "Player 1 should have 5 pieces at position 5");
        assertEquals(3, game.player1[7], "Player 1 should have 3 pieces at position 7");
        assertEquals(5, game.player1[12], "Player 1 should have 5 pieces at position 12");
        assertEquals(2, game.player1[23], "Player 1 should have 2 pieces at position 23");
    }
    
    @Test
    void testDiceRoll() {
        game.roll();
        assertTrue(game.dice[0] >= 1 && game.dice[0] <= 6, "First die should be between 1 and 6");
        assertTrue(game.dice[1] >= 1 && game.dice[1] <= 6, "Second die should be between 1 and 6");
    }
    
    @Test
    void testGameClone() {
        Game clonedGame = game.clone();
        assertNotSame(game, clonedGame, "Cloned game should be a different object");
        assertEquals(game.gameOver, clonedGame.gameOver, "Game over status should be the same");
        assertEquals(game.turn, clonedGame.turn, "Turn should be the same");
        assertArrayEquals(game.player1, clonedGame.player1, "Player 1 positions should be the same");
        assertArrayEquals(game.player2, clonedGame.player2, "Player 2 positions should be the same");
    }
    
    @Test
    void testEndGame() {
        // Test endgame detection for player with pieces only in home board (positions 0-5)
        int[] homePlayer = new int[24];
        homePlayer[0] = 5;
        homePlayer[1] = 3;
        homePlayer[2] = 4;
        homePlayer[3] = 2;
        homePlayer[4] = 1;
        
        assertTrue(game.endGame(homePlayer), "Player with pieces only in home board should be in endgame");
        
        // Test with pieces outside home board
        int[] nonHomePlayer = new int[24];
        nonHomePlayer[0] = 5;
        nonHomePlayer[6] = 1; // Piece outside home board
        
        assertFalse(game.endGame(nonHomePlayer), "Player with pieces outside home board should not be in endgame");
    }
}