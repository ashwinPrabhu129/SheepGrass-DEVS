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

public class scalableDigraph
    extends friendlyDigraph
    implements scalableDigraphInterface
{

  public scalableDigraph(String nm) {
    super(nm);
  }
  public scalableDigraph() {
    this("scalableDigraph");
  }

  public void addNewModel()
  {
    //to be coded by the user

    //"This fn can be used to customize the addition of any model in this window"
    //"The user adds his own code here"
    //"This gets executed when he tries to add components using the mouse"

  }

  public void removeOldModel(String withName)
  {
    //to be coded by the user

    //"This fn can be used to customize the REMOVAL of any model in this window"
    //"The user adds his own code here"
    //"This gets executed when he tries to delete components using the mouse"

  }
}