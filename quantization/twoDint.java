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

public class twoDint extends ViewableAtomic{

protected vect2DEnt lastInput,input,state,initialState,nextState;
protected double quantum, remainingQuant, clock;
protected Color myCol = Color.black;

protected    vect2DEnt deriv2;  //for twoDint2ndOrd
protected double elapsed;


public twoDint(String  name, double Quantum, vect2DEnt state, Color myCol){
this(name,Quantum,state);
this.myCol = myCol;

}

public twoDint(String  name, double Quantum, vect2DEnt state){
super(name);

addInport("in");
addInport("stop");
addOutport("out");
addOutport("outPos");
addOutport("outSigma");
addOutport("outDraw");
addOutport("outOldNew");

quantum = Quantum;
initialState = state;
}

public twoDint(){
this("twoDint",1,new vect2DEnt(0,0));
double  root2 = 1/Math.sqrt(2);
addTestInput("in",new vect2DEnt(root2,root2),0);
addTestInput("in",new vect2DEnt(root2,root2),.5);
addTestInput("in",new vect2DEnt(-root2,root2),0);
addTestInput("in",new vect2DEnt(root2,-root2),0);
addTestInput("in",new vect2DEnt(-root2,-root2),0);
addTestInput("in",new vect2DEnt(-root2,-root2),.5);
}

public void initialize(){
     input = new  vect2DEnt(0,0);
     lastInput = input;
     state = initialState;
     remainingQuant = quantum;
     sigma = 0;
     phase = state.toString();
     super.initialize();
     nextState = state.copy();
     clock = 0.0;

 }



public void setInp(vect2DEnt Input){
    lastInput = input;
    input = Input;
}

public void timeAdvance(double difference){
 if (input.norm() == 0)sigma = INFINITY;
   else sigma = Math.abs(difference/input.norm());
}

public void update(double e){

   // state.addSelf(lastInput.scalarMult(e)); //this changes initialState, why???
   if (e <= 0)return; //
    vect2DEnt v = lastInput.scalarMult(e);
    state = state.add(v);//new vect2DEnt(state.x+v.x,state.y+v.y);
    remainingQuant -= v.norm();
// if (remainingQuant < 0)
    //  System.out.println(getName()+ " ERROR: remainingQuant can't be negative");
}


public void computeIntNextstate(){
      timeAdvance(quantum);
      vect2DEnt move = input.scalarMult(sigma);
      if (move.norm()>remainingQuant)
       move = move.scalarMult(remainingQuant/move.norm());
      nextState = state.add(move);
      remainingQuant = quantum;
 //     if (input.norm() ==  0)
   //      System.out.println(getName()+ "ERROR: input can't be zero");

}

public void computeExtNextstate(){
timeAdvance(remainingQuant);
nextState = state.add(input.scalarMult(sigma));
}




public void deltcon(double e,message x)
{

 deltint();

 deltext(0,x);
}

public void  deltext(double e,message   x)
{

for (int i=0; i< x.getLength();i++)
  if (messageOnPort(x,"stop",i)){
  passivate();
  break;
  }
  Continue(e);
  clock = clock + e;
  vect2DEnt sum = new vect2DEnt(0,0);

 for (int i=0; i< x.getLength();i++)
  if (messageOnPort(x,"in",i)){
       entity ent = x.getValOnPort("in",i);
      // sum.addSelf((vect2DEnt)ent);
         sum = sum.add((vect2DEnt)ent);
    }

     setInp(sum);
     update(e);
     computeExtNextstate();
     phase = state.toString();

}

public void  deltint( )
{
clock = clock + sigma;
state = nextState.copy();
computeIntNextstate();
phase = state.toString();
}

public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+" quantum: "+ quantum
   +"\n"+" remainingQuant: "+ remainingQuant
  + "\n"+ "initialState :"+ initialState
     +"\n"+"input :"+ input
   +"\n"+"lastInput :"+ lastInput;
  }


public message    out( )
{
message   m = new message();
if (clock <= 0 && sigma == 0){
m.add(makeContent("out",  state));
m.add(makeContent("outDraw",new DrawCellEntity(state.x, state.y,
              myCol, Color.lightGray)));
              }
else{
m.add(makeContent("out",  nextState));
timeAdvance(quantum);
m.add(makeContent("outSigma",  new doubleEnt(sigma)));
vect2DEnt diff = nextState.subtract(state);
//System.out.println();diff.toString();
m.add(makeContent("outPos",new doubleEnt(diff.norm())));
vect2DEnt os = new vect2DEnt(Math.ceil(state.x),Math.ceil(state.y));
vect2DEnt ns = new vect2DEnt(Math.ceil(nextState.x),Math.ceil(nextState.y));
if (!os.equals(ns))m.add(makeContent("outOldNew",new Pair(os,ns)));
m.add(makeContent("outDraw",new DrawCellEntity(nextState.x, state.y,
              myCol, Color.lightGray)));

   }
return m;
}



}

