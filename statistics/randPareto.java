/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */


package  statistics;

import GenCol.*;
import java.lang.*;
import java.util.*;


public class randPareto extends rand{
protected double location, shape;


public randPareto(long seed,double location ,double shape){
  super(seed);
  this.location = location;
  this.shape = shape;
}

public randPareto(long seed){
this(seed,1,1);
}

public randPareto(){
this(1,1,1);
}

public double sample(){
return pareto(location,shape);
}

    static public void main(String[] args)
    {
       rand r = new randPareto();
       for (int i = 0;i<10;i++)
         System.out.println(r.sample());

    }
}