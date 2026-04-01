package  oneDCellSpace;




import java.lang.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import genDevs.modeling.*;
import genDevs.plots.*;
import genDevs.simulation.*;
import GenCol.*;
import util.*;
import simView.*;



public class oneDimCellSpace extends ViewableDigraph{


public int numCells = 100;
    /**
     * See parent method.
     */
public boolean layoutForSimViewOverride()
    {
        preferredSize = new Dimension(900, 1300);

        ViewableComponentUtil.layoutCellsInGrid(numCells, "oneDCell_", 3, this,
            200, 70);
        return true;
    }

public void doNeighborCoupling(int i,String sourcePt,String destPt){
componentIterator it1 = components.cIterator();
while(it1.hasNext()) {
   Object o1 = it1.nextComponent();
  if (o1 instanceof oneDCell){
    oneDCell d1 = (oneDCell)o1;
     componentIterator it2 = components.cIterator();
      while(it2.hasNext()) {
         Object o2 = it2.nextComponent();
       if (o2 instanceof oneDCell){
        oneDCell d2 = (oneDCell)o2;
        if (d1.getId()+i == d2.getId())
        addCoupling(d1,sourcePt,d2,destPt);
        }
   }
   }
   }
}

public void hideAll(){
componentIterator it1 = components.cIterator();
while(it1.hasNext()) {
   Object o1 = it1.nextComponent();
    ViewableComponent d1 = (ViewableComponent)o1;
    d1.setHidden(true);
        }
}

public oneDCell addCell(int i,oneDCell another){
add(another);
another.setDrawPos(i-numCells/2);
return another;
}


////////////////////////////////////////
public void setDrawPos(int i){
if (this instanceof oneDimCellSpace){
    componentIterator it1 = components.cIterator();
    while(it1.hasNext()) {
    devs d1 = (devs)it1.nextComponent();
     if (d1 instanceof oneDimCellSpace)
        ((oneDimCellSpace)d1).setDrawPos(i);
    else if (d1 instanceof oneDCell)
        ((oneDCell)d1).setDrawPos(i);
        }
        }
 }

public void addPlots(double stateMax,int transitionMax,double timeMax){
addPlots(stateMax,transitionMax,timeMax,1);
}

public void addPlots(double stateMax,int transitionMax,double timeMax,
                             double delay){


CellGridPlot transitionP = new CellGridPlot(" Transition Plot",delay,
                        "location",numCells,
                        "transitions", transitionMax);
//transitionP.setHidden(false);
add(transitionP);


CellGridPlot stateP = new CellGridPlot(" State Plot",delay,"location",
                        numCells,
                       "state",stateMax);
add(stateP);

CellGridPlot sigmaP = new CellGridPlot(" Time Advance Plot",delay,
                        "location",numCells,
                        "time advance",100 );
    add(sigmaP);


CellGridPlot timeP = new CellGridPlot(" Time Plot",10*delay,stateMax);
timeP.setSpaceSize(100,40);
timeP.setCellSize(5);
timeP.setTimeScale(timeMax);
add(timeP);


    componentIterator it1 = components.cIterator();
    while(it1.hasNext()) {
    devs d1 = (devs)it1.nextComponent();
    if (!d1.eq(" State Plot"))
      addCoupling(d1,"outDraw",stateP,"drawCellToScale");
    if (!d1.eq(" Transition Plot"))
      addCoupling(d1,"outNum",transitionP,"drawCellToScale");
    if (!d1.eq(" Time Advance Plot"))
      addCoupling(d1,"outSigma",sigmaP,"drawCellToScale");
    if (!d1.eq(" Time Plot"))
       addCoupling(d1, "outDrawTime", timeP, "timePlot");

   setPlotLocations(stateP,timeP,transitionP,sigmaP);
}

}

public void addPlots(String nm,String from)
{
    CellGridView[] w  = {
        ((CellGridPlot)withName(from + " Plot")).getCellGridView(),
        ((CellGridPlot)withName(from + " Transition Plot")).getCellGridView(),
        ((CellGridPlot)withName(from + " Time Plot")).getCellGridView(),
        ((CellGridPlot)withName(from + " Time Advance Plot")).getCellGridView(),
    };

    CellGridPlot stateP = new CellGridPlot(nm + " Plot",1,w[0]);
    add(stateP);

    CellGridPlot transitionP = new CellGridPlot(nm+" Transition Plot",1,w[1]);
    add(transitionP);

    CellGridPlot timeP = new CellGridPlot(nm +" Time Plot",10,w[2]);
    add(timeP);

    timeP.setTimeScale(((CellGridPlot)withName(from + " Time Plot")).getTimeScale());

    CellGridPlot sigmaP = new CellGridPlot(nm+" Time Advance Plot",1,w[3]);
    add(sigmaP);

    doPlotCoupling(nm, stateP, timeP, transitionP, sigmaP);

}

    protected void doPlotCoupling(String name, CellGridPlot stateP,
         CellGridPlot timeP, CellGridPlot transitionP, CellGridPlot sigmaP)
    {
        devs d = (devs)withName(name);
        addCoupling(d, "outDraw", stateP, "drawCellToScale");
       // addCoupling(d, "out", timeP, "timePlot");
         addCoupling(d, "outNum", transitionP, "drawCellToScale");
         addCoupling(d, "outSigma", sigmaP, "drawCellToScale");
    }

    protected void setPlotLocations(CellGridPlot stateP,
         CellGridPlot timeP, CellGridPlot transitionP, CellGridPlot sigmaP)
    {
        boolean largeScreen = true;
        if (largeScreen) {
            stateP.setCellGridViewLocation(660,1);
            timeP.setCellGridViewLocation(660,440);
            transitionP.setCellGridViewLocation(200,700);
            sigmaP.setCellGridViewLocation(660,700);
        }
        else {
            stateP.setCellGridViewLocation(200,20);
            timeP.setCellGridViewLocation(220,40);
            transitionP.setCellGridViewLocation(240,60);
            sigmaP.setCellGridViewLocation(260,80);
        }

    }


public oneDimCellSpace(){
this("oneDimCellSpace");
}

public oneDimCellSpace(String nm, int numCells_){
 this(nm);
 this.numCells = numCells_;
}

public oneDimCellSpace(String nm){
super(nm);
/*
oneDimCell.numCells = this.numCells = 40;

for (int i = 0;i<numCells;i++)
  addCell(i,new oneDimCell(i));

doAllToAllCoupling();

CellGridPlot transitionP = new CellGridPlot(" Transition Plot",1,
                        "location",numCells,
                        "transitions", 500);
//transitionP.setHidden(false);
add(transitionP);
    componentIterator it1 = components.cIterator();
    while(it1.hasNext()) {
    devs d1 = (devs)it1.nextComponent();
    if (!d1.eq(" Transition Plot"))
      addCoupling(d1,"outNum",transitionP,"drawCellToScale");

    }

CellGridPlot stateP = new CellGridPlot(" State Plot",1,"location",
                        numCells,
                       "state",3);
add(stateP);

     it1 = components.cIterator();
    while(it1.hasNext()) {
    devs d1 = (devs)it1.nextComponent();
    if (!d1.eq(" State Plot"))
      addCoupling(d1,"outDraw",stateP,"drawCellToScale");

    }
    */
}


}
