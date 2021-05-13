package animals.mapElements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Genotype {
    private ArrayList<Integer> genes = new ArrayList<>();
    private int[] genesCounter = new int[8];

    public Genotype(){
        for(int i = 0; i < 8; i ++){
            this.genes.add(i);
            this.genesCounter[i] ++;
        }
        Random r = new Random();
        for(int i = 8; i < 32; i++){
            int g = r.nextInt(8);
            this.genes.add(g);
            this.genesCounter[g] ++;
        }
        Collections.sort(genes);
    }

    public Genotype(Genotype A, Genotype B){
        Random r = new Random();
        int split1 = r.nextInt(30);
        int split2 = r.nextInt(30 - split1) + split1 + 1;

        int[] genesAmount = new int[8];

        for(int i = 0; i <= split1; i++){
            genesAmount[A.genes.get(i)] += 1;
        }
        for(int i = split1 + 1; i <= split2; i++){
            genesAmount[B.genes.get(i)] += 1;
        }
        for(int i = split2; i < 32; i++){
            genesAmount[A.genes.get(i)] += 1;
        }

        this.genes = mutate(genesAmount);
    }


    public ArrayList mutate(int[] genesAmount){
        ArrayList<Integer> fewGenes = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            if(genesAmount[i] > 0){
                fewGenes.add(i);
            }
        }

        for(int i = 0; i < 8; i++){
            if (genesAmount[i] == 0){
                Random r = new Random();
                int index = r.nextInt(fewGenes.size());
                int j = fewGenes.get(index);
                genesAmount[j] -= 1;
                genesAmount[i] += 1;
                if (genesAmount[j] <= 1) { fewGenes.remove(index); }
            }
        }

        ArrayList<Integer> newGenes = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < genesAmount[i]; j++){
                newGenes.add(i);
            }
        }
        this.genesCounter = genesAmount;
        return newGenes;
    }

    public MapDirection newOrientation(MapDirection orientation){
        Random r = new Random();
        int index = r.nextInt(30);
        int turns = this.genes.get(index);
        for(int i = 0; i < turns; i++){
            orientation = orientation.next();
        }
        return orientation;
    }

    public boolean equals(Object other){
        if( this == other ) return true;
        if (!(other instanceof Genotype)) return false;

        Genotype that = (Genotype) other;
        boolean equal = true;
        for(int i = 0; i < 8; i++){
            if(this.genesCounter[i] != that.genesCounter[i]){
                equal = false;
                break;
            }
        }
        return equal;
    }

    public String toString(){
        StringBuffer res = new StringBuffer();
        for(int i=0; i<genes.size(); i++){
            res.append(genes.get(i));
        }
        return res.toString();
    }


    @Override
    public int hashCode() {
        return Arrays.hashCode(genesCounter);
    }

}

