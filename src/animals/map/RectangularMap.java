package animals.map;

import animals.mapElements.Plant;
import animals.mapElements.Vector2d;
import animals.mapElements.Animal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RectangularMap implements IWorldMap, IObserver {

    private final Vector2d lowerJungleCoord;
    private final Vector2d upperJungleCoord;
    private final Statistics statistics;
    private final int width;
    private final int height;

    Map<Vector2d, Set<Animal>> animals = new HashMap<>();
    Map<Vector2d, Plant> plants = new HashMap<>();

    public RectangularMap(int width, int height, Vector2d lowerCoord, Vector2d upperCoord) {
        this.width = width;
        this.height = height;
        this.lowerJungleCoord = lowerCoord;
        this.upperJungleCoord = upperCoord;
        this.statistics = new Statistics(this);
    }

    public void place(Object o, Vector2d position) {
        position = adjustPosition(position);
        if (o instanceof Plant) {
            this.plants.put(((Plant) o).getPosition(), (Plant) o);
            ((Plant) o).addObserver(this);
            this.statistics.onPlantAddition(position);
        }
        else if (o instanceof Animal) {
            if (animals.get(position) == null) {
                this.animals.put(position, new HashSet<>());
            }
            this.animals.get(position).add((Animal) o);
            ((Animal) o).addObserver(this);
            this.statistics.onAnimalAddition((Animal)o);
        }
    }

    public boolean isOccupied(Vector2d position) {
        position = adjustPosition(position);
        return this.animals.get(position) != null || plants.get(position) != null;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object o) {
        oldPosition = adjustPosition(oldPosition);
        newPosition = adjustPosition(newPosition);
        if (o instanceof Animal) {
            this.animals.get(oldPosition).remove(o);
            if (this.animals.get(oldPosition).size() == 0) {
                this.animals.remove(oldPosition);
            }
            if (animals.get(newPosition) == null) {
                this.animals.put(newPosition, new HashSet<>());
            }
            this.animals.get(newPosition).add((Animal) o);
        }

        this.statistics.onPositionChanged(oldPosition,newPosition);
    }

    public void positionRemoved(Object o, Vector2d position) {
        position = adjustPosition(position);
        if (o instanceof Plant) {
            this.plants.remove(position);
            this.statistics.onPlantRemoval(position);
        }
        if (o instanceof Animal) {
            this.animals.get(position).remove(o);
            if (this.animals.get(position).size() == 0) {
                this.animals.remove(position);
            }
            this.statistics.onAnimalRemoval((Animal)o);
        }
    }

    public void energyChanged(int n){
        this.statistics.onEnergyChange(n);
    }

    public void childrenChanged(){
        this.statistics.onChildAddition();
    }

    public Map<Vector2d, Set<Animal>> getAnimals() {
        return this.animals;
    }

    public Map<Vector2d, Plant> getPlants() {
        return this.plants;
    }

    public Set<Animal> getAnimalsAt(Vector2d position) {
        return animals.get(position);
    }

    public Vector2d getLowerJungleCoord() {
        return this.lowerJungleCoord;
    }

    public Vector2d getUpperJungleCoord() {
        return this.upperJungleCoord;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Statistics getStatistics(){ return this.statistics;}


    public Vector2d adjustPosition(Vector2d position) {
        int x = position.x;
        int y = position.y;
        if (position.x < 0) {
            x = (this.width + 1) - ( (- position.x) % (this.width + 1) );
        }
        if (position.x > this.width) {
            x = position.x % (this.width + 1);
        }
        if (position.y < 0) {
            y = (this.height + 1) - ( (- position.y) % (this.height + 1) );
        }
        if (position.y > this.height) {
            y = position.y % (this.height + 1);
        }
        return new Vector2d(x, y);
    }

    public boolean isInJungle(Vector2d position){
        position = adjustPosition(position);
        if (position.follows(this.lowerJungleCoord) && position.precedes(this.upperJungleCoord)){
            return true;
        }
        return false;
    }



}
