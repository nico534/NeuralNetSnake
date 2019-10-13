package actions;

import game.Snake;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler extends KeyAdapter {
    private Snake s;

    public KeyHandler(Snake s){
        this.s = s;
    }

    public void keyPressed(KeyEvent e) {
        System.out.println("Detect press: " + e.getKeyCode());
        switch (e.getKeyCode()){
            case KeyEvent.VK_W:
                s.goUp();
                return;
            case KeyEvent.VK_A:
                s.goLeft();
                return;
            case KeyEvent.VK_S:
                s.goDown();
                return;
            case KeyEvent.VK_D:
                s.goRight();
                return;
        }
    }
}
