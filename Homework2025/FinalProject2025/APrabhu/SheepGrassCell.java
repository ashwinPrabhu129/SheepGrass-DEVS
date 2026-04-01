package Homework2025.FinalProject2025.APrabhu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import GenCol.entity;
import genDevs.modeling.message;
import genDevs.plots.newCellGridView;
import twoDCellSpace.TwoDimCell;

public class SheepGrassCell extends TwoDimCell {

    public enum EcoState { VOID, FLORA, FAUNA, OBSTACLE }

    private EcoState currentState;
    private newCellGridView renderer;
    private final GlobalRef ctx;

    private double tMove;
    private double tDie;
    private double tBreed;

    private entity packet = null;
    private String outPort = "";

    public SheepGrassCell(int x, int y) {
        super(new GenCol.Pair(x, y));
        this.ctx = GlobalRef.getInstance();
        this.currentState = EcoState.VOID;
    }

    @Override
    public void initialize() {
        super.initialize();
        this.renderer = ((SheepGrassCellSpace) getParent()).plotter.getCellGridView();
        applyState(currentState, 0, 0);
    }

    public void forceState(EcoState s) {
        this.currentState = s;
        ctx.state[xcoord][ycoord] = stateToString(s);

        if (s == EcoState.OBSTACLE) ctx.registerObstacle();
        if (s == EcoState.FAUNA) ctx.registerBirth();
    }

    private void applyState(EcoState s, double dieTime, double breedTime) {
        currentState = s;
        ctx.state[xcoord][ycoord] = stateToString(s);

        switch (s) {
            case VOID:
                draw(Color.WHITE);
                passivate();
                break;
            case FLORA:
                draw(Color.GREEN);
                holdIn("growing", ctx.grassReproduceT);
                break;
            case FAUNA:
                draw(Color.RED);
                tMove = ctx.sheepMoveT;
                tDie = (dieTime > 0) ? dieTime : ctx.sheepLifeT;
                tBreed = (breedTime > 0) ? breedTime : ctx.sheepReproduceT;
                planNextAction();
                break;
            case OBSTACLE:
                draw(Color.GRAY);
                passivate();
                break;
        }
    }

    private String stateToString(EcoState s) {
        if (s == EcoState.FLORA) return "grass";
        if (s == EcoState.FAUNA) return "sheep";
        if (s == EcoState.OBSTACLE) return "rock";
        return "empty";
    }

    @Override
    public void deltext(double e, message x) {
        Continue(e);
        if (currentState == EcoState.OBSTACLE) return;

        if (currentState == EcoState.FAUNA) decreaseTimers(e);

        for (int i = 0; i < x.getLength(); i++) {
            if (isNeighborMsg(x, i)) {
                entity val = getMsgValue(x, i);
                if (val != null) handleIncoming(val);
            }
        }

        if (currentState == EcoState.FAUNA) planNextAction();
    }

    private void handleIncoming(entity val) {
        if (val.getName().equals("grow_grass") && currentState == EcoState.VOID) {
            applyState(EcoState.FLORA, 0, 0);
        }
        else if (val instanceof SheepEntity) {
            SheepEntity incoming = (SheepEntity) val;
            if (currentState == EcoState.FLORA) {
                applyState(EcoState.FAUNA, incoming.toDieTime + ctx.sheepLifeT, incoming.toReproduceTime);
                ctx.registerBirth();
            } else if (currentState == EcoState.VOID) {
                applyState(EcoState.FAUNA, incoming.toDieTime, incoming.toReproduceTime);
                ctx.registerBirth();
            }
        }
    }

    @Override
    public void deltint() {
        if (currentState == EcoState.OBSTACLE) { passivate(); return; }

        if (phaseIs("sending")) {
            finalizeTransmission();
            return;
        }

        if (currentState == EcoState.FLORA) {
            logicFlora();
        } else if (currentState == EcoState.FAUNA) {
            logicFauna();
        }
    }

    private void finalizeTransmission() {
        if (packet instanceof SheepEntity) {
            if (packet.getName().equals("migrating")) {
                ctx.registerDeath();
                applyState(EcoState.VOID, 0, 0);
            } else if (packet.getName().equals("newborn")) {
                tBreed = ctx.sheepReproduceT;
                planNextAction();
            }
        } else if (packet != null && packet.getName().equals("grow_grass")) {
            holdIn("growing", ctx.grassReproduceT);
        }
    }

    private void logicFlora() {
        int[] target = scanFor(EcoState.VOID);
        if (target != null) {
            send(new entity("grow_grass"), target);
        } else {
            holdIn("growing", ctx.grassReproduceT);
        }
    }

    private void logicFauna() {
        decreaseTimers(sigma);
        double eps = 1E-6;

        if (tDie <= eps) {
            ctx.registerDeath();
            applyState(EcoState.VOID, 0, 0);
        } else if (tBreed <= eps) {
            int[] target = scanFor(EcoState.VOID);
            if (target != null) {
                send(new SheepEntity("newborn", ctx.sheepLifeT, ctx.sheepReproduceT), target);
            } else {
                tBreed = ctx.sheepReproduceT;
                planNextAction();
            }
        } else if (tMove <= eps) {
            int[] target = findMigrationSpot();
            if (target != null) {
                send(new SheepEntity("migrating", tDie, tBreed), target);
            } else {
                tMove = ctx.sheepMoveT;
                planNextAction();
            }
        }
    }

    private void decreaseTimers(double t) {
        tMove -= t;
        tDie -= t;
        tBreed -= t;
    }

    private void planNextAction() {
        if (currentState != EcoState.FAUNA) return;
        double next = Math.min(tMove, Math.min(tDie, tBreed));
        holdIn("active", next);
    }

    private void send(entity content, int[] coords) {
        this.packet = content;
        this.outPort = ((SheepGrassCellSpace)getParent()).getPortNameFromCoords(this, coords[0], coords[1]);
        holdIn("sending", 0);
    }

    @Override
    public message out() {
        message m = new message();
        if (phaseIs("sending") && packet != null) {
            m.add(makeContent(outPort, packet));
        }
        return m;
    }

    private void draw(Color c) {
        renderer.drawCell(xcoord, GlobalRef.yDim - 1 - ycoord, c);
    }


    private int[] scanFor(EcoState type) {
        List<int[]> matches = new ArrayList<>();
        String searchStr = stateToString(type);

        for (int i = 0; i < 8; i++) {
            int[] pos = ((SheepGrassCellSpace) getParent()).getNeighborXYCoord(this, i);
            if (ctx.state[pos[0]][pos[1]].equals(searchStr)) {
                matches.add(pos);
            }
        }
        if (matches.isEmpty()) return null;
        return matches.get(ctx.rng.nextInt(matches.size()));
    }

    private int[] findMigrationSpot() {
        List<int[]> food = new ArrayList<>();
        List<int[]> space = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int[] pos = ((SheepGrassCellSpace) getParent()).getNeighborXYCoord(this, i);
            String st = ctx.state[pos[0]][pos[1]];
            if (st.equals("grass")) food.add(pos);
            else if (st.equals("empty")) space.add(pos);
        }

        if (!food.isEmpty()) return food.get(ctx.rng.nextInt(food.size()));
        if (!space.isEmpty()) return space.get(ctx.rng.nextInt(space.size()));
        return null;
    }

    private boolean isNeighborMsg(message x, int i) {
        String[] dirs = {"inN", "inNE", "inE", "inSE", "inS", "inSW", "inW", "inNW"};
        for (String p : dirs) if (messageOnPort(x, p, i)) return true;
        return false;
    }

    private entity getMsgValue(message x, int i) {
        String[] dirs = {"inN", "inNE", "inE", "inSE", "inS", "inSW", "inW", "inNW"};
        for (String p : dirs) if (messageOnPort(x, p, i)) return x.getValOnPort(p, i);
        return null;
    }
}