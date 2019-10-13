package clock;
import game.GameValues;
import game.Settings;
import game.Snake;

import javax.swing.*;
import java.util.Random;

public class GameClock extends Thread {
    public static boolean running = true;

    Snake[] snakes;
    Random r = new Random();

    public GameClock(Snake[] snakes){
        super();
        this.snakes = snakes;
    }

    public void run(){
        while (running){
            try {
                sleep(Settings.sleepTime);
                for(Snake s: snakes) {
                    s.move();
                    if (s.checkCollision()) {
                        if(Settings.stopByCoolssion) {
                            running = false;
                        } else {
                            Snake[] fittest = getFittest();
                            s.clearSnake(fittest[0], fittest[1]);
                        }
                    }
                    s.pickupCollision();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(Settings.save) {
            JFileChooser jfc = new JFileChooser("F:/IntelliJProjects/Snake");
            jfc.showOpenDialog(null);
            for (Snake s : snakes) {
                s.saveNN(jfc.getCurrentDirectory());
            }
        }
    }

    private Snake[] getFittest(){
        Snake[] fittest = new Snake[2];

        int[][] snakeFitness = new int[snakes.length][2];

        for(int i = 0; i < snakes.length; i++){
            snakeFitness[i][0] = snakes[i].getFitness();
            snakeFitness[i][1] = i;
        }

        bubblesort(snakeFitness);
        int index = (int)Math.round(Math.pow(r.nextGaussian(),2))%snakes.length;
        fittest[0] = snakes[snakeFitness[index][1]];
        int index2 = (int)Math.round(Math.pow(r.nextGaussian(),2))%snakes.length;
        if(index2 == index){
            index2 = (index2+1)%snakes.length;
        }
        fittest[1] = snakes[snakeFitness[index2][1]];
        return fittest;
    }

    private void bubblesort(int[][] zusortieren) {
        int[] temp;
        for(int i=1; i<zusortieren.length; i++) {
            for(int j=0; j<zusortieren.length-i; j++) {
                if(zusortieren[j][0]<zusortieren[j+1][0]) {
                    temp=zusortieren[j];
                    zusortieren[j]=zusortieren[j+1];
                    zusortieren[j+1]=temp;
                }
            }
        }
    }
}
