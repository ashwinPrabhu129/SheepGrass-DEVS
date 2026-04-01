/*      Copyright 2002 Arizona Board of regents on behalf of 
 *                  The University of Arizona 
 *                     All Rights Reserved 
 *         (USE & RESTRICTION - Please read COPYRIGHT file) 
 * 
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */ 

package util;

import java.awt.*;

/**
 * A utility class containing various methods relating to awt components.
 *
 * @author      Jeff Mather
 */
public class ComponentUtil
{
    /**
     * Returns the offset x- and y-distances that the given component is
     * from its given ancestor.
     *
     * @param   comp        The component for whose location we are asking.
     * @param   ancestor    The ancestor component considered to be at (0,0)
     *                      for this calculation.
     *
     * @return              The x- and y- offset distances.
     */
    static public Point getLocationRelativeToAncestor(Component comp,
        Component ancestor)
    {
        // for each component up the chain from the given component to its
        // given ancestor
        Point location = new Point();
        for (Component c = comp; c != ancestor; c = c.getParent()) {
            // add this component's offsets from its parent to our running
            // x- and y- total offsets
            Point cLocation = c.getLocation();
            location.translate(cLocation.x, cLocation.y);
        }

        return location;
    }
}