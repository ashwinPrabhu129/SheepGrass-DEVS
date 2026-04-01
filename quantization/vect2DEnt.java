package quantization;

import simView.*;
import util.*;
import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;

public class vect2DEnt  extends entity {
public double x,y;

public static vect2DEnt ZERO = new vect2DEnt(0,0);

public vect2DEnt(double x, double y){
super("vect2DEnt");
this.x = x;
this.y = y;
}

public vect2DEnt add(double x, double y){
return new vect2DEnt(this.x+x,this.y+y);
}

public vect2DEnt add(vect2DEnt v){
return add(v.x,v.y);
}

/*
//////not safe
public void addSelf(vect2DEnt v){
x += v.x;
y += v.y;
}
*/

public vect2DEnt subtract(vect2DEnt v){
return add(-v.x,-v.y);
}

public double dotProd(vect2DEnt v){
return x*v.x + y*v.y;
}

public double vectorProd(vect2DEnt v){
return x*v.y - y*v.x;
}

public double distanceTo(vect2DEnt v){
return Math.abs(vectorProd(v))/norm();
}

public vect2DEnt perpendicular(){
return new vect2DEnt(-y,x);
}

public vect2DEnt scalarMult(double d){
return new vect2DEnt(this.x*d,this.y*d);
}

public vect2DEnt maxLimit(double max){
if (norm() <= max)
return this;
else{
vect2DEnt na = normalize();
return na.scalarMult(max);
}
}
public double norm(){
return Math.sqrt(x*x + y*y);
}

public vect2DEnt normalize(){
double norm = norm();
if (norm == 0)
return ZERO;
else return new vect2DEnt(x/norm,y/norm);
}

public vect2DEnt copy(){
return new vect2DEnt(this.x,this.y);
}

public String toString(){
return doubleFormat.niceDouble( x )+
 ","+doubleFormat.niceDouble(y);
}


public static vect2DEnt toObject(String nm){
int commaIndex = nm.indexOf(",");
String xs = nm.substring(0,commaIndex);
String ys = nm.substring(commaIndex+1,nm.length());
return new vect2DEnt(Double.parseDouble(xs),Double.parseDouble(ys));
}

public static vect2DEnt toObject(entity ent){
return toObject(ent.getName());
}


public String getName(){
return toString();
}

public int hashCode(){
return (int)Math.pow(Math.ceil(x),Math.ceil(y));
}

public boolean equals(Object o){    //overrides pointer equality of Object
if  (o instanceof vect2DEnt)
{
vect2DEnt ov = (vect2DEnt)o;
//return ov.x == this.x && ov.y == this.y;
//System.out.println(Math.abs(ov.x-this.x)<.0001);
return (Math.abs(ov.x-this.x)<.001        //round off requires this coarse
    && Math.abs(ov.y-this.y)<.001);

}
else return false;
}

public boolean sameDir(vect2DEnt v){
return normalize().equals(v.normalize());
}



public int  quadrant(){//counterclockwise
if (x>=0){
 if (y>=0) return 1;
 else//if (y<0)
  return 4;
 }
else{ //(x<0)
 if (y>=0) return 2;
 else //if (y<0)
 return 3;
 }
}

public double angle(){
int q = quadrant();
double angleIn1 = Math.atan(Math.abs(y)/Math.abs(x));
if (q == 1) return angleIn1;
else if (q == 2) return Math.PI-angleIn1;
else if (q == 3) return Math.PI+angleIn1;
else //if (q == 3)
 return 2*Math.PI-angleIn1;
}


public static double addAngle(double a1,double a2){
return (a1+a2)%(2*Math.PI);
}

public double angle(vect2DEnt v){
return addAngle(v.angle(),-angle());
}

public vect2DEnt rotate(double rotAngle){
vect2DEnt rv =  new vect2DEnt(Math.cos(addAngle(angle(),rotAngle)),
                               Math.sin(addAngle(angle(),rotAngle)));
return rv.scalarMult(norm());
}

public double proj(vect2DEnt v){
double angle = angle(v);
return norm()*v.norm()*Math.cos(angle);
}

public vect2DEnt transform(vect2DEnt v){//to my coords
vect2DEnt rv =  new vect2DEnt(Math.cos(angle(v)),
                               Math.sin(angle(v)));
return rv.scalarMult(v.norm());
}
public static void main(String[] args){
double f = 8/16.;//Math.PI/10;
for (int i = 0;i<10;i++){
  f= f*2;
  System.out.println(Math.floor(f));
  f = f%1;
  }
for (int i = 0;i<10;i++)
  System.out.println(new vect2DEnt(1,0).rotate(i*Math.PI/5).quadrant());
  //should run through the quandrants in order
System.out.println("XXXXXXXXXXXXXXXXXXXX");
vect2DEnt v = new vect2DEnt(0,1);
//System.out.println(v.transform(new vect2DEnt(-1,1)).quadrant());
for (int i = 0;i<10;i++)
  System.out.println(v.transform(new vect2DEnt(1,-1).rotate(i*Math.PI/5)).quadrant());
//should start at quad 3

  //vect2DEnt v = new vect2DEnt(Math.PI,-666.66666);
//System.out.println(toObject(v.getName()).getName());
//System.out.println(toObject(ZERO.getName()).getName());
//double z = Double.POSITIVE_INFINITY;
//System.out.println(Double.toString(z));
//System.out.println(Double.parseDouble("Infinity"));

/*
correct to allow using INFINITY also E notation
System.out.println(toObject(new vect2DEnt(Math.PI,Double.POSITIVE_INFINITY)//Double.NEGATIVE_INFINITY)
            .getName()).getName());
            */
}
}