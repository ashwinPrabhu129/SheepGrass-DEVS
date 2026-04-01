/*      Copyright 2002 Arizona Board of regents on behalf of 
 *                  The University of Arizona 
 *                     All Rights Reserved 
 *         (USE & RESTRICTION - Please read COPYRIGHT file) 
 * 
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */ 


package GenDevsTest;

import util.*;
import GenCol.*;
import java.util.*;
import java.awt.Color;
import genDevs.modeling.*;
import genDevs.simulation.*;
import simView.*;

public class HierarAtomicModel extends ViewableAtomic
{
    protected entity source;
    public HierarAtomicModel(String nm)
    {
        super(nm);
        source = new entity("none");
        addInport("start");
        addOutport("out");
        addOutport("outDraw");
        addTestInput("start", new entity(nm), 0);
        addTestInput("start", new entity(nm), 2);
    }

    public HierarAtomicModel()
    {
        this("HierarAtomicModel");
    }

    public void initialize()
    {
        //if (name.startsWith("third")||name.startsWith("second"))
        if (name.startsWith("first"))
            holdIn("active", 1);
        else passivate();
        //passivate();
    }

    public void deltext(double e, message x)
    {
        Continue(e);
        ContentIteratorInterface cit = x.mIterator();
        while (cit.hasNext()) {
            content c = (content) cit.next();
            if (messageOnPort(x, new port("start"), c)) {
                source = (entity) x.getValOnPort("start", c);
                System.out.println(" " + this.getName() + " received input from " + source.getName() + " on start port!");
                if (phaseIs("passive"))
                    holdIn("active", 4);
            }
        }
    }

    public void deltint()
    {
        if (phaseIs("active"))
            passivate();
    }

    public message out()
    {
        message m = new message();
        m.add(makeContent("out", new entity(getName())));
        return m;
    }

    public void showState()
    {
        super.showState();
    }

    public String stringState()
    {
        return
            "\n" + "source :" + source.getName();
    }
}
