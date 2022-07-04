import java.util.ArrayList;

public class Bot {
    final int ENEMY_KNOCKED_MOD = 5;
    final int BOT_KNOCKED_MOD = -2;
    final int OPEN_PEGS = -10;
    final int DEPTH_LIMIT = 4;
    TreeNode root;

    public Game bestMove(Game game){
        //if depth is 1, just return the current node
        //get a list of all possible moves in the current game state
        ArrayList<Game> possMoves = possibleMoves(game);
        //if no moves are found, skip your turn
        if(possMoves.size() == 0){
            game.skipTurn();
            return game;
        }

        //find the move with the highest value
        int bestIndex = 0;
        int curVal = -1000;
        for(int i = 0; i < possMoves.size(); i ++){
            eval(possMoves.get(i));
        }
        return possMoves.get(bestIndex);
    }

    private int eval(Game game){
        int score = 0;
        for(int i = 0; i < game.player2.length; i ++){

        }
        return 
    }

    private ArrayList<Game> possibleMoves(Game game){
        
        ArrayList<Game> possMoves = new ArrayList<Game>();

        for(int i = 0; i < game.player2.length; i++){

            int possIndex1 = i + game.dice[0];
            //if the peg has pieces and the first dice move from this peg is empty
            if(game.player2[i] > 0 && game.player1[23 - possIndex1] < 2){
                
                for(int j = 0; j < game.player2.length; j ++){
                    int possIndex2 = j + game.dice[1];
                    //if the peg has a piece or for the case where its the same peg it has more than 1 piece
                    if( game.player1[23 - possIndex2 ] < 2 && (game.player2[j] > 0 && i != j || game.player2[j] > 1 && i == j) ){
                        Game possGame = game.clone();
                        possGame.player2[i] -= 1;
                        possGame.player2[j] -= 1;

                        //check if the indexees selected have an enemy piece on them
                        if(possGame.player1[23 - (possIndex1) ] == 1 ){
                            possGame.player1[23- possIndex1] = 0;
                        }
                        if(possGame.player1[23 - (possIndex2) ] == 1 ){
                            possGame.player1[23- possIndex2] = 0;
                        }

                        //move pieces
                        possGame.player2[possIndex1] += 1;
                        possGame.player2[possIndex2] += 1;

                        //make it next turn
                        possGame.skipTurn();
                        
                        //add game to array
                        possMoves.add(possGame);
                    }
                }
            }
        }
        return possMoves;
    }

    private int openPegs(int[] player){
        int openPegs = 0;
        for(int peg: player){
            if(peg == 1){
                openPegs ++;
            }
        }
        return openPegs;
    }
}
