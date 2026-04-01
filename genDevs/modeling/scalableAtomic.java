/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 3.0 Beta
 *  Date       : 02-22-03
 */
package genDevs.modeling;


import java.util.*;
import GenCol.*;

import genDevs.modeling.*;
import genDevs.simulation.*;

import util.*;

/**
 * Class to provide the Variable Structure methodology
 * @author Saurabh Mittal
 */

public class scalableAtomic
    extends friendlyAtomic
    implements scalableAtomicInterface {


  public scalableAtomic(String nm) {
    super(nm);
  }
  public scalableAtomic() {
    this("scalableAtomic");
  }

  public void addNewModel(){
    //to be coded by the user

    //"This fn can be used to customize the addition of any model in this window"
    //"The user adds his own code here"
    //"This gets executed when he tries to add components using the mouse"


  }

  public void removeOldModel(){
   //to be coded by the user

   //"This fn can be used to customize the REMOVAL of any model in this window"
   //"The user adds his own code here"
   //"This gets executed when he tries to delete components using the mouse"


  }


}