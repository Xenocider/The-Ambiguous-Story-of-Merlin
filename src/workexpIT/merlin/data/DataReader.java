package workexpIT.merlin.data;

import workexpIT.merlin.Merlin;
import workexpIT.merlin.Output;
import workexpIT.merlin.WorldData;
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
                System.out.println(value + ":" + c);

                //If it's a new line
                if (value == 10) {
                    Tile tile = new Tile(data.toString());
                    WorldData.tiles[x][y] = tile;
                    x=0;
                    y=y+1;
                    data.clear();
                }
                //If it's a comma
                else if (c == ',') {
                    Tile tile = new Tile(data.toString());
                    WorldData.tiles[x][y] = tile;
                    x=x+1;
                    data.clear();
                }
                //If it's a character
                else {
                    data.add(c);
                }
            }
        } catch (FileNotFoundException e) {
            Output.write("Could not find the specified map file: " + mapid);
            e.printStackTrace();
        } catch (IOException e) {
            Output.write("IO Exception while attempting to read the map file: " + mapid);
            e.printStackTrace();
        }
    }

    /*public static void saveMap(String mapid) {
        File file = new File(mapid);
        try {
            file.createNewFile();
        } catch (IOException e) {
            Output.write("IO Exception while attempting to create the map file: " + mapid);
            e.printStackTrace();
        }
        FileWriter writer;
        try {
            writer = new FileWriter(mapid);
            writer.write(//TODO save map data)
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Output.write("IO Exception while attempting to write to the map file: " + mapid);
            e.printStackTrace();
        }

    }*/

}
