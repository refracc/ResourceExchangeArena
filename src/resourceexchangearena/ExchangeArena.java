package resourceexchangearena;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

/**
 * The arena is the core of the simulation, this is where all the resource exchanges take place.
 */
public class ExchangeArena {
    // The current version of the simulation, used to organise output data.
    private static final String RELEASE_VERSION = "v1.0";

    // Constants defining the scope of the simulation.
    private static final int SIMULATION_RUNS = 50;
    private static final int DAYS = 50;
    private static final int EXCHANGES = 200;
    private static final int POPULATION_SIZE = 96;
    private static final int MAXIMUM_PEAK_CONSUMPTION = 16;
    static final int UNIQUE_TIME_SLOTS = 24;

    // Constants representing the available agent types for the simulation.
    static final int SELFISH = 1;
    static final int SOCIAL = 2;

    // Create a single Random object for generating random numerical data for the simulation.
    static Random random = new Random();

    // List of all the Agents that are part of the current simulation.
    static List<Agent> agents = new ArrayList<>();

    // List of all the possible allocations that exist in the current simulation.
    private static List<Integer> availableTimeSlots = new ArrayList<>();

    /**
     * This is the main method which runs the entire ResourceExchangeArena simulation.
     *
     * @param args Unused.
     * @exception IOException On input error.
     * @see IOException
     */
    public static void main(String[] args) throws IOException {
        // Agent types that will be simulated.
        int[] agentTypes = {SELFISH, SOCIAL};

        // Days that will have the Agents average satisfaction over the course of the day,
        // and satisfaction distribution at the end of the day visualised.
        int[] daysOfInterest = {1,25,50};

        // Array of the unique agent types used in the simulation.
        ArrayList<Integer> uniqueAgentTypes = new ArrayList<>();
        for (int type : agentTypes) {
            if (!uniqueAgentTypes.contains(type)) {
                uniqueAgentTypes.add(type);
            }
        }

        // Sort the agent types so that they are ordered correctly in the output csv files.
        Collections.sort(uniqueAgentTypes);

        int numberOfEachAgentType = POPULATION_SIZE / agentTypes.length;

        // Array lists used to temporarily store data before averaging and adding it to csv files.
        ArrayList<ArrayList<Double>> endOfDayAverageSatisfactions = new ArrayList<>();
        ArrayList<ArrayList<Double>> endOfRoundAverageSatisfactions = new ArrayList<>();
        ArrayList<ArrayList<Double>> endOfDayIndividualSatisfactions = new ArrayList<>();

        // Create a unique name that will pair files from the same run of the simulation.
        long uniqueTag = System.currentTimeMillis();

        // Create directories to store the data output by the simulation.
        String rawDataOutputFolder =
                "outputData/" + RELEASE_VERSION + "/" + uniqueTag + "/rawData";
        Path rawDataOutputPath = Paths.get(rawDataOutputFolder);
        Files.createDirectories(rawDataOutputPath);

        String prePreparedDataOutputFolder =
                "outputData/" + RELEASE_VERSION + "/" + uniqueTag + "/prePreparedData";
        Path prePreparedDataOutputPath = Paths.get(prePreparedDataOutputFolder);
        Files.createDirectories(prePreparedDataOutputPath);

        // Create an identifying filename containing the seed and types of agents in the simulation to form
        // the basis of the filenames for all data output by the simulation.
        StringBuilder fileName = new StringBuilder();
        for (Integer type : uniqueAgentTypes) {
            fileName.append(getHumanReadableAgentType(type));
        }
        fileName.append("_");
        fileName.append(uniqueTag);

        // Stores the average satisfaction of each Agent type at the end of each day, as well
        // as the optimum average satisfaction and the satisfaction if allocations remained random.
        File averageFile = new File(
                rawDataOutputFolder ,
                "endOfDayAverages_" + fileName + ".csv");

        FileWriter averageCSVWriter = new FileWriter(averageFile);

        averageCSVWriter.append("Simulation Run");
        averageCSVWriter.append(",");
        averageCSVWriter.append("Day");
        averageCSVWriter.append(",");
        averageCSVWriter.append("Random (No exchange)");
        averageCSVWriter.append(",");
        averageCSVWriter.append("Optimum (No exchange)");
        for (Integer type : uniqueAgentTypes) {
            averageCSVWriter.append(",");
            averageCSVWriter.append(getHumanReadableAgentType(type));
        }
        averageCSVWriter.append("\n");

        // Stores the average satisfaction of each Agent type at the end of each day, as well
        // as the optimum average satisfaction and the satisfaction if allocations remained random.
        // Values are averaged over multiple simulation runs rather than stored separately.
        File prePreparedAverageFile = new File(
                prePreparedDataOutputFolder ,
                "prePreparedEndOfDayAverages_" + fileName + ".csv");

        FileWriter prePreparedAverageCSVWriter = new FileWriter(prePreparedAverageFile);

        prePreparedAverageCSVWriter.append("Day");
        prePreparedAverageCSVWriter.append(",");
        prePreparedAverageCSVWriter.append("Random (No exchange)");
        prePreparedAverageCSVWriter.append(",");
        prePreparedAverageCSVWriter.append("Optimum (No exchange)");
        for (Integer type : uniqueAgentTypes) {
            prePreparedAverageCSVWriter.append(",");
            prePreparedAverageCSVWriter.append(getHumanReadableAgentType(type));
        }
        prePreparedAverageCSVWriter.append("\n");

        // Stores the satisfaction of each individual Agent at the end of every round throughout the simulation.
        File individualFile = new File(
                rawDataOutputFolder,
                "duringDayAverages_" + fileName + ".csv");

        FileWriter individualCSVWriter = new FileWriter(individualFile);

        individualCSVWriter.append("Simulation Run");
        individualCSVWriter.append(",");
        individualCSVWriter.append("Day");
        individualCSVWriter.append(",");
        individualCSVWriter.append("Round");
        individualCSVWriter.append(",");
        individualCSVWriter.append("Agent ID");
        individualCSVWriter.append(",");
        individualCSVWriter.append("Agent Type");
        individualCSVWriter.append(",");
        individualCSVWriter.append("Satisfaction");
        individualCSVWriter.append("\n");

        // Stores the satisfaction of each individual Agent at the end of every round throughout the simulation.
        // Only stores data for days in the daysOfInterest array and averages data over multiple simulation runs.
        File prePreparedIndividualFile = new File(
                prePreparedDataOutputFolder,
                "prePreparedDuringDayAverages_" + fileName + ".csv");

        FileWriter prePreparedIndividualCSVWriter = new FileWriter(prePreparedIndividualFile);

        prePreparedIndividualCSVWriter.append("Day");
        prePreparedIndividualCSVWriter.append(",");
        prePreparedIndividualCSVWriter.append("Round");
        prePreparedIndividualCSVWriter.append(",");
        prePreparedIndividualCSVWriter.append("Agent Type");
        prePreparedIndividualCSVWriter.append(",");
        prePreparedIndividualCSVWriter.append("Satisfaction");
        prePreparedIndividualCSVWriter.append("\n");

        // Stores the satisfaction of each individual Agent at the end of each of the days in the daysOfInterest array.
        // Satisfactions are averaged over multiple simulation runs and can be separated by the Agents Type in order
        // to produce box and whisker plots of the satisfaction distributions.
        File prePreparedBoxPlotFile = new File(
                prePreparedDataOutputFolder,
                "prePreparedBoxPlotData_" + fileName + ".csv");


        FileWriter prePreparedBoxPlotCSVWriter = new FileWriter(prePreparedBoxPlotFile);

        prePreparedBoxPlotCSVWriter.append("Day");
        prePreparedBoxPlotCSVWriter.append(",");
        prePreparedBoxPlotCSVWriter.append("Agent Type");
        prePreparedBoxPlotCSVWriter.append(",");
        prePreparedBoxPlotCSVWriter.append("Satisfaction");
        prePreparedBoxPlotCSVWriter.append("\n");

        for (int i = 1; i <= SIMULATION_RUNS; i++) {

            // The current system time is used to generate a replicable seed.
            long seed = System.currentTimeMillis();
            random.setSeed(seed);

            int agentType = 0;
            int agentsOfThisType = 0;

            // Create the Agents for the simulation.
            for (int j = 1; j <= POPULATION_SIZE; j++){
                if (agentsOfThisType >= numberOfEachAgentType) {
                    agentType++;
                    agentsOfThisType = 0;
                }
                new Agent(j, agentTypes[agentType]);
                agentsOfThisType++;
            }

            // Initialise each Agents relations with each other Agent.
            for (Agent a: agents) {
                a.initializeFavoursStore();
            }

            // Create a copy of the Agents list that can be shuffled so Agents act in a random order.
            List<Agent> shuffledAgents = new ArrayList<>(agents);

            for (int j = 1; j <= DAYS; j++) {
                // Fill the available time slots with all the slots that exist each day.
                for (int k = 1; k <= UNIQUE_TIME_SLOTS; k++) {
                    for (int l = 1; l <= MAXIMUM_PEAK_CONSUMPTION; l++) {
                        availableTimeSlots.add(k);
                    }
                }
                // Agents start the day by requesting and receiving an allocation of time slots.
                Collections.shuffle(shuffledAgents, random);
                for (Agent a : shuffledAgents) {
                    ArrayList<Integer> requestedTimeSlots = a.requestTimeSlots();
                    ArrayList<Integer> allocatedTimeSlots = getRandomInitialAllocation(requestedTimeSlots);
                    a.receiveAllocatedTimeSlots(allocatedTimeSlots);
                }

                // The random and optimum average satisfaction scores are calculated before exchanges take place.
                double averageSatisfaction = averageAgentSatisfaction();
                double optimumSatisfaction = optimumAgentSatisfaction();

                averageCSVWriter.append(String.valueOf(seed));
                averageCSVWriter.append(",");
                averageCSVWriter.append(String.valueOf(j));
                averageCSVWriter.append(",");
                averageCSVWriter.append(String.valueOf(averageSatisfaction));
                averageCSVWriter.append(",");
                averageCSVWriter.append(String.valueOf(optimumSatisfaction));

                for (int k = 1; k <= EXCHANGES; k++) {
                    ArrayList<ArrayList<Integer>> advertisingBoard = new ArrayList<>();

                    // Reset the check for whether each Agent has made an interaction this round.
                    for (Agent a: agents) {
                        a.setMadeInteraction(false);
                    }

                    // Exchanges start by Agents advertising time slots they may be willing to exchange.
                    Collections.shuffle(shuffledAgents, random);
                    for (Agent a : shuffledAgents) {
                        ArrayList<Integer> unlockedTimeSlots = a.publishUnlockedTimeSlots();
                        if(!unlockedTimeSlots.isEmpty()) {
                            ArrayList<Integer> advert = new ArrayList<>();
                            advert.add(a.agentID);
                            advert.addAll(a.publishUnlockedTimeSlots());
                            advertisingBoard.add(advert);
                        }
                    }

                    // Each Agent has the opportunity to make exchange requests for advertised time slots.
                    Collections.shuffle(shuffledAgents, random);
                    for (Agent a : shuffledAgents) {
                        if (!a.getMadeInteraction()) {
                            ArrayList<Integer> chosenAdvert = a.requestExchange(advertisingBoard);
                            a.setMadeInteraction(true);
                            if (!chosenAdvert.isEmpty()) {
                                // Select an unwanted time slot to offer in the exchange.
                                ArrayList<Integer> unwantedTimeSlots = a.publishUnwantedTimeSlots();
                                int selector = random.nextInt(unwantedTimeSlots.size());
                                int unwantedTimeSlot = unwantedTimeSlots.get(selector);

                                ArrayList<Integer> request = new ArrayList<>();
                                request.add(a.agentID);
                                request.add(chosenAdvert.get(1));
                                request.add(unwantedTimeSlot);

                                // The agent who offered the requested time slot receives the exchange request.
                                for (Agent b : agents) {
                                    if (b.agentID == chosenAdvert.get(0)) {
                                        b.receiveExchangeRequest(request);
                                        b.setMadeInteraction(true);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    // Agents who have received a request consider it.
                    Collections.shuffle(shuffledAgents, random);
                    for (Agent a : shuffledAgents) {
                        if (!a.getExchangeRequestReceived().isEmpty()) {
                            a.considerRequest();
                        }
                    }

                    // Agents confirm and complete approved requests if they are able to do so, and update
                    // their relations with other Agents accordingly.
                    Collections.shuffle(shuffledAgents, random);
                    for (Agent a: shuffledAgents) {
                        if (a.getExchangeRequestApproved()) {
                            ArrayList<Integer> offer = a.getExchangeRequestReceived();
                            if (a.finalCheck(offer.get(1))) {
                                for (Agent b : agents) {
                                    if (b.agentID == offer.get(0)) {
                                        if (b.finalCheck(offer.get(2))) {
                                            b.completeRequestedExchange(offer, a.agentID);
                                            a.completeReceivedExchange(offer);
                                        }
                                        break;
                                    }
                                }
                            }
                            a.setExchangeRequestApproved();
                        }
                        // Clear the agents accepted offers list before the next exchange round.
                        if (!a.getExchangeRequestReceived().isEmpty()) {
                            a.setExchangeRequestReceived();
                        }
                    }

                    for (Agent a : agents) {
                        individualCSVWriter.append(String.valueOf(seed));
                        individualCSVWriter.append(",");
                        individualCSVWriter.append(String.valueOf(j));
                        individualCSVWriter.append(",");
                        individualCSVWriter.append(String.valueOf(k));
                        individualCSVWriter.append(",");
                        individualCSVWriter.append(String.valueOf(a.agentID));
                        individualCSVWriter.append(",");
                        individualCSVWriter.append(String.valueOf(a.getAgentType()));
                        individualCSVWriter.append(",");
                        individualCSVWriter.append(String.valueOf(a.calculateSatisfaction(null)));
                        individualCSVWriter.append("\n");
                    }

                    // The average end of round satisfaction is stored for each Agent type if the current day exists in
                    // the daysOfInterest array. This data can later be averaged over simulation runs and added to
                    // the prePreparedIndividualFile.
                    int currentDay = j;
                    if (IntStream.of(daysOfInterest).anyMatch(val -> val == currentDay)) {
                        for (int uniqueAgentType : uniqueAgentTypes) {
                            double averageSatisfactionForType = averageAgentSatisfaction(uniqueAgentType);
                            ArrayList<Double> endOfRoundAverageSatisfaction = new ArrayList<>();
                            endOfRoundAverageSatisfaction.add((double) j);
                            endOfRoundAverageSatisfaction.add((double) k);
                            endOfRoundAverageSatisfaction.add((double) uniqueAgentType);
                            endOfRoundAverageSatisfaction.add(averageSatisfactionForType);

                            endOfRoundAverageSatisfactions.add(endOfRoundAverageSatisfaction);
                        }
                    }
                }

                // The average end of day satisfaction is stored for each Agent type to later be averaged
                // and added to the prePreparedAverageFile.
                ArrayList<Double> endOfDayAverageSatisfaction = new ArrayList<>();
                endOfDayAverageSatisfaction.add((double) j);
                endOfDayAverageSatisfaction.add(averageSatisfaction);
                endOfDayAverageSatisfaction.add(optimumSatisfaction);

                // The end of day satisfaction is stored for each Agent if the current day exists in
                // the daysOfInterest array. This data can later be averaged over simulation runs and added to
                // the prePreparedBoxPlotFile.
                int currentDay = j;
                if (IntStream.of(daysOfInterest).anyMatch(val -> val == currentDay)) {
                    for (Agent a: agents) {
                        ArrayList<Double> individualSatisfaction = new ArrayList<>();
                        individualSatisfaction.add((double) i);
                        individualSatisfaction.add((double) j);
                        individualSatisfaction.add((double) a.getAgentType());
                        individualSatisfaction.add(a.calculateSatisfaction(null));

                        endOfDayIndividualSatisfactions.add(individualSatisfaction);
                    }
                }

                // Store the end of day average satisfaction for each agent type.
                for (int uniqueAgentType : uniqueAgentTypes) {
                    double typeAverageSatisfaction = averageAgentSatisfaction(uniqueAgentType);
                    averageCSVWriter.append(",");
                    averageCSVWriter.append(String.valueOf(typeAverageSatisfaction));
                    endOfDayAverageSatisfaction.add(typeAverageSatisfaction);
                }
                averageCSVWriter.append("\n");
                endOfDayAverageSatisfactions.add(endOfDayAverageSatisfaction);

                // Only update the user on the simulations status every 10 days to reduce output spam.
                if (j % 10 == 0) {
                    System.out.println("RUN: " + i + "  DAY: " + j);
                }
            }

            // Clear the list of agents before the next simulation begins.
            if (!agents.isEmpty()) {
                agents.clear();
            }
        }

        // The end of day satisfactions for each agent type, as well as for random and optimum allocations,
        // are averaged over simulation runs and appended to the prePreparedAverageFile.
        int types = uniqueAgentTypes.size() + 2;
        for (int i = 1; i <= DAYS; i++) {
            prePreparedAverageCSVWriter.append(String.valueOf(i));
            for (int j = 1; j <= types; j++) {
                ArrayList<Double> allSatisfactions = new ArrayList<>();
                for (ArrayList<Double> endOfDayAverageSatisfaction : endOfDayAverageSatisfactions) {
                    if (endOfDayAverageSatisfaction.get(0) == (double) i) {
                        allSatisfactions.add(endOfDayAverageSatisfaction.get(j));
                    }
                }
                double averageOverSims = allSatisfactions.stream().mapToDouble(val -> val).average().orElse(0.0);
                prePreparedAverageCSVWriter.append(",");
                prePreparedAverageCSVWriter.append(String.valueOf(averageOverSims));
            }
            prePreparedAverageCSVWriter.append("\n");
        }

        // The average end of round satisfaction is stored for each Agent type for all rounds during the days in the
        // daysOfInterest array. These end of round averages are themselves averaged over all simulation runs before
        // being added to the prePreparedIndividualFile.
        for (int day: daysOfInterest) {
            for (int i = 1; i <= EXCHANGES; i++) {
                for (int agentType : uniqueAgentTypes) {
                    ArrayList<Double> allSimsEndOfRoundAverageSatisfaction = new ArrayList<>();
                    for (ArrayList<Double> endOfRoundAverageSatisfaction : endOfRoundAverageSatisfactions) {
                        if ((endOfRoundAverageSatisfaction.get(0) == (double) day) &&
                                (endOfRoundAverageSatisfaction.get(1) == (double) i) &&
                                (endOfRoundAverageSatisfaction.get(2) == (double) agentType)) {
                            allSimsEndOfRoundAverageSatisfaction.add(endOfRoundAverageSatisfaction.get(3));
                        }
                    }
                    double averageOverSims = allSimsEndOfRoundAverageSatisfaction.stream().mapToDouble(val -> val).average().orElse(0.0);

                    prePreparedIndividualCSVWriter.append(String.valueOf(day));
                    prePreparedIndividualCSVWriter.append(",");
                    prePreparedIndividualCSVWriter.append(String.valueOf(i));
                    prePreparedIndividualCSVWriter.append(",");
                    prePreparedIndividualCSVWriter.append(String.valueOf(agentType));
                    prePreparedIndividualCSVWriter.append(",");
                    prePreparedIndividualCSVWriter.append(String.valueOf(averageOverSims));
                    prePreparedIndividualCSVWriter.append("\n");
                }
            }
        }

        // The end of day satisfaction is stored for each Agents for all days in the daysOfInterest array. Each Agent's
        // satisfaction is averaged over all simulation runs, such that the least satisfied Agent one day is averaged
        // with the least satisfied Agent of each other day, the second least with the second least etc.
        for (int day: daysOfInterest) {
            for (int agentType : uniqueAgentTypes) {
                ArrayList<ArrayList<Double>> thisType = new ArrayList<>();
                for (ArrayList<Double> endOfDayIndividualSatisfaction : endOfDayIndividualSatisfactions) {
                    if ((endOfDayIndividualSatisfaction.get(1) == (double) day) &&
                            (endOfDayIndividualSatisfaction.get(2) == (double) agentType)) {
                        ArrayList<Double> thisAgent = new ArrayList<>();
                        thisAgent.add(endOfDayIndividualSatisfaction.get(0));
                        thisAgent.add(endOfDayIndividualSatisfaction.get(3));
                        thisType.add(thisAgent);
                    }
                }
                ArrayList<ArrayList<Double>> allSatisfactionLevels = new ArrayList<>();
                int size = 0;
                for (int i = 1; i <= SIMULATION_RUNS; i++) {
                    ArrayList<Double> satisfactionLevels = new ArrayList<>();
                    for (ArrayList<Double> thisAgent : thisType) {
                        if (thisAgent.get(0) == i) {
                            satisfactionLevels.add(thisAgent.get(1));
                        }
                    }
                    Collections.sort(satisfactionLevels);
                    size = satisfactionLevels.size();
                    allSatisfactionLevels.add(satisfactionLevels);
                }
                for (int i = 0; i < size; i++) {
                    ArrayList<Double> averageForPosition = new ArrayList<>();
                    for (ArrayList<Double> satisfactionLevel : allSatisfactionLevels) {
                        averageForPosition.add(satisfactionLevel.get(i));
                    }
                    double averageOverSims = averageForPosition.stream().mapToDouble(val -> val).average().orElse(0.0);

                    prePreparedBoxPlotCSVWriter.append(String.valueOf(day));
                    prePreparedBoxPlotCSVWriter.append(",");
                    prePreparedBoxPlotCSVWriter.append(String.valueOf(agentType));
                    prePreparedBoxPlotCSVWriter.append(",");
                    prePreparedBoxPlotCSVWriter.append(String.valueOf(averageOverSims));
                    prePreparedBoxPlotCSVWriter.append("\n");
                }
            }
        }

        // Close the csv file writers once the simulation is complete.
        individualCSVWriter.close();
        averageCSVWriter.close();
        prePreparedAverageCSVWriter.close();
        prePreparedIndividualCSVWriter.close();
        prePreparedBoxPlotCSVWriter.close();

        // Collect the required data and pass it to the Python data visualiser to produce graphs of the data.
        String pythonExe = "I:/code/REA_CondaEnvironment/python.exe";
        String pythonPath = "I:/code/ResourceExchangeArena/src/datahandler/DataVisualiser.py";
        String daysToAnalyse = Arrays.toString(daysOfInterest);

        List<String> pythonArgs = new ArrayList<>();

        pythonArgs.add(pythonExe);
        pythonArgs.add(pythonPath);
        pythonArgs.add(RELEASE_VERSION);
        pythonArgs.add(Long.toString(uniqueTag));
        pythonArgs.add(prePreparedAverageFile.getAbsolutePath());
        pythonArgs.add(prePreparedIndividualFile.getAbsolutePath());
        pythonArgs.add(prePreparedBoxPlotFile.getAbsolutePath());
        pythonArgs.add(daysToAnalyse);

        ProcessBuilder builder = new ProcessBuilder(pythonArgs);

        // IO from the Python is shared with the same terminal as the Java code.
        builder.inheritIO();
        builder.redirectErrorStream(true);

        Process process = builder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gives a random initial time slot allocation to an Agent based on the number of time slots it requests and
     * the time slots that are currently available.
     *
     * @param requestedTimeSlots The time slots that the Agent has requested.
     * @return ArrayList<Integer> Returns a list of time slots to allocated to the Agent.
     */
    private static ArrayList<Integer> getRandomInitialAllocation(ArrayList<Integer> requestedTimeSlots) {                // TODO: CAN ASSIGN SAME SLOT TWICE TO AN AGENT, NEED TO USE PSEUDO RANDOM APPROACH WITH HASH MAPS, ASSIGNING MOST COMMONLY AVAILABLE SLOTS FIRST TO SOLVE THIS.
        ArrayList<Integer> timeSlots = new ArrayList<>();

        for (int i = 1; i <= requestedTimeSlots.size(); i++) {
            // Only allocate time slots if there are slots available to allocate.
            if (!availableTimeSlots.isEmpty()) {
                int selector = random.nextInt(availableTimeSlots.size());
                int timeSlot = availableTimeSlots.get(selector);

                timeSlots.add(timeSlot);
                availableTimeSlots.remove(selector);
            }
        }
        return timeSlots;
    }

    /**
     * Takes all Agents individual satisfactions and calculates the average satisfaction of all Agents
     * in the simulation.
     *
     * @return Double Returns the average satisfaction between 0 and 1 of all agents in the simulation.
     */
    private static double averageAgentSatisfaction() {
        ArrayList<Double> agentSatisfactions = new ArrayList<>();
        for (Agent a : agents) {
            agentSatisfactions.add(a.calculateSatisfaction(null));
        }
        return agentSatisfactions.stream().mapToDouble(val -> val).average().orElse(0.0);
    }

    /**
     * Takes all Agents of a given types individual satisfactions and calculates the average satisfaction 
     * of the Agents of that type.
     *
     * @param agentType The type for which to calculate the average satisfaction of all Agents of that type.
     * @return Double Returns the average satisfaction between 0 and 1 of all agents of the given type.
     */
    private static double averageAgentSatisfaction(int agentType) {
        ArrayList<Double> agentSatisfactions = new ArrayList<>();
        for (Agent a : agents) {
            if (a.getAgentType() == agentType) {
                agentSatisfactions.add(a.calculateSatisfaction(null));
            }
        }
        return agentSatisfactions.stream().mapToDouble(val -> val).average().orElse(0.0);
    }

    /**
     * Returns the optimum average satisfaction possible for all agents given the current requests and allocations 
     * in the simulation.
     *
     * @return Double Returns the highest possible average satisfaction between 0 and 1 of all agents in the simulation.
     */
    private static double optimumAgentSatisfaction() {
        ArrayList<Integer> allRequestedSlots = new ArrayList<>();
        ArrayList<Integer> allAllocatedSlots = new ArrayList<>();

        for (Agent a : agents) {
            allRequestedSlots.addAll(a.publishRequestedTimeSlots());
            allAllocatedSlots.addAll(a.publishAllocatedTimeSlots());
        }

        // Stores the number of slots that could potentially be fulfilled with perfect trading.
        int satisfiedSlots = 0;

        // Stores the total number of slots requested by all Agents.
        int totalSlots = allRequestedSlots.size();

        for (Integer slot : allRequestedSlots) {
            if (allAllocatedSlots.contains(slot)) {
                // For each request, if it has been allocated to any agent, increase the number of satisfied slots.
                satisfiedSlots++;

                // Remove the slot from the list of all allocated slots so no slots can be allocated twice.
                allAllocatedSlots.remove(slot);
            }
        }
        return ((double)satisfiedSlots) / totalSlots;
    }

    /**
     * Takes an agentType and converts it from it's integer format to a descriptive string name to organise the data
     * output by the simulation.
     *
     * @return String Returns the given agentType as a descriptive string.
     */
    private static String getHumanReadableAgentType(int agentType) {
        String name;
        // Names match types specified by constant integers for the ExchangeArena.
        switch(agentType) {
            case SOCIAL:
                name = "Social";
                break;
            case SELFISH:
                name = "Selfish";
                break;
            default:
                // If a human readable name doesnt exist, return the integer agentType as a string.
                name = String.valueOf(agentType);
        }
        return name;
    }
}
