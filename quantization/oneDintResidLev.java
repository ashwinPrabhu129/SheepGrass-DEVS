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


public class oneDintResidLev extends oneDint{

protected double quantumLeft;
protected rand r;

public oneDintResidLev(String  name, double quantum, double initialState){
super(name,quantum,initialState);
}

public oneDintResidLev(){
this("oneDintResidLev",1,0);
}



public void initialize(){
super.initialize();
quantumLeft = quantum;
r = new rand(name.hashCode());
}


public double timeAdvance(){
if (input != 0){
//return Math.abs(quantumLeft/input);
//return   r.expon(quantumLeft/Math.abs(input));
//double num = 10.0/Math.abs(input);
//return r.normal(Math.abs(quantumLeft/input),0.000001*Math.sqrt(num));
//
return r.normal(Math.abs(quantumLeft/input),0.0001);
}
else return  INFINITY;
}



public void deltint(){
super.deltint();
quantumLeft = quantum;
}

public void  deltext(double e,message   x)
{
clock+=e;
state += signOf(input)*quantumLeft*e/sigma;
if (state <level*quantum) //gone below current level
    level = level - 1;

double lastInput = input;
input = equivInputZero(sumValuesOnPort(x,"in"));

if (signOf(input)== signOf(lastInput))
 quantumLeft = quantumLeft*(1 - e/sigma);
else if (input > 0)
  quantumLeft = (level + 1)*quantum - state;
else
  quantumLeft = state - level*quantum;

holdIn(""+state,timeAdvance());
}



}
