package animals;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import animals.map.IWorldMap;
import animals.map.RectangularMap;
import animals.mapElements.Vector2d;
import animals.simulation.IEngine;
import animals.simulation.SimulationEngine;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main{
    public static void main(String[] args){

        JSONParser parser = new JSONParser();

        int delay = 100;

        try {

            Object obj = parser.parse(new FileReader("src/animals/json/data.json"));
            JSONObject jsonObject = (JSONObject) obj;

            int width = Integer.parseInt((String) jsonObject.get("width"));
            int height = Integer.parseInt((String) jsonObject.get("height"));
            int startEnergy = Integer.parseInt((String) jsonObject.get("startEnergy"));
            int moveEnergy = Integer.parseInt((String) jsonObject.get("moveEnergy"));
            int plantsEnergy = Integer.parseInt((String) jsonObject.get("plantsEnergy"));
            int animalsNumber = Integer.parseInt((String) jsonObject.get("animalsNumber"));
            double jungleRatio = Double.parseDouble((String) jsonObject.get("jungleRatio"));

            int jungleWidth = (int) (width * jungleRatio);
            int jungleHeight = (int) (height * jungleRatio);
            int x1 = (width / 2) - (jungleWidth / 2);
            int x2 = x1 + jungleWidth;
            int y1 = (height / 2) - (jungleHeight / 2);
            int y2 = y1 + jungleHeight;


            IWorldMap map = new RectangularMap(width, height, new Vector2d(x1, y1), new Vector2d(x2, y2));
            IWorldMap map2 = new RectangularMap(width, height, new Vector2d(x1, y1), new Vector2d(x2, y2));
            IEngine e = new SimulationEngine(map, map2, animalsNumber, startEnergy, moveEnergy, plantsEnergy, delay);
            e.run();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
