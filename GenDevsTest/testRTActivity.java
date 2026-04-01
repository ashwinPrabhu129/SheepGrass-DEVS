/*      Copyright 2002 Arizona Board of regents on behalf of 
 *                  The University of Arizona 
 *                     All Rights Reserved 
 *         (USE & RESTRICTION - Please read COPYRIGHT file) 
 * 
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */ 


package GenDevsTest;


import GenCol.*;

import java.util.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;

public class testRTActivity  extends testGeneral{
protected coupledRTSimulator as;
protected static ActModel g;


public testRTActivity(coupledRTSimulator as){
super("GenDevsTest.testRTActivity");
this.as = as;
}

public static void main (String[ ] args){

g = new ActModel("second",10);
//g = new actModel("first",10);
coupledRTSimulator as = new coupledRTSimulator(g);
testRTActivity t = new testRTActivity(as);
t.applyTests("activitySimulation of actModel is correct");

}

public boolean testSimulation(){
description = "simulation of activity";
precondition = new Boolean(true);
as.initialize();
as.simulate(1);      //consume 1*10 seconds
try{Thread.sleep(15000);}catch(Exception e){}
return g.toString().startsWith("phase :"+ "passive");
}


}