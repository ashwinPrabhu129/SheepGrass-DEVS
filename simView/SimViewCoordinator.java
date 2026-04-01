/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 3.0 Beta
 *  Date       : 02-22-03
 */

package simView;

import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;
import util.*;

/**
 * A tunable coordinator specialized to interoperate with a SimView.
 */
public class SimViewCoordinator extends TunableCoordinator
{
    /**
     * Holds part of the implementation for this coordinator.
     */
    protected SimViewCoordinatorBase base;
    protected variableStructureViewer variableViewer;

    /**
     * Constructor.
     *
     * See parent constructor for parameter descriptions.
     */
    public SimViewCoordinator(digraph digraph, Listener listener)
    {
        super(digraph, listener);
        if(listener instanceof variableStructureViewer) variableViewer = (variableStructureViewer)listener;
    }

    /**
     * See parent method.
     */
    protected void constructorHook1() {base = new SimViewCoordinatorBase();}

    /**
     * See parent method.
     */
    protected TunableCoupledCoordinator addCoordinatorHook1(digraph digraph)
    {
        return new SimViewCoupledCoordinator(digraph,
            (TunableCoupledCoordinator.Listener)listener);
    }

    /**
     * See parent method.
     */
    protected void addSimulatorHook1(coupledSimulator simulator)
    {
        base.setListenerIntoSimulator(listener, simulator);
    }

    /**
     * See parent method.
     */
    protected coupledSimulator createSimulatorHook1(IOBasicDevs devs)
    {
        return base.createSimulator(devs);
    }

    // for variable structure  Xiaolin Hu, Febrary 2, 2003
    public void addCoupling(String src, String p1, String dest, String p2){
      IODevs srcModel = myCoupled.withName(src);
      if(srcModel==null){
        if(src.compareTo(myCoupled.getName())==0) srcModel=(IODevs)myCoupled;
        else return;
      }
      IODevs destModel = myCoupled.withName(dest);
      if(destModel==null){
        if(dest.compareTo(myCoupled.getName())==0) destModel=(IODevs)myCoupled;
        else return;
      }
      super.addCoupling(src,p1,dest,p2);
      variableViewer.couplingAdded(srcModel,p1,destModel,p2);
    }

    public void removeCoupling(String src, String p1, String dest, String p2){
      IODevs srcModel = myCoupled.withName(src);
      if(srcModel==null){
        if(src.compareTo(myCoupled.getName())==0) srcModel=(IODevs)myCoupled;
        else return;
      }
      IODevs destModel = myCoupled.withName(dest);
      if(destModel==null){
        if(dest.compareTo(myCoupled.getName())==0) destModel=(IODevs)myCoupled;
        else return;
      }
      super.removeCoupling(src,p1,dest,p2);

//      s msg  = new s();
//      msg.s("printing inside the SimviewCoordinator-----------------");
//      super.myCoupled.printCouprel(super.myCoupled.getCouprel());


      variableViewer.couplingRemoved(srcModel,p1,destModel,p2);


    }

public void setNewSimulator(IOBasicDevs iod){
    if(iod instanceof atomic){    //do a check on what model is
        coupledSimulator s = createSimulatorHook1(iod);
        addSimulatorHook1(s);
        s.setRootParent(this);
        newSimulators.add(s);
        internalModelTosim.put(iod.getName(),s);
        s.initialize(getCurrentTime());
    }
    else if(iod instanceof digraph){
        coupledCoordinator s = addCoordinatorHook1((digraph)iod);
        s.setRootParent(this);
        newSimulators.add(s);
        internalModelTosim.put(iod.getName(),s);
        s.initialize(getCurrentTime());
    }
    if(myCoupled instanceof ViewableDigraph && iod instanceof ViewableComponent)
        variableViewer.modelAdded((ViewableComponent)iod,(ViewableDigraph)myCoupled);
}

public void removeModel(IODevs model){
  String modelName = model.getName();
  if(internalModelTosim.get(modelName) instanceof CoupledSimulatorInterface){
    coupledSimulator sim = (coupledSimulator)internalModelTosim.get(modelName);
    deletedSimulators.add(sim);
    internalModelTosim.remove(modelName);
  }
  else if(internalModelTosim.get(modelName) instanceof CoupledCoordinatorInterface){
    coupledCoordinator sim = (coupledCoordinator)internalModelTosim.get(modelName);
    deletedSimulators.add(sim);
    internalModelTosim.remove(modelName);
  }
  if(myCoupled instanceof ViewableDigraph && model instanceof ViewableComponent)
    variableViewer.modelRemoved((ViewableComponent)model,(ViewableDigraph)myCoupled);
}


}