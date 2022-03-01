package animals.visualization;

import animals.mapElements.Plant;
import animals.mapElements.Vector2d;
import animals.mapElements.Animal;
import animals.map.IWorldMap;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

public class MapVisualization extends JPanel {
    private final IWorldMap map;
    private int width;
    private int height;
    private final Vector2d LowerJungleCoord;
    private final Vector2d UpperJungleCoord;
    private final int jungleWidth;
    private final int jungleHeight;
    private int scale;
    private boolean markDominantAnimals = false;

    public void mark(){
        markDominantAnimals = !markDominantAnimals;
    }

    public MapVisualization(IWorldMap map){
        this.setSize((width+1) * scale, (height+1) * scale + 60);
        this.setLocation(0,0);
        this.map = map;
        this.width = this.map.getWidth();
        this.height = this.map.getHeight();
        this.scale = Math.min(360/(this.width + 1), 360/(this.height + 1));
        this.LowerJungleCoord = this.map.getLowerJungleCoord();
        this.UpperJungleCoord = this.map.getUpperJungleCoord();
        this.jungleWidth = UpperJungleCoord.x - LowerJungleCoord.x;
        this.jungleHeight = UpperJungleCoord.y - LowerJungleCoord.y;
    }

    public int getScaledValue(int n){
        return n/scale;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(173, 217, 61));
        g.fillRect(0, 0, (width+1) * scale, (height+1) * scale);
        g.setColor(new Color(0, 204, 0));
        g.fillRect(LowerJungleCoord.x * scale, LowerJungleCoord.y * scale, (jungleWidth+1) * scale, (jungleHeight+1) * scale);
        g.setColor(new Color(238, 227, 200));
        g.fillRect(0, (height+1) * scale, (width+1) * scale, 720 - ((height+1) * scale));

        for (Map.Entry<Vector2d, Plant> item : this.map.getPlants().entrySet()) {
            Vector2d key = item.getKey();
            g.setColor(new Color(0,153,76));
            int x = key.x;
            int y = key.y;
            g.fillRect(x*scale,y*scale,scale,scale);
        }

        for (Map.Entry<Vector2d, Set<Animal>> item : this.map.getAnimals().entrySet()){
            Vector2d key = this.map.adjustPosition(item.getKey());
            Set<Animal> animals = item.getValue();
            for(Animal animal : animals){
                g.setColor(animal.getColor());
                int x = key.x * scale;
                int y = key.y * scale;
                g.fillOval(x, y, scale, scale);
                if(animal.getGenotype().equals(map.getStatistics().getDominantGenotype()) && markDominantAnimals){
                    g.setColor(Color.BLUE);
                    g.drawOval(x,y,scale,scale);
                }
                break;
            }
        }

        g.setColor(new Color(173, 217, 61));
        g.fillRect(0,scale*height+20,10,10);
        g.setColor(Color.black);
        g.drawString("Steppe",11,scale*height+30);

        g.setColor(new Color(0, 204, 0));
        g.fillRect(0,scale*height+35,10,10);
        g.setColor(Color.black);
        g.drawString("Jungle",11,scale*height+45);

        g.setColor(new Color(213, 103, 60));
        g.fillOval(0,scale*+height+50,10,10);
        g.setColor(Color.black);
        g.drawString("Animal",11,scale*height+60);

        g.setColor(new Color(0,153,76));
        g.fillRect(0,scale*height+65,10,10);
        g.setColor(Color.black);
        g.drawString("Plant",11,scale*height+75);

        g.setColor(Color.blue);
        g.drawOval(0,scale*height+80,10,10);
        g.setColor(Color.black);
        g.drawString("Animals with dominant genotype",11,scale*height+90);
    }
}