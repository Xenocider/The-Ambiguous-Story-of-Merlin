package workexpIT.merlin.data;

import workexpIT.merlin.GameLoop;
import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.entities.Sign;
import workexpIT.merlin.graphics.Drawer;
import workexpIT.merlin.graphics.JavaDrawer;
import workexpIT.merlin.tiles.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static workexpIT.merlin.graphics.JavaDrawer.flipImage;
import static workexpIT.merlin.graphics.JavaDrawer.rotateImage;

/**
 * Created by ict11 on 2016-02-03.
 */
public class DataReader {

    public static String OS = System.getProperty("os.name").toLowerCase();

    public static void loadMap(String mapid) {
        int xSize = loadMapSizeX(mapid);
        int ySize = loadMapSizeY(mapid);
        WorldData.mapSizeX = xSize;
        WorldData.mapSizeY = ySize;
        try {
            for (int i = 0; i < WorldData.entities.size(); i++) {
                if (WorldData.entities.get(i).getName().equals("player")) {
                    //Keep player but delete all other entities
                } else {
                    WorldData.entities.remove(i);
                    i = i - 1;
                    Output.write("Removed an entity");
                }
            }
        } catch (Exception e) {
        }
        //Center camera on player
        WorldData.tiles = new Tile[xSize][ySize];
        try {
            Output.write("Reading map data: " + mapid);
            FileReader FReader = new FileReader("resources/worlddata/default/" + mapid + "/tiledata.txt");
            BufferedReader BReader = new BufferedReader(FReader);

            int x = 0;
            int y = 0;

            List<Character> data = new ArrayList<Character>();

            int value = 0;
            while ((value = BReader.read()) != -1) {
                // converts int to character
                char c = (char) value;

                //If it's a new line
                if (value == 10) {
                    String id = data.toString().substring(1, data.toString().length() - 1);
                    if (OS.contains("win")) {
                        id = id.substring(0, id.length() - 1);
                    }
                    boolean isPC = true;
                    if (isPC) {
                        data.clear();
                    }
                    else {
                        //PC only ^
                        if (!id.equals("")) {
                            Output.write("Tile data = " + id);
                            int i = -1;
                            String rotation = null;
                            String flip = null;
                            int instance = 0;

                            try {
                                instance = Integer.parseInt(id.substring(id.length() - 1));
                                flip = id.substring(id.length() - 4, id.length() - 3);
                                rotation = id.substring(id.length() - 7, id.length() - 6);
                                i = Integer.parseInt(id.substring(0, id.length() - 9));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (i < Reference.tileIds.length) {
                            Tile.Flip f = Tile.Flip.DEFAULT;
                            switch (flip) {
                                case "b":
                                    f = Tile.Flip.HORIZONTAL;
                                    break;
                                case "c":
                                    f = Tile.Flip.VERTICAL;
                                    break;
                            }
                            if (i > -1) {
                                switch (rotation) {
                                    case "b":
                                        loadTile(i, instance, x, y, Tile.Rotation.RIGHT, f);
                                        break;
                                    case "c":
                                        loadTile(i, instance, x, y, Tile.Rotation.DOWN, f);
                                        break;
                                    case "d":
                                        loadTile(i, instance, x, y, Tile.Rotation.LEFT, f);
                                        break;
                                    default: //this includes the "a" rotation
                                        loadTile(i, instance, x, y, Tile.Rotation.UP, f);
                                        break;
                                }
                                Output.write("Adding tile to " + x + " " + y);
                            }
                            }
                        }
                    }
                    x = 0;
                    y = y + 1;
                    data.clear();
                }
                //If it's a comma
                else if (c == ',') {
                    String id = data.toString().substring(1, data.toString().length() - 1);
                    if (!id.equals("")) {
                        Output.write(id + "");
                        int i = -1;
                        String rotation = null;
                        String flip = null;
                        int instance = 0;

                        try {
                            instance = Integer.parseInt(id.substring(id.length()-1));
                            flip = id.substring(id.length() - 4, id.length() - 3);
                            rotation = id.substring(id.length() - 7, id.length() - 6);
                            i = Integer.parseInt(id.substring(0, id.length() - 9));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Tile.Flip f = Tile.Flip.DEFAULT;
                        switch (flip) {
                            case "b":
                                f = Tile.Flip.HORIZONTAL;
                                break;
                            case "c":
                                f = Tile.Flip.VERTICAL;
                                break;
                        }
                        if (i > -1) {
                                switch (rotation) {
                                    case "b":
                                        loadTile(i, instance, x, y, Tile.Rotation.RIGHT,f);
                                        break;
                                    case "c":
                                        loadTile(i, instance, x, y, Tile.Rotation.DOWN,f);
                                        break;
                                    case "d":
                                        loadTile(i, instance, x, y, Tile.Rotation.LEFT,f);
                                        break;
                                    default: //this includes the "a" rotation
                                        loadTile(i, instance, x, y, Tile.Rotation.UP,f);
                                        break;
                                }
                                Output.write("Adding tile to " + x + " " + y);
                        }
                    }
                    x = x + 1;
                    data.clear();
                }
                //If it's a character
                else {
                    data.add(c);
                }
            }
        } catch (FileNotFoundException e) {
            Output.error("Could not find the specified map file: " + mapid);
            e.printStackTrace();
        } catch (IOException e) {
            Output.error("IO Exception while attempting to read the map file: " + mapid);
            e.printStackTrace();
        }
        WorldData.scaledMap = JavaDrawer.loadMapIntoOneImage();
        Output.write(WorldData.scaledMap.getWidth()+"");
        WorldData.mapName = mapid;
        loadEntityData(mapid);
        loadMiscData(mapid);
        loadAnimatedTiles();
        EventReader.loadEventData(mapid);
        WorldData.battleBackground = ImageReader.loadImage("resources/graphics/backgrounds/" + mapid + ".png");
        //Center camera
        try {
            Drawer.setCamera((-WorldData.getPlayer().getX() + Drawer.ww / 2 / Drawer.w) * Drawer.w, (-WorldData.getPlayer().getY() + Drawer.wh / 2 / Drawer.h) * Drawer.h);
        } catch (Exception e) {
        }
    }

    private static void loadAnimatedTiles() {
        for (int y = 0; y < WorldData.mapSizeY; y++) {
            for (int x = 0; x < WorldData.mapSizeX; x++) {
                try {
                    if (WorldData.tiles[x][y].animation) {
                        WorldData.animatedTiles.add(new int[]{x, y});
                    }
                }
                catch (Exception e){}
            }
        }
    }


    private static int loadMapSizeX(String mapid) {
        int x = 0;
        FileReader FReader = null;
        try {
            FReader = new FileReader("resources/worlddata/default/" + mapid + "/worldsize.txt");
            BufferedReader BReader = new BufferedReader(FReader);

            String line = null;

            line = BReader.readLine();
                Output.write(line);
                x = Integer.parseInt(line);

        } catch (FileNotFoundException e) {
            x = 50;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return x;
    }
    private static int loadMapSizeY(String mapid) {
        int x = 0;
        FileReader FReader = null;
        try {
            FReader = new FileReader("resources/worlddata/default/" + mapid + "/worldsize.txt");
            BufferedReader BReader = new BufferedReader(FReader);

            String line = null;

            line = BReader.readLine();
            line = BReader.readLine();
            Output.write(line);
            x = Integer.parseInt(line);

        } catch (FileNotFoundException e) {
            x = 50;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return x;
    }

    private static void loadTile(int id, int instance, int x, int y, Tile.Rotation rotation, Tile.Flip f) {
        Tile tile = null;
        Constructor c = null;
        try {
            tile = (Tile) Class.forName("workexpIT.merlin.tiles."+ Reference.tileIds[id]).newInstance();
            tile.setRotation(rotation);
            tile.setFlip(f);
            tile.setInstance(instance);
            tile.x = x;
            tile.y = y;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (tile == null) {
            Output.error("Could not load a tile with that id: " + id);
        }
        try {WorldData.tiles[x][y] = tile;}
        catch (ArrayIndexOutOfBoundsException e) {
            Output.error("Map data too large! Max size is " + WorldData.mapSizeX + " by " + WorldData.mapSizeY);
            System.exit(0);
        }
    }


    public static void loadMiscData(String mapid) {
        Output.write("Loading Misc Data...");
        FileReader FReader = null;
        try {
            FReader = new FileReader("resources/worlddata/default/"+mapid+"/miscdata.txt");
            BufferedReader BReader = new BufferedReader(FReader);

            String line = null;

            boolean doors = false;
            boolean signtext = false;

            while ((line = BReader.readLine()) != null) {
                Output.write(line);

                if (line.contains("doors")) {
                    doors = true;
                    signtext = false;
                }
                if (line.contains("signtext")) {
                    doors = false;
                    signtext = true;
                }

                if (doors) {

                    String doorX1 = null;
                    String doorY1 = null;
                    String doorMap = null;
                    String doorX2 = null;
                    String doorY2 = null;


                    line = BReader.readLine();
                    Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
                    m.find();
                    doorX1 = m.group(1);
                    m.find();
                    doorY1 = m.group(1);
                    m.find();
                    doorMap = m.group(1);
                    m.find();
                    doorX2 = m.group(1);
                    m.find();
                    doorY2 = m.group(1);

                        Output.write("[Door data] x1: " + doorX1 + " y1: " + doorY1 + " map: " + doorMap + " x2: " + doorX2 + " y2: " + doorY2);
                        WorldData.tiles[Integer.parseInt(doorX1)][Integer.parseInt(doorY1)].setDoor(true, doorMap, Integer.parseInt(doorX2), Integer.parseInt(doorY2));
                }
                if (signtext) {
                    String signX = null;
                    String signY = null;
                    String text = null;

                    line = BReader.readLine();
                    Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
                    m.find();
                    signX = m.group(1);
                    m.find();
                    signY = m.group(1);
                    m.find();
                    text = m.group(1);

                    Output.write("[Sign Text Data] x: " + signX + " y: " + signY + " text: " + text);
                    boolean located = false;
                    for (int i = 0; i < WorldData.entities.size(); i++) {
                        if (WorldData.entities.get(i).getX() == Integer.parseInt(signX) && WorldData.entities.get(i).getY() == Integer.parseInt(signY)) {
                            located = true;
                            if (WorldData.entities.get(i).getName() == "sign") {
                                Sign sign = (Sign) (WorldData.entities.get(i));
                                sign.setText(text);
                            }
                            else {
                                Output.error("Entity found was not a sign");
                            }
                        }
                    }
                    if (!located) {
                        Output.error("No entity was found at that location");
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadEntityData(String mapid) {
        Output.write("Loading Entity Data...");
        FileReader FReader = null;
        try {
            FReader = new FileReader("resources/worlddata/default/"+mapid+"/entitydata.txt");
            BufferedReader BReader = new BufferedReader(FReader);

            String line = null;

            while ((line = BReader.readLine()) != null) {
                Output.write(line);

                    int part = 0;

                    String entityId = null;
                    String state = null;
                    String level = null;
                    String x = null;
                    String y = null;
                    String dialog = null;
                Boolean talk = false;


                    Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
                    m.find();
                    entityId = m.group(1);
                    m.find();
                    state = m.group(1);
                    m.find();
                    level = m.group(1);
                    m.find();
                    x = m.group(1);
                    m.find();
                    y = m.group(1);
                    try {
                        m.find();
                        dialog = m.group(1);
                        m.find();
                        if (m.group(1).contains("true")) {
                            talk = true;
                        }
                    }
                    catch (Exception e) {Output.write("No dialog data for this entity");}

                    Output.write("[Entity data] ID: " + entityId + " state: " + state + " level: " + level + " x: " + x + " y: " + y + " dialog: " + dialog + " talkable: " + talk);

                    //Create new class from the string: entityId
                Entity entity;
                   /* if (dialog == null) {
                    Constructor c = Class.forName("workexpIT.merlin.entities." + entityId).getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
                        entity = (Entity) c.newInstance(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(state), Integer.parseInt(level));
                }
                else {*/
                        Constructor c = Class.forName("workexpIT.merlin.entities." + entityId).getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE,String.class,boolean.class);
                        entity = (Entity) c.newInstance(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(state), Integer.parseInt(level),dialog,talk);
                //}
                    WorldData.entities.add(entity);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void editMap(String mapid) {
        int xSize = loadMapSizeX(mapid);
        int ySize = loadMapSizeY(mapid);
        WorldData.mapSizeX = xSize;
        WorldData.mapSizeY = ySize;
        WorldData.tiles=new Tile[WorldData.mapSizeX][WorldData.mapSizeY];
        try {
            Output.write("Reading map data: " + mapid);
            FileReader FReader = new FileReader("resources/worlddata/default/"+mapid+"/tiledata.txt");
            BufferedReader BReader = new BufferedReader(FReader);

            int x = 0;
            int y = 0;

            List<Character> data = new ArrayList<Character>();

            int value = 0;
            while((value = BReader.read()) != -1) {
                // converts int to character
                char c = (char)value;

                //If it's a new line
                if (value == 10) {
                    String id = data.toString().substring(1, data.toString().length()-1);
                    Output.write("ID = " + id);
                    //PC only v
                    if (OS.contains("win")) {
                        id = id.substring(0, id.length() - 1);
                    }
                    //PC only ^
                    if (!id.equals("")) {
                        Output.write(id + "");
                        int i = -1;
                        String rotation = null;
                        String flip = null;
                        int instance = 0;

                        try {
                            instance = Integer.parseInt(id.substring(id.length()-1));
                            flip = id.substring(id.length() - 4, id.length() - 3);
                            rotation = id.substring(id.length() - 7, id.length() - 6);
                            i = Integer.parseInt(id.substring(0, id.length() - 9));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Tile.Flip f = Tile.Flip.DEFAULT;
                        switch (flip) {
                            case "b":
                                f = Tile.Flip.HORIZONTAL;
                                break;
                            case "c":
                                f = Tile.Flip.VERTICAL;
                                break;
                        }
                        if (i > -1) {
                            switch (rotation) {
                                case "b":
                                    loadTile(i, instance, x, y, Tile.Rotation.RIGHT,f);
                                    break;
                                case "c":
                                    loadTile(i, instance, x, y, Tile.Rotation.DOWN,f);
                                    break;
                                case "d":
                                    loadTile(i, instance, x, y, Tile.Rotation.LEFT,f);
                                    break;
                                default: //this includes the "a" rotation
                                    loadTile(i, instance, x, y, Tile.Rotation.UP,f);
                                    break;
                            }
                            Output.write("Adding tile to " + x + " " + y);
                        }
                    }
                    x=0;
                    y=y+1;
                    data.clear();
                }
                //If it's a comma
                else if (c == ',') {
                    String id = data.toString().substring(1, data.toString().length() - 1);
                    if (!id.equals("")) {
                        Output.write(id + "");
                        int i = -1;
                        String rotation = null;
                        String flip = null;
                        int instance = 0;

                        try {
                            instance = Integer.parseInt(id.substring(id.length()-1));
                            flip = id.substring(id.length() - 4, id.length() - 3);
                            rotation = id.substring(id.length() - 7, id.length() - 6);
                            i = Integer.parseInt(id.substring(0, id.length() - 9));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Tile.Flip f = Tile.Flip.DEFAULT;
                        switch (flip) {
                            case "b":
                                f = Tile.Flip.HORIZONTAL;
                                break;
                            case "c":
                                f = Tile.Flip.VERTICAL;
                                break;
                        }
                        if (i > -1) {
                            switch (rotation) {
                                case "b":
                                    loadTile(i, instance, x, y, Tile.Rotation.RIGHT, f);
                                    break;
                                case "c":
                                    loadTile(i, instance, x, y, Tile.Rotation.DOWN, f);
                                    break;
                                case "d":
                                    loadTile(i, instance, x, y, Tile.Rotation.LEFT, f);
                                    break;
                                default: //this includes the "a" rotation
                                    loadTile(i, instance, x, y, Tile.Rotation.UP, f);
                                    break;
                            }
                            Output.write("Adding tile to " + x + " " + y);
                        }
                    }
                    x = x + 1;
                    data.clear();
                }
                //If it's a character
                else {
                    data.add(c);
                }
            }
        } catch (FileNotFoundException e) {
            Output.error("Could not find the specified map file: " + mapid);
            //TODO Create new world
        } catch (IOException e) {
            Output.error("IO Exception while attempting to read the map file: " + mapid);
            e.printStackTrace();
        }
        WorldData.scaledMap = JavaDrawer.loadMapIntoOneImage();
        WorldData.mapName = mapid;
        loadEntityData(mapid);
        loadMiscData(mapid);

    }

    public static void saveMap(String mapid) {
        saveTileData(mapid);
        saveEntityData(mapid);
        Output.write("Svaing complete!");
    }

    public static void saveEntityData(String mapid) {
        Output.write("Saving map " + mapid + "...");
        File file = new File("resources/worlddata/default/" + mapid + "/entitydata.txt");
        File theDir = new File("resources/worlddata/default/" + mapid);

// if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory");
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
            if (result) {
                System.out.println("DIR created");
            }
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Output.error("IO Exception while attempting to create the map file: " + mapid);
            e.printStackTrace();
        }
        FileWriter writer;
        try {
            writer = new FileWriter("resources/worlddata/default/" + mapid + "/entitydata.txt");

            for (int i = 0; i < WorldData.entities.size(); i ++) {
                Entity e = WorldData.entities.get(i);
                writer.write("("+e.getClass().getSimpleName()+")("+e.getState()+")("+e.getLevel()+")("+e.getX()+")("+e.getY()+")");
                if (e.dialog != null) {
                    writer.write("("+e.dialog+")");
                }
                writer.write(System.lineSeparator());
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            Output.error("IO Exception while attempting to write to the map file: " + mapid);
            e.printStackTrace();
        }
    }

    public static void saveTileData(String mapid) {
        Output.write("Saving map " + mapid + "...");
        File file = new File("resources/worlddata/default/"+mapid+"/tiledata.txt");
        File theDir = new File("resources/worlddata/default/"+mapid);

// if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory");
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Output.error("IO Exception while attempting to create the map file: " + mapid);
            e.printStackTrace();
        }
        FileWriter writer;
        try {
            writer = new FileWriter("resources/worlddata/default/"+mapid+"/tiledata.txt");
            for (int b = 0; b<WorldData.mapSizeY; b++) {
                for (int a = 0;a<WorldData.tiles.length; a++) {
                try {
                    String rot = "a";
                    String flip = "a";
                    int instance = WorldData.tiles[a][b].instance;
                    switch (WorldData.tiles[a][b].rotation) {
                        case UP:
                            rot = "a";
                            break;
                        case RIGHT:
                            rot = "b";
                            break;
                        case DOWN:
                            rot = "c";
                            break;
                        case LEFT:
                            rot = "d";
                            break;
                    }
                    switch (WorldData.tiles[a][b].flip) {
                        case HORIZONTAL:
                            flip = "b";
                            break;
                        case VERTICAL:
                            flip = "c";
                            break;
                    }
                    for (int i = 0; i < Reference.tileIds.length; i ++ ) {
                        if (WorldData.tiles[a][b].getClass().getSimpleName().equals(Reference.tileIds[i])) {
                            writer.write(i+rot+flip+instance+",");
                            Output.write(WorldData.tiles[a][b].getClass().getSimpleName() + " at " + a + ", " + b);
                        }
                    }
                }
                catch (NullPointerException e) {
                    //Output.write("NOTHING");
                    writer.write(",");
                }
                //writer.write();
            }
            writer.write(System.lineSeparator()); //newline
        }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Output.error("IO Exception while attempting to write to the map file: " + mapid);
            e.printStackTrace();
        }
    }

}
