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
import genDevs.modeling.*;
import genDevs.simulation.*;

/**
 * class to provide the VariableStructure methodology
 * @author Saurabh Mittal
 */

public interface scalableDigraphInterface extends friendlyDigraphInterface{

  public void addNewModel() ;

  public void removeOldModel(String withName) ;


}