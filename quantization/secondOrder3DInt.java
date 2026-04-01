/*
 * mass.java
 *
 * Created on February 2, 2003, 11:05 AM
 */

package quantization;

import java.lang.*;
import java.awt.*;
import java.util.*;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;
import util.*;
import genDevs.plots.*;

public class secondOrder3DInt extends ViewableAtomic{

protected vect3DEnt lastaccel,accel,initialvelocity,velocity,lastvelocity,state,initialState,initialaccel;
protected double quantum, remainingQuant, clock;
protected vect3DEnt nextState, nextvelocity;

public secondOrder3DInt(String name, double Quantum, vect3DEnt velocity, vect3DEnt state){
super(name);

addInport("in");
//addInport("inV");
addInport("stop");

addOutport("out");
addOutport("out1");

addOutport("outY");
addOutport("out1Y");

addOutport("outDraw2D");
addTestInput("in",new vect3DEnt(0,0,0),0);
addTestInput("in",new vect3DEnt(0,1,0),0);
addTestInput("in",new vect3DEnt(0,-9.8,0),0);

quantum = Quantum;
initialState = state;
initialvelocity = velocity;
}
public secondOrder3DInt(){
this("SecOrder3DIntegrator",1,new vect3DEnt(0,0,0),new vect3DEnt(0,0,0));
double  root2 = 1/Math.sqrt(2);
addTestInput("in",new vect3DEnt(0,0,0),0);
addTestInput("in",new vect3DEnt(root2,root2,0),0);
addTestInput("in",new vect3DEnt(root2,root2,0),.5);
addTestInput("in",new vect3DEnt(-root2,root2,0),0);
addTestInput("in",new vect3DEnt(root2,-root2,0),0);
addTestInput("in",new vect3DEnt(-root2,-root2,0),0);
addTestInput("in",new vect3DEnt(-root2,-root2,0),.5);
addTestInput("in",new vect3DEnt(0,-9.8,0),0);
}

public void initialize(){
     accel = new vect3DEnt(0,0,0);// initialaccel;
     lastaccel = accel;
     state = initialState;
     velocity = initialvelocity;
     lastvelocity = velocity;
     remainingQuant = quantum;
     sigma = 0;
     phase = state.toString();
     super.initialize();
     nextState = state;
     nextvelocity =  velocity;
     clock = 0.0;
 }




public static int signOf(double x){
    if (x == 0) return 0;
    else if (x > 0) return 1;
    else return -1;
}

public void setInp(vect3DEnt accel){
    lastaccel = accel;
    this.accel = accel;
}

public void timeAdvance(double difference){


    if ((accel.norm()==0) & (velocity.norm() == 0)){
        sigma = INFINITY;

    }
   else
   if ((accel.norm()==0) & !(velocity.norm() == 0)){
         sigma = Math.abs(difference/velocity.norm());

   } else
         {
         double delta=Math.sqrt(Math.pow(velocity.norm(),2)+2*difference*accel.norm());
         if (delta<0){
             delta=Math.sqrt(Math.pow(velocity.norm(),2)+2*(-quantum+difference)*accel.norm());
         }
         double t1=(-velocity.norm()+delta)/accel.norm();
         double t2=(-velocity.norm()-delta)/accel.norm();
         if (t1>0 & t2>0) { sigma=Math.min(t1,t2); }
                    else  { sigma=Math.max(t1,t2); //to choose the positive one
                    }
         if (sigma<0) { System.out.println("Error: Sigma cant be negative, set to zero");
                        sigma=0;
                      }

   }
}
public void update(double e){
    if (sigma != INFINITY ) {
    vect3DEnt v = lastaccel.scalarMult(e);
    v=lastvelocity.add(v).scalarMult(e);
    state = state.add(v);

       remainingQuant -= (lastaccel.scalarMult(e*e/2).add(lastvelocity.scalarMult(e))).norm();

    if (remainingQuant < 0)
              System.out.println(getName()+ " ERROR: remainingQuant can't be negative");
    }
}


public void computeIntNextstate(){

      timeAdvance(quantum);

      remainingQuant = quantum;
      nextvelocity = velocity.add(accel.scalarMult(sigma));
      //System.out.println(" istate =" +velocity +"accel=" +accel);
      //System.out.println(" next-istate =" +nextvelocity );
      nextState = state.add(accel.scalarMult(sigma*sigma/2).add(velocity.scalarMult(sigma)));

   //   if (accel.norm() ==  0)
   //      System.out.println(getName()+ "ERROR: accel can't be zero");

}

public void computeExtNextstate(){
timeAdvance(remainingQuant);
nextvelocity = velocity.add(accel.scalarMult(sigma));
nextState = state.add(accel.scalarMult(sigma*sigma/2).add(velocity.scalarMult(sigma)));
}




public void deltcon(double e,message x)
{

 deltint();

 deltext(0,x);
}

public void  deltext(double e,message   x)
{
  vect3DEnt sum = new vect3DEnt(0,0,0);
  vect3DEnt sumV = new vect3DEnt(0,0,0);

  Continue(e);
  clock = clock + e;

 for (int i=0; i< x.getLength();i++){

  if (messageOnPort(x,"stop",i)){
  passivate();
  break;
  }

//  if (messageOnPort(x,"inV",i)){
//       entity ent = x.getValOnPort("inV",i);
//       sumV.addSelf((vect3DEnt)ent);
//    }


   if (messageOnPort(x,"in",i)){
       entity ent = x.getValOnPort("in",i);
       sum.addSelf((vect3DEnt)ent);
    }

 }/*end for i*/

  //if accel is relative to last accel
/*
  if (sum.norm()>0 || sumV.norm()>0 ){

   if (sum.norm()>0 ) {
     setInp(accel.add(sum) );  //if accel is relative to last accel

   }
//   if (sumV.norm()>0 ){
//     lastvelocity = velocity;
//     velocity.addSelf(sumV); //for relative accels
//   }

     update(e);
     computeExtNextstate();
     phase = state.toString();
  }
*/
  //if accel is absolute
  // {
     setInp(sum);
     update(e);
     computeExtNextstate();
     phase = state.toString();
  // }
}

public void  deltint( )
{
clock = clock + sigma;
state = nextState;
velocity = nextvelocity;
computeIntNextstate();
phase = state.toString();
}

public String getTooltipText(){
   return
  super.getTooltipText()
  +"\n"+" quantum: "+ quantum
   +"\n"+" remainingQuant: "+ remainingQuant
  + "\n"+ "initialState :"+ initialState
     + "\n"+ "velocity :"+ velocity
     +"\n"+"accel :"+ accel
   +"\n"+"lastaccel :"+ lastaccel;
  }


public message out()
{

message   mes = new message();
if (clock <= 0 && sigma == 0){
mes.add(makeContent("out",  state));
mes.add(makeContent("outY", new doubleEnt(nextState.y)));
mes.add(makeContent("out1",  velocity));
mes.add(makeContent("out1Y", new doubleEnt(velocity.y)));
mes.add(makeContent("outDraw2D",new DrawCellEntity(state.x, state.y,
              Color.gray , Color.gray)));
              }
else{
mes.add(makeContent("out",  nextState));
mes.add(makeContent("outY",  new doubleEnt(nextState.y)));
mes.add(makeContent("out1",  nextvelocity));
mes.add(makeContent("out1Y", new doubleEnt(velocity.y)));
mes.add(makeContent("outDraw2D",new DrawCellEntity(nextState.x, state.y,
              Color.black , Color.gray)));
   }



return mes;

}

}