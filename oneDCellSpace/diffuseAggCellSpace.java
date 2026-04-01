
package oneDCellSpace;

import java.lang.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import genDevs.modeling.*;
import genDevs.plots.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.TunableCoordinator;
import GenCol.*;
import util.*;
import simView.*;

////////////////////////////////////
class diffuseAggCell extends oneDimCell{
  public final int cellType;
  public static int maxRows, numCells;
  protected double activity = 0.0, numTrueTransitions=0.0;
  protected double val = 0, lastval=-1, right=0, left=0;
  protected static double   kSmall = 100,deltaxSmall = 1,finalDensity = .5,
                                 deltatSmall = 0.5*deltaxSmall*deltaxSmall/kSmall ;//courant satsified
  protected static double  numPerBlock = 13.5;
  protected static int numSmallCells = 100,
       numBlocks = (int)Math.rint(numSmallCells/numPerBlock);
  protected static double deltax = deltaxSmall*numPerBlock,deltat = deltatSmall*numPerBlock*numPerBlock;
  //k = numPerBlock*numPerBlock*kSmall;
  protected static double quantum = .0001, kappa = kSmall*deltatSmall/deltaxSmall*deltaxSmall;
                       //make quantum negative to remove its effect

  protected int row = 1;
  boolean confluent = false;
  double clock; //bpz  to get actual transitions in binDevs
  int numOutputs; //bpz

  public diffuseAggCell(){
    this(0,0);
  }

  public diffuseAggCell(int i, int cellType){
    super(i);
    this.cellType=cellType;

    addInport("inLeft");
    addInport("inRight");
    addOutport("out");
    addOutport("outMove");
    addOutport("outPos");
    addOutport("outPair");
  }

  public void initialize(){
    super.initialize(); // all cells are initially passive
    numTransitions = 0;
    numTrueTransitions = 0;
    numOutputs = 0;
    clock = 0;
    row = 1;
    val = 0; lastval=0;
    left = 0;
    right = 0;


   if (id == numBlocks/2){  // center cell
     val = numSmallCells*finalDensity;//same amount to distribute
   //   double inv = 1/(double)numBlocks;
    // if (true){
   //    val = 4*inv*finalDensity*(numBlocks-Math.abs(id -numBlocks/2));//same amount to distribute
    holdIn("active",0);
    }
     else passivate();

  }


public message out(){

message m = super.out();

m.add(makeContent("outDraw",  new DrawCellEntity("drawCellToScale",
       drawPos, val)));

m.add(makeContent("outPair", new Pair(new Integer(id),
                             new Double(val))));
m.add(makeContent("outDrawTime", new doubleEnt(val)));
return outputRealOnPort(m,val,"outPos");

}

  public String getTooltipText(){
    return
    super.getTooltipText()
        +"\n"+"Kappa: "+kappa
        +"\n"+"numTransitions :"+ numTransitions
        +"\n"+"numTrueTransitions :"+ numTrueTransitions
        +"\n"+"activity :"+ activity
        +"\n"+"clock :"+ clock
        +"\n"+"numOutputs :"+ numOutputs
        +"\n"+"row :"+ row
        +"\n"+"val :"+ val;
  }




  public void deltcon(double e,message x){
    confluent=true;
    deltext(e,x);
 //   deltint();
    confluent=false;
    numTransitions++; // total transitions
  }



public double avgNeighPos(){
if (id == 0) left = val;
else if (id == numCells-1) right = val;
return (left + right)/2;
}

public boolean quantumChange(double x,double y){
return (Math.abs(x-y)> quantum);
}

  public void deltext(double e,message x){
    clock += e;
    Continue(e);
   double lleft  = left;
   double lright = right;

    numTransitions++; // keep track of every transition (ext and int)

    if (somethingOnPort(x,"inLeft") || somethingOnPort(x,"inRight")){
      if (somethingOnPort(x,"inLeft"))
        lleft = getRealValueOnPort(x,"inLeft");

       if (somethingOnPort(x,"inRight"))
        lright = getRealValueOnPort(x,"inRight");

      if (quantumChange(left,lleft) || quantumChange(right,lright)){
        // at least one neighbour has changed

        // update my neighbours' states
        right = lright;
        left = lleft;
        // update my state
        val =  (1 - kappa)*val + kappa*avgNeighPos();

       if (quantumChange(val,lastval)){
          // true transition
          lastval = val;
          numTrueTransitions++;
          numOutputs++;
          holdIn("active",deltat);
        } else
       // not a true transition
       // System.out.println(id+" : passivate(), val " + val + "!= lastval " + lastval);
          passivate(); // -> nothing to tell my neighbours -> passivate

    activity = numTrueTransitions / (double)numTransitions;
  }
  }
}

  public void deltint(){

    val =  (1 - kappa)*val + kappa*avgNeighPos();

    if (quantumChange(val,lastval)){
      lastval = val;
      numTrueTransitions++;
      holdIn("active",deltat);
    }
    else passivate();

    row++;            // keep track of plot location (row)

    // compute new inherent activity
  activity = numTrueTransitions / (double)numTransitions;

    if (confluent==false){
      numTransitions++; // total transitions
      numOutputs++;
      clock += sigma;
      }
  }

}
/////////////////////////////////////////////////

public class diffuseAggCellSpace extends oneDimCellSpace{
  protected final int cellType;

  public diffuseAggCellSpace(){
    // number of cells
    this (diffuseAggCell.numBlocks);
  }

  public diffuseAggCellSpace(int numCells){
    /////////////////////////////
    // change configuration here
    // (0) rule30BinCell
    // (1) rule30BinDevsCell
    /////////////////////////////
    this(numCells, 0);
  }

  public diffuseAggCellSpace(int numCells, int cellType){

    super("diffuseAgg "+"kappa "+diffuseAggCell.kappa+" "+numCells);
    this.cellType=cellType;

    addInport("inLeft");
    addInport("inRight");
    addOutport("out");
    addOutport("outDraw");

    diffuseAggCell.numCells = numCells;
    diffuseAggCell.maxRows = numCells;

    this.numCells = numCells;


    for (int i = 0;i<numCells;i++){
      if (cellType == 0)
        addCell(i, new diffuseAggCell(i, numCells));
       /*
      else if (cellType == 1)
        addCell(i, new rule30BinDevsCell(i, numCells));
         */
      // set cell location
      ((ViewableComponent)withName("cell_"+i)).setPreferredLocation(
          new Point(10+((25+i*130)/800)*200,(25+i*130)%800));

    }

    restOfConstructor();
  }


  public void restOfConstructor(){

    doNeighborCoupling(1,"outPos","inLeft");
    doNeighborCoupling(-1,"outPos","inRight");

//
hideAll();

/*  */
    coupleAllTo("out",this,"out");
    coupleOneToAll(this,"stop","stop");
    coupleAllTo("outPair",this,"outPair");





 activityTrans m = new activityTrans("minmaxIndiv", numCells);
 add(m);
 coupleAllTo("outPair",m,"inPair");





   addPlots(30,1400,100); //at end so that will not be coupled in
/*
  minmaxTrans mmt = new minmaxTrans("std",numCells);
  add(mmt);
coupleAllTo("outPair",mmt,"inPair");
*/

CellGridPlot timePS = new CellGridPlot(" Std Time Plot",1,10);
timePS.setSpaceSize(100,40);
timePS.setCellSize(5);
timePS.setTimeScale(100);
add(timePS);
//addCoupling(mmt,"outStd",timePS,"timePlot");
//timePS.setHidden(false);
/**/
    }

  ////////////////////////////////////////


public static void main(String args[]){

int numits = 1000;
CoordinatorInterface r;
long initTime, termTime;

//if (args[0] != null){
//int param  = Integer.parseInt(args[0]);
digraph d = new diffuseAggCellSpace();
r = new coordinator(d);


//System.out.println("Simulating "+r.getCoupled().getName() +" "+param);
r.initialize();

initTime = System.currentTimeMillis();
System.out.println("Start time: "+initTime);

for (int j = 1; j <= numits/10;j++){
r.simulate(10);

d.showState();

termTime = System.currentTimeMillis();
System.out.println("End time "+termTime);
System.out.println("Execution Time in secs. for "+ j*100 +" iterations: "
                    +((termTime-initTime)/1000.0));
}
System.exit(0);
//}
}

}

