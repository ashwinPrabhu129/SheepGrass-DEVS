package genDevs.simulation.heapSim;


import genDevs.simulation.*;
import genDevs.modeling.*;
import java.lang.*;
import java.awt.*;
import java.io.*;
import GenCol.*;
import util.*;
import simView.*;
import oneDCellSpace.*;
import twoDCellSpace.TwoDimCell;


public class HeapSim  extends coupledSimulator{
protected boolean tNChanged=true;
protected int myheapIdx = 0;

public HeapSim(IOBasicDevs cell){
  super(cell);
}

public int myModelId(){
  if (myModel instanceof oneDCell) {
    oneDCell oc = (oneDCell)myModel;
    int i = oc.getId();
    return i;
  }
  else if(myModel instanceof TwoDimCell) {
    TwoDimCell oc = (TwoDimCell)myModel;
    int i = oc.getOneDimId();
    return i;
  }
return hashCode();
}
public synchronized void initialize(){ //for non real time usage, assume the time begins at 0
  super.initialize();
 }

public double nextTN(){
if(tN<DevsInterface.INFINITY){
  if(getRootParent()!=null)
      ((HeapCoord)getRootParent()).h.updateTN(this,tN);
  else if(getParent()!=null)
    ((HeapCoupledCoord)getParent()).h.updateTN(this,tN);}
return tN;
}

public boolean  equalTN(double t){
   return t == tN;
}

public double getTN(){
return tN;
}

public double getTL(){
return tL;
}

 public void setTN(double tt){ //Xiaolin Hu, Sept, 08, used in partial modular implementation
  tN = tt;
  if(getRootParent()!=null)
      ((HeapCoord)getRootParent()).h.updateTN(this,tN);
  else if(getParent()!=null)
      ((HeapCoupledCoord)getParent()).h.updateTN(this,tN);
  tNChanged = true;
 }

public void DeltFunc(double t) {
   wrapDeltfunc(t,input);
   input = new message();
}

public void putMessages(ContentInterface c){
input.add(c);
if(getRootParent()!=null)
    ((HeapCoord)getRootParent()).addInfluencee(this);
else if(getParent()!=null)
    ((HeapCoupledCoord)getParent()).addInfluencee(this);
}

public void changeTN() {
  if(tNChanged){
    tNChanged = false;
    if(getRootParent()!=null)
        ((HeapCoord)getRootParent()).h.updateTN(this,tN);
    else if(getParent()!=null)
        ((HeapCoupledCoord)getParent()).h.updateTN(this,tN);   // System.out.println(superi.p);
  }
}

public  synchronized void  wrapDeltfunc(double t,MessageInterface x){
 if(x == null){
    System.out.println("ERROR RECEIVED NULL INPUT  " + myModel.toString());
    return;
  }
  if (x.isEmpty() && !equalTN(t)) {
    return;
  }
  else if((!x.isEmpty()) && equalTN(t)) {
    double e = t - tL;
    myModel.deltcon(e,x);
  }
  else if(tN <= t) {// relax to enable revival
    myModel.deltint();
  }
  else if(!x.isEmpty()) {
    double e = t - tL;
    myModel.deltext(e,x);
  }
  wrapDeltfuncHook2();
  tL = t;
  tN = tL + myModel.ta();

  tNChanged = true;
}


}
