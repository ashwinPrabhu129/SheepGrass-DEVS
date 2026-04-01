
package pulseExpFrames;


import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class instantReal extends ViewableAtomic{
protected double  inval,outval;


///////////////////////////////////////////////
public static class divide extends instantReal{
protected double  dividend;

public divide (String nm,double dividend){
super(nm);
this.dividend = dividend;
addInport("setDividend"); //result = dividend/divisor
addRealTestInput("setDividend",2);
}

public divide (String nm){
this(nm,1);
}

public double fn(double x){
return dividend/x;
}

public void deltext(double e,message x){
if (somethingOnPort(x,"setDividend"))
dividend = getRealValueOnPort(x,"setDividend");
outval = fn(inval);
super.deltext(e,x);
}

public String getTooltipText(){
   return
  super.getTooltipText()
      +"\n"+"dividend :"+ dividend;
  }
}
///////////////////////////////////////////////
public static class multiply extends instantReal{
protected double  multiplier;

public multiply (String nm,double multiplier){
super(nm);
this.multiplier = multiplier;
addInport("setMultiplier"); //result = multiplier*input
addRealTestInput("setMultiplier",2);
}

public multiply (String nm){
this(nm,0);
}

public void initialize(){
super.initialize();
inval = 0;
multiplier = 0;
}

public double fn(double x){
return multiplier*x;
}

public void deltext(double e,message x){
if (somethingOnPort(x,"setMultiplier"))
multiplier = getRealValueOnPort(x,"setMultiplier");
outval = fn(inval);
super.deltext(e,x);
}

public String getTooltipText(){
   return
  super.getTooltipText()
      +"\n"+"multiplier :"+ multiplier;
  }
}
////////////////////////////////////////
public instantReal(String nm){
super(nm);
addInport("in");
addOutport("out");
addRealTestInput("in",0.1);
addRealTestInput("in", 0.01);
addRealTestInput("in", 0.001);
addRealTestInput("in", 0);
addRealTestInput("in", 0.5);
addRealTestInput("in", 0.8);
addRealTestInput("in", 0.9);
addRealTestInput("in", 0.99);
addRealTestInput("in", 1);
addRealTestInput("in", 1.9);
addRealTestInput("in", 1.99);
addRealTestInput("in", 2);
addRealTestInput("in",Math.PI/2);
addRealTestInput("in",-2*Math.PI/3);
addRealTestInput("in",Math.PI/4);
}




public instantReal(){
this("instantReal");
}


public void initialize(){
super.initialize();
passivate();
}

public double fn(double x){ //override
//return Math.sin(x)/x;
//if (x = 3)return 1;
//return Math.sin(1/x);
//return Math.sin(x);
//return x*x - 7*x*x*x +5;
//return 1/((x-2)*(x-2));
//return Math.sin(1/x);
return (x-1)/(x*x -1);
}

public void deltext(double e,message x){
if (somethingOnPort(x,"in")){
inval = getRealValueOnPort(x,"in");
outval = fn(inval);
}
holdIn("output",0);
}

public void deltint(){
passivate();
}

public message out(){
return outputRealOnPort(outval,"out");
}


public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"inval :"+ inval
   +"\n"+"outval :"+ outval;
  }

public static void main(String args[]){
new  instantReal(" ");
}
 }





