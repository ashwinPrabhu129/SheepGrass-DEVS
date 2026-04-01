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
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.io.*;

public class transducer_Real_dataSaving extends  ViewableAtomic{ 
	double observationInterval = configData.stepInterval;
	double observationDuration = configData.numberofsteps*observationInterval;
	PrintWriter arrivedCar_input, finishedCar_output;
	PrintWriter observationData;

	int numOfarrivingcars, numOfFinishedCars;
	double remainingObservationTime;
	
	//record individual car's arriving time so that the waiting time can be computed
	double[] carArrivingTime = new double[5000];
 
 
 public transducer_Real_dataSaving(String  name){
  super(name);
  addInport("ariv");
  addInport("solved");
  addInport("report_in");
  addOutport("out");
  addTestInput("ariv",new entity("val"));
  addTestInput("solved",new entity("val"));
  
	try{
		arrivedCar_input = new PrintWriter(new FileOutputStream(configData.DataPathName+"arrivedCar_input.txt"), true);
		finishedCar_output = new PrintWriter(new FileOutputStream(configData.DataPathName+"finishedCar_output.txt"), true);
		observationData = new PrintWriter(new FileOutputStream(configData.DataPathName+"observationData.txt"), true);
	}
	catch (FileNotFoundException e) {System.out.println("File creation error!!!!!");}

	arrivedCar_input.println("Time"+"\t"+"eventName"+"\t"+"event");
	finishedCar_output.println("Time"+"\t"+"eventName"+"\t"+"event"+"\t"+"eventArrivingTime"+"\t"+"waitingTime");
	observationData.println("Time"
			+"\t"+"arrvCarCount_noise"
			+"\t"+"finishedCarCount_noise"
			+"\t"+"carQueue"
			+"\t"+"workingState"
			+"\t"+"phaseSigma"
			+"\t"+"elapseTimeInWorkingOrBreak"
			+"\t"+"arrvCarCount_noNoise"
			+"\t"+"finishedCarCount_noNoise"
			);
 }

 public transducer_Real_dataSaving() {this("transd");}

 public void initialize(){
		remainingObservationTime=observationDuration;
		numOfarrivingcars=0;
		numOfFinishedCars=0;
		holdIn("active", observationInterval);
 }


 public void  deltext(double e,message  x){
	 Continue(e);
	 remainingObservationTime -=e;

	 entity  val;
	 if(!phaseIs("passive")) {
		 for(int i=0; i< x.size();i++){
			 if(messageOnPort(x,"ariv",i)){
				 val = x.getValOnPort("ariv",i);
				 int index = Integer.valueOf(val.getName().substring(4,val.getName().length()));
				 carArrivingTime[index]=this.getSimulationTime();
				 numOfarrivingcars++;
				 arrivedCar_input.println(this.getSimulationTime()+"\t"+val.getName()+"\t"+1); // use 1 to represent an event
			 }
			 if(messageOnPort(x,"solved",i)){
				 val = x.getValOnPort("solved",i);
				 numOfFinishedCars++;
				 int index = Integer.valueOf(val.getName().substring(4,val.getName().length()));
				 double processingTime = this.getSimulationTime()-carArrivingTime[index];
				 finishedCar_output.println(this.getSimulationTime()+"\t"+val.getName()+"\t"+1
						 +"\t"+carArrivingTime[index]+"\t"+processingTime); // use 1 to represent an event
			 }
			 else if (messageOnPort(x, "report_in", i)) {
				 stateEntity ent = (stateEntity)x.getValOnPort("report_in", i);

				 // add noise to the observations
				 // For now, let's assume the observation data has noise

				 observationData.println(this.getSimulationTime()
						 +"\t"+numOfarrivingcars
						 +"\t"+numOfFinishedCars
						 +"\t"+ent.carQueueSize
						 +"\t"+((ent.workingState.startsWith("working"))? 20: 0)
						 +"\t"+ent.sigma							
						 +"\t"+ent.elapseTimeInPhase
						 +"\t"+numOfarrivingcars
						 +"\t"+numOfFinishedCars
						 );

				 //reset the count values
				 numOfarrivingcars=0;
				 numOfFinishedCars=0;

				 //System.out.println("remainingObservationTime: "+remainingObservationTime);
				 double randomNumPrecisionBuffer = 0.0000001; // Note: this is a temporary solution to handle the double variable precision issue
				 if(remainingObservationTime<=0) {
					 System.out.println("Finishing Saving Data");
					 passivate();
					 arrivedCar_input.close();
					 finishedCar_output.close();
					 observationData.close();
				 }
				 else
					 holdIn("active", observationInterval);
			 }
		 }		 
	 }
 }

 public void  deltint(){
	 remainingObservationTime-=sigma;
	 passivateIn("waitForReport");
 }

 public  message    out( ){
	 message  m = new message();
	 if(phaseIs("active")) {
		 content con = makeContent("out", new entity("report"));
		 m.add(con);
	 }
	 return m;
 }
 
 public String getTooltipText(){
		return super.getTooltipText()
				+"\n numOfarrivingcars:"+numOfarrivingcars
				+"\n numOfFinishedCars:"+numOfFinishedCars;
	}
}
