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

public class twoDint2ndOrd extends twoDint{


static double diplacement(double v,double a,double t){
return v*t + .5*a*t*t;
}

static double timeToCross(double v,double a, double q){
double tm,xm,r,tc0;

if (v ==0 && a == 0) return Double.POSITIVE_INFINITY;
if (v<0)return timeToCross(-v,-a,q);
//v is now non negative
if (v == 0) v =.0000000001;
//if (a == 0) a =.0000000001;
if (a<0){
tm = -v/a;
xm = -v*v/(2*a);
r = q/xm;
tc0 = q/v;
if (r == 1)
return v/a;
else if (r <= 1)
return tc0 -.5*(a*tc0*tc0)/(v+a*tc0);
else //r > 1
return 2*tm + timeToCross(-v,a,q);
}
else if (a == 0)return q/v;
else{ // a>0,r = a*q/(2*v*v);
return (Math.sqrt(v*v+2*a*q) - v)/a;
}
}

public twoDint2ndOrd(String  name, double Quantum, vect2DEnt state, Color myCol){
super(name,Quantum,state,myCol);
elapsed = 0;
}

public twoDint2ndOrd(String  name, double Quantum, vect2DEnt state){
this(name,Quantum,state,Color.black);
}

public twoDint2ndOrd(){
this("twoDint2ndOrd",1,vect2DEnt.ZERO);
}


public void initialize(){
elapsed = 0;
deriv2 = vect2DEnt.ZERO;
super.initialize();
}



public void setInp(vect2DEnt Input){


    lastInput = input;
    input = Input;

    if (elapsed > 0){
    deriv2 = input.subtract(lastInput);

    deriv2 = deriv2.scalarMult(1/elapsed);

    elapsed = 0;
    }

}

public vect2DEnt  computeDisplacement(vect2DEnt v,vect2DEnt a,double e){
    double dx = diplacement(v.x,a.x,e);
    double dy = diplacement(v.y,a.y,e);
   return new vect2DEnt(dx,dy);
}

public void timeAdvance(double difference){

    sigma = Math.min(
                timeToCross(input.x,deriv2.x,difference)
                ,timeToCross(input.y,deriv2.y,difference));
}


public void update(double e){

   vect2DEnt v = computeDisplacement(lastInput,deriv2,e);
    state = state.add(v);
    vect2DEnt diff = nextState.subtract(state);
    remainingQuant = diff.norm();
    if (remainingQuant == 0)  remainingQuant = quantum;
    if (remainingQuant < 0)
              System.out.println(getName()+ " ERROR: remainingQuant can't be negative");
}


public void computeIntNextstate(){

      timeAdvance(quantum);
      remainingQuant = quantum;
      if (sigma < INFINITY){
          vect2DEnt v = computeDisplacement(input,deriv2,sigma);
         nextState = state.add(v);
         }
}

public void computeExtNextstate(){
      timeAdvance(remainingQuant);
      if (sigma < INFINITY){
      vect2DEnt v = computeDisplacement(input,deriv2,sigma);
      nextState =  state.add(v);
}
}


public void  deltext(double e,message   x){
elapsed = e;
super.deltext(e,x);

}
double q =.05;

/*
void changeQuantum(){
if (state.norm() < 3)quantum = 10*q;
else if (state.norm() < 6)quantum = 5*q;
else
quantum = q;
}
*/

void changeQuantum(){

double inputNorm = input.norm();
double derivNorm = deriv2.norm();
if (clock <= 0)
quantum = q;
else if (derivNorm ==0)
quantum = INFINITY;
//
else
//quantum = Math.max(q,inputNorm*inputNorm/derivNorm);//no good
//quantum = Math.max(q,10*q*Math.pow(derivNorm,-.2));
quantum = Math.max(q,.1*Math.pow(derivNorm,-.2));
//quantum = q/Math.sqrt(derivNorm); //koffman

}



public void  deltint( )
{

clock = clock + sigma;
state = nextState.copy();
//
changeQuantum();
computeIntNextstate();
phase = state.toString();
}


static double v = 5;//0;//50;
static double a = -.1;//1000;//0;//.1;//-1;
static double x(double t){return v*t + .5*a*t*t;}


public static void main1(String[] args){

//v >=0
if (v ==0 && a == 0) return;

double q = 100;

if (a<0){

double tm = -v/a;

double xm = -v*v/(2*a);

//to set r = 1 q = xm;
//to set r > 1 q = xm*2;

double r = q/xm;
double tc0 = q/v;

if (r <= 1){
System.out.println("tm "+tm+" xm "+xm +" x "+x(tm));
System.out.println("r "+r+" pred %err "+(100*r/4));
System.out.println("tc0 "+tc0 + " %error "+100* Math.abs((x(tc0)-q)/q));
double dt = -.5*(a*tc0*tc0)/(v+a*tc0);
double tc = tc0+dt;
System.out.println("pred %err "+100*Math.pow(r/4,3));
System.out.println("dt "+dt + " tc "+tc +" %error "+100* Math.abs((x(tc)-q)/q));
}
else{//r>1  cross at -q
System.out.println("r "+r);
double tau = 2*tm;
System.out.println("tau "+tau);
v = -v;
a = -a;

tc0 = (Math.sqrt(v*v+2*a*q) - v)/a; // better use the case for a>0
double tc = tau+ tc0;
System.out.println("tc0 "+tc0 + " tc "+tc);
System.out.println("%error "+100* Math.abs((x(tc0)-q)/q));
}
}
else{ // a >=0
if (v == 0) v=.0000000001;
if (a == 0) a =.0000000001;

double r = a*q/(2*v*v);
System.out.println("r "+r);

double tc = (Math.sqrt(v*v+2*a*q) - v)/a;
double tc0;

if(r>=1){
tc0 = Math.sqrt(2*q/a); // = tc =if v was 0
System.out.println("%pred error "+ 100/(8*r));
}
else{ //if (r < 1)
tc0 = q/v; //=tc if a was 0
System.out.println("%pred error "+ 300*r);
}
System.out.println("tc0 "+tc0 + " "+" tc "+tc);
System.out.println(" %error in t "+100* Math.abs((tc-tc0)/tc));
System.out.println(" %error in x "+100* Math.abs((x(tc0)-q)/q));

}
}

}

