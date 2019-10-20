package gui;

import actions.KeyHandler;
import actions.Main;
import clock.GameClock;
import game.GameValues;
import game.Settings;
import game.Snake;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.util.Set;

public class Gui {
    /*JFrame jf;
    Draw d;
    Snake[] snakes;

    public Gui(Snake[] snakes){
        this.snakes = snakes;
    }

    public void create(){
        jf = new JFrame("Snake");
        jf.setSize(GameValues.GAME_WIDTH.getValue() + 200, GameValues.GAME_HEIGHT.getValue());
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setLayout(null);
        jf.setResizable(true);

        JPanel main = new JPanel();
        main.setLayout(new FlowLayout(FlowLayout.LEADING));

        main.setSize(GameValues.GAME_WIDTH.getValue() + 100, GameValues.GAME_HEIGHT.getValue());

        //KeyBindings
        ActionMap actionMap = main.getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = main.getInputMap(condition);

        String wKey = "VK_W";
        String aKey = "VK_A";
        String sKey = "VK_S";
        String dKey = "VK_D";

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), wKey);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), aKey);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), sKey);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), dKey);

        actionMap.put(wKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakes[0].goUp();
            }
        });
        actionMap.put(aKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakes[0].goLeft();
            }
        });
        actionMap.put(sKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakes[0].goDown();
            }
        });
        actionMap.put(dKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakes[0].goRight();
            }
        });


        JPanel options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.PAGE_AXIS));

        d = new Draw(snakes);
        d.setBounds(0, 0, GameValues.GAME_WIDTH.getValue(), GameValues.GAME_HEIGHT.getValue());
        d.setVisible(true);
        d.setSize(new Dimension(GameValues.GAME_WIDTH.getValue(), GameValues.GAME_HEIGHT.getValue()));
        d.setPreferredSize(new Dimension(GameValues.GAME_WIDTH.getValue(), GameValues.GAME_HEIGHT.getValue()));
        d.setMinimumSize(new Dimension(GameValues.GAME_WIDTH.getValue(), GameValues.GAME_HEIGHT.getValue()));

        JPanel drawPanel = new JPanel();
        drawPanel.setLayout(new BorderLayout());
        drawPanel.add(d, BorderLayout.CENTER);
        drawPanel.setSize(new Dimension(GameValues.GAME_WIDTH.getValue(), GameValues.GAME_HEIGHT.getValue()));
        drawPanel.setPreferredSize(new Dimension(GameValues.GAME_WIDTH.getValue(), GameValues.GAME_HEIGHT.getValue()));
        drawPanel.setMinimumSize(new Dimension(GameValues.GAME_WIDTH.getValue(), GameValues.GAME_HEIGHT.getValue()));

        JButton stop = new JButton("Stop");
        stop.setBounds(GameValues.GAME_WIDTH.getValue() + 5,10, 80, 20);
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameClock.running = false;
            }
        });

        JSlider sleepTime = new JSlider(0,300, Settings.sleepTime);
        sleepTime.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Settings.sleepTime = sleepTime.getValue();
            }
        });
        sleepTime.setBounds(GameValues.GAME_WIDTH.getValue() + 5, 30, 80, 30);

        JSlider mutation = new JSlider(5, 400, Settings.mutationValue);
        mutation.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Settings.mutationValue = mutation.getValue();
            }
        });
        mutation.setBounds(GameValues.GAME_WIDTH.getValue() + 5, 60, 80, 30);

        JSlider mutRate = new JSlider(1, 50, Settings.mutationRate);
        mutRate.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Settings.mutationRate = mutRate.getValue();
            }
        });

        mutRate.setBounds(GameValues.GAME_WIDTH.getValue() + 5, 100, 80, 30);

        stop.setBounds(GameValues.GAME_WIDTH.getValue() + 5,10, 80, 20);
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameClock.running = false;
            }
        });

        options.add(stop);
        options.add(sleepTime);
        options.add(mutation);
        options.add(mutRate);
        System.out.println(options.getSize().width + " " + options.getSize().height);
        System.out.println(drawPanel.getSize().width+ " " + drawPanel.getSize().height);

        main.add(options);
        main.add(drawPanel);
        jf.add(main);
        jf.requestFocus();
        jf.setVisible(true);
    }

     */
}
