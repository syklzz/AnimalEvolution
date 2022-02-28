package animals.visualization;

import animals.json.CreateJsonDocument;
import animals.map.IWorldMap;
import animals.mapElements.Vector2d;
import animals.simulation.SimulationEngine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Visualization extends JPanel {
    private final MapVisualization mapV;
    private final StatisticsVisualization statsV;
    private final JButton startButton;
    private final JButton stopButton;
    private final JButton getStatisticsButton;
    private final JButton markAnimals;
    private final SimulationEngine engine;

    public Visualization(IWorldMap map, SimulationEngine engine){
        this.mapV = new MapVisualization(map);
        this.statsV = new StatisticsVisualization(map);
        this.engine = engine;
        this.startButton = new JButton("Start");
        startButton.addActionListener(ae -> this.engine.runTimer());
        this.stopButton = new JButton("Stop");
        stopButton.addActionListener(ae -> this.engine.stopTimer());
        this.getStatisticsButton = new JButton("Get statistics");
        getStatisticsButton.addActionListener(ae -> {
            CreateJsonDocument c = new CreateJsonDocument(map.getStatistics(), "src/animals/json/output.json");
            c.create();
        });
        this.markAnimals = new JButton("Show animals with dominant genotype");
        this.markAnimals.addActionListener(ae -> {
            mapV.mark();
            mapV.repaint();
        });
    }

    public void setFrame(){
        JFrame frame = new JFrame("Simulation");
        frame.setSize(720, 500);
        frame.setLayout(new GridLayout(1, 2, 10, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        startButton.setBounds(0, 0, 80, 20);
        stopButton.setBounds(80, 0, 80, 20);
        getStatisticsButton.setBounds(160,0,180,20);
        markAnimals.setBounds(0,20,340,20);
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(getStatisticsButton);
        panel.add(markAnimals);
        statsV.setBounds(0,60,500,400);
        panel.add(statsV);

        frame.add(mapV);
        frame.add(panel);

        mapV.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                statsV.changeFollowedAnimal(new Vector2d(mapV.getScaledValue(e.getX()), mapV.getScaledValue(e.getY()) ));
                statsV.repaint();
            }
        });
    }

    public void update(){
        this.mapV.repaint();
        this.statsV.repaint();
    }
}
