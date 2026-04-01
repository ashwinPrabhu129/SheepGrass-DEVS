package Homework2025.FinalProject2025.APrabhu;

import genDevs.modeling.*;
import java.util.Random;
import genDevs.modeling.IODevs;

public class GlobalRef {
    protected static int xDim;
    protected static int yDim;
    protected static GlobalRef _instance=null;

    public String[][] state;
    public IODevs[][] cell_ref;
    public Random rng;

    public double grassReproduceT = 1.3;
    public double sheepMoveT = 1.5;
    public double sheepLifeT  = 3.0;
    public double sheepReproduceT = 4.0;

    public double obstacleDensity = 0.7; //ROCK PERCENTAGE

    private int currentSheepPopulation = 0;
    private int maxSheepPopulation = 0;
    private int obstacleCount = 0;
    private int totalGridCells = 0;

    private GlobalRef(){
        rng = new Random(12345);
    }

    public static GlobalRef getInstance(){
        if(_instance!=null) return _instance;
        else {
            _instance = new GlobalRef();
            return _instance;
        }
    }

    public void setDim(int x, int y){
        xDim = x;
        yDim = y;
        totalGridCells = x * y;
        state = new String[xDim][yDim];
        cell_ref = new IODevs[xDim][yDim];

        currentSheepPopulation = 0;
        maxSheepPopulation = 0;
        obstacleCount = 0;
    }

    public synchronized void registerObstacle() {
        obstacleCount++;
    }

    public synchronized void registerBirth() {
        currentSheepPopulation++;
        if (currentSheepPopulation > maxSheepPopulation) {
            maxSheepPopulation = currentSheepPopulation;
        }
    }

    public synchronized void registerDeath() {
        currentSheepPopulation--;
    }

    public double getObstaclePercentage() {
        if (totalGridCells == 0) return 0.0;
        return (double) obstacleCount / totalGridCells * 100.0;
    }

    public int getMaxSheep() {
        return maxSheepPopulation;
    }
}