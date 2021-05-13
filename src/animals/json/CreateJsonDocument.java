package animals.json;

import java.io.FileWriter;
import java.io.IOException;

import animals.map.Statistics;
import org.json.simple.JSONObject;

public class CreateJsonDocument {

    private final Statistics stats;
    private final String fileName;

    public CreateJsonDocument(Statistics stats, String fileName){
        this.stats = stats;
        this.fileName = fileName;
    }

    public void create() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("animals", this.stats.getOverallAliveAnimals());
        jsonObject.put("plants", this.stats.getOverallPlants());
        jsonObject.put("energy", this.stats.getOverallEnergy());
        jsonObject.put("lifespan", this.stats.getOverallLifeSpan());
        jsonObject.put("children", this.stats.getOverallChildren());
        jsonObject.put("genotype", this.stats.getOverallMaxGenotype());
        try {
            FileWriter file = new FileWriter(fileName);
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
