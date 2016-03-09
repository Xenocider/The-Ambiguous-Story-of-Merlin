package workexpIT.merlin.data;

import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.entities.Entity;
import workexpIT.merlin.graphics.Drawer;
import workexpIT.merlin.tiles.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ict11 on 2016-02-03.
 */
public class DataReader {

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
                    //PC only v
                    //if (Merlin.platform.equals("pc")) {id = id.substring(0, id.length()-3);}
                    //PC only ^
                    if (!id.equals("")) {
                        Output.write(id + "");
                        int i = -1;
                        String rotation = null;
                        String flip = null;
                        try {
                            flip = id.substring(id.length()-1);
                            rotation = id.substring(id.length()-4,id.length()-3);
                            i = Integer.parseInt(id.substring(0,id.length()-6));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Tile.Flip f = null;
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
                                    loadTile(i, x, y, Tile.Rotation.RIGHT,f);
                                    break;
                                case "c":
                                    loadTile(i, x, y, Tile.Rotation.DOWN,f);
                                    break;
                                case "d":
                                    loadTile(i, x, y, Tile.Rotation.LEFT,f);
                                    break;
                                default: //this includes the "a" rotation
                                    loadTile(i, x, y, Tile.Rotation.UP,f);
                                    break;
                            }
                            Output.write("Adding tile to " + x + " " + y);

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
                        try {
                            flip = id.substring(id.length()-1);
                            rotation = id.substring(id.length()-4,id.length()-3);
                            i = Integer.parseInt(id.substring(0,id.length()-6));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Tile.Flip f = null;
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
                                        loadTile(i, x, y, Tile.Rotation.RIGHT,f);
                                        break;
                                    case "c":
                                        loadTile(i, x, y, Tile.Rotation.DOWN,f);
                                        break;
                                    case "d":
                                        loadTile(i, x, y, Tile.Rotation.LEFT,f);
                                        break;
                                    default: //this includes the "a" rotation
                                        loadTile(i, x, y, Tile.Rotation.UP,f);
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
        WorldData.mapName = mapid;
        loadMiscData(mapid);
        loadEntityData(mapid);
        WorldData.battleBackground = ImageReader.loadImage("resources/graphics/backgrounds/" + mapid + ".png");
        //Center camera
        try {
            Drawer.setCamera((-WorldData.getPlayer().getX() + Drawer.ww / 2 / Drawer.w) * Drawer.w, (-WorldData.getPlayer().getY() + Drawer.wh / 2 / Drawer.h) * Drawer.h);
        } catch (Exception e) {
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return x;
    }

    private static void loadTile(int id, int x, int y, Tile.Rotation rotation, Tile.Flip f) {
        Tile tile = null;
        Constructor c = null;
        try {
            tile = (Tile) Class.forName("workexpIT.merlin.tiles."+ Reference.tileIds[id]).newInstance();
            tile.setRotation(rotation);
            tile.setFlip(f);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
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

            while ((line = BReader.readLine()) != null) {
                Output.write(line);
                if (line.contains("doors")) {
                    Output.write("Found door code indicator");

                        int part = 0;

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

                    Output.write("[Entity data] ID: " + entityId + " state: " + state + " level: " + level + " x: " + x + " y: " + y);

                    //Create new class from the string: entityId
                    Constructor c = Class.forName("workexpIT.merlin.entities."+entityId).getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
                    Entity entity = (Entity) c.newInstance(Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(state),Integer.parseInt(level));
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
                    //PC only v
                    //if (Merlin.platform.equals("pc")) {id = id.substring(0, id.length()-3);}
                    //PC only ^
                    if (!id.equals("")) {
                        Output.write(id + "");
                        int i = -1;
                        String rotation = null;
                        String flip = null;
                        try {
                            flip = id.substring(id.length()-1);
                            rotation = id.substring(id.length()-4,id.length()-3);
                            i = Integer.parseInt(id.substring(0,id.length()-6));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Tile.Flip f = null;
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
                                    loadTile(i, x, y, Tile.Rotation.RIGHT,f);
                                    break;
                                case "c":
                                    loadTile(i, x, y, Tile.Rotation.DOWN,f);
                                    break;
                                case "d":
                                    loadTile(i, x, y, Tile.Rotation.LEFT,f);
                                    break;
                                default: //this includes the "a" rotation
                                    loadTile(i, x, y, Tile.Rotation.UP,f);
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
                        try {
                            flip = id.substring(id.length() - 1);
                            rotation = id.substring(id.length() - 4, id.length() - 3);
                            i = Integer.parseInt(id.substring(0, id.length() - 6));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Tile.Flip f = null;
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
                                    loadTile(i, x, y, Tile.Rotation.RIGHT, f);
                                    break;
                                case "c":
                                    loadTile(i, x, y, Tile.Rotation.DOWN, f);
                                    break;
                                case "d":
                                    loadTile(i, x, y, Tile.Rotation.LEFT, f);
                                    break;
                                default: //this includes the "a" rotation
                                    loadTile(i, x, y, Tile.Rotation.UP, f);
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
        WorldData.mapName = mapid;
        loadMiscData(mapid);
        loadEntityData(mapid);
    }

    public static void saveMap(String mapid) {
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
                    for (int i = 0; i < Reference.tileIds.length; i ++ ) {
                        if (WorldData.tiles[a][b].getClass().getSimpleName().equals(Reference.tileIds[i])) {
                            writer.write(i+",");
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
