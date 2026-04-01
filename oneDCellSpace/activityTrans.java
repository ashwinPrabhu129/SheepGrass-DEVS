/*      Copyright 1999 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA2.6
 *  Date       : 04-15-00
 */

package oneDCellSpace;

import simView.*;

import java.lang.*;
import java.awt.*;
import genDevs.modeling.*;
import genDevs.plots.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;
import util.*;
import pulseModels.*;

public class activityTrans extends realDevs{
protected boolean[] dir;
protected double[] lastVal,currentVal,activity;
protected double min,max,avg,sum;
protected double displayPeriod,savedSigma;
protected int mostActive;

public void setFalse(){//up
for (int i = 0;i<lastVal.length;i++)
dir[i] = false;
}

public void computeMax(){
max = Double.NEGATIVE_INFINITY;
for (int i = 0;i<activity.length;i++)
if (activity[i] > max) {
max = activity[i];
mostActive = i;
}
}

public void computeMin(){
min = Double.POSITIVE_INFINITY;
for (int i = 0;i<activity.length;i++)
//if (activity[i] != 0 && activity[i] < min) min = activity[i];
if (activity[i] < min) min = activity[i];
}

public void computeAvg(){
sum = 0;
for (int i = 0;i<activity.length;i++)
 sum += activity[i];
avg = sum/activity.length;
}

public activityTrans(){
    this("activityTrans",100);
}

public activityTrans(String name,int numCells){
   super(name);
      dir = new boolean[numCells];
      lastVal = new double[numCells];
      currentVal = new double[numCells];
      activity = new double[numCells];
   addInport("in");
   addInport("inPair");
   addOutport("out");
   addOutport("outDraw");
   addTestInput("inPair",new Pair(new Integer(1),new Double(1.5)));
   addRealTestInput("in",1,0);
   addRealTestInput("in",2,0);
   addRealTestInput("in",3,0);
}


public void initialize(){
      passivate();
      setFalse();
      lastVal = new double[lastVal.length];
      currentVal = new double[lastVal.length];
      activity = new double[lastVal.length];
     super.initialize();
 }

public void  deltext(double e,message x){
    Continue(e);
 for (int i = 0; i < x.getLength(); i++)
  if (messageOnPort(x, "inPair", i)) {
    Pair p = (Pair)x.getValOnPort("inPair", i);
    Object key = p.getKey();
    Object value = p.getValue();

    Integer id = (Integer)key;//p.getKey();
    Double val = (Double)value;//p.getValue();
    int idi = id.intValue();

    if (dir[idi]){
     if (val.doubleValue()>currentVal[id.intValue()]){
      currentVal[id.intValue()] = val.doubleValue();
      }
      else {
      dir[idi] = false;
      activity[idi] += Math.abs(val.doubleValue()-lastVal[id.intValue()]);
      lastVal[id.intValue()] = val.doubleValue();
     currentVal[id.intValue()] = val.doubleValue();
         }
      }
     if (!dir[idi]){
     if (val.doubleValue()<currentVal[id.intValue()]){
      currentVal[id.intValue()] = val.doubleValue();
      }
      else {
      dir[idi] = true;
      activity[idi] += Math.abs(val.doubleValue()-lastVal[id.intValue()]);
      lastVal[id.intValue()] = val.doubleValue();
     currentVal[id.intValue()] = val.doubleValue();
         }
      }
    computeMax();
    computeMin();
    computeAvg();
      }

}



public void  deltint( ){
 passivate();
}


public message    out( ){
message m = new message();
return m;
}

public String toString(){
String st = "\n"; //String full = "\n";
for (int i = 0;i<activity.length;i++){
  if (activity[i] >= 1)
    st += i+": "+activity[i]+"\n";
 // full += i+": "+activity[i]+"\n";
  }
//  System.out.println(full);
return st;
}

public void showState(){
System.out.println("sum :"+ sum);
}



public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"avg :"+ avg
  +"\n"+"max :"+ max
  +"\n"+"mostActive :"+ mostActive
  +"\n"+"min :"+ min
  +"\n"+"sum :"+ sum
  +toString();

  }



}

