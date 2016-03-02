package workexpIT.merlin.graphics;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
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
        long startTime = System.currentTimeMillis();
        super.paintComponent(g);
        zoomIn();
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
            drawBattleEntities(g);
            drawStatusBars(g);
            drawBattleMenu(g);
        }
        long endTime = System.currentTimeMillis();
        //Output.write("Took " + (endTime-startTime) + "ms to render this frame");
        //Output.write("FPS: " + Math.pow((endTime-startTime),-1)*1000);
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
        g.drawImage(GameLoop.save, frame.getWidth()-10-32, 10,null);
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
                g.drawImage(sprite,(int)((x * imageSize + offsetX)*scale)+frame.getWidth()/2, (int)((y * imageSize - h/scale + offsetY)*scale)+frame.getHeight()/2,null);
            } else {
                BufferedImage sprite = scale(WorldData.entities.get(i).getSprites()[WorldData.entities.get(i).spriteId],scale,scale);
                int x = WorldData.entities.get(i).getX();
                int y = WorldData.entities.get(i).getY();
                int w = sprite.getWidth();
                int h = sprite.getHeight();
                g.drawImage(sprite,(int)((x * imageSize + offsetX)*scale)+frame.getWidth()/2, (int)((y * imageSize - h/scale + offsetY)*scale)+frame.getHeight()/2,null);
            }
        }
    }

    private void drawTiles(Graphics g) {
        for (int a = 0; a < WorldData.tiles.length; a++) {
            for (int b = 0; b < WorldData.tiles[a].length; b++) {
                if (WorldData.tiles[a][b] != null) {
                    BufferedImage image = scale(WorldData.tiles[a][b].getTexture(),scale,scale);
                    g.drawImage(image,(int)((a * imageSize + offsetX)*scale)+frame.getWidth()/2, (int)((b * imageSize - imageSize + offsetY)*scale)+frame.getHeight()/2,null);
                }
            }
        }
    }

    public void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        for (int x = 0; x < WorldData.mapSizeX+1; x++) {
            g.fillRect((int) (((x * imageSize + offsetX) * scale) + frame.getWidth() / 2) - 1, (int) (((0 * imageSize - imageSize + offsetY) * scale) + frame.getHeight() / 2), 2, (int) (WorldData.mapSizeX * imageSize * scale));
        }
        for (int y = 0; y < WorldData.mapSizeY+1; y++) {
            g.fillRect((int) (((0 * imageSize + offsetX) * scale) + frame.getWidth() / 2), (int)(((y * imageSize - imageSize + offsetY) * scale) + frame.getHeight() / 2)-1, (int) ((WorldData.mapSizeY * imageSize) * scale), 2);
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


    //BATTLE STUFF IS DOWN HERE

    //Image Object Locations
    public static int playerX;
    public static int playerY;
    public static int enemyX;
    public static int enemyY;
    public static int playerBarX;
    public static int playerBarY;
    public static int enemyBarX;
    public static int enemyBarY;
    public static int menuX;
    public static int menuY;
    public static int fightX;
    public static int fightY;
    public static int bagX;
    public static int bagY;
    public static int restX;
    public static int restY;
    public static int attack1X;
    public static int attack1Y;
    public static int attack2X;
    public static int attack2Y;
    public static int attack3X;
    public static int attack3Y;
    public static int attack4X;
    public static int attack4Y;
    public static int attack5X;
    public static int attack5Y;
    public static int attack6X;
    public static int attack6Y;

    //Offsets
    public static int playerOffsetFromBottom = -300;
    public static int playerOffsetFromSide = 20;
    public static int enemyOffsetFromTop = 100;
    public static int enemyOffsetFromSide = 20;
    public static int playerBarOffsetFromBottom = 325;
    public static int playerBarOffsetFromSide = 20;
    public static int enemyBarOffsetFromTop = 50;
    public static int enemyBarOffsetFromSide = 20;
    public static int healthBarXOffset = 10;
    public static int healthBarYOffset = 10;
    public static int manaBarXOffset = 10;
    public static int manaBarYOffset = 20;
    public static int fightButtonXOffset = 100;
    public static int fightButtonYOffset = 50;
    public static int bagButtonXOffset = 100;
    public static int bagButtonYOffset = 150;
    public static int restButtonXOffset = 100;
    public static int restButtonYOffset = 50;
    public static int attackButtonXOffset = 100;
    public static int attackButtonYOffset = 50;

    //Sizes
    public static int manaBarLength = 100;
    public static int healthBarLength = 100;
    public static int menuBackgroundHeight = 300;
    public static int fightButtonWidth;
    public static int fightButtonHeight = 200;
    public static int otherButtonWidth;
    public static int otherButtonHeight = 90;
    public static int attackButtonWidth;
    public static int attackButtonHeight = 90;

    public static boolean pfaint = false;
    public static boolean efaint = false;


    private void drawBackground(Graphics g) {
        g.drawImage(WorldData.battleBackground,0,0,null);
    }

    private void drawBattleEntities(Graphics g) {

        if(efaint) {
            BattleAnimator.enemyOffsetY = BattleAnimator.enemyOffsetY + 8;
        }
        if (pfaint) {
            BattleAnimator.playerOffsetY = BattleAnimator.playerOffsetY + 8;
        }

        BufferedImage player = WorldData.getPlayer().battleSprite;
        BufferedImage enemy = GameLoop.enemy.battleSprite;
        playerX = playerOffsetFromSide;
        playerY = JavaDrawer.frame.getHeight() - player.getHeight() - playerOffsetFromBottom + BattleAnimator.playerOffsetY;
        enemyX = JavaDrawer.frame.getWidth() - enemy.getWidth() - enemyOffsetFromSide;
        enemyY = enemyOffsetFromTop + BattleAnimator.enemyOffsetY;
        g.drawImage(enemy, enemyX, enemyY,null);
        g.drawImage(player, playerX,playerY,null);
    }

    private void drawStatusBars(Graphics g) {
        BufferedImage statusBar = GameLoop.statusBar;

        playerBarX = playerBarOffsetFromSide;
        playerBarY = JavaDrawer.frame.getHeight()-playerBarOffsetFromBottom - statusBar.getHeight();
        enemyBarX = JavaDrawer.frame.getWidth()-enemyBarOffsetFromSide - statusBar.getWidth();
        enemyBarY = enemyBarOffsetFromTop;

        g.drawImage(statusBar,playerBarX,playerBarY,null);
        g.drawImage(statusBar, enemyBarX, enemyBarY, null);

        g.setColor(Color.GREEN);
        g.fillRect(playerBarX + healthBarXOffset,playerBarY + healthBarYOffset,WorldData.getPlayer().health*WorldData.getPlayer().healthMax/healthBarLength,5);
        g.fillRect(enemyBarX + healthBarXOffset,enemyBarY + healthBarYOffset,GameLoop.enemy.health*GameLoop.enemy.healthMax/healthBarLength,5);

        g.setColor(Color.BLUE);
        g.fillRect(playerBarX + manaBarXOffset,playerBarY + manaBarYOffset,WorldData.getPlayer().mana*WorldData.getPlayer().manaMax/manaBarLength,5);
        g.fillRect(enemyBarX + manaBarXOffset,enemyBarY + manaBarYOffset,GameLoop.enemy.mana*GameLoop.enemy.manaMax/manaBarLength,5);
    }

    private void drawBattleMenu(Graphics g) {
        BufferedImage menuBackground = GameLoop.menu;
        menuBackground = scaleToSize(menuBackground, JavaDrawer.frame.getWidth(),menuBackgroundHeight);
        menuX = 0;
        menuY = JavaDrawer.frame.getHeight() - menuBackground.getHeight()-20;
        g.drawImage(menuBackground,menuX,menuY,null);

        if (!GameLoop.fightMenu) {
            fightButtonWidth = (int) (JavaDrawer.frame.getWidth()*0.60);
            BufferedImage fightBackground = scaleToSize(GameLoop.button,fightButtonWidth,fightButtonHeight);
            fightX = menuX + fightButtonXOffset;
            fightY = menuY + fightButtonYOffset;
            g.drawImage(fightBackground,fightX ,fightY , null);

            otherButtonWidth = (int) (JavaDrawer.frame.getWidth()*0.20);
            BufferedImage buttonBackground = scaleToSize(GameLoop.button,otherButtonWidth,otherButtonHeight);

            bagX = menuX+menuBackground.getWidth()-buttonBackground.getWidth()-bagButtonXOffset;
            bagY =  menuY+bagButtonYOffset;
            g.drawImage(buttonBackground,bagX ,bagY, null);

            restX = menuX+menuBackground.getWidth()-buttonBackground.getWidth()-restButtonXOffset;
            restY =  menuY+restButtonYOffset;
            g.drawImage(buttonBackground, restX, restY, null);
        }
        else {
            attackButtonWidth = (int)(JavaDrawer.frame.getWidth() * 0.25);
            BufferedImage attackBackground = scaleToSize(GameLoop.button, attackButtonWidth, attackButtonHeight);

            attack1X = menuX + attackButtonXOffset + (attackButtonWidth + 20)*0;
            attack1Y =  menuY + attackButtonYOffset;
            attack2X = menuX + attackButtonXOffset + (attackButtonWidth + 20)*1;
            attack2Y =  menuY + attackButtonYOffset;
            attack3X = menuX + attackButtonXOffset + (attackButtonWidth + 20)*2;
            attack3Y =  menuY + attackButtonYOffset;
            attack4X = menuX + attackButtonXOffset + (attackButtonWidth + 20)*0;
            attack4Y = menuY + attackButtonYOffset + attackButtonHeight+10;
            attack5X = menuX + attackButtonXOffset + (attackButtonWidth + 20)*1;
            attack5Y = menuY + attackButtonYOffset + attackButtonHeight+10;
            attack6X = menuX + attackButtonXOffset + (attackButtonWidth + 20)*2;
            attack6Y = menuY + attackButtonYOffset + attackButtonHeight+10;

            g.drawImage(attackBackground, attack1X, attack1Y, null);
            g.drawImage(attackBackground, attack2X, attack2Y, null);
            g.drawImage(attackBackground, attack3X, attack3Y, null);
            g.drawImage(attackBackground, attack4X, attack4Y, null);
            g.drawImage(attackBackground, attack5X, attack5Y, null);
            g.drawImage(attackBackground, attack6X, attack6Y, null);

        }
    }

    private BufferedImage scaleToSize(BufferedImage sbi, float x, float y) {
        float fWidth = x/sbi.getWidth();
        float fHeight = y/sbi.getHeight();
        BufferedImage dbi = null;
        if(sbi != null) {
            int imageType = sbi.getType();
            dbi = new BufferedImage((int)x,(int)y, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }

    public static boolean startBattleZoom = false;
    public static void startBattle() {
        startBattleZoom = true;
    }
    public static int zoomCount = 0;
    public static void zoomIn() {
        if (startBattleZoom) {
            zoomCount = zoomCount + 1;
            scale = scale + 0.25f;
            if (zoomCount > 30) {
                scale = 2;
                Merlin.mode = Merlin.Mode.BATTLE;
                GameLoop.pause = false;
                startBattleZoom = false;
                zoomCount = 0;
            }
        }
    }
}
