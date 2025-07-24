package com.tavloo;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Listener thread for handling network communication while waiting for player connections.
 * Used by player 1 when waiting for player 2 to connect in online games.
 */
public class Listener implements Runnable {

    /** Input stream for receiving network data */
    ObjectInputStream objIn;
    /** Reference to the game panel for UI updates */
    MyPanel panel;
    /** Flag to control thread execution */
    boolean stop;

    Listener(ObjectInputStream objIn, MyPanel panel){
        this.objIn = objIn;
        this.panel = panel;
        this.stop = false;
    }
    public void run(){
        while(!stop){
            System.out.print("");
            if(panel.gameMode == -1){
                try {
                    objIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    
}
