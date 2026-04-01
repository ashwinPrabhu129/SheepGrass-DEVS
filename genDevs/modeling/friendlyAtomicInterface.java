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


public interface friendlyAtomicInterface extends AtomicInterface{


public boolean somethingOnPort(message x,String port);

public entity getEntityOnPort(message x,String port);

public int getIntValueOnPort(message x,String port);

public double getRealValueOnPort(message x,String port);

public double sumValuesOnPort(message x,String port);

public String getNameOnPort(message x,String port);

public message outputNameOnPort(String nm,String port);

public message outputNameOnPort(message m,String nm,String port);

public message outputIntOnPort(int r,String port);

public message outputRealOnPort(message m,int r,String port);

public message outputRealOnPort(double r,String port);

public message outputRealOnPort(message m,double r,String port);


}

