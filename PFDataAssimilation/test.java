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
import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;


public class test{

protected static digraph testDig;

  public test(){}

  public static void main(String[ ] args)
  {
      testDig = new carWashSys_Real();
      
      genDevs.simulation.coordinator cs = new genDevs.simulation.coordinator(testDig);

      cs.initialize();
//      cs.simulate(50000);
      cs.simulate_TN(configData.numberofsteps*configData.stepInterval); 
      System.out.println("simulation finished!");
  }
}
