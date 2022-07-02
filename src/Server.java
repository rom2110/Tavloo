import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int PORT = 12300;
    public static void main(String[] args) throws Exception{
        //ServerFrame was for peer to peer connection
        //new MyServerFrame();
        Game game = new Game();
        
        ServerSocket serverSocket = new ServerSocket(PORT);

        //connect first player
        Socket socket1 = serverSocket.accept();

        //Create object streams for player 1
        ObjectOutputStream objOut1 = new ObjectOutputStream(socket1.getOutputStream());
        ObjectInputStream objIn1 = new ObjectInputStream(socket1.getInputStream());

        
        //send the current game and tell the client theyre player 1
        objOut1.writeObject(false);
        game.sendGameData(objOut1);
        System.out.println("Player 1 Connected");

        //wait for player 2 to connect
        Socket socket2 = serverSocket.accept();

        //Create object streams
        ObjectOutputStream objOut2 = new ObjectOutputStream(socket2.getOutputStream());
        ObjectInputStream objIn2 = new ObjectInputStream(socket2.getInputStream());

        //send the current game and let them know they are player 2
        objOut2.writeObject(true);
        game.sendGameData(objOut2);
        System.out.println("Player 2 Connected");

        //let player 1 know someone connected
        objOut1.writeObject("SERVER: Player 2 Connected");

        //let player 1 know player 2 connected
        while(!socket1.isClosed() && !socket2.isClosed()){
            //player 2's turn
            if(game.turn){
                System.out.println("Sending data to player 1");
                
                game.recieveGameData(objIn2);
                System.out.println(game.turn);
                game.sendGameData(objOut1);
            }
            //player 1's turn
            else{
                System.out.println("Sending data to player 2");
                
                game.recieveGameData(objIn1);
                System.out.println(game.turn);
                game.sendGameData(objOut2);
            }
        }
        socket2.close();
        socket1.close();
        serverSocket.close();
        
    }
}
