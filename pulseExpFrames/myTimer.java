
package pulseExpFrames;




import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class myTimer extends ViewableAtomic{

    protected double time,duration;

    public myTimer(String nm,double duration){
        super(nm);
        this.duration = duration;
        addInport("start");
        addInport("restart");
        addOutport("out");
        addRealTestInput("in", 0,5);
        addRealTestInput("in", 0,10);
        addRealTestInput("start", 5,0);
        addRealTestInput("start", 20 ,0);
        addRealTestInput("start", 1000,0);
        addPortTestInput("restart");
    }

   public myTimer(String nm){
   this(nm,0);
   }

    public myTimer()
    {
        this("myTimer");
    }

    public void initialize()
    {
        super.initialize();
        if (duration != 0)
        holdIn("active",duration);
        else
        passivate();
        time = 0;
    }

    public void deltext(double e, message x)
    {
        Continue(e);
        time += e;

     if (phaseIs("passive"))
      if (somethingOnPort(x,"start")){
         duration = getRealValueOnPort(x,"start");
         time = 0;
         holdIn("active",duration);
     }
     else if (somethingOnPort(x,"restart"))
         holdIn("active",duration);
    }

    public void deltint()
    {
         time += sigma;
         passivate();
    }

    public message out()
    {
    if (phaseIs("active"))
    return outputRealOnPort(time+sigma,"out");
    else return outputRealOnPort(0,"dum");
    }

    public void showState()
    {
        super.showState();
        System.out.println("time :" + time);
    }

    public String stringState()
    {
        return
            super.stringState()
            + "\n" + "time :" + time;
    }
}
