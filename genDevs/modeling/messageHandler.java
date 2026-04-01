package genDevs.modeling;

import GenCol.*;

import java.util.*;
import util.*;


public class messageHandler implements IODevs{
  protected ports inports,outports, removedInports, removedOutports;
  protected message input,output;

  public ports getInports(){
  return inports;
  }
  public ports getOutports(){
  return outports;
  }

  public messageHandler(){
  inports = new ports();
  outports = new ports();
  removedInports = new ports();
  removedOutports = new ports();
  }

  public ExternalRepresentation getExtRep(){return new ExternalRepresentation.ByteArray();}
  public String getName(){return "messageHandler";}
  public Object equalName(String name){return null; }

  public void addInport(String portName){
      inports.add(new port(portName));
  }
  public void addOutport(String portName){
      outports.add(new port(portName));
  }

//by saurabh
  public void removeInport(String portName){
    Iterator ip = inports.iterator();
   while(ip.hasNext()){
      port p = (port)ip.next();
      if(portName.equals(p.getName())){
        //System.out.println("inside the mh class, removing inport....Now, no. of inports"+inports.size());
         //inports.remove(p);
        removedInports.add(p);
        s md = new s();
        md.s("inport removed: "+portName);
      }
    }
//    System.out.println("printing the removed inports");
//    printPorts(removedInports);
    inports.removeAll(removedInports);
  }
  public void removeOutport(String portName){
    Iterator op = outports.iterator();
    while(op.hasNext()){
      port p = (port)op.next();
      if(portName.equals(p.getName())){
        //System.out.println("inside the mh class, removing outport..Now, no. of Out..ports"+outports.size());
        //outports.remove(p);
        removedOutports.add(p);
        s md = new s();
        md.s("Outport removed: "+portName);
      }
    }
//    System.out.println("printing the removed outports");
//    printPorts(removedOutports);
    outports.removeAll(removedOutports);
  }

  public void printPorts(ports col){
    Iterator i = col.iterator();
    while(i.hasNext()){
      port p = (port)i.next();
      System.out.println("port name: "+p.getName());
    }
  }

  public void printInports(){
    printPorts(this.inports);
  }

  public void printOutports(){
    printPorts(this.outports);
  }

  public ContentInterface makeContent(PortInterface p,EntityInterface value){
  return new content((port)p,(entity)value);
  }
  public content makeContent(String p,entity value){  //for use in usual devs
  return new content(p,value);
  }
  public boolean   messageOnPort(MessageInterface x,PortInterface p, ContentInterface c){
  return  x.onPort(p,c);
  }
  public boolean   messageOnPort(message x,String p, int i){ //for use in usual devs
  return x.onPort(p,i);
  }



}