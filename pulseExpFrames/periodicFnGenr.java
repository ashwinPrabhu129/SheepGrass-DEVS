package pulseExpFrames;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;



public class periodicFnGenr extends pulseGenr{


static class sinGenr extends periodicFnGenr{
double omega;

public sinGenr(String nm,double period,double amplitude,int samples){
super(nm,period,amplitude,samples);
omega =  2*Math.PI/period;
this.amplitude = amplitude;
}

public double fn(double x){
return amplitude*Math.sin(omega*clock);
}

}




double clock,amplitude,period;

public periodicFnGenr(String nm,double period,double amplitude,int samples){
super(nm,period/samples);//sampleInterval = period/samples
this.period = period;
this.amplitude = amplitude;
}


public void initialize(){
clock = 0;
size = 0;
super.initialize();
holdIn("active",0);
}

public double fn(double x){
return 1.5*amplitude*Math.pow(x/period,4);//2);
}

public void   deltint(){
clock += interPulseTime;
double time = ((clock /period) % 1) * period;
size = fn(time);
holdIn("active",interPulseTime);
}
}



