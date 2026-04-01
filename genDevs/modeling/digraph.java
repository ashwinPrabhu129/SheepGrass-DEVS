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
import genDevs.simulation.*;
import util.*;




public class digraph extends devs implements Coupled{
protected coordinator myCoordinator;
protected ComponentsInterface components;
protected couprel cp;

public digraph(String nm){
super(nm);
components = new Components();
cp = new couprel();
}

public void add(IODevs iod){
components.add(iod);
((devs)iod).setParent(this);
}

public void addCoupling(IODevs src, String p1, IODevs dest, String p2){
cp.add((entity)src,new port(p1),(entity)dest,new port(p2));
}

public void addPair(Pair cs, Pair cd){
cp.add(cs,cd);
}

public void removePair(Pair cs, Pair cd){
cp.remove(cs,cd);
}

public void remove(IODevs d){
components.remove(d);
}

//by saurabh
public void removeCoupling(IODevs src, String p1, IODevs dest, String p2){
  this.getCoordinator().removeCoupling(src.getName(),p1,dest.getName(),p2);
  cp.remove((entity)src,new port(p1),(entity)dest,new port(p2));
}

public IODevs withName(String nm){
Class [] classes  = {ensembleBag.getTheClass("java.lang.String")};
Object [] args  = {nm};
return (IODevs)components.whichOne("equalName",classes,args);
}

public ComponentsInterface getComponents(){
return components;
}

public couprel getCouprel(){
return cp;
}

public String toString(){
String s = "";
componentIterator cit = getComponents().cIterator();
while (cit.hasNext()){
IOBasicDevs iod = cit.nextComponent();
s += " " + iod.toString();
}
return s;
}

public void showState(){
components.tellAll("showState");
}


public void initialize(){
components.tellAll("initialize");
}


public void setSimulator(CoupledSimulatorInterface sim){}
public ActivityInterface getActivity(){return new activity("name");}
public void deltext(double e,MessageInterface x){}
public void deltcon(double e,MessageInterface x){}
public void deltint(){}
public MessageInterface Out(){return new message();}
public double ta(){return 0;}

public componentIterator iterator(){
  return getComponents().cIterator();
}

public void setCoordinator(coordinator coordinator_) {myCoordinator = coordinator_;}
public coordinator getCoordinator() {return myCoordinator;}

//by saurabh

public void printComponents(){
  System.out.println("The components of the digraph "+getName()+ " are:---------->");

  ComponentsInterface cpi = getComponents();
  componentIterator i = cpi.cIterator();
  while(i.hasNext()){
    IOBasicDevs iodb = i.nextComponent();
    System.out.println("Printing the iodb name " + iodb.getName());
  }
}

public boolean checkNameUniqueness(String modelName){
  ComponentsInterface cpi = getComponents();
  componentIterator i = cpi.cIterator();
  String nm;
  while(i.hasNext()){
    nm = (i.nextComponent()).getName();
    if(nm.compareTo(modelName)==0) return false;
  }
  return true;
}


public void printCouprel(couprel cr){
  Iterator i = cr.iterator();
  while(i.hasNext())
    System.out.println("Now printing relations: "+i.next().toString());

}

public void addInport(String modelName, String port){
    //s.s("Inside digraph addInport");
    digraph P = (digraph)getParent();
    if (P != null)
      ((IODevs)P.withName(modelName)).addInport(port);
       else
         s.s("parent is not defined");
       addInportHook1(modelName,port);
}

public void addInportHook1(String modelName, String port){
  //s.s("Inside digraph addInport hook 1");
  s.s("Inport added: "+port+"      component: "+modelName);
}

public void addOutport(String modelName, String port){
  //s.s("Inside digraph addOutport");
  digraph P = (digraph)getParent();
  if (P != null)
    ((IODevs)P.withName(modelName)).addOutport(port);
  else
    s.s("parent is not defined");
  addOutportHook1(modelName,port);
}

public void addOutportHook1(String modelName, String port){
  //s.s("Inside digraph addOutport hook 1");
  s.s("Outport added: "+port+"      component: "+modelName);
}

public void removeInport(String modelName, String port){
  //s.s("Inside digraph removeInport");
   digraph P = (digraph)getParent();
     if (P != null)
     ((IODevs)P.withName(modelName)).removeInport(port);
     else
       s.s("parent is not defined");
     removeInportHook1(modelName,port);
}

public void removeInportHook1(String modelName, String port){
  //s.s("Inside digraph removeInport hook 1");
  s.s("Inport removed: "+port+"      component: "+modelName);
}

public void removeOutport(String modelName, String port){
  //s.s("Inside digraph removeOutport");
   digraph P = (digraph)getParent();
     if (P != null)
     ((IODevs)P.withName(modelName)).removeOutport(port);
     else
       s.s("parent is not defined");
     removeOutportHook1(modelName,port);
}

public void removeOutportHook1(String modelName, String port){
  //s.s("Inside removeOutport hook 1");
  s.s("Outport removed: "+port+"      component: "+modelName);

}

//------------------------------------------------------------------------------
/**
 *  This method adds couplings between any two models belonging to the hierarchical
 *  tree of this coupled model.
 */
public void addHierarchicalCoupling(String src, String p1, String dest, String p2){
   devs srcModel, destModel;
   //first try to find the src model
   srcModel = findModelWithName(src);
   destModel = srcModel.findModelWithName(dest);  // check if the dest is "child" of src model
   if(destModel!=null)  // destination model is "child" of the source model
     ((digraph)(destModel.getParent())).addUpCouplingPath(srcModel.getName(),p1,dest,p2);
   else
     ((digraph)(srcModel.getParent())).findDestToAddCoupling(srcModel,p1,dest,p2);
}

public void findDestToAddCoupling(devs srcModel, String p1, String dest, String p2){
  devs destModel;
  // check destination model is child of this model
  destModel = findModelWithName(dest);
  if(destModel!=null){
    if(destModel==this) //this means the origianl srcModel actually belongs to destModel
      addCoupling(srcModel,p1,this, p2);
    else  // the destModel belongs to a model which is brother model of the srcModel
      ((digraph)(destModel.getParent())).addUpCouplingPath(srcModel.getName(),p1,dest,p2);
  }
  else{ // first adds a coupling path from the srcModel to this model and then goes one layer up
    addOutport(srcModel.getName()+"_"+p1);
    addCoupling(srcModel,p1,this, srcModel.getName()+"_"+p1);
    ((digraph)(getParent())).findDestToAddCoupling(this,srcModel.getName()+"_"+p1,dest,p2);
  }
}

public void addUpCouplingPath(String src, String p1, String dest, String p2){
    devs srcM = (devs)withName(src);
    devs destM = (devs)withName(dest);
    if(srcM!=null){    //this is the output coupling
      if(destM!=null) addCoupling(srcM,p1,destM,p2); // we found the destination
      else{
        if(dest.compareTo(getName())==0) addCoupling(srcM,p1,this,p2);// this model is the destination model
        else{// doesn't find the destination, needs to go one layer upper
          addOutport(src+"_"+p1);
          addCoupling(srcM,p1,this,src+"_"+p1);
          digraph P = (digraph)getParent();
          if(P!=null) P.addUpCouplingPath(getName(), src+"_"+p1, dest, p2);
          else System.out.println("the source or the destination of the coupling cannot be found!");
        }
      }
    }
    else if(destM!=null){  //this is the input coupling
      if(src.compareTo(getName())==0) addCoupling(this,p1,destM,p2);// this model is the src model
      else { // doesn't find the source, needs to go one layer upper
        addInport(dest+"_"+p2);
        addCoupling(this,dest+"_"+p2,destM,p2);
        digraph P = (digraph)getParent();
        if(P!=null) P.addUpCouplingPath(src, p1, getName(), dest+"_"+p2);
        else System.out.println("the source or the destination of the coupling cannot be found!");
      }
    }
    else System.out.println("the source or the destination of the coupling cannot be found!");
}

public devs findModelWithName(String nm){
  if(nm.compareTo(this.getName())==0) return this;
  else{
      componentIterator cit = getComponents().cIterator();
      while (cit.hasNext()){
        devs iod = (devs)(cit.nextComponent());
        devs m = iod.findModelWithName(nm);
        if(m!=null) return m;
      }
      return null;
  }
}

//------------------------------------------------------------------------------
// the following methods are taken from atomic.java. They allow a coupled model
// to initiate a dynamic structure change
// Created by Xiaolin Hu
// Nov. 15, 2005

 public void addDynamicCoupling(IODevs srcM, String p1, IODevs destM, String p2){
      String src = srcM.getName();
      String dest = destM.getName();
      //update its parent model's coupling info database
      addPair(new Pair(src,p1),new Pair(dest,p2));
      // update the corresponding simulator's coupling info database
      coordinator PCoord = getCoordinator();
      PCoord.addCoupling(src,p1,dest,p2);
  }

 public void removeDynamicCoupling(IODevs srcM, String p1, IODevs destM, String p2){
      String src = srcM.getName();
      String dest = destM.getName();
      //update its parent model's coupling info database
      removePair(new Pair(src,p1),new Pair(dest,p2));
      // update the corresponding simulator's coupling info database
      coordinator PCoord = getCoordinator();
      PCoord.removeCoupling(src,p1,dest,p2);
 }

 public void addDynamicModel(IODevs iod){
   if(checkNameUniqueness(iod.getName())){
     add(iod);
     coordinator PCoord = getCoordinator();
     PCoord.setNewSimulator( (IOBasicDevs) iod);
   }
   else System.out.println("Conflict!! -- the model name already exists");
 }

 public void removeDynamicModel(IODevs iod){
   coordinator PCoord = getCoordinator();
   // first remove all the coupling related to that model
   String modelName = iod.getName();
   PCoord.removeModelCoupling(modelName);
   // then remove the model
   remove(iod);
   PCoord.removeModel(iod);
 }

}




