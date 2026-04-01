/*  Georgia State University
 *  CSC 8840 - Modeling and Simulation Theory and Application
 *  This java class is part of a class project that involves using particle filter 
 *  to carry out data assimilation for a DEVS-based discrete event simulation model
 *
 *  Author    : Prof. Xiaolin Hu
 *  Date      : 10-14-2024
 */

package PFDataAssimilation;

public interface configData {
	
	//////////////////////////////////////////////////////
	// ---- Model configure data
	//////////////////////////////////////////////////////
	
	double carServingTime_mean = 5.0;
	double carServingTime_sigma = 1.0;
	double carServingTime_rangeDelta = 2.0; //---- this is used if treating car washing time as a truncated normal distribution between low and high
	
	double schedule_workingDuration = 180;
	double schedule_workingDuration_emptyQueue = 120;
	double schedule_breakDuration = 40;
	
    /**
     * For car generator 
     * lambda = 1/6.0 ----> queue increases over time and unlikely to go back to zero
     * lambda = 1/6.5 ----> queue seems to be balanced but can be large 
     * lambda = 1/7.0 ----> queue seems to be balanced 
     * lambda = 1/7.5 ----> queue is empty frequently 
     */    
	
//	/**
//	 * Case 1 carGenerator_lambda and random number seed
//	 */
//	double carGenerator_lambda = 1.0/7.0;
//	long randSeed_carGenerator = 3997;
//	long randSeed_carWashCenter = 311149;
	
	/**
	 * Case 2 carGenerator_lambda and random number seed
	 */
	double carGenerator_lambda = 1.0/6.5;
	long randSeed_carGenerator = 833997;
	long randSeed_carWashCenter = 2311149;
	

	//////////////////////////////////////////////////////
	// ---- Particle Filter configure data
	//////////////////////////////////////////////////////
	public static String DataPathName = "PF_carWashCenter/";
	public String realSensorDataFileName = "observationData.txt";
	
	int numberofparticles = 2000;
	int numberofsteps = 400;//500;//200;
	double stepInterval = 30; // every 30 seconds
	
	long PF_globalRandSeed = 98392824;
	
	
}
