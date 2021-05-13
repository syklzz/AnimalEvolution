package animals.mapElements;

import animals.map.IObserver;

import java.awt.*;
import java.util.*;

public class Animal implements IElements{

    private final int birth;
    private int death = -1;
    private int energy;
    private final int startEnergy;
    private final Genotype genotype;
    private final ArrayList<Animal> children = new ArrayList<>();
    private MapDirection orientation;
    private Vector2d position;
    private final ArrayList<IObserver> observers = new ArrayList<>();

    public Animal(Vector2d initialPosition, int energy){
        this.orientation = MapDirection.getOrientation();
        this.birth = 1;
        this.startEnergy = energy;
        this.energy = energy;
        this.genotype = new Genotype();
        this.orientation = genotype.newOrientation(this.orientation);
        this.position = initialPosition;

    }

    public Animal(Animal A, Animal B, int day, Vector2d initialPosition, int StartEnergy){
        this.orientation = MapDirection.getOrientation();
        this.startEnergy = StartEnergy;
        this.energy = A.getEnergy()/4;
        this.energy += B.getEnergy()/4;
        this.birth = day;
        this.genotype = new Genotype(A.getGenotype(), B.getGenotype());
        this.orientation =  this.genotype.newOrientation(this.orientation);
        this.position = initialPosition;
        A.addChild(this);
        B.addChild(this);
    }

    public void move(){
        this.orientation = genotype.newOrientation(this.orientation);
        Vector2d oldPosition = this.position;
        Vector2d newPosition = this.position.add(this.orientation.toUnitVector());
        positionChanged(oldPosition, newPosition, this);
        this.position = newPosition;
    }


    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Object o){
        for (int i =0; i < observers.size(); i++){
            this.observers.get(i).positionChanged(oldPosition, newPosition, this);
        }
    }

    public void positionRemoved(){
        for (int i =0; i < observers.size(); i++){
            this.observers.get(i).positionRemoved(this, this.position);
        }
    }

    public void energyChanged(int n){
        for (int i = 0; i < observers.size(); i++){
            this.observers.get(i).energyChanged(n);
        }
    }

    public void childrenChanged(){
        for (int i = 0; i < observers.size(); i++){
            this.observers.get(i).childrenChanged();
        }
    }

    public void addObserver(IObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver(IObserver observer){
        int index = this.observers.indexOf(observer);
        this.observers.remove(index);
    }


    public Vector2d getPosition(){ return this.position; }

    public int getEnergy(){ return this.energy; }

    public int getLifespan(){ return this.death - this.birth;}

    public Genotype getGenotype(){ return this.genotype; }

    public ArrayList<Animal> getChildren(){ return this.children; }

    public String getDeathDay(){
        if(this.death == -1){
            return "";
        }
        return String.valueOf(this.death);
    }

    public void setDeath(Integer n){
        this.death = n;
        positionRemoved();
    }

    public boolean isDead(){ return this.energy <= 0; }

    public void reduceEnergy(Integer n){
        this.energy -= n;
        energyChanged(-n);
    }

    public void increaseEnergy(Integer n){
        this.energy += n;
        energyChanged(n);
    }

    public void addChild(Animal child){
        this.children.add(child);
        childrenChanged();
    }

    public Color getColor(){
        if(this.energy >= this.startEnergy) {
            return new Color(255,204,153);
        }
        else if(this.energy > this.startEnergy * 3 /4 ){
            return new Color(255,178,102);
        }
        else if(this.energy <= this.startEnergy * 3 / 4  && this.energy > this.startEnergy / 2){
            return new Color(255,153,51);
        }
        else if(this.energy <= this.startEnergy / 2 && this.energy > this.startEnergy / 4){
            return new Color(204,102,0);
        }
        else if(this.energy <= this.startEnergy / 4 && this.energy > 0){
            return new Color(153,76,0);
        }
        return new Color(51,25,0);

    }

}
