/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */


package pulseModels;

import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class preyPredator extends ViewableAtomic{

protected double prey, pred;


  public preyPredator() {
    this("preyPredator",100,0);//140,0);//10);
  }

  public preyPredator (String name, double prey, double pred) {
   super(name);
   addInport("in");
   addOutport("out");
   addOutport("gasp");
   this.prey = prey;
   this.pred = pred;
   addTestInput("in",new entity(""));
   }

 public void initialize(){
      phase = "active";
      sigma = pred>0?pred/(prey+1):INFINITY;
      if (sigma < INFINITY)
       phase = "active";
       else phase = "passive";
      super.initialize();
  }

  public void  deltint(){
   if(prey>=pred)
    {
      prey = prey - pred;
      pred= 2*pred;
      sigma = pred/(prey+1);
     }
    else if (prey>0 && prey< pred)
      {
        prey = 0;
        pred = pred + prey;
      }
      else {
        prey = 0;
        pred = 0;
      }
     sigma = pred>0?pred/(prey+1):INFINITY;
     if (sigma < INFINITY)
          phase = "active";
     else phase = "passive";

  }

public void  deltext(double e,message x)
{
  if (pred > 0){
    Continue(e);
   for (int i=0; i< x.getLength();i++)
      if (messageOnPort(x,"in",i))
         pred = pred +1;
    }
  else { // pred = 0
         pred = 1;
         sigma = 1/(prey+1);
         phase = "active";
       }
  }

public void deltcon(double e,message x){ //usual devs
   deltint();
   deltext(0,x);
}

  public message  out( )
{
   message  m = new message();
   content con = makeContent("out",
            new entity("totalPop" + (pred+prey)));
   m.add(con);

   if (pred > 0 && prey == 0){
    con = makeContent("gasp",
            new entity("last gasp"));
  m.add(con);
  }
  return m;
}

public String getTooltipText(){
   return
   super.getTooltipText()
    +"\n"+" prey: " + prey
     +"\n"+" pred: " + pred;
  }

}
