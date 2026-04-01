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


public class carWashCenter_VaryingSchedule extends ViewableAtomic{

double carServingTime;
double carServingTime_mean, carServingTime_sigma;
double workingDuration, workingDuration_emptyQueue, breakDuration;
double elapsedTimeInPhase; 
entity car,currentJob = null;
protected DEVSQueue q;
protected Random ran;

// variables for storing the initial state of a model
String initState;
double initSigma;

//the following two variables are used to restore the phase and sigma after reporting
String phaseBeforeReporting;
double sigmaBeforeReporting;


public carWashCenter_VaryingSchedule() {this("carWashCenter_Schedule");}

public carWashCenter_VaryingSchedule(String name){
	this(name, 0, "working_passive", configData.schedule_workingDuration_emptyQueue, 0, new Random(configData.randSeed_carWashCenter));
}

// The following constructor is used by the PF algorithm
public carWashCenter_VaryingSchedule(String name, int QSize, String State, double Sigma, double initElapsedTimeInPhase, Random rd){
    super(name);
    addInport("carIn");
    addOutport("out");
    addInport("report");
    addOutport("report_out");
    
    addTestInput("carIn",new entity("testCar"));
    
    currentJob= new entity("init job");
    
    initState = State;
    initSigma = Sigma;
    elapsedTimeInPhase = initElapsedTimeInPhase;
    
    carServingTime_mean = configData.carServingTime_mean;
    carServingTime_sigma = configData.carServingTime_sigma;
    workingDuration = configData.schedule_workingDuration;
    workingDuration_emptyQueue = configData.schedule_workingDuration_emptyQueue;
    breakDuration = configData.schedule_breakDuration;
    
    ran = rd;

    q = new DEVSQueue();
    for(int i=0; i< QSize; i++) {
    		q.add(new entity("ICar"+i));
    }
}

public void initialize(){
     holdIn(initState, initSigma); // the initState and initSigma are defined in the constructor method
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
	
	// handle the report message separately, after everything else in the message bag has been handled
	for (int i = 0; i < x.getLength(); i++) {
		if (messageOnPort(x, "report", i)) {
			phaseBeforeReporting = this.getPhase();
			sigmaBeforeReporting = this.getSigma();
			holdIn("report",0);
		}
	}
	
}


public void  deltint( ){
	elapsedTimeInPhase += sigma;
	if(phaseIs("working_active")){
		//System.out.println("Time:"+this.getSimulationTime()+" Queue size:"+q.size());
		if(elapsedTimeInPhase < workingDuration){	// the default working time is not finished
			if(!q.isEmpty()) { // queue is not empty
				currentJob = (entity)q.first();
				carServingTime = getCarWashTime();
				holdIn("working_active", carServingTime);
				q.remove();
			}
			else {  // queue is empty
				if(elapsedTimeInPhase >= workingDuration_emptyQueue) { // having reached the workingDuration_emptyQueue threshold
					holdIn("break", breakDuration);
					elapsedTimeInPhase = 0; // reset the elapsedTimeInPhase
				}
				else  // not reach the workingDuration_emptyQueue threshold and have no car to wash
					holdIn("working_passive", (workingDuration_emptyQueue-elapsedTimeInPhase));
			}
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
			holdIn("working_passive", workingDuration_emptyQueue);
	}
	else if(phaseIs("report")) {
		holdIn(phaseBeforeReporting, sigmaBeforeReporting);
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
	else if (phaseIs("report")) {
		m.add(makeContent("report_out", new stateEntity(q.size(),
				phaseBeforeReporting, sigmaBeforeReporting, elapsedTimeInPhase)));
	}
	
	//

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
	
	// The following code makes the mean car serving time related to the queue size
	//if(q.size()<5)
	//	carServingTime_mean=configData.carServingTime_mean+1;
	//else if(q.size()<10)
	//	carServingTime_mean=configData.carServingTime_mean+0.5;
	//else if(q.size()<15)
	//	carServingTime_mean=configData.carServingTime_mean;
	//else if(q.size()<20)
	//	carServingTime_mean=configData.carServingTime_mean-0.5;
	//else
	//	carServingTime_mean=configData.carServingTime_mean-1;
	
	
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

