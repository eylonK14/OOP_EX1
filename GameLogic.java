import java.util.*;


public class GameLogic implements PlayableLogic {
    ArrayList<ConcretePiece[][]> historyBoard = new ArrayList<>();
    ArrayList<Position[][]> historyboardpositions = new ArrayList<>();
    ArrayList<ConcretePiece> deadPiece = new ArrayList<>();
    private Position[][] boardpositions = new Position[Board_size][Board_size];
    private ConcretePiece[][] board = new ConcretePiece[Board_size][Board_size];
    private ConcretePlayer defender = new ConcretePlayer(true);
    private ConcretePlayer attacker = new ConcretePlayer(false); //
    private Position kingsPos = new Position(5, 5); // new King(defender ,new Position(5, 5));
    private final Position[] kingsArea = {new Position(0, 0), new Position(10, 0), new Position(0, 10), new Position(10, 10)};
    private static final int Board_size = 11;
    private static boolean winner;
    private boolean currPlayer = false;

    public GameLogic() {
        reset();
    }

    @Override
    public boolean move(Position a, Position b) {
        if ((getPieceAtPosition(a) != null)) { // check if we aren't trying to move an empty place
            if (inRange(b) && isNotDiagonal(a, b) && inForbiddenArea(getPieceAtPosition(a), b) && isClear(a, b)) {// check if the
                saveBoard(); // move is to  a legal place+isn't diagonal+ to a forbidden area+ nobody is in the way
                board[b.getX()][b.getY()] = board[a.getX()][a.getY()];//move the player
                board[a.getX()][a.getY()] = null;//empty the former spot
                board[b.getX()][b.getY()].addPosition(b); //add this pos to the peice postition array
                if (boardpositions[b.getX()][b.getY()] == null) {

                    boardpositions[b.getX()][b.getY()] = new Position(b.getX(), b.getY());
                }
                boardpositions[b.getX()][b.getY()].addToStepppedon(getPieceAtPosition(b));

                if (board[b.getX()][b.getY()].getName().charAt(0) == 'K') {
                    this.kingsPos.setX(b.getX());
                    this.kingsPos.setY(b.getY());
                    //  board[this.kingsPos.getX()][this.kingsPos.getY()].setPosition(b);
                    // board[this.kingsPos.getX()][this.kingsPos.getY()].addPosition(b);
                }

                this.currPlayer = !this.currPlayer;
                kill(b);
                isGameFinished();
                return true;
            }
        }
        isGameFinished();
        return false;
    }


    @Override
    public ConcretePiece getPieceAtPosition(Position position) {
        return board[position.getX()][position.getY()];
    }

    @Override
    public Player getFirstPlayer() {
        return attacker;
    }

    @Override
    public Player getSecondPlayer() {
        return defender;
    }

    @Override
    public boolean isGameFinished() {
        if (isInKingsArea(kingsPos)) {
            winner = true;

            defender.incWins();
            PrintPiecePositions(winner);
            printPieceByKills();
            printPieceByDistance();
            printPositons();
            return true;
        } else if (isKingCaptured()) {
            winner = false;

            attacker.incWins();
            PrintPiecePositions(winner);
            printPieceByKills();
            printPieceByDistance();
            printPositons();
            return true;
        }
        return false;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return !this.currPlayer;
    }

    @Override
    public void reset() {
        initBoard();
    }

    @Override
    public void undoLastMove() {
        if (!this.historyBoard.isEmpty()) {
            this.board = this.historyBoard.remove(this.historyBoard.size() - 1);
            this.boardpositions = this.historyboardpositions.remove(this.historyBoard.size() - 1);
            this.currPlayer = !this.currPlayer;

        }
    }

    @Override
    public int getBoardSize() {
        return Board_size;
    }

    private void initBoard() {
        for (int i = 0; i < Board_size; i++) {
            for (int j = 0; j < Board_size; j++) {
                board[i][j] = null;
            }
        }

        board[5][3] = new Pawn(defender, "D1", new Position(5, 3), "\u2659");
        board[4][4] = new Pawn(defender, "D2", new Position(4, 4), "\u2659");
        board[5][4] = new Pawn(defender, "D3", new Position(5, 4), "\u2659");
        board[6][4] = new Pawn(defender, "D4", new Position(6, 4), "\u2659");
        board[3][5] = new Pawn(defender, "D5", new Position(3, 5), "\u2659");
        board[4][5] = new Pawn(defender, "D6", new Position(4, 5), "\u2659");
        board[6][5] = new Pawn(defender, "D8", new Position(6, 5), "\u2659");
        board[7][5] = new Pawn(defender, "D9", new Position(7, 5), "\u2659");
        board[4][6] = new Pawn(defender, "D10", new Position(4, 6), "\u2659");
        board[5][6] = new Pawn(defender, "D11", new Position(5, 6), "\u2659");
        board[6][6] = new Pawn(defender, "D12", new Position(6, 6), "\u2659");
        board[5][7] = new Pawn(defender, "D13", new Position(5, 7), "\u2659");

        ///////////////////attacker///////////////////////////////////////////////////
        board[3][0] = new Pawn(attacker, "A1", new Position(3, 0), "\u265F");
        board[4][0] = new Pawn(attacker, "A2", new Position(4, 0), "\u265F");
        board[5][0] = new Pawn(attacker, "A3", new Position(5, 0), "\u265F");
        board[6][0] = new Pawn(attacker, "A4", new Position(6, 0), "\u265F");
        board[7][0] = new Pawn(attacker, "A5", new Position(7, 0), "\u265F");
        board[5][1] = new Pawn(attacker, "A6", new Position(5, 1), "\u265F");

        board[0][3] = new Pawn(attacker, "A7", new Position(0, 3), "\u265F");
        board[0][4] = new Pawn(attacker, "A9", new Position(0, 4), "\u265F");
        board[0][5] = new Pawn(attacker, "A11", new Position(0, 5), "\u265F");
        board[0][6] = new Pawn(attacker, "A15", new Position(0, 6), "\u265F");
        board[0][7] = new Pawn(attacker, "A17", new Position(0, 7), "\u265F");
        board[1][5] = new Pawn(attacker, "A12", new Position(1, 5), "\u265F");

        board[10][3] = new Pawn(attacker, "A8", new Position(10, 3), "\u265F");
        board[10][4] = new Pawn(attacker, "A10", new Position(10, 4), "\u265F");
        board[10][5] = new Pawn(attacker, "A14", new Position(10, 5), "\u265F");
        board[10][6] = new Pawn(attacker, "A16", new Position(10, 6), "\u265F");
        board[10][7] = new Pawn(attacker, "A18", new Position(10, 7), "\u265F");
        board[9][5] = new Pawn(attacker, "A13", new Position(9, 5), "\u265F");

        board[5][9] = new Pawn(attacker, "A19", new Position(5, 9), "\u265F");
        board[3][10] = new Pawn(attacker, "A20", new Position(3, 10), "\u265F");
        board[4][10] = new Pawn(attacker, "A21", new Position(4, 10), "\u265F");
        board[5][10] = new Pawn(attacker, "A22", new Position(5, 10), "\u265F");
        board[6][10] = new Pawn(attacker, "A23", new Position(6, 10), "\u265F");
        board[7][10] = new Pawn(attacker, "A24", new Position(7, 10), "\u265F");

        board[5][5] = new King(defender, new Position(5, 5));
        kingsPos = new Position(5, 5);

        for (int i = 0; i < Board_size; i++) {
            for (int j = 0; j < Board_size; j++) {
                if (board[i][j] != null) {
                    Position pos1 = board[i][j].getCurrPos();
                    pos1.stepppedon.add(board[i][j]);
                    boardpositions[i][j] = pos1;
                }
            }

        }
        this.currPlayer = false;
    }

    private boolean inRange(Position pos) {
        return (-1 < pos.getX() && pos.getX() < Board_size) && (-1 < pos.getY() && pos.getY() < Board_size);
    }

    private boolean isNotDiagonal(Position pos1, Position pos2) {
        return (pos1.getX() == pos2.getX()) || (pos1.getY() == pos2.getY());

    }

    private boolean inForbiddenArea(ConcretePiece p, Position pos) {
        if (p.getName().charAt(0) == 'D' || p.getName().charAt(0) == 'A') {
            return !(isInKingsArea(pos));
        }
        return true;
    }

    private boolean isClear(Position a, Position b) {
        int maxX = Math.max(a.getX(), b.getX());
        int maxY = Math.max(a.getY(), b.getY());
        int minX = Math.min(a.getX(), b.getX());
        int minY = Math.min(a.getY(), b.getY());

        if (a.getX() != b.getX()) {
            for (int i = minX; i <= maxX; i++) {
                if (board[i][a.getY()] != null && a.getX() != i) {
                    return false;
                }
            }
        }

        if (a.getY() != b.getY()) {
            for (int i = minY; i <= maxY; i++) {
                if (board[a.getX()][i] != null && a.getY() != i) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isKingCaptured() {
        int kingX = kingsPos.getX();
        int kingY = kingsPos.getY();
        Position[] enemies = {new Position(kingX + 1, kingY), new Position(kingX - 1, kingY),
                new Position(kingX, kingY + 1), new Position(kingX, kingY - 1)};

        for (Position enemy : enemies) {
            if (inRange(enemy) && (getPieceAtPosition(enemy) == null ||
                    getPieceAtPosition(enemy).getName().charAt(0) != 'A')) {
                return false;
            }
        }
        return true;
    }

    private boolean kill(Position pos) {
        boolean ans = false;
        if (getPieceAtPosition(pos).getName().charAt(0) == 'K') {
            return false;
        }
        Position[] positions = {new Position(pos.getX() + 1, pos.getY()), new Position(pos.getX(), pos.getY() + 1),
                new Position(pos.getX(), pos.getY() - 1), new Position(pos.getX() - 1, pos.getY())};

            for (Position enemyPos : positions) {
                if (inRange(enemyPos) && this.board[enemyPos.getX()][enemyPos.getY()] != null && getPieceAtPosition(enemyPos).getName().charAt(0) != 'K'
                        && this.board[enemyPos.getX()][enemyPos.getY()].getName().charAt(0) != this.board[pos.getX()][pos.getY()].getName().charAt(0)) {
                    if (isBlocked(enemyPos, new Position(enemyPos.getX() + (enemyPos.getX() - pos.getX()),
                            enemyPos.getY() + (enemyPos.getY()) - pos.getY()))) {
                        deadPiece.add(board[enemyPos.getX()][enemyPos.getY()]);
                        ((Pawn) board[pos.getX()][pos.getY()]).incKillCount();
                        board[enemyPos.getX()][enemyPos.getY()] = null;
                        ans = true;
                    }
                }
            }

        return ans;
    }

    private boolean isBlocked(Position pos, Position posToCheck) { //POS == ENEMY!!!!!!
        int posX = pos.getX();
        int posY = pos.getY();

        return (!inRange(posToCheck)) || isInKingsArea(posToCheck) || (this.board[posToCheck.getX()][posToCheck.getY()] != null &&
                getPieceAtPosition(posToCheck).getName().charAt(0) != 'K' &&
                this.board[posX][posY].getName().charAt(0) != this.board[posToCheck.getX()][posToCheck.getY()].getName().charAt(0));
    }

    private boolean isInKingsArea(Position pos) {
        for (Position position : this.kingsArea) {
            if (pos.compare(position)) return true;
        }
        return false;
    }

    private void saveBoard() {
        ConcretePiece[][] newBoard = new ConcretePiece[Board_size][Board_size];
        Position[][] newPositions = new Position[Board_size][Board_size];
        for (int i = 0; i < Board_size; i++)
            for (int j = 0; j < Board_size; j++) {
                newBoard[i][j] = this.board[i][j];
                newPositions[i][j] = this.boardpositions[i][j];
            }

        historyBoard.add(newBoard);
        historyboardpositions.add(newPositions);


    }


    Comparator<ConcretePiece> comparePositions = new Comparator<ConcretePiece>() {
        @Override
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            int ans = 0;
            if (p1.getPositionsLength() > p2.getPositionsLength()) {
                ans = 1;
            } else if (p1.getPositionsLength() < p2.getPositionsLength()) {
                ans = -1;
            } else {
                if (Integer.parseInt(p1.getName().substring(1)) > Integer.parseInt(p2.getName().substring(1))) {
                    ans = 1;
                } else if (Integer.parseInt(p1.getName().substring(1)) < Integer.parseInt(p2.getName().substring(1))) {
                    ans = -1;
                }
            }
            return ans;
        }
    };

    Comparator<Pawn> compareKills = new Comparator<Pawn>() {
        @Override
        public int compare(Pawn p1, Pawn p2) {
            int ans = 0;
            if (p1.getKillCount() < p2.getKillCount()) {
                return 1;
            } else if (p1.getKillCount() > p2.getKillCount()) {
                return -1;
            } else {
                if (Integer.parseInt(p1.getName().substring(1)) < Integer.parseInt(p2.getName().substring(1))) {
                    return -1;
                } else if (Integer.parseInt(p1.getName().substring(1)) > Integer.parseInt(p2.getName().substring(1))) {
                    return 1;
                } else {
                    return compareByWinner(p1, winner);
                }
            }

        }

        public int compareByWinner(Pawn p1, boolean winner) {
            if (p1.getOwner().isPlayerOne() == winner)
                return -1;
            return 1;
        }
    };

    Comparator<ConcretePiece> compareSteps = new Comparator<ConcretePiece>() {
        @Override
        public int compare(ConcretePiece p1, ConcretePiece p2) {
            int ans = 0;
            if (p1.calcDistance() < p2.calcDistance()) {
                ans = 1;
            } else if (p1.calcDistance() > p2.calcDistance()) {
                ans = -1;
            } else {
                if (Integer.parseInt(p1.getName().substring(1)) < Integer.parseInt(p2.getName().substring(1))) {
                    ans = -1;
                } else if (Integer.parseInt(p1.getName().substring(1)) > Integer.parseInt(p2.getName().substring(1))) {
                    ans = 1;
                } else {
                    ans = -compareByWinner(p1, winner);
                }
            }
            return ans;
        }

        public int compareByWinner(ConcretePiece p1, boolean winner) {
            if (p1.getOwner().isPlayerOne() == winner)
                return 1;
            return -1;
        }
    };

    private void print75() {
        System.out.println();
        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println();
    }

    private void PrintPiecePositions(boolean winner) {
        ArrayList<ConcretePiece> defenderPieces = new ArrayList<ConcretePiece>();//array of defender's pawn+king
        ArrayList<ConcretePiece> attackerPieces = new ArrayList<ConcretePiece>();// array of  attacker's pawns
        int k = 0;//king is in first spot
        int l = 0;
        int howManyDefender = 1;
        int howManyAttacker = 0;
       if(getPieceAtPosition(kingsPos).getPositionsLength()>1) {
           defenderPieces.add(0, getPieceAtPosition(kingsPos));
           k++;
       }
        for (int i = 0; i < Board_size; i++) {
            for (int j = 0; j < Board_size; j++) {
                if ((board[i][j] != null) && (board[i][j].getPositionsLength() > 1) && (board[i][j].getName().charAt(0) == 'D')) {
                    defenderPieces.add(k, board[i][j]);
                    k++;
                    howManyDefender++;
                } else if (board[i][j] != null && board[i][j].getPositionsLength() > 1 && board[i][j].getName().charAt(0) == 'A') {
                    attackerPieces.add(l, board[i][j]);
                    l++;
                    howManyAttacker++;
                }

            }
        }
        for (int j = 0; j < deadPiece.size(); j++) { // add all the dead pieces to the arrays
            ConcretePiece currPiece = deadPiece.get(j);
            if (currPiece.getPositionsLength() > 1) {
                if (currPiece.getName().charAt(0) == 'D' || currPiece.getName().charAt(0) == 'k') {
                    defenderPieces.add(howManyDefender, currPiece);
                    howManyDefender++;
                } else {
                    attackerPieces.add(howManyAttacker, currPiece);
                    howManyAttacker++;
                }
            }
        }
        defenderPieces.sort(comparePositions);
        attackerPieces.sort(comparePositions);
        if (winner) { //defender has won
            printArrayWin(defenderPieces, attackerPieces);
        } else {   //attacker has won
            printArrayWin(attackerPieces, defenderPieces);
        }
        print75();
    }

    private void printPieceByKills() {
        ArrayList<Pawn> pawns = new ArrayList<>();

        for (int i = 0; i < Board_size; i++) {
            for (int j = 0; j < Board_size; j++) {
                if (board[i][j] != null && board[i][j].getName().charAt(0) != 'K' && ((Pawn) board[i][j]).getKillCount() > 0) {
                    pawns.add((Pawn) board[i][j]);
                }
            }
        }

        for (ConcretePiece piece : deadPiece) { // add all the dead pieces to the arrays
            if (((Pawn) piece).getKillCount() > 0) {
                pawns.add((Pawn) piece);
            }
        }

        pawns.sort(compareKills);

        for (Pawn p : pawns) {
            if (p == pawns.get(pawns.size() - 1)) System.out.print(p.getName() + ": " + p.getKillCount() + " kills");
            else System.out.println(p.getName() + ": " + p.getKillCount() + " kills");
        }

        print75();
    }

    private void printPieceByDistance() {
        ArrayList<ConcretePiece> pieces = new ArrayList<>();
        for (int i = 0; i < Board_size; i++) {
            for (int j = 0; j < Board_size; j++) {
                if (board[i][j] != null && board[i][j].calcDistance() > 0) {
                    pieces.add(board[i][j]);
                }
            }
        }

        for (ConcretePiece piece : deadPiece) { // add all the dead pieces to the arrays
            if (piece.calcDistance() > 0) {
                pieces.add(piece);
            }
        }

        pieces.sort(compareSteps);

        for (ConcretePiece p : pieces) {
            if (p == pieces.get(pieces.size() - 1)) System.out.print(p.getName() + ": " + p.calcDistance() + " squares");
            else System.out.println(p.getName() + ": " + p.calcDistance() + " squares");
        }

        print75();
    }

    private void printArrayWin(ArrayList<ConcretePiece> a, ArrayList<ConcretePiece> b) {
        for (int i = 0; i < a.size(); i++) {
            if (i == a.size() - 1) {
                System.out.print(a.get(i).getName() + ":" + " ");
                System.out.print(a.get(i).getPositions().toString());
            } else if (a.get(i) != null) {
                System.out.print(a.get(i).getName() + ":" + " ");
                System.out.println(a.get(i).getPositions().toString());

            }
        }
        System.out.println();
        for (int i = 0; i < b.size(); i++) {
            if (i == b.size() - 1) {
                System.out.print(b.get(i).getName() + ":" + " ");
                System.out.print(b.get(i).getPositions().toString());
            } else if (b.get(i) != null) {
                System.out.print(b.get(i).getName() + ":" + " ");
                System.out.println(b.get(i).getPositions().toString());
            }
        }
    }

    private void printPositons() {
        ArrayList<Position> usedpositions = new ArrayList<Position>();
        for (int i = 0; i < boardpositions.length; i++) {
            for (int j = 0; j < boardpositions.length; j++) {
                if (boardpositions[i][j] != null) {
                    if (boardpositions[i][j].getStepppedonSize() > 1) {
                        usedpositions.add(boardpositions[i][j]);
                    }
                }
            }
        }
        Collections.sort(usedpositions, new CompareQuantity().reversed());
        for (int i = 0; i < usedpositions.size() - 1; i++) {
            Position currpos = usedpositions.get(i);
            System.out.println(currpos.toString() + currpos.stepppedon.size() + " pieces");
        }
        Position currpos = usedpositions.get(usedpositions.size() - 1);
        System.out.print(currpos.toString() + currpos.stepppedon.size() + " pieces");
        print75();
    }
}


