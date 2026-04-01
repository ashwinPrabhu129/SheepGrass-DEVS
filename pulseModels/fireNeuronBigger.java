
package pulseModels;

import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;



public class fireNeuronBigger extends fireOnceNeuron{
double size = 1;

public fireNeuronBigger(){
this("fireNeuronBigger",10);
}

public fireNeuronBigger(String nm,double fireDelay){
super(nm,fireDelay);
}

public void initialize(){
super.initialize();
size = 1;
}
public void deltext(double e,message x){
//Continue(e); //don't reduce sigma
if (phaseIs("receptive")&& somethingOnPort(x,"in"))
holdIn("fire",fireDelay);
else if (phaseIs("fire")&& somethingOnPort(x,"in"))
size += e/sigma;
holdIn("fire",fireDelay);
}
public message out(){
if (phaseIs("fire"))
return outputRealOnPort(size,"out");
else return new message();
}

public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"size :"+ size;

  }

}