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


public class randNormalPos extends randNormal{

public randNormalPos(long seed,double mean ,double sig){
  super(seed,mean,sig);
}

public randNormalPos(long seed){
this(seed,0,1);
}

public randNormalPos(){
this(1,0,1);
}

public double sample(){
return posNormal(mean,sig);
}

    static public void main(String[] args)
    {
       rand r = new randNormalPos();
       for (int i = 0;i<10;i++)
       System.out.println(r.sample());
    }
}