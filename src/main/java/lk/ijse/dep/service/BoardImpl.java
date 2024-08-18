package lk.ijse.dep.service;

public class BoardImpl implements Board {

    private Piece[][] pieces;
    private BoardUI boardUI;

    public BoardImpl(BoardUI boardUI) {
        this.boardUI = boardUI;
        pieces = new Piece[NUM_OF_COLS][NUM_OF_ROWS];

        for (int i = 0; i < NUM_OF_COLS; i++) {
            for (int j = 0; j < NUM_OF_ROWS; j++) {
                pieces[i][j] = Piece.EMPTY;
            }
        }
    }

    @Override
    public BoardUI getBoardUI() {
        return boardUI;
    }

    @Override
    public int findNextAvailableSpot(int col) {

        int count = 0; //to count filled spots raw wisely

        for (int i=0; i< NUM_OF_ROWS; i++ ){
            if ( pieces[col][i] != Piece.EMPTY ){
                count++;
            }
        }

        if(count==NUM_OF_ROWS) {
            return -1;
        }
        return count;
    }

    @Override
    public boolean isLegalMove(int col) {

        int count = findNextAvailableSpot(col);
        return count==-1  ? false : true ;  //return false if the all raws are filled

    }

    @Override
    public boolean existLegalMoves() {

        for (int i = 0; i < NUM_OF_COLS; i++) {
            for (int j = 0; j < NUM_OF_ROWS; j++) {
                if (pieces[i][j] == Piece.EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateMove(int col, Piece move) {
        pieces[col][findNextAvailableSpot(col)] = move;
    }

   @Override
    public void updateMove(int col, int row, Piece move) {
        pieces[col][row] = move;
    }

    @Override
    public Winner findWinner() {

        Winner winner = new Winner();

        for(int i=0; i< NUM_OF_COLS;i++){
            int count1 = findNextAvailableSpot(i); //to check filled spots raw wisely

            if(count1==4 || count1==-1){
                if(pieces[i][0]==pieces[i][1] && pieces[i][1]==pieces[i][2] && pieces[i][2]==pieces[i][3]){
                    winner = new Winner(pieces[i][0], i, i, 0, 3);

                } else if ( pieces[i][1]==pieces[i][2] && pieces[i][2]==pieces[i][3] && pieces[i][3] ==pieces[i][4]) {
                    winner = new Winner(pieces[i][1], i, i, 1, 4);

                }
            }
        }

        for(int i=0; i< NUM_OF_ROWS;i++){
            int count2 = 0;  //to check filled spots column wisely

            for(int j=0; j<NUM_OF_COLS;j++){
                if(pieces[j][i]!=Piece.EMPTY){
                    count2++;
                }
            }

            if(count2>=4){
                if(pieces[0][i]==pieces[1][i] && pieces[1][i]==pieces[2][i] && pieces[2][i]==pieces[3][i]){
                    winner = new Winner(pieces[0][i], 0, 3, i, i);

                } else if (pieces[1][i]==pieces[2][i] && pieces[2][i]==pieces[3][i] && pieces[3][i]==pieces[4][i]) {
                    winner = new Winner(pieces[1][i], 1, 4, i, i);

                } else if (pieces[2][i] == pieces[3][i] && pieces[3][i] == pieces[4][i] && pieces[4][i] == pieces[5][i]) {
                    winner = new Winner(pieces[2][i], 2, 5, i, i);
                }
            }
        }

        if(winner.getWinningPiece()==Piece.EMPTY){
            winner= new Winner(Piece.EMPTY);
        }

        return winner;
    }





}