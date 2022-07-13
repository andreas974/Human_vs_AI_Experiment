package edu.kit.exp.impl.continuousCompetition.client;

import edu.kit.exp.impl.continuousCompetition.server.ContinuousCompetitionMarketDataCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.*;

/**
 * Created by dschnurr on 18.03.15.
 */

public class AgentStrategy {

    private static final Logger log4j = LogManager.getLogger(AgentStrategy.class.getName());
    private final AgentCore agentCore;

    private int myRole;
    public int[] actionsofotherfirm = new int[3600];
    private int actionSpace = 21;
    private double[][] q = new double[actionSpace][actionSpace];
    private int state;
    private Parameter parameter;
    private final Random rnd = new Random(42);
    private int numberOfEpisodes;       // Number of episodes that have been conducted.


    public AgentStrategy(AgentCore agentCore) {
        log4j.info("AgentStrategy(): {}", agentCore);
        this.agentCore = agentCore;
        log4j.info("this.AgentInterface = {}", this.agentCore);
        if (agentCore.isTriopolyTreatment==false) {
            if (agentCore.isCournotTreatment==false){
                parameter = new AgentStrategy.Parameter(0.165, 0.99);
            }
            else if (agentCore.isCournotTreatment==true){
                parameter = new AgentStrategy.Parameter(0.2, 0.97);
            }
        }
        else if (agentCore.isTriopolyTreatment==true){
            if (agentCore.isCournotTreatment==false){
                parameter = new AgentStrategy.Parameter(0.140, 0.99, 1);
            }
            else if (agentCore.isCournotTreatment==true){
                parameter = new AgentStrategy.Parameter(0.020, 0.98, 1);
            }
        }
    }




    void init(ContinuousCompetitionParamObject initialMarketUpdate) {
        log4j.info("Initialize information based on received initial market update.");

        // Optional: read and process information conveyed by the initial marketUpdate
        // For example: read role code
        myRole = initialMarketUpdate.getRoleCode();

        // Todo: set inital action for the start of the simulation
        // For example: set action 25 as the initial action

        int initAction = 0;
        /*Sequential Matrizes
        if (agentCore.isTriopolyTreatment==false) {
            if (agentCore.isCournotTreatment==false){
                if (myRole==0){
                    initAction = 41;
                }
                else if (myRole==1){
                    initAction = 43;
                }
            }
            else if (agentCore.isCournotTreatment==true){
                if (myRole==0){
                    initAction = 52;
                }
                else if (myRole==1){
                    initAction = 48;
                }
            }
        }
        else if (agentCore.isTriopolyTreatment==true){
            if (agentCore.isCournotTreatment==false){
                if (myRole==0){
                    initAction = 37;
                }
                else if (myRole==1){
                    initAction = 37;
                }
                else if (myRole==2){
                    initAction = 36;
                }
            }
            else if (agentCore.isCournotTreatment==true){
                if (myRole==0){
                    initAction = 55;
                }
                else if (myRole==1){
                    initAction = 49;
                }
                else if (myRole==2){
                    initAction = 46;
                }
            }
        }*/

        if (agentCore.isTriopolyTreatment==false) {
            if (agentCore.isCournotTreatment==false){
                if (myRole==0){
                    initAction = 10;
                    //initAction =41;
                }
                else if (myRole==1){
                    initAction = 10;
                    //initAction =43;
                }
            }
            else if (agentCore.isCournotTreatment==true){
                if (myRole==0){
                    initAction = 55;
                }
                else if (myRole==1){
                    initAction = 45;
                }
            }
        }
        else if (agentCore.isTriopolyTreatment==true){
            if (agentCore.isCournotTreatment==false){
                if (myRole==0){
                    initAction = 54;
                }
                else if (myRole==1){
                    initAction = 54;
                }
                else if (myRole==2){
                    initAction = 54;
                }
            }
            else if (agentCore.isCournotTreatment==true){
                if (myRole==0){
                    initAction = 53;
                }
                else if (myRole==1){
                    initAction = 53;
                }
                else if (myRole==2){
                    initAction = 63;
                }
            }
        }

        readcsv();
        initQMatrix(readcsv());
        printq();
        System.out.println("Parameter gesetzt auf Alpha: " + parameter.alpha + " und Delta: " + parameter.delta);

        // Initial action is sent to server
        agentCore.updateAction(initAction);
        log4j.info("Set initial action to {}", initAction);


    }


    // Market updates are received every 500ms
    void processMarketUpdate(ContinuousCompetitionParamObject marketUpdate) {
        log4j.info("Received marketUpdate (id: {})", marketUpdate.getCountId());

        // Todo: implement information processing
        // countId identifies the marketUpdate and is incremented for every new marketUpdate
        int countId = marketUpdate.getCountId();
        log4j.info("(int) countID: {}", countId);

        // read average market action
        double averageMarketAction = marketUpdate.getaMarket();
        log4j.info("(double) pMarket: {}", averageMarketAction);
        log4j.info("(int) pMarket: {}",(int) averageMarketAction);

        // Store actions of all four firms in array
        int[] actions = new int[4];
        actions[0] = marketUpdate.getaFirmA();
        actions[1] = marketUpdate.getaFirmB();
        actions[2] = marketUpdate.getaFirmC();
        actions[3] = marketUpdate.getaFirmD();
        // The current own action can then be retrieved as follows:
        int myLastAction = actions[myRole];
        log4j.info("Own action in last round: {}", myLastAction);

        int newAction = myLastAction;
        // You may want to set a new action based on the received information, e.g. the average market price
        // newAction = (int) averageMarketAction;

        //Check Role for Agent:
        //System.out.println("Rolle des Agenten:" + myRole);

        double profitcoefficient = 1;
        if (agentCore.isTriopolyTreatment==false){
            if (agentCore.isCournotTreatment==false){
                profitcoefficient = 1.25;
            }
            else if (agentCore.isCournotTreatment==true){
                profitcoefficient = 1;
            }
        }
        else if (agentCore.isTriopolyTreatment==true){
            if (agentCore.isCournotTreatment==false){
                profitcoefficient = 2.3625;
            }
            else if (agentCore.isCournotTreatment==true){
                profitcoefficient = 1.5625;
            }
        }

        //System.out.println("Der Profitkoeffizient ist: " + profitcoefficient);


        if (agentCore.isTriopolyTreatment == false) {
            // Set new action according to the Q-Matrix and update Q-Matrix
            if (myRole == 0) {
                updateMatrix(myLastAction, marketUpdate.getaFirmA()*marketUpdate.getoFirmA()*profitcoefficient, marketUpdate);
                newAction=runEpisode(marketUpdate);
            } else if (myRole == 1) {
                updateMatrix(myLastAction, marketUpdate.getaFirmB()*marketUpdate.getoFirmB()*profitcoefficient, marketUpdate);
                newAction=runEpisode(marketUpdate);
            }


        }
        else if(agentCore.isTriopolyTreatment==true){
            if (myRole == 0) {
                updateMatrix(myLastAction, marketUpdate.getaFirmA()*marketUpdate.getoFirmA()*profitcoefficient, marketUpdate);
                newAction=runEpisode(marketUpdate);
            } else if (myRole == 1) {
                updateMatrix(myLastAction, marketUpdate.getaFirmB()*marketUpdate.getoFirmB()*profitcoefficient, marketUpdate);
                newAction=runEpisode(marketUpdate);
            }
            else if (myRole == 2) {
                updateMatrix(myLastAction, marketUpdate.getaFirmC()*marketUpdate.getoFirmC()*profitcoefficient, marketUpdate);
                newAction=runEpisode(marketUpdate);
            }
        }

        log4j.info("Current Q-value: {}", q[state][myLastAction]);

        //Just to check Q-Matrix Updates
        //printq();


        // newAction = (int) marketUpdate.getaFirmA();
        // Alternatively you may want to set a random action every 10 seconds (10 * 2 * 500ms updates => countid = 20)
        // if (countId % 20 == 0) {
        //     newAction = setNewRandomAction();
        //}


        //Fill array of actions of other firm
        if (myRole == 0){
            actionsofotherfirm[countId]= marketUpdate.getaFirmB();
        }
        else {
            actionsofotherfirm[countId] = marketUpdate.getaFirmA();
        }

        //just a quick check
        /*for (int i = 0; i < 500; i++) {
            System.out.println("Action in Periode " + i + " ist " + actionsofotherfirm[i]);
        }*/

        // Send updated action to server but only if action of other participant changes or every 20 seconds

        if (countId > 1) {

            if (getState(marketUpdate) != actionsofotherfirm[countId - 1] || countId % 40 == 0 || countId == 2) {
                agentCore.updateAction(newAction);

                log4j.info("Updated action to {}", newAction);
            } else
                log4j.info("No action update in this period");
        }

        //agentCore.updateAction(newAction);

        /*for (int i = 10; i < 11; i++) {
            for (int j = 0; j < q.length; j++) {
                System.out.println(i + ". Zeile und " + j + ".Spalte, Wert:" + (q[i][j]) + " ");
            }
        }
        agentCore.updateAction(newAction);*/

    }



    /*int setNewRandomAction() {

        // Todo: implement action update based on internal processing (e.g. learning algorithm)
        // Simple example: set a random action between 0 and 100:
        Random rand = new Random();
        int randomAction = rand.nextInt(101);

        log4j.info("Created new random action {}", randomAction);
        return randomAction;
    }*/


    /**
     * Calculates the state of this Q-Learning algorithm object
     *
     * @param marketUpdate all other firms' actions.
     * @return state of this Q-Learning algorithm.
     */
    private int getState(ContinuousCompetitionParamObject marketUpdate) {
        int result = 0;
        if (agentCore.isTriopolyTreatment==false) {
            if (myRole == 0) {
                result = (int) marketUpdate.getaFirmB();
            }
            else if (myRole == 1) {
                result = (int) marketUpdate.getaFirmA();
            }
        }
        else if (agentCore.isTriopolyTreatment==true){
            if (myRole == 0) {
                if (marketUpdate.getaFirmB()<marketUpdate.getaFirmC()){
                    result = (int) (parameter.gamma*marketUpdate.getaFirmB()+(1- parameter.gamma)* marketUpdate.getaFirmC());
                }
                else {
                    result = (int) (parameter.gamma*marketUpdate.getaFirmC()+(1- parameter.gamma)* marketUpdate.getaFirmB());
                }
            }
            else if (myRole == 1) {
                if (marketUpdate.getaFirmA()<marketUpdate.getaFirmC()){
                    result = (int) (parameter.gamma*marketUpdate.getaFirmA()+(1- parameter.gamma)* marketUpdate.getaFirmC());
                }
                else {
                    result = (int) (parameter.gamma*marketUpdate.getaFirmC()+(1- parameter.gamma)* marketUpdate.getaFirmA());
                }
            }
            else if (myRole == 2) {
                if (marketUpdate.getaFirmA()<marketUpdate.getaFirmB()){
                    result = (int) (parameter.gamma*marketUpdate.getaFirmA()+(1- parameter.gamma)* marketUpdate.getaFirmB());
                }
                else {
                    result = (int) (parameter.gamma*marketUpdate.getaFirmB()+(1- parameter.gamma)* marketUpdate.getaFirmA());
                }
            }
        }


        //This uses the average for the other firms
        /*else if (agentCore.isTriopolyTreatment==true){
            if (myRole == 0) {
                result = (marketUpdate.getaFirmB()+marketUpdate.getaFirmC())/2;
            }
            else if (myRole == 1) {
                result = (marketUpdate.getaFirmA()+marketUpdate.getaFirmC())/2;
            }
            else if (myRole == 2) {
                result = (marketUpdate.getaFirmB()+marketUpdate.getaFirmC())/2;
            }
        }*/
        return result;
    }


    /**
     * Selects an action according to epsilon-greedy policy. Here, the perceived optimal action is chosen
     * if a random value is greater than the current epsilon (exploit), while a random action gets chosen otherwise
     * (explore).
     *
     * @param state current state of the firm deploying this Q-Learning algorithm.
     * @return index determined by the epsilon-greedy policy.
     */
    private int selectAction(int state){
        int index;

        index = getMaxActionIndex(state);


        //No Explore strategy -> Agent should always exploit (Calvano et al., 2020)
        /*if (rnd.nextDouble() > parameter.epsilon) {
            // Exploit
            index = getMaxActionIndex(state);
        } else {
            // Explore
            index = rnd.nextInt(101);
        }*/

        return index;
    }


    /**
     * Get the action's index of the Q matrix with the highest (optimal) value given a certain state.
     *
     * @param state state to which the optimal action is requested.
     * @return the index of the optimal action given a certain state.
     */
    private int getMaxActionIndex(int state) {
        double value;
        double maxValue = 0;
        int maxIndex = 0;

        for (int i = 0; i < actionSpace; i++) {
            value = q[state][i];

            if (value > maxValue) {
                maxValue = value;
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * For simultaneous interaction:
     * Updates a cell of the Q-matrix based on the previous action (price/quantity) and reward (profit)
     * of the corresponding firm and by determining the new state based on the latest actions (prices/quantities)
     * of all other firms.
     *
     * @param marketUpdate latest prices/quantities of all other other firms to determine the new state.
     * @param action previous price/quantity chosen by the corresponding firm.
     * @param reward previous profit generated by the corresponding firm after choosing the action (price/quantity).
     */
    void updateMatrix(int action, Double reward, ContinuousCompetitionParamObject marketUpdate) {
        // Get the new state based on all other firms' actions.
        int newState = getState(marketUpdate);

        // Observe maxQ for the new state.
        double nextMaxQ = q[newState][getMaxActionIndex(newState)];

        // Update corresponding Q-matrix cell but only of current state
        //q[state][action] = (1 - parameter.alpha) * q[state][action] + parameter.alpha * (reward + parameter.delta * nextMaxQ);

        // Update Q-matrix cell but of maximum action
        q[state][getMaxActionIndex(state)] = (1 - parameter.alpha) * q[state][action] + parameter.alpha * (((getMaxActionIndex(state)*ContinuousCompetitionMarketDataCalculator.scale) *(60-1.8*(getMaxActionIndex(newState)*ContinuousCompetitionMarketDataCalculator.scale)+1.2*(newState*ContinuousCompetitionMarketDataCalculator.scale))*1.25) + parameter.delta * nextMaxQ);
        //Update all Cells of Current State (What if Scenario for Agent)
        /*for (int i=0; i<actionSpace; i=i+5){
            q[state][i] = (1 - parameter.alpha) * q[state][i] + parameter.alpha * ((i*(60-1.8*i+1.2*state)*1.25) + parameter.delta * nextMaxQ);
        }*/
    }


    /**
     * Carries out one episode of the Q-Learning algorithm. To do so, one first has to determine the current state
     * (= (weighted) price/quantity of the competing firm(s)) and select an action by using a policy
     * (either by exploration or exploitation). Finally, the counter for the number of episodes is increased,
     * beta (necessary for the action-selection policy) is decreased, and the chosen action is returned.
     *
     * @param marketUpdate prices/quantities of all other other firms to determine the state.
     * @return result (= price/quantity) of this Q-Learning episode.
     */
    int runEpisode(ContinuousCompetitionParamObject marketUpdate) {

        state = getState(marketUpdate);

        // Choose A from S using policy.
        int action = selectAction(state);
        numberOfEpisodes++;
        // Decrease epsilon
        parameter.decreaseEpsilon(numberOfEpisodes);

        return action;
    }

    public static class Parameter {
        private int maxNumberOfPeriods = 1800000;
        private double alpha;                   // Learning factor
        private double delta;                   // Discount factor

        private double gamma = 1.0;                   // Weighting factor

        private double epsilon = 1;             // Probability of exploration
        private double beta = 1 - Math.pow(0.000001, epsilon / maxNumberOfPeriods); // Factor for decreasing epsilon

        public Parameter(double alpha, double delta, double gamma) {
            this.alpha = alpha;
            this.delta = delta;
            this.gamma = gamma;
        }

        public Parameter(double alpha, double delta) {
            this.alpha = alpha;
            this.delta = delta;
        }

        public double getAlpha() {
            return alpha;
        }

        public double getDelta() {
            return delta;
        }

        public double getGamma() {
            return gamma;
        }

        public double getBeta() {
            return beta;
        }

        public void decreaseEpsilon(int numberOfEpisodes) {
            epsilon = Math.pow(1 - beta, numberOfEpisodes);
        }
    }

    /**
     * Read the csv file from Sebastian's Q-Learning Agent and store all the values in an array called QMatrix
     *
     * @return QMatrix is an Array of all the QMatrix values
     */
    public double[] readcsv(){

        String path = "";

        if (agentCore.isTriopolyTreatment) {
            if (agentCore.isCournotTreatment) {
                path = "edu/kit/exp/common/resources/SEQ_Q_3_Export.csv";
            }
            else if (!agentCore.isCournotTreatment){
                path = "edu/kit/exp/common/resources/SEQ_P_3_Export.csv";
            }
        }
        else if (!agentCore.isTriopolyTreatment) {
            if (agentCore.isCournotTreatment) {
                path = "edu/kit/exp/common/resources/SEQ_Q_2_Export.csv";
            }
            else if (!agentCore.isCournotTreatment){
                //AverageMatrix
                //path = "ExpCommon/src/edu/kit/exp/common/resources/QMatrix21Avg.csv";
                //Max Matrix:
                path = "ExpCommon/src/edu/kit/exp/common/resources/QMatrix_final_with1.0_FirmA.csv";
                //path = "ExpCommon/src/edu/kit/exp/common/resources/QMatrixfull_reduced_with0.998.csv";

            }
        }

        // Old Reader with Matrix in one column
        /*BufferedReader reader = null;
        String line = "";
        double[] QMatrix_temp = new double[30603];
        double[] QMatrix = new double[10201];

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            int i = 0;
            while((line = reader.readLine()) != null) {
                String[] row = line.split(":");
                QMatrix_temp[i] = Double.parseDouble(row[1]);
                i++;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/

        // Read CSV Matrix with new appearance
        //Full QMatrix
        //double[] QMatrix_temp = new double[(actionSpace*actionSpace)];
        //double[] QMatrix = new double[(actionSpace*actionSpace)];

        //Reduced QMatrix
        double[] QMatrix = new double[(actionSpace*actionSpace)];
        String delimiter = ",";
        String line;

        //Full QMatrix
        //for (int i = 0; i<(actionSpace*actionSpace); i++) {
            //for (int k = 0; k<actionSpace; k++)
        //Reduced QMatrix
        for (int i = 0; i<(actionSpace*actionSpace); i++) {
            for (int k = 0; k<actionSpace; k++)
                try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                    while ((line = br.readLine()) != null) {

                        //List values = Arrays.asList(line.split(delimiter));
                        //lines.add(values);
                        String[] row = line.split(delimiter);
                        QMatrix[i] = Double.parseDouble(row[k]);
                        //System.out.println("i= " + i + " k= " + k + "QMatrix= " + QMatrix[i]);
                        i++;
                    }

                    //lines.forEach(l -> System.out.println(l));
                } catch (Exception e) {
                    System.out.println(e);
                }

        }

        /*//Matrix Check
        for (int i = 0; i < QMatrix_temp.length; i++){
            System.out.println(QMatrix_temp[i]);
        }*/

        //Assign Q-Matrix to firm, same Q-Matrix for all roles

        //Full QMatrix
        //for (int i = 0, j = 0; i < (actionSpace*actionSpace) && j < (actionSpace*actionSpace); i++, j++) {
        //Reduced QMatrix



        //Assign Q-Matrix to firm, different Q-Matrices for different roles


        /*if (myRole == 0) {
            for (int i = 0, j = 0; i < (actionSpace*actionSpace) && j < (actionSpace*actionSpace); i++, j++) {
                QMatrix[j] = QMatrix_temp[i];
            }
        } else if (myRole == 1) {
            for (int i = (actionSpace*actionSpace), j = 0; i < (2*(actionSpace*actionSpace)) && j < (actionSpace*actionSpace); i++, j++) {
                QMatrix[j] = QMatrix_temp[i];
            }
        } else if (myRole == 2) {
            for (int i = (2*(actionSpace*actionSpace)), j = 0; i < (3*(actionSpace*actionSpace)) && j < (actionSpace*actionSpace); i++, j++) {
                QMatrix[j] = QMatrix_temp[i];
            }
        }*/

        return QMatrix;

    }



    /*//Method just to check whether Matrix Input was correct

    public void printMatrix(double[] matrix){
        for (int i = 0; i < matrix.length; i++){
            System.out.println(matrix[i]);
       }
    }*/

    public void initQMatrix(double[] qliste) {
        //Full QMatrix
        for (int i = 0; i < q.length; i++) {
            for (int j = 0; j < q.length; j++) {
        //Reduced QMatrix
        //for (int i = 0; i < q.length; i=i+5) {
            //for (int j = 0; j < q.length; j=j+5) {
                //Full Matrix with Actionspace * Actionspace
                q[j][i] = qliste[(j % q.length + i * q.length)];
                //Reduced Matrix:
                //q[j][i] = qliste[(j/5) % 21 + (i/5) * 21];
            }
        }
    }

    public void printq(){

        System.out.println("Neue Matrix:");
        for (int i=0; i<q.length; i++)
            for (int j = 0; j < q.length; j++) {
                System.out.println(i + ". Zeile und " + j + ".Spalte, Wert:" + q[i][j]);
            }


    }
}




