package animals.simulation;

import animals.mapElements.Plant;
import animals.mapElements.Vector2d;
import animals.visualization.Visualization;
import animals.mapElements.Animal;
import animals.map.IWorldMap;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.Timer;

public class SimulationEngine implements IEngine {
    private final IWorldMap map;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantsEnergy;
    private Visualization visualization;
    private final Timer timer;

    public SimulationEngine(IWorldMap map, int animalsNumber, int startEnergy, int moveEnergy, int plantsEnergy, int delay){
        this.map = map;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantsEnergy = plantsEnergy;

        placeAnimals(animalsNumber);

        this.timer = new Timer(delay, e -> {
            if(this.map.getStatistics().getAliveAnimalsCounter() == 0){
                stopTimer();
                return;
            }
            visualization.update();
            removeDead();
            moveAnimals();
            eat();
            reproduce();
            addPlants();
            this.map.getStatistics().updateOverallStatistics();
            this.map.getStatistics().nextDay();
        });

        this.visualization = new Visualization(this.map, this);
    }

    public void run() {
        visualization.setFrame();
    }

    public void stopTimer(){
        timer.stop();
    }

    public void runTimer(){
        timer.start();
    }

    void removeDead(){
        List<Animal> animalsToBeRemoved = new ArrayList<>();
        for (Map.Entry<Vector2d, Set<Animal>> item : map.getAnimals().entrySet()){
            Set<Animal> animals = item.getValue();
            for (Animal animal : animals) {
                if(animal.isDead()){ animalsToBeRemoved.add(animal);}
            }
        }
        for(Animal animal : animalsToBeRemoved){
            animal.setDeath(map.getStatistics().getDay());
        }
    }

    void moveAnimals(){
        List<Animal> animalsList = new ArrayList<>();
        for (Map.Entry<Vector2d, Set<Animal>> item : map.getAnimals().entrySet()){
            Set<Animal> animals = item.getValue();
            animalsList.addAll(animals);
        }
        for(Animal animal : animalsList){
            animal.move();
            animal.reduceEnergy(this.moveEnergy);
        }
    }

    void eat(){
        List<Plant> plantsToBeRemoved = new ArrayList<>();
        for (Map.Entry<Vector2d, Plant> item : map.getPlants().entrySet()){
            Vector2d key = item.getKey();
            Plant plant = item.getValue();

            Set<Animal> animals = map.getAnimalsAt(key);
            if(animals != null) {
                Animal A = animals.stream().max(Comparator.comparingInt(Animal::getEnergy)).orElse(null);
                int maxEnergy = A.getEnergy();
                animals = animals.stream().filter(a -> a.getEnergy() == maxEnergy).collect(Collectors.toSet());
                for (Animal animal : animals) {
                    animal.increaseEnergy(plantsEnergy / animals.size());
                }
                plantsToBeRemoved.add(plant);
            }
        }
        for(Plant plant : plantsToBeRemoved){
            plant.removePlant();
        }
    }

    void reproduce(){
        List<Animal> children = new ArrayList<>();

        for (Map.Entry<Vector2d, Set<Animal>> item : map.getAnimals().entrySet()){
            Vector2d key = item.getKey();
            Set<Animal> animals = item.getValue();

            List <Animal> animals2 = animals.stream().filter(a -> a.getEnergy() >= startEnergy / 2).collect(Collectors.toList());

            if (animals2.size() > 1) {

                Animal animal = animals2.stream().max(Comparator.comparingInt(Animal::getEnergy)).orElse(null);
                int maxEnergy = animal.getEnergy();
                Random r = new Random();
                Animal A;
                Animal B;

                if(animals2.stream().filter(a -> a.getEnergy() == maxEnergy).count() > 1){
                    animals2 = animals2.stream().filter(a -> a.getEnergy() == maxEnergy).collect(Collectors.toList());
                    int index = r.nextInt(animals2.size());
                    int index2 = r.nextInt(animals2.size());
                    while(index == index2){index2 = r.nextInt(animals2.size());}
                    A = animals2.get(index);
                    B = animals2.get(index2);
                }
                else{
                    A = animal;
                    animals2 = animals2.stream().filter(a -> a.getEnergy() != maxEnergy).collect(Collectors.toList());
                    animal = animals2.stream().max(Comparator.comparingInt(Animal::getEnergy)).orElse(null);
                    int maxEnergy2 = animal.getEnergy();
                    animals2 = animals2.stream().filter(a -> a.getEnergy() == maxEnergy2).collect(Collectors.toList());
                    int index = r.nextInt(animals2.size());
                    B = animals2.get(index);
                }

                List<Vector2d> surroundings = new ArrayList<>();
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) {
                            Vector2d v = new Vector2d(i, j);
                            surroundings.add(v.add(key));
                        }
                    }
                }

                if (surroundings.stream().anyMatch(a -> !map.isOccupied(a))) {
                    surroundings = surroundings.stream().filter(a -> !map.isOccupied(a)).collect(Collectors.toList());
                }
                int index = r.nextInt(surroundings.size());
                A.reduceEnergy(A.getEnergy()/4);
                B.reduceEnergy((B.getEnergy()/4));
                Animal C = new Animal(A, B, map.getStatistics().getDay(), surroundings.get(index), this.startEnergy);
                children.add(C);
            }

        }
        for(Animal child : children){
            map.place(child, child.getPosition());
        }
    }

    void addPlants(){
        Vector2d position;
        Random r = new Random();
        Vector2d lowerCoord = map.getLowerJungleCoord();
        Vector2d upperCoord = map.getUpperJungleCoord();
        int width = map.getWidth();
        int height = map.getHeight();
        int widthInJungle = upperCoord.x - lowerCoord.x;
        int heightInJungle = upperCoord.y - lowerCoord.y;
        int positionsInJungle = (widthInJungle + 1) * (heightInJungle + 1);
        int positionsOnSteppe = (width + 1) * (height + 1) - positionsInJungle;

        int counter = 0;

        while(counter < 2) {
            if (map.getStatistics().getElementsInJungle() < positionsInJungle) {
                do {
                    int x = r.nextInt(upperCoord.x + 1 - lowerCoord.x) + lowerCoord.x;
                    int y = r.nextInt(upperCoord.y + 1 - lowerCoord.y) + lowerCoord.y;
                    position = new Vector2d(x, y);
                } while (map.isOccupied(position));

                Plant plant1 = new Plant(position);
                map.place(plant1, position);
            }
            counter ++;
        }
        counter = 0;

        while(counter < 2) {
            if (map.getStatistics().getElementsOnSteppe() < positionsOnSteppe) {
                do {
                    int x, y;
                    do {
                        x = r.nextInt(width + 1);
                        y = r.nextInt(height + 1);
                        position = new Vector2d(x, y);
                    } while (map.isInJungle(position));
                } while (map.isOccupied(position));
                Plant plant2 = new Plant(position);
                map.place(plant2, position);
            }
            counter ++;
        }
    }

    void placeAnimals(int animalsAmount){
        Random r = new Random();
        int width = this.map.getWidth();
        int height = this.map.getHeight();
        Vector2d position;
        for(int i=0; i<animalsAmount; i++){
            int x,y;
            do{
                x = r.nextInt(width);
                y = r.nextInt(height);
                position = new Vector2d(x ,y);
            }while(this.map.isOccupied(position));
            Animal animal = new Animal(position, this.startEnergy);
            this.map.place(animal, position);
        }
    }
}
