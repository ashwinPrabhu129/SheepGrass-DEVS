
package pulseExpFrames;



import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class differencer extends instantReal{
protected double lastVal,initLastVal;

public double fn(double x){
return inval - lastVal;
}

public differencer(String nm,double lastVal){
super(nm);
initLastVal = lastVal;
addRealTestInput("in",0,0);
addRealTestInput("in",1,5);
addRealTestInput("in",2,10);
}

public differencer(String nm){
this(nm,0);
}

public differencer(){
this("differencer");
}

public void initialize(){
lastVal = initLastVal;
super.initialize();
}

public void deltext(double e,message x){
super.deltext(e,x);
lastVal = inval;
}
/*

public void deltext(double e,message x){
Continue(e);
if (somethingOnPort(x,"in")){
inval = getRealValueOnPort(x,"in");
outval = fn(inval);

double newVal = getRealValueOnPort(x,"in");
outval = newVal - lastVal;
lastVal = newVal;
holdIn("output",0);
}
}
*/

public String getTooltipText(){
   return
   super.getTooltipText()
    +"\n"+" lastVal: "+ lastVal;
  }
}
