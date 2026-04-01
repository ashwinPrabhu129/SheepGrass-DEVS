package Homework2025.FinalProject2025.APrabhu;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;
import GenCol.*;
import genDevs.plots.*;
import genDevs.plots.newCellGridPlot;
import twoDCellSpace.TwoDimCell;
import twoDCellSpace.TwoDimCellSpace;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.*;
import java.io.*;
import java.util.*;

public class SheepGrassCellSpace extends TwoDimCellSpace {

    public newCellGridPlot plotter;
    private final GlobalRef ctx;

    private JFrame statsWindow;
    private DefaultTableModel statsModel;
    private JTable statsTable;

    public SheepGrassCellSpace() {
        this(60, 60);
    }

    public SheepGrassCellSpace(int w, int h) {
        super("SheepGrassCellSpace", w, h);
        this.ctx = GlobalRef.getInstance();
        ctx.setDim(w, h);

        setupPlot();
        populateGrid(w, h);

        applyScenario(6);

        linkNeighbors();

        launchStatsUI();
    }

    private void setupPlot() {
        plotter = new newCellGridPlot("Ecosystem Monitor", 0.1, "", 600, "", 600);
        plotter.setCellSize(10);
        plotter.setCellGridViewLocation(570, 100);
        add(plotter);
    }

    private void populateGrid(int w, int h) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                SheepGrassCell cell = new SheepGrassCell(i, j);
                add(cell);
                ctx.cell_ref[i][j] = cell;
                ctx.state[i][j] = "empty";
            }
        }
    }

    private void launchStatsUI() {
        String[] columns = {"Metric", "Value"};
        statsModel = new DefaultTableModel(columns, 0);
        statsModel.addRow(new Object[]{"Rock Percentage", "0.0%"});
        statsModel.addRow(new Object[]{"Max Sheep Count", "0"});

        statsTable = new JTable(statsModel);
        JScrollPane pane = new JScrollPane(statsTable);

        statsWindow = new JFrame("Live Stats");
        statsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statsWindow.setLayout(new BorderLayout());
        statsWindow.add(pane, BorderLayout.CENTER);
        statsWindow.setSize(300, 150);
        statsWindow.setLocation(600, 720);
        statsWindow.setVisible(true);

        new javax.swing.Timer(500, e -> updateStats()).start();
    }

    private void updateStats() {
        String rockPct = String.format("%.2f%%", ctx.getObstaclePercentage());
        String maxSheep = String.valueOf(ctx.getMaxSheep());

        statsModel.setValueAt(rockPct, 0, 1);
        statsModel.setValueAt(maxSheep, 1, 1);
    }

    private void applyScenario(int id) {
        int cx = xDimCellspace / 2;
        int cy = yDimCellspace / 2;

        if (id == 6) {
            generateRandomMap(cx, cy);
        } else {
            modifyCell(cx, cy, SheepGrassCell.EcoState.FLORA);
        }
    }

    private void generateRandomMap(int cx, int cy) {
        int range = 5;

        for (int i = 0; i < xDimCellspace; i++) {
            for (int j = 0; j < yDimCellspace; j++) {
                if (ctx.rng.nextDouble() < ctx.obstacleDensity) {
                    modifyCell(i, j, SheepGrassCell.EcoState.OBSTACLE);
                }
            }
        }

        for (int i = 0; i < 20; i++) {
            int rx = cx - range + ctx.rng.nextInt(range * 2 + 1);
            int ry = cy - range + ctx.rng.nextInt(range * 2 + 1);

            if (isValid(rx, ry) && !isRock(rx, ry)) {
                modifyCell(rx, ry, SheepGrassCell.EcoState.FAUNA);
            }
        }

        for (int i = 0; i < 40; i++) {
            int rx = cx - range + ctx.rng.nextInt(range * 2 + 1);
            int ry = cy - range + ctx.rng.nextInt(range * 2 + 1);

            if (isValid(rx, ry) && isVoid(rx, ry)) {
                modifyCell(rx, ry, SheepGrassCell.EcoState.FLORA);
            }
        }
    }

    private void modifyCell(int x, int y, SheepGrassCell.EcoState s) {
        ((SheepGrassCell) withId(x, y)).forceState(s);
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < xDimCellspace && y >= 0 && y < yDimCellspace;
    }

    private boolean isRock(int x, int y) {
        return ctx.state[x][y].equals("rock");
    }

    private boolean isVoid(int x, int y) {
        return ctx.state[x][y].equals("empty");
    }

    private void linkNeighbors() {
        doNeighborToNeighborCoupling();
        DoBoundaryToBoundaryCoupling();
    }

    public static void main(String args[]){
        TunableCoordinator runner = new TunableCoordinator(new SheepGrassCellSpace());
        runner.setTimeScale(0.003); //TIME
        runner.initialize();
        runner.simulate(10000);
    }

    private void DoBoundaryToBoundaryCoupling()
    {
        for( int x = 1; x < xDimCellspace-1; x++ )
        {
            addCoupling(withId(x, 0), "outS", withId(x, yDimCellspace-1), "inN");
            addCoupling(withId(x, 0), "outSW", withId(x-1, yDimCellspace-1), "inNE");
            addCoupling(withId(x, 0), "outSE", withId(x+1, yDimCellspace-1), "inNW");

            addCoupling(withId(x, yDimCellspace-1), "outN", withId(x, 0), "inS");
            addCoupling(withId(x, yDimCellspace-1), "outNE", withId(x+1, 0), "inSW");
            addCoupling(withId(x, yDimCellspace-1), "outNW", withId(x-1, 0), "inSE");
        }
        for( int y = 1; y < yDimCellspace-1; y++ )
        {
            addCoupling(withId(0, y), "outW", withId(xDimCellspace-1, y), "inE");
            addCoupling(withId(0, y), "outSW", withId(xDimCellspace-1, y-1), "inNE");
            addCoupling(withId(0, y), "outNW", withId(xDimCellspace-1, y+1), "inSE");

            addCoupling(withId(xDimCellspace-1, y), "outE", withId(0, y), "inW");
            addCoupling(withId(xDimCellspace-1, y), "outNE", withId(0, y+1), "inSW");
            addCoupling(withId(xDimCellspace-1, y), "outSE", withId(0, y-1), "inNW");
        }
        addCoupling(withId(0, 0), "outNW", withId(xDimCellspace-1, 1), "inSE");
        addCoupling(withId(0, 0), "outW", withId(xDimCellspace-1, 0), "inE");
        addCoupling(withId(0, 0), "outSW", withId(xDimCellspace-1, yDimCellspace-1), "inNE");
        addCoupling(withId(0, 0), "outS", withId(0, yDimCellspace-1), "inN");
        addCoupling(withId(0, 0), "outSE", withId(1, yDimCellspace-1), "inNW");
        addCoupling(withId(xDimCellspace-1, 0), "outSW", withId(xDimCellspace-2, yDimCellspace-1), "inNE");
        addCoupling(withId(xDimCellspace-1, 0), "outE", withId(0, 0), "inW");
        addCoupling(withId(xDimCellspace-1, 0), "outSE", withId(0, yDimCellspace-1), "inNW");
        addCoupling(withId(xDimCellspace-1, 0), "outS", withId(xDimCellspace-1, yDimCellspace-1), "inN");
        addCoupling(withId(xDimCellspace-1, 0), "outNE", withId(0, 1), "inSW");
        addCoupling(withId(0, yDimCellspace-1), "outSW", withId(xDimCellspace-1, yDimCellspace-2), "inNE");
        addCoupling(withId(0, yDimCellspace-1), "outW", withId(xDimCellspace-1, yDimCellspace-1), "inE");
        addCoupling(withId(0, yDimCellspace-1), "outNE", withId(1, 0), "inSW");
        addCoupling(withId(0, yDimCellspace-1), "outN", withId(0, 0), "inS");
        addCoupling(withId(0, yDimCellspace-1), "outNW", withId(xDimCellspace-1, 0), "inSE");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outNW", withId(xDimCellspace-2, 0), "inSE");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outE", withId(0, yDimCellspace-1), "inW");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outSE", withId(0, yDimCellspace-2), "inNW");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outN", withId(xDimCellspace-1, 0), "inS");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outNE", withId(0, 0), "inSW");
    }

    public int[] getNeighborXYCoord(TwoDimCell myCell, int direction )
    {
        int[][] shifts = {
                {0, 1}, {1, 1}, {1, 0}, {1, -1},
                {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}
        };

        if (direction < 0 || direction > 7) return new int[]{myCell.getXcoord(), myCell.getYcoord()};

        int nx = myCell.getXcoord() + shifts[direction][0];
        int ny = myCell.getYcoord() + shifts[direction][1];

        if (nx >= xDimCellspace) nx = 0; else if (nx < 0) nx = xDimCellspace - 1;
        if (ny >= yDimCellspace) ny = 0; else if (ny < 0) ny = yDimCellspace - 1;

        return new int[]{nx, ny};
    }

    public String getPortNameFromCoords(SheepGrassCell from, int tx, int ty) {
        int dx = tx - from.getXcoord();
        int dy = ty - from.getYcoord();

        if (dx > 1) dx = -1; else if (dx < -1) dx = 1;
        if (dy > 1) dy = -1; else if (dy < -1) dy = 1;

        if (dx == 0 && dy == 1) return "outN";
        if (dx == 1 && dy == 1) return "outNE";
        if (dx == 1 && dy == 0) return "outE";
        if (dx == 1 && dy == -1) return "outSE";
        if (dx == 0 && dy == -1) return "outS";
        if (dx == -1 && dy == -1) return "outSW";
        if (dx == -1 && dy == 0) return "outW";
        if (dx == -1 && dy == 1) return "outNW";
        return "";
    }
}