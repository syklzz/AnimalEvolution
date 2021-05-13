package animals.map;

import animals.mapElements.Vector2d;
import animals.mapElements.Animal;
import animals.mapElements.Genotype;

import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private final IWorldMap map;

    private int day = 1;
    private int deadAnimalsCounter = 0;
    private int aliveAnimalsCounter = 0;
    private int plantsCounter = 0;
    private int sumEnergy = 0;
    private int sumChildren = 0;
    private int sumLifeSpan = 0;

    private int overallAliveAnimals = 0;
    private int overallPlants = 0;
    private int overallEnergy = 0;
    private int overallLifeSpan = 0;
    private int overallChildren = 0;

    private int plantsInJungle = 0;
    private int plantsOnSteppe = 0;
    private int animalsInJungle = 0;
    private int animalsOnSteppe = 0;

    Map<Genotype, Integer> genotypes = new HashMap<>();
    Map<Genotype, Integer> overallGenotypes = new HashMap<>();

    private Animal followedAnimal = null;
    private int observationStartDay;
    private int followedAnimalChildren = 0;
    private int followedAnimalDescendants = 0;

    private boolean isRelated = false;


    public Statistics(IWorldMap map){
        this.map = map;
    }



    public Animal getFollowedAnimal() {
        return this.followedAnimal;
    }

    public void setFollowedAnimal(Animal animal){
        this.followedAnimalChildren = 0;
        this.followedAnimalDescendants = 0;
        this.followedAnimal = animal;
        this.observationStartDay = this.day;
    }

    public int getFollowedAnimalChildren(){
        return this.followedAnimalChildren;
    }

    public int getFollowedAnimalDescendants(){
        return this.followedAnimalDescendants;
    }

    public boolean isChildren(Animal A){
        for(int i = 0; i < followedAnimal.getChildren().size(); i++){
            if(followedAnimal.getChildren().get(i).equals(A)){
                return true;
            }
        }
        return false;
    }

    public void isDescendant(Animal A, Animal B){
        if(!this.isRelated) {
            for (int i = 0; i < B.getChildren().size(); i++) {
                if (B.getChildren().get(i).equals(A)) {
                    this.isRelated = true;
                    break;
                }
                else{
                    isDescendant(A, (Animal)B.getChildren().get(i));
                }
            }
        }
    }

    public int getDaysUnderObservation(){
        return this.day - this.observationStartDay;
    }



    public int getDay(){ return this.day;}

    public void nextDay(){
        this.day += 1;
    }



    public String getCurrentMaxGenotype(){
        if(getMaxGenotype(genotypes) == null){
            return "";
        }
        return getMaxGenotype(genotypes).toString();
    }

    public int getElementsInJungle(){
        return this.plantsInJungle + this.animalsInJungle;
    }

    public int getElementsOnSteppe(){
        return this.plantsOnSteppe + this.animalsOnSteppe;
    }

    public int getAliveAnimalsCounter(){ return this.aliveAnimalsCounter;}

    public int getPlantsCounter(){return this.plantsCounter;}

    public int getAverageEnergy(){
        if(this.aliveAnimalsCounter == 0){ return 0;}
        return this.sumEnergy/this.aliveAnimalsCounter;
    }

    public int getAverageLifeSpan(){
        if(this.deadAnimalsCounter == 0){ return 0;}
        return this.sumLifeSpan/this.deadAnimalsCounter;
    }

    public int getAverageChildrenAmount(){
        if(this.aliveAnimalsCounter == 0){ return 0;}
        return this.sumChildren/this.aliveAnimalsCounter;
    }




    public void onPositionChanged(Vector2d oldPosition, Vector2d newPosition){
        if(this.map.isInJungle(oldPosition)){
            this.plantsInJungle --;
        }
        else{
            this.plantsOnSteppe --;
        }
        if(this.map.isInJungle(newPosition)){
            this.plantsInJungle ++;
        }
        else{
            this.plantsOnSteppe ++;
        }
    }

    public void onAnimalAddition(Animal A){
        this.aliveAnimalsCounter ++;
        this.sumEnergy += A.getEnergy();
        addGenotype(A.getGenotype(), genotypes);
        if(this.map.isInJungle(A.getPosition())){
            this.plantsInJungle ++;
        }
        else{
            this.plantsOnSteppe ++;
        }
        if(this.followedAnimal != null){
            if(isChildren(A)){this.followedAnimalChildren ++;}

            this.isRelated = false;
            isDescendant(A, followedAnimal);
            if(isRelated){this.followedAnimalDescendants ++;}
            this.isRelated = false;
        }

    }

    public void onPlantRemoval(Vector2d position){
        this.plantsCounter --;
        if(this.map.isInJungle(position)){
            this.plantsInJungle --;
        }
        else{
            this.plantsOnSteppe --;
        }
    }

    public void onPlantAddition(Vector2d position){
        this.plantsCounter ++;
        if(this.map.isInJungle(position)){
            this.plantsInJungle ++;
        }
        else{
            this.plantsOnSteppe ++;
        }
    }

    public void onAnimalRemoval(Animal A){
        this.deadAnimalsCounter ++;
        this.aliveAnimalsCounter --;
        this.sumEnergy -= A.getEnergy();
        this.sumChildren -= A.getChildren().size();
        this.sumLifeSpan += A.getLifespan();
        removeGenotype(A.getGenotype());
        if(this.map.isInJungle(A.getPosition())){
            this.plantsInJungle --;
        }
        else{
            this.plantsOnSteppe --;
        }

    }

    public void onEnergyChange(int n){ this.sumEnergy += n;}

    public void onChildAddition(){ this.sumChildren += 1; }




    public void updateOverallStatistics(){
        this.overallAliveAnimals += this.aliveAnimalsCounter;
        this.overallPlants += this.plantsCounter;
        this.overallEnergy +=  this.sumEnergy / this.day;
        this.overallLifeSpan += this.sumLifeSpan / this.day;
        this.overallChildren += this.sumChildren / this.day;

        for (Map.Entry<Genotype, Integer> item : genotypes.entrySet()) {
            Genotype genotype = item.getKey();
            Integer amount = item.getValue();
            for(int i = 0; i < amount; i++){
                addGenotype(genotype, overallGenotypes);
            }
        }

    }

    public String getOverallAliveAnimals() {
        return String.valueOf(this.overallAliveAnimals / this.day);
    }

    public String getOverallPlants(){
        return String.valueOf(this.overallPlants / this.day);
    }

    public String getOverallEnergy(){
        return String.valueOf(this.overallEnergy / this.day);
    }

    public String getOverallLifeSpan(){
        return String.valueOf(this.overallLifeSpan / this.day);
    }

    public String getOverallChildren() {
        return String.valueOf(this.overallChildren / this.day);
    }

    public String getOverallMaxGenotype() {
        if(getMaxGenotype(overallGenotypes) == null){
            return "";
        }
        return getMaxGenotype(overallGenotypes).toString();
    }



    public Genotype getDominantGenotype(){
        Genotype maxGenotype = null;
        int maxGenotypeCounter = 0;

        for (Map.Entry<Genotype, Integer> item : genotypes.entrySet()) {
            Genotype genotype = item.getKey();
            Integer amount = item.getValue();
            if (amount > maxGenotypeCounter) {
                maxGenotypeCounter = amount;
                maxGenotype = genotype;
            }
        }
        return maxGenotype;
    }

    public void addGenotype(Genotype G, Map<Genotype, Integer> genotypes){
        if(genotypes.get(G) == null){
            genotypes.put(G, 0);
        }
        int amount = genotypes.get(G);
        genotypes.remove(G);
        genotypes.put(G, amount+1);
    }

    public void removeGenotype(Genotype G){
        if(genotypes.get(G) == null){
            genotypes.put(G, 0);
        }
        int amount = genotypes.get(G);
        genotypes.remove(G);
        if(amount > 1){
            genotypes.put(G, amount-1);
        }
    }

    public Genotype getMaxGenotype(Map<Genotype, Integer> genotypes){
        Genotype maxGenotype = null;
        int maxGenotypeCounter = 0;

        for (Map.Entry<Genotype, Integer> item : genotypes.entrySet()) {
            Genotype genotype = item.getKey();
            Integer amount = item.getValue();
            if (amount > maxGenotypeCounter) {
                maxGenotypeCounter = amount;
                maxGenotype = genotype;
            }
        }
        return maxGenotype;
    }

}
