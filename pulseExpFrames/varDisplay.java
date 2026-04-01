package pulseExpFrames;

import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;

import java.util.*;

public class varDisplay extends  ViewableAtomic{
protected double state;


public varDisplay(String nm){
super(nm);
addInport("in");
addRealTestInput("in",1);
addRealTestInput("in",-1);
}


public varDisplay(){
this("varDisplay");
}


public void initialize(){
super.initialize();
state = 0;
passivateIn(name+": "+state);
}

public void deltext(double e,message x){
if (somethingOnPort(x,"in")){
state  =  getRealValueOnPort(x,"in");
passivateIn(name+": "+state);
 }
}


public void deltint(){
passivateIn(name+": "+state);
}



public void showState(){
   super.showState();
   System.out.println(
  "\n"+ "state :"+ state
  );
  }

public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"state :"+ state;
  }

public static void main(String args[]){
System.out.println(Math.exp(.001));
System.out.println(Math.log(50)/Math.log(10));
System.out.println(Math.pow(10,1.6989));
//new  varDisplay(" ");
//entity e = new intEnt(1);
}
 }





