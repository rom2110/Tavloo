

import java.io.*;
import java.net.*;

import javax.swing.*;

public class MyClientFrame extends JFrame{
    final String SERVER_IP = "192.168.56.1";
    final int SERVER_PORT = 12300;

    MyClientFrame() throws Exception{
        System.out.println(InetAddress.getLocalHost());
        //connect to server
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);

        //create input and output object streams
        ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());

        //Setting up basic frame and panel
        MyPanel panel = new MyPanel(true);
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
        while(!socket.isClosed()){
            if( (panel.change && panel.gameParam.turn) || panel.gameParam.almostnextturn){
                panel.gameParam.almostnextturn = false;
                panel.change = false;
                panel.gameParam.sendGameData(objOut);
                System.out.println("Sent GameData");
            }
            else if(!panel.gameParam.turn){
                panel.gameParam.recieveGameData(objIn);
                panel.repaint();
                System.out.println("Recieved GameData");
            }
        }

        //cleanup
        socket.close();
    }   
}
