package pulseExpFrames;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import statistics.*;

public class pulseGenrRand extends pulseGenr{

  protected rand r;
  protected long seed;

public pulseGenrRand(){
this("pulseGenrRand",1);
}

public pulseGenrRand(String nm,long seed){
this(nm,10,1);
this.seed = seed;
}

public pulseGenrRand(String nm,double interPulseTime){
this(nm,interPulseTime,1);
}

public pulseGenrRand(String nm,double interPulseTime,double size){
super(nm,interPulseTime,size);
}

public void initialize(){
   r = new rand(seed);
   holdIn("active",r.expon(interPulseTime));
   super.initialize();
 }

public void deltext(double e,message x){
Continue(e);
if (somethingOnPort(x,"start"))
  holdIn("active",r.expon(interPulseTime));
else if (somethingOnPort(x,"stop"))
  passivate();
}

public void   deltint(){
//holdIn("active",r.uniform(interPulseTime));
holdIn("active",r.expon(interPulseTime));
}


}
