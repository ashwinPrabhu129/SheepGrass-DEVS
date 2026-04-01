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


public class randUniform extends rand{
protected double lo, hi;


public randUniform(long seed,double lo ,double hi){
  super(seed);
  this.lo = lo;
  this.hi = hi;
}

public randUniform(long seed){
this(seed,0,1);
}

public randUniform(){
this(1,0,1);
}

public double sample(){
return uniform(lo,hi);
}

    static public void main(String[] args)
    {
       rand r = new randUniform();
       for (int i = 0;i<10;i++)
       System.out.println(r.sample());
    }
}