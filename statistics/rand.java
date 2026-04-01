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


public class rand extends entity{

Random ran;

public rand(long seed){
  super();
  ran = new Random((long)seed);
}

public double sample(){
return uniform(1);
}


public  double uniform(double lo ,double hi)
{
  return  ran.nextDouble() * (hi - lo) + lo;

}

public double uniform(double hi)
{
  return uniform(0,hi);
}

public int iuniform(int lo, int hi)
{
  return (int)Math.rint(uniform(lo,hi));
      //rint = round to integral value (double)
}

public int iuniform(int hi)
{
  return iuniform(0,hi);
}

public  double expon(double mean){
  return -mean*Math.log(uniform(0,1));
}

public  double normal1(){
  double u = uniform(0,1);
  return Math.sqrt(-2*Math.log(u))*Math.cos(2*Math.PI*u);
}


public double normal(double mean,double sig){
   return mean + sig*normal1();
}

public double posNormal(double mean,double sig){
   double save = normal(mean,sig);
return save > 0?save:posNormal(mean,sig);
}

public double pareto( double location, double shape ){
	double u = uniform(0,1);
	return location * Math.pow( u, (-1/shape) );
}

public double lognormal( double mean, double sig ){
	return Math.pow( 10, normal( mean, sig ) );
}
}