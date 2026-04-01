package pulseExpFrames;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import statistics.*;



public class pulseGenrRandInterval extends pulseGenr{
protected rand r,rinit;

public pulseGenrRandInterval(){
this("pulseGenrRandInterval",1);
}

public pulseGenrRandInterval(String nm,double interPulseTime){
this(nm,interPulseTime,1,new randUniform(1));
}

public pulseGenrRandInterval(String nm,double interPulseTime,double size,rand rinit){
super(nm,interPulseTime,size);
this.rinit = rinit;
}

public void initialize(){
   r = rinit;
   holdIn("active",r.sample());
   super.initialize();
 }

public void deltext(double e,message x){
Continue(e);
if (somethingOnPort(x,"start"))
  holdIn("active",r.sample());
else if (somethingOnPort(x,"stop"))
  passivate();
}

public void   deltint(){
holdIn("active",r.sample());
}


}
