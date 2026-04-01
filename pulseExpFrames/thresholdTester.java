package pulseExpFrames;



import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class thresholdTester extends instantReal{

public static class thresholdTesterNot extends thresholdTester{
public thresholdTesterNot(String nm,double threshold){
super(nm,threshold);
}

public double fn(double x){
if (x < threshold)
return 0;
else return 1;
}
}

protected double threshold;

public thresholdTester(String nm,double threshold){
super(nm);
this.threshold = threshold;
addInport("setThreshold");
addRealTestInput("setThreshold",0);
addRealTestInput("setThreshold",1);
}

public thresholdTester(double threshold){
this("thresholdTester",threshold);
}

public thresholdTester(){
this(100);
}

public double fn(double x){
if (x >= threshold)
return 0;
else return 1;
}


public void deltext(double e,message x){
if (somethingOnPort(x,"setThreshold"))
threshold = getRealValueOnPort(x,"setThreshold");

if (somethingOnPort(x,"in")){
inval = getRealValueOnPort(x,"in");
outval = fn(inval);
if (outval == 0)
holdIn("output",0);  //only output a 0 if threshold passed
}
else passivate();
}


public String getTooltipText(){
   return
   super.getTooltipText()
    +"\n"+" threshold: "+ threshold;
  }

}
