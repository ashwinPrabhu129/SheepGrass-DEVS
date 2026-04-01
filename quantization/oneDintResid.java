package quantization;



import java.lang.*;
import java.awt.*;
import java.util.*;


import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import simView.*;
import util.*;
import genDevs.plots.*;
import pulseModels.*;
import statistics.*;


public class oneDintResid extends realDevs{

protected double state, input,quantum,quantumLeft,initialState;//q,x,D
protected double clock;
public double eps = 0;//10./100; //choice could be max input / 100;


//protected rand r;


public oneDintResid(String  name, double quantum, double initialState){
super(name);
this.quantum = quantum;
this.initialState = initialState;
addOutport("outNeg");
}

public oneDintResid(){
this("oneDintResid",1,0);
}

public static int positive(double x){
     if (x > 0) return 1;
    else return  0;
}

public  double equivInputZero(double x){
if (Math.abs(x) <= eps)
 return 0;
else return x;
}

public void initialize(){
super.initialize();
//r = new rand(name.hashCode());
state = initialState;
quantumLeft = quantum;
holdIn(""+state,0);
input = 0;
clock = 0;
}


public double timeAdvance(){
if (input != 0){
//
return Math.abs(quantumLeft/input);
}
else return  INFINITY;
}

public double nextState(){
return state + signOf(input)*quantumLeft;
}


public void deltint(){
clock+=sigma;
state = nextState();
quantumLeft = quantum;
holdIn(""+state,timeAdvance());
}

public void  deltext(double e,message   x)
{
clock+=e;
state +=input*e;// signOf(input)*quantumLeft*e/sigma;
quantumLeft = quantumLeft -Math.abs(e*input); // quantumLeft*(1 - e/sigma);
input = equivInputZero(sumValuesOnPort(x,"in"));
holdIn(""+state,timeAdvance());
}

public void  deltcon(double e,message   x)
{
deltint();
input = equivInputZero(sumValuesOnPort(x,"in"));
holdIn(""+state,timeAdvance());
}


public message    out( )
{
message   m = new message();
if (clock <= 0){
m.add(makeContent("out",  new doubleEnt(state)));
m.add(makeContent("outNeg",  new doubleEnt(-state)));
}
else {
m.add(makeContent("out",  new doubleEnt(nextState())));
m.add(makeContent("outNeg",  new doubleEnt(-nextState())));
}
return m;
}


public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"quantum :"+ quantum
  +"\n"+"state :"+ state
   +"\n"+"input :"+ input;
}
}
