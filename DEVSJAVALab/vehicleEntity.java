package DEVSJAVALab;

import GenCol.*;

public class vehicleEntity extends entity{
  protected double processingTime;
  protected double price;
  protected int priority;
  
  public vehicleEntity(){
	  this("vehicle", 10, 10, 1);
  }
  
  public vehicleEntity(String name, double _procTime, double _price, int _priority){
	  super(name);
	  processingTime = _procTime;
	  price = _price;
	  priority = _priority;
  }
	
  public double getProcessingTime(){
	  return processingTime;
  }
  
  public double getPrice(){
	  return price;
  }
  
  public int getPriority(){
	  return priority;
  }
  
  public String toString(){
	  //return name+"_"+processingTime;
	  return name+"_"+((double)((int)(processingTime*100)))/100;
  }
		
}
