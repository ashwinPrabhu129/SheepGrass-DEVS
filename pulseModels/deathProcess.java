/* Author: Andree Jacobson (andree@cs.arizona.edu)
 * Date  : 09/03/02
 * Course: ECE575 - OO Simulation & Discrete Event Models
 */

package pulseModels;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class deathProcess extends ViewableAtomic{
  protected double timeToDie;


  public deathProcess(){
    this("deathProcess",.4 ); //timeToShoot includes travel time
  }


  public deathProcess(String nm,double timeToDie)
  {
    super(nm);
    this.timeToDie = timeToDie;


    addInport("getHit");
    addOutport("outDead");

    addPortTestInput("getHit");
    addPortTestInput("getHit",.1);

   }

  public void initialize(){
    super.initialize();
    passivateIn("alive");
  }


  public void deltext(double e,message x){
    Continue(e);
if (phaseIs("alive"))
  if (somethingOnPort(x,"getHit"))
     holdIn("dying",timeToDie);

}

  public void   deltint(){
    if (phaseIs("dying"))
       passivateIn("dead");
      }


  public message out(){
    if (phaseIs("dying"))
      return  outputNameOnPort("dead","outDead");
   else return new message();

  }


}
