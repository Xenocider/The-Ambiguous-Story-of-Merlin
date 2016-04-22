package workexpIT.merlin.graphics;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.attacks.Attack;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.listeners.JavaKeyListener;
import workexpIT.merlin.listeners.KeyBinder;
import workexpIT.merlin.listeners.MouseListener;
import workexpIT.merlin.tiles.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.Date;


/**
 * Created by ict11 on 2016-02-22.
 */
public class JavaDrawer extends JPanel implements Runnable {

    public static JFrame frame;
    public static float offsetX = 0;
    public static float offsetY = 0;
    public static int imageSize = 16;
    public static int ww = 800;
    public static int wh = 800;
    public static float scale = 2;

    public static int count = 0;
    public static boolean runAnimation = false;

    public static boolean drawDialog = false;

    public static int editorMenuSize = 200;
    public static int walkingStage = 1;
    private static int maxCount = 7;
    public static int maxWalkingStage = maxCount*2+1;
    private float offsetSpeed = 0.5f;
    public boolean pause = false;

    public static int minOffsetX;
    public static int maxOffsetX;
    public static int minOffsetY;
    public static int maxOffsetY;

    public static boolean animateText = false;


    public static Font f = new Font("Helvetica", Font.PLAIN, 30);

    public static void init() {
        createWindow();
        maxOffsetX = (int) (-frame.getWidth()/2/scale);
        maxOffsetY = (int) (-frame.getHeight()/2/scale+imageSize);
        minOffsetX = (int) (-(imageSize*WorldData.mapSizeX-frame.getWidth()/2/scale)-imageSize*2/scale);
        minOffsetY = (int) (-(imageSize*WorldData.mapSizeY-frame.getHeight()/2/scale)-imageSize*(5.5f-scale)/scale);
        frame.getGraphics().drawImage(ImageReader.loadImage("resources/graphics/loadscreen.png"),0,0,null);
        frame.getGraphics().drawString("LOREM IPSUM",0,0);
    }

    @Override
    public void run() {
        //clearScreen();
        //drawTiles(frame.getGraphics());
        //drawEntities(frame.getGraphics());
        frame.repaint();

        maxOffsetX = (int) (-frame.getWidth()/2/scale);
        maxOffsetY = (int) (-frame.getHeight()/2/scale+imageSize);
        minOffsetX = (int) (-(imageSize*WorldData.mapSizeX-frame.getWidth()/2/scale)-imageSize*2/scale);
        minOffsetY = (int) (-(imageSize*WorldData.mapSizeY-frame.getHeight()/2/scale)-imageSize*(5.5f-scale)/scale);

    }

    private void changeAnimationStage() {
        for (int i = 0; i < WorldData.entities.size(); i++) {
            WorldData.entities.get(i).changeAnimationStage();
        }
        for (int a = 0; a < WorldData.tiles.length; a++) {
            for (int b = 0; b < WorldData.tiles.length; b++) {
                if (WorldData.tiles[a][b] != null) {
                    WorldData.tiles[a][b].changeAnimationStage();
                }
            }
        }
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
            recordStart();
            long startTime = System.currentTimeMillis();
            super.paintComponent(g);
            zoomIn();

            //Run animation code before drawing anything
            for (int i = 0; i < Animator.currentAnimators.size(); i++) {
                Animator.currentAnimators.get(i).run();
            }
            Output.log("[JavaDrawer] Took " + recordEnd() + " milliseconds to run current animators");
            //Draw stuff
            if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
                recordStart();

                moveScreen();
                drawGrid(g);
                Output.log("[JavaDrawer] Took " + recordEnd() + " milliseconds to repaint the editor grid");

            }
            if (Merlin.mode.equals(Merlin.Mode.GAME)) {
                recordStart();

                //smoothOffset();
                centerCameraOnPlayer();
                Output.log("[JavaDrawer] Took " + recordEnd() + " milliseconds to repaint the screen's smooth offsetting");

            }
            if (Merlin.mode.equals(Merlin.Mode.GAME) || Merlin.mode.equals(Merlin.Mode.EDITOR)) {
                recordStart();
                //drawTiles(g); //Need to load the whole map as one image and not do so many loops
                drawMap(g);
                Output.log("[JavaDrawer] Took " + recordEnd() + " milliseconds to draw the Tiles");
                recordStart();
                drawEntities(g);
                Output.log("[JavaDrawer] Took " + recordEnd() + " milliseconds to draw the Entities");
                if (drawDialog) {
                    drawDialogScreen(g);
                }
                //drawMapAndEntities(g);
            }
            if (Merlin.mode.equals(Merlin.Mode.EDITOR)) {
                recordStart();
                drawEditorMenu(g);
                Output.log("[JavaDrawer] Took " + recordEnd() + " milliseconds to draw the editor menu");
                updateMap();

            }
            if (Merlin.mode.equals(Merlin.Mode.BATTLE)) {
                recordStart();
                drawBackground(g);
                drawBattleEnemy(g);
                drawAttack(g);
                drawBattlePlayer(g);
                drawStatusBars(g);
                drawBattleMenu(g);
                Output.log("[JavaDrawer] Took " + recordEnd() + " milliseconds to draw the Battle Scene");
            }
            long endTime = System.currentTimeMillis();
            //Output.write("Took " + (endTime-startTime) + "ms to render this frame");
            //Output.write("FPS: " + Math.pow((endTime-startTime),-1)*1000);
    }

    public int textCount = 0;
    public int charPerLine = 45;
    public int typeSpeed = 4;

    private void drawDialogScreen(Graphics g) {

        BufferedImage image = ImageReader.loadImage("resources/graphics/dialogbox.png");
        g.drawImage(image,0,frame.getHeight()-image.getHeight()-25,null);
        g.setColor(Color.black);
        g.setFont(f);
        int line = (int)(textCount/typeSpeed/charPerLine);
        Output.write(line+"");
        for (int i = 0; i <= line;i++) {
            String text = "error";
            if (textCount/typeSpeed/charPerLine > i) {
                text = GameLoop.dialogText.substring(charPerLine * i, (charPerLine*typeSpeed*(i+1)) / typeSpeed);
            }
            else {
                text = GameLoop.dialogText.substring(charPerLine * i, (textCount) / typeSpeed);
            }
            g.drawString(text, 18, frame.getHeight() - image.getHeight() + 30 - 10+35*i);
        }
        if (animateText) {
            textCount = textCount + 1;
        }
        if (textCount/typeSpeed == GameLoop.dialogText.length()) {
            animateText = false;
            pause = true;
        }
    }

    private void drawMapAndEntities(Graphics g) {
        BufferedImage tile;
        for (int x = 0; x < WorldData.mapSizeX; x++) {
            for (int y = 0; y < WorldData.mapSizeY; y++) {
                if (WorldData.tiles[x][y] != null) {
                    tile = WorldData.tiles[x][y].getTexture()[WorldData.tiles[x][y].animationStage];
                    double radians = 0.0;
                    switch (WorldData.tiles[x][y].rotation) {
                        case UP:
                            radians = Math.PI/2.0*0.0;
                            break;
                        case RIGHT:
                            radians = Math.PI/2.0*1.0;
                            break;
                        case DOWN:
                            radians = Math.PI/2.0*2.0;
                            break;
                        case LEFT:
                            radians = Math.PI/2.0*3.0;
                            break;
                    }
                    tile = rotateImage(tile, radians);
                    try {
                        switch (WorldData.tiles[x][y].flip) {
                            case HORIZONTAL:
                                tile = flipImage(tile, true);
                                break;
                            case VERTICAL:
                                tile = flipImage(tile, false);
                                break;
                        }
                    }
                    catch (NullPointerException e) {}
                    g.drawImage(scale(tile,scale,scale),(int)((x * imageSize + offsetX)*scale)+frame.getWidth()/2, (int)((y * imageSize - imageSize + offsetY)*scale)+frame.getHeight()/2,null);
                }
                for (int i =0; i< WorldData.entities.size(); i++) {
                    if (WorldData.entities.get(i).getX() == x && WorldData.entities.get(i).getY() == y) {
                        BufferedImage sprite = null;
                        switch (WorldData.entities.get(i).facing){
                            case Entity.MOVE_UP:
                                if (WorldData.entities.get(i).moving) {
                                    sprite = scale(WorldData.entities.get(i).upWalkingSprites[WorldData.entities.get(i).animationStage], scale, scale);
                                }
                                else {
                                    sprite = scale(WorldData.entities.get(i).upSprite, scale, scale);
                                }
                                break;
                            case Entity.MOVE_RIGHT:
                                if (WorldData.entities.get(i).moving) {
                                    sprite = scale(WorldData.entities.get(i).rightWalkingSprites[WorldData.entities.get(i).animationStage], scale, scale);
                                }
                                else {
                                    sprite = scale(WorldData.entities.get(i).rightSprite, scale, scale);
                                }                        break;
                            case Entity.MOVE_DOWN:
                                if (WorldData.entities.get(i).moving) {
                                    Output.write("ANIMATING with stage = " + WorldData.entities.get(i).animationStage);
                                    sprite = scale(WorldData.entities.get(i).downWalkingSprites[WorldData.entities.get(i).animationStage], scale, scale);
                                }
                                else {
                                    sprite = scale(WorldData.entities.get(i).downSprite, scale, scale);
                                }                      break;
                            case Entity.MOVE_LEFT:
                                if (WorldData.entities.get(i).moving) {
                                    sprite = scale(WorldData.entities.get(i).leftWalkingSprites[WorldData.entities.get(i).animationStage], scale, scale);
                                }
                                else {
                                    sprite = scale(WorldData.entities.get(i).leftSprite, scale, scale);
                                }                        break;
                        }
                        int entityX = WorldData.entities.get(i).lastLoc[0]*imageSize + (WorldData.entities.get(i).getX()*imageSize - WorldData.entities.get(i).lastLoc[0]*imageSize)*walkingStage/maxWalkingStage;
                        int entityY = WorldData.entities.get(i).lastLoc[1]*imageSize + (WorldData.entities.get(i).getY()*imageSize - WorldData.entities.get(i).lastLoc[1]*imageSize)*walkingStage/maxWalkingStage;
                        int w = sprite.getWidth();
                        int h = sprite.getHeight();
                        g.drawImage(sprite, (int) ((entityX + offsetX) * scale) + frame.getWidth() / 2, (int) ((entityY - h / scale + offsetY) * scale) + frame.getHeight() / 2, null);
                    }
                }
            }
        }
    }


    private void drawEditorMenu(Graphics g) {
        int x = 0;
        int y = 0;
        g.setColor(Color.WHITE);
        g.fillRect(frame.getWidth()-editorMenuSize,0,editorMenuSize,frame.getHeight());
        if (GameLoop.tileEditor) {
            for (int i = 0; i < WorldData.menuTiles.size(); i++) {

                BufferedImage image = scale(WorldData.menuTiles.get(i).getTexture()[WorldData.menuTiles.get(i).animationStage], 4, 4);

                g.drawImage(image, (int) (x * JavaDrawer.imageSize * 4 + frame.getWidth() - editorMenuSize + 10 * x + 10), (int) (y * JavaDrawer.imageSize * 4 + 10 * y + 10), null);
                if (x == 0) {
                    x = 1;
                } else {
                    x = 0;
                    y = y + 1;
                }
            }
        }
        else {
            for (int i = 0; i < WorldData.entityMenu.size(); i++) {

                WorldData.entityMenu.get(i);

                BufferedImage image = scale(WorldData.entityMenu.get(i).getSprites().getSubimage(0,10,16,16),4,4);

                g.drawImage(image, (int) (x * JavaDrawer.imageSize * 4 + frame.getWidth() - editorMenuSize + 10 * x + 10), (int) (y * JavaDrawer.imageSize * 4 + 10 * y + 10), null);
                if (x == 0) {
                    x = 1;
                } else {
                    x = 0;
                    y = y + 1;
                }
            }
        }
        g.drawImage(GameLoop.save, frame.getWidth()-10-32, 10,null);
    }

    private void smoothOffset() {
        try {

        float newOffsetX = (-WorldData.getPlayer().getX()*imageSize-WorldData.getPlayer().downSprite.getWidth()/scale);
        float newOffsetY = (-WorldData.getPlayer().getY()*imageSize+WorldData.getPlayer().downSprite.getHeight()/scale);


            if (newOffsetX > offsetX) {
                offsetX = offsetX + offsetSpeed;
            }
            if (newOffsetY > offsetY) {
                offsetY = offsetY + offsetSpeed;
            }
            if (newOffsetX < offsetX) {
                offsetX = offsetX - offsetSpeed;
            }
            if (newOffsetY < offsetY) {
                offsetY = offsetY - offsetSpeed;
            }
        }
        catch (Exception e) {}
    }

    private void clearScreen() {
        frame.getGraphics();
    }

    private void drawEntities(Graphics g) {
        if (runAnimation && !pause) {
            walkingStage = walkingStage + 1;
            Output.write(walkingStage + " < " + maxWalkingStage);
            count = count + 1;
            if (count == maxCount) {
                count = 0;
                changeAnimationStage();
                Output.write("Changed animation stage");
                updateMap();
            }
            if (walkingStage == maxWalkingStage) {
                for (int i = 0; i < WorldData.entities.size(); i++) {
                    WorldData.entities.get(i).lastLoc[0] = WorldData.entities.get(i).getX();
                    WorldData.entities.get(i).lastLoc[1] = WorldData.entities.get(i).getY();
                    WorldData.entities.get(i).moving = false;
                }
                runAnimation = false;
                walkingStage = 1;
                //count = 0;
                GameLoop.pause = false;
                Output.write("Movement Animation Stage Finished");
            }
        }
        //Output.write("Drawing Entities");
        for (int checkY = 0; checkY < WorldData.mapSizeY; checkY++) {
            for (int checkX = 0; checkX < WorldData.mapSizeX; checkX++) {
                for (int i = 0; i < WorldData.entities.size(); i++) {
                    if (WorldData.entities.get(i).getX() == checkX && WorldData.entities.get(i).getY() == checkY) {
                        BufferedImage sprite = null;
                        switch (WorldData.entities.get(i).facing) {
                            case Entity.MOVE_UP:
                                if (WorldData.entities.get(i).moving) {
                                    sprite = WorldData.entities.get(i).upWalkingSprites[WorldData.entities.get(i).animationStage];
                                } else {
                                    sprite = WorldData.entities.get(i).upSprite;
                                }
                                break;
                            case Entity.MOVE_RIGHT:
                                if (WorldData.entities.get(i).moving) {
                                    sprite = WorldData.entities.get(i).rightWalkingSprites[WorldData.entities.get(i).animationStage];
                                } else {
                                    sprite = WorldData.entities.get(i).rightSprite;
                                }
                                break;
                            case Entity.MOVE_DOWN:
                                if (WorldData.entities.get(i).moving) {
                                    //Output.write("ANIMATING with stage = " + WorldData.entities.get(i).animationStage);
                                    sprite = WorldData.entities.get(i).downWalkingSprites[WorldData.entities.get(i).animationStage];
                                } else {
                                    sprite = WorldData.entities.get(i).downSprite;
                                }
                                break;
                            case Entity.MOVE_LEFT:
                                if (WorldData.entities.get(i).moving) {
                                    sprite = WorldData.entities.get(i).leftWalkingSprites[WorldData.entities.get(i).animationStage];
                                } else {
                                    sprite = WorldData.entities.get(i).leftSprite;
                                }
                                break;
                        }
                        int x = WorldData.entities.get(i).lastLoc[0] * imageSize + (WorldData.entities.get(i).getX() * imageSize - WorldData.entities.get(i).lastLoc[0] * imageSize) * walkingStage / maxWalkingStage;
                        int y = WorldData.entities.get(i).lastLoc[1] * imageSize + (WorldData.entities.get(i).getY() * imageSize - WorldData.entities.get(i).lastLoc[1] * imageSize) * walkingStage / maxWalkingStage;
                        int w = sprite.getWidth();
                        int h = sprite.getHeight();
                        g.drawImage(sprite, (int) ((x + offsetX) * scale) + frame.getWidth() / 2, (int) ((y - h / scale + offsetY) * scale) + frame.getHeight() / 2, null);
                    }
                }
            }
        }
    }

    public void centerCameraOnPlayer() {
        if (WorldData.getPlayer() != null) {
            try {
                int x = WorldData.getPlayer().lastLoc[0] * imageSize + (WorldData.getPlayer().getX() * imageSize - WorldData.getPlayer().lastLoc[0] * imageSize) * walkingStage / maxWalkingStage;
                int y = WorldData.getPlayer().lastLoc[1] * imageSize + (WorldData.getPlayer().getY() * imageSize - WorldData.getPlayer().lastLoc[1] * imageSize) * walkingStage / maxWalkingStage;
                int w = WorldData.getPlayer().downSprite.getWidth();
                int h = WorldData.getPlayer().downSprite.getHeight();
                int offX = (int) (-x);
                int offY = (int) (-y);
                //offsetX = offsetX + (offX - offsetX)*0.05f;
                //offsetY = offsetY + (offY - offsetY)*0.05f;
                offsetX = offX;
                offsetY = offY;
                if (offsetX > maxOffsetX) {
                    offsetX = maxOffsetX;
                }
                if (offsetY > maxOffsetY) {
                    offsetY = maxOffsetY;
                }
                if (offsetY < minOffsetY) {
                    offsetY = minOffsetY;
                }
                if (offsetX < minOffsetX) {
                    offsetX = minOffsetX;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateMap() {
        //WorldData.map = JavaDrawer.loadMapIntoOneImage();
        //WorldData.scaledMap = JavaDrawer.scale(WorldData.map,JavaDrawer.scale,JavaDrawer.scale);
        //WorldData.scaledMap = JavaDrawer.loadMapIntoOneImage();
        BufferedImage map = WorldData.scaledMap;
        try {
            Graphics2D g2 = map.createGraphics();
            for (int i = 0; i < WorldData.animatedTiles.size(); i++) {
                int a = WorldData.animatedTiles.get(i)[0];
                int b = WorldData.animatedTiles.get(i)[1];
                BufferedImage tile = scale(WorldData.tiles[a][b].getTexture()[WorldData.tiles[a][b].animationStage], scale, scale);
                double radians = 0.0;
                switch (WorldData.tiles[a][b].rotation) {
                    case UP:
                        radians = Math.PI / 2.0 * 0.0;
                        break;
                    case RIGHT:
                        radians = Math.PI / 2.0 * 1.0;
                        break;
                    case DOWN:
                        radians = Math.PI / 2.0 * 2.0;
                        break;
                    case LEFT:
                        radians = Math.PI / 2.0 * 3.0;
                        break;
                }
                tile = rotateImage(tile, radians);
                try {
                    switch (WorldData.tiles[a][b].flip) {
                        case HORIZONTAL:
                            tile = flipImage(tile, true);
                            break;
                        case VERTICAL:
                            tile = flipImage(tile, false);
                            break;
                    }
                } catch (NullPointerException e) {
                }
                g2.drawImage(tile, null, (int) (a * 16 * scale), (int) ((b * 16) * scale));
            }
            g2.dispose();
            WorldData.scaledMap = map;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static BufferedImage loadMapIntoOneImage() {
        BufferedImage map = new BufferedImage((int)(WorldData.mapSizeX*16*scale),(int)(WorldData.mapSizeY*16*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = map.createGraphics();
        BufferedImage tile;
        for (int a = 0; a < WorldData.tiles.length; a++) {
            for (int b = 0; b < WorldData.tiles[a].length; b++) {
                if (WorldData.tiles[a][b] != null) {
                    tile = scale(WorldData.tiles[a][b].getTexture()[WorldData.tiles[a][b].animationStage],scale,scale);
                    double radians = 0.0;
                    switch (WorldData.tiles[a][b].rotation) {
                        case UP:
                            radians = Math.PI/2.0*0.0;
                            break;
                        case RIGHT:
                            radians = Math.PI/2.0*1.0;
                            break;
                        case DOWN:
                            radians = Math.PI/2.0*2.0;
                            break;
                        case LEFT:
                            radians = Math.PI/2.0*3.0;
                            break;
                    }
                    tile = rotateImage(tile, radians);
                    try {
                        switch (WorldData.tiles[a][b].flip) {
                            case HORIZONTAL:
                                tile = flipImage(tile, true);
                                break;
                            case VERTICAL:
                                tile = flipImage(tile, false);
                                break;
                        }
                    }
                    catch (NullPointerException e) {}
                    g2.drawImage(tile,null,(int)(a*16*scale),(int)((b*16)*scale));
                }
            }
        }
        g2.dispose();
        return map;
    }
    public static BufferedImage rotateImage(BufferedImage input, double radians) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(radians, input.getWidth()/2, input.getHeight()/2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage output = op.filter(input, null);
        return output;
    }

    public static BufferedImage flipImage(BufferedImage input, boolean horizontal) {
        AffineTransform at = new AffineTransform();
        if (horizontal) {
            at.concatenate(AffineTransform.getScaleInstance(-1, 1));
            at.concatenate(AffineTransform.getTranslateInstance(-input.getHeight(), 0));
        }
        else {
            at.concatenate(AffineTransform.getScaleInstance(1, -1));
            at.concatenate(AffineTransform.getTranslateInstance(0, -input.getHeight()));
        }
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage output = op.filter(input, null);
        return output;
    }

    private void drawMap(Graphics g) {
        //BufferedImage scaledMap = scale(WorldData.map,scale,scale);
        g.drawImage(WorldData.scaledMap,(int)(offsetX*scale+frame.getWidth()/2),(int)((offsetY-imageSize)*scale+frame.getHeight()/2),null);
    }

    private void drawTiles(Graphics g) {
        for (int a = 0; a < WorldData.tiles.length; a++) {
            for (int b = 0; b < WorldData.tiles[a].length; b++) {
                if (WorldData.tiles[a][b] != null) {
                    BufferedImage image = scale(WorldData.tiles[a][b].getTexture()[0],scale,scale);
                    double radians = 0.0;
                    switch (WorldData.tiles[a][b].rotation) {
                        case UP:
                            radians = Math.PI/2.0*0.0;
                            break;
                        case RIGHT:
                            radians = Math.PI/2.0*1.0;
                            break;
                        case DOWN:
                            radians = Math.PI/2.0*2.0;
                            break;
                        case LEFT:
                            radians = Math.PI/2.0*3.0;
                            break;
                    }
                    image = rotateImage(image, radians);
                    try {
                        switch (WorldData.tiles[a][b].flip) {
                            case HORIZONTAL:
                                image = flipImage(image, true);
                                break;
                            case VERTICAL:
                                image = flipImage(image, false);
                                break;
                        }
                    }
                    catch (NullPointerException e) {}
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
            Output.write("Adding Keylistener...");
            drawer.addKeyListener(Merlin.keyListener);
            Output.write("KeyListener added!");
            //KeyBinder.init();
            frame.addKeyListener(Merlin.keyListener);

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

    
    //WARNING THIS FUNCTION IS VERY VERY CPU INTENSIVE AND WILL LAG THE COMPUTER IF RUN ON A LOOP!!!!
    //TODO WARNING THIS FUNCTION IS VERY VERY CPU INTENSIVE AND WILL LAG THE COMPUTER IF RUN ON A LOOP!!!!
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
    public static int playerAdditionOffsetX = 0;
    public static int enemyOffsetFromTop = 100;
    public static int enemyOffsetFromSide = 20;
    public static int enemyAdditionOffsetX = 0;
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

    //Animation Offsets
    public static int enemyAnimationOffsetX = 0;
    public static int enemyAnimationOffsetY = 0;
    public static int playerAnimationOffsetX = 0;
    public static int playerAnimationOffsetY = 0;
    public static int attackAnimationOffsetX = 0;
    public static int attackAnimationOffsetY = 0;



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

    public static boolean pFlinch = false;
    public static boolean eFlinch = false;




    private void drawBackground(Graphics g) {
        g.drawImage(WorldData.battleBackground,0,0,null);
    }

    private void drawBattleEnemy(Graphics g) {

        BufferedImage enemy = GameLoop.enemy.battleSprite;
        enemyX = JavaDrawer.frame.getWidth() - enemy.getWidth() - enemyOffsetFromSide + enemyAnimationOffsetX;
        enemyY = enemyOffsetFromTop + enemyAnimationOffsetY;
        if (!eFlinch) {
            g.drawImage(enemy, enemyX + enemyAdditionOffsetX, enemyY,null);
        }
    }

    private void drawBattlePlayer(Graphics g){
        BufferedImage player = WorldData.getPlayer().battleSprite;
        playerX = playerOffsetFromSide + playerAnimationOffsetX;
        playerY = JavaDrawer.frame.getHeight() - player.getHeight() - playerOffsetFromBottom + playerAnimationOffsetY;
        if (!pFlinch) {
            g.drawImage(player, playerX + playerAdditionOffsetX, playerY, null);
        }
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


    private void drawAttack(Graphics g) {
        if (AttackAnimator.animate && AttackAnimator.animationTexture != null && attackAnimationOffsetX != 0 && attackAnimationOffsetY != 0) {
            g.drawImage(AttackAnimator.animationTexture, attackAnimationOffsetX, attackAnimationOffsetY, null);
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
            //scale = scale + 0.25f;
            if (zoomCount > 30) {
                scale = 2;
                Merlin.mode = Merlin.Mode.BATTLE;
                GameLoop.pause = false;
                startBattleZoom = false;
                zoomCount = 0;
            }
            WorldData.scaledMap = JavaDrawer.scale(WorldData.map,scale,scale);
        }
    }

    public static void runAnimation() {
        runAnimation = true;
        Output.write("Movement Animation Stage Started");
    }

    private static Timestamp recordStartTime;
    public static void recordStart() {
        recordStartTime = new Timestamp(new Date().getTime());
    }
    public static long recordEnd() {
        Timestamp recordEndTime = new Timestamp(new Date().getTime());
        long time = recordEndTime.getTime() - recordStartTime.getTime();
        return time;
    }

    public static void drawDialog(String text) {
        drawDialog = true;
        animateText = true;
    }
}
