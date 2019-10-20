package game;

public class Settings {
    public static final int[] NETWORK = {21, 20, 20, 3};
    public static long generation = 0;
    public static int maxHealth = 0;
    public static int mutationValue = 100;
    public static int mutationRate = 10;
    public static int sleepTime = 200;
    public static final int population = 30;
    public static boolean newGeneration = false;
    public static boolean justBest = false;

    public static boolean bestWaits = false;
    public static boolean goThroughWalls = false;
    public static boolean inherit = false;
    public static boolean die = true;
    public static boolean showVision = true;
    public static boolean showJustBest = true;
    public static boolean humanPlayer = false;
    public static boolean bot = false;
    public static boolean stopByCollision = false;

    public static boolean trainWithBackprob = false;
    public static double learningRate = 0.3;
}
