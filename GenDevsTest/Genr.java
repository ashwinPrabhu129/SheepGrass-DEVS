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

public class Genr extends ViewableAtomic
{
    private int count;
    public Genr(String nm)
    {
        super(nm);
        addInport("start");
        addInport("stop");
        addOutport("out");
        addTestInput("start", new entity("start"), 0);
        addTestInput("start", new entity("start"), 2);
        addTestInput("stop", new entity("stop"), 0);
        addTestInput("stop", new entity("stop"), 4);
    }

    public Genr()
    {
        this("genr");
    }

    public void initialize()
    {
        super.initialize();
        count = 0;
        if (name.startsWith("first"))
            holdIn("active", 10);
        else passivate();
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
                    holdIn("active", 1);
            }
        }
    }

    public void deltint()
    {
        count++;
        if (phaseIs("active"))
            holdIn("active", 10);
    }

    public message out()
    {
        message m = new message();
        m.add(makeContent("out", new entity("job" + count)));
        return m;
    }

    public void showState()
    {
        super.showState();
        System.out.println(stringState());
    }

    public String stringState()
    {
        return "\n" + "count :" + count;
    }
}
