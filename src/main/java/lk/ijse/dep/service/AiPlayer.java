package lk.ijse.dep.service;

public class AiPlayer extends Player {
    public AiPlayer(Board newBoard) {
        super(newBoard);
    }

   /* @Override
    public void movePiece(int col) {
        int i = 0;
        do {
            i = (int) ((Math.random() * (6 - 0)) + 0);
        } while (!board.isLegalMove(i));

        col = i;
        board.updateMove(col, Piece.GREEN);
        board.getBoardUI().update(col, false);
        Winner winner = board.findWinner();

        if (winner.getWinningPiece() == Piece.GREEN) {
            board.getBoardUI().notifyWinner(winner);

        } else {
            if (!board.existLegalMoves()) {
                board.getBoardUI().notifyWinner(new Winner(Piece.EMPTY));
            }
        }
    }*/


    public void movePiece(int col) {

        boolean isUserWinning = false; //to record blue winning possibility
        int tied_column = 0 ; //to record column without any win or tied
        int winning_column = 6 ; //to record AI winning column, assigning 6 as it's out of column index

        for (int i = 0; i < board.NUM_OF_COLS; i++) {

            if (board.isLegalMove(i)) {
                int row = board.findNextAvailableSpot(i);
                //temperately assigning green ball to each column respectively
                board.updateMove(i, Piece.GREEN);

                //calling minimax to check whether assigned green bal will help to win, lose or tie
                int heuristic_val = minimax(1, false);
                //System.out.println ("final val "+heuristic_val);
                //this get heuristic values for each column

                // removing temperately assigned ball
                board.updateMove(i, row, Piece.EMPTY);

                //if the move is going to win by AI, assigning that column to winning_column or tied, assigning respective column to tied column
                if (heuristic_val == 1) {
                    winning_column = i;

                } else if (heuristic_val == -1) {
                    isUserWinning = true;

                } else {
                    tied_column = i;
                }
            }
        }

        //first checking for any AI winning move prioritizing winning move next human winner blocking move
        if(winning_column!=6){
            col=winning_column;

        //if the other moves are going to win by human, assigning the tied column to col, so it prevents the move from winning by human
        }else if (isUserWinning && board.isLegalMove(tied_column)) {
            col = tied_column;

        } else {  /*if the move doing nothing (tie), randomly select column to assign to col
                     so if there is very low or no probability to win, this will randomly select a spot rather focusing on winning move */
            
                int j;
                do {
                    j = (int) ((Math.random() * (6 - 0)) + 0);
                } while (!board.isLegalMove(j));

                col = j;
        }

        board.updateMove(col, Piece.GREEN);
        board.getBoardUI().update(col, false);

        //checking for a winner and notifying
        Winner winner = board.findWinner();
        if (winner.getWinningPiece() == Piece.GREEN) {
            board.getBoardUI().notifyWinner(winner);
        } else if (!board.existLegalMoves()) {
            board.getBoardUI().notifyWinner(new Winner(Piece.EMPTY));
        }
    }

    private int minimax(int depth, boolean maximizingPlayer) {

        //checking whether the move will give a winner
        Winner winner = board.findWinner();
        if (winner.getWinningPiece() == Piece.GREEN) {
            return 1;
        } else if (winner.getWinningPiece() == Piece.BLUE) {
            return -1;

        //return to below condition if the move doesn't give any winner or the depth between 0 and 1
        } else if (board.existLegalMoves() && depth >= 0) {

            /*after temperately assigning green then it should assign a blue ball.
            As this is not the maximizing player and the passed argument is false this will firstly go through below if condition*/
            if (!maximizingPlayer) {
                for(int i = 0; i < board.NUM_OF_COLS; i++) {

                    if (board.isLegalMove(i)) {
                        int row = board.findNextAvailableSpot(i);
                        board.updateMove(i, Piece.BLUE);

                        /*after temperately assigning a blue to first column this will again assign a green using minimax with true argument
                        and after completing the else loop this will again assign a blue to next colum , this will continue as a loop 6X6 */
                        int heuristic_val = minimax(depth-1, true);
                        //System.out.println("depth_min "+i+" "+depth);

                        //removing temperately assigned blue balls
                        board.updateMove(i, row, Piece.EMPTY);

                        //this is the minimizing player so if the heuristic val is -1 this will return its value
                        if (heuristic_val == -1) {
                            //System.out.println("val_min "+heuristic_val);
                            return heuristic_val;
                        }
                    }
                }
            } else {
                for(int i = 0; i < board.NUM_OF_COLS; i++) {

                    if (board.isLegalMove(i)) {
                        int row = board.findNextAvailableSpot(i);
                        
                        //temperately assigning green ball
                        board.updateMove(i, Piece.GREEN);
                        
                        int heuristic_val = minimax(depth - 1, false);
                        //System.out.println("depth_max "+i+" "+depth);

                        //removing temperately assigned green balls
                        board.updateMove(i, row, Piece.EMPTY);

                        /* like mentioned above this loop will continue 6 times after assigning one blue ball to each column respectively
                        as this the maximizing player this pass 1 if heuristic val gives 1*/
                        if (heuristic_val == 1) {
                            return heuristic_val;
                        }
                    }
                }
            }
            //returning 0 if the columns are tied or with no winning move
            return 0;
        } else {
            return 0;  
        }
    }
}
