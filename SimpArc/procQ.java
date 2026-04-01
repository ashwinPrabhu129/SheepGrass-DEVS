/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */


package SimpArc;

import simView.*;

import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;

public class procQ extends proc{
  protected DEVSQueue q;


public procQ(String  name,double  Processing_time){
super(name,Processing_time);
q = new DEVSQueue();
}

public procQ(){
super("procQ",20);
addTestInput("in",new entity("job1"));
addTestInput("in",new entity("job2"));
addTestInput("none",new entity("job"));
initialize();
}

public void initialize(){
q = new DEVSQueue();
super.initialize();

 }


 public void  deltext(double e, message   x){
  Continue(e);
  if (phaseIs("passive")){
    for (int i=0; i< x.size(); i++)
      if (messageOnPort(x, "in", i)){
        job = x.getValOnPort("in", i);
        holdIn("busy", processing_time);
        q.add(job);
      }

    job = (entity)q.first();  // this makes sure the processed job is the one at
                  //the front
     }

   else if (phaseIs("busy")){
   for (int i=0; i< x.size();i++)
      if (messageOnPort(x, "in", i))
      {
      entity jb = x.getValOnPort("in", i);
      q.add(jb);
      }
   }
}

public void  deltint( ){
q.remove();
if(!q.isEmpty()){
  job = (entity)q.first();
   holdIn("busy", processing_time);
}
else passivate();
}

 public void showState(){
    super.showState();
    System.out.println("Queue length: " + q.size());
 }

}


