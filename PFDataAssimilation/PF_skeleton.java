/* Georgia State University
 * CSC 8840 - Modeling and Simulation Theory and Application
 * This java class is part of a class project that involves using particle filter
 * to carry out data assimilation for a DEVS-based discrete event simulation model
 *
 * Author    : Prof. Xiaolin Hu
 * Date      : 10-14-2024
 */

package PFDataAssimilation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import genDevs.modeling.DevsInterface;
import genDevs.simulation.*;

public class PF_skeleton {
    public double[] weight = new double[configData.numberofparticles]; // save particle's weights -- not normalized
    public double[] normalizedWeight = new double[configData.numberofparticles];
    int[] selectedParticles = new int[configData.numberofparticles];
    stateEntity[] particleStateArray = new stateEntity[configData.numberofparticles];
    stateEntity[] newParticleStateArray = new stateEntity[configData.numberofparticles];

    carWashSys_DA[] particleArray = new carWashSys_DA[configData.numberofparticles];

    // array to store data from the "real" system
    sensorDataEntity[] sensorDataArray = new sensorDataEntity[configData.numberofsteps];
    stateEntity[] realStateArray = new stateEntity[configData.numberofsteps];

    Random globalRand;

    PrintWriter DA_result, step_result;

    boolean batchRun = false;

    // stdv for the measurement noise (e.g., +/- 1 car)
    public double measurementNoise_stdv = 1.0;

    public PF_skeleton() {

        try {
            DA_result = new PrintWriter(new FileOutputStream(configData.DataPathName + "DAResult.txt"), true);
        } catch (FileNotFoundException e) {
            System.out.println("File creation error!!!!!");
        }

        DA_result.println("step" + "\t" + "time"
                + "\t" + "real_workingState"
                + "\t" + "real_elapseTime"
                + "\t" + "real_carQueue"
                + "\t" + "DA_workingState"
                + "\t" + "DA_workingElapsedTime"
                + "\t" + "DA_breakState"
                + "\t" + "DA_breakElapsedTime"
                + "\t" + "DA_combinedElapseTime"
                + "\t" + "DA_carQueue"
                + "\t" + "DA_carQueue_std"
        );
    }

    void startComputing() {
        globalRand = new Random(configData.PF_globalRandSeed);

        // read observation data and store it in the sensorDataArray
        // ++++++++++++++++++++  Add your code below +++++++++++++++++++++++++
        readRealSensorData();

        // run the PF algorithm
        // ++++++++++++++++++++  Add your code below +++++++++++++++++++++++++

        for (int currentStep = 0; currentStep < configData.numberofsteps; currentStep++) {
            System.out.println("---------------- PF algorithm Step: " + currentStep + " ----------------");

            samplingAndComputeWeights(currentStep);

            for (int j = 0; j < configData.numberofparticles; j++) {
                particleStateArray[j] = newParticleStateArray[j];
            }

            normalizeWeights(configData.numberofparticles);

            resampling(configData.numberofparticles, selectedParticles);

            analyzeAndSaveResults(0, currentStep);
        }

        System.out.println("!!!!!!!!!!!!! PF algorithm finished all steps !!!!!!!!!!!!!");
        DA_result.close();
    }

    public void samplingAndComputeWeights(int step) {

        for (int idx = 0; idx < configData.numberofparticles; idx++) {

            carWashSys_DA simModel;

            // --------------- create particle -----------------------------
            String workingState;
            double phaseSigma;
            double elaspedTimeInPhase;
            int carQueueSize;

            if (step == 0) {

                // generate an initial random state
                // ++++++++++++++++++++  Add your code below +++++++++++++++++++++++++

                boolean isWorking = globalRand.nextBoolean();
                double randomTimeFactor = globalRand.nextDouble();

                if (isWorking) {
                    workingState = "working_passive";
                    double maxWorkTime = configData.schedule_workingDuration_emptyQueue;
                    elaspedTimeInPhase = randomTimeFactor * maxWorkTime;
                    phaseSigma = maxWorkTime - elaspedTimeInPhase;
                } else {
                    workingState = "break";
                    double maxBreakTime = configData.schedule_breakDuration;
                    elaspedTimeInPhase = randomTimeFactor * maxBreakTime;
                    phaseSigma = maxBreakTime - elaspedTimeInPhase;
                }
                carQueueSize = globalRand.nextInt(10);
            } else {
                workingState = particleStateArray[selectedParticles[idx]].workingState;
                phaseSigma = particleStateArray[selectedParticles[idx]].sigma;
                elaspedTimeInPhase = particleStateArray[selectedParticles[idx]].elapseTimeInPhase;
                carQueueSize = particleStateArray[selectedParticles[idx]].carQueueSize;

                // Add noise to the state --- we need this Particle Rejuvenation step  because DEVS model is a deterministic model
                // ++++++++++++++++++++  Add your code below +++++++++++++++++++++++++

                int queueJitter = globalRand.nextInt(3) - 1;
                carQueueSize = carQueueSize + queueJitter;
                if (carQueueSize < 0) {
                    carQueueSize = 0;
                }

                double temporalJitter = globalRand.nextGaussian() * 1.0;

                double newElapsedTime = elaspedTimeInPhase + temporalJitter;
                double newRemainingTime = phaseSigma - temporalJitter;

                if (newElapsedTime < 0) {
                    newRemainingTime += newElapsedTime;
                    newElapsedTime = 0;
                }

                if (newRemainingTime < 0) {
                    newRemainingTime = 0;
                }

                elaspedTimeInPhase = newElapsedTime;
                phaseSigma = newRemainingTime;
            }

            // create the simulation model and assign it to simModel
            // ++++++++++++++++++++  Add your code below +++++++++++++++++++++++++

            long particleSeed = globalRand.nextInt();
            Random particleRNG = new Random(particleSeed);

            simModel = new carWashSys_DA("particle" + idx, carQueueSize, workingState, phaseSigma, elaspedTimeInPhase, particleRNG);

            particleArray[idx] = simModel;

            // --------------- run simulation -----------------------------
            coordinator temp_coord = new coordinator(simModel);
            temp_coord.initialize();
            temp_coord.simulate_TN(configData.stepInterval);
            //System.gc();

            // --------------- collect simulation results and store in the newParticleStateArray -----------------------------
            if (simModel.trand.stateEnt == null)
                System.out.println("step=" + step + " idx=" + idx + " State Data Not Received!!!");

            stateEntity sEnt = simModel.trand.stateEnt.copy();
            newParticleStateArray[idx] = sEnt;

            // --------------- compute weight -----------------------------
            double wt = computeWeight(step, simModel.trand.numOfFinishedCars);
            weight[idx] = wt;
            //System.out.println("particle "+idx+" weight="+wt);
        }

    }

    public void analyzeAndSaveResults(int run, int step) {

        // summarize estimation results
        int workingStateCount = 0, breakStateCount = 0;
        double workingElapseTime_sum = 0, breakElapseTime_sum = 0, combinedElapseTime_sum = 0;
        try {
            step_result = new PrintWriter(new FileOutputStream(configData.DataPathName + "run" + run + "_step_" + step + ".txt"), true);
        } catch (FileNotFoundException e) {
            System.out.println("File creation error!!!!!");
        }
        step_result.println("particleIdx" + "\t" + "state" + "\t" + "sigma" + "\t" + "elapseTimeInPhase"
                + "\t" + "carQueueSize" + "\t" + "arrivingCarCount" + "\t" + "departureCarCount");

        double carQueueSizeSum = 0;
        double[] carQArray = new double[configData.numberofparticles];
        for (int i = 0; i < configData.numberofparticles; i++) {
            String pState = particleStateArray[selectedParticles[i]].workingState;
            double sigma = particleStateArray[selectedParticles[i]].sigma;
            double elapseTimeInPhase = particleStateArray[selectedParticles[i]].elapseTimeInPhase;
            int carQueueSize = particleStateArray[selectedParticles[i]].carQueueSize;

            carQueueSizeSum += carQueueSize;
            carQArray[i] = carQueueSize;

            if (pState.startsWith("working")) {
                workingStateCount++;
                workingElapseTime_sum += elapseTimeInPhase;
                combinedElapseTime_sum += elapseTimeInPhase;
            } else if (pState.startsWith("break")) {
                breakStateCount++;
                breakElapseTime_sum += elapseTimeInPhase;
                combinedElapseTime_sum += elapseTimeInPhase;
            }

            int arrivingCarCount = particleArray[selectedParticles[i]].trand.numOfarrivingcars;
            int departureCarCount = particleArray[selectedParticles[i]].trand.numOfFinishedCars;

            step_result.println(i + "\t" + pState + "\t" + sigma + "\t" + elapseTimeInPhase + "\t" + carQueueSize + "\t"
                    + arrivingCarCount + "\t" + departureCarCount);
        }
        step_result.close(); // Close step file

        double aveQueue = carQueueSizeSum / configData.numberofparticles;
        double carQsize_std, carQsize_std_sum = 0;
        for (int i = 0; i < configData.numberofparticles; i++) {
            carQsize_std_sum += (carQArray[i] - aveQueue) * (carQArray[i] - aveQueue);
        }
        carQsize_std = Math.sqrt(carQsize_std_sum / configData.numberofparticles);


        DA_result.println(step + "\t" + step * configData.stepInterval
                + "\t" + ((realStateArray[step].workingState.startsWith("working")) ? 20 : 0)
                + "\t" + realStateArray[step].elapseTimeInPhase
                + "\t" + realStateArray[step].carQueueSize
                + "\t" + (workingStateCount * 1.0 / configData.numberofparticles * 20.0)
                + "\t" + ((workingStateCount != 0) ? workingElapseTime_sum / workingStateCount : 0)
                + "\t" + (breakStateCount * 1.0 / configData.numberofparticles * 20.0)
                + "\t" + ((breakStateCount != 0) ? breakElapseTime_sum / breakStateCount : 0)
                + "\t" + combinedElapseTime_sum / configData.numberofparticles
                + "\t" + aveQueue
                + "\t" + carQsize_std
        );
    }

    public double computeWeight(int step, int numFinishedCars) {
        // compute the weight
        double weight = 0;
        // ++++++++++++++++++++  Add your code below +++++++++++++++++++++++++

        int groundTruthObservation = sensorDataArray[step].finishedCarCount;

        double predictionError = (double) groundTruthObservation - (double) numFinishedCars;
        double errorSquared = predictionError * predictionError;

        double variance = measurementNoise_stdv * measurementNoise_stdv;

        double likelihood = Math.exp(-errorSquared / (2.0 * variance));

        weight = likelihood;

        return weight;
    }

    void normalizeWeights(int totalNumOfParticles) {
        double sumofWeights = 0;
        for (int i = 0; i < totalNumOfParticles; i++) {
            sumofWeights = sumofWeights + weight[i];
        }

        if (sumofWeights == 0) {
            // This is a recovery mechanism to prevent division by zero
            // It can happen if all particles have a weight of 0 (e.g., very bad initial guess)
            System.out.println("Warning: Sum of weights is zero at step " +
                    (globalRand.nextDouble() / totalNumOfParticles)); // Avoid 0
            sumofWeights = 1.0;
            for (int j = 0; j < totalNumOfParticles; j++) {
                normalizedWeight[j] = 1.0 / totalNumOfParticles;
            }
        } else {
            for (int j = 0; j < totalNumOfParticles; j++) {
                normalizedWeight[j] = weight[j] / sumofWeights;
            }
        }
    }

    // the following method implements a standard resampling method
    public void resampling(int totalNumOfParticles, int[] selectedParticles) {
        double u_temperature[] = new double[totalNumOfParticles];
        double q_temperature[] = new double[totalNumOfParticles];

        for (int i = 0; i < totalNumOfParticles; i++) {
            if (i == 0) {
                q_temperature[i] = normalizedWeight[0];
            } else {
                q_temperature[i] = q_temperature[i - 1] + normalizedWeight[i];
            }
        }

        for (int j = 0; j < totalNumOfParticles; j++) {
            u_temperature[j] = globalRand.nextDouble();
        }
        java.util.Arrays.sort(u_temperature);

        int sampleIndex = 0;
        for (int j = 0; j < totalNumOfParticles; j++) {
            while (sampleIndex < totalNumOfParticles - 1 && q_temperature[sampleIndex] < u_temperature[j]) {
                sampleIndex = sampleIndex + 1;
            }
            selectedParticles[j] = sampleIndex;
        }
    }

    public void readRealSensorData() {
        String realSensorDataFile = configData.DataPathName + configData.realSensorDataFileName;
        try {
            FileInputStream MyInputStream = new FileInputStream(realSensorDataFile);
            TextReader input;
            input = new TextReader(MyInputStream);

            //skip the first line
            input.readLine();

            for (int i = 0; i < configData.numberofsteps; i++) {
                //System.out.println("read data for step "+i);
                input.readDouble(); // skip the time
                int arrvCarCount = input.readInt();
                int finishedCarCount = input.readInt();

                int carQueue = input.readInt();
                int workingState = input.readInt();
                double sgm = input.readDouble();
                double elapseTimeInWorkingOrBreak = input.readDouble();
                String aa = input.readLine(); // skip the rest of the line

                sensorDataArray[i] = new sensorDataEntity(i, arrvCarCount, finishedCarCount);
                realStateArray[i] = new stateEntity(carQueue, ((workingState == 20) ? "working" : "break"), sgm, elapseTimeInWorkingOrBreak);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public static void main(String[] args) {
        PF_skeleton PFA = new PF_skeleton();
        PFA.startComputing();
    }

    /// //////////////////////////////////////////////////////////////////////////////
    private double getTruncatedNormalDistribution(double low, double high, double mean, double sigma) {
        double u = mean + sigma * globalRand.nextGaussian();
        while (u < low || u > high) // resample
            u = mean + sigma * globalRand.nextGaussian();
        return u;
    }

    public static double percentile(double[] latencies, double percentile) {
        int index = (int) Math.ceil(percentile / 100.0 * latencies.length);
        return latencies[index - 1];
    }
}