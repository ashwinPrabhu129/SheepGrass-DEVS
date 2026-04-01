package pulseExpFrames;


import simView.*;

import java.lang.*;
import java.util.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import statistics.*;
import util.*;
////////////////
public class statCollector extends  ViewableAtomic{

public static class Reset extends  statCollector {


public Reset(String nm,int NumClasses){
super(nm,NumClasses);
addInport("inReset");
}

public Reset(String nm){
this(nm,10); //number of classes for distribution
}

public Reset(){
this("statCollectorReset");
}



public void deltext(double e,message x){
Continue(e);
  stat = new statistics();
for (int i=0; i< x.getLength();i++){
  if (messageOnPort(x,"inReset",i)){
   doubleEnt dent = (doubleEnt)x.getValOnPort("inReset",i);
    stat.add(dent.getv());
    holdIn("sendOut",0);
   }
   }
}
}

////////////


protected statistics stat;

public statCollector(String nm,int NumClasses){
super(nm);
statistics.numClasses = NumClasses;
addInport("in");
addInport("reset");
addInport("report");
addOutport("outAvg");
addOutport("outStd");
addOutport("outDist");

addRealTestInput("in",1);
addRealTestInput("in",0);
addRealTestInput("in",-1);
addPortTestInput("reset");
addPortTestInput("report");
}

public statCollector(String nm){
this(nm,10); //number of classes for distribution
}

public statCollector(){
this("statCollector");
}


public void initialize(){
super.initialize();
stat = new statistics();
passivate();
}

public void deltext(double e,message x){
Continue(e);
for (int i=0; i< x.getLength();i++)
  if (messageOnPort(x,"in",i)){
   doubleEnt dent = (doubleEnt)x.getValOnPort("in",i);
    stat.add(dent.getv());
    holdIn("sendOut",0);
   }
if (somethingOnPort(x,"reset"))
stat = new statistics();
else
passivate();
}


public void deltint(){
passivate();
}

public void deltcon(double e,message x)
{
 deltext(e,x);
}

public message out(){
stat.toArray();
message m = outputRealOnPort(stat.average(),"outAvg");
m = outputRealOnPort(m,stat.std(),"outStd");
return outputNameOnPort(m,stat.distribution(),"outDist");
}


public String getTooltipText(){
stat.toArray();
   return
  super.getTooltipText()
    +"\n"+"average :"+ doubleFormat.niceDouble(stat.average())
    +"\n"+"std :"+ doubleFormat.niceDouble(stat.std())
   +"\n"+"distribution :"+ stat.distribution();
  }

public static void main(String args[]){
}

}





