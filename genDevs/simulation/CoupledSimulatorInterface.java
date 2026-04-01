


package genDevs.simulation;

import GenCol.*;

import java.util.*;
import genDevs.modeling.*;


public interface CoupledSimulatorInterface extends
            coreSimulatorInterface
            ,ActivityProtocolInterface
            ,CouplingProtocolInterface
            ,HierParent
                     {
public void putMessages(ContentInterface c);
public void sendMessages();
            }


//public
interface ActivityProtocolInterface {
public void startActivity(ActivityInterface a);
public void returnResultFromActivity(EntityInterface  result);
}


