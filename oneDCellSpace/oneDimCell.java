package  oneDCellSpace;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import genDevs.plots.*;
import statistics.*;
import simView.*;


public class oneDimCell extends ViewableAtomic implements oneDCell{
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

public oneDimCell(){
this(0);
}

public oneDimCell(String nm){
this();
}
public oneDimCell(int id){
this(id,new rand(id));
}

public oneDimCell(int id,rand r){
super("cell_"+id);
this.id = id;
this.r = r;
addInport("in");
addOutport("out");
//addOutport("outDraw");
//addOutport("outDrawTime");
addOutport("outNum");
addOutport("outSigma");
addRealTestInput("in",1);
addRealTestInput("in",numCells-1);
addRealTestInput("in",numCells-2);
}


public boolean neighbor(int otherId){
int modIdPlus = (otherId+1)%numCells;
int modIdMinus = (otherId-1)%numCells;
return modIdPlus == id || modIdMinus == id;
}

boolean initial = false;

public void initialize(){
super.initialize();
//rand r = new rand(id);
initial = false;
numTransitions = 0;
passivate();
}


public void   deltint(){
if (initial){r = new rand(id);initial = false;}
numTransitions++;
passivate();
}

public message out(){
message m = new message();
if (!initial){
m.add(makeContent("outNum",  new DrawCellEntity("drawCellToScale",
       drawPos, numTransitions)));
m.add(makeContent("outSigma",  new DrawCellEntity("drawCellToScale",
       drawPos, sigma)));
//m.add(makeContent("outDrawTime", new doubleEnt(interPulseTime)));
//m.add(makeContent("outDraw",  new DrawCellEntity("drawCellToScale",
 //      drawPos, interPulseTime)));
 }
return m;
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

public void addNeighborCoupling(int i,String outpt,String inpt){
oneDimCellSpace cs = getCellSpaceParent();
oneDCell oc = (oneDCell)cs.withName("cell_"+(id+i));
if ( oc != null){
addCoupling(getName(),outpt,oc.getName(),inpt);
}
}

public String getTooltipText(){
   return
   super.getTooltipText()
    +"\n"+" id: "+ id;
  }

}
