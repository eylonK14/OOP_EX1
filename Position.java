import java.util.ArrayList;
import java.util.Comparator;

public class Position extends Object {
    protected ArrayList<ConcretePiece> stepppedon = new ArrayList<ConcretePiece>();
    protected int x;
    protected int y;
    private int steppedOnCount = 0;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.steppedOnCount = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void incStepped() {
        this.steppedOnCount++;
    }

    public boolean compare(Position p) {
        return this.x == p.x && this.y == p.y;
    }

    @Override
    public String toString() {
        return ("(" + this.getX() + ", " + this.getY() + ")");
    }

    public boolean addToStepppedon(ConcretePiece a) {
        boolean ans = false;
        if (!(this.stepppedon.contains(a))) {
            this.stepppedon.add(a);
            ans = true;
        }
        return ans;
    }

    public ArrayList<ConcretePiece> getStepppedon() {
        return this.stepppedon;
    }

    public int getStepppedonSize() {
        return this.stepppedon.size();
    }
}

class CompareQuantity implements Comparator<Position> {
    @Override
    public int compare(Position p1, Position p2) {
        int ans = 0;
        if (p1.stepppedon.size() > p2.stepppedon.size()) {
            ans = 1;
        } else if (p1.stepppedon.size() < p2.stepppedon.size()) {
            ans = -1;
        } else {
            if (p1.x > p2.x) {
                ans = -1;
            } else if (p1.x < p2.x) {
                ans = 1;
            } else {
                if (p1.y > p2.y) {
                    ans = -1;
                } else if (p1.y < p2.y) {
                    ans = 1;
                }
            }
        }
        return ans;
    }
}
