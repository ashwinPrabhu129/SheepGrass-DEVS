package pulseExpFrames;


import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class thresholdTesterRefract extends thresholdTester{
protected double refractPeriod;

public thresholdTesterRefract(double threshold,double refractPeriod){
super(threshold);
this.refractPeriod = refractPeriod;
addRealTestInput("in", 1,5);
}

public thresholdTesterRefract(){
this(100,10);
}



public void deltext(double e,message x){
Continue(e);
if (somethingOnPort(x,"setThreshold"))
threshold = getRealValueOnPort(x,"setThreshold");

if (phaseIs("passive")){
if (somethingOnPort(x,"in")){
inval = getRealValueOnPort(x,"in");
outval = fn(inval);
if (outval == 0)
holdIn("output",0);  //only output a 0 if threshold passed
}
}
//System.out.println(phase);
}

public void deltint(){
if (phaseIs("output"))
holdIn("refract",refractPeriod);
else passivate();
}

}
