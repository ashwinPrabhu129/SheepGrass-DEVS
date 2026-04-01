package pulseExpFrames;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class pulseGenr extends ViewableAtomic{
protected double interPulseTime = 50;
protected double size = 1;

public pulseGenr(){
this("pulseGenr");
}

public pulseGenr(String nm){
this(nm,10,1);
}

public pulseGenr(String nm,double interPulseTime){
this(nm,interPulseTime,1);
}

public pulseGenr(String nm,double interPulseTime,double size){
super(nm);
addInport("start");
addInport("stop");
addInport("setSize");
addInport("setInterPulseTime");
addOutport("out");
this.interPulseTime = interPulseTime;
this.size = size;
addPortTestInput("start");
addPortTestInput("stop");
addRealTestInput("setSize",1,0);
addRealTestInput("setSize",-1,0);
addRealTestInput("setInterPulseTime",20,0);
addRealTestInput("setInterPulseTime",10,0);
addRealTestInput("setInterPulseTime",0,0);
}


public void initialize(){
super.initialize();
//holdIn("active",interPulseTime);
    passivate();
}


public void deltext(double e,message x){
Continue(e);
if (somethingOnPort(x,"setInterPulseTime")){
  interPulseTime =  getRealValueOnPort(x,"setInterPulseTime");
  //if (interPulseTime > 0)
  holdIn("active",interPulseTime);
 // else passivate();
}
else if (somethingOnPort(x,"setSize")){
  size =  getRealValueOnPort(x,"setSize");
  holdIn("active",interPulseTime);
}
else if (somethingOnPort(x,"start"))
  holdIn("active",interPulseTime);
else if (somethingOnPort(x,"stop"))
  passivate();
}

public void   deltint(){
holdIn("active",interPulseTime);
}

public message out(){
return outputRealOnPort(size,"out");
}


public String getTooltipText(){
   return
   super.getTooltipText()
    +"\n"+" size: "+ size
    +"\n"+" interPulseTime: "+ interPulseTime;
  }

}
