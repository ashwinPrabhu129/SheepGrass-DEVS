package genDevs.simulation.heapSim;

import genDevs.simulation.*;
import genDevs.modeling.*;
import java.lang.*;
import java.awt.*;
import java.io.*;
import GenCol.*;
import util.*;
import simView.*;
import java.util.*;

public class HeapCoord extends coordinator{
  protected Vector imminents,influencees;
  protected minHeap h;
  protected double min;
  protected int maxCells=150;

  public HeapCoord() {
    super();
  }

  public HeapCoord(coupledDevs c){
    super(c,false,null);
    imminents = new Vector();
    influencees = new Vector();
     h = new minHeap(maxCells);
    setSimulators();
    informCoupling();
}

  public HeapCoord(coupledDevs c, int numCells) {
    super(c,false,null);
    imminents = new Vector();
    influencees = new Vector();
    h = new minHeap(maxCells);
    setSimulators();
    informCoupling();
  }

  public void setSimulators(){
  componentIterator cit = myCoupled.getComponents().cIterator();
  while (cit.hasNext()){
    IOBasicDevs iod = cit.nextComponent();
    if(iod instanceof atomic)    //do a check on what model is
      addSimulator(iod);
    else if(iod instanceof digraph)
      addCoordinator((Coupled) iod);
  }
  tellAllSimsSetModToSim();
}


  public void addSimulator(IOBasicDevs comp){
    HeapSim s = new HeapSim(comp);
    s.setRootParent(this);      // set the parent
    simulatorCreated(s, comp);
  }

  public void addCoordinator(Coupled comp){
    HeapCoupledCoord s = new HeapCoupledCoord(comp);
    s.setRootParent(this); // set the parent
    simulatorCreated(s, comp);
  }

  public void setNewSimulator(IOBasicDevs iod){
      if(iod instanceof atomic){    //do a check on what model it is
          HeapSim s = new HeapSim(iod);
          s.setRootParent(this);
          newSimulators.add(s);
          internalModelTosim.put(iod.getName(),s);
          s.initialize(getCurrentTime());
          h.insert(s,s.getTN());
      }
      else if(iod instanceof digraph){
          HeapCoupledCoord s = new HeapCoupledCoord((Coupled)iod);
          s.setRootParent(this);
          newSimulators.add(s);
          internalModelTosim.put(iod.getName(),s);
          s.initialize(getCurrentTime());
          h.insert(s,s.getTN());
      }
  }

  public void  simulate(int num_iter){
  int j=1;
  while( (tN < DevsInterface.INFINITY) && (j<=num_iter) ) {
   computeInputOutput(tN);
    wrapDeltFunc(tN);
    tN = shortNextTN();
    j++;
  }
}

  public void  simulate_TN(double time){
    int j=1;
    while( tN < time ) {
      computeInputOutput(tN);
      wrapDeltFunc(tN);
      tL = tN;
      tN = shortNextTN();
      j++;
    }
}

public void  simulate_continue(double time){
  while( tN < time ) {
    computeInputOutput(tN);
    wrapDeltFunc(tN);
    tL = tN;
    tN = shortNextTN();
  }
}

public void initialize(){
  Iterator sit = simulators.iterator();
  while (sit.hasNext()){
    Object sim = sit.next();
    if(sim instanceof CoupledSimulatorInterface){
      coupledSimulator c = (coupledSimulator)sim;
      c.initialize();
      h.insert(c, c.getTN());
    }
    else{
      HeapCoupledCoord c = (HeapCoupledCoord)sim;
      c.initialize();
      h.insert(c, c.getTN());
    }
  }
 tL = 0;
 if (h.numCells == 0)  tN = Double.POSITIVE_INFINITY;
 else {
   tN = h.p[1].tN;
   imminents = new Vector();
   imminents.add(h.p[1].simulator);
   setImminents(1, tN);
 }
updateChangedSimulators();
}

public double shortNextTN() {
  updateHeap();
  if(h.p[1]!=null){
    min = h.p[1].tN;
    int i = 1;
    imminents = new Vector();
    imminents.add(h.p[1].simulator);
    setImminents(i, min);
  }
  else min= DevsInterface.INFINITY;
  return min;
}

public void updateHeap() {
  Iterator t = imminents.iterator();
  while(t.hasNext()){
    Object o = t.next();
    if(o instanceof HeapSim) ((HeapSim)o).changeTN();
    else if(o instanceof HeapCoupledCoord) ((HeapCoupledCoord)o).changeTN();
  }
}
public void setImminents(int i, double mintN) {
  int l= 2*i;
  int r=l+1;
  if (l<=h.numCells && h.p[l].tN==mintN ) {
    imminents.add(h.p[l].simulator);
    setImminents(l,mintN);
}
  if (r<=h.numCells && h.p[r].tN==mintN ) {
    imminents.add(h.p[r].simulator);
    setImminents(r,mintN);
  }
}

public void addInfluencee(coupledSimulator sim){
  for (int i = 0; i < influencees.size(); i++){
      coupledSimulator cs = (coupledSimulator)influencees.get(i);
      if (cs == sim) return;
  }
  influencees.add(sim);
}

public void addInfluencee(coupledCoordinator sim){
  for (int i = 0; i < influencees.size(); i++){
      coupledCoordinator cs = (coupledCoordinator)influencees.get(i);
      if (cs == sim) return;
  }
  influencees.add(sim);
}

public void computeInputOutput(double time) {
  if(imminents.size()>0)
 {
   for(int index =0; index<imminents.size();index++)
   {
     if(imminents.elementAt(index) instanceof CoupledSimulatorInterface){
       coupledSimulator c = (coupledSimulator)imminents.elementAt(index);
       c.computeInputOutput(time);
     }
     else{
       coupledCoordinator c = (coupledCoordinator)imminents.elementAt(index);
       c.computeInputOutput(time);
     }
    }
 }
 if(imminents.size()>0)
 {
   for(int index =0; index<imminents.size();index++)
   {
     if(imminents.elementAt(index) instanceof CoupledSimulatorInterface){
       coupledSimulator c = (coupledSimulator)imminents.elementAt(index);
       c.sendMessages();
     }
     else{
       coupledCoordinator c = (coupledCoordinator)imminents.elementAt(index);
       c.sendMessages();
     }
    }
 }
}

public void wrapDeltFunc(double time) {
sendDownMessages();
  for (int i = 0; i < influencees.size(); i++){
   Object o = influencees.get(i);
   boolean notIn = true;
   for (int j = 0; j < imminents.size(); j++) {
     if (o == imminents.get(j)) {
       notIn = false;
       break;
     }
   }
   if (notIn) imminents.add(o);
 }
 if(imminents.size()>0)
 {
   for(int index =0; index<imminents.size();index++)
   {
     if(imminents.elementAt(index) instanceof CoupledSimulatorInterface){
       coupledSimulator c = (coupledSimulator)imminents.elementAt(index);
       c.DeltFunc(time);
     }
     else{
       coupledCoordinator c = (coupledCoordinator)imminents.elementAt(index);
       c.DeltFunc(time);
     }
    }
 }
influencees = new Vector();
input = new message();
output = new message();
//for variable structure models
updateChangedSimulators();
}

public void updateChangedSimulators(){  // for variable structure capability
 //check if there are added or removed simulators
 Iterator nsit = newSimulators.iterator();
 Iterator dsit = deletedSimulators.iterator();
 int tempIdx=0;
 if(nsit.hasNext()||dsit.hasNext()){
     // need to update the simulators and download the internalModelTosim to simulators
     while (nsit.hasNext()){
       Object o = nsit.next();
       simulators.add(o);
       if(o instanceof HeapSim){
         HeapSim s = (HeapSim)o;
       }
     }
     while (dsit.hasNext()) {
       Object o = dsit.next();
        if (o instanceof HeapSim) {
          tempIdx = ((HeapSim)o).myheapIdx;
        }
        else if (o instanceof HeapCoupledCoord) {
          tempIdx = ((HeapCoupledCoord)o).myheapIdx;
        }
       simulators.remove(o);
       if (tempIdx>0)
         h.delete(tempIdx);
     }
     Iterator sit = simulators.iterator();
     while (sit.hasNext()){
       Object sim = sit.next();
       if(sim instanceof CoupledSimulatorInterface){
         coupledSimulator c = (coupledSimulator)sim;
         c.setModToSim(internalModelTosim);
       }
       else{
         HeapCoupledCoord c = (HeapCoupledCoord)sim;
         c.setModToSim(internalModelTosim);
       }
     }
 }
 // reset newSimulators and deletedSimulators to empty
 newSimulators = new ensembleSet();
 deletedSimulators = new ensembleSet();
}

}
