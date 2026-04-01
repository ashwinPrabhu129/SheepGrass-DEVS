
package quantization;

import simView.*;
import genDevs.plots.*;
import java.awt.*;
import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;
import GenCol.*;

import java.util.*;

public class arizonaDriving extends  ViewableAtomic{

  class condition extends entity{
    protected double delay;

    public condition(String s){
      super(s);
      if (s.equals("sun"))
        delay=1.0;
      else if (s.equals("rain"))
        delay=2.0;
      else if (s.equals("hail"))
        delay=3.0;
      else if (s.equals("snow"))
        delay=4.0;
      else if (s.equals("accident")) //bpz
        delay=INFINITY;
      else
        delay = -1;
    }
    double getDelay(){
      return this.delay;
    }
  }
  // cities
  final String[] cities = {"Tucson", "Phoenix", "Flagstaff", "Casa Grande", "Kingman", "Yuma"};
  final int n = cities.length;
  final double[][] t = new double[n][n];

  // state variables
  protected int dest=-1, nextDest=-1, lastStop=-1;
  protected condition cond;
  protected double remainingTime=INFINITY;

  public int cityIndex(String s){
    int i=-1;

    for (i=0; i<n; i++)
      if (cities[i].equals(s))
        break;
    return i;
  }

  public arizonaDriving(){
    super("arizonaDriving");

    addInport("destination");
    addInport("condition");
    addOutport("out");
                                      //bpa
    addTestInput("destination",new entity("Phoenix"), 1);
    addTestInput("destination",new entity("Tucson"), 0);
    addTestInput("destination", new entity("Flagstaff"),0);
    addTestInput("destination", new entity("Flagstaff"),127);//test deltcon
    addTestInput("destination", new entity("Casa Grande"), 0);
    addTestInput("destination", new entity("Kingman"), 1);
    addTestInput("destination",new entity("Yuma"), 10);
    addTestInput("condition",new entity("rain"), 0);
    addTestInput("condition",new entity("hail"), 0);
    addTestInput("condition",new entity("sun"), 0);
    addTestInput("condition",new entity("snow"), 0);
    addTestInput("condition",new entity("accident"), 0); //bpz
      // one hour drive
    addTestInput("condition",new entity("rain"), 60);
    addTestInput("condition",new entity("hail"), 60);
    addTestInput("condition",new entity("sun"), 60);
    addTestInput("condition",new entity("snow"), 60);

    // travel times in minutes (source: AAA Arizona Map)
    // Tucson
    t[0][0]=0; t[0][1]=127; t[0][2]=296; t[0][3]=69; t[0][4]=359; t[0][5]=266;
    // Phoenix
    t[1][0]=127; t[1][1]=0; t[1][2]=169; t[1][3]=58; t[1][4]=232; t[1][5]=208;
    // Flag
    t[2][0]=296; t[2][1]=169; t[2][2]=0; t[2][3]=227; t[2][4]=164; t[2][5]=377;
    // Casa Grande
    t[3][0]=69; t[3][1]=58; t[3][2]=227; t[3][3]=0; t[3][4]=290; t[3][5]=197;
    // Kingman
    t[4][0]=359; t[4][1]=232; t[4][2]=164; t[4][3]=290; t[4][4]=0; t[4][5]=310;
    // Yuma
    t[5][0]=266; t[5][1]=208; t[5][2]=377; t[5][3]=197; t[5][4]=310; t[5][5]=0;
  }

  public void initialize(){
    lastStop = dest = nextDest = cityIndex("Tucson"); // initial city
    outputNameOnPort(cities[lastStop],"lastStopOut");
    cond = new condition("sun");    // initial condition

    passivateIn("in_"+cities[lastStop]);
    super.initialize();
  }

  public void  deltext(double e, message x)
  {
    Continue(e);
    if (cond.getDelay() < INFINITY) //bpz
    remainingTime = (remainingTime - e)/cond.delay;

    for (int i=0; i< x.getLength();i++){
      if (messageOnPort(x,"destination",i)){
        nextDest = cityIndex(getNameOnPort(x, "destination"));    // override next destination
        if (this.phase.startsWith("in")){
          dest=nextDest;

          if (cond.getDelay() >= INFINITY) //bpz
          passivateIn(phase); //set sigma = INFINTITY
          else {
          remainingTime = t[lastStop][dest] * cond.getDelay();
          holdIn("to_"+cities[dest], remainingTime);
          }
        }
      }
      if (messageOnPort(x,"condition",i)){
        cond = new condition(getNameOnPort(x, "condition"));
        if (this.phase.startsWith("to")){

         if (cond.getDelay() >= INFINITY) //bpz
         passivateIn(phase); //set sigma = INFINTITY
          else {
          remainingTime = remainingTime * cond.getDelay();
          holdIn("to_"+cities[dest], remainingTime);
          }
        }
      }
    }
  }

  public void  deltint( )
  {
    lastStop=dest; // arrived at city "dest"

    if (nextDest!=dest){
      // new destination
      dest=nextDest;
      remainingTime = t[lastStop][dest] * cond.getDelay();
      holdIn("to_"+cities[dest], remainingTime); // schedule next trip
    }
    else
      // wait for new destination
      passivateIn("in_"+cities[lastStop]);
  }

  public void deltcon(double e,message x)
  {
/* preferred bpz
      deltint(); // finish this leg
      deltext(0,x);//start up again
 */
    deltext(e,x);//bpz origninal = deltext(0,x); // handle next dest or condition, sigma will be zero, anyway
                   ///in general need e, so continue reduces sigma to 0
    deltint(); // drive

  }
  public message out(){
    return outputNameOnPort(cities[dest],"out");
  }
  public String getTooltipText(){
      return
      super.getTooltipText()
          +"\n"+"last stop :"+ cities[lastStop]
          +"\n"+"dest :"+ cities[dest]
          +"\n"+"next dest :"+ cities[nextDest]
          +"\n"+"condition :"+ cond.getName()
          +"\n"+"remaining time :"+ remainingTime
          ;
  }
}