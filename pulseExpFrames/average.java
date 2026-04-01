package pulseExpFrames;


import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;

import java.util.*;

public class average extends  ViewableAtomic{
protected double sum,avg;
protected int number;


public average(String nm){
super(nm);
addInport("in");
addInport("reset");
addOutport("out");
addRealTestInput("in",1);
addRealTestInput("in",1);
addRealTestInput("in",-1);
addPortTestInput("reset");
}


public average(){
this("average");
}


public void initialize(){
super.initialize();
sum = avg = 0;
number = 0;
passivateIn("avg: "+ avg);
}

public void deltext(double e,message x){
Continue(e);
if (somethingOnPort(x,"in")){
sum += sumValuesOnPort(x,"in");
number += x.size();
avg = sum/number;
   }
else if (somethingOnPort(x,"reset")){
sum = avg = 0;
number = 0;
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
    +"\n"+"number :"+ number
    +"\n"+"sum :"+ sum
   +"\n"+"avg :"+ avg;
  }

public static void main(String args[]){
}

}





