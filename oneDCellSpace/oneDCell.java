package  oneDCellSpace;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import genDevs.plots.*;
import util.*;


public interface oneDCell extends IODevs{

public void setDrawPos(int i);

public int getId();

public void addNeighbor(int i,oneDCell n);

public void addNeighborCoupling(int i,String outpt,String inpt);

public static int numCells = 10;

}
