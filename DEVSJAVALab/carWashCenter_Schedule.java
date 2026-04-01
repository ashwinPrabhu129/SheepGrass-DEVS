/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */


package DEVSJAVALab;

import simView.*;


import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;

public class carWashCenter_Schedule extends ViewableAtomic{

double carServingTime = 20;
double truckServingTime =40;
double schedule_workingDuration = 100;
double schedule_breakDuration = 20;
double remaining_scheduleTime; 
entity car,currentJob = null;
protected DEVSQueue q;

public carWashCenter_Schedule() {this("carWashCenter_Schedule");}

public carWashCenter_Schedule(String name){
    super(name);
    addInport("carIn");
    addInport("truckIn");
    addOutport("out");

//    addTestInput("carIn",new entity("testCar"));
//    addTestInput("truckIn",new entity("testTruck"),5);

    addTestInput("carIn",new vehicleEntity("testCar", 10, 5, 1));
    addTestInput("truckIn",new vehicleEntity("testTruck", 20, 7, 1),5);
}

public void initialize(){
     q = new DEVSQueue();
     holdIn("working_passive", schedule_workingDuration);
     remaining_scheduleTime = schedule_workingDuration;
}


public void  deltext(double e,message x)
{
Continue(e);
remaining_scheduleTime = remaining_scheduleTime -e;

if(phaseIs("working_passive")){
   for (int i=0; i< x.getLength();i++){  // assume only one car or truck arrives at one time
     if (messageOnPort(x, "carIn", i)) {
       car = x.getValOnPort("carIn", i);
       currentJob = car;
       holdIn("working_active", ((vehicleEntity)currentJob).getProcessingTime());
     }
     else if (messageOnPort(x, "truckIn", i)) {
       car = x.getValOnPort("truckIn", i);
       currentJob = car;
       holdIn("working_active", ((vehicleEntity)currentJob).getProcessingTime());
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
	remaining_scheduleTime = remaining_scheduleTime -sigma;
	if(phaseIs("working_active")){
		if(remaining_scheduleTime>0){	// working time is not finished
			if(!q.isEmpty()) {
				currentJob = (entity)q.first();
				holdIn("working_active", ((vehicleEntity)currentJob).getProcessingTime());
				q.remove();
			}
			else
				holdIn("working_passive", remaining_scheduleTime);
		}
		else{ // working time is finished
			holdIn("break", schedule_breakDuration);
			remaining_scheduleTime = schedule_breakDuration; // reset the remaining_scheduleTime
		}
	}
	else if(phaseIs("working_passive")){  // working time is finished
		holdIn("break", schedule_breakDuration);
		remaining_scheduleTime = schedule_breakDuration; // reset the remaining_scheduleTime		
	}
	else if(phaseIs("break")){ // break time is finished
		remaining_scheduleTime = schedule_workingDuration; // reset the remaining_scheduleTime		
		if(!q.isEmpty()) {
			currentJob = (entity)q.first();
			holdIn("working_active", ((vehicleEntity)currentJob).getProcessingTime());
			q.remove();
		}
		else
			holdIn("working_passive", remaining_scheduleTime);
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
	return super.getTooltipText()+"\n number of cars in queue:"+q.size()+
	"\n my current job is:" + currentJob.toString();
	else return "simulation has not started!";
}



}

