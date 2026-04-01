/*  Georgia State University
 *  CSC 8840 - Modeling and Simulation Theory and Application
 *  This java class is part of a class project that involves using particle filter 
 *  to carry out data assimilation for a DEVS-based discrete event simulation model
 *
 *  Author    : Prof. Xiaolin Hu
 *  Date      : 10-14-2024
 */

package PFDataAssimilation;

import simView.*;


import java.lang.*;
import java.util.Random;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;


public class carWashCenter_Schedule extends ViewableAtomic{

double carServingTime;
double carServingTime_mean, carServingTime_sigma;
double workingDuration, breakDuration;
double elapsedTimeInPhase; 
entity car,currentJob = null;
protected DEVSQueue q;
protected Random ran;

public carWashCenter_Schedule() {this("carWashCenter_Schedule");}

public carWashCenter_Schedule(String name){
    super(name);
    addInport("carIn");
    addOutport("out");
    addTestInput("carIn",new entity("testCar"));
    
    carServingTime_mean = configData.carServingTime_mean;
    carServingTime_sigma = configData.carServingTime_sigma;
    workingDuration = configData.schedule_workingDuration;
    breakDuration = configData.schedule_breakDuration;
    
    ran = new Random(configData.randSeed_carWashCenter);
}

public void initialize(){
     q = new DEVSQueue();
     holdIn("working_passive", workingDuration);
     elapsedTimeInPhase = 0;
}


public void  deltext(double e,message x)
{
Continue(e);
elapsedTimeInPhase += e;

if(phaseIs("working_passive")){
   for (int i=0; i< x.getLength();i++){  // assume only one car or truck arrives at one time
     if (messageOnPort(x, "carIn", i)) {
       car = x.getValOnPort("carIn", i);
       currentJob = car;
       carServingTime = getCarWashTime();
       holdIn("working_active", carServingTime);
     }
   }
}
 else { // phase is working_active, or break
    for (int i=0; i< x.getLength();i++){
      if (messageOnPort(x, "carIn", i)) {
        car = x.getValOnPort("carIn", i);
        q.add(car);
      }
    }
 }
}


public void  deltint( ){
	elapsedTimeInPhase += sigma;
	if(phaseIs("working_active")){
		if(elapsedTimeInPhase < workingDuration){	// working time is not finished
			if(!q.isEmpty()) {
				currentJob = (entity)q.first();
			       carServingTime = getCarWashTime();
				holdIn("working_active", carServingTime);
				q.remove();
			}
			else
				holdIn("working_passive", (workingDuration-elapsedTimeInPhase));
		}
		else{ // working time is finished
			holdIn("break", breakDuration);
			elapsedTimeInPhase = 0; // reset the elapsedTimeInPhase
		}
	}
	else if(phaseIs("working_passive")){  // working time is finished
		holdIn("break", breakDuration);
		elapsedTimeInPhase = 0; // reset the elapsedTimeInPhase		
	}
	else if(phaseIs("break")){ // break time is finished
		elapsedTimeInPhase = 0; // reset the elapsedTimeInPhase		
		if(!q.isEmpty()) {
			currentJob = (entity)q.first();
		       carServingTime = getCarWashTime();
			holdIn("working_active", carServingTime);
			q.remove();
		}
		else
			holdIn("working_passive", workingDuration);
	}
}

public message  out( )
{
	message  m = new message();
	if(phaseIs("working_active")){
		content con = makeContent("out",
				new entity(currentJob.getName()));
		m.add(con);
	}

	return m;

}

public String getTooltipText(){
	if(currentJob != null)
	return super.getTooltipText()
			+"\n elapsed time in phase:"+((double)((int)(elapsedTimeInPhase*100)))/100
			+"\n number of cars in queue:"+q.size()
			+"\n my current job is:" + currentJob.toString();
	else return "simulation has not started!";
}

private double getCarWashTime() {
	double carWashTime;
	double carServingTime_LowBound = carServingTime_mean-configData.carServingTime_rangeDelta; // 3 seconds ---- this is used if treating car passing as a truncated normal distribution between low and high
	double carServingTime_UpBound = carServingTime_mean+configData.carServingTime_rangeDelta; // 7 seconds ---- this is used if treating car passing as a truncated normal distribution between low and high
	carWashTime = getTruncatedNormalDistributionRate(carServingTime_LowBound, carServingTime_UpBound,
			carServingTime_mean, carServingTime_sigma);
	
	return carWashTime;
}

private double getTruncatedNormalDistributionRate(double low, double high, double mean, double sigma) {
	double u = mean + sigma * ran.nextGaussian();
	while (u<low || u > high) // resample if out of bounds 
		u= mean + sigma * ran.nextGaussian();
	return u;
}

}

