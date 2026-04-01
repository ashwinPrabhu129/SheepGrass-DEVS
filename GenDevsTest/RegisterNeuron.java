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

public class RegisterNeuron extends ViewableAtomic
{
    protected int count, predictedNum;
    protected double clock, criterionMean, estimatedMean, std, interval, threshold;
    public RegisterNeuron()
    {
        this("registerNeuron", 100, 30);
        addTestInput("in", new entity("pulse"), 0);
        addTestInput("in", new entity("pulse"), 1);
        addTestInput("none", new entity("pulse"), 10);
    }

    public RegisterNeuron(String  name, int predictedNum, double interval)
    {
        super(name);
        this.predictedNum = predictedNum;
        this.interval = interval;
        criterionMean = interval / predictedNum;
        addInport("in");
        addInport("none");
        addOutport("out");
    }

    public void reset()
    {
        std = criterionMean;
        clock = 0;
        threshold = 1;
        passivateIn("receptive");
    }

    public void initialize()
    {
        reset();
        super.initialize();
    }

    public void  deltext(double e, message x)
    {
        Continue(e);
        clock += e;
        if (phaseIs("receptive")) {
            for (int i = 0; i < x.getLength(); i++)
                if (messageOnPort(x, "in", i)) {
                    entity ent = x.getValOnPort("in", i);
                    count++;
                    estimatedMean = clock / count;
                    std = criterionMean / Math.sqrt(count);
                    if (std < threshold && Math.abs(estimatedMean - criterionMean) < 3 * std)
                        holdIn("fire", 0);
                }
        } else  for (int i = 0; i < x.getLength(); i++)
                if (messageOnPort(x, "reset", i))
                    initialize();
    }

    public void  deltint()
    {
        clock += sigma;
        threshold = threshold / 3;
        passivateIn("receptive");
        //passivate();
    }

    public message    out()
    {
        message   m = new message();
        if (phaseIs("fire")) {
            content con = makeContent("out", new entity("" + predictedNum));
            m.add(con);
        }
        return m;
    }

    public void showState()
    {
        super.showState();
        System.out.println(stringState());
    }

    public String stringState()
    {
        return "\n" + "predictedNum :" + predictedNum
            + "\n" + "interval :" + interval
            + "\n" + " criterionMean :" + criterionMean
            + "\n" + "clock :" + clock
            + "\n" + "count :" + count
            + "\n" + "std :" + std
            + "\n" + " estimatedMean :" + estimatedMean;
    }
}
