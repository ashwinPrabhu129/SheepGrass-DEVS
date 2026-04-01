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
import simView.*;

public class ActModel extends ViewableAtomic
{
    public ActModel() {this("actModel", 8);}

    public ActModel(String nm, double processingTime)
    {
        super(nm);
        addInport("outputFromActivity");
    }

    public void initialize()
    {
        if (name.startsWith("first")) {
            a = new SimActivity("actModelActivity", 8);
            holdIn("active", a.getProcessingTime() + 2, a);
        } else passivate();
    }

    public void deltext(double e, message x)
    {
        Continue(e);
        for (int i = 0; i < x.getLength(); i++) {
            if (messageOnPort(x, "outputFromActivity", i)) {
                entity ent = x.getValOnPort("outputFromActivity", i);
                System.out.println("At elapse time:" + e + " received " + ent.getName());
                holdIn("send", 20);
            }
            if (messageOnPort(x, "in", i)) {
                entity ent = x.getValOnPort("in", i);
                System.out.println("At elapse time:" + e + " received " + ent.getName());
                a = new SimActivity("actModelActivity", 18);
                holdIn("active", a.getProcessingTime() + 2, a);
            }
        }
    }

    public void deltint()
    {
        if (phaseIs("active")) {
            System.out.println("too late deadline for activity passed");
            a.kill();
        }
        passivate();
    }

    public void deltcon(double e, message x)
    {
        deltext(e, x);
        deltint();
    }

    public message out()
    {
        message m = new message();
        if (phaseIs("send"))
            m.add(makeContent("out", new entity("job" + name)));
        return m;
    }
}
