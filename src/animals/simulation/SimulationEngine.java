package animals.simulation;

import animals.mapElements.Plant;
import animals.mapElements.Vector2d;
import animals.visualization.Visualization;
import animals.mapElements.Animal;
import animals.map.IWorldMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.Timer;

public class SimulationEngine implements IEngine {

    private final IWorldMap map;
    private final IWorldMap map2;
    int startEnergy;
    int moveEnergy;
    int plantsEnergy;

    Visualization visualization;
    Timer timer;
    Timer timer2;
    int delay;

    JButton startButton;
    JButton startButton2;
    JButton stopButton;
    JButton stopButton2;

    public SimulationEngine(IWorldMap map, IWorldMap map2, int animalsNumber, int startEnergy, int moveEnergy, int plantsEnergy, int delay){
        this.map = map;
        this.map2 = map2;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantsEnergy = plantsEnergy;

        placeAnimals(animalsNumber);

        this.delay = delay;
        this.timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(map.getStatistics().getAliveAnimalsCounter() == 0){
                    timer.stop();
                    return;
                }
                visualization.update();
                removeDead(map);
                moveAnimals(map);
                eat(map);
                reproduce(map);
                addPlants(map);
                map.getStatistics().updateOverallStatistics();
                map.getStatistics().nextDay();
            }
        });

        this.timer2 = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(map2.getStatistics().getAliveAnimalsCounter() == 0){
                    timer2.stop();
                    return;
                }
                visualization.update();
                removeDead(map2);
                moveAnimals(map2);
                eat(map2);
                reproduce(map2);
                addPlants(map2);
                map2.getStatistics().updateOverallStatistics();
                map2.getStatistics().nextDay();
            }
        });

        this.startButton = new JButton("Start");
        this.startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                timer.start();
            }
        });
        this.startButton2 = new JButton("Start");
        this.startButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                timer2.start();
            }
        });
        this.stopButton = new JButton("Stop");
        this.stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                timer.stop();
            }
        });
        this.stopButton2 = new JButton("Stop");
        this.stopButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                timer2.stop();
            }
        });

        this.visualization = new Visualization(this.map, this.map2, startButton, startButton2, stopButton, stopButton2);

    }

    public void run() {
        visualization.setFrame();
        timer.start();
        timer2.start();
    }

    void removeDead(IWorldMap map){
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

    void moveAnimals(IWorldMap map){
        List<Animal> animalsList = new ArrayList<>();
        for (Map.Entry<Vector2d, Set<Animal>> item : map.getAnimals().entrySet()){
            Set<Animal> animals = item.getValue();
            for (Animal animal : animals) { animalsList.add(animal); }
        }
        for(Animal animal : animalsList){
            animal.move();
            animal.reduceEnergy(this.moveEnergy);
        }
    }

    void eat(IWorldMap map){
        List<Plant> plantsToBeRemoved = new ArrayList<>();
        for (Map.Entry<Vector2d, Plant> item : map.getPlants().entrySet()){
            Vector2d key = item.getKey();
            Plant plant = item.getValue();

            Set<Animal> animals = map.getAnimalsAt(key);
            if(animals != null) {

                Animal A = animals.stream().max((a,b) -> Integer.compare(a.getEnergy(), b.getEnergy())).orElse(null);
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

    void reproduce(IWorldMap map){
        List<Animal> children = new ArrayList<>();

        for (Map.Entry<Vector2d, Set<Animal>> item : map.getAnimals().entrySet()){
            Vector2d key = item.getKey();
            Set<Animal> animals = item.getValue();

            List <Animal> animals2 = animals.stream().filter(a -> a.getEnergy() >= startEnergy / 2).collect(Collectors.toList());

            if (animals2.size() > 1) {

                Animal animal = animals2.stream().max((a,b) -> Integer.compare(a.getEnergy(), b.getEnergy())).orElse(null);
                int maxEnergy = animal.getEnergy();
                Random r = new Random();
                Animal A;
                Animal B;

                if(animals2.stream().filter(a -> a.getEnergy() == maxEnergy).collect(Collectors.toList()).size() > 1){
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
                    animal = animals2.stream().max((a,b) -> Integer.compare(a.getEnergy(), b.getEnergy())).orElse(null);
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

                if (surroundings.stream().filter(a -> !map.isOccupied(a)).collect(Collectors.toList()).size() > 0) {
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

    void addPlants(IWorldMap map){
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

                Plant plant1 = new Plant(position, this.plantsEnergy);
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
                Plant plant2 = new Plant(position, this.plantsEnergy);
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
            Animal animal2 = new Animal(position, this.startEnergy);
            this.map.place(animal, position);
            this.map2.place(animal2, position);
        }
    }


}

