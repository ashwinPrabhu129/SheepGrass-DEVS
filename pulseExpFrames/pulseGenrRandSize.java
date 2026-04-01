package pulseExpFrames;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import util.*;
import statistics.*;


public class pulseGenrRandSize extends pulseGenr{
protected rand r,rinit;


public pulseGenrRandSize(){
this("pulseGenrRandSize",1,0);
}

public pulseGenrRandSize(String nm,double mean,double sig){
this(nm,mean,sig,new randUniform(1));
}

public pulseGenrRandSize(String nm,double mean,double sig,rand rinit){
super(nm,10,mean);
this.rinit = rinit;
}

public void initialize(){
   r = rinit;
   size = r.sample();
   holdIn("active",10);
   super.initialize();
 }

public void deltext(double e,message x){
Continue(e);
if (somethingOnPort(x,"start"))
  holdIn("active",10);
else if (somethingOnPort(x,"stop"))
  passivate();
}

public void   deltint(){
size = r.sample();
holdIn("active",10);
}

}
