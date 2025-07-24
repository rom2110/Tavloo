package com.tavloo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class for hosting Tavloo multiplayer games.
 * Handles client connections and manages game state synchronization between players.
 */
public class Server implements Runnable{
    /** Default port number for the server */
    static final int PORT = 12300;
    
    private Socket socket1;

    private Socket socket2;

    private ObjectOutputStream objOut1;
    private ObjectOutputStream objOut2;

    private Game game;
    
    /**
     * Creates a new Server instance with the specified connections and game.
     * @param socket1 Socket connection for player 1
     * @param socket2 Socket connection for player 2  
     * @param objOut1 Output stream for player 1
     * @param objOut2 Output stream for player 2
     * @param game The game instance to manage
     */
    Server(Socket socket1, Socket socket2,ObjectOutputStream objOut1,ObjectOutputStream objOut2, Game game){
        this.game = game;
        this.socket1 = socket1;
        this.socket2 = socket2;
        this.objOut1 = objOut1;
        this.objOut2 = objOut2;
    }
    /**
     * Main method that starts the Tavloo server.
     * Listens for client connections and creates game instances.
     * @param args Command line arguments (not used)
     * @throws Exception If an error occurs during server startup
     */
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(PORT);
        while(true){
            Game game = new Game();

            //connect first player
            Socket socket1 = serverSocket.accept();

            //Create object streams for player 1
            ObjectOutputStream objOut1 = new ObjectOutputStream(socket1.getOutputStream());

            
            //send the current game and tell the client theyre player 1
            objOut1.writeObject(false);
            game.sendGameData(objOut1);
            System.out.println("Player 1 Connected");

            Socket socket2 = new Socket();
            try{
                //wait for player 2 to connect
                socket2.close();
                socket2 = serverSocket.accept();

                //Create object streams
                ObjectOutputStream objOut2 = new ObjectOutputStream(socket2.getOutputStream());

                //send the current game and let them know they are player 2
                objOut2.writeObject(true);
                game.sendGameData(objOut2);
                System.out.println("Player 2 Connected");

                //let player 1 know someone connected
                objOut1.writeObject("SERVER: Player 2 Connected");

                //Set up thread
                Server server = new Server(socket1, socket2, objOut1, objOut2, game);
                Thread thread = new Thread(server);

                //start thread
                thread.start();
                
            } catch(Exception e){
                System.out.println("Error Occured with connecting to socket, waiting for player 1 again...");
                socket2.close();
            }
        }
    }
    @Override
    public void run() {
        
        try {
            ObjectInputStream objIn1 = new ObjectInputStream(socket1.getInputStream());
            ObjectInputStream objIn2 = new ObjectInputStream(socket2.getInputStream());

            while( !socket1.isClosed() && !socket2.isClosed()){
                //player 2's turn
                if(game.turn){
                    System.out.println("Sending data to player 1");
                    try{
                        game.recieveGameData(objIn2);
                        game.sendGameData(objOut1);
                    } catch(Exception e){
                        System.out.println("Failed sending data to player 1... \nStopping thread...");
                        break;
                    }
                    
                }
                //player 1's turn
                else{
                    System.out.println("Sending data to player 2");
                    try{
                        game.recieveGameData(objIn1);
                        game.sendGameData(objOut2);
                    } catch(Exception e){
                        System.out.println("Failed sending data to player 2... \nStopping thread...");
                        break;
                    }
                }
            }
            socket1.close();
            socket2.close();
            
        } catch (Exception e1) {
            System.out.println("Someone disconnected... \nStopping thread...");
            try {
                socket1.close();
            } catch (IOException e) {
            }
            try {
                socket2.close();
            } catch (IOException e) {
            }
        }
    }
}
