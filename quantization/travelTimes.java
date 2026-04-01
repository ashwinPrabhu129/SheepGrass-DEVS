/*      Copyright 2002 Arizona Board of regents on behalf of
*                  The University of Arizona
*                     All Rights Reserved
*         (USE & RESTRICTION - Please read COPYRIGHT file)
*
*  Version    : DEVSJAVA 2.7
*  Date       : 08-15-02
 */

package quantization;

import java.awt.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import simView.*;

////////////////////////////////////////////////////
// travelTimes
////////////////////////////////////////////////////
public class travelTimes extends ViewableDigraph {

  /////////////////////////////////////////////////////////
  // routeGenr: simulation of external events
  /////////////////////////////////////////////////////////
  class routeGenr extends ViewableAtomic{
    protected double firstDuration, secondDuration, thirdDuration, fourthDuration;
    protected String firstOutput, secondOutput, thirdOutput, fourthOutput, fifthOutput;

    public routeGenr(
        String nm,
        double firstDuration, double secondDuration, double thirdDuration, double fourthDuration,
      String firstOutput, String secondOutput, String thirdOutput, String fourthOutput, String fifthOutput){
      super(nm);
      this.firstDuration = firstDuration; this.secondDuration = secondDuration;
      this.thirdDuration = thirdDuration; this.fourthDuration = fourthDuration;
      this.firstOutput = firstOutput; this.secondOutput = secondOutput;
      this.thirdOutput = thirdOutput; this.fourthOutput = fourthOutput;
      this.fifthOutput = fifthOutput;

      addInport("start");
      addInport("stop");
      addInport("test");
      addOutport("dest");
      addOutport("cond");
      addPortTestInput("start");
      addPortTestInput("stop");
      addPortTestInput("test", 0);
      addPortTestInput("test", 1.0);
      addPortTestInput("test", 2.0);
    }
    public void initialize(){
      super.initialize();
      holdIn("active",0);
    }
    public void deltext(double e,message x){
      Continue(e);

      if (somethingOnPort(x,"start"))
        holdIn("first",firstDuration);
      else if (somethingOnPort(x,"stop"))
        passivate();
    }
    public void   deltint(){

      if (phaseIs("active"))
        holdIn("first",firstDuration);
      else if (phaseIs("first"))
        holdIn("second",secondDuration);
      else if (phaseIs("second"))
        holdIn("third",thirdDuration);
      else if (phaseIs("third"))
        holdIn("fourth",fourthDuration);
      else // (phaseIs("fourth"))
        passivate();
    }
    public message out(){
      if (phaseIs("active"))
        return outputNameOnPort(firstOutput,"dest");
      else if (phaseIs("first"))
        return outputNameOnPort(secondOutput,"cond");
      else if (phaseIs("second"))
        return outputNameOnPort(thirdOutput,"cond");
      else if (phaseIs("third"))
        return outputNameOnPort(fourthOutput,"dest");
      else // if (phaseIs("fourth"))
        return outputNameOnPort(fifthOutput,"dest");
    }
}

//////////////////////////////////////////////////
  public travelTimes(){
    super("travelTimes");

   //ViewableAtomic rg = new routeGenr("routeGenr", 1.0, 50.0, 100.0, 100.0,
                                        // dest, cond, cond, dest, dest
   //                                     "Casa Grande", "rain", "hail", "Phoenix", "Yuma");
   ViewableAtomic rg = new routeGenr("routeGenr", 0.0, 2*69, 100.0, 100.0,
                                       // dest, cond, cond, dest, dest
                                        "Casa Grande", "rain", "hail", "Phoenix", "Yuma"); add(rg);

    ViewableAtomic ad = new arizonaDriving();
    add(ad);

    ViewableAtomic disp = new varDisp("arrived");
    add(disp);

    addCoupling(this,"destination",ad,"destination");
    addCoupling(this,"condition",ad,"condition");

    addCoupling(rg,"dest",ad,"destination");
    addCoupling(rg,"cond",ad,"condition");
    addCoupling(ad,"out",disp,"inString");
    addTestInput("start",new entity());

    addInport("destination");
    addInport("condition");

    addTestInput("destination",new entity("Phoenix"), 1);
    addTestInput("destination",new entity("Tucson"), 0);
    addTestInput("destination", new entity("Flagstaff"),0);
    addTestInput("destination", new entity("Casa Grande"), 0);
    addTestInput("destination", new entity("Kingman"), 1);
    addTestInput("destination",new entity("Yuma"), 10);
    addTestInput("condition",new entity("rain"), 0);
    addTestInput("condition",new entity("hail"), 0);
    addTestInput("condition",new entity("sun"), 0);
    addTestInput("condition",new entity("snow"), 0);
      // one hour drive
    addTestInput("condition",new entity("rain"), 60);
    addTestInput("condition",new entity("hail"), 60);
    addTestInput("condition",new entity("sun"), 60);
    addTestInput("condition",new entity("snow"), 60);

    initialize();
  }

  /////////////////////////////////////////
  // varDisp : Displays values and/or strings
  /////////////////////////////////////////
  public class varDisp extends  ViewableAtomic{
  protected double state,initState;
  public varDisp(String nm,double state){
    super(nm);
    initState = state;
    addInport("in");
    addInport("inString");
    addInport("reset");
    addOutport("out");
    this.addNameTestInput("inString","test");
    addRealTestInput("in",1);
    addRealTestInput("in",1,3);
    addRealTestInput("in",1,5);
    addPortTestInput("reset");
  }
  public varDisp(String nm){
    this(nm,0);
  }
  public varDisp(){
    this("varDisp");
  }
  public void initialize(){
    super.initialize();
    state = initState;
    passivateIn(String.valueOf(state));
  }
  public void deltext(double e,message x){
    Continue(e);
    if (somethingOnPort(x,"in")){
      state = getRealValueOnPort(x,"in");
      passivateIn(String.valueOf(state));
    }
    if (somethingOnPort(x,"inString")){
      passivateIn(getNameOnPort(x,"inString"));
    }
    else if (somethingOnPort(x,"reset")){
      state = 0;
      passivateIn(String.valueOf(state));
    }
  }
  public void deltint(){
    passivateIn(String.valueOf(state));
  }
  public void deltcon(double e,message x)
  {
    deltext(e,x);
  }
  public message out(){
    return outputRealOnPort(state,"out");
  }
  public void showState(){
    super.showState();
    System.out.println(
        "\n"+ "state :"+ state
        );
  }

  public String getTooltipText(){
    return
    super.getTooltipText()
        +"\n"+"state :"+ state;
  }
  }
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(571, 240);
        ((ViewableComponent)withName("arizonaDriving")).setPreferredLocation(new Point(109, 103));
        ((ViewableComponent)withName("arrived")).setPreferredLocation(new Point(287, 178));
        ((ViewableComponent)withName("routeGenr")).setPreferredLocation(new Point(52, 18));
    }
}
