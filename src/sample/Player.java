package sample;

public class Player {
    private int health ;
    private int gold;

    public Player() {
        health = 5;
        gold = 100;
    }
    public int getGold() {
        return gold;
    }

    @Override
    public String toString() {
        return gold +"\n" + health;
    }
    public void subtractGold(int cost) {
        gold -= cost;
    }

    public void getDamage() {
        health--;
    }
    public void getReward(int reward) {
        gold += reward;
    }
    public boolean dead() {
        return health == 0;
    }
}
