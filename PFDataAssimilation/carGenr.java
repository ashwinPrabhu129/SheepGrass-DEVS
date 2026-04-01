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

public class carGenr extends ViewableAtomic{

  protected double int_gen_time;
  protected int count;
  protected Random ran;
  double nextCarTime_Lambda = 1/6.0; // 1 car per 6 seconds 

  public carGenr() {this("carGenr");}

  public carGenr(String name){
	  this(name, new Random(configData.randSeed_carGenerator));
  }

//The following constructor is used by the PF algorithm
public carGenr(String name, Random rd){
   super(name);
   addInport("in");
   addOutport("out");   
   addTestInput("in",new entity("an entity"),7);
   
   ran = rd;
   nextCarTime_Lambda = configData.carGenerator_lambda;
}

public void initialize(){
	int_gen_time = getNextCarTime(nextCarTime_Lambda);
	holdIn("active", int_gen_time);
	count = 0;
}


public void  deltext(double e,message x){
Continue(e);
   for (int i=0; i< x.getLength();i++){
     if (messageOnPort(x, "in", i)) { //the stop message from tranducer
       passivate();
     }
   }
}

public void  deltint( ){
if(phaseIs("active")){
	count = count +1;
	int_gen_time = getNextCarTime(nextCarTime_Lambda);
	holdIn("active", int_gen_time);
}
else passivate();
}

public message  out( ){
//System.out.println(name+" out count "+count);
   message  m = new message();
   content con = makeContent("out", new entity("car_" + count));
   m.add(con);

  return m;
}

private double getNextCarTime(double Lambda) {
	return getPoissonNextTime(Lambda);
}

//The following is taken from 
//https://preshing.com/20111007/how-to-generate-random-timings-for-a-poisson-process/
private double getPoissonNextTime(double lambda) {
	double u = ran.nextDouble();
	double nextTime = -Math.log(u) / lambda;
	
	return nextTime;
}


}

