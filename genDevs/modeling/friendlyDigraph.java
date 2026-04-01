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




public class friendlyDigraph extends digraph{
//implements Coupled{

public friendlyDigraph(String nm){
super(nm);
}



public void doAllToAllCoupling(){
componentIterator it1 = components.cIterator();
while(it1.hasNext()) {
    devs d1 = (devs)it1.nextComponent();
     componentIterator it2 = components.cIterator();
      while(it2.hasNext()) {
        devs d2 = (devs)it2.nextComponent();
        if (!d1.equals(d2))
        addCoupling(d1,"out",d2,"in");
   }
   }
}

public void coupleAllTo(String sourcePt,devs d,String destinPt){
componentIterator it1 = components.cIterator();
while(it1.hasNext()) {
    devs d1 = (devs)it1.nextComponent();
        if (!d1.equals(d))
        addCoupling(d1,sourcePt,d,destinPt);
   }
}

public void coupleOneToAll(devs d,String sourcePt,String destinPt){
componentIterator it1 = components.cIterator();
while(it1.hasNext()) {
    devs d1 = (devs)it1.nextComponent();
        if (!d1.equals(d))
        addCoupling(d,sourcePt,d1,destinPt);
   }
}

public void coupleAllToExcept(String sourcePt,devs d,String destinPt
                     ,devs other){

 componentIterator it1 = components.cIterator();
while(it1.hasNext()) {
    devs d1 = (devs)it1.nextComponent();
        if (!d1.equals(d)&& !d1.equals(other))
        addCoupling(d1,sourcePt,d,destinPt);
   }
}

public void coupleOneToAllExcept(devs d,String sourcePt,
            String destinPt,devs other){
componentIterator it1 = components.cIterator();
while(it1.hasNext()) {
    devs d1 = (devs)it1.nextComponent();
        if (!d1.equals(d)&&!d1.equals(other))
        addCoupling(d,sourcePt,d1,destinPt);
   }
}
}












