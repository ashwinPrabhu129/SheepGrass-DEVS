package PFDataAssimilation;
/*  Georgia State University
 *  CSC 8840 - Modeling and Simulation Theory and Application
 *  This java class is part of a class project that involves using particle filter 
 *  to carry out data assimilation for a DEVS-based discrete event simulation model
 *
 *  Author    : Prof. Xiaolin Hu
 *  Date      : 10-14-2024
 */

import GenCol.*;
import genDevs.modeling.DevsInterface;

public class stateEntity extends entity{
  protected int carQueueSize;
  protected String workingState; 
  protected double sigma;
  protected double elapseTimeInPhase;
  
  public stateEntity(){
	  this(0, "working", DevsInterface.INFINITY, 0);
  }
  
  public stateEntity(int qSize, String phase, double sgm, double elapseTime){
	  super("stateEntity");
	  carQueueSize = qSize;
	  workingState = phase;
	  sigma = sgm;
	  elapseTimeInPhase = elapseTime;
  }
  
  public stateEntity copy() {
	  return new stateEntity(carQueueSize, workingState, sigma, elapseTimeInPhase);
  }
  
  public String toString(){
	  return carQueueSize+"_"+workingState
			  +"_"+sigma+"_"+elapseTimeInPhase;
//			  ((double)((int)(elapseTimeInPhase*1000)))/1000;
  }
		
}
