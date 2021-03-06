package workexpIT.merlin.tiles;


import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.data.DataReader;
import workexpIT.merlin.data.ImageReader;
import workexpIT.merlin.data.SoundHandler;
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
        BufferedImage image = ImageReader.loadImage("resources/graphics/materials/"+ this.getClass().getSimpleName() +"" + instance + ".png");
        BufferedImage scaledImage = JavaDrawer.scale(image,JavaDrawer.scale,JavaDrawer.scale);
        Output.write(scaledImage.getWidth()+"");
        texture = loadTextures(scaledImage);
        Output.write(this.getClass().getSimpleName() + " has " + texture.length + " number of animation textures and has a max of " + maxAnimationStage + " stages");
        animation = needsToBeUpdated;
    }

    private BufferedImage[] loadTextures(BufferedImage image) {
        int frames = (int) (image.getWidth()/16/JavaDrawer.scale);
        BufferedImage[] output = new BufferedImage[frames];
        for (int i = 0; i < frames; i++) {
            output[i] = ImageReader.cropImage(image, (int)(i * 16*JavaDrawer.scale), 0, (int)(16*JavaDrawer.scale), image.getHeight());
        }
        maxAnimationStage = frames - 1;
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
        BufferedImage image = ImageReader.loadImage("resources/graphics/materials/"+ this.getClass().getSimpleName() +"" + instance + ".png");
        BufferedImage scaledImage = JavaDrawer.scale(image,JavaDrawer.scale,JavaDrawer.scale);
        Output.write(scaledImage.getWidth()+"");
        texture = loadTextures(scaledImage);
    }

    public void changeAnimationStage() {
        if (animationStage < maxAnimationStage) {
            animationStage = animationStage + 1;
            JavaDrawer.redrawMap(getLocation());
        }
        else if (animationStage > maxAnimationStage){
            animationStage = 0;
            JavaDrawer.redrawMap(getLocation());
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
        Output.write("Door has been set");
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
            Output.write("Triggering event");
            GameLoop.triggerLocationEvent(new int[] {x,y});
        }
        for (int i = 0; i < WorldData.entities.size(); i ++) {
            for (int checkX = 0; checkX < WorldData.entities.get(i).entityWidth/16; checkX++) {
                for (int checkY = 0; checkY < WorldData.entities.get(i).entityHeight/16; checkY++) {
                    if ((WorldData.entities.get(i).getX()+checkX) == x && (WorldData.entities.get(i).getY()+checkY) == y && WorldData.entities.get(i)!=entity) {
                        GameLoop.entityInteract(WorldData.entities.get(i), entity);
                        Output.write("Theres an entity in the way");
                        return false;
                    }
                }
            }
        }
        if (!movingOnToTileExtra(entity)) {
            return false;
        }
        if (checkForDoor(entity)) {
            return true;
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
                SoundHandler.playNewSound("resources/audio/exit.wav",1);
                Thread door = new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        DataReader.loadMap(doorMap);
                        Output.write("DOOOOOOOOR");
                        WorldData.enterX = doorX;
                        WorldData.enterY = doorY;
                        WorldData.getPlayer().setX(doorX);
                        WorldData.getPlayer().setY(doorY);
                        WorldData.getPlayer().lastLoc[0] = doorX;
                        WorldData.getPlayer().lastLoc[1] = doorY;
                        JavaDrawer.offsetX = (-WorldData.getPlayer().getX() * JavaDrawer.imageSize - WorldData.getPlayer().downSprite.getWidth() / JavaDrawer.scale);
                        JavaDrawer.offsetY = (-WorldData.getPlayer().getY() * JavaDrawer.imageSize + WorldData.getPlayer().downSprite.getHeight() / JavaDrawer.scale);
                    }
                };
                JavaDrawer.fadeAway = true;
                door.start();
                return true;
            }
        }
        return false;
    }
    public BufferedImage[] getTexture() {
        return texture;
    }
    public int[] getLocation() {
        return new int[]{x,y};
    }
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

