/*
Numaan Cheema
COS 223-01
Dr. Ferdous

**********************************************************************
PROJECT 2 - APPLICATION OF FOR LOOPS & ARRAYS USING DOODLEPAD PACKAGE
**********************************************************************

Goals:
1. Take user input(using Scanner) to define count of enemies, targets,
   and frame rate
2. Enemies, targets, and user stays within the bounds of the program
   even when re-spawning
3. The # of enemies is upgradeable
4. Score updates as target objects are intersected
5. Lives decrement when enemy objects intersect user
6. When lives == 0, "Game Over" is displayed

 */

import doodlepad.*;
import java.util.*;

public class Project2 {

    //definitions and assignments
    static Scanner scnr = new Scanner(System.in);
    static int enemyCount, rate = 60, targetCount;
    static Pad p;
    static Oval o;
    static Text livesLeftText, scoreCountText, lostText;
    static int livesLeftNum = 10, scoreCountNum = 0;
    static int[] directionX;
    static int[] directionY;
    static Random r = new Random();
    static Oval[] enemyArr, targetArr;
    static int width = 1000;
    static int height = 700;

    public static void main(String[] args) {
        //Initial values prompted from the user to run program
        System.out.println("Enter # of enemies: ");
        enemyCount = scnr.nextInt();
        System.out.println("Enter # of targets/food: ");
        targetCount = scnr.nextInt();
        System.out.println("Difficulty? (60 Baby, 80 Adult, 100 Hero, 150 Legend");
        rate = scnr.nextInt();

        //Initialize the needed arrays with the user-inputted values
        targetArr = new Oval[targetCount];
        enemyArr = new Oval[enemyCount];
        directionX = new int[enemyCount];
        directionY = new int[enemyCount];

        p = new Pad(width, height);

        //initialize user/player
        o = new Oval(500,500, 50, 50);
        o.setFillColor(0, 0, 255);

        //populate oval array with targets/foods
        for (int i = 0; i < (targetArr.length); i++) {
            targetArr[i] = new Oval(r.nextInt(750), r.nextInt(650), 25, 25);
            targetArr[i].setFillColor(0, 255, 0);
        }

        //populate the oval array with enemies
        for (int i = 0; i < (enemyArr.length); i++) {
            enemyArr[i] = new Oval(r.nextInt(800), r.nextInt(625), 25, 25);
            enemyArr[i].setFillColor(0, 0, 0);

            //each index in the array populates a separate array for X direction - needed for movement
            for (int j = 0; j < directionX.length; j++){
                directionX[j] = 1;
            }
            //each index in the array populates a separate array for Y direction - needed for movement
            for (int k = 0; k < directionX.length; k++){
                directionY[k] = 1;
            }
        }

        //Initialize and set 'livesleft' text object
        livesLeftText = new Text(" ", 100, 20, 25);
        livesLeftText.setText("Lives: " + livesLeftNum);

        //Initialize and set 'scorecount' text object
        scoreCountText = new Text(" ", 700, 20, 25);
        scoreCountText.setText("Score: " + scoreCountNum);

        //Initialize the text needed to be display at completion. set false.
        lostText = new Text("Game Over", 370, 350, 50);
        lostText.setFillColor(255,0,0);
        lostText.setVisible(false);

        //KeyPressedHandler and KeyReleaseadHandler allows for quicker maneuvering
        p.setKeyPressedHandler(Project2::onKeyPressed);
        p.setKeyReleasedHandler(Project2::onKeyPressed);

        //Tick handler causes onTick to be called and repeated 'rate' amount of times
        p.setTickHandler(Project2::onTick);
        p.setTickRate(rate);
        p.startTimer();

    }

    //method to use when keys pressed on the keyboard.  Only utilizing 'A S W D' keys
    public static void onKeyPressed(Pad pad, String keyText, String km) {
        switch (keyText) {
            case "A":
                if((o.getCenter().getX() > 25)) {
                    o.setCenter(o.getCenter().getX() - 10, o.getCenter().getY());
                    break;
                }
            case "S":
                if((o.getCenter().getY() < 675)) {
                    o.setCenter(o.getCenter().getX(), o.getCenter().getY() + 10);
                    break;
                }
            case "W":
                if((o.getCenter().getY() > 25)) {
                    o.setCenter(o.getCenter().getX(), o.getCenter().getY() - 10);
                    break;
                }
            case "D":
                if((o.getCenter().getX() < 975)) {
                    o.setCenter(o.getCenter().getX() + 10, o.getCenter().getY());
                    break;
                }
        }

    }

    private static void onTick (Pad p,long when){


        //checks to see if user is intersecting target objects
        for (int k = 0; k < targetArr.length; k++) {
            if (o.intersects(targetArr[k])){
                targetArr[k].setCenter(r.nextInt(750), r.nextInt(600));
                scoreCountNum += 1;
                scoreCountText.setText("Score: " + scoreCountNum);

                }
        }

        for (int i = 0; i < enemyArr.length; i++) {

            double x = enemyArr[i].getCenter().getX();
            double y = enemyArr[i].getCenter().getY();

            //bounds for the 'enemy' objects to bounce off the walls
            if (y > 687.5) {
                directionY[i] = -1;
            }

            if (y < 12.5) {
                directionY[i] = 1;
            }

            if (x < 12.5) {
                directionX[i] = 1;
            }

            if (x > 987.5) {
                directionX[i] = -1;
            }
            //enemy object changes direction when hits a bound
            enemyArr[i].setCenter(x + directionX[i] * 2, y + directionY[i] * 2);

            //when user intersects an enemy object, livesLeftNum is updated and enemy respawns elsewhere
            for(int j = 0; j < enemyArr.length; j++) {
                if (o.intersects(enemyArr[j])) {
                    //checks to see if any lives left
                    if (livesLeftNum >= 1) {
                        enemyArr[j].setCenter(r.nextInt(800), r.nextInt(650));
                        livesLeftNum -= 1;
                        livesLeftText.setText("Lives: " + livesLeftNum);
                    } else {
                        //ends game by making lost text visible and making everything else invisible
                        lostText.setVisible(true);
                        for (int k = 0; k < enemyArr.length; k++){
                            enemyArr[k].setVisible(false);
                        }
                        for (int k = 0; k < targetArr.length; k++){
                            targetArr[k].setVisible(false);
                        }
                        o.setVisible(false);
                    }
                }
            }
        }
    }
}