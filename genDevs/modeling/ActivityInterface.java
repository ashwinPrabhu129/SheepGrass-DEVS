package genDevs.modeling;

import GenCol.*;

import java.util.*;
import genDevs.simulation.*;

public interface ActivityInterface extends Runnable{
public void setSimulator(CoupledSimulatorInterface sim);
public double getProcessingTime();
public String getName();
public void kill();
public void start();
public entity computeResult();
}