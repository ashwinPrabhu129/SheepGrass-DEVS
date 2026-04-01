package  oneDCellSpace;

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
import genDevs.simulation.special.*;

////////////////////////////////////
class rule30Cell extends oneDimCell{
  public final int cellType;
  public static int maxRows, numCells;
  protected double delta = 1, activity = 0.0, numTrueTransitions=0.0;
  protected int row = 1, val = 0, lastval=-1, right=-1, left=-1, lright=-1, lleft=-1;
  boolean confluent = false;
  double clock; //bpz  to get actual transitions in binDevs
  int numOutputs; //bpz

  public rule30Cell(){
    this(0,0);
  }

  public rule30Cell(int i, int cellType){
    super(i);
    this.cellType=cellType;
     setHidden(false);
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
    left = 0; lleft=-1;
    right = 0; lright=-1;

 //  if (id == (numCells/2)){
      // center cell
//   val = 1;
// }
/*
    r = new rand(id);
    val = r.uniform(1)>.5?0:1;
    holdIn("active",0);
*/
    if (id == (numCells/2)){
      // center cell
      val = 1;
    holdIn("active",0);
    }
     else passivate();

  }

  int rule30(){
    int l, r;
    l=left;
    r=right;

////    //////////////////////////////////////////// rule 30
//    if (l==1 && val==1 && r==1)
//      // 111->0
//      return 0;
//    else if (l==1 && val==1 && r==0)
//      // 110->0
//      return 0;
//    else if (l==1 && val==0 && r==1)
//      // 101->0
//      return 0;
//    else if (l==1 && val==0 && r==0)
//      // 100->1
//      return 1;
//    else if (l==0 && val==1 && r==1)
//      // 011->1
//      return 1;
//    else if (l==0 && val==1 && r==0)
//      // 010->1
//      return 1;
//    else if (l==0 && val==0 && r==1)
//      // 001->1
//      return 1;
//    else if (l==0 && val==0 && r==0)
//      // 000->0
//      return 0;
//    else
//      return -1;

    //////////////////////////////////////////// rule 110
    if (l==1 && val==1 && r==1)
      // 111->0
      return 0;
    else if (l==1 && val==1 && r==0)
      // 110->1
      return 1;
    else if (l==1 && val==0 && r==1)
      // 101->1
      return 1;
    else if (l==1 && val==0 && r==0)
      // 100->0
      return 0;
    else if (l==0 && val==1 && r==1)
      // 011->1
      return 1;
    else if (l==0 && val==1 && r==0)
      // 010->1
      return 1;
    else if (l==0 && val==0 && r==1)
      // 001->1
      return 1;
    else if (l==0 && val==0 && r==0)
      // 000->0
      return 0;
    else
      return -1;
  }


  public message out(){
    /**/

    message m = super.out();

//    m.add(makeContent("outDraw",  new DrawCellEntity("drawCellToScale",
//                                                     drawPos, activity)));

    row = (int)(this.getSimulationTime());
    //System.out.println("xxxxxxxxxxxxxxxxx "+id+ "  drwPos="+drawPos + "  row="+row + " y="+(.5*maxRows - row));
    m.add(makeContent("outMove",new DrawCellEntity(
                                                   //drawPosI, drawPosJ,
                                                   drawPos,cellType==0?.5*maxRows - row:0,
                                                   val == 1?Color.black:Color.white,
                                                   val == 1?Color.black:Color.white
                                                   )));
//     m.add(makeContent("outPair", new Pair(new Integer(id),
//                             new Integer(val))));
    return  outputIntOnPort(m,val,"outPos");

  }

  public String getTooltipText(){
    return
    super.getTooltipText()
        +"\n"+"numTransitions :"+ numTransitions
        +"\n"+"numTrueTransitions :"+ numTrueTransitions
        +"\n"+"activity :"+ activity
        +"\n"+"clock :"+ clock
        +"\n"+"numOutputs :"+ numOutputs
        +"\n"+"row :"+ row
        +"\n"+"val :"+ val;
  }
}
///////////////////////////////////////
// rule30BinCell
///////////////////////////////////////
class rule30BinCell extends rule30Cell{

  public rule30BinCell(int i, int numCells){
    super(i,0);
    this.numCells=numCells;
  }

  public void initialize(){
    super.initialize();
  }

public void addRule30Neighbor(){
addNeighbor(1,new rule30BinCell(id+1,numCells));
addNeighborCoupling(1,"outPos","inLeft");
addNeighbor(-1,new rule30BinCell(id-1,numCells));
addNeighborCoupling(-1,"outPos","inLeft");
addCoupling("cell_"+(id+1),"outMove","Rule30 Move Plot","drawCellToScale");
}

  public void deltext(double e,message x){

  //addRule30Neighbor();
    clock += e;
    Continue(e);

    if (somethingOnPort(x,"inLeft") || somethingOnPort(x,"inRight")){
      if (somethingOnPort(x,"inLeft"))
        left =  getIntValueOnPort(x,"inLeft");
      if (somethingOnPort(x,"inRight"))
        right = getIntValueOnPort(x,"inRight");

//      if(id==19)
//    	  System.out.println("break");
      val = rule30(); // compute my new value

      if (val != lastval){
        // true transition
        lastval = val;
        numTrueTransitions++;
      }
       numOutputs++;
       holdIn("active",delta);
       //System.out.println("xxxxxxxxxxxxxxxxx "+id+ "  "+val);
    }

     if (confluent==false)
      numTransitions++; // total transitions
  }

  public void deltint(){
    row++;            // keep track of plot location (row)
    numOutputs++;
    holdIn("active",delta);
    // compute new inherent activity
   // activity = numTrueTransitions / (double)numTransitions;
     double distFromCenter =  Math.abs(numCells/2 -id);
    if (row >=distFromCenter)
   activity = numTrueTransitions / ((double)numTransitions-distFromCenter);

  //  if (numTransitions==numCells/2)
  //    passivateIn("max reached");
    if (confluent==false){
      numTransitions++; // total transitions
      clock += sigma;
      }
  }

  public void deltcon(double e,message x){
    confluent=true;
    deltext(e,x);
    deltint();
    confluent=false;
    numTransitions++; // total transitions
  }
}
////////////////////////////////////////////////////////
//Only replicate one dim cell transitions not 2D pattern
////////////////////////////////////////////////////////
class rule30BinDevsCell extends rule30Cell{
  protected int lastval, input;
  protected boolean first;

  public rule30BinDevsCell(int i, int numCells){
    super(i,1);
    this.numCells=numCells;
  }

  public void initialize(){
    super.initialize();
    if (val == 1){
      // center cell
      lastval=1;
      numOutputs++;
      holdIn("active",0);
    } else
      passivate();
  }
public void addRule30BinNeighbor(){
addNeighbor(1,new rule30BinCell(id+1,numCells));
addNeighborCoupling(1,"outPos","inLeft");
addNeighbor(-1,new rule30BinCell(id-1,numCells));
addNeighborCoupling(-1,"outPos","inLeft");
addCoupling("cell_"+(id+1),"outMove","Rule30 Move Plot","drawCellToScale");
}

  public void deltext(double e,message x){

  addRule30BinNeighbor();
    clock += e;
    Continue(e);

    numTransitions++; // keep track of every transition (ext and int)

    if (somethingOnPort(x,"inLeft") || somethingOnPort(x,"inRight")){
      if (somethingOnPort(x,"inLeft")){
        left = getIntValueOnPort(x,"inLeft");
      }
      if (somethingOnPort(x,"inRight")){
        right = getIntValueOnPort(x,"inRight");
      }
      if (left != lleft || right != lright){
        // at least one neighbour has changed

        // update my neighbours' states
        lright = right;
        lleft = left;
        // update my state
        val = rule30();

        if (val != lastval){
          // true transition
          lastval = val;
          numTrueTransitions++;
          numOutputs++;
          holdIn("active",delta);
        }else{
          // not a true transition
          // System.out.println(id+" : passivate(), val " + val + "!= lastval " + lastval);
          passivate(); // -> nothing to tell my neighbours -> passivate
        }
      }
      else
        // neighbours not changed, thus me neither
        passivate(); // -> nothing to tell my neighbours -> passivate
    }
    activity = numTrueTransitions / (double)numTransitions;
  //System.out.println("xxxxxxxxxxxxxxxxx "+id+ "  "+val);
  }
  public void deltint(){
    // new internal computation
    // notice : not executed in a confluent situation
    //          only when passive...
    clock += sigma;
    numTransitions++;

    val = rule30();

    if (val != lastval){
      lastval = val;
      numTrueTransitions++;
      numOutputs++;
      holdIn("active",delta);
    }
    else
      passivate();
    // compute new inherent activity
    activity = numTrueTransitions / (double)numTransitions;
  }

  public void deltcon(double e,message x){
    // only external transitions
    deltext(e,x);
    // compute new inherent activity
    activity = numTrueTransitions / (double) numTransitions;
  }

  public String getTooltipText(){
    return
    super.getTooltipText()
        +"\n"+"lastval :"+ lastval
        +"\n"+"right :"+ right
        +"\n"+"left :"+ left;
  }

}
/////////////////////////////////////////////////

public class rule30CellSpace extends oneDimCellSpace{
  protected final int cellType;

  public rule30CellSpace(){
    // number of cells
    this (40);//1000);//(500);//this(17);
  }

  public rule30CellSpace(int numCells){
    /////////////////////////////
    // change configuration here
    // (0) rule30BinCell
    // (1) rule30BinDevsCell
    /////////////////////////////
    this(numCells, 0);// 60);
  }

  public rule30CellSpace(int numCells, int cellType){

    super("rule30 "+numCells);
    this.cellType=cellType;

    addInport("inLeft");
    addInport("inRight");
    addOutport("out");
    addOutport("outDraw");

    rule30Cell.numCells = numCells;
    rule30Cell.maxRows = numCells;

    this.numCells = numCells;
   // for (int i = numCells/2-3;i<numCells/2+3;i++){
   for (int i = 0;i<numCells;i++){
      if (cellType == 0)
        addCell(i, new rule30BinCell(i, numCells));
      else if (cellType == 1)
        addCell(i, new rule30BinDevsCell(i, numCells));
      // set cell location
      ((ViewableComponent)withName("cell_"+i)).setPreferredLocation(
          new Point(10+((25+i*130)/800)*200,(25+i*130)%800));
    }

    restOfConstructor();
  }

  public void restOfConstructor(){

    doNeighborCoupling(1,"outPos","inLeft");
    doNeighborCoupling(-1,"outPos","inRight");

    //hideAll();

/**/

    coupleAllTo("out",this,"out");
    coupleOneToAll(this,"stop","stop");
    coupleAllTo("outPair",this,"outPair");

//    CellGridPlot t = new CellGridPlot(
//        "Rule30 Move Plot",0, numCells,numCells);
    newCellGridPlot t = new newCellGridPlot(
            "Rule30 Move Plot",0, numCells*5+40,numCells*5+40);
    //t.setSpaceSize(100, 60);
    t.setCellSize(5);
    t.setCellGridViewLocation(10,10);

    add(t);
    //t.setHidden(false);
    coupleAllTo("outMove",t,"drawCellToScale");

    // addPlots(2,200,80);//stateMax,transition,Max,time
/*
    int transitionMax=1000;
    CellGridPlot totalTransitionP = new CellGridPlot("TOTAL Transition Plot",1,
                                                     "location", numCells, // x
                                                     "transitions", transitionMax); // y
    add(totalTransitionP);
    coupleAllTo("outNum",totalTransitionP,"drawCellToScale");


    CellGridPlot activityP = new CellGridPlot("Inherent Activity Plot", 1,
                                           "location", numCells, // x
                                        "state", 2);   // y
    add(activityP);
    //
     activityP.setHidden(false);
    coupleAllTo("outDraw", activityP,"drawCellToScale");
  //  preferredSize = new Dimension(946, 559);

    ViewableDigraph def = new discActivityExpFrame("def",numCells);
    add(def);
    coupleAllTo("outPair",def,"inPair");
*/
    }

  ////////////////////////////////////////


public static void main(String args[]){
int numcells = 200;//100;//1000;//5000;
CoordinatorInterface r;
long initTime, termTime;

//if (args[0] != null){
int param  = 0;//Integer.parseInt(args[0]);
r = new coordinator(new rule30CellSpace(numcells,param));
//
//r = new oneDCoord(new rule30CellSpace(numcells,param),10,numcells);


System.out.println("Simulating "+r.getCoupled().getName() +" "+param);
r.initialize();

initTime = System.currentTimeMillis();
System.out.println("Start time: "+initTime);

//for (int j = 1; j <= numcells/100;j++){
r.simulate(numcells);

termTime = System.currentTimeMillis();
System.out.println("End time "+termTime);
//System.out.println("Execution Time in secs. for "+ j*100 +" iterations: "
//                    +((termTime-initTime)/1000.0));
//}
//System.exit(0);
//}
}
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(900, 1300);
        ((ViewableComponent)withName("cell_52")).setPreferredLocation(new Point(268, 46));
        ((ViewableComponent)withName("cell_49")).setPreferredLocation(new Point(1410, 795));
        ((ViewableComponent)withName("cell_53")).setPreferredLocation(new Point(69, 22));
        ((ViewableComponent)withName("cell_51")).setPreferredLocation(new Point(389, 14));
        ((ViewableComponent)withName("cell_48")).setPreferredLocation(new Point(56, 64));
        ((ViewableComponent)withName("cell_50")).setPreferredLocation(new Point(1610, 125));
        ((ViewableComponent)withName("cell_46")).setPreferredLocation(new Point(161, 105));
        ((ViewableComponent)withName("cell_45")).setPreferredLocation(new Point(4, 92));
        ((ViewableComponent)withName("cell_47")).setPreferredLocation(new Point(514, 16));
    }
}

