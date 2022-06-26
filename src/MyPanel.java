
/*TO DO:
 * - Start Screen
 * - modify code that skips turn if there are no possible moves
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class MyPanel extends JPanel implements KeyListener, MouseListener{
    final int WINDOW_WIDTH = 800;
    final int WINDOW_HEIGHT = 800;

    //Board parameters
    final int BOARD_WIDTH = 3*WINDOW_WIDTH/5;
    final int BOARD_HEIGHT = 3 *WINDOW_HEIGHT/5;
    final int BOARD_X = (WINDOW_WIDTH - BOARD_WIDTH)/2;
    final int BOARD_Y = (WINDOW_HEIGHT - BOARD_HEIGHT)/2;
    final int BOARD_MIDDLE = BOARD_WIDTH/15;
    final int BORDER_THICKNESS = BOARD_WIDTH/20;

    //Peg perameters
    final int TRI_WIDTH = (BOARD_WIDTH - BOARD_MIDDLE)/12;
    final int TRI_HEIGHT = BOARD_HEIGHT/2;
    final int TRI_VER_SEP = BOARD_HEIGHT/4;

    final int PIECE_DIAMETER = TRI_WIDTH * 10/12;

    //Bowl perameters
    final int BOWL_DIAMETER = 3 * BOARD_X / 5;
    final int BOWL_X = (BOARD_X - BORDER_THICKNESS/2 - BOWL_DIAMETER)/2;
    final int BOWL_Y = (WINDOW_HEIGHT - BOWL_DIAMETER)/2;

    //Dice perameters
    final int DICE_SIDE = BOWL_DIAMETER/3;
    final int DICE_SEP = DICE_SIDE/3;
    final int DICE_X = BOWL_X + (BOWL_DIAMETER - DICE_SEP)/2 - DICE_SIDE;
    final int DICE_Y = BOWL_Y + (BOWL_DIAMETER - DICE_SIDE)/2;
    final int DICE_DOT_DIAMETER = DICE_SIDE / 4;

    //gameParam holds all the parameters relating to the actual game as well as game logic
    public Game gameParam;

    private boolean player;
    public boolean change;

    MyPanel(boolean player){
        
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(new Color(40,62,0));
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
        
        change = false;
        this.player = player;

        gameParam = new Game();

    }

    public void paint (Graphics g){
        super.paint(g);//paints background
        
        
        Graphics2D g2D = (Graphics2D) g;


        //paint board background and bowl for dice
        g2D.setColor(new Color(150,60,0));
        g2D.fillRect(BOARD_X-BORDER_THICKNESS/2, BOARD_Y-BORDER_THICKNESS/2, BOARD_WIDTH+BORDER_THICKNESS, BOARD_HEIGHT+BORDER_THICKNESS);
        g2D.fillOval(BOWL_X- BORDER_THICKNESS/2, BOWL_Y- BORDER_THICKNESS/2, BOWL_DIAMETER + BORDER_THICKNESS, BOWL_DIAMETER + BORDER_THICKNESS);

        g2D.setColor(new Color(255,100,0));
        g2D.fillRect(BOARD_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT);
        g2D.fillOval(BOWL_X, BOWL_Y, BOWL_DIAMETER, BOWL_DIAMETER);
        
        //paint dice
        g2D.setColor(Color.WHITE);
        if(gameParam.double_dice){
            g2D.setColor(new Color(255,230,0));
        }
        if(gameParam.diceUsed[0]){
            g2D.setColor(Color.GRAY);
            if(gameParam.double_dice){
                g2D.setColor(Color.WHITE);
            }
        }
        g2D.fillRect(DICE_X, DICE_Y, DICE_SIDE, DICE_SIDE);

        g2D.setColor(Color.WHITE);
        if(gameParam.double_dice){
            g2D.setColor(new Color(255,230,0));
        }
        if(gameParam.diceUsed[1]){
            g2D.setColor(Color.GRAY);
            if(gameParam.double_dice){
                g2D.setColor(Color.WHITE);
            }
        }
        g2D.fillRect(DICE_X + DICE_SEP + DICE_SIDE, DICE_Y, DICE_SIDE, DICE_SIDE);

        //paint the dice faces
        g2D.setColor(Color.BLACK);
        for(int i = 0; i < 2; i ++){
            int sep = i * (DICE_SEP + DICE_SIDE);
            if(gameParam.dice[i] % 2 == 1){
                g2D.fillOval(DICE_X + (DICE_SIDE - DICE_DOT_DIAMETER)/2 + sep, DICE_Y + (DICE_SIDE - DICE_DOT_DIAMETER)/2, DICE_DOT_DIAMETER, DICE_DOT_DIAMETER);
            }
            if(gameParam.dice[i] > 1){
                g2D.fillOval(DICE_X + DICE_SIDE/6 - DICE_DOT_DIAMETER/2 + sep, DICE_Y + DICE_SIDE/6 - DICE_DOT_DIAMETER/2, DICE_DOT_DIAMETER, DICE_DOT_DIAMETER);
                g2D.fillOval(DICE_X + 5 * DICE_SIDE/6 - DICE_DOT_DIAMETER/2 + sep, DICE_Y + 5 * DICE_SIDE/6 - DICE_DOT_DIAMETER/2, DICE_DOT_DIAMETER, DICE_DOT_DIAMETER);
            }
            if(gameParam.dice[i] > 3){
                g2D.fillOval(DICE_X + 5 * DICE_SIDE/6 - DICE_DOT_DIAMETER/2 + sep, DICE_Y + DICE_SIDE/6 - DICE_DOT_DIAMETER/2, DICE_DOT_DIAMETER, DICE_DOT_DIAMETER);
                g2D.fillOval(DICE_X + DICE_SIDE/6 - DICE_DOT_DIAMETER/2 + sep, DICE_Y + 5 * DICE_SIDE/6 - DICE_DOT_DIAMETER/2, DICE_DOT_DIAMETER, DICE_DOT_DIAMETER);
            }
            if(gameParam.dice[i] == 6){
                g2D.fillOval(DICE_X + 5 * DICE_SIDE/6 - DICE_DOT_DIAMETER/2 + sep, DICE_Y + DICE_SIDE/2 - DICE_DOT_DIAMETER/2, DICE_DOT_DIAMETER, DICE_DOT_DIAMETER);
                g2D.fillOval(DICE_X + DICE_SIDE/6 - DICE_DOT_DIAMETER/2 + sep, DICE_Y + DICE_SIDE/2 - DICE_DOT_DIAMETER/2, DICE_DOT_DIAMETER, DICE_DOT_DIAMETER);
            }
        }

        //paint triangles on board 
        int[] trianglex = {BOARD_X,TRI_WIDTH + BOARD_X, TRI_WIDTH/2 + BOARD_X};
        int[] triangley = {BOARD_Y , BOARD_Y, BOARD_Y + TRI_HEIGHT - TRI_VER_SEP/2};
        Color[] tri_colors = {new Color(100,40,0), new Color(60,40,0)};

        for(int j = 0; j < 12; j ++){
            //top triangle
            g2D.setColor(tri_colors[j%2]);
            g2D.fillPolygon(trianglex, triangley, 3);

            triangley[0] += BOARD_HEIGHT;
            triangley[1] += BOARD_HEIGHT;
            triangley[2] += TRI_VER_SEP;
            
            //bottom triangle
            g2D.setColor(tri_colors[(j+1)%2]);
            g2D.fillPolygon(trianglex, triangley, 3);

            triangley[0] -= BOARD_HEIGHT;
            triangley[1] -= BOARD_HEIGHT;
            triangley[2] -= TRI_VER_SEP;

            for(int i = 0; i < 3; i ++){
                if(j == 5){
                    trianglex[i] += BOARD_MIDDLE;    
                }
                trianglex[i] += TRI_WIDTH;
            }
        }

        //draw middle part of board
        g2D.setColor(new Color(150,60,0));
        g2D.fillRect(BOARD_X + (BOARD_WIDTH - BOARD_MIDDLE/2)/2 -3, BOARD_Y, BOARD_MIDDLE/2, BOARD_HEIGHT);

        //paint player 1's bottom pieces
        g2D.setColor(Color.WHITE);
        int[] cur_location = {BOARD_X + (TRI_WIDTH - PIECE_DIAMETER)/2, BOARD_Y + BOARD_HEIGHT - PIECE_DIAMETER};
        for(int i = 0; i < gameParam.player1.length /2; i ++){
            for(int j = 0; j < gameParam.player1[i]; j ++){
                g2D.fillOval(cur_location[0], cur_location[1], PIECE_DIAMETER, PIECE_DIAMETER);
                cur_location[1] -= PIECE_DIAMETER + 1;
                if(j == 4){
                    cur_location[1] = BOARD_Y + BOARD_HEIGHT - 3*PIECE_DIAMETER/2;
                    g2D.setColor(new Color(180,180,180));
                }
            }
            g2D.setColor(Color.WHITE);
            if(i == 5){
                cur_location[0] += BOARD_MIDDLE;
            }
            
            cur_location[0] += TRI_WIDTH;
            cur_location[1] = BOARD_Y + BOARD_HEIGHT - PIECE_DIAMETER;
        }

        //paint player1's top pieces
        cur_location[0] = BOARD_X + BOARD_WIDTH + BOARD_MIDDLE - (3*TRI_WIDTH + PIECE_DIAMETER)/2;
        cur_location[1] = BOARD_Y;
        for(int i = gameParam.player1.length/2; i < gameParam.player1.length; i ++){
            for(int j = 0; j < gameParam.player1[i]; j ++){
                g2D.fillOval(cur_location[0], cur_location[1], PIECE_DIAMETER, PIECE_DIAMETER);
                cur_location[1] += PIECE_DIAMETER + 1;
                if(j == 4){
                    cur_location[1] = BOARD_Y + PIECE_DIAMETER/2;
                    g2D.setColor(new Color(180,180,180));
                }
            }
            g2D.setColor(Color.WHITE);
            if(i == 5 + gameParam.player1.length/2){
                cur_location[0] -= BOARD_MIDDLE;
            }
            
            cur_location[0] -= TRI_WIDTH;
            cur_location[1] = BOARD_Y;
        }

        g2D.setColor(Color.BLACK);

        //paint player2's top pieces
        cur_location[0] = BOARD_X + (TRI_WIDTH - PIECE_DIAMETER)/2;
        cur_location[1] = BOARD_Y;

        for(int i = 0; i < gameParam.player2.length/2; i ++){
            for(int j = 0; j < gameParam.player2[i]; j ++){
                g2D.fillOval(cur_location[0], cur_location[1], PIECE_DIAMETER, PIECE_DIAMETER);
                cur_location[1] += PIECE_DIAMETER + 1;
                if(j == 4){
                    cur_location[1] = BOARD_Y + PIECE_DIAMETER/2;
                    g2D.setColor(new Color(50,50,50));
                }
            }
            g2D.setColor(Color.BLACK);
            if(i == 5){
                cur_location[0] += BOARD_MIDDLE;
            }
            
            cur_location[0] += TRI_WIDTH;
            cur_location[1] = BOARD_Y;
        }

        //paint player2's bottom pieces
        cur_location[0] = BOARD_X + BOARD_WIDTH + BOARD_MIDDLE - (3*TRI_WIDTH + PIECE_DIAMETER)/2;
        cur_location[1] = BOARD_Y + BOARD_HEIGHT - PIECE_DIAMETER;
        
        for(int i = gameParam.player2.length/2; i < gameParam.player1.length; i ++){
            for(int j = 0; j < gameParam.player2[i]; j ++){
                g2D.fillOval(cur_location[0], cur_location[1], PIECE_DIAMETER, PIECE_DIAMETER);
                cur_location[1] -= PIECE_DIAMETER + 1;
                if(j == 4){
                    cur_location[1] = BOARD_Y + BOARD_HEIGHT - 3 *PIECE_DIAMETER/2;
                    g2D.setColor(new Color(50,50,50));
                }
            }
            g2D.setColor(Color.BLACK);
            if(i == 5 + gameParam.player2.length/2){
                cur_location[0] -= BOARD_MIDDLE;
            }
            
            cur_location[0] -= TRI_WIDTH;
            cur_location[1] = BOARD_Y + BOARD_HEIGHT - PIECE_DIAMETER;
        }

        //display which players turn it is
        String message = "Current turn:";
        g.setColor(Color.GRAY);
        int fontSize = 2 * (WINDOW_WIDTH - (BOARD_WIDTH + BOARD_X + BORDER_THICKNESS))/message.length();
        g.setFont(new Font("Calibri", Font.BOLD, fontSize));
        g.drawString(message, BOARD_X + BOARD_WIDTH + BORDER_THICKNESS, BOARD_Y + (BOARD_HEIGHT - PIECE_DIAMETER)/2);

        g2D.setColor(Color.WHITE);
        if(gameParam.turn){
            g2D.setColor(Color.BLACK);
        }
        g2D.fillOval( BOARD_X/2 + BOARD_WIDTH/2 - PIECE_DIAMETER/2 + WINDOW_WIDTH/2, BOARD_Y + (BOARD_HEIGHT - PIECE_DIAMETER + fontSize)/2, PIECE_DIAMETER, PIECE_DIAMETER);

        //paint pieces out of play
        g2D.setColor(Color.WHITE);
        for(int i = 0; i < gameParam.player1_out_of_play; i ++){
            //player1's pieces
            g2D.fillOval(BOARD_X + (i%5) * (PIECE_DIAMETER + 1), BOARD_Y + BOARD_HEIGHT + (PIECE_DIAMETER + 1)*(i/5) +1 + BORDER_THICKNESS, PIECE_DIAMETER, PIECE_DIAMETER);
        }
        g2D.setColor(Color.BLACK);
        for(int i = 0; i < gameParam.player2_out_of_play; i ++){
            //player2's pieces
            g2D.fillOval(BOARD_X + (i%5) * (PIECE_DIAMETER + 1), BOARD_Y - BORDER_THICKNESS - (i/5) * (PIECE_DIAMETER + 1) - PIECE_DIAMETER, PIECE_DIAMETER, PIECE_DIAMETER);
        }

        //paint pieces knocked out
        g2D.setColor(Color.WHITE);
        int numOfPieces = gameParam.player1_knocked + gameParam.player2_knocked;

        for(int i = 0; i < numOfPieces  ; i ++){
            if(i == gameParam.player1_knocked){
                g2D.setColor(Color.BLACK);
            }
            g2D.fillOval(BOARD_X + (BOARD_WIDTH - PIECE_DIAMETER - BOARD_MIDDLE/4)/2, BOARD_Y + (BOARD_HEIGHT - numOfPieces * (PIECE_DIAMETER + 1) )/2 + (PIECE_DIAMETER + 1) * i, PIECE_DIAMETER, PIECE_DIAMETER);
        }

        //highlight selected triangle
        if(gameParam.select){
            g2D.setColor(Color.YELLOW);
            int disp = gameParam.indexSelect > 5 && gameParam.indexSelect < 12 || gameParam.indexSelect > 17 ? BOARD_MIDDLE : 0;
            if(gameParam.indexSelect < 12){
                g2D.drawRect(gameParam.indexSelect * TRI_WIDTH + BOARD_X + disp, BOARD_Y - TRI_HEIGHT + BOARD_HEIGHT + TRI_VER_SEP/2, TRI_WIDTH, TRI_HEIGHT - TRI_VER_SEP/2);
            }
            else{
                g2D.drawRect( BOARD_WIDTH - (gameParam.indexSelect - 12) * TRI_WIDTH + BOARD_X - 2 * TRI_WIDTH + BOARD_MIDDLE - disp, BOARD_Y , TRI_WIDTH, TRI_HEIGHT - TRI_VER_SEP/2);
            }
        }
        //paint the winner if the game is over
        if(gameParam.gameOver){
            boolean tavloo = gameParam.player1_out_of_play == 0 || gameParam.player2_out_of_play == 0;
            String player = gameParam.player1_out_of_play == 15 ? "1" : "2";
            String msg = "Player " + player + " wins!";
            if(tavloo){
                msg = "TAVLOO!!! " + msg;
            }
            fontSize = 3*WINDOW_WIDTH/(msg.length() * 4); 
            g2D.setFont(new Font("Calbiri", Font.BOLD, fontSize));
            g2D.drawString(msg, (WINDOW_WIDTH  - msg.length() * fontSize/2)/2, WINDOW_HEIGHT/2);
            
        }
    }

    //gets index for player 1
    // 23 - index to get the location in perspective of other player
    private int getPlayerIndex(int x, int y){
        if( ( (x > 0 && x < (BOARD_WIDTH - BOARD_MIDDLE)/2) || (x > (BOARD_WIDTH + BOARD_MIDDLE)/2  && x < BOARD_WIDTH) ) && //Horizontal Bounds for selecting a peg
            ( (y > 0 && y < (BOARD_HEIGHT - TRI_VER_SEP)/2)  || (y > (BOARD_HEIGHT + TRI_VER_SEP)/2  && y < BOARD_HEIGHT) ) ){ //Vertical Bounds for selectring a peg
        
            switch(y/(BOARD_HEIGHT/2)){
                case 0: return x > TRI_WIDTH * 6 ? 24 - (x - BOARD_MIDDLE + TRI_WIDTH) / TRI_WIDTH  : 24 - (x + TRI_WIDTH)/TRI_WIDTH;
                case 1: return x < TRI_WIDTH * 6 ? x / TRI_WIDTH : (x - BOARD_MIDDLE)/TRI_WIDTH;
            }
        }
        return -1;
    }

    //MOUSE LISTENER

    @Override
    public void mousePressed(MouseEvent e) {
        if(player == gameParam.turn){
            //convert mouse input to location index
            int index = getPlayerIndex(e.getX() - BOARD_X, e.getY() - BOARD_Y);
            //make a move for player
            gameParam.playerClicked(index);
            change = true;
            repaint();
        }
    }

    

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_ESCAPE: System.exit(0);
            case KeyEvent.VK_S:{
                if(player == gameParam.turn){
                    gameParam.skipTurn();
                    repaint();
                }
            }
            
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
