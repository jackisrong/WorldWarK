package worldwark;

import java.util.ArrayList;
import java.util.Random;

public class EnemyFactory {

    private Random rand = new Random();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Boss boss;
    private int reverse = 0;
    private int diff = 0;
    
    public ArrayList<Enemy> makeEnemies(int score, WorldWarK panel) {
        int choice = getChoice(score, WorldWarK.panel);
        diff = panel.difficulty;
        enemies.clear();
        if (score <= panel.snapShot + 2500) {
            spawnEnemiesUnder2500(choice);
        } else if (score > panel.snapShot + 2500 && score <= panel.snapShot + 7500) {
            spawnEnemiesUnder5000(choice);
        } else if (score > panel.snapShot + 7500 && score <= panel.snapShot + 10000) {
            spawnEnemiesUnder10000(choice);
        } else if (panel.b == 0) {
            spawnBoss(choice, panel);
        } else {
            spawnBossEnemies(choice);
        }
        return enemies;
    }

    private int getChoice(int score, WorldWarK panel) {

        if (score >= 0 && score <= panel.snapShot + 3000) {
            return rand.nextInt(6) + 1;
        } else if (score > panel.snapShot + 3000 && score <= panel.snapShot + 7500) {
            return rand.nextInt(8) + 1;
        } else if (score > panel.snapShot + 7500 && score < panel.snapShot + 10000) {
            return rand.nextInt(9) + 1;
        } else {
            return rand.nextInt(3) + 1;
        }
    }

    private void spawnEnemiesUnder2500(int choice) { // adding this way saves a few more lines
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemies.add(new Enemy(50 + i, 0 - i, 64, 64, 0, 5 + diff, 50 * diff, 3, 50, 1000 + i * 2));
                    enemies.add(new Enemy(385 - i, 0 - i, 64, 64, 0, 5 + diff, 50 * diff, 3, 50, 50 + i * 2));
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemies.add(new Enemy(50 - i * 2, 250, 64, 64, 5 + diff, 0, 50 * diff, 0, 50, 1000 + i * 2));
                        enemies.add(new Enemy(450 + i * 2, 150, 64, 64, -5 - diff, 0, 50 * diff, 0, 50, 1000 + i * 2));
                    }
                }
                break;
            case 3:
                Enemy enemyLeft = new Enemy(300, 0, 64, 64, 0, 5 + diff, 50 * diff, 0, 50, 1000);
                Enemy enemyRight = new Enemy(400, 0, 64, 64, 0, 5 + diff, 50 * diff, 0, 50, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 4:
                enemyLeft = new Enemy(0, 100, 64, 64, 5 - diff, 0, 50 * diff, 0, 50, 1000);
                enemyRight = new Enemy(0, 300, 64, 64, 5 - diff, 0, 50 * diff, 0, 50, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 5:
                for (int i = 0; i < 150; i += 50) {
                    enemies.add(new Enemy(-200 + i, 32 + i, 64, 64, 3 + diff, 0, 50 * diff, 0, 50, 1000 + i * 2));
                    enemies.add(new Enemy(700 - i, 296 - i, 64, 64, -3 - diff, 0, 50 * diff, 0, 50, 1000 + i * 2));
                }
                break;
            case 6:
                for (int i = 0; i < 150; i += 50) {
                    enemies.add(new Enemy(-200 + i, -i, 64, 64, 4 + diff, 5 + diff, 50 * diff, 0, 50, 1000 + i * 2));
                    enemies.add(new Enemy(700 - i, -i, 64, 64, -4 - diff, 5 + diff, 50 * diff, 0, 50, 1000 + i * 2));
                }
                break;
        }
    }

    private void spawnEnemiesUnder5000(int choice) { // uses more lines but idk
        Enemy enemyLeft = null;
        Enemy enemyRight = null;
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 7 + diff, 100 * diff, 4, 75, 1000 + i * 2);
                    enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 7 + diff, 100 * diff, 4, 75, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 7 + diff, 0, 100 * diff, 1, 75, 1000 + i * 2);
                        enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -7 - diff, 0, 100 * diff, 1, 75, 1000 + i * 2);
                        enemies.add(enemyLeft);
                        enemies.add(enemyRight);
                    }
                }
                break;
            case 3:
                enemyLeft = new Enemy(300, 0, 64, 64, 0, 7 + diff, 100 * diff, 1, 75, 1000);
                enemyRight = new Enemy(400, 0, 64, 64, 0, 7 + diff, 100 * diff, 1, 75, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 4:
                enemyLeft = new Enemy(0, 100, 64, 64, 7 + diff, 0, 100 * diff, 1, 75, 1000);
                enemyRight = new Enemy(0, 300, 64, 64, 7 + diff, 0, 100 * diff, 1, 75, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 5:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, 32 + i, 64, 64, 5 + diff, 0, 100 * diff, 1, 75, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, 296 - i, 64, 64, -5 - diff, 0, 100 * diff, 1, 75, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 6:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, -i, 64, 64, 5 + diff, 7 + diff, 100 * diff, 1, 75, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, -i, 64, 64, -5 - diff, 7 + diff, 100 * diff, 1, 75, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 7:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        reverse = -1;
                    } else {
                        reverse = 1;
                    }
                    enemyLeft = new Enemy(0 - i * 2, 100 + i - 25, 64, 64, 3 + diff, (-3 - diff) * reverse, 100 * diff, 6, 75, 1000 + i * 2);
                    enemyRight = new Enemy(450 + i * 2, 300 - i + 25, 64, 64, -3 - diff, (3 + diff) * reverse, 100 * diff, 6, 75, 1000 + i * 2);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 8:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        reverse = -1;
                    } else {
                        reverse = 1;
                    }
                    enemyLeft = new Enemy(50, 0 - i - 25, 64, 64, (-3 - diff) * reverse, 7 + diff, 100 * diff, 8, 75, 1000 + i * 2);
                    enemyRight = new Enemy(385, 0 - i - 25, 64, 64, (3 + diff) * reverse, 7 + diff, 100 * diff, 8, 75, 1000 + i * 2);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
        }
    }

    private void spawnEnemiesUnder10000(int choice) {
        Enemy enemyLeft = null;
        Enemy enemyRight = null;
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 8 + diff, 100 * diff, 5, 100, 1000 + i * 2);
                    enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 8 + diff, 100 * diff, 5, 100, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 8 + diff, 0, 100 * diff, 2, 100, 1000 + i * 2);
                        enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -8 - diff, 0, 100 * diff, 2, 100, 1000 + i * 2);
                        enemies.add(enemyLeft);
                        enemies.add(enemyRight);
                    }
                }
                break;
            case 3:
                enemyLeft = new Enemy(300, 0, 64, 64, 0, 8 + diff, 100 * diff, 2, 100, 1000);
                enemyRight = new Enemy(400, 0, 64, 64, 0, 8 + diff, 100 * diff, 2, 100, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 4:
                for (int i = 0; i <= 50; i += 50) {
                    enemyLeft = new Enemy(0 + i * 10, 100 + i, 64, 64, 8 + diff, 0, 100 * diff, 2, 100, 1000);
                    enemyRight = new Enemy(0 + i * 10, 300 + i, 64, 64, 8 + diff, 0, 100 * diff, 2, 100, 1000);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 5:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, 32 + i, 64, 64, 6 + diff, 0, 100 * diff, 2, 100, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, 296 - i, 64, 64, -6 - diff, 0, 100 * diff, 2, 100, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 6:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, -i, 64, 64, 6 + diff, 8 + diff, 100 * diff, 2, 50, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, -i, 64, 64, -6 - diff, 8 + diff, 100 * diff, 2, 50, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 7:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        reverse = -1;
                    } else {
                        reverse = 1;
                    }
                    enemyLeft = new Enemy(0 - i * 2, 100 + i - 25, 64, 64, 2 + diff, (-2 - diff) * reverse, 100 * diff, 6, 75, 1000 + i * 2);
                    enemyRight = new Enemy(450 + i * 2, 300 - i + 25, 64, 64, -2 - diff, (2 + diff) * reverse, 100 * diff, 6, 75, 1000 + i * 2);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 8:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        reverse = -1;
                    } else {
                        reverse = 1;
                    }
                    enemyLeft = new Enemy(50, 0 - i - 25, 64, 64, (-2 - diff) * reverse, 6 + diff, 100 * diff, 8, 75, 1000 + i * 2);
                    enemyRight = new Enemy(385, 0 - i - 25, 64, 64, (2 + diff) * reverse, 6 + diff, 100 * diff, 8, 75, 1000 + i * 2);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 9:
                for (int h = 0; h <= 100; h += 100) {

                    for (int i = 0; i <= 1; i++) {
                        if (i == 1) {
                            int r = -1;
                            enemyLeft = new Enemy(0 - h, 50 + h, 64, 64, 5 + diff, 0, 100 * diff, 10, 50, 1000);
                            enemyRight = new Enemy(500 + h, 250 + h, 64, 64, -5 - diff, 0, 100 * diff, 10, 50, 1000);
                            enemyLeft.setReverse(true);
                            enemyRight.setReverse(true);
                            enemies.add(enemyLeft);
                            enemies.add(enemyRight);
                        }
                    }
                }
                break;
        }
    }

    private void spawnBoss(int choice, WorldWarK panel) {
        boss = new Boss(150, 0, 219, 196, 0, 3 + diff, 5000 * diff, 0, 300, 2000);
        enemies.add(boss);
        panel.b = 1;
    }

    private void spawnBossEnemies(int choice) {
        Enemy enemyLeft = null;
        Enemy enemyRight = null;
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 5 + diff, 50 * diff, 3, 50, 1000 + i * 2);
                    enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 5 + diff, 50 * diff, 3, 50, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 5 + diff, 0, 50 * diff, 0, 50, 1000 + i * 2);
                        enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -5 - diff, 0, 50 * diff, 0, 50, 1000 + i * 2);
                        enemies.add(enemyLeft);
                        enemies.add(enemyRight);
                    }
                }
                break;
            case 3:
                enemyLeft = new Enemy(300, 0, 64, 64, 0, 5 + diff, 50 * diff, 0, 50, 1000);
                enemyRight = new Enemy(400, 0, 64, 64, 0, 5 + diff, 50 * diff, 0, 50, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
        }
    }
}
