package workexpIT.merlin.graphics;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.listeners.MouseListener;
import workexpIT.merlin.tiles.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by ict11 on 2016-02-22.
 */
public class JavaDrawer extends JPanel implements Runnable {

    public static JFrame frame;
    public static int offsetX = 0;
    public static int offsetY = 0;
    public static int imageSize = 16;
    public static int ww = 1200;
    public static int wh = 800;
    public static float scale = 2;

    public static int editorMenuSize = 200;

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

    private void moveScreen() {
        if (Merlin.keyListener.upPressed || Merlin.keyListener.upTemp) {
            JavaDrawer.offsetY = JavaDrawer.offsetY + 1 * JavaDrawer.imageSize;
            Merlin.keyListener.upTemp = false;
        }
        if (Merlin.keyListener.leftPressed || Merlin.keyListener.leftTemp) {
            JavaDrawer.offsetX = JavaDrawer.offsetX + 1 * JavaDrawer.imageSize;
            Merlin.keyListener.leftTemp = false;
        }
        if (Merlin.keyListener.downPressed || Merlin.keyListener.downTemp) {
            JavaDrawer.offsetY = JavaDrawer.offsetY - 1 * JavaDrawer.imageSize;
            Merlin.keyListener.downTemp = false;
        }
        if (Merlin.keyListener.rightPressed || Merlin.keyListener.rightTemp) {
            JavaDrawer.offsetX = JavaDrawer.offsetX - 1 * JavaDrawer.imageSize;
            Merlin.keyListener.rightTemp = false;
        }

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
            moveScreen();
            drawGrid(g);
        }
        if (Merlin.mode.equals(Merlin.Mode.GAME)) {
            smoothOffset();
        }
        if (Merlin.mode.equals(Merlin.Mode.GAME) || Merlin.mode.equals(Merlin.Mode.EDITOR)) {
            drawTiles(g);
            drawEntities(g);
        }
        if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
            drawEditorMenu(g);
        }
        if (Merlin.mode.equals(Merlin.Mode.BATTLE)) {
            drawBackground(g);
        }
    }

    private void drawBackground(Graphics g) {
        BufferedImage image = ImageReader.loadImage("resources/graphics/backgrounds/"+WorldData.mapName+".png");
        g.drawImage(image,0,0,null);
    }

    private void drawEditorMenu(Graphics g) {
        int x = 0;
        int y = 0;
        g.setColor(Color.WHITE);
        g.fillRect(frame.getWidth()-editorMenuSize,0,editorMenuSize,frame.getHeight());
        for (int i = 0; i < Reference.tileIds.length; i ++) {
            Tile tile = null;
            try {
                tile = (Tile) Class.forName("workexpIT.merlin.tiles."+ Reference.tileIds[i]).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            BufferedImage image = scale(tile.getTexture(),4,4);

            g.drawImage(image,(int)(x * JavaDrawer.imageSize*4 + frame.getWidth()-editorMenuSize + 10*x + 10),(int)(y * JavaDrawer.imageSize*4 + 10*y + 10),null);
            if (x == 0) {x = 1;} else {x = 0;y=y+1;}
        }
        g.drawImage(ImageReader.loadImage("resources/graphics/save.png"), frame.getWidth()-10-32, 10,null);
    }

    private void smoothOffset() {
        //try {

        int newOffsetX = (int) (-WorldData.getPlayer().getX()*imageSize-WorldData.getPlayer().getSprites()[0].getWidth()/2);
        int newOffsetY = (int) (-WorldData.getPlayer().getY()*imageSize+WorldData.getPlayer().getSprites()[0].getHeight()/2);


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
                BufferedImage sprite = scale(WorldData.entities.get(i).getSprites()[0],scale,scale);
                int x = WorldData.entities.get(i).getX();
                int y = WorldData.entities.get(i).getY();
                int w = sprite.getWidth();
                int h = sprite.getHeight();
                g.drawImage(sprite,(int)((x * imageSize + offsetX)*scale)+ww/2, (int)((y * imageSize - h/scale + offsetY)*scale)+wh/2,null);
            } else {
                BufferedImage sprite = scale(WorldData.entities.get(i).getSprites()[WorldData.entities.get(i).spriteId],scale,scale);
                int x = WorldData.entities.get(i).getX();
                int y = WorldData.entities.get(i).getY();
                int w = sprite.getWidth();
                int h = sprite.getHeight();
                g.drawImage(sprite,(int)((x * imageSize + offsetX)*scale)+ww/2, (int)((y * imageSize - h/scale + offsetY)*scale)+wh/2,null);
            }
        }
    }

    private void drawTiles(Graphics g) {
        for (int a = 0; a < WorldData.tiles.length; a++) {
            for (int b = 0; b < WorldData.tiles[a].length; b++) {
                if (WorldData.tiles[a][b] != null) {
                    BufferedImage image = scale(WorldData.tiles[a][b].getTexture(),scale,scale);
                    g.drawImage(image,(int)((a * imageSize + offsetX)*scale)+ww/2, (int)((b * imageSize - imageSize + offsetY)*scale)+wh/2,null);
                }
            }
        }
    }

    public void drawGrid(Graphics g) {
        for (int x = 0; x < Reference.mapSize+1; x++) {
            g.fillRect((int) (((x * imageSize + offsetX) * scale) + ww / 2) - 1, (int) (((0 * imageSize - imageSize + offsetY) * scale) + wh / 2), 2, (int) (Reference.mapSize * imageSize * scale));
        }
        for (int y = 0; y < Reference.mapSize+1; y++) {
            g.fillRect((int) (((0 * imageSize + offsetX) * scale) + ww / 2), (int)(((y * imageSize - imageSize + offsetY) * scale) + wh / 2)-1, (int) ((Reference.mapSize * imageSize) * scale), 2);
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
            drawer.addMouseListener(new MouseListener());
            frame.setContentPane(drawer);

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void scaleUp() {
        scale = scale + 0.25f;
        Output.write("Zooming In");
    }
    public static void scaleDown() {
        scale = scale - 0.25f;
        Output.write("Zooming Out");
    }

    public static BufferedImage scale(BufferedImage sbi, double fWidth, double fHeight) {
        BufferedImage dbi = null;
        if(sbi != null) {
            int imageType = sbi.getType();
            int dWidth = sbi.getWidth();
            int dHeight = sbi.getHeight();
            dbi = new BufferedImage((int)(dWidth*fWidth), (int)(dHeight*fHeight), imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }

    public static void zoom() {

    }
}
