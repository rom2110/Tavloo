package com.tavloo;

/* TO-DO:
 * - create the bot using minimax tree search methods
 * - add feature that makes player have to click dice to roll for their turn
 * - Make first move decide which person (or bot) is which player
 */

import java.io.*;
import java.net.*;

import javax.swing.*;

/**
 * Main game window frame that handles different game modes and network connections.
 * Supports local play, online multiplayer, and bot games.
 */
public class MyFrame extends JFrame{
    /** Server IP address for online games */
    final String SERVER_IP = "server.onnik.com";
    /** Server port number for online games */
    final int SERVER_PORT = 12300;

    MyFrame() throws Exception{
        
        //create panel with what player they are
        MyPanel panel = new MyPanel();

        //Setting up basic frame 
        this.setTitle("Tavloo: Shanteegan's Rath");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        //loop the options for when they want to leave and select a different mode
        while(true){
            //wait for player to make selection
            while(panel.gameMode < 0){
                System.out.println("Waiting for client to select game mode...");
            }

            //if they chose to play locally
            if(panel.gameMode == 0){
                System.out.println("LOCAL GAME SELECTED");
                while(panel.gameMode == 0){
                    System.out.println(panel.player);
                    panel.player = panel.gameParam.turn;
                }
            }
            //if they chose to play the bot
            else if(panel.gameMode == 2){
                Bot bot = new Bot();
                //set the client as player 1
                panel.setPlayer(false);
                //skip waiting
                panel.waiting = false;
                panel.repaint();
                System.out.println("MATCH WITH SHANTEEGAN CHOSEN");

                while(panel.gameMode == 2){
                    System.out.print("");
                    //if bots turn
                    if(panel.gameParam.turn){
                        System.out.println("BOTS TURN");
                        System.out.println(panel.gameParam.dice[0]);
                        System.out.println(panel.gameParam.dice[1]);
                        
                        //have the bot figure out the best move
                        panel.gameParam = bot.bestMove(panel.gameParam);
                        panel.repaint();
                    }
                    //else just let the panel handel it
                }
                //if it breakes out of the while loop, the user wants to go back to the main menue
            }

            //if they choose to verse somone else online
            try{
                if(panel.gameMode == 1){
                    System.out.println("ONLINE GAME SELECTED");
    
                    //if they chose online, open a socket connection to the server
    
                    //connect to server if they click on start
                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);
    
                    //create input and output object streams
                    ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
    
                    //read in boolean to see what player they are
                    panel.setPlayer((boolean) objIn.readObject());
    
                    //recieve initial game
                    panel.gameParam.recieveGameData(objIn);
                    System.out.println("Recieved GameData");
                    //repaint newly added parameters
                    panel.repaint();

                    //if player1, wait for the boolean to start
                    if(!panel.player){
                        //open a thread to see if the gameMode changes while listening
                        Listener listener = new Listener(objIn, panel);
                        Thread listThread = new Thread(listener);
                        listThread.start();

                        try{
                            System.out.println( (String) objIn.readObject());
                        } catch(Exception e){
                            System.out.println("Client left game");
                            socket.close();
                        }

                        //stop listener for if client wants to leave and stop waiting
                        listener.stop = true;
                        panel.waiting = false;
                        panel.repaint();
                    }
    
                    while(panel.gameMode == 1){
                        //opponent turn
                        if( (!panel.gameParam.turn == panel.player && !panel.gameParam.almostnextturn) ){
                            System.out.println("Recieving GameData");
                            panel.gameParam.recieveGameData(objIn);
                            panel.repaint();
                        }
                        //your turn
                        else if( (panel.change && panel.gameParam.turn == panel.player) || (panel.gameParam.almostnextturn && (!panel.gameParam.turn == panel.player) ) ){
                            System.out.println("Sending GameData");
                            
                            //reset alost next turn and change
                            panel.gameParam.almostnextturn = false;
                            panel.change = false;
                            //send gameParam data
                            panel.gameParam.sendGameData(objOut);
                        }
                        else{
                            System.out.print("");
                        }
                    }
                    //cleanup
                    socket.close();
                }
            }catch(Exception e){
                System.out.println("Connection closed \nSent back to Game Mode select");
                panel.gameMode = -1;
                panel.repaint();
            }
        }
    }   
}
