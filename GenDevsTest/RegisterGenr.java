/*      Copyright 2002 Arizona Board of regents on behalf of 
 *                  The University of Arizona 
 *                     All Rights Reserved 
 *         (USE & RESTRICTION - Please read COPYRIGHT file) 
 * 
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */ 


package GenDevsTest;

import java.awt.Color;
import GenCol.*;
import java.util.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import simView.*;

public class RegisterGenr extends ViewableAtomic
{
    protected int totalNum;
    protected double interval, remainInterval, meanInter;
    protected Random r;
    private int count;
    public RegisterGenr(String nm, long seed, int totalNum, double interval)
    {
        super("genr" + nm); //need unique name for coupling to work
        r = new Random(seed);
        this.totalNum = totalNum;
        this.interval = interval;
        remainInterval = interval;
        meanInter = interval / totalNum;
        addInport("start");
        addInport("stop");
        addInport("none");
        addOutport("out");
        addOutport("outStart");
        addTestInput("start", new entity("start"), 0);
        addTestInput("start", new entity("start"), 2);
        addTestInput("stop", new entity("stop"), 0);
        addTestInput("stop", new entity("stop"), 4);
        addTestInput("none", new entity(), 10);
    }

    public RegisterGenr()
    {
        this("genr", 1, 200, 30);
    }

    public double expon()
    {
        double u = r.nextDouble();
        return -meanInter * Math.log(u);
    }

    public double exponWithin()
    {
        double projected = 0;
        while (true) {
            projected = expon();
            if (projected <= remainInterval)break;
        }
        remainInterval -= projected;
        return projected;
    }

    public void initialize()
    {
        super.initialize();
        count = 0;
        //holdIn("active",exponWithin());
        passivate();
    }

    public void deltext(double e, message x)
    {
        Continue(e);
        for (int i = 0; i < x.getLength(); i++)
            if (messageOnPort(x, "stop", i)) {
                entity ent = x.getValOnPort("stop", i);
                if (phaseIs("active"))
                    passivate();
            }
        ContentIteratorInterface cit = x.mIterator();
        while (cit.hasNext()) {
            content c = (content) cit.next();
            if (messageOnPort(x, new port("start"), c)) {
                entity ent = (entity) x.getValOnPort("start", c);
                if (phaseIs("passive"))
                    holdIn("active", exponWithin());
            }
        }
    }

    public void deltint()
    {
        count++;
        if (phaseIs("active")) {
            if (count < totalNum)
                holdIn("active", exponWithin());
            else holdIn("final", 0);
        } else passivate();
    }

    public message out()
    {
        message m = new message();
        m.add(makeContent("out", new entity("job" + count)));
        if (phaseIs("final"))
            m.add(makeContent("outStart", new entity("start")));
        return m;
    }

    public void showState()
    {
        super.showState();
        System.out.println(stringState());
    }

    public String stringState()
    {
        return "\n" + "count :" + count
            + "\n" + "totalNum :" + totalNum
            + "\n" + "interval :" + interval
            + "\n" + " meanInter :" + meanInter;
    }
}
