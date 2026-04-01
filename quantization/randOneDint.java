package quantization;



import java.lang.*;
import java.awt.*;
import java.util.*;


import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import simView.*;
import util.*;
import genDevs.plots.*;
import statistics.*;

public class randOneDint extends ViewableAtomic{

protected rand r;
protected int numTransitions;
// ts/ps= q/deriv  so ps = deriv*(ts/q)
//1 = maxder*(ts/q) so ts/q = 1/maxder
//so ps = deriv/maxder

protected double myPos,initPos,maxder,quantum,ts,input,ps;

public randOneDint(String  name, double quantum, double initPos, double maxder){
super(name);
this.quantum = quantum;
this.initPos = initPos;
this.maxder = maxder;
ts = quantum/maxder;
addInport("in");
addInport("stop");
addOutport("out");
addOutport("outPos");
addOutport("outSigma");
addOutport("outDraw");
addOutport("outOldNew");
addOutport("outNeg");
}

public randOneDint(String  name, double quantum, double initPos){
this(name,quantum,initPos,1);
}

public randOneDint(){
this("randOneDint",1,0);
}


public void initialize(){
r = new rand(name.hashCode());
myPos = initPos;
numTransitions  = 0;
holdIn("start",0);
}



public void computePandT(){

   if (Math.abs(input)<=0){
   sigma = INFINITY;
   ps = 0;
   }
   else{
   //sigma = ts;
   sigma = r.expon(ts);
   ps = Math.min(1,Math.abs(input)/maxder);
   }
    phase = ""+myPos;
}

public void  deltext(double e,message   x)
{

if (somethingOnPort(x,"in")){
    input = sumValuesOnPort(x,"in");
    computePandT();
    }
}


public void  deltint( ){

if (phaseIs("success"))
computePandT();
else
if (r.uniform(1)<ps){
myPos += input >0?quantum:-quantum;
numTransitions++;
holdIn("success",0);
   }
}

public void deltcon(double e,message x){
    deltext(e,x);
}

public message    out( )
{
message   m = new message();
if (phaseIs("start")|| phaseIs("success")){
m.add(makeContent("out",  new doubleEnt(myPos)));
m.add(makeContent("outPos", new doubleEnt(myPos)));
m.add(makeContent("outNeg",  new doubleEnt(-myPos)));
}
return m;
}

public String getTooltipText(){
   return
   super.getTooltipText()
   +"\n"+" maxder: "+ maxder
      +"\n"+" input: "+ input
    +"\n"+" ps: "+ ps;
  }
}
