/*  Georgia State University
 *  CSC 8840 - Modeling and Simulation Theory and Application
 *  This java class is part of a class project that involves using particle filter 
 *  to carry out data assimilation for a DEVS-based discrete event simulation model
 *
 *  Author    : Prof. Xiaolin Hu
 *  Date      : 10-14-2024
 */


package PFDataAssimilation;

import GenCol.*;

public class sensorDataEntity extends entity{
  int step;
  int arrvCarCount;  
  int finishedCarCount; // this is the sensor data that we use in HW3
  
  public sensorDataEntity(){
	  this(0,0,0);
  }
  
  public sensorDataEntity(int stp, int arrivCar, int finishedCar){
	  super("sensorDataEntity");
	  step = stp;
	  arrvCarCount = arrivCar;
	  finishedCarCount = finishedCar;
  }
		
}
