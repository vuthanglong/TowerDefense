package Entity.Enemies;

public interface EnemiesInfo {
    String NORMAL_ENEMY_IMAGE_PATH = "Model/Images/normal.png";
    int NORMAL_ENEMY_VELOCITY = 3;
    double NORMAL_ENEMY_MAX_HP = 120;
    int NORMAL_ENEMY_REWARD = 10;
    int NORMAL_ENEMY_ARMOR = 10;

    String SMALLER_ENEMY_IMAGE_PATH = "Model/Images/smaller.png";
    int SMALLER_ENEMY_VELOCITY = 6;
    double SMALLER_ENEMY_MAX_HP = 60;
    int SMALLER_ENEMY_REWARD = 10;
    int SMALLER_ENEMY_ARMOR = 0;

    String TANKER_ENEMY_IMAGE_PATH = "Model/Images/tank.png";
    int TANKER_ENEMY_VELOCITY = 1;
    double TANKER_ENEMY_MAX_HP = 200;
    int TANKER_ENEMY_REWARD = 10;
    int TANKER_ENEMY_ARMOR = 30;

    String BOSS_ENEMY_IMAGE_PATH = "Model/Images/boss.png";
    int BOSS_ENEMY_VELOCITY = 1;
    double BOSS_ENEMY_MAX_HP = 500;
    int BOSS_ENEMY_REWARD = 10;
    int BOSS_ENEMY_ARMOR = 50;
}
