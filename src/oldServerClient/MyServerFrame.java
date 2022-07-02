

import java.io.*;
import java.net.*;

import javax.swing.*;

public class MyServerFrame extends JFrame{
    //Server Parameters
    public static final int PORT = 12300;

    MyServerFrame() throws Exception{
        //Set up socket
        ServerSocket serverSocket = new ServerSocket(12300);
        Socket socket = serverSocket.accept();

        //Create object streams
        ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());

        //Game set up
        MyPanel panel = new MyPanel(false);
        this.setTitle("Tavloo Server: Shanteegan's Rath");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        
        //send the current game
        panel.gameParam.sendGameData(objOut);
        System.out.println("Sent GameData");
        
        while(!socket.isClosed()){
            //player1's turn, send out data
            if( (panel.change && !panel.gameParam.turn) || panel.gameParam.almostnextturn){
                panel.gameParam.almostnextturn = false;
                panel.change = false;
                panel.gameParam.sendGameData(objOut);
                System.out.println("Sent GameData");
            }
            else if(panel.gameParam.turn){
                panel.gameParam.recieveGameData(objIn);
                panel.repaint();
                System.out.println("Recieved GameData");
            }
        }
        System.out.println("Should not see this message");
        //cleanup
        socket.close();
        serverSocket.close();
    }
}
