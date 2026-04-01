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

public class truckGenr extends ViewableAtomic{


  protected double int_gen_time;
  protected int count;
  protected rand r;

  public truckGenr() {this("truckGenr", 30);}

public truckGenr(String name,double period){
   super(name);
   addInport("in");
   addOutport("out");

   int_gen_time = period ;
}

public void initialize(){
   holdIn("active", 2);
   r = new rand(2887766);
   count = 0;
}


public void  deltext(double e,message x)
{
Continue(e);

}


public void  deltint( )
{

if(phaseIs("active")){
   count = count +1;
   holdIn("active",20+r.uniform(int_gen_time));
}
else passivate();
}

public message  out( )
{

//System.out.println(name+" out count "+count);

   message  m = new message();
//   content con = makeContent("out", new entity("truck" + count));
   content con = makeContent("out", new vehicleEntity("truck" + count, 10+r.uniform(30), 100+r.uniform(100), 1));
   m.add(con);

  return m;
}


}

