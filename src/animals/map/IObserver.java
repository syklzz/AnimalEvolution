package animals.map;

import animals.mapElements.Vector2d;

public interface IObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object o);
    void positionRemoved(Object o, Vector2d position);
    void energyChanged(int n);
    void childrenChanged();
}
