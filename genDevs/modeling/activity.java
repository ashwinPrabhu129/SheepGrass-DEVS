package genDevs.modeling;

import GenCol.*;
import genDevs.simulation.*;
import java.util.*;

public class activity extends Thread implements ActivityInterface {

protected double processingTime;

protected CoupledSimulatorInterface sim;

public activity(String name){
 super(name);
 processingTime = 10;

 }

public activity(String name, double pt){
this(name);
processingTime = pt;
}

public void setSimulator(CoupledSimulatorInterface sim){
this.sim = sim;
}

public void returnTheResult(entity myresult) {
System.out.println("return result in Activity");
 sim.returnResultFromActivity(myresult);
}

public double getProcessingTime() {
    return processingTime;
}
/*
public void run(){
   try {
 sleep((long)getProcessingTime()*1000);
} catch (InterruptedException e) {return;}
returnTheResult();
}
*/
public entity computeResult(){
return  new entity(getName() + " --activity result");
}

public void kill(){
interrupt();
}


}