package quantization;


import simView.*;
import util.*;
import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.*;

public class vectAim{




public static double timeToIntersect(vect2DEnt p,vect2DEnt v){
if (p.sameDir(v)){
if (v.norm() == 0)return 0;
else
return p.norm()/v.norm();
}
 else return  Double.POSITIVE_INFINITY;
 }

public static double timeToIntersect(vect2DEnt p1,vect2DEnt p2,
                     vect2DEnt v1,vect2DEnt v2){
return timeToIntersect(p2.subtract(p1),v1.subtract(v2)); //note sign diff
}

public static vect2DEnt placeOfIntersect(vect2DEnt p1,vect2DEnt p2,
                     vect2DEnt v1,vect2DEnt v2){
double t =  timeToIntersect( p1,p2, v1, v2);
if (t == 0)return p1;
else if (t>= Double.POSITIVE_INFINITY)
return new vect2DEnt(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);
else return p1.add(v1.scalarMult(timeToIntersect(p1,p2,v1,v2)));
}


public static vect2DEnt vRel (vect2DEnt p1,vect2DEnt p2, vect2DEnt v2){
vect2DEnt los = p2.subtract(p1);
return los.transform(v2);
}

public static Pair classify(int quad){
if (quad == 1)return new Pair("left","chase");
else if (quad == 2)return new Pair("left","greet");
else if (quad == 3)return new Pair("right","greet");
else //if (quad == 4)
return new Pair("right","chase");
}

public static Pair decideAct(vect2DEnt p1,vect2DEnt p2, vect2DEnt v2){
vect2DEnt v2Rel = vRel (p1,p2, v2);
int vquad = v2Rel.quadrant();
return classify(vquad);
}

public static vect2DEnt predictLoc(vect2DEnt p2, vect2DEnt v2,double ta){
return p2.add(v2.scalarMult(ta));
}

public static vect2DEnt dir(vect2DEnt p1,vect2DEnt p2p){
return p2p.subtract(p1).normalize();
}
public static double speed(vect2DEnt p1,vect2DEnt p2p,double ta){
return p2p.subtract(p1).norm()/ta;
}

public static vect2DEnt aim(vect2DEnt p1,vect2DEnt p2, vect2DEnt v2,double ta){
vect2DEnt p2p = predictLoc (p2, v2,ta);
vect2DEnt dir = dir(p1,p2p);
double speed = speed(p1,p2p,ta);
return dir.scalarMult(speed);
}
/*
public static vect2DEnt aim(vect2DEnt p1,vect2DEnt p2, vect2DEnt v2,double ta){
double speed = p2.subtract(p1).norm()/ta;
vect2DEnt los = p2.subtract(p1).normalize();
vect2DEnt v1par = los.scalarMult(speed);
double angle = v2.angle(los);
double v2projOnLos = los.dotProd(v2);
double vv = v2.norm()*Math.cos(angle);
//System.out.println(vv);
double v2projOrth = v2.norm()*Math.sin(angle);
vect2DEnt perp = los.perpendicular();
vect2DEnt v2Orth = perp.scalarMult(v2projOrth);
v1par = v1par.add(los.scalarMult(v2projOnLos));
return v1par.add(v2Orth);
}
*/
public static boolean confirmAct(Pair pr,
                vect2DEnt p1,vect2DEnt p2, vect2DEnt v1, vect2DEnt v2){
boolean lfAgree = false,cgAgree = false;
vect2DEnt v1Rel = vRel (p1,p2, v1);
int v1quad = v1Rel.quadrant();

if (pr.getKey().equals("left") && (v1quad == 1 || v1quad == 2))
lfAgree = true;
if (pr.getKey().equals("right") && (v1quad == 3 || v1quad == 4))
lfAgree =  true;

if (pr.getValue().equals("chase") && v1.norm()>=v2.norm())
cgAgree = true;
if (pr.getValue().equals("greet") && v1.norm()<= v2.norm())
cgAgree = true;

return lfAgree && cgAgree;
}

public static double timeToGoalLine(vect2DEnt p2, vect2DEnt v2,
                   vect2DEnt goalLine){

vect2DEnt goalDir = goalLine.normalize();
double distToGoalLine = goalDir.distanceTo(p2);
double speedToGoalLine = Math.abs(v2.vectorProd(goalDir));
return distToGoalLine/speedToGoalLine;
}

public static boolean crossedGoalLine(vect2DEnt p1,vect2DEnt p2,
                                   vect2DEnt goalLine){
 //are they on same or different side of goalLine
 double sinp1 = p1.vectorProd(goalLine);
 double sinp2 = p2.vectorProd(goalLine);
 return Math.abs(sinp1*Math.abs(sinp2) - sinp2*Math.abs(sinp1))>.01;
 }

 public static vect2DEnt meet(vect2DEnt p1, vect2DEnt p2, vect2DEnt v2,
                               vect2DEnt goalLine,
                               double maxSpeed){
double tg = timeToGoalLine(p2,v2,goalLine);
vect2DEnt vch = vect2DEnt.ZERO;
for (int i = 10;i>=1;i--){
vect2DEnt v = aim(p1,p2,v2,i*tg/10);
vect2DEnt pint = placeOfIntersect(p1,p2,v,v2);
//System.out.println("ZZZZZZZZZZZZZZZ "+pint+" "+v.norm());
if (!crossedGoalLine(p2,pint,goalLine) && v.norm()< maxSpeed){
 //
 System.out.println(i+" XXXXXXXXXXXXXXX "+pint+" "+v);
 vch =  v;
 }
}
return vch;
}


public static void main(String[] args){
/*
vect2DEnt p1 = new vect2DEnt(1,1);
vect2DEnt p2 = new vect2DEnt(2,1);//4,-20);
vect2DEnt v2 = new vect2DEnt(-1,-1);
//System.out.println(vRel(p1,p2,v2).quadrant());
Pair pr = decideAct(p1,p2,v2);
System.out.println(pr);
vect2DEnt v1 = aim(p1,p2,v2,10);
System.out.println(v1);
System.out.println(timeToIntersect(p1,p2,v1,v2));
System.out.println(confirmAct(pr,p1,p2,v1,v2));


vect2DEnt p1 = new vect2DEnt(0,0);
vect2DEnt p2 = new vect2DEnt(-10,0);
vect2DEnt v2 = new vect2DEnt(1,1);
vect2DEnt goal = new vect2DEnt(0,1);
System.out.println(vRel(p1,p2,v2).quadrant());
System.out.println(timeToGoalLine(p2,v2,goal));
System.out.println(crossedGoalLine(p2,v2,goal));
boolean stop = false;
double maxV = 2;
double minDist = Double.POSITIVE_INFINITY;
vect2DEnt vch = vect2DEnt.ZERO;
vect2DEnt vabs = vect2DEnt.ZERO;
for (int i = 1;i<=15;i++){
vect2DEnt v = aim(p1,p2,v2,i*timeToGoalLine(p2,v2,goal)/10);
vect2DEnt pint = placeOfIntersect(p1,p2,v,v2);
if ( pint.norm() < Double.POSITIVE_INFINITY)
System.out.println(pint.subtract(p1).norm() +" "+v.norm());
if (!crossedGoalLine(p1,pint,goal) && pint.subtract(p1).norm()<minDist){
 minDist = pint.subtract(p1).norm();
 System.out.println("XXXXXXXXXXXXXXXXX "+minDist);
 vch = v;
 System.out.println("YYYYYYYYYYYYYYYY "+v);
 }
 if (!crossedGoalLine(p1,pint,goal) && v.norm()<maxV&& !stop ){
 vabs =  v;
 System.out.println("ZZZZZZZZZZZZZZZ "+v);
  stop = true;
 }
}
System.out.println("v for minDist "+vch);
System.out.println("v allowed by maxV "+vabs);
*/
vect2DEnt p1 = new vect2DEnt(-5,0);
vect2DEnt p2 = new vect2DEnt(-10,0);
vect2DEnt v2 = new vect2DEnt(1,1);
vect2DEnt goal = new vect2DEnt(0,1);
vect2DEnt pint = new vect2DEnt(1,10);
System.out.println(vRel(p1,p2,v2).quadrant());
System.out.println(timeToGoalLine(p2,v2,goal));
System.out.println(crossedGoalLine(p2,pint,goal));
boolean stop = false;
double maxV = 4.1;
vect2DEnt v = meet(p1,p2,v2,goal,maxV);
System.out.println("computed "+v);
}
}