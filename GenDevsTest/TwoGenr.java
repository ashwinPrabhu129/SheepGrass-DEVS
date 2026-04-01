/*      Copyright 2002 Arizona Board of regents on behalf of 
 *                  The University of Arizona 
 *                     All Rights Reserved 
 *         (USE & RESTRICTION - Please read COPYRIGHT file) 
 * 
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */ 

package GenDevsTest;

import java.awt.*;
import java.util.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import simView.*;

public class TwoGenr extends ViewableDigraph
{
    public TwoGenr()
    {
        super("TwoGenr");

        // add a digraph, to make this hierarchical
        SubTwoGenr two = new SubTwoGenr();
        two.setPreferredLocation(new Point(10, 120));
        add(two);

        Genr first = new Genr("first");
        first.setPreferredLocation(new Point(20, 20));
        add(first);

        Genr second = new Genr("second");
        second.setPreferredLocation(new Point(250, 20));
        add(second);

        addCoupling(first, "out", two, "in");
        addCoupling(two, "out", second, "start");
        addCoupling(second, "out", first, "stop");
    }

    public static void main(String[] args)
    {
        coordinator c = new coordinator(new TwoGenr());
        c.initialize();
        c.simulate(1000000);
    }

    protected class SubTwoGenr extends ViewableDigraph
    {
        public SubTwoGenr()
        {
            super("SubTwoGenr");

            preferredSize = new Dimension(472, 90);

            addInport("in");
            addOutport("out");

            Genr first = new Genr("SubTwoGenrA");
            first.setPreferredLocation(new Point(10, 20));
            add(first);

            Genr second = new Genr("SubTwoGenrB");
            second.setPreferredLocation(new Point(200, 20));
            add(second);

            addCoupling(this, "in", first, "start");
            addCoupling(first, "out", second, "start");
            addCoupling(second, "out", this, "out");
        }
    }
}

