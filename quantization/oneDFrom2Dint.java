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

public class oneDFrom2Dint extends twoDint{//2ndOrd{


public oneDFrom2Dint(String  name, double Quantum, double x, Color myCol){
this(name,Quantum,x);
this.myCol = myCol;
}

public oneDFrom2Dint(String  name, double Quantum, double x){
super(name,Quantum,new vect2DEnt(x,0));
addOutport("outNeg");
}

public oneDFrom2Dint(){
this("oneDFrom2Dint",1,0);
}




public void  deltext(double e,message   x)
{

message nm = new message();
double sum = 0;
 for (int i=0; i< x.getLength();i++)
  if (somethingOnPort(x,"in"))
    sum = sumValuesOnPort(x,"in");

nm.add(makeContent("in",new vect2DEnt(sum,0)));
super.deltext(e,nm);

}

public message    out( )
{
message   m = new message();
if (clock <= 0 && sigma == 0){
m.add(makeContent("out",  new doubleEnt(state.x)));
m.add(makeContent("outNeg",  new doubleEnt(-state.x)));

m.add(makeContent("outDraw",new DrawCellEntity(state.x, state.y,
              myCol, Color.lightGray)));
              }
else{
m.add(makeContent("out",  new doubleEnt(nextState.x)));
m.add(makeContent("outNeg",  new doubleEnt(-nextState.x)));
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
