//Method scalarMult(double d) has been modyfied by shahab to handle INFINITY    jan 30 2003
//Method innerProduct(vect3DEnt v) has been added by shahab                     feb 10 2003

package quantization;

import simView.*;
import util.*;
import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;

public class vect3DEnt extends entity {
public double x,y,z;

public static vect3DEnt ZERO = new vect3DEnt(0,0,0);

public vect3DEnt(double x, double y,double z){
super("vect3DEnt");
this.x = x;
this.y = y;
this.z = z;
}

public vect3DEnt add(double x, double y, double z){
return new vect3DEnt(this.x+x,this.y+y,this.z+z);
}

public vect3DEnt add(vect3DEnt v){
return add(v.x,v.y,v.z);
}

public void addSelf(vect3DEnt v){
x += v.x;
y += v.y;
z += v.z;
}

public vect3DEnt subtract(vect3DEnt v){
return add(-v.x,-v.y,-v.z);
}

public double norm(){
return Math.sqrt(x*x + y*y+ z*z);
}

public vect3DEnt normalize(){
double norm = norm();
if (norm == 0)
return ZERO;
else return new vect3DEnt(x/norm,y/norm,z/norm);
}

public vect3DEnt perpendicular(){
return new vect3DEnt(y*z/2,x*z/2,-x*y);
}

public vect3DEnt scalarMult(double d){  //modyfied by shahab to handle 0*INFINITY=0
    vect3DEnt v=new vect3DEnt(this.x*d,this.y*d, this.z*d);
    if (this.x==0) v.x=0;
    if (this.y==0) v.y=0;
    if (this.z==0) v.z=0;
return v;
}
/*
public String toString(){
return doubleFormat.niceDouble( x )+
 ","+doubleFormat.niceDouble(y);
}
*/

public static vect3DEnt toObject(String nm){
int commaIndex = nm.indexOf(",");
String xs = nm.substring(0,commaIndex);
nm=nm.substring(commaIndex+1,nm.length());
commaIndex = nm.indexOf(",");
String ys = nm.substring(0,commaIndex);
String zs = nm.substring(commaIndex+1,nm.length());
return new vect3DEnt(Double.parseDouble(xs),Double.parseDouble(ys),Double.parseDouble(zs));
}
public static vect3DEnt toObject(entity ent){
return toObject(ent.getName());
}

public String getName(){
return toString();
}

public boolean equals(Object o){    //overrides pointer equality of Object
if  (o instanceof vect3DEnt)
{
vect3DEnt ov = (vect3DEnt)o;
return ov.x == this.x && ov.y == this.y && ov.z == this.z;
}
else return false;
}
public double innerProduct(vect3DEnt v){
    return (this.x*v.x+this.y*v.y+this.z*v.z);
}
public vect3DEnt crossProduct(vect3DEnt v){
    return new vect3DEnt(y*v.z-z*v.y,z*v.x-x*v.z,x*v.y-y*v.x);
}
public vect3DEnt maxLimit(double max){
if (norm() <= max)
return this;
else{
vect3DEnt na = normalize();
return na.scalarMult(max);
}
}
public String toString(){
    return Double.toString(x)+","+Double.toString(y)+","+Double.toString(z);
}



}