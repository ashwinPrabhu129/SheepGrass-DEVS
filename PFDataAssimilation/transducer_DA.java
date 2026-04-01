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

public class transducer_DA extends  ViewableAtomic{ 
	double observationInterval = configData.stepInterval;
	double observationDuration = observationInterval;//observe for only 1 period

	int numOfarrivingcars, numOfFinishedCars;
	stateEntity stateEnt = null;
	double remainingObservationTime;
	 
 public transducer_DA(String  name){
  super(name);
  addInport("ariv");
  addInport("solved");
  addInport("report_in");
  addOutport("out");
  
 }

 public transducer_DA() {this("transd");}

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
				 numOfarrivingcars++;
			 }
			 else if(messageOnPort(x,"solved",i)){
				 numOfFinishedCars++;
			 }
			 else if (messageOnPort(x, "report_in", i)) {
				 stateEnt = (stateEntity)x.getValOnPort("report_in", i);
				 passivate();
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
