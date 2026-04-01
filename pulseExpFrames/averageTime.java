package pulseExpFrames;


import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;

import java.util.*;

//time average of piecewise constant input

public class averageTime extends ViewableAtomic{
protected double sum,avg,clock,lastInput,bias,scale;



public averageTime(String nm,double bias,double scale){
super(nm);
this.bias = bias;
this.scale = scale;
addInport("in");
addInport("reset");
addOutport("out");
addRealTestInput("in",1);
addRealTestInput("in",1);
addRealTestInput("in",-1);
addPortTestInput("reset");
}


public averageTime(){
this("averageTime",0,1);
}


public void initialize(){
super.initialize();
sum = avg = lastInput =clock = 0;
passivateIn("avg: "+ avg);
}

public void deltext(double e,message x){
Continue(e);
clock += e;
sum += lastInput*e;
avg = clock>0?scale*((sum/clock)-bias):0;
if (somethingOnPort(x,"in"))
lastInput = sumValuesOnPort(x,"in");

else if (somethingOnPort(x,"reset")){
sum = avg = lastInput =clock = 0;
passivateIn("avg: "+ avg);
}
holdIn("avg: "+avg,0);
}


public void deltint(){
passivateIn("avg: "+ avg);
}

public void deltcon(double e,message x)
{
 deltext(e,x);
}

public message out(){
return outputRealOnPort(avg,"out");
}


public String getTooltipText(){
   return
  super.getTooltipText()
    +"\n"+"clock :"+ clock
    +"\n"+"sum :"+ sum
   +"\n"+"avg :"+ avg;
  }

public static void main(String args[]){
}

}





