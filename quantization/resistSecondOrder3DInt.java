/*
 * mass.java
 *
 * Created on February 2, 2003, 11:05 AM
 */

package quantization;

import java.lang.*;
import java.awt.*;
import java.util.*;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;
import util.*;
import genDevs.plots.*;

public class resistSecondOrder3DInt extends secondOrder3DInt{

protected double resistance;
protected vect3DEnt inputaccel;

public resistSecondOrder3DInt(String name, double Quantum,double resistance,
                      vect3DEnt velocity, vect3DEnt state){
super(name,Quantum,velocity,state);
this.resistance = resistance;
}


public resistSecondOrder3DInt(){
this("SecOrder3DIntegrator",1,0,new vect3DEnt(0,0,0),new vect3DEnt(0,0,0));
}
public void initialize(){
     inputaccel = new vect3DEnt(0,0,0);
     super.initialize();
 }

public void setInp(vect3DEnt inputaccel){
    this.inputaccel = inputaccel;
    lastaccel = accel;
    vect3DEnt rv = velocity.scalarMult(resistance);
    this.accel = inputaccel.subtract(rv);
}

public void  deltint( )
{
clock = clock + sigma;
state = nextState;
velocity = nextvelocity;
setInp(inputaccel);
computeIntNextstate();
phase = state.toString();
}

}