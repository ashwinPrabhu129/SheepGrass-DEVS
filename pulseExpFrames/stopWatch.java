
package pulseExpFrames;




import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class stopWatch extends ViewableAtomic{
    protected double time;


    public stopWatch(String nm)
    {
        super(nm);
        addInport("start");
        addInport("stop");
        addInport("time?");
        addInport("reset");
        addOutport("timeIs");
        addPortTestInput("start");
        addPortTestInput("stop",10);
        addPortTestInput("reset");
        addPortTestInput("time?");
        addPortTestInput("time?",4);

    }

    public stopWatch()
    {
        this("stopWatch");
    }

    public void initialize()
    {
        super.initialize();
        passivate();
        time = 0;
    }

    public void deltext(double e, message x)
    {

        Continue(e);

if (phaseIs("passive")){

    if (somethingOnPort(x,"start"))

    passivateIn("active");

    else if (somethingOnPort(x,"reset"))
    time = 0;
    else if (somethingOnPort(x,"time?"))
       holdIn("respond", 0);
   }

else if (phaseIs("active")) {
    time += e;   //always update in case of spurious inputs
    if (somethingOnPort(x,"stop")){
      passivateIn("stopped");
     }
   if (somethingOnPort(x,"time?")&& somethingOnPort(x,"stop"))
     holdIn("respond", 0);  //for simultaneous stop & time?
    }
else if (phaseIs("stopped")){
       // don't update time in this phase
     if (somethingOnPort(x,"time?"))
       holdIn("respond", 0);
   }
}

    public void deltint()
    {
    if (phaseIs("respond"))
         //always
         passivate();
    }

    public message out()
    {
    if (phaseIs("respond"))
    return outputRealOnPort(time,"timeIs");
    else
    return new message();
    }

    public void showState()
    {
        super.showState();
        System.out.println("time :" + time);
    }


    public String getTooltipText(){
    return
     super.getTooltipText()
            + "\n" + "time :" + time;
    }
}
