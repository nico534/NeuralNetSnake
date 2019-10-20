package game;


import Brain.NeuralNetwork;
import actions.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Snake {

    private static int nrDiet = 0;
    private UIHead head;
    private ArrayList<Tail> tails;
    private boolean waitToMove;
    private int health;
    private PickUp pickup;

    private int snakeNr;
    private Color headColor;
    private Color tailColor;
    private Color foodColor;
    private byte[][] board;

    private boolean isBest = false;
    private long bestFor;


    public Snake(int snakeNr) {
        this.snakeNr = snakeNr;
        Random r = new Random();

        this.tailColor = new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1.0);
        this.headColor = new Color(tailColor.getRed(), tailColor.getGreen(), tailColor.getBlue(), 0.4);
        this.foodColor = new Color(0, r.nextDouble(), 0, 1.0);

        this.head = new UIHead();
        init();
    }

    public Snake(int snakeNr, String nnPath) throws IOException {
        File file = new File(nnPath + "/snake" + snakeNr + ".csv");
        if(Settings.justBest){
            file = new File(nnPath + "/bestSnake.csv");
        }
        this.snakeNr = snakeNr;
        Random r = new Random();
        this.headColor = new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1.0);
        this.tailColor = new Color(headColor.getRed(), headColor.getGreen(), headColor.getBlue(), 0.5);
        this.foodColor = new Color(0, r.nextDouble(), 0, 1.0);
        this.head = new UIHead(file);
        init();
    }

    private void init() {
        this.waitToMove = false;
        this.tails = new ArrayList<Tail>();
        this.health = GameValues.START_HEALTH.getValue() + snakeNr;
        this.board = new byte[GameValues.NR_OF_FIELDS.getValue()][GameValues.NR_OF_FIELDS.getValue()];
        this.pickup = new PickUp();
        checkNewPickupPosition();
        this.board[head.getX()][head.getY()] = 1;
        this.board[pickup.getX()][pickup.getY()] = 2;
    }

    private void addTail() {
        if (tails.size() == 0) {
            tails.add(new Tail(head.getX(), head.getY()));
        } else {
            tails.add(new Tail(tails.get(tails.size() - 1).getX(), tails.get(tails.size() - 1).getY()));
        }
    }


    public void saveNN(File f) {
        File file = new File(f.getAbsolutePath() + "/snake" + snakeNr + ".csv");
        try {
            this.head.getNN().saveNeuralNetwork(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(isBest){
            file = new File(f.getAbsolutePath() + "/bestSnake.csv");
            try {
                this.head.getNN().saveNeuralNetwork(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearSnake(Snake s1, Snake s2) {
        head.resetPosition();
        init();
        if (isBest) {
            Settings.maxHealth = 0;
        } else if (Settings.inherit && !Settings.humanPlayer) {
            head.inherit(s1, s2);
        }
        if (nrDiet > Settings.population) {
            nrDiet = 0;
            Settings.generation++;
        } else {
            nrDiet++;
        }
    }

    public void move() {


        //Best snake waits
        if (getFitness() > Settings.maxHealth) {
            Settings.maxHealth = getFitness();
            this.isBest = true;
            bestFor = Settings.generation;
        } else if (getFitness() < Settings.maxHealth) {
            this.isBest = false;
        }

        // Just best for 50 Generations
        if (isBest && bestFor + 50 > Settings.generation && Settings.bestWaits) {
            return;
        }

        //move tails
        if (tails.size() > 1) {
            for (int i = tails.size() - 1; i > 0; i--) {
                if (tails.get(i).isWait()) {
                    tails.get(i).setWait(false);
                } else {
                    // if last tale delete position
                    if (i == tails.size() - 1) {
                        board[tails.get(i).getX()][tails.get(i).getY()] = 0;
                    }
                    tails.get(i).setX(tails.get(i - 1).getX());
                    tails.get(i).setY(tails.get(i - 1).getY());
                }
            }
        }

        //Move first Tail to head
        if (tails.size() >= 1) {
            if (tails.get(0).isWait()) {
                tails.get(0).setWait(false);
            } else {
                // if last tale delete position
                if (tails.size() == 1) {
                    board[tails.get(0).getX()][tails.get(0).getY()] = 0;
                }
                tails.get(0).setX(head.getX());
                tails.get(0).setY(head.getY());
            }
        }

        // if just head delete head Position
        if (tails.size() == 0) {
            board[head.getX()][head.getY()] = 0;
        }
        head.desideDirektion(board);
        // Move Head
        switch (head.getDir()) {
            case RIGHT:

                head.setX(setHeadTo(head.getX() + 1));
                break;
            case LEFT:
                head.setX(setHeadTo(head.getX() - 1));
                break;
            case UP:
                head.setY(setHeadTo(head.getY() - 1));
                break;
            case DOWN:
                head.setY(setHeadTo(head.getY() + 1));
                break;
        }
        if (!checkCollision()) {
            //just the head has a new position
            board[head.getX()][head.getY()] = 1;
        }

        if (getFitness() == Settings.maxHealth) {
            if (Settings.die) {
                health-= 2;
            }
            Settings.maxHealth = getFitness();
        } else if (Settings.die) {
            health-= 2;
        }
        waitToMove = false;
        head.updateVision(board);
    }

    private int setHeadTo(int x) {
        if (Settings.goThroughWalls) {
            if (x < 0) {
                return GameValues.NR_OF_FIELDS.getValue() - 1;
            } else if (x > GameValues.NR_OF_FIELDS.getValue() - 1) {
                return 0;
            }
        }

        return x;
    }

    public void goDown() {
        if (!(head.getDir() == Dir.UP) && !waitToMove) {
            head.setDir(Dir.DOWN);
            waitToMove = true;
        }
    }

    public void goUp() {
        if (!(head.getDir() == Dir.DOWN) && !waitToMove) {
            head.setDir(Dir.UP);
            waitToMove = true;
        }
    }

    public void goLeft() {
        if (!(head.getDir() == Dir.RIGHT) && !waitToMove) {
            head.setDir(Dir.LEFT);
            waitToMove = true;
        }
    }

    public void goRight() {
        if (!(head.getDir() == Dir.LEFT) && !waitToMove) {
            head.setDir(Dir.RIGHT);
            waitToMove = true;
        }
    }

    public ArrayList<Tail> getTails() {
        return tails;
    }

    public UIHead getHead() {
        return head;
    }

    public PickUp getPickup() {
        return pickup;
    }

    public void pickupCollision() {
        if (head.getX() == pickup.getX() && head.getY() == pickup.getY()) {
            pickup.reset();
            checkNewPickupPosition();
            addTail();
            this.health += GameValues.EAT_BONUS.getValue();
            this.board[pickup.getX()][pickup.getY()] = 2;
        }
    }

    private void checkNewPickupPosition() {

        for (Tail t : tails) {
            if (t.getX() == pickup.getX() && t.getY() == pickup.getY()) {
                pickup.reset();
                checkNewPickupPosition();
                return;
            }
        }
        if (head.getX() == pickup.getX() && head.getY() == pickup.getY()) {
            pickup.reset();
            checkNewPickupPosition();
        }
    }

    public boolean checkCollision() {

        if (head.getX() < 0 || head.getX() > GameValues.NR_OF_FIELDS.getValue() - 1 || head.getY() < 0 || head.getY() > GameValues.NR_OF_FIELDS.getValue() - 1) {
            return true;
        }

        for (Tail t : tails) {
            if (t.getX() == head.getX() && t.getY() == head.getY() && !t.isWait()) {
                return true;
            }
        }
        if (health < 1) {
            return true;
        }
        return false;
    }

    private Point ptc(int x, int y) {
        Point p = new Point(0, 0);
        p.x = x * GameValues.FIELD_SIZE.getValue() + GameValues.X_OFF.getValue();
        p.y = y * GameValues.FIELD_SIZE.getValue() + GameValues.Y_OFF.getValue();

        return p;
    }

    public int getFitness() {
        if (health < 1) {
            return 0;
        }
        return 20 * (tails.size() + 1) + 3 * health;
    }

    public Color getHeadColor(){
        return headColor;
    }

    public Color getTailColor(){
        return tailColor;
    }

    public Color getFoodColor(){
        return foodColor;
    }


    public Point[][] getVision() {
        int[][] side;
        if (head.getDir() == Dir.UP) {
            side = DirMutilayer.UP.getValue();
        } else if (head.getDir() == Dir.DOWN) {
            side = DirMutilayer.DOWN.getValue();
        } else if (head.getDir() == Dir.RIGHT) {
            side = DirMutilayer.RIGHT.getValue();
        } else {
            side = DirMutilayer.LEFT.getValue();
        }
        Point[][] vision = new Point[7][3];
        for (int i = 0; i < 7; i++) {
            // seen wall pos
            vision[i][0] = new Point(head.getX() + side[i][0] * head.getDrowVision()[i][0], head.getY() + side[i][1] * head.getDrowVision()[i][0]);
            if (head.getDrowVision()[i][1] != 0) {
                // seen tail pos
                vision[i][1] = new Point(head.getX() + side[i][0] * head.getDrowVision()[i][1], head.getY() + side[i][1] * head.getDrowVision()[i][1]);
            }
            if (head.getDrowVision()[i][2] != 0) {
                //seen food pos
                vision[i][2] = new Point(head.getX() + side[i][0] * head.getDrowVision()[i][2], head.getY() + side[i][1] * head.getDrowVision()[i][2]);
            }
        }
        return vision;
    }

    public FakeSnake createFakeSnake(){
        return new FakeSnake(headColor, tailColor, foodColor, pickup.copy(), new Point(head.getX(), head.getY()), getVision(), head.getVisionStrength(), new ArrayList<>(tails));
    }
}