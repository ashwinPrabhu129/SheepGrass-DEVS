/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 3.0 Beta
 *  Date       : 02-22-03
 */

package genDevs.modeling;

import GenCol.*;

import java.util.*;
import genDevs.simulation.*;


public interface friendlyDigraphInterface {


public void doAllToAllCoupling();


public void coupleAllTo(String sourcePt,devs d,String destinPt);


public void coupleOneToAll(devs d,String sourcePt,String destinPt);

public void coupleAllToExcept(String sourcePt,devs d,String destinPt
                     ,devs other);


}
