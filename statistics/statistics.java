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
import java.util.*;
import util.*;

public class statistics{

protected double numbers[];
protected DEVSQueue q;
public static int numClasses = 10;

public statistics (){
q = new DEVSQueue();
}

public statistics (double numbers[]){
this.numbers = numbers;
}

public int size(){
return numbers.length;
}

public void add(double n){
q.add(new doubleEnt(n));;
}

public void toArray(){
numbers = new double[q.size()];

for (int i = 0;i<numbers.length;i++){
numbers[i] = ((doubleEnt)q.get(i)).getv();

 }
 }

public String toString(double[] array){
String s = "{";
for (int i = 0;i<array.length;i++)//-1
 //if (array[i]!=0)
  s += "("+i+": "+array[i]+"), ";
  return s +")}";
}

public String toString(){
return toString(numbers);
}

public String distribution(){
if (numbers.length == 0)return " ";
double low = getMin();
double high = getMax();
if (low>=high) return " ";

double classSize = (high-low)/(double)numClasses;
String s = "\n"+"{";
double array[] = partition(low,high,numClasses);
double lower = low,upper = low+classSize;
for (int i = 1;i<array.length-1;i++){
    String label = " ["+doubleFormat.niceDouble(lower)+"->"+doubleFormat.niceDouble(upper)+"]=";
     s += label+doubleFormat.niceDouble(array[i]/size())+"\n";
     lower = upper;
     upper = lower+classSize;
  }
  return s +")}";
}

public double average(){
if (numbers.length == 0)return 0;
double sum = 0;
for (int i = 0;i<numbers.length;i++)
  sum += numbers[i];
return sum/numbers.length;
}

public double avgOfSquares(){
if (numbers.length == 0)return 0;
double sum = 0;
for (int i = 0;i<numbers.length;i++)
  sum += numbers[i]*numbers[i];
return sum/numbers.length;
}
public double variance(){
double avg = average();
return avgOfSquares() - avg*avg;
}

public double std(){
return Math.sqrt(variance());
}

public double getMax(){
double max = Double.NEGATIVE_INFINITY;
double dist;
if (q != null){
Iterator it = q.iterator();
while(it.hasNext()){
dist = ((doubleEnt)it.next()).getv();
max = dist>max?dist:max;
}
return max;
}
for (int i = 0;i<numbers.length;i++){
dist = numbers[i];
max = dist>max?dist:max;
}
return max;
}

public double getMin(){
double min = Double.POSITIVE_INFINITY;
double dist;
if (q != null){
Iterator it = q.iterator();
while(it.hasNext()){
dist = ((doubleEnt)it.next()).getv();
min = dist<min?dist:min;
}
return min;
}
for (int i = 0;i<numbers.length;i++){
dist = numbers[i];
min = dist<min?dist:min;
}
return min;
}

public double[] partition(double begin,double end, int numClasses){
if (begin >= end)return new double[0];
double size =  (end-begin)/numClasses;
if (size <= 0) {System.out.println("invalid class specification"); return null;}
double classes[] = new double[numClasses+2];
for (int i = 0;i<numbers.length;i++){
   for (int j = 1;j<=numClasses;j++){
       if (numbers[i] <= begin + j*size){
           classes[j]++;
           break;
           }
        if (numbers[i] >= end)classes[numClasses+1]++;
       }
       }
 return classes;
 }

public static void main(String args[]){

/*
double [] numArray = {1,2,3,4,5,6,7,8,9};
//statistics s = new statistics(numArray);

statistics s = new statistics();
s.add(1);
s.add(2);
s.add(3);
s.toArray();

System.out.println(s.average());
System.out.println(s.avgOfSquares());
System.out.println(s.variance());
System.out.println(s.std());
System.out.println("------------------------------");
s.add(4);
s.toArray();
System.out.println(s.average());
System.out.println("------------------------------");

rand r = new rand(1);
*/
System.out.println("------------------------------");
int i=0,num = 1000;
double [] numArray = new double[num];
//
//for (i = 0;i<numArray.length;i++)
//
statistics s = new statistics();
double t = 0,q = .001,dt,val=0,tl=0;
num = (int)Math.ceil(1/q);
// System.out.println(num);
for (i = 0;i<num;i++){
 double qt = q;
  //
  if (val <= .3) qt = 10*q;//10*q;
  //
  else  if (val <= .6) qt= 2*q;//5*q;
 //qt = Math.min(100*q,q/Math.sin(t));
 //
 if (Math.cos(t) <=0)break;

 dt = qt/Math.cos(t);
 //t = Math.asin(val);
 //dt = t-tl;
 //tl = t;
//val+=q;
//System.out.println(t+ " "+dt);
t += dt;
 s.add(dt);

}
s.toArray();
//System.out.println("num steps " +i + " "+Math.PI/2+" "+val);
 //numArray[i] =Math.sin(i*Math.PI/(2*num));
  // numArray[i] = Math.exp(-i/(double)num);
   //   numArray[i] =(1-i/(double)num)*Math.exp(-i/(double)num);
   // Math.min(1/(1-i/(double)num)*Math.exp(-i/(double)num),1000)
   // (i/(double)num)*Math.exp(-i/(double)num)
   // numArray[i] = Math.exp(i/(double)num);

  // numArray[i] = Math.min(1/(1-i/(double)num)*Math.exp(-i/(double)num),5);
//);

//statistics s = new statistics(numArray);

System.out.println(s.average());
//System.out.println(s.avgOfSquares());
//System.out.println(s.variance());
System.out.println(s.std());
System.out.println(s.distribution());
/*
System.out.println("------------------------------");


numArray = new double[300];
for (int i = 0;i<numArray.length;i++)
 numArray[i] = r.lognormal(0,.7);

s = new statistics(numArray);
System.out.println(s.average());
System.out.println(s.avgOfSquares());
System.out.println(s.variance());
System.out.println(s.std());
System.out.println(s.toString(s.partition(0,100,10)));
System.out.println("------------------------------");
numArray = new double[300];
for (int i = 0;i<numArray.length;i++)
 numArray[i] = r.pareto(10,2);

s = new statistics(numArray);
System.out.println(s.average());
System.out.println(s.avgOfSquares());
System.out.println(s.variance());
System.out.println(s.std());
System.out.println("------------------------------");
*/
}

/**/
}