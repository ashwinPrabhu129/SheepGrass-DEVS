package pulseExpFrames;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class fnRateGenr extends pulseGenr{
double clock, quantum, power;

public fnRateGenr(String nm,double power,double quantum){
super(nm);
this.power = power;
this.quantum = quantum;
}

public fnRateGenr(double power){
this("fnRateGenr",power,1);
}

double inputFn(){
return Math.pow(clock,power);
}
double derivativeFn(){
return power*Math.pow(clock,power-1);
}

public void initialize(){
clock = 1;
interPulseTime = Math.abs(quantum/derivativeFn());
size = (power>0?1:-1)*quantum;
super.initialize();
}

public void   deltint(){
clock += sigma;
interPulseTime = Math.abs(quantum/derivativeFn());
holdIn("active",interPulseTime);
}

public message out(){
message m = super.out();//puts out pulses with pulse size
return outputRealOnPort(m,derivativeFn(),"rate");

}

}