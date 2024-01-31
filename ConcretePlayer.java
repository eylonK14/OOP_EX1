public class ConcretePlayer implements Player {
    private boolean role; // true - attacker, false -defender
    private int winCounter=0;
    public ConcretePlayer(boolean role){

        this.role = role;
        this.winCounter = 0;
    }
    @Override
    public boolean isPlayerOne() { return this.role; }
    @Override
    public int getWins() {
        return winCounter;
    }
    public void incWins(){ this.winCounter++; }
}
