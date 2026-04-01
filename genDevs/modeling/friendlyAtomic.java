package genDevs.modeling;

import java.util.*;
import GenCol.*;

import util.*;

public class friendlyAtomic extends  atomic implements friendlyAtomicInterface{

public friendlyAtomic(String nm){
super(nm);
}

public friendlyAtomic(){
this("friendlyAtomic");
}



public boolean somethingOnPort(message x,String port){
for (int i=0; i< x.getLength();i++)
   if (messageOnPort(x,port,i))
    return true;
    return false;


}

public entity getEntityOnPort(message x,String port){

for (int i=0; i< x.getLength();i++)
   if (messageOnPort(x,port,i)){
    return x.getValOnPort(port,i);
    }

return null;
}

public int getIntValueOnPort(message x,String port){
    intEnt dv = (intEnt)getEntityOnPort(x,port);
    return dv.getv();
}


public double getRealValueOnPort(message x,String port){

    doubleEnt dv = (doubleEnt)getEntityOnPort(x,port);
    return dv.getv();
}


public double sumValuesOnPort(message x,String port){
double val = 0;
for (int i=0; i< x.getLength();i++)
   if (messageOnPort(x,port,i)){
    doubleEnt dv = (doubleEnt)x.getValOnPort(port,i);
    val += dv.getv();
    }
return val;
}

public String getNameOnPort(message x,String port){
    entity ent = getEntityOnPort(x,port);
    return ent.getName();
}


public message outputNameOnPort(String nm,String port){
message m = new message();
m.add(makeContent(port,new entity(nm)));
return m;
}

public message outputNameOnPort(message m,String nm,String port){
m.add(makeContent(port,new entity(nm)));
return m;
}

public message outputIntOnPort(message m,int r,String port){
m.add(makeContent(port,new intEnt(r)));
return m;
}

public message outputIntOnPort(int r,String port){
message m = new message();
m.add(makeContent(port,new intEnt(r)));
return m;
}

public message outputRealOnPort(message m,int r,String port){
m.add(makeContent(port,new doubleEnt(r)));
return m;
}

public message outputRealOnPort(double r,String port){
message m = new message();
m.add(makeContent(port,new doubleEnt(r)));
return m;
}

public message outputRealOnPort(message m,double r,String port){
m.add(makeContent(port,new doubleEnt(r)));
return m;
}



public static void main(String args[]){
friendlyAtomic re = new  friendlyAtomic(" ");
String s = re.getNameOnPort(
    re.outputNameOnPort("test","in"),"in");
System.out.println(s);
}
 }





