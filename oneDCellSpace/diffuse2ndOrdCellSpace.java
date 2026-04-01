package  oneDCellSpace;

import java.lang.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import genDevs.modeling.*;
import genDevs.plots.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;
import GenCol.*;
import statistics.*;
import simView.*;
import pulseExpFrames.*;
import genDevs.simulation.special.*;
import quantization.*;

class diffuse2ndOrdCell extends oneDFrom2Dint implements oneDCell{

protected int id = 0;
protected rand r;
protected int drawPos,numTransitions;


public void setDrawPos(int i){
drawPos = i;
}

public int getId(){
return id;
}

public static int numCells = 10;

public void addNeighborCoupling(int i,String outpt,String inpt){
oneDimCellSpace cs = getCellSpaceParent();
oneDCell oc = (oneDCell)cs.withName("cell_"+(id+i));
if ( oc != null){
addCoupling(getName(),outpt,oc.getName(),inpt);
}
}


public oneDimCellSpace getCellSpaceParent(){
IODevs p = getParent();
return (oneDimCellSpace)p;
}

public void addNeighbor(int i,oneDCell n){
oneDimCellSpace cs = getCellSpaceParent();
if (cs.withName("cell_"+(id+i)) == null){
cs.addCell(id+1,n);
addModel(n);
}
}

public static double speedFactor = 1,quantum =.01;//.001;////.0001;// .003;//.005;
public static double height = 11;
public static double length = 11;
public static double deltaxsq = (length/numCells)*(length/numCells);


protected double myPos,leftPos,rightPos,nextPos,speed;


public diffuse2ndOrdCell(){
this(0);
}

public diffuse2ndOrdCell(int id){
super("cell_"+id,quantum,0);
this.id = id;
this.r = r;
speedFactor = 1;
addOutport("outDraw");
addOutport("outNum");
addOutport("outSigma");
addInport("inLeft");
addInport("inRight");
addOutport("outPair");
addOutport("outPos");
}


public void initialize(){
myPos = leftPos = rightPos = nextPos = quantum;
myPos = id*height/(numCells-1)*(length/numCells);//use the actual value,
                                                 //not the density
speed = 10;
if (id > numCells/2)speed = speed*speedFactor;


     input = new  vect2DEnt(0,0);
     lastInput = input;
      state = new vect2DEnt(myPos,0);
     remainingQuant = quantum;
     sigma = 0;
     phase = state.toString();
// super.initialize();
     nextState = state.copy();
     clock = 0.0;

     elapsed = 0;
deriv2 = vect2DEnt.ZERO;
}


public void  deltext(double e,message   x)
{

message nm = new message();

double sum = 0;
if (somethingOnPort(x,"inLeft"))
leftPos = getRealValueOnPort(x,"inLeft");

if (somethingOnPort(x,"inRight"))
rightPos = getRealValueOnPort(x,"inRight");

nm.add(makeContent("in",new doubleEnt(deriv())));

super.deltext(e,nm);
myPos = state.x;

}


public void  deltint( )
{
numTransitions++;
clock = clock + sigma;
state = nextState.copy();
computeIntNextstate();
phase = state.toString();
}


public double avgNeighPos(){
if (id == 0)leftPos = myPos;
else if (id == numCells-1) rightPos = myPos;

if (id != numCells/2)
return (leftPos + rightPos)/2;
else
return (leftPos +rightPos*speedFactor)/(1+speedFactor);
}

public double spatialDeriv(){
if (id != numCells/2)
return 2*(avgNeighPos() - myPos)/deltaxsq;
else return (1+speedFactor)*(avgNeighPos() - myPos)/deltaxsq;
}

public double deriv(){
return speed*spatialDeriv();
}



int count = 0;

public message    out( )
{
message   m = new message();

m.add(makeContent("outNum",  new DrawCellEntity("drawCellToScale",
       drawPos, numTransitions)));
m.add(makeContent("outSigma",  new DrawCellEntity("drawCellToScale",
       drawPos, sigma)));

if (clock <= 0 && sigma == 0){
m.add(makeContent("outPos",  new doubleEnt(state.x)));
m.add(makeContent("outDraw",  new DrawCellEntity("drawCellToScale",
       drawPos, state.x)));
              }
else{
m.add(makeContent("outPos",  new doubleEnt(nextState.x)));
m.add(makeContent("outDraw",  new DrawCellEntity("drawCellToScale",
       drawPos, state.x)));

   }
return m;
}


public String getTooltipText(){
   return
   super.getTooltipText()
    +"\n"+" myPos: "+ myPos
    +"\n"+" nextPos: "+ nextPos
      +"\n"+" rightPos: "+ rightPos
    +"\n"+" leftPos: "+ leftPos;
  }

}

public class diffuse2ndOrdCellSpace extends oneDimCellSpace{

public diffuse2ndOrdCellSpace(){
this(50);
}

public diffuse2ndOrdCellSpace(int numCells){
//super("diffuse2ndOrdCellSpace "+numCells);
super("diffuse2ndOrdCellSpace "+numCells,numCells);
addInport("outPair");

diffuse2ndOrdCell.numCells = numCells;


for (int i = 0;i<numCells;i++)
  addCell(i,new diffuse2ndOrdCell(i));

//
hideAll();  //hides only components so far


doNeighborCoupling(1,"outPos","inLeft");//to right on inLeft
doNeighborCoupling(-1,"outPos","inRight");//to left on inRight

totalTransitions t = new totalTransitions();
add(t);
coupleAllTo("outNum",t,"in");
/*
activityTrans m = new activityTrans("minmaxIndiv", numCells);
add(m);
coupleAllTo("outPair",m,"inPair");
*/
addPlots((double)3*11*11/numCells,1400,100,.1); //at end so that will not be coupled in

}


public static void main(String args[]){

int numCells = 50,numiter = 100000;//2*numCells;
//int numCells = 2000,numiter = 10000;//100;//2*numCells;
int base = 6;//100;//5;// 2;//50;

//coordinator c = new coordinator(new diffuse2ndOrdCellSpace(numCells ));
//TunableCoordinator c = new TunableCoordinator(new diffuse2ndOrdCellSpace(numCells));
//c.setTimeScale(50);

oneDCoord c = new oneDCoord(new diffuse2ndOrdCellSpace(numCells),base,numCells);
//oneDCoord c = new oneDCoord(new diffuse2ndOrdCellSpace(numCells),base,numCells);
c.initialize();

System.out.println("Simulating "+c.getCoupled().getName()+" base "+base);
c.initialize();

long initTime,termTime;

initTime = System.currentTimeMillis();
System.out.println("Start time: "+initTime);

c.simulate(numiter);

termTime = System.currentTimeMillis();
System.out.println("End time "+termTime);
System.out.println("Execution Time in secs. for "+ numiter +" iterations: "
                    +((termTime-initTime)/1000.0));
System.exit(0);

}
}

/////////////////////////

class totalTransitions extends  sum{


public totalTransitions(){
super("totalTransitions");
}


public void initialize(){
super.initialize();
state = initState;
passivateIn(""+state);
}

public void deltext(double e,message x){
Continue(e);
if (somethingOnPort(x,"in")){
state += x.getLength();
 passivateIn(""+state);
   }
else if (somethingOnPort(x,"reset")){
 // state = 0;
 //passivateIn("passive "+state);
  holdIn("outAccum",0);
 }
}



 }
