package game;

public class Settings {
    public static final int[] NETWORK = {21, 20, 20, 3};
    public static long generation = 0;
    public static int maxHealth = 0;
    public static int mutationValue = 100;
    public static int mutationRate = 10;
    public static int sleepTime = 100;

    public static final boolean bestWaits = false;
    public static final boolean goThroughWalls = false;
    public static final boolean inherit = false;
    public static final int population = 30;
    public static final boolean die = true;
    public static boolean showView = true;
    public static boolean showJustBest = true;
    public static boolean humanPlayer = false;
    public static boolean bot = false;
    public static boolean save = false;
    public static boolean stopByCoolssion = false;
    public static boolean justBest = false;

    public static boolean newGeneration = false;

    public static boolean trainWithBackprob = false;
    public static double learningRate = 0.3;
}
