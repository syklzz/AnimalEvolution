package animals.visualization;

import animals.mapElements.Vector2d;
import animals.json.CreateJsonDocument;
import animals.map.IWorldMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class Visualization extends JPanel {

    IWorldMap map;
    IWorldMap map2;
    MapVisualization mapV;
    MapVisualization mapV2;
    StatisticsVisualization statsV;
    StatisticsVisualization statsV2;

    JButton startButton;
    JButton startButton2;
    JButton stopButton;
    JButton stopButton2;
    JButton getStatisticsButton;
    JButton getStatisticsButton2;
    JButton markAnimals;
    JButton markAnimals2;

    public Visualization(IWorldMap map, IWorldMap map2, JButton start, JButton start2, JButton stop, JButton stop2){
        this.map = map;
        this.map2 = map2;
        this.mapV = new MapVisualization(map);
        this.mapV2 = new MapVisualization(map2);
        this.statsV = new StatisticsVisualization(map);
        this.statsV2 = new StatisticsVisualization(map2);
        this.startButton =start;
        this.startButton2 = start2;
        this.stopButton = stop;
        this.stopButton2 = stop2;

        this.getStatisticsButton = new JButton("Get statistics");
        getStatisticsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                CreateJsonDocument c = new CreateJsonDocument(map.getStatistics(), "src/animals/json/output.json");
                c.create();
            }
        });

        this.getStatisticsButton2 = new JButton("Get statistics");
        this.getStatisticsButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                CreateJsonDocument c = new CreateJsonDocument(map2.getStatistics(), "src/animals/json/output2.json");
                c.create();
            }
        });

        this.markAnimals = new JButton("Show animals with dominant genotype");
        this.markAnimals.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                mapV.mark();
                mapV.repaint();
            }
        });

        this.markAnimals2 = new JButton("Show animals with dominant genotype");
        this.markAnimals2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                mapV2.mark();
                mapV2.repaint();
            }
        });
    }

    public void setFrame(){
        JFrame frame = new JFrame("Simulation");
        frame.setSize(1000, 800);
        frame.setLayout(new GridLayout(2, 2, 10, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        startButton.setBounds(0, 0, 80, 20);
        stopButton.setBounds(80, 0, 80, 20);
        getStatisticsButton.setBounds(160,0,140,20);
        markAnimals.setBounds(0,20,300,20);
        panel1.add(startButton);
        panel1.add(stopButton);
        panel1.add(getStatisticsButton);
        panel1.add(markAnimals);
        statsV.setBounds(0,60,500,400);
        panel1.add(statsV);

        JPanel panel2 = new JPanel();
        panel2.setLayout(null);
        startButton2.setBounds(0, 0,80, 20);
        stopButton2.setBounds(80, 0, 80, 20);
        getStatisticsButton2.setBounds(160,0,140,20);
        markAnimals2.setBounds(0,20,300,20);
        panel2.add(startButton2);
        panel2.add(stopButton2);
        panel2.add(getStatisticsButton2);
        panel2.add(markAnimals2);
        statsV2.setBounds(0,60,500,400);
        panel2.add(statsV2);

        frame.add(mapV);
        frame.add(mapV2);
        frame.add(panel1);
        frame.add(panel2);

        mapV.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                statsV.changeFollowedAnimal(new Vector2d(mapV.getScaledValue(e.getX()), mapV.getScaledValue(e.getY()) ));
                statsV.repaint();
            }
        });

        mapV2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                statsV2.changeFollowedAnimal(new Vector2d(mapV2.getScaledValue(e.getX()), mapV2.getScaledValue(e.getY()) ));
                statsV2.repaint();
            }
        });
    }

    public void update(){
        this.mapV.repaint();
        this.mapV2.repaint();
        this.statsV.repaint();
        this.statsV2.repaint();
    }





}
