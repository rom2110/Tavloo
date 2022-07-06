import java.io.IOException;
import java.io.ObjectInputStream;

//This is used for player 1 when it connects and its waiting for player 2 to connect
//if the gamemode changes, it just closes the input stream
public class Listener implements Runnable {

    ObjectInputStream objIn;
    MyPanel panel;
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
