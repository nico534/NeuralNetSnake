import Mtrix.Matrix;
import game.Snake;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SnakeTest {

    @Test
    @DisplayName("Save Load Snake test")
    public void saveLoadTest() throws IOException {

        //load Snake
        Snake s1 = new Snake(0, "F:/IntelliJProjects/Snake");

        File saveFile = new File("F:/Bibliothek/Desktop/Snake");
        //save Snake
        s1.saveNN(saveFile);

        Snake s2 = new Snake(0, saveFile.getAbsolutePath());

        Matrix[] ws1 = s1.getHead().getNN().getWeights();
        Matrix[] ws2 = s2.getHead().getNN().getWeights();

        Matrix[] bs1 = s1.getHead().getNN().getBiases();
        Matrix[] bs2 = s2.getHead().getNN().getBiases();

        assertEquals(ws1.length, ws2.length);
        assertEquals(bs1.length, bs2.length);

        for(int i = 0; i < ws1.length; i++){
            assertEquals(ws1[i].getRows(), ws2[i].getRows());
            assertEquals(ws1[i].getCols(), ws2[i].getCols());
            for(int j = 0; j < ws1[i].getRows(); j++){
                for(int k = 0; k < ws1[i].getCols(); k++){
                    assertEquals(ws1[i].get(j,k), ws2[i].get(j,k));
                }
            }
        }

        for(int i = 0; i < bs1.length; i++){
            assertEquals(bs1[i].getRows(), bs2[i].getRows());
            assertEquals(bs1[i].getCols(), bs2[i].getCols());
            for(int j = 0; j < bs1[i].getRows(); j++){
                for(int k = 0; k < bs1[i].getCols(); k++){
                    assertEquals(bs1[i].get(j,k), bs2[i].get(j,k));
                }
            }
        }


    }
}
