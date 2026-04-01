
package pulseModels;

import simView.*;
import java.awt.*;
import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import statistics.rand;
import java.util.*;

class nonLinearGen extends linearGen{

public nonLinearGen(String nm,double coefficient,double quantum){
super(nm,coefficient,quantum);
}



public double posPart(double x){

double y = Math.abs(x);
double c = Math.abs(coefficient);
double f = 50;
//return c*y;//original linear
//
return c*(y+Math.pow(y,2.1));//power > 1
//return f*(1-Math.exp(-y*c/f));//f>=10c

}

public double rateFn(){
return signOf(coefficient)*signOf(input)*posPart(input);
}

}

public class linearGen extends varGen{
protected double input,coefficient;

public linearGen(String nm,double coefficient,double quantum){
super(nm,1,quantum);
addInport("setInput");
this.coefficient = coefficient;
input = 0;
}

public double rateFn(){
return coefficient*input;
}


public double signedTimeAdvance(){//override
//return quantum*inv(rateFn());
return currQuantum*inv(rateFn());
}


public void initialize(){
super.initialize();
rate = initRate;
input = 0;
holdIn("active ",Math.abs(signedTimeAdvance()));
}

double a =1;//.5;
double currQuantum = a*quantum;

public void quantumChange(double input){
double myState = Math.sqrt(100-input*input);

if (myState < 3) currQuantum = 10*a*quantum;//100*quantum;
else if (myState < 6)currQuantum = 5*a*quantum;// 2*a*quantum;
else currQuantum = a*quantum;//a*quantum; //for > 6 need small q

}

public void deltext(double e,message x){
if (somethingOnPort(x,"setInput")){
  input = getRealValueOnPort(x,"setInput");
 //
 quantumChange(input);
  rate = rateFn();
  holdIn("active",Math.abs(signedTimeAdvance()));
     //always use the new rate immediately
      }
  super.deltext(e,x); //to use the stop/start of parent

}

public message out(){
message m = outputRealOnPort(quantum*nextDirection(),"out");
m = outputRealOnPort(m,Math.min(.0025,sigma),"outSigma");
//return outputRealOnPort(m,quantum*Math.abs(nextDirection()),"outPos");
return outputRealOnPort(m,currQuantum*Math.abs(nextDirection()),"outPos");
}
}