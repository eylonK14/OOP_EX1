import java.util.ArrayList;
import java.util.Iterator;

public abstract class ConcretePiece implements Piece {
    private Position currPos;
    private ConcretePlayer player;
    private ArrayList<Position> positions;//all the positions the piece stepped on
    private String name;
    private String type;

    public ConcretePiece(ConcretePlayer Player, String name, Position currPos, String type) {
        this.positions = new ArrayList<Position>();
        this.positions.add(currPos);

        this.player = Player;
        this.name = name;
        this.currPos = currPos;
        this.type = type;

    }

    @Override
    public Player getOwner() {
        return this.player;
    }

    @Override
    public String getType() {
        return this.type;
    }

    public Position getCurrPos() {
        return this.currPos;
    }
    public String getName() {
        return this.name;
    }

    public void setPosition(Position pos) {
        this.currPos.setX(pos.getX());
        this.currPos.setY(pos.getY());
    }

    public void addPosition(Position pos) {
        this.positions.add(pos);
    }

    public int getPositionsLength() {
        return this.positions.size();
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

    public String toString(ArrayList<Position> positions) {
        String ans = null;
        for (int i = 0; i < positions.size(); i++) {
            ans = ans + positions.get(i).toString();
        }
        return ans;
    }

    public int calcDistance() {
        int sum = 0;

        for (int i = 0; i < positions.size() - 1; i++) {
            sum += Math.abs(positions.get(i).getX() - positions.get(i + 1).getX()) + Math.abs(positions.get(i).getY() - positions.get(i + 1).getY());
        }
        return sum;
    }
}


