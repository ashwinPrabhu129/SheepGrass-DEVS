package  oneDCellSpace;

import java.awt.*;
import java.lang.*;
import java.lang.Math;
import java.io.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import simView.*;
import java.util.*;

public class minSelect extends ViewableAtomic{
public int numIter;
public boolean root;
protected double tnext;
protected genDevs.simulation.special.minEnvironment de;
protected Pair whichMin;

public minSelect(String name,double tN){
 super(name);
 de = new genDevs.simulation.special.minEnvironment();
 tnext = tN;
 root = false;
//as defined on the fly
 addOutport("out");
}


public minSelect(String name){
this(name,0);
}

public minSelect(){
  this("minSelect");
}

public void addChild(String childName){
de.addName(childName);
}


public void superInitialize(){//needed for inheritance
     de.reset();
     super.initialize();
     }


public void initialize(){
  superInitialize();
     passivate();
}


public void  deltext(double e,message x)
{
Continue(e);
if (phaseIs("passive")){
FunctionIterator itr = (FunctionIterator)de.iterator();
    while(itr.hasNext())
      {
        Pair p = (Pair)itr.next();
        Object ent = p.getKey();
        String inf = ent.toString();

   for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,inf,i)) {
         entity en = x.getValOnPort(inf,i);
         de.setPair(inf,(Pair)en);
        }

      }
whichMin = de.whichMin();
holdIn("sendUp",0);
if (root){
numIter++;
System.out.println("number of imminents "+((HashSet)whichMin.getKey()).size());
holdIn("sendDown",0);
}
}
}

public void  deltint( )
{
passivate();
}

public message  out( )
{
   message  m = new message();
if (phaseIs("sendUp"))
   m.add(makeContent("out",whichMin));
else if (phaseIs("sendDown"))
   m.add(makeContent("go",whichMin));
  return m;
}

public String getTooltipText(){
return
    super.getTooltipText()
     + "\n"+"minEnvir : " + de
        + "\n"+"whichMin : " +whichMin;

}


}

///////////////////////////////////////

class leafGen extends minSelect{
protected double count;
protected ensembleSet hs;

public leafGen(String name,double tN){
 super(name,tN);
  addInport("go");
}

public leafGen(String name){
this(name,0);
}

public leafGen(){
  this("leafGen");
}

public void initialize(){
  superInitialize();
   count = 0;
   hs = new ensembleSet();
   hs.add(name);
   whichMin = new Pair(hs,new doubleEnt(tnext));
   holdIn("sendUp",0);
}


public void  deltext(double e,message x)
{
Continue(e);
if (phaseIs("passive")){
       if (somethingOnPort(x,"go")){
       Pair p = (Pair)getEntityOnPort(x,"go");
       Set b = (Set)p.getKey();
       if (b.contains(name)){
       count++;
       whichMin = new Pair(hs,new doubleEnt(tnext+count));
       holdIn("sendUp",1);
        }
        }
      }
}


public void  deltint( )
{
passivate();
}

}

////////////////////////////////////

