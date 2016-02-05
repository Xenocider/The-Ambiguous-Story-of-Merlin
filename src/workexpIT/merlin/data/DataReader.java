package workexpIT.merlin.data;

import workexpIT.merlin.Output;
import workexpIT.merlin.tiles.Tile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ict11 on 2016-02-03.
 */
public class DataReader {

    public static void loadMap(String mapid) {
        try {
            FileReader FReader = new FileReader(mapid);
            BufferedReader BReader = new BufferedReader(FReader);

            int x = 0;
            int y = 0;

            List<Character> data = new ArrayList<Character>();

            int value = 0;
            while((value = BReader.read()) != -1) {
                // converts int to character
                char c = (char)value;
                Output.write(value + ":" + c);

                //If it's a new line
                if (value == 10) {
                    String id = data.toString().substring(1, data.toString().length()-1);
                    id = id.substring(0, id.length()-3);
                    Output.write(id);
                    int i = Integer.parseInt(id);
                    Tile tile = new Tile(i);
                    WorldData.tiles[x][y] = tile;
                    Output.write("Adding tile to " + x + " " + y);
                    x=0;
                    y=y+1;
                    data.clear();
                }
                //If it's a comma
                else if (c == ',') {
                    String id = data.toString().substring(1, data.toString().length()-1);
                    int i = Integer.parseInt(id);
                    Tile tile = new Tile(i);
                    WorldData.tiles[x][y] = tile;
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
