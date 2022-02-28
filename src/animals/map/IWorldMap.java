package animals.map;

import animals.mapElements.Plant;
import animals.mapElements.Vector2d;
import animals.mapElements.Animal;

import java.util.Map;
import java.util.Set;

public interface IWorldMap {
    void place(Object o, Vector2d position);
    boolean isOccupied(Vector2d position);
    Map<Vector2d, Set<Animal>> getAnimals();
    Map<Vector2d, Plant> getPlants();
    Set<Animal> getAnimalsAt(Vector2d position);
    Vector2d adjustPosition(Vector2d v);
    boolean isInJungle(Vector2d position);
    Vector2d getLowerJungleCoord();
    Vector2d getUpperJungleCoord();
    int getWidth();
    int getHeight();
    Statistics getStatistics();
}
