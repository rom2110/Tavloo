import java.util.ArrayList;

public class Bot {
    final int ENEMY_KNOCKED_MOD = 10;
    final int BOT_KNOCKED_MOD = -1;
    final int OPEN_PEGS = -5;
    final int OUT_OF_PLAY = 4;
    final int DEPTH_LIMIT = 4;

    private void printArray(int[] player2, String message){
        System.out.println(message);
        for(int element : player2){
            System.out.print(element + ", ");
        }
        System.out.print("\n");
    }
    

    public Game bestMove(Game game){

        //get possible moves with one die
        ArrayList<Game> onePossMoves1 = possibleMoves(game.dice[0], game);
        ArrayList<Game> onePossMoves2 = possibleMoves(game.dice[1], game);
        
        //get the two dice possible moves
        ArrayList<Game> twoPossMoves = new ArrayList<Game>();
        for(int i = 0; i < onePossMoves1.size(); i ++){
            twoPossMoves.addAll(possibleMoves(game.dice[1], onePossMoves1.get(i)));
        }
        for(int i = 0; i < onePossMoves2.size(); i ++){
            twoPossMoves.addAll(possibleMoves(game.dice[0], onePossMoves2.get(i)));
        }

        ArrayList<Game> threePossMoves = new ArrayList<Game>();
        ArrayList<Game> fourPossMoves = new ArrayList<Game>();

        if(game.double_dice){
            for(int i = 0; i < twoPossMoves.size(); i ++){
                threePossMoves.addAll(possibleMoves(game.dice[0], twoPossMoves.get(i)));
            }
            for(int i = 0; i < threePossMoves.size(); i ++){
                fourPossMoves.addAll(possibleMoves(game.dice[0], threePossMoves.get(i)));
            }
        }

        //decide whether to look at the one dice moves, two dice moves, or three dice moves
        ArrayList<Game> possibleMoves = new ArrayList<Game>();
        if(fourPossMoves.size() > 0){
            possibleMoves.addAll(fourPossMoves);
        }
        else if(threePossMoves.size() > 0){
            possibleMoves.addAll(threePossMoves);
        }
        else if(twoPossMoves.size() > 0){
            possibleMoves.addAll(twoPossMoves);
        }
        else if(onePossMoves1.size() > 0 || onePossMoves2.size() > 0){
            possibleMoves.addAll(onePossMoves1);
            possibleMoves.addAll(onePossMoves2);
        }
        
        //initialize to current game in case there are no possible moves
        Game bestMove = game;

        //evaluate the best move from the list
        if(possibleMoves.size() > 0){
            bestMove = possibleMoves.get(0);
            int curVal = eval(bestMove);
            for(Game possibleMove : possibleMoves){
                int possVal = eval(possibleMove);
                if(possVal > curVal){
                    curVal = possVal;
                    bestMove = possibleMove;
                }
            }
        }

        //see how the game changed
        printArray(game.player2, "Preveous Game: ");
        printArray(bestMove.player2 , "New Game: ");
        int[] diff = new int[game.player2.length];
        for(int i = 0; i < game.player2.length; i ++){
            //negative will be pieces moved, positive will be pieces placed
            diff[i] = bestMove.player2[i] - game.player2[i];
        }
        printArray(diff, "Difference: ");

        bestMove.skipTurn();
        return bestMove;
    }

    private int eval(Game game){

        //check for open pegs
        int openPegs = 0;
        for(int i = 0; i < game.player2.length; i ++){
            if(game.player2[i] == 1){
                openPegs += 1;
            }
        }
        //add all the values and give a score
        return OPEN_PEGS * openPegs + BOT_KNOCKED_MOD * game.player2_knocked + ENEMY_KNOCKED_MOD * game.player1_knocked + OUT_OF_PLAY * game.player2_out_of_play;
    }

    private ArrayList<Game> possibleMoves(int dice, Game game){
        ArrayList<Game> possMoves = new ArrayList<Game>();
        //if regular move possible
        if(game.player2_knocked == 0){
            //only check indicies in the range of possible moves
            for(int i = dice; i < game.player2.length; i++){
                int possIndex = i - dice;
                if(game.player2[i] > 0 && game.player1[23 - possIndex] < 2 ){
                    Game possGame = game.clone();

                    //check if the bot hit the opponenet
                    if(possGame.player1[23 - possIndex] == 1){
                        possGame.player1[23 - possIndex] = 0;
                        possGame.player1_knocked += 1;
                    }

                    possGame.player2[i] -= 1;
                    possGame.player2[possIndex] += 1;
                    possMoves.add(possGame);

                }
            }
            //check for piece that could be removed if in endgame
            if(game.endGame(game.player2)){
                
                int farthPeg = farthestPeg(game.player2);

                if(game.player2[dice - 1] > 0){
                    Game possGame = game.clone();
                    possGame.player2[dice-1] -= 1;
                    possGame.player2_out_of_play += 1;
                    possMoves.add(possGame);
                }
                else if(farthPeg < dice-1){
                    Game possGame = game.clone();
                    possGame.player2[farthPeg] -= 1;
                    possGame.player2_out_of_play += 1;
                    possMoves.add(possGame);
                }
            }
        }
        //if they have knocked pieces
        else if(game.player2_knocked > 0 && game.player1[dice - 1] < 2){
            //create clone
            Game possGame = game.clone();

            //modify number of knocked pieces
            possGame.player2_knocked -= 1;

            //remove piece if there is an enemy there
            if(game.player1[dice - 1] == 1){
                game.player1_knocked += 1;
                game.player1[dice - 1] = 0;
            }

            //place piece
            possGame.player2[24 - dice] += 1;
            possMoves.add(possGame);
        }
        return possMoves;
    }

    //returns index of farthest peg containing a piece in end zone
    private int farthestPeg(int[] player) {
        int curPeg = -1;
        for(int i = 0; i < 6; i ++){
            if(player[i] > 0){
                curPeg = i;
            }
        }
        return curPeg;
    }


    private ArrayList<Game> oldPossibleMoves(Game game){
        
        ArrayList<Game> possMoves = new ArrayList<Game>();

        
        //only select indecies in range of the possible moves
        for(int i = game.dice[0] ; i < game.player2.length; i ++){

            int possIndex1 = i - game.dice[0];
            //if the peg has pieces and the first dice move from this peg is empty
            if(game.player2[i] > 0 && game.player1[23 - possIndex1] < 2){
                
                //only select indecies in range of the possible moves
                for(int j = game.dice[1]; j < game.player2.length ; j ++){
                    int possIndex2 = j - game.dice[1];
                    //if the peg has a piece or for the case where its the same peg it has more than 1 piece
                    if(game.player1[23 - possIndex2 ] < 2 && (game.player2[j] > 0 && i != j || game.player2[j] > 1 && i == j) ){
                        //create a copy of the game
                        Game possGame = game.clone();

                        //move pieces
                        possGame.player2[possIndex1] += 1;
                        possGame.player2[possIndex2] += 1;

                        possGame.player2[i] -= 1;
                        possGame.player2[j] -= 1;

                        //check if the indexees selected have an enemy piece on them
                        if(possGame.player1[23 - (possIndex1) ] == 1 ){
                            possGame.player1[23- possIndex1] = 0;
                            possGame.player1_knocked += 1;
                        }
                        if(possGame.player1[23 - (possIndex2) ] == 1 ){
                            possGame.player1[23- possIndex2] = 0;
                            possGame.player1_knocked += 1;
                        }
                        
                        //add game to array
                        possMoves.add(possGame);
                    }
                }
            }
        }
        return possMoves;
    }
}
