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

public class NewGenr extends Genr
{
    public NewGenr(String nm)
    {
        super(nm);
    }

    public NewGenr()
    {
        super("newGenr");
    }

    public void deltext(double e, message x)
    {
        Continue(e);
        ensembleBag b = x.getPortNames();
        if (b.size() >= 2) //both stop and start arrive
            holdIn("active", 10);
        else if (b.contains("stop")) {
            if (phaseIs("active"))
                passivate();
        } else if (b.contains("start")) {
            if (phaseIs("passive"))
                holdIn("active", 100);
        }
    }
}
