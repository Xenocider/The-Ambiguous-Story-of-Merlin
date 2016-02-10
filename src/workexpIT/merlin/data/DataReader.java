package workexpIT.merlin.data;

import workexpIT.merlin.Output;
import workexpIT.merlin.Reference;
import workexpIT.merlin.entities.Bob;
import workexpIT.merlin.tiles.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ict11 on 2016-02-03.
 */
public class DataReader {

    public static void loadMap(String mapid) {
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
                    //id = id.substring(0, id.length()-3);
                    Output.write(id+"");
                    int i = Integer.parseInt(id);
                    loadTile(i,x,y);
                    Output.write("Adding tile to " + x + " " + y);
                    x=0;
                    y=y+1;
                    data.clear();
                }
                //If it's a comma
                else if (c == ',') {
                    String id = data.toString().substring(1, data.toString().length() - 1);
                    int i = Integer.parseInt(id);
                    loadTile(i,x,y);
                    Output.write("Adding tile to " + x + " " + y);
                    x=x+1;
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
        loadMiscData(mapid);
        loadEntityData(mapid);
    }

    private static void loadTile(int id, int x, int y) {
        Tile tile = null;
        for (int i=0; i< Reference.numOfMaterials; i++) {
            if (id == Reference.grass) {
                tile = new Grass();
            }
            if (id == Reference.dirt) {
                tile = new Dirt();
            }
        }
        if (tile == null) {
            Output.error("Could not load a tile with that id: " + id);
        }
        try {WorldData.tiles[x][y] = tile;}
        catch (ArrayIndexOutOfBoundsException e) {
            Output.error("Map data too large! Max size is " + Reference.mapSize + " by " + Reference.mapSize);
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
                //if (entityId == "bob") {
                    //Output.write("good");
                    WorldData.entities.add(new Bob(Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(state),Integer.parseInt(level)));
                //}
                //else {Output.write(entityId);}
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*public static void saveMap(String mapid) {
        File file = new File(mapid);
        try {
            file.createNewFile();
        } catch (IOException e) {
            Output.error("IO Exception while attempting to create the map file: " + mapid);
            e.printStackTrace();
        }
        FileWriter writer;
        try {
            writer = new FileWriter(mapid);
            writer.write(//TODO save map data)
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Output.error("IO Exception while attempting to write to the map file: " + mapid);
            e.printStackTrace();
        }

    }*/

}
