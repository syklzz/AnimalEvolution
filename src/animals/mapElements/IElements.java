package animals.mapElements;

import animals.map.IObserver;

public interface IElements {
    void addObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void positionRemoved();
    Vector2d getPosition();
}
