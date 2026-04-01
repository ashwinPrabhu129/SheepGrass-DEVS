Number of PF steps and step interval are defined in the configData.java

Generate observation data and true state data
   -- Run the test.java by instantiating the carWashSys_Real model. 
   -- Case I and Case II random seeds are defined in configData.java
   -- the data are saved by the transducer_Real_dataSaving.java
   -- You may setting the stepInterval (in the configData.java) to be 1.0 to see in detail how the "real system" state changes over time
          
Run particle filter to estimate state
  -- run the PF_skeleton.java 
  -- the DA results are saved by the DAResult.txt file
  
      