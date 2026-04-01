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


public class randExpon extends rand{
protected double mean;


public randExpon(long seed,double mean){
  super(seed);
  this.mean = mean;
}

public randExpon(long seed){
this(seed,1);
}

public randExpon(){
this(1,1);
}

public double sample(){
return expon(mean);
}

    static public void main(String[] args)
    {
       rand r = new randExpon();
       for (int i = 0;i<10;i++)
       System.out.println(r.sample());
    }
}