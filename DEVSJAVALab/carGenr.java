/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */


package DEVSJAVALab;

import simView.*;


import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import util.*;
import statistics.*;

public class carGenr extends ViewableAtomic{


  protected double int_gen_time;
  protected int count;
  protected rand r;

  public carGenr() {this("carGenr", 7);}

public carGenr(String name,double period){
   super(name);
   addInport("in");
   addOutport("out");

   int_gen_time = period ;
   
   addTestInput("in",new entity("an entity"),7);
}

public void initialize(){
  r = new rand(888888);
   holdIn("active", int_gen_time);
   count = 0;
}


public void  deltext(double e,message x)
{
Continue(e);
   for (int i=0; i< x.getLength();i++){
     if (messageOnPort(x, "in", i)) { //the stop message from tranducer
       passivate();
     }
   }
}

public void  deltint( )
{

if(phaseIs("active")){
   count = count +1;
//   holdIn("active",int_gen_time);
   holdIn("active",10+r.uniform(int_gen_time));
}
else passivate();
}

public message  out( )
{
//System.out.println(name+" out count "+count);
   message  m = new message();
//   content con = makeContent("out", new entity("car_" + count));
   content con = makeContent("out", new vehicleEntity("car" + count, 5+r.uniform(20), 50+r.uniform(100), 1));
   m.add(con);

  return m;
}


}

