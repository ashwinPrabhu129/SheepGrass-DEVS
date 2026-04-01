package BridgeSegment;

import genDevs.simulation.coordinator;
import genDevs.simulation.heapSim.HeapCoord;

/**
 * The class performing basic test for a bridge system Note: when performing your test, you need to change the BridgeSystem class to yours
 * 
 * @author Haidong
 */
public class Test
{
	public static void main(String[] args)
	{
		// case 1
		AbstractBridgeSystem.BridgeSystemSetting.Bridge3InitialState = AbstractBridgeSystem.BridgeState.WEST_TO_EAST;
		AbstractBridgeSystem.BridgeSystemSetting.Bridge3TrafficLightDurationTime = 100;
		AbstractBridgeSystem.BridgeSystemSetting.Bridge2InitialState = AbstractBridgeSystem.BridgeState.WEST_TO_EAST;
		AbstractBridgeSystem.BridgeSystemSetting.Bridge2TrafficLightDurationTime = 100;
		AbstractBridgeSystem.BridgeSystemSetting.Bridge1InitialState = AbstractBridgeSystem.BridgeState.WEST_TO_EAST;
		AbstractBridgeSystem.BridgeSystemSetting.Bridge1TrafficLightDurationTime = 100;
		
			//case2
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge3InitialState = AbstractBridgeSystem.BridgeState.WEST_TO_EAST;
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge3TrafficLightDurationTime = 5;
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge2InitialState = AbstractBridgeSystem.BridgeState.WEST_TO_EAST;
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge2TrafficLightDurationTime = 5;
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge1InitialState = AbstractBridgeSystem.BridgeState.WEST_TO_EAST;
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge1TrafficLightDurationTime = 5;
//			
//			//case 3
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge3InitialState = AbstractBridgeSystem.BridgeState.EAST_TO_WEST;
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge3TrafficLightDurationTime = 100;
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge2InitialState = AbstractBridgeSystem.BridgeState.EAST_TO_WEST;
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge2TrafficLightDurationTime = 5;
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge1InitialState = AbstractBridgeSystem.BridgeState.EAST_TO_WEST;
//			AbstractBridgeSystem.BridgeSystemSetting.Bridge1TrafficLightDurationTime = 100;
		
		//case 4
//		AbstractBridgeSystem.BridgeSystemSetting.Bridge3InitialState = AbstractBridgeSystem.BridgeState.EAST_TO_WEST;
//		AbstractBridgeSystem.BridgeSystemSetting.Bridge3TrafficLightDurationTime = 30;
//		AbstractBridgeSystem.BridgeSystemSetting.Bridge2InitialState = AbstractBridgeSystem.BridgeState.EAST_TO_WEST;
//		AbstractBridgeSystem.BridgeSystemSetting.Bridge2TrafficLightDurationTime = 30;
//		AbstractBridgeSystem.BridgeSystemSetting.Bridge1InitialState = AbstractBridgeSystem.BridgeState.EAST_TO_WEST;
//		AbstractBridgeSystem.BridgeSystemSetting.Bridge1TrafficLightDurationTime = 30;
		

		// Create a bridge system object
		AbstractBridgeSystem sys = new Homework2025.BridgeSegment.APrabhu.BridgeSystem("a_bridge_system"); // change it to your bridge system class
		
		// Simulate the system
		// HeapCoord r = new HeapCoord(sys);
		coordinator r = new coordinator(sys);
		r.initialize();
		System.out.println("Simulation started");
		r.simulate(120);
		System.out.println("Simulation finished");

		// Show results
		sys.printResults();

//		// Create the base  model that we have developed before and run simulation. We then compare the results from your model with the results from the base model. 
//		// Note: you do not have access to this base model. So you may remove the following code or use your own model as a dummy base model. 
//		AbstractBridgeSystem compare = new hXue.hXue.BridgeSystem("bridge_system"); // you may change it to your bridge system class for testing purpose
//		r = new coordinator(compare);
//		r.initialize();
//		r.simulate(120);
//		
//		compare.printResultComparison(sys);
		
	}

}
