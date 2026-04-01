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

public class TestCoupled extends testGeneral
{
    protected coordinator cs;
    protected static digraph testDig;
    public TestCoupled(coordinator cs)
    {
        super("GenDevsTest.testCoupled");
        this.cs = cs;
    }

    public static void main(String[ ] args)
    {
        //testDig = new gg();
        testDig = new HierarCoupledModel2();
        coordinator cs = new coordinator(testDig);
        TestCoupled t = new TestCoupled(cs);
        t.applyTests("Coupled");
        cs.initialize();
        message m = new message();
        m.add(new content("in", new entity()));
        cs.simInject(8.0, m);
        cs.simulate(5);
    }
    /*
     public boolean testWithName(){
     description = "withName";
     precondition = Boolean.TRUE;
     digraph d = new gg();
     IODevs iod  = d.withName("first");
     return iod.getName().equals("first");
     }

     public boolean testInit(){
     description = "initialization of GG is correct";
     precondition = Boolean.TRUE;
     String tpas = "phase :"+ "passive" ;
     String tact = "phase :"+ "active" ;
     cs.initialize();
     String cstate = cs.toString();
     return cstate.indexOf(tpas) != -1 && cstate.indexOf(tact) != -1;
     }

     public boolean testSim(){
     description = "simulation of GG is correct";
     precondition = Boolean.TRUE;
     //cs.initialize();
     String tpas = "phase :"+ "passive" ;
     String tact = "phase :"+ "active" ;
     cs.simulate(5);
     String cstate = cs.toString();
     return cstate.indexOf(tpas) != -1 && cstate.indexOf(tact) != -1;
     }
     public boolean testActivy(){
     description = "simulation of Activity is correct";
     precondition = Boolean.TRUE;
     actModel m = new actModel("actModel",10);
     testDig.add(m);
     cs.addSimulator(m);
     cs.initialize();
     String tpas = "phase :"+ "passive" ;
     String tact = "phase :"+ "active" ;
     cs.simulate(3);
     String cstate = cs.toString();
     return cstate.indexOf(tpas) != -1 && cstate.indexOf(tact) != -1;
     }
     /**/
}
