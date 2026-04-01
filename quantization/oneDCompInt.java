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


class sv {
public double s,v;

public sv(double s, double v){
this.s = s;
this.v = v;
}
public String toString(){
    return util.doubleFormat.niceDouble(s)
         + ","+util.doubleFormat.niceDouble(v);
  }
}

public class oneDCompInt extends realDevs{

protected double  input,quantum,quantumLeft;
protected double clock;
public double eps = 0;//10./100; //choice could be max input / 100;

protected sv state,initialState;

public oneDCompInt(String  name, double quantum, sv initialState){
super(name);
this.quantum = quantum;
this.initialState = initialState;
addOutport("outNeg");
addOutport("outV");
addOutport("outNegV");
}

public oneDCompInt(String  name, double quantum, double s, double v,double eps){
this ( name, quantum, new sv(s,v));
this.eps = eps;
}

public oneDCompInt(){
  this("oneDCompInt",.1,new sv(100,0));
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
state = initialState;
quantumLeft = quantum;
holdIn(state.toString(),0);
input = 0;
clock = 0;
}

  public double timeAdvance(){
    double tav,tax;
  if ( equivInputZero(input) != 0){
  tav = Math.abs(quantumLeft/input);//time adv is based on accel
  }
  else tav = INFINITY;
  if (equivInputZero(state.v) != 0){
  tax = Math.abs(10*quantumLeft / state.v); //time adv is based on vel
   }
  else tax = INFINITY;
   return  Math.min(tav,tax);
  }


public sv nextState(double e){
if (e <INFINITY){
double nexts = state.s +e*state.v;
double nextv = state.v + e*input;
return new sv(nexts,nextv);
}
else return state;
}


public void deltint(){
clock+=sigma;
state = nextState(sigma);
quantumLeft = quantum;
holdIn(state.toString(),timeAdvance());
}

public void  deltext(double e,message   x)
{
clock+=e;
state = nextState(e);
quantumLeft = quantumLeft*(1 - e/sigma);
input = equivInputZero(sumValuesOnPort(x,"in"));
holdIn(state.toString(),timeAdvance());
if (somethingOnPort(x,"stop")) passivate();
}

public void  deltcon(double e,message   x)
{
clock+=sigma;
state = nextState(e);
quantumLeft = quantum;
input = equivInputZero(sumValuesOnPort(x,"in"));
holdIn(state.toString(),timeAdvance());
if (somethingOnPort(x,"stop")) passivate();
}


public message    out( )
{
message   m = new message();
if (clock <= 0){
m.add(makeContent("out",  new doubleEnt(state.s)));
m.add(makeContent("outNeg",  new doubleEnt(-state.s)));
m.add(makeContent("outV",  new doubleEnt(state.v)));
m.add(makeContent("outVNeg",  new doubleEnt(-state.v)));
}
else {
sv next = nextState(sigma);
m.add(makeContent("out",  new doubleEnt(next.s)));
m.add(makeContent("outNeg",  new doubleEnt(-next.s)));
m.add(makeContent("outV",  new doubleEnt(next.v)));
m.add(makeContent("outVNeg",  new doubleEnt(-next.v)));
}
return m;
}


public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+"quantum :"+ quantum
  +"\n"+"state.s :"+ state.s
    +"\n"+"state.v :"+ state.v
    +"\n"+"input :"+ input;
}
}
