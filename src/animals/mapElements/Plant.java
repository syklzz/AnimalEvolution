package animals.mapElements;

import animals.map.IObserver;

import java.util.ArrayList;

public class Plant implements IElements{
    private final Vector2d position;
    private final ArrayList<IObserver> observers = new ArrayList<>();

    public Plant(Vector2d position){
        this.position = position;
    }

    public void removePlant(){
        positionRemoved();
    }

    public void addObserver(IObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver(IObserver observer){
        int index = this.observers.indexOf(observer);
        this.observers.remove(index);
    }

    public void positionRemoved(){
        for (int i =0; i < observers.size(); i++){
            this.observers.get(i).positionRemoved(this, this.position);
        }
    }

    public Vector2d getPosition(){ return this.position; }
}
