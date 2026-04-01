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


public class multiServerCoord extends Coord{

protected DEVSQueue  procs;
protected DEVSQueue   jobs;
//protected proc   pcur;
protected message yMessage;

public multiServerCoord(String  name){
super(name);

jobs = new DEVSQueue();
procs = new DEVSQueue();
}

public multiServerCoord(){
super("multiServerCoord");

jobs = new DEVSQueue();
procs = new DEVSQueue();


//CAUTION: start with port "setup" to test

addTestInput("setup",new entity(""));
addTestInput("in",new entity("val"));
addTestInput("x",new Pair(new proc("p",5000),
                       new entity("val")));

//initialize();
}

public void initialize(){
     phase = "passive";
     sigma = INFINITY;
     job = null;
     super.initialize();;
 }

public void showState(){
    super.showState();
    System.out.println("number of jobs: " + jobs.size());
    System.out.println("number of procs: " + procs.size());
}


//protected void add_procs(proc   p){ //BPZ 99 this cause problem
protected void add_procs(devs   p){

procs.add(p);
//pcur = p;
}

public void  deltext(double e,message   x)
{

Continue(e);

if (phaseIs("passive"))
{
 for (int i=0; i< x.size();i++)
 if (messageOnPort(x,"setup",i))
     add_procs(new proc("p",1000));
}

if (phaseIs("passive"))
{
  yMessage = new message();
for (int i=0; i< x.size();i++)
  if (messageOnPort(x,"in",i))
      {
      job = x.getValOnPort("in",i);

      if ( !procs.isEmpty())
      {
       entity   pcur  = (entity)procs.first();
       procs.remove();
       yMessage.add(makeContent("y",new Pair(pcur,job)));
       holdIn("send_y",0);
      }
      }
}
// (proc,job) Pairs returned on port x
//always accept so that no processor is lost

 for (int i=0; i< x.size();i++)
 if (messageOnPort(x,"x",i))
    {
      entity   val = x.getValOnPort("x",i);
      Pair pr = (Pair)val;
      procs.add(pr.getKey());
      entity   jb = (entity)pr.getValue();
      jobs.add(jb);
    }
 //output completed jobs at earliest opportunity
    if (phaseIs("passive") && !jobs.isEmpty())
       holdIn("send_out",0);

}

public void  deltint( )
{
if (phaseIs("send_out"))
{
      jobs = new DEVSQueue();
      passivate();
}
 //output completed jobs at earliest opportunity

else if (phaseIs("send_y") && !jobs.isEmpty())
       holdIn("send_out",0);
else passivate();
}

public message    out( )
{
message   m = new message();
  if (phaseIs("send_out"))
 for (int i= 0; i< jobs.size();i++)
     {
      entity   job = (entity)jobs.get(i);
      m.add( makeContent("out",job));
     }
else
 if (phaseIs("send_y"))
    m = yMessage;
return m;
}


}


