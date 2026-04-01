/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 3.0 Beta
 *  Date       : 02-22-03
 */
package genDevs.modeling;

import GenCol.*;
import java.util.*;
import java.awt.Color;
import genDevs.simulation.*;
import genDevs.simulation.distributed.*;
import util.*;

//public abstract
public class atomic extends devs implements AtomicInterface,variableStructureInterface{
 protected double sigma;
 protected String phase;
 protected ActivityInterface a;
 protected double  INFINITY  = DevsInterface.INFINITY;
 protected CoupledSimulatorInterface mySim;

public CoupledSimulatorInterface getSim(){
return mySim;
}

public atomic(){
this("atomic");
}

public atomic(String name){
super(name);
sigma = INFINITY;
}
public atomic(ActivityInterface a){
this(a.getName());
this.a = a;
}
public void setSimulator(CoupledSimulatorInterface mySim){
this.mySim = mySim;
}

public String getPhase(){
return phase;
}

public void setSigma(double sigma){

  this.sigma = sigma;
}

public double getSigma(){
return sigma;
}

public double getSimulationTime(){   //Xiaolin Hu, Jun 16, 2004
   return ((coupledSimulator)getSim()).getCurrentTime();
}

public void initialize(){
}

public void Continue(double e){
 if (sigma < INFINITY)
    setSigma(sigma - e);
}




    public void passivateIn(String phase) {holdIn(phase, INFINITY);}
    public void passivate() {passivateIn("passive");}
    public void holdIn(String phase, double sigma) {holdIn(phase, sigma, null);}

    public void holdIn(String phase, double sigma,ActivityInterface a)
    {

        this.phase = phase;
        setSigma(sigma);
        if (a != null) {
            this.a = a;
            mySim.startActivity(a);
        }
    }


  public void startActiviry(ActivityInterface a){
    this.a = a;
    mySim.startActivity(a);
  }

  public boolean phaseIs(String phase){
  return this.phase.equals(phase);
  }


  public double ta(){
      return sigma;
  }

  public void deltint(){}
  public void deltext(double e,MessageInterface x){deltext(e,(message)x);}
  public void deltext(double e,message x){} //usual devs
  public void deltcon(double e,MessageInterface x){deltcon(e,(message)x);}
  public void deltcon(double e,message x){ //usual devs
   deltint();
   deltext(0,x);
}

 public MessageInterface Out(){

 return out();
}

 public message out(){
 return new message();
}

 public String toString(){
 return "phase :"+ phase + " sigma : " + sigma ;
 }

  public void showState(){
    Logging.log("Model "+ getName() + "'s state: ", Logging.full);
    Logging.log("phase :"+ phase + " sigma : " + sigma, Logging.full);
  }

  public ActivityInterface getActivity(){
  return a;
  }

    public String stringState(){
    return "";
    }

// the code below is for variable structure DEVS
 public void addCoupling(String src, String p1, String dest, String p2){
    digraph P = (digraph)getParent();
    if(P!=null){
      //update its parent model's coupling info database
      P.addPair(new Pair(src,p1),new Pair(dest,p2));
      // update the corresponding simulator's coupling info database
      coordinator PCoord = P.getCoordinator();
      PCoord.addCoupling(src,p1,dest,p2);
    }
    else{   // this is for the distributed simulation situation
      if (mySim instanceof RTCoupledSimulatorClient)
        ((RTCoupledSimulatorClient)mySim).addDistributedCoupling(src,p1,dest,p2);
    }
  }

 public void removeCoupling(String src, String p1, String dest, String p2){
    digraph P = (digraph)getParent();
    if(P!=null){
      //update its parent model's coupling info database
      P.removePair(new Pair(src,p1),new Pair(dest,p2));
      // update the corresponding simulator's coupling info database
      coordinator PCoord = P.getCoordinator();
      PCoord.removeCoupling(src,p1,dest,p2);
    }
    else{   // this is for the distributed simulation situation
      if (mySim instanceof RTCoupledSimulatorClient)
        ((RTCoupledSimulatorClient)mySim).removeDistributedCoupling(src,p1,dest,p2);
    }
 }

 public void addModel(IODevs iod){
   digraph P = (digraph)getParent();
   if(P.checkNameUniqueness(iod.getName())){
 //    System.out.println(" add model: "+iod.getName());
     P.add(iod);
     coordinator PCoord = P.getCoordinator();
     PCoord.setNewSimulator( (IOBasicDevs) iod);
   }
   else System.out.println("Conflict!! -- the model name already exists");
 }

 public void removeModel(String modelName){
   digraph P = (digraph)getParent();
   coordinator PCoord = P.getCoordinator();
   // first remove all the coupling related to that model
   PCoord.removeModelCoupling(modelName);
   // then remove the model
   IODevs iod = P.withName(modelName);
   P.remove(iod);
   PCoord.removeModel(iod);
 }

 // by Yi Sun Sep, 2007
 public void removeModel(IODevs model){
   digraph P = (digraph)getParent();
   coordinator PCoord = P.getCoordinator();
   // first remove all the coupling related to that model
   PCoord.removeModelCoupling(model.getName());
   // then remove the model
   //IODevs iod = P.withName(model);
//   System.out.println(iod);
   P.remove(model);
   PCoord.removeModel(model);
 }

 public couprel getModelCoupling(String modelName){
   digraph P = (digraph)getParent();
   coordinator PCoord = P.getCoordinator();
   //copy coupling
   return PCoord.getModelCoupling(modelName);
   }

  public void copyModelCoupling(couprel nc,String oldnm,IODevs newd){
    Iterator it = nc.iterator();
    while (it.hasNext()){
      Pair pr = (Pair)it.next();
      Pair cs = (Pair)pr.getKey();
      Pair cd = (Pair)pr.getValue();
      String src =  (String)cs.getKey();
      String dst =  (String)cd.getKey();
      addCoupling(src.equals(oldnm)?newd.getName():src,(String)cs.getValue(),
                dst.equals(oldnm)?newd.getName():dst,(String)cd.getValue());
    }
  }

 //by saurabh
  public void addInport(String modelName, String port){
    //s.s("Inside atomic inport");
    digraph P = (digraph)getParent();
    IODevs iod = (IODevs)P.withName(modelName);
    if (P != null){
      if (iod instanceof atomic)
        iod.addInport(port);
      else
        ((digraph)iod).addInport(iod.getName(),port);
    }else
      s.s(modelName+ ": Its parent is not defined");

    addInportHook1(modelName,port);
  }

  public void addInportHook1(String modelName, String port){
    //s.s("Inside addInport hook 1");
    s.s("Inport added: "+port+"      component: "+modelName);
  }

  public void addOutport(String modelName, String port){
    digraph P = (digraph)getParent();
    IODevs iod = (IODevs)P.withName(modelName);
    if (P != null){
      if (iod instanceof atomic)
        iod.addOutport(port);
      else
        ((digraph)iod).addOutport(iod.getName(),port);
    }else
      s.s(modelName+ ": Its parent is not defined");

    addOutportHook1(modelName,port);
  }

  public void addOutportHook1(String modelName, String port){
     //s.s("Inside addOutport hook 1");
    s.s("Outport added: "+port+"      component: "+modelName);
  }

  public void removeInport(String modelName, String port){
    //s.s("Inside atomic removeInport");
    digraph P = (digraph)getParent();
      if (P != null)
      ((IODevs)P.withName(modelName)).removeInport(port);
      else
        s.s(modelName+ ": Its parent is not defined");

      removeInportHook1(modelName,port);
  }

  public void removeInportHook1(String modelName, String port){
    //s.s("Inside removeInport hook 1");
    s.s("Inport removed: "+port+"      component: "+modelName);
  }

  public void removeOutport(String modelName, String port){
    //s.s("Inside atomic removeOutport");
    digraph P = (digraph)getParent();
      if (P != null)
        ((IODevs)P.withName(modelName)).removeOutport(port);
      else
        s.s(modelName+ ": Its parent is not defined");

      removeOutportHook1(modelName,port);
  }

  public void removeOutportHook1(String modelName, String port){
    //s.s("Inside removeOutport hook 1");
    s.s("Inport removed: "+port+"      component: "+modelName);
  }


}