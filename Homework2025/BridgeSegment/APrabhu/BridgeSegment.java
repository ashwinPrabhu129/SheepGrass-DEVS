package Homework2025.BridgeSegment.APrabhu;

import java.util.LinkedList;
import BridgeSegment.AbstractBridgeSystem;
import GenCol.entity;
import genDevs.modeling.content;
import genDevs.modeling.message;
import simView.ViewableAtomic;

public class BridgeSegment extends ViewableAtomic {

    protected LinkedList<entity> west;
    protected LinkedList<entity> east;
    protected double wait_time;
    protected double light_time;
    protected entity on_bridge;
    protected String direction;


    public BridgeSegment(String name, double processing_time) {
        super(name);
        wait_time = processing_time;

        addInport("westbound_in");
        addInport("eastbound_in");
        addOutport("westbound_out");
        addOutport("eastbound_out");
    }

    @Override
    public void initialize() {

        west = new LinkedList<>();
        east = new LinkedList<>();
        on_bridge = null;

        if (this.getName().contains("1")) {
            light_time = AbstractBridgeSystem.BridgeSystemSetting.Bridge1TrafficLightDurationTime;
            if (AbstractBridgeSystem.BridgeSystemSetting.Bridge1InitialState == AbstractBridgeSystem.BridgeState.WEST_TO_EAST) {
                holdIn("westToEastGreen", light_time);
            } else {
                holdIn("eastToWestGreen", light_time);
            }
        } else if (this.getName().contains("2")) {
            light_time = AbstractBridgeSystem.BridgeSystemSetting.Bridge2TrafficLightDurationTime;
            if (AbstractBridgeSystem.BridgeSystemSetting.Bridge2InitialState == AbstractBridgeSystem.BridgeState.WEST_TO_EAST) {
                holdIn("westToEastGreen", light_time);
            } else {
                holdIn("eastToWestGreen", light_time);
            }
        } else {
            light_time = AbstractBridgeSystem.BridgeSystemSetting.Bridge3TrafficLightDurationTime;
            if (AbstractBridgeSystem.BridgeSystemSetting.Bridge3InitialState == AbstractBridgeSystem.BridgeState.WEST_TO_EAST) {
                holdIn("westToEastGreen", light_time);
            } else {
                holdIn("eastToWestGreen", light_time);
            }
        }
        super.initialize();
    }

    @Override
    public void deltext(double e, message x) {
        Continue(e);

        for (int i = 0; i < x.getLength(); i++) {
            if (messageOnPort(x, "westbound_in", i)) {
                entity car = x.getValOnPort("westbound_in", i);
                if (phaseIs("westToEastGreen")) {
                    on_bridge = car;
                    direction = "east";
                    holdIn("east_busy", wait_time);
                } else {
                    west.add(car);
                }
            }
            else if (messageOnPort(x, "eastbound_in", i)) {
                entity car = x.getValOnPort("eastbound_in", i);
                if (phaseIs("eastToWestGreen")) {
                    on_bridge = car;
                    direction = "west";
                    holdIn("west_busy", wait_time);
                } else {
                    east.add(car);
                }
            }
        }
    }

    @Override
    public void deltint() {
        if (phaseIs("east_busy")) {
            on_bridge = null;
            if (!west.isEmpty()) {
                on_bridge = west.removeFirst();
                direction = "east";
                holdIn("east_busy", wait_time);
            } else {
                holdIn("westToEastGreen", light_time - sigma);
            }
        }
        else if (phaseIs("west_busy")) {
            on_bridge = null;
            if (!east.isEmpty()) {
                on_bridge = east.removeFirst();
                direction = "west";
                holdIn("west_busy", wait_time);
            } else {
                holdIn("eastToWestGreen", light_time - sigma);
            }
        }
        else if (phaseIs("westToEastGreen")) {
            if (!east.isEmpty()) {
                on_bridge = east.removeFirst();
                direction = "west";
                holdIn("west_busy", wait_time);
            } else {
                holdIn("eastToWestGreen", light_time);
            }
        }
        else if (phaseIs("eastToWestGreen")) {
            if (!west.isEmpty()) {
                on_bridge = west.removeFirst();
                direction = "east";
                holdIn("east_busy", wait_time);
            } else {
                holdIn("westToEastGreen", light_time);
            }
        }
    }

    @Override
    public message out() {
        message m = new message();
        if (phaseIs("east_busy")) {
            content con = makeContent("eastbound_out", on_bridge);
            m.add(con);
        } else if (phaseIs("west_busy")) {
            content con = makeContent("westbound_out", on_bridge);
            m.add(con);
        }
        return m;
    }
}