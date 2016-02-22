package workexpIT.merlin.graphics;

import oracle.jvm.hotspot.jfr.JFR;
import workexpIT.merlin.Output;
import workexpIT.merlin.data.WorldData;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ict11 on 2016-02-22.
 */
public class JavaDrawer implements Runnable {

    static JFrame frame;
    public static int offsetX = 0;
    public static int offsetY = 0;
    public static int imageSize = 64;

    public static void init() {
        createWindow();
    }

    @Override
    public void run() {
        drawTiles();
    }

    private void drawTiles() {
        for (int a = 0; a < WorldData.tiles.length; a++) {
            for (int b = 0; b < WorldData.tiles[a].length; b++) {
                if (WorldData.tiles[a][b] != null) {
                    frame.getGraphics().drawImage(WorldData.tiles[a][b].texture,a * imageSize + offsetX, b * imageSize + offsetY,null);
                }
            }
        }
    }

    private static void createWindow() {
        try {
            frame = new JFrame("The Ambiguous Story of Merlin");
            frame.setSize(300,400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }


}
