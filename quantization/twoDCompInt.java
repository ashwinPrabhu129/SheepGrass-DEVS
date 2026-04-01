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




class svVect {
public vect2DEnt s,v;

public svVect(vect2DEnt s, vect2DEnt v){
this.s = s;
this.v = v;
}
public String toString(){
    return s.toString()+" "+v.toString();
  }
}

//have s.x and s.y =  components of displacement in 2 dim
//v.x and v.y = components of velocity in 2 dim

public class twoDCompInt extends ViewableAtomic{

protected vect2DEnt  input;
double quantum,quantumLeft;
protected double clock;
public double eps = 0;//10./100; //choice could be max input / 100;
protected Color myCol = Color.black;

protected svVect state,initialState;

public twoDCompInt(String  name, double quantum, svVect initialState){
super(name);
this.quantum = quantum;
this.initialState = initialState;
addOutport("outV");
addOutport("outSpeed");
}

public twoDCompInt(){
this("twoDCompInt",1,new svVect(new vect2DEnt(0,0),new vect2DEnt(0,0)));
}


  public static int signOf(double x){
      if (x == 0) return 0;
      else if (x > 0) return 1;
      else return -1;
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
input = new vect2DEnt(0,0);
clock = 0;
}

  public double timeAdvance(double input,double vel){
    double tav,tax;
  if ( equivInputZero(input) != 0){
  tav = Math.abs(quantumLeft/input);//time adv is based on accel
  }
  else tav = INFINITY;
  if (equivInputZero(vel) != 0){
  tax = Math.abs(10*quantumLeft / vel); //time adv is based on vel
   }
  else tax = INFINITY;
  return  Math.min(tav,tax);
  }


public double timeAdvance(){//smallest ta in x and y directions
return Math.min(timeAdvance(input.x,state.v.x),timeAdvance(input.y,state.v.y));
}


public svVect nextState(double e){
if (timeAdvance()<INFINITY){
vect2DEnt nexts = state.s.add(state.v.scalarMult(e));
vect2DEnt nextv = state.v.add(input.scalarMult(e));
return new svVect(nexts,nextv);
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
input = new vect2DEnt(0,0);
 for (int i=0; i< x.getLength();i++)
  if (messageOnPort(x,"in",i)){
       entity ent = (vect2DEnt)x.getValOnPort("in",i);
         input = input.add((vect2DEnt)ent);
    }
holdIn(state.toString(),timeAdvance());
}

public void  deltcon(double e,message   x)
{
  clock+=sigma;
  state = nextState(sigma);
  quantumLeft = quantum;

  input = new vect2DEnt(0,0);
   for (int i=0; i< x.getLength();i++)
    if (messageOnPort(x,"in",i)){
         entity ent = (vect2DEnt)x.getValOnPort("in",i);
           input = input.add((vect2DEnt)ent);
      }
  holdIn(state.toString(),timeAdvance());
}


public message    out( )
{
message   m = new message();
if (clock <= 0){
m.add(makeContent("out",  state.s));
m.add(makeContent("outDraw",new DrawCellEntity(state.s.x, state.s.y,
              myCol, Color.lightGray)));
m.add(makeContent("outV",  state.v));
m.add(makeContent("outSpeed",
        new doubleEnt(Math.sqrt(state.v.x*state.v.x+state.v.y*state.v.y))));
}
else {
m.add(makeContent("out",  nextState(sigma).s));
svVect next = nextState(sigma);
m.add(makeContent("outDraw",new DrawCellEntity(next.s.x, next.s.y,
              myCol, Color.lightGray)));
m.add(makeContent("outV",  next.v));
m.add(makeContent("outSpeed",
        new doubleEnt(Math.sqrt(next.v.x*next.v.x+next.v.y*next.v.y))));
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
