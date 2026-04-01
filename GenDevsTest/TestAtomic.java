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

public class TestAtomic extends testGeneral
{
    protected atomicSimulator as;
    protected static Genr g;
    public TestAtomic(atomicSimulator as)
    {
        super("GenDevsTest.testAtomic");
        this.as = as;
    }

    public static void main(String[ ] args)
    {
        //g = new genr();
        g = new NewGenr();
        atomicSimulator as = new atomicSimulator(g);
        TestAtomic t = new TestAtomic(as);
        t.applyTests("simulation of genr is correct");
    }

    public boolean testInitial()
    {
        description = "initialization";
        precondition = Boolean.TRUE;
        as.initialize();
        String ts = "phase :" + "passive" + " sigma : " + "Infinity";
        return ts.equals(g.toString());
    }

    public boolean testInjectStart()
    {
        description = "injection of Start induces phase active";
        String ts = "phase :" + "passive" + " sigma : " + "Infinity";
        precondition = new Boolean(ts.equals(g.toString()));
        as.simInject(13.0, "start", new entity());
        return g.toString().startsWith("phase :" + "active");
    }

    public boolean testSimulation()
    {
        description = "simulation retains phase active";
        precondition = new Boolean(g.toString().startsWith("phase :" + "active"));
        as.simulate(3);
        return g.toString().startsWith("phase :" + "active");
    }

    public boolean testInjectStop()
    {
        description = "injection of Stop induces phase passive";
        String ts = "phase :" + "passive" + " sigma : " + "Infinity";
        precondition = new Boolean(g.toString().startsWith("phase :" + "active"));
        as.simInject(0.0, "stop", new entity());
        return g.toString().startsWith("phase :" + "passive");
    }

    public boolean testInjectStopStart()
    {
        description = "simultaneous Stop and Start induces phase active";
        String ts = "phase :" + "passive" + " sigma : " + "Infinity";
        precondition = Boolean.TRUE;
        message m = new message();
        m.add(new content("start", new entity()));
        m.add(new content("stop", new entity()));
        as.simInject(0.0, m);
        return g.toString().startsWith("phase :" + "active");
    }
}
