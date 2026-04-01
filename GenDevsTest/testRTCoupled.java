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

public class testRTCoupled  extends testGeneral{
protected RTcoordinator cs;


public testRTCoupled(RTcoordinator cs){
super("GenDevsTest.testRTCoupled");
this.cs = cs;
}

public static void main (String[ ] args){

//coordinator cs = new coordinator(new gg());
RTcoordinator cs = new RTcoordinator(new ActCoupledModel());
//testRTCoupled t = new testRTCoupled(cs);
/**/
message m = new message();
m.add(new content("in",new entity()));

cs.simInject(5,m);

cs.initialize();
//cs.simulate(5,90000);
cs.simulate(1000000);
/*
// use centralized coordinator  --- only one thread
RTCentralCoord cs = new RTCentralCoord(new gg());
cs.initialize();
cs.simulate(8);
*/
}

}

