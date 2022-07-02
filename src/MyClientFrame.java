/* TO-DO:
 * - make it so that it pauses the game if it is player 1
 */

import java.io.*;
import java.net.*;

import javax.swing.*;

public class MyClientFrame extends JFrame{
    final String SERVER_IP = "localhost";
    final int SERVER_PORT = 12300;

    MyClientFrame() throws Exception{
        System.out.println(InetAddress.getLocalHost());
        //connect to server
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);

        //create input and output object streams
        ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());


        //create panel with what player they are
        boolean player = (boolean) objIn.readObject();
        MyPanel panel = new MyPanel(player);
        //Setting up basic frame 

        this.setTitle("Tavloo Client: Shanteegan's Rath");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

        //recieve initial game
        panel.gameParam.recieveGameData(objIn);
        System.out.println("Recieved GameData");

        //if player1, wait for the boolean to start
        if(!player){
            System.out.println( (String) objIn.readObject());
        }

        

        while(!socket.isClosed()){
            if( (!panel.gameParam.turn == player && !panel.gameParam.almostnextturn) ){
                panel.gameParam.recieveGameData(objIn);
                panel.repaint();
                System.out.println("Recieved GameData");
            }
            else if( (panel.change && panel.gameParam.turn == player) || (panel.gameParam.almostnextturn && (!panel.gameParam.turn == player) ) ){
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
