/* TO-DO:
 * - make it so that it pauses the game if it is player 1 and waiting for player 2 to connect
 * - also still need to add the main screen thing
 */

import java.io.*;
import java.net.*;

import javax.swing.*;

public class MyFrame extends JFrame{
    final String SERVER_IP = "localhost";
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

        //wait for player to make selection
        while(panel.gameMode < 0){
            System.out.println("Waiting for client to select game mode...");
        }

        //if they chose to play locally
        if(panel.gameMode == 0){
            System.out.println("LOCAL GAME SELECTED");
            while(true){
                System.out.println(panel.player);
                panel.player = panel.gameParam.turn;
            }
        }

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
            System.out.println( (String) objIn.readObject());
            panel.waiting = false;
            panel.repaint();
        }

        while(!socket.isClosed()){
            if( (!panel.gameParam.turn == panel.player && !panel.gameParam.almostnextturn) ){
                panel.gameParam.recieveGameData(objIn);
                panel.repaint();
                System.out.println("Recieved GameData");
            }
            else if( (panel.change && panel.gameParam.turn == panel.player) || (panel.gameParam.almostnextturn && (!panel.gameParam.turn == panel.player) ) ){
                //reset alost next turn and change
                panel.gameParam.almostnextturn = false;
                panel.change = false;
                //send gameParam data
                panel.gameParam.sendGameData(objOut);
                System.out.println("Sent GameData");
            }
        }

        //cleanup
        socket.close();
    }   
}