package sample;

public class Player {
    public int health ;
    int gold;

    public Player() {
        health = 5;
        gold = 100;
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
