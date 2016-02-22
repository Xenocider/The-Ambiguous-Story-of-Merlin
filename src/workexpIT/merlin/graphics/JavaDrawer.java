package workexpIT.merlin.graphics;

import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.data.WorldData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ict11 on 2016-02-22.
 */
public class JavaDrawer extends JPanel implements Runnable {

    static JFrame frame;
    public static int offsetX = 0;
    public static int offsetY = 0;
    public static int imageSize = 64;
    public static int ww = 1200;
    public static int wh = 800;

    public static void init() {
        createWindow();
    }

    @Override
    public void run() {
        //clearScreen();
        //drawTiles(frame.getGraphics());
        //drawEntities(frame.getGraphics());
        frame.repaint();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        smoothOffset();
        drawTiles(g);
        drawEntities(g);
    }

    private void smoothOffset() {
        //try {
            int newOffsetX = (-WorldData.getPlayer().getX() + ww / 2 / imageSize) * imageSize;
            int newOffsetY = (-WorldData.getPlayer().getY() + wh / 2 / imageSize) * imageSize;

            Output.write(newOffsetX+" " + offsetX);

            if (newOffsetX > offsetX) {
                offsetX = offsetX + 1;
            }
            if (newOffsetY > offsetY) {
                offsetY = offsetY + 1;
            }
            if (newOffsetX < offsetX) {
                offsetX = offsetX - 1;
            }
            if (newOffsetY < offsetY) {
                offsetY = offsetY - 1;
            }
        //}
        //catch (Exception e) {}
    }

    private void clearScreen() {
        frame.getGraphics();
    }

    private void drawEntities(Graphics g) {
        //Output.write("Drawing Entities");
        for (int i = 0; i < WorldData.entities.size(); i++) {
            if (WorldData.entities.get(i).spriteId == -1) {
                BufferedImage sprite = WorldData.entities.get(i).getSprites()[0];
                int x = WorldData.entities.get(i).getX();
                int y = WorldData.entities.get(i).getY();
                int w = sprite.getWidth();
                int h = sprite.getHeight();
                g.drawImage(sprite,x * w + offsetX, y * h - h + offsetY,null);
            } else {
                BufferedImage sprite = WorldData.entities.get(i).getSprites()[WorldData.entities.get(i).spriteId];
                int x = WorldData.entities.get(i).getX();
                int y = WorldData.entities.get(i).getY();
                int w = sprite.getWidth();
                int h = sprite.getHeight();
                g.drawImage(sprite,x * w + offsetX, y * h - h + offsetY,null);
            }
        }
    }

    private void drawTiles(Graphics g) {
        for (int a = 0; a < WorldData.tiles.length; a++) {
            for (int b = 0; b < WorldData.tiles[a].length; b++) {
                if (WorldData.tiles[a][b] != null) {
                    g.drawImage(WorldData.tiles[a][b].texture,a * imageSize + offsetX, b * imageSize - imageSize + offsetY,null);
                }
            }
        }
    }

    private static void createWindow() {
        try {
            frame = new JFrame("The Ambiguous Story of Merlin");
            frame.setSize(ww,wh);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            JavaDrawer drawer = new JavaDrawer();
            drawer.setSize(ww,wh);
            drawer.setFocusable(true);
            drawer.addKeyListener(Merlin.keyListener);
            frame.setContentPane(drawer);

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }


}
