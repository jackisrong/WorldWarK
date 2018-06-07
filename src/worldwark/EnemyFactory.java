package worldwark;

import java.util.ArrayList;
import java.util.Random;

public class EnemyFactory {

    private Random rand = new Random();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Boss boss;
    private int r = 0;
    private int d = 0;

    public ArrayList<Enemy> makeEnemies(int score, WorldWarK panel) {
        // Spawns certain groups of enemies based on score
        int choice = getChoice(score, WorldWarK.panel);
        d = panel.difficulty;
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
        // Generates random number based on score
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

    /*
    Case 1: Vertical straight
    Case 2: Horizontal straight
    Case 3: Vertical down and return
    Case 4: Horizontal down and return
    Case 5: Horizontal diagonal formation
    Case 6: Diagonal cross
    Case 7: Horizontal zigzag
    Case 8: Vertical zigzag
    Case 9: Stop and random dispersion 
    */
    private void spawnEnemiesUnder2500(int choice) {
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemies.add(new Enemy(50 + i, 0 - i, 64, 64, 0, 5 + d, 50 * d, 3, 50, 1000 + i * 2));
                    enemies.add(new Enemy(385 - i, 0 - i, 64, 64, 0, 5 + d, 50 * d, 3, 50, 50 + i * 2));
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemies.add(new Enemy(50 - i * 2, 250, 64, 64, 5 + d, 0, 50 * d, 0, 50, 1000 + i * 2));
                        enemies.add(new Enemy(450 + i * 2, 150, 64, 64, -5 - d, 0, 50 * d, 0, 50, 1000 + i * 2));
                    }
                }
                break;
            case 3:
                Enemy enemyLeft = new Enemy(300, 0, 64, 64, 0, 5 + d, 50 * d, 0, 50, 1000);
                Enemy enemyRight = new Enemy(400, 0, 64, 64, 0, 5 + d, 50 * d, 0, 50, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 4:
                enemyLeft = new Enemy(0, 100, 64, 64, 5 - d, 0, 50 * d, 0, 50, 1000);
                enemyRight = new Enemy(0, 300, 64, 64, 5 - d, 0, 50 * d, 0, 50, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 5:
                for (int i = 0; i < 150; i += 50) {
                    enemies.add(new Enemy(-200 + i, 32 + i, 64, 64, 3 + d, 0, 50 * d, 0, 50, 1000 + i * 2));
                    enemies.add(new Enemy(700 - i, 296 - i, 64, 64, -3 - d, 0, 50 * d, 0, 50, 1000 + i * 2));
                }
                break;
            case 6:
                for (int i = 0; i < 150; i += 50) {
                    enemies.add(new Enemy(-200 + i, -i, 64, 64, 4 + d, 5 + d, 50 * d, 0, 50, 1000 + i * 2));
                    enemies.add(new Enemy(700 - i, -i, 64, 64, -4 - d, 5 + d, 50 * d, 0, 50, 1000 + i * 2));
                }
                break;
        }
    }

    private void spawnEnemiesUnder5000(int choice) {
        Enemy enemyLeft = null;
        Enemy enemyRight = null;
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 7 + d, 100 * d, 4, 75, 1000 + i * 2);
                    enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 7 + d, 100 * d, 4, 75, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 7 + d, 0, 100 * d, 1, 75, 1000 + i * 2);
                        enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -7 - d, 0, 100 * d, 1, 75, 1000 + i * 2);
                        enemies.add(enemyLeft);
                        enemies.add(enemyRight);
                    }
                }
                break;
            case 3:
                enemyLeft = new Enemy(300, 0, 64, 64, 0, 7 + d, 100 * d, 1, 75, 1000);
                enemyRight = new Enemy(400, 0, 64, 64, 0, 7 + d, 100 * d, 1, 75, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 4:
                enemyLeft = new Enemy(0, 100, 64, 64, 7 + d, 0, 100 * d, 1, 75, 1000);
                enemyRight = new Enemy(0, 300, 64, 64, 7 + d, 0, 100 * d, 1, 75, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 5:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, 32 + i, 64, 64, 5 + d, 0, 100 * d, 1, 75, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, 296 - i, 64, 64, -5 - d, 0, 100 * d, 1, 75, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 6:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, -i, 64, 64, 5 + d, 7 + d, 100 * d, 1, 75, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, -i, 64, 64, -5 - d, 7 + d, 100 * d, 1, 75, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 7:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        r = -1;
                    } else {
                        r = 1;
                    }
                    enemyLeft = new Enemy(0 - i * 2, 100 + i - 25, 64, 64, 3 + d, (-3 - d) * r, 100 * d, 6, 75, 1000 + i * 2);
                    enemyRight = new Enemy(450 + i * 2, 300 - i + 25, 64, 64, -3 - d, (3 + d) * r, 100 * d, 6, 75, 1000 + i * 2);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 8:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        r = -1;
                    } else {
                        r = 1;
                    }
                    enemyLeft = new Enemy(50, 0 - i - 25, 64, 64, (-3 - d) * r, 7 + d, 100 * d, 8, 75, 1000 + i * 2);
                    enemyRight = new Enemy(385, 0 - i - 25, 64, 64, (3 + d) * r, 7 + d, 100 * d, 8, 75, 1000 + i * 2);
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
                    enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 10 + d, 100 * d, 5, 100, 1000 + i * 2);
                    enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 10 + d, 100 * d, 5, 100, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 10 + d, 0, 100 * d, 2, 100, 1000 + i * 2);
                        enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -10 - d, 0, 100 * d, 2, 100, 1000 + i * 2);
                        enemies.add(enemyLeft);
                        enemies.add(enemyRight);
                    }
                }
                break;
            case 3:
                enemyLeft = new Enemy(300, 0, 64, 64, 0, 10 + d, 100 * d, 2, 100, 1000);
                enemyRight = new Enemy(400, 0, 64, 64, 0, 10 + d, 100 * d, 2, 100, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 4:
                for (int i = 0; i <= 50; i += 50) {
                    enemyLeft = new Enemy(0 + i * 10, 100 + i, 64, 64, 10 + d, 0, 100 * d, 2, 100, 1000);
                    enemyRight = new Enemy(0 + i * 10, 300 + i, 64, 64, 10 + d, 0, 100 * d, 2, 100, 1000);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 5:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, 32 + i, 64, 64, 7 + d, 0, 100 * d, 2, 100, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, 296 - i, 64, 64, -7 - d, 0, 100 * d, 2, 100, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 6:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, -i, 64, 64, 7 + d, 10 + d, 100 * d, 2, 50, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, -i, 64, 64, -7 - d, 10 + d, 100 * d, 2, 50, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 7:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        r = -1;
                    } else {
                        r = 1;
                    }
                    enemyLeft = new Enemy(0 - i * 2, 100 + i - 25, 64, 64, 3 + d, (-3 - d) * r, 100 * d, 6, 75, 1000 + i * 2);
                    enemyRight = new Enemy(450 + i * 2, 300 - i + 25, 64, 64, -3 - d, (3 + d) * r, 100 * d, 6, 75, 1000 + i * 2);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 8:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        r = -1;
                    } else {
                        r = 1;
                    }
                    enemyLeft = new Enemy(50, 0 - i - 25, 64, 64, (-3 - d) * r, 7 + d, 100 * d, 8, 75, 1000 + i * 2);
                    enemyRight = new Enemy(385, 0 - i - 25, 64, 64, (3 + d) * r, 7 + d, 100 * d, 8, 75, 1000 + i * 2);
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
                            enemyLeft = new Enemy(0 - h, 50 + h, 64, 64, 5 + d, 0, 100 * d, 10, 50, 1000);
                            enemyRight = new Enemy(500 + h, 250 + h, 64, 64, -5 - d, 0, 100 * d, 10, 50, 1000);
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
        boss = new Boss(150, 0, 219, 196, 0, 3 + d, 5000 * d, 0, 300, 2000);
        enemies.add(boss);
        panel.b = 1;
    }

    private void spawnBossEnemies(int choice) {
        Enemy enemyLeft = null;
        Enemy enemyRight = null;
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 5 + d, 50 * d, 3, 50, 1000 + i * 2);
                    enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 5 + d, 50 * d, 3, 50, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 5 + d, 0, 50 * d, 0, 50, 1000 + i * 2);
                        enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -5 - d, 0, 50 * d, 0, 50, 1000 + i * 2);
                        enemies.add(enemyLeft);
                        enemies.add(enemyRight);
                    }
                }
                break;
            case 3:
                enemyLeft = new Enemy(300, 0, 64, 64, 0, 5 + d, 50 * d, 0, 50, 1000);
                enemyRight = new Enemy(400, 0, 64, 64, 0, 5 + d, 50 * d, 0, 50, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
        }
    }
}
