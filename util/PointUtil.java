/*      Copyright 2002 Arizona Board of regents on behalf of 
 *                  The University of Arizona 
 *                     All Rights Reserved 
 *         (USE & RESTRICTION - Please read COPYRIGHT file) 
 * 
 *  Version    : DEVSJAVA 2.7 
 *  Date       : 08-15-02 
 */ 

package util;

import java.awt.Point;

public class PointUtil
{
    static public void translate(Point a, Point b)
    {
        a.translate(b.x, b.y);
    }

    static public void negativeTranslate(Point a, Point b)
    {
        a.translate(-b.x, -b.y);
    }
}