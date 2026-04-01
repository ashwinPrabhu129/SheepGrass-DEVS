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

public class testRTAtomic  extends testGeneral{
protected atomicRTSimulator as;
protected static Genr g;


public testRTAtomic(atomicRTSimulator as){
super("GenDevsTest.testRTAtomic");
this.as = as;
}

public static void main (String[ ] args){

g = new Genr("first");
//g = new Genr("second");
atomicRTSimulator as = new atomicRTSimulator(g);
testRTAtomic t = new testRTAtomic(as);
t.applyTests("RTSimulation of genr is correct");

}
public boolean testInitActive(){
description = "initialization to active";
precondition = new Boolean(g.getName().equals("first"));
as.initialize();
String ts = "phase :"+ "active" + " sigma : " + "10.0" ;
return ts.equals(g.toString());
}

public boolean testSimulation(){
description = "simulation retains phase active";
precondition = new Boolean(g.toString().startsWith("phase :"+ "active"));
as.simulate(20);      //consume 2*10 seconds
try{Thread.sleep(20500);}catch(Exception e){}
return g.toString().startsWith("phase :"+ "active");
}


/*
public boolean testInitialPassive(){
description = "initialization to passive";
precondition = new Boolean(g.getName().equals("second"));
as.initialize();
String ts = "phase :"+ "passive" + " sigma : " + "Infinity" ;
return ts.equals(g.toString());
}
*/

public boolean testInjectStop(){
description = "injection of Stop induces phase passive";
precondition = new Boolean(g.toString().startsWith("phase :"+ "active"));
as.simInject(1.0,"stop",new entity());
try{Thread.sleep(2000);}catch(Exception e){}
return g.toString().startsWith("phase :"+ "passive");
}


public boolean testInjectStart(){
description = "injection of Start induces phase active";
String ts = "phase :"+ "passive" + " sigma : " + "Infinity" ;
precondition = new Boolean( ts.equals(g.toString()));
as.simInject(2.0,"start",new entity());
try{Thread.sleep(3000);}catch(Exception e){}
return g.toString().startsWith("phase :"+ "active");
}


}


