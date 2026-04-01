package pulseModels;

import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;

public class fireOnceNeuron extends realDevs{
protected double fireDelay;

public fireOnceNeuron(String nm,double fireDelay){
super(nm);
this.fireDelay = fireDelay;
addInport("in");
addOutport("out");
addPortTestInput("in");
addPortTestInput("in",1);
addPortTestInput("in",9);
addPortTestInput("in",10);
}

public fireOnceNeuron(){
this("fireOnceNeuron",10);
}


public void initialize(){
super.initialize();
passivateIn("receptive");
}


public void deltext(double e,message x){
Continue(e);
if (phaseIs("receptive")&& somethingOnPort(x,"in"))
holdIn("fire",fireDelay);
}

public void deltint(){
if (phaseIs("fire"))
passivateIn("refract");
}

  public void deltcon(double e,message x){
      deltint();
   //   deltext(0,x); //ignore pulse at end of fire phase

}

public message out(){
if (phaseIs("fire"))
return outputNameOnPort("pulse","out");
else return new message();
}


public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"fireDelay :"+ fireDelay;

  }

public static void main(String args[]){
new  fireOnceNeuron();
}
 }





