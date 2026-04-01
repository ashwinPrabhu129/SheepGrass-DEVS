/* Author: Andree Jacobson (andree@cs.arizona.edu)
 * Date  : 09/03/02
 * Course: ECE575 - OO Simulation & Discrete Event Models
 */

package pulseModels;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class scubaModel extends realDevs{
  protected double firstDuration, secondDuration, thirdDuration;
  protected String firstOutput, secondOutput, thirdOutput;

  public scubaModel(){
    this("scubaModel",20,20,10, // 1st = 20 min, 2nd = 20 min, 3rd = 10 min
	"60 ft",		// Messages are now strings of depth
	"40 ft",
	"15 ft");
  }


  public scubaModel(String nm,
      double firstDuration, double secondDuration,double thirdDuration,
      String firstOutput, String secondOutput,String thirdOutput)
    /* Durations specified in minutes, Message is a string */
  {
    super(nm);
    this.firstDuration = firstDuration;
    this.secondDuration = secondDuration;
    this.thirdDuration = thirdDuration;
    this.firstOutput = firstOutput;
    this.secondOutput = secondOutput;
    this.thirdOutput = thirdOutput;

    /* Changed names of ports to better fit model (Throughout file) */
    addInport("Dive");
    addInport("Surface");
    addOutport("Depth");
    addPortTestInput("Dive");
    addPortTestInput("Surface");
  }


  public void initialize(){
    super.initialize();
    holdIn("active",0);
  }


  public void deltext(double e,message x){
    Continue(e);

    if (somethingOnPort(x,"Dive"))
      holdIn("first",firstDuration);
    else if (somethingOnPort(x,"Surface"))
      //passivate();
      holdIn("emergency", 1);
    }

  public void   deltint(){
    /* Must multiply durations by 60 since holdIn obviously expects seconds. */
    if (phaseIs("active"))
      holdIn("first",firstDuration*60);
    else if (phaseIs("first"))
      holdIn("second",secondDuration*60);
    else if (phaseIs("second"))
      holdIn("third",thirdDuration*60);
    else if (phaseIs("third"))
      passivate();
    else // Emergency
      passivate();
  }

  public message out(){
    /* Changed the output ports to Depth... */
    if (phaseIs("active"))
      return outputNameOnPort(firstOutput,"Depth");
    else if (phaseIs("first"))
      return outputNameOnPort(secondOutput,"Depth");
    else if (phaseIs("second"))
      return outputNameOnPort(thirdOutput,"Depth");
    else if (phaseIs("third"))
      return outputNameOnPort("Surface","Depth");
    else // if phaseIs("emergency")
      return outputNameOnPort("Emergency","Depth");
  }

}
