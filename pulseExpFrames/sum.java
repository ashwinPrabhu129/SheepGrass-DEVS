package pulseExpFrames;


import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;

import java.util.*;

public class sum extends  ViewableAtomic{
protected double state,initState,lossPercent;


public sum(String nm,double state,double lossPercent){
super(nm);
initState = state;
this.lossPercent = lossPercent;
addInport("in");
addInport("reset");
addInport("switchWLoss");
addOutport("out");
addOutport("outAccum");
addRealTestInput("in",1);
addRealTestInput("in",-1);
addPortTestInput("reset");
}

public sum(String nm,double state){
this(nm,state,0);
}


public sum(String nm){
this(nm,0,0);
}

public sum(){
this("sum");
}


public void initialize(){
super.initialize();
state = initState;
holdIn("active "+state,0);
}

public void deltext(double e,message x){
Continue(e);
if (somethingOnPort(x,"in")){
state += sumValuesOnPort(x,"in");
state = state*(1-lossPercent/100);
   holdIn("active "+state,0);
   }
else if (somethingOnPort(x,"reset")){
 // state = 0;
 //passivateIn("passive "+state);
  holdIn("outAccum",0);
 }
else if (somethingOnPort(x,"switchWLoss")){
  double loss = getRealValueOnPort(x,"switchWLoss");
  state = state*(1-loss);
   state =-state;
   holdIn("active "+state,0);
   }

}


public void deltint(){
  if (phaseIs("outAccum"))state = 0;
  passivateIn("passive "+state);
}

public void deltcon(double e,message x)
{
 deltext(e,x);
}

public message out(){
message m = new message();
if (phaseIs("outAccum"))
m = outputRealOnPort(m,state,"outAccum");
return outputRealOnPort(m,state,"out");
}

public void showState(){
   super.showState();
   System.out.println(
  "\n"+ "state :"+ state
  );
  }

public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"state :"+ state;
  }

public static void main(String args[]){
System.out.println(Math.exp(.001));
System.out.println(Math.log(50)/Math.log(10));
System.out.println(Math.pow(10,1.6989));
//new  sum(" ");
//entity e = new intEnt(1);
}
 }





