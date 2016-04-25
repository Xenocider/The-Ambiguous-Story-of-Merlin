package workexpIT.merlin.tiles;


import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.WorldData;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.entities.Player;
import workexpIT.merlin.graphics.JavaDrawer;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * Created by ict11 on 2016-02-03.
 */
public class Tile {

    public boolean locationTrigger = false;
    public boolean animation = false;

    public Tile(int numOfInstances,boolean needsToBeUpdated) {
        maxInstances = numOfInstances;
        texture = loadTextures(ImageReader.loadImage("resources/graphics/materials/"+ this.getClass().getSimpleName() +"" + instance + ".png"));
        animation = needsToBeUpdated;
    }

    private BufferedImage[] loadTextures(BufferedImage image) {
        int frames = image.getWidth()/16;
        BufferedImage[] output = new BufferedImage[frames];
        for (int i = 0; i < frames; i++) {
            output[i] = ImageReader.cropImage(image, i * 16, 0, 16, image.getHeight());
        }
        maxAnimationStage = frames;
        return output;
    }

    protected BufferedImage[] texture;

    public int maxInstances = 1;
    public int instance = 0;

    public int animationStage = 0;
    public int maxAnimationStage = 3;

    public int x;
    public int y;

    public void setInstance(int instance) {
        this.instance = instance;
        texture = loadTextures(ImageReader.loadImage("resources/graphics/materials/"+ this.getClass().getSimpleName() +"" + instance + ".png"));
    }

    public void changeAnimationStage() {
        animationStage = animationStage + 1;
        if (animationStage == maxAnimationStage) {
            animationStage = 0;
        }
    }

    public enum Rotation {UP,RIGHT,DOWN,LEFT}
    public enum Flip {HORIZONTAL,VERTICAL,DEFAULT}
    public Rotation rotation = Rotation.UP;
    public Flip flip = Flip.DEFAULT;

    protected boolean door;
    protected String doorMap;
    protected int doorX;
    protected int doorY;

    public void setDoor(boolean b, String map, int x, int y) {
        door = b;
        doorMap = map;
        doorX = x;
        doorY = y;
    }

    public void setRotation(Rotation rot){
        rotation = rot;
    }
    public void setFlip(Flip f){
        flip = f;
    }


    public boolean movingOnToTile(Entity entity) {
        if (WorldData.getPlayer() == entity && locationTrigger) {
            GameLoop.triggerLocationEvent(getLocation());
        }
        for (int i = 0; i < WorldData.entities.size(); i ++) {
            if (WorldData.entities.get(i).getX() == x && WorldData.entities.get(i).getY() == y) {
                GameLoop.entityInteract(WorldData.entities.get(i),entity);
                return false;
            }
        }
        if (!movingOnToTileExtra(entity)) {
            return false;
        }
        if (checkForDoor(entity)) {
            return false;
        }
        return true;
    }

    public boolean movingOnToTileExtra(Entity e) {
        //Used in child classes only
        return true;
    }

    public boolean checkForDoor(Entity entity) {
        if (entity.getClass().equals(Player.class)) {
            if (door) {
                Output.write("DOOOOOOOOR");
                        WorldData.getPlayer().setX(doorX);
                        WorldData.getPlayer().setY(doorY);
                        WorldData.getPlayer().lastLoc[0] = doorX;
                        WorldData.getPlayer().lastLoc[1] = doorY;
                        JavaDrawer.offsetX = (-WorldData.getPlayer().getX()*JavaDrawer.imageSize-WorldData.getPlayer().downSprite.getWidth()/JavaDrawer.scale);
                        JavaDrawer.offsetY = (-WorldData.getPlayer().getY()*JavaDrawer.imageSize+WorldData.getPlayer().downSprite.getHeight()/JavaDrawer.scale);
                DataReader.loadMap(doorMap);
                return true;
            }
        }
        return false;
    }
    public BufferedImage[] getTexture() {
        return texture;
    }
    public int[] getLocation() {
        for (int y = 0; y < WorldData.mapSizeY; y++) {
            for (int x = 0; x < WorldData.mapSizeX; x++) {
                Output.write("Searching " + x + " " + y);
                if (WorldData.tiles[x][y] == this) {
                    return new int[]{x,y};
                }
            }
        }
        return null;
    }
}

