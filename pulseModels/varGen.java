package pulseModels;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;

public class varGen extends  realDevs{
protected int direction; //1,0,-1
protected double initRate,rate,quantum = 1;




public double rateFn(){//override
return rate;
}

public double signedTimeAdvance(){//override
return quantum*inv(rateFn());
}

public int nextDirection(){
  double sta = signedTimeAdvance();
  return signOf(sta);
}



public varGen(String nm,double rate,double quantum){
super(nm);
initRate = rate;
this.quantum = quantum;
addInport("start");
addInport("stop");
addInport("setRate");
addOutport("out");
addOutport("outPos");
addOutport("outSigma");
addRealTestInput("setRate",5,0);
addRealTestInput("setRate",-5,0);
}

public varGen(String nm,double rate){
this(nm,rate,1);
}

public varGen(String nm){
this(nm,1);
}

public varGen(){
this("varGen");

}
/*
public void setSigma(){
  double newSigma = Math.abs(signedTimeAdvance());
  if (newSigma >= INFINITY)
  passivate();
  else if (newSigma < sigma)
  holdIn("active",newSigma);
  else
  holdIn("active",sigma);//only to show level
}
*/

public void initialize(){
super.initialize();
rate = initRate;
direction = nextDirection();
holdIn("active ",Math.abs(signedTimeAdvance()));
}

public void deltext(double e,message x){
Continue(e);
if (somethingOnPort(x,"setRate")){
  rate =  getRealValueOnPort(x,"setRate");
  double minSig = Math.min(sigma,Math.abs(signedTimeAdvance()));
    //this is needed where rate keeps decreasing
  holdIn("active",minSig);
}
else if (somethingOnPort(x,"start"))
 holdIn("active",Math.abs(signedTimeAdvance()));
else if (somethingOnPort(x,"stop"))
  passivate();
}

public void deltint(){
holdIn("active",Math.abs(signedTimeAdvance()));
}

public void deltcon(double e,message x)
{
 deltint();
 deltext(0,x);
}

public message out(){
message m = outputRealOnPort(quantum*nextDirection(),"out");
m = outputRealOnPort(m,Math.min(.0025,sigma),"outSigma");
return outputRealOnPort(m,quantum*Math.abs(nextDirection()),"outPos");
}

public void showState(){
   super.showState();
   System.out.println(
    "\n"+" rate: "+ rate);
  }

public String getTooltipText(){
   return
   super.getTooltipText()
    +"\n"+" rate: "+ rate;
  }

public static void main(String args[]){
//new  varGen(" ");
//entity e = new intEnt(1);
}
 }
