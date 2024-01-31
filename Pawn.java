import java.util.ArrayList;
public class Pawn extends ConcretePiece{
    private int killCount;
    public Pawn(ConcretePlayer player, String name, Position currPos, String type){
        super(player, name, currPos, type);

        this.killCount = 0;
    }
    public int getKillCount() {
        return this.killCount;
    }
    public void incKillCount() {this.killCount++;}

}
