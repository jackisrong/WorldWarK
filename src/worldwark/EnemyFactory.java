package worldwark;

import java.util.ArrayList;
import java.util.Random;

public class EnemyFactory {

    private static Random rand = new Random();
    private static ArrayList<Enemy> enemies = new ArrayList<>();
    private static int b = 0;
    private static Boss boss;
    
    public static ArrayList<Enemy> makeEnemies(int score) {
        int choice = getChoice(score);
        enemies.clear();
        if (score <= 2500) {
            spawnEnemiesUnder2500(choice);
        } else if (score <= 5000) {
            spawnEnemiesUnder5000(choice);
        } else if (score <= 10000) {
            spawnEnemiesUnder10000(choice);
        } else if (b == 0) {
            spawnBoss(choice);
        } else {
            spawnBossEnemies(choice);
        }
        return enemies;
    }

    private static int getChoice(int score) {
        if (score > 2500 && score <= 5000) {
            return rand.nextInt(8) + 1;
        } else if (score > 5000 && score <= 7500) {
            return rand.nextInt(9) + 1;
        } else if (score >= 10000) {
            return rand.nextInt(3) + 1;
        } else {
            return rand.nextInt(6) + 1;
        }
    }

    private static void spawnEnemiesUnder2500(int choice) { // adding this way saves a few more lines
        System.out.println(choice);
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemies.add(new Enemy(50 + i, 0 - i, 64, 64, 0, 5, 50, 3, 50, 1000 + i * 2));
                    enemies.add(new Enemy(385 - i, 0 - i, 64, 64, 0, 5, 50, 3, 50, 1000 + i * 2));
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemies.add(new Enemy(50 - i * 2, 250, 64, 64, 5, 0, 15, 0, 50, 1000 + i * 2));
                        enemies.add(new Enemy(450 + i * 2, 150, 64, 64, -5, 0, 15, 0, 50, 1000 + i * 2));
                    }
                }
                break;
            case 3:
                Enemy enemyLeft = new Enemy(300, 0, 64, 64, 0, 5, 50, 0, 50, 1000);
                Enemy enemyRight = new Enemy(400, 0, 64, 64, 0, 5, 50, 0, 50, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 4:
                enemyLeft = new Enemy(0, 100, 64, 64, 5, 0, 50, 0, 50, 1000);
                enemyRight = new Enemy(0, 300, 64, 64, 5, 0, 50, 0, 50, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 5:
                for (int i = 0; i < 150; i += 50) {
                    enemies.add(new Enemy(-200 + i, 32 + i, 64, 64, 3, 0, 15, 0, 50, 1000 + i * 2));
                    enemies.add(new Enemy(700 - i, 296 - i, 64, 64, -3, 0, 15, 0, 50, 1000 + i * 2));
                }
                break;
            case 6:
                for (int i = 0; i < 150; i += 50) {
                    enemies.add(new Enemy(-200 + i, -i, 64, 64, 4, 5, 15, 0, 50, 1000 + i * 2));
                    enemies.add(new Enemy(700 - i, -i, 64, 64, -4, 5, 15, 0, 50, 1000 + i * 2));
                }
                break;
        }
    }

    private static void spawnEnemiesUnder5000(int choice) { // uses more lines but idk
        Enemy enemyLeft = null;
        Enemy enemyRight = null;
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 7, 50, 4, 75, 1000 + i * 2);
                    enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 7, 50, 4, 75, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 7, 0, 15, 1, 75, 1000 + i * 2);
                        enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -7, 0, 15, 1, 75, 1000 + i * 2);
                        enemies.add(enemyLeft);
                        enemies.add(enemyRight);
                    }
                }
                break;
            case 3:
                enemyLeft = new Enemy(300, 0, 64, 64, 0, 7, 50, 1, 75, 1000);
                enemyRight = new Enemy(400, 0, 64, 64, 0, 7, 50, 1, 75, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 4:
                enemyLeft = new Enemy(0, 100, 64, 64, 7, 0, 50, 1, 75, 1000);
                enemyRight = new Enemy(0, 300, 64, 64, 7, 0, 50, 1, 75, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 5:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, 32 + i, 64, 64, 5, 0, 15, 1, 75, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, 296 - i, 64, 64, -5, 0, 15, 1, 75, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 6:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, -i, 64, 64, 5, 7, 15, 1, 75, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, -i, 64, 64, -5, 7, 15, 1, 75, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 7:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        b = -1;
                    } else {
                        b = 1;
                    }
                    System.out.println(b);
                    enemyLeft = new Enemy(0 - i * 2, 100 + i - 25, 64, 64, 3, -3 * b, 50, 6, 75, 1000 + i * 2);
                    enemyRight = new Enemy(450 + i * 2, 300 - i + 25, 64, 64, -3, 3 * b, 50, 6, 75, 1000 + i * 2);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 8:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        b = -1;
                    } else {
                        b = 1;
                    }
                    System.out.println(b);
                    enemyLeft = new Enemy(50, 0 - i - 25, 64, 64, -3 * b, 7, 50, 8, 75, 1000 + i * 2);
                    enemyRight = new Enemy(385, 0 - i - 25, 64, 64, 3 * b, 7, 50, 8, 75, 1000 + i * 2);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
        }
    }

    private static void spawnEnemiesUnder10000(int choice) {
        Enemy enemyLeft = null;
        Enemy enemyRight = null;
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 10, 50, 5, 100, 1000 + i * 2);
                    enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 10, 50, 5, 100, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 10, 0, 15, 2, 100, 1000 + i * 2);
                        enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -10, 0, 15, 2, 100, 1000 + i * 2);
                        enemies.add(enemyLeft);
                        enemies.add(enemyRight);
                    }
                }
                break;
            case 3:
                enemyLeft = new Enemy(300, 0, 64, 64, 0, 10, 50, 2, 100, 1000);
                enemyRight = new Enemy(400, 0, 64, 64, 0, 10, 50, 2, 100, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
            case 4:
                for (int i = 0; i <= 50; i += 50) {
                    enemyLeft = new Enemy(0 + i * 10, 100 + i, 64, 64, 10, 0, 50, 2, 100, 1000);
                    enemyRight = new Enemy(0 + i * 10, 300 + i, 64, 64, 10, 0, 50, 2, 100, 1000);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 5:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, 32 + i, 64, 64, 7, 0, 15, 2, 100, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, 296 - i, 64, 64, -7, 0, 15, 2, 100, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 6:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(-200 + i, -i, 64, 64, 7, 10, 15, 2, 50, 1000 + i * 2);
                    enemyRight = new Enemy(700 - i, -i, 64, 64, -7, 10, 15, 2, 50, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 7:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        b = -1;
                    } else {
                        b = 1;
                    }
                    System.out.println(b);
                    enemyLeft = new Enemy(0 - i * 2, 100 + i - 25, 64, 64, 3, -3 * b, 50, 6, 75, 1000 + i * 2);
                    enemyRight = new Enemy(450 + i * 2, 300 - i + 25, 64, 64, -3, 3 * b, 50, 6, 75, 1000 + i * 2);
                    enemyLeft.setReverse(true);
                    enemyRight.setReverse(true);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 8:
                for (int i = 0; i < 150; i += 50) {
                    if (i == 50) {
                        b = -1;
                    } else {
                        b = 1;
                    }
                    System.out.println(b);
                    enemyLeft = new Enemy(50, 0 - i - 25, 64, 64, -3 * b, 7, 50, 8, 75, 1000 + i * 2);
                    enemyRight = new Enemy(385, 0 - i - 25, 64, 64, 3 * b, 7, 50, 8, 75, 1000 + i * 2);
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
                            enemyLeft = new Enemy(0 - h, 50 + h, 64, 64, 5, 0, 50, 10, 50, 1000);
                            enemyRight = new Enemy(500 + h, 250 + h, 64, 64, -5, 0, 50, 10, 50, 1000);
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

    private static void spawnBoss(int choice) {
            boss = new Boss(250, 0, 72, 72, 0, 3, 1000, 0, 300, 2000);
            enemies.add(boss);
            b = 1;
            System.out.println("Y Position: " + boss.getYPos());
            System.out.println("X Position: " + boss.getXPos());
            System.out.println("-----------------------");
    }
    
    private static void spawnBossEnemies(int choice) {
        Enemy enemyLeft = null;
        Enemy enemyRight = null;
        switch (choice) {
            case 1:
                for (int i = 0; i < 150; i += 50) {
                    enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 5, 50, 3, 50, 1000 + i * 2);
                    enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 5, 50, 3, 50, 1000 + i * 2);
                    enemies.add(enemyLeft);
                    enemies.add(enemyRight);
                }
                break;
            case 2:
                for (int i = 0; i <= 100; i++) {
                    if (i == 0 || i == 50 || i == 100) {
                        enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 5, 0, 15, 0, 50, 1000 + i * 2);
                        enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -5, 0, 15, 0, 50, 1000 + i * 2);
                        enemies.add(enemyLeft);
                        enemies.add(enemyRight);
                    }
                }
                break;
            case 3:
                enemyLeft = new Enemy(300, 0, 64, 64, 0, 5, 50, 0, 50, 1000);
                enemyRight = new Enemy(400, 0, 64, 64, 0, 5, 50, 0, 50, 1000);
                enemyLeft.setReverse(true);
                enemyRight.setReverse(true);
                enemies.add(enemyLeft);
                enemies.add(enemyRight);
                break;
        }
    }
}