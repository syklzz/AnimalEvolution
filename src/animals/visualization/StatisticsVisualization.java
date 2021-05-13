package animals.visualization;

import animals.mapElements.Vector2d;
import animals.mapElements.Animal;
import animals.map.IWorldMap;
import animals.map.Statistics;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class StatisticsVisualization extends JPanel {

    private final IWorldMap map;
    private final Statistics stats;

    public StatisticsVisualization(IWorldMap map){
        this.map = map;
        this.stats = map.getStatistics();
    }

    public void changeFollowedAnimal(Vector2d position){
        Set<Animal> animals = map.getAnimalsAt(position);
        if(animals == null){
            return;
        }
        int maxEnergy = 0;
        for(Animal animal : animals){
            if(animal.getEnergy() >= maxEnergy ){
                maxEnergy = animal.getEnergy();
                stats.setFollowedAnimal(animal);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font font = new Font("Serif", Font.PLAIN, 15);
        g.setFont(font);
        g.drawString("CURRENT STATISTICS" , 0,20);
        g.drawString("Day: " + stats.getDay(), 0,40);
        g.drawString("Number of animals: " + stats.getAliveAnimalsCounter(), 0,60);
        g.drawString("Number of plants: " + stats.getPlantsCounter(), 0,80);
        g.drawString("Average energy: " + stats.getAverageEnergy(), 0,100);
        g.drawString("Life expectancy: " + stats.getAverageLifeSpan(), 0,120);
        g.drawString("Average number of children: " + stats.getAverageChildrenAmount(), 0,140);
        g.drawString("Dominant genotype: " + stats.getCurrentMaxGenotype(), 0,160);

        if(stats.getFollowedAnimal() != null){
            g.drawString("FOLLOWED ANIMAL:" , 0,200);
            g.drawString("Genotype: " + stats.getFollowedAnimal().getGenotype(), 0,220);
            g.drawString("New children: " + stats.getFollowedAnimalChildren(), 0,240);
            g.drawString("New descendants: " + stats.getFollowedAnimalDescendants(), 0,260);
            g.drawString("Day of death: " + stats.getFollowedAnimal().getDeathDay(), 0,280);
            g.drawString("Days under observation: " + stats.getDaysUnderObservation(), 0,300);
        }

    }

}
