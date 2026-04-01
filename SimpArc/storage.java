/*      Copyright 2002 Arizona Board of regents on behalf of 
 *                  The University of Arizona 
 *                     All Rights Reserved 
 *         (USE & RESTRICTION - Please read COPYRIGHT file) 
 * 
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */ 


package SimpArc;

import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;

public class storage extends siso{
protected double store;
protected double response_time;

  public storage(){
    super("storage");
AddTestPortValue(1);
AddTestPortValue(2);
}

public storage(String name,double Response_time){
   super(name);
   response_time = Response_time;
}

public void initialize(){
     phase = "passive";
     sigma = INFINITY;
     store = 0;
     response_time = 10;
     super.initialize();
 }


public void  Deltext(double e,double input){
    Continue(e);
    if (input != 0)  // 0 is query
    store = input;
    else holdIn("respond", response_time);
}



public void  deltint( ){
    passivate();
}


public double sisoOut(){
  if (phaseIs("respond"))  return store;
  else return 0;
}

 public void showState(){
  super.showState();
  System.out.println("store: " + store);
 }



}

