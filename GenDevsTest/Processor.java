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
import genDevs.modeling.*;
import genDevs.simulation.*;
import simView.*;

public class Processor //extends ViewableAtomic
{
/*
    protected entity job;
    protected double processingTime;

    public Processor()
    {
        super("proc");

        addInport("in");

        // this allows testing for null input which should
        // cause only "continue"
        addInport("none");

        addOutport("out");

        processingTime = 20;

        addTestPortValue("in", new entity("job1"));
        addTestPortValue("in", new entity("job2"));
        addTestPortValue("none", new entity("job"));
    }

    public Processor(String name, double processingTime_)
    {
        super(name);

        processingTime = processingTime_;

        addInport("in");
        addOutport("out");
    }

    public void initialize()
    {
        phase = "passive";
        sigma = INFINITY;
        job = new entity("job");
        super.initialize();
    }

    public void deltext(double e, message x)
    {
        Continue(e);
        if (phaseIs("passive"))
            for (int i = 0; i < x.get_length(); i++)
                if (messageOnPort(x, "in", i)) {
                    job = x.getValOnPort("in", i);
                    holdIn("busy", processing_time);
                }
    }

    public void deltint()
    {
        passivate();
        job = new entity("none");
    }

    public void deltcon(double e, message x)
    {
        deltint();
        deltext(0, x);
    }

    public message out()
    {
        message m = new message();

        if (phaseIs("busy")) {
            m.add(makeContent("out", job));
        }

        return m;
    }

    public void showState()
    {
        super.showState();
        System.out.println("job: " + job.get_name());
    }
*/
}
