
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



class pascalCell extends oneDimCell{

public double delta = 1;

protected int cnk = 0, row  = 1,lastcnk;

public pascalCell(){
this(0);
}

public pascalCell(int i){
super(i);
addInport("inLeft");
addInport("inRight");
addOutport("out");
addOutport("outMove");
addOutport("outPos");
//
setHidden(true);
}




public void initialize(){
super.initialize();
numTransitions = 0;
row = 1;
cnk = 1;
if (id == 1) cnk =2;
if (id == 0 || id == 1)
holdIn("active",0);
}


public void deltext(double e,message x){
addPascalNeighbor();
Continue(e);

if (somethingOnPort(x,"inLeft")){
int in =  getIntValueOnPort(x,"inLeft");

cnk = cnk + in;
if (cnk != lastcnk){
numTransitions++;
lastcnk = cnk;
}
holdIn("active",delta);
}
}

public void addPascalNeighbor(){
addNeighbor(1,new pascalCell(id+1));
addNeighborCoupling(1,"outPos","inLeft");
addCoupling("cell_"+(id+1),"outMove","Pascal Move Plot","drawCellToScale");
}

public void   deltint(){ //effective transitions
if (cnk != lastcnk){
numTransitions++;
lastcnk = cnk;
}
row++;
holdIn("active",delta);
}

public void deltcon(double e,message x){
deltext(e,x);
row++;
}

public message out(){
/**/
message m = super.out();



m.add(makeContent("outDraw",  new DrawCellEntity("drawCellToScale",
       drawPos, Math.log(cnk))));

m.add(makeContent("outMove",new DrawCellEntity(
            //drawPosI, drawPosJ,
            drawPos,.5*numCells - row,
              cnk%2 == 1?Color.black:Color.white,
              cnk%2 == 1?Color.black:Color.white
              )));

m.add(makeContent("outPair", new Pair(new Integer(id),
                             new Integer(cnk))));

m.add(makeContent("out",new doubleEnt(id)));


return  outputIntOnPort(m,cnk,"outPos");

//return  outputRealOnPort(cnk,"outPos");
}

public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"cnk :"+ cnk
  +"\n"+"row :"+ row;
  }
}
//////////////////////////////////////////////

class pascalBinCell extends pascalCell{

public pascalBinCell(){
this(0);
}

public pascalBinCell(int i){
super(i);
}




public void initialize(){
super.initialize();
if (id == 1) cnk = 0;
}


public void deltext(double e,message x){

Continue(e);

if (somethingOnPort(x,"inLeft")){
int in = getIntValueOnPort(x,"inLeft");


cnk = cnk + in;
cnk = cnk%2;
if (cnk != lastcnk){
numTransitions++;
lastcnk = cnk;
}
holdIn("active",delta);


}
}


}
/////////////////////////////////////////////////

//Only replicate one dim cell transitions not 2D pattern

class pascalBinDevsCell extends pascalBinCell{
protected int lastcnk,input;
protected boolean first;


public pascalBinDevsCell(){
this(0);
}

public pascalBinDevsCell(int i){
super(i);
}




public void initialize(){
super.initialize();
first = true;
if (id == 1){
 cnk =0;
//passivate();
}
//lastcnk = cnk;
input = 0;
}


public void deltext(double e,message x){

Continue(e);

if (somethingOnPort(x,"inLeft")){
int in = getIntValueOnPort(x,"inLeft");
if (in != input|| first){
  input = in;

int newcnk = cnk + in;
newcnk = newcnk%2;
if (newcnk != cnk || first){
   cnk = newcnk;
  // row++;
   numTransitions++;
   holdIn("active",delta);
   }
//else holdIn("rowIncr",delta);
}
}
}

public void   deltint(){
//row++;
int newcnk = cnk + input;
newcnk = newcnk%2;
if (newcnk != cnk){
numTransitions++;
   cnk = newcnk;
   holdIn("active",delta);
   }
//else holdIn("rowIncr",delta);
else passivate();

}

public void deltcon(double e,message x){
deltext(e,x);

}

public String getTooltipText(){
   return
  super.getTooltipText()
    +"\n"+"lastcnk :"+ lastcnk
  +"\n"+"input :"+ input;
  }

}
/////////////////////////////////////////////////

public class pascalCellSpace extends oneDimCellSpace{



public pascalCellSpace(){
this(100);
}

public pascalCellSpace(int numCells){
this(numCells,0);
}

public pascalCellSpace(int numCells,int cellType){

super("pascal "+numCells);
addInport("inLeft");
addInport("inRight");
addOutport("out");
addOutport("outDraw");


pascalCell.numCells =  numCells;

/*
for (int i = 0;i<numCells;i++)
   if (cellType == 0)addCell(i,new pascalCell(i));
   else if (cellType == 1)addCell(i,new pascalBinCell(i));
     else addCell(i,new pascalBinDevsCell(i));
*/
addCell(0,new pascalCell(0));
addCell(1,new pascalCell(1));
  restOfConstructor();
}



public void restOfConstructor(){

doNeighborCoupling(1,"outPos","inLeft");


//hideAll();

coupleAllTo("out",this,"out");
coupleOneToAll(this,"stop","stop");
coupleAllTo("outPair",this,"outPair");

CellGridPlot t = new CellGridPlot( "Pascal Move Plot",1,numCells,numCells);

t.setCellGridViewLocation(600,300);
add(t);
//t.setHidden(false);
coupleAllTo("outMove",t,"drawCellToScale");

/*
//addPlots(100000,50,100);//stateMax,transition,Max,time

//addPlots(100,500,100);//stateMax,transition,Max,time



 ViewableDigraph def = new discActivityExpFrame("def",numCells);
 add(def);
 coupleAllTo("outPair",def,"inPair");
*/
}


public static void main(String args[]){
int numcells = 100;
CoordinatorInterface r;
long initTime, termTime;



//if (args[0] != null){
//
int param  = 1;//Integer.parseInt(args[0]);


int base = numcells;

//
r = new coordinator(new pascalCellSpace(numcells,param));
//r = new DTSSCoord(new pascalCellSpace(numcells,param));
//r = new oneDCoord(new pascalCellSpace(numcells,param),numcells,base);

System.out.println("Simulating "+r.getCoupled().getName() +" "+param);
r.initialize();


initTime = System.currentTimeMillis();
System.out.println("Start time: "+initTime);

for (int j = 1; j <= numcells/100.;j++){
r.simulate(100);

termTime = System.currentTimeMillis();
System.out.println("End time "+termTime);
System.out.println("Execution Time in secs. for "+ j*100 +" iterations: "
                    +((termTime-initTime)/1000.0));
}
System.exit(0);
//}
}

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(900, 1300);
        ((ViewableComponent)withName("cell_4")).setPreferredLocation(new Point(241, 145));
        ((ViewableComponent)withName("cell_0")).setPreferredLocation(new Point(-19, 289));
        ((ViewableComponent)withName("cell_3")).setPreferredLocation(new Point(26, 146));
        ((ViewableComponent)withName("cell_1")).setPreferredLocation(new Point(187, 300));
        ((ViewableComponent)withName("cell_2")).setPreferredLocation(new Point(410, 297));
    }
}

