package com.tavloo;

/**
 * Main client entry point for the Tavloo game.
 * Creates the main game frame when executed.
 */
public class Client {
    /**
     * Main method that starts the Tavloo client application.
     * @param args Command line arguments (not used)
     * @throws Exception If an error occurs during frame creation
     */
    public static void main(String[] args) throws Exception{
        new MyFrame();
    }
}
