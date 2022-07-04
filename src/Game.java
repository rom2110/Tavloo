

import java.io.*;
import java.util.Random;

public class Game implements Serializable{
    //player startup pieces
    final int[] player_setup = {0,0,0,0,0,5,0,3,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,2};

    //Used for testing endgame
    //final int[] player_setup = {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    //game over
    public boolean gameOver;

    //player 1 parameters
    public int[] player1;
    public int player1_out_of_play;
    public int player1_knocked;

    //player 2 parameters
    public int[] player2;
    public int player2_out_of_play;
    public int player2_knocked;

    //false == player 1, true == player 2
    public boolean turn;
    
    //dice parameters
    public int[] dice = {0,0};
    public boolean[] diceUsed = {false, false};
    public boolean double_dice;

    //User Interface to select pegs
    //false == unselected, true == selected
    public boolean select;
    public int indexSelect;

    public boolean almostnextturn;

    public Random rand;
    
    Game(){
        gameOver = false;
        rand = new Random();

        player1 = player_setup.clone();
        player1_out_of_play = 0;
        player1_knocked = 0;

        player2 = player_setup.clone();
        player2_out_of_play = 0;
        player2_knocked = 0;

        turn = false;

        roll();

        diceUsed[0] = false;
        diceUsed[1] = false;

        double_dice = dice[0] == dice[1];

        select = false;
        indexSelect = 0;

        almostnextturn = false;
    }

    //clones current instance of the game
    public Game clone(){
        //create knew Game;
        Game cloneGame = new Game();
        //copy game over boolean
        cloneGame.gameOver = gameOver;
        //copy player data
        for(int i = 0; i < player1.length; i++){
            cloneGame.player1[i] = player1[i];
            cloneGame.player1[i] = player1[i];
        }
        cloneGame.player1_out_of_play = player1_out_of_play;
        cloneGame.player1_knocked = player1_knocked;
        cloneGame.player2_out_of_play = player2_out_of_play;
        cloneGame.player2_knocked = player2_knocked;
        
        cloneGame.turn = turn;
        for(int i = 0; i < 2; i ++ ){
            cloneGame.dice[i] = dice[i];
            cloneGame.diceUsed[i] = diceUsed[i];
        }
        cloneGame.double_dice = double_dice;
        cloneGame.rand = rand;
        
        return cloneGame;
    }

    public void sendGameData(ObjectOutputStream objOut) throws Exception{
        objOut.writeObject(gameOver);

        //player 1 parameters
        for(int i : player1){
            objOut.writeObject(i);
        }
        objOut.writeObject(player1_out_of_play);
        objOut.writeObject( player1_knocked);

        //player 2 parameters
        for(int i : player2){
            objOut.writeObject(i);
        }
        objOut.writeObject(player2_out_of_play);
        objOut.writeObject(player2_knocked);

        //false == player 1, true == player 2
        objOut.writeObject(turn);
        
        //dice parameters
        for(int i: dice){
            objOut.writeObject(i);
        }
        for(boolean i: diceUsed){
            objOut.writeObject(i);
        }
        objOut.writeObject(double_dice);

        //User Interface to select pegs
        //false == unselected, true == selected
        objOut.writeObject(select);
        objOut.writeObject(indexSelect);
    }
    public void recieveGameData(ObjectInputStream objIn) throws Exception{
        gameOver = (boolean) objIn.readObject();

        //player 1 parameters
        for(int i = 0; i< player1.length; i ++){
            player1[i] = (int) objIn.readObject();
        }
        player1_out_of_play = (int) objIn.readObject();
        player1_knocked = (int) objIn.readObject();

        //player 2 parameters
        for(int i = 0; i< player2.length; i ++){
            player2[i] = (int) objIn.readObject();
        }
        player2_out_of_play = (int) objIn.readObject();
        player2_knocked = (int) objIn.readObject();

        //false == player 1, true == player 2
        turn = (boolean) objIn.readObject();
        
        //dice parameters
        for(int i = 0; i < dice.length; i ++){
            dice[i] = (int) objIn.readObject();
        }
        for(int i = 0; i < diceUsed.length; i ++){
            diceUsed[i] = (boolean) objIn.readObject();
        }
        double_dice = (boolean) objIn.readObject();

        //User Interface to select pegs
        //false == unselected, true == selected
        select = (boolean) objIn.readObject();
        indexSelect = (int) objIn.readObject();
    }

    public void roll(){
        dice[0] = rand.nextInt(6) + 1;
        dice[1] = rand.nextInt(6) + 1;
    }
    public void checkNextTurn(){
        if(diceUsed[0] && diceUsed[1]){
            if(!double_dice){
                almostnextturn = true;
                turn = !turn;
                roll();
                if(dice[0] == dice[1]){
                    double_dice = true;
                }
            }
            else{
                double_dice = false;
            }
            diceUsed[0] = false;
            diceUsed[1] = false;
            //select = false;
            //skip a die roll if its not possible to play
            if(player1_knocked > 1 && !turn){
                diceUsed[0] = player2[dice[0]-1] > 1;
                diceUsed[1] = player2[dice[1]-1] > 1;
                checkNextTurn();
            }
            else if(player1_knocked == 1 && player2[dice[0]+1] > 1 && player2[dice[1] + 1] > 1 ){
                diceUsed[0] = false;
                diceUsed[1] = false;
                checkNextTurn();
            }
            //skip a die roll if its not possible to plays
            if(player2_knocked > 1 && turn){
                diceUsed[0] = player1[dice[0]-1] > 1;
                diceUsed[1] = player1[dice[1]-1] > 1;
                checkNextTurn();
            }
        }
    }

    public void playerClicked(int index){
        if(!gameOver && (index > -1 || (endGame(player1) && !turn) || (endGame(player2) && turn) )){
            //player2's turn:
            if(turn){
                //if player has knocked pieces
                if(player2_knocked > 0){
                    int pegSelected = index + 1;
                    if(player1[pegSelected-1] < 2){
                        //check if you can use die 1
                        if(!diceUsed[0] && pegSelected == dice[0]){
                            //knock enemy if it hits
                            if(player1[index] == 1){
                                player1[index] = 0;
                                player1_knocked += 1;
                            }
                            player2[23 - index] += 1;
                            player2_knocked -= 1;
                            diceUsed[0] = true;
                        }
                        //check if you can use die 2
                        else if(!diceUsed[1] && pegSelected == dice[1]){
                            //knock enemy if it hits
                            if(player1[index] == 1){
                                player1[index] = 0;
                                player1_knocked += 1;
                            }
                            player2[23 - index] += 1;
                            player2_knocked -= 1;
                            diceUsed[1] = true;
                        }
                        //next turn if both dice used
                        checkNextTurn();
                    }
                }
                //if they've selected a piece to move from
                else if(select){
                    if(index == indexSelect){
                        select = false;
                    }
                    else if(index == -1){
                        if(!diceUsed[0] && ( 24 - indexSelect == dice[0] || 24 - indexSelect < dice[0] && farthestPeg(23 - indexSelect, player2)) ){
                            diceUsed[0] = true;
                            player2[23 - indexSelect] -= 1;
                            player2_out_of_play += 1;
                            select = false;
                        }
                        else if(!diceUsed[1] && ( 24 - indexSelect == dice[1] || 24 - indexSelect < dice[1] && farthestPeg(23 - indexSelect, player2)) ){
                            diceUsed[1] = true;
                            player2[23 - indexSelect] -= 1;
                            player2_out_of_play += 1;
                            select = false;
                        }
                        
                        checkNextTurn();
                    }
                    else{
                        int movePieceBy = index - indexSelect;
                        //Check if the enemy is stacked on the peg
                        if(player1[index] < 2){
                            //use die 1
                            if(!diceUsed[0] && movePieceBy == dice[0]){
                                //knock out enemy player if there is only one
                                if(player1[index] == 1){
                                    player1[index] = 0;
                                    player1_knocked += 1;
                                }
                                diceUsed[0] = true;
                                player2[23 - indexSelect] -= 1;
                                player2[23 - index] += 1;
                                select = false;
                            }
                            //use die 2
                            else if(!diceUsed[1] && movePieceBy == dice[1]){
                                //knock out enemy player if there is only one
                                if(player1[index] == 1){
                                    player1[index] = 0;
                                    player1_knocked += 1;
                                }
                                diceUsed[1] = true;
                                player2[23 - indexSelect] -= 1;
                                player2[23 - index] += 1;
                                select = false;
                            }
                        }
                        checkNextTurn();
                    }
                }
                //if they havent selected a piece yet and it has at least one piece on it
                else if(index > -1 && player2[23 - index] > 0){
                    indexSelect = index;
                    select = true;
                }
            }



            //player 1's turn:
            else{
                //if player has knocked pieces
                if(player1_knocked > 0){
                    int pegSelected = 24 - index;
                    if(player2[pegSelected-1] < 2){
                        //check if you can use die 1
                        if(!diceUsed[0] && pegSelected == dice[0]){
                            //knock enemy if it hits
                            if(player2[23-index] == 1){
                                player2[23-index] = 0;
                                player2_knocked += 1;
                            }
                            player1[index] += 1;
                            player1_knocked -= 1;
                            diceUsed[0] = true;
                        }
                        //check if you can use die 2
                        else if(!diceUsed[1] && pegSelected == dice[1]){
                            //knock enemy if it hits
                            if(player2[23 - index] == 1){
                                player2[23 - index] = 0;
                                player2_knocked += 1;
                            }
                            player1[index] += 1;
                            player1_knocked -= 1;
                            diceUsed[1] = true;
                        }
                        //next turn if both dice used
                        checkNextTurn();
                    }
                }
                else if(select){
                    if(index == indexSelect){
                        select = false;
                    }
                    //endgame
                    else if(index == -1){
                        //check if you can use the first dice
                        //make sure dice is not used    and if so, remove if it's a match or if it is the farthest possible peg
                        if(!diceUsed[0] && ( indexSelect == dice[0] - 1 || indexSelect < dice[0] - 1 && farthestPeg(indexSelect, player1) )){
                            diceUsed[0] = true;
                            player1[indexSelect] -= 1;
                            player1_out_of_play += 1;
                            select = false;
                            
                        }
                        else if(!diceUsed[1] && ( indexSelect == dice[1] - 1 || indexSelect < dice[1] - 1 && farthestPeg(indexSelect, player1) )){
                            diceUsed[1] = true;
                            player1[indexSelect] -= 1;
                            player1_out_of_play += 1;
                            select = false;
                        }
                        checkNextTurn();
                    }
                    else{
                        int movePieceBy = indexSelect - index;
                        //check if the enemy is not stacked on the peg
                        if(player2[23 - index] < 2){
                            if(!diceUsed[0] && movePieceBy == dice[0]){
                                //knock out the enemy piece if there is only one
                                if(player2[23-index] == 1){
                                    player2[23 - index] = 0;
                                    player2_knocked += 1;
                                }
                                diceUsed[0] = true;
                                player1[indexSelect] -= 1;
                                player1[index] += 1;
                                select = false;
                            }
                            else if(!diceUsed[1] && movePieceBy == dice[1]){
                                //knock out the enemy piece if there is only one
                                if(player2[23-index] == 1){
                                    player2[23 - index] = 0;
                                    player2_knocked += 1;
                                }
                                diceUsed[1] = true;
                                player1[indexSelect] -= 1;
                                player1[index] += 1;
                                select = false;
                            }
                        }
                        checkNextTurn();
                    }
                }
                //if they havent selected a piece yet and it has at least one piece on it
                else if(index > -1 && player1[index] > 0){
                    indexSelect = index;
                    select = true;
                }
            }
        }
        //check if game is over
        if(player1_out_of_play == 15 || player2_out_of_play == 15){
            diceUsed[0] = false;
            diceUsed[1] = false;
            gameOver = true;
        }
    }

    //when in endgame, verifies that the peg selected is the peg farthest from the end
    private boolean farthestPeg(int index, int[] player){
        for(int i = index + 1; i < 6; i ++){
            if(player[i] > 0){
                return false;
            }
        }
        return true;
    }

    //checks if the current player is in the end game or not
    private boolean endGame(int[] player) {
        for(int i = 6; i < 24; i ++){
            if(player[i] > 0){
                return false;
            }
        }
        return true;
    }

    //skips the turn if called
    public void skipTurn(){
        select = false;
        diceUsed[0] = true;
        diceUsed[1] = true;
        double_dice = false;
        checkNextTurn();
    }
}
