package resourceexchangearena;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Overarching parent class containing parameters that alter the scope of the simulation.
 */
public class ResourceExchangeArena {
    // REQUIRED SYSTEM PATHS, SET THESE BEFORE RUNNING THE SIMULATION.
    // Conda env. location.
    static final String pythonExe = "/home/nathan/anaconda3/envs/ResourceExchangeArena/bin/python";
    // Data visualiser location, most users will only need to change the username here.
    static final String pythonPath = "/home/nathan/code/ResourceExchangeArena/src/datahandler/DataVisualiser.py";

    // Constants representing the available agent types for the simulation.
    static final int SELFISH = 1;
    static final int SOCIAL = 2;

    // Create a single Random object for generating random numerical data for the simulation.
    static Random random = new Random();
    static long seed;

    /**
     * This is the main method which runs the entire ResourceExchangeArena simulation.
     *
     * @param args Unused.
     * @exception IOException On input error.
     * @see IOException
     */
    public static void main(String[] args) throws IOException {

        // Name of the folder that will contain all of the simulations currently being ran.
        final String FOLDER_NAME = "newTest";

        //#############################################################################################################
        // ALTER THESE PARAMETERS IN ORDER TO SIMULATE VARIOUS SCENARIOS.
        // In order to schedule multiple parameter combinations when performing a parameter sweep, add more items to
        // the following arrays. All possible combinations will be simulated.

        // Number of exchange rounds per day.
        final int[] EXCHANGES_ARRAY = {50};

        // Number of agents that will evolve their strategy per day.
        final int[] NUMBER_OF_AGENTS_TO_EVOLVE_ARRAY = {10,0};

        // Ratio of starting agent types, i.e. {SELFISH, SELFISH, SOCIAL} would cause the simulation to start with two
        // selfish agents for each social agent.
        final int[][] AGENT_TYPES_ARRAY = {{SELFISH, SOCIAL}};
        //#############################################################################################################

        // Alter the length of time to be simulated.
        final int DAYS = 365;

        // Increase the number of simulation runs for more consistent results.
        final int SIMULATION_RUNS = 5;

        // Alter the number of Agents and their requirements. Note that the simulation has not been designed in order
        // to support this and so some combinations may cause errors.
        final int POPULATION_SIZE = 96;
        final int MAXIMUM_PEAK_CONSUMPTION = 16;
        final int UNIQUE_TIME_SLOTS = 24;
        final int SLOTS_PER_AGENT = 4;

        // Days that will have the Agents average satisfaction over the course of the day,
        // and satisfaction distribution at the end of the day visualised.
        final int[] DAYS_OF_INTEREST = {1, 25, 50};

        // Configures the simulation to output the state of each agent after each exchange and at the end of each day.
        // DUE TO THE POTENTIAL VOLUME OF DATA THIS CAN GENERATE, IT IS HIGHLY RECOMMENDED THAT THIS REMAINS SET TO
        // 'false' OUTSIDE OF STATISTICAL TESTING OR WHERE OTHERWISE REQUIRED.
        final boolean ADDITIONAL_DATA = false;

        // The seed can be set to replicate previous simulations.
        seed = System.currentTimeMillis();

        // Set the simulations initial random seed.
        random.setSeed(seed);

        /*
         * The arena is the environment in which all simulations take place.
         *
         * @param folderName String representing the output destination folder, used to organise output data.
         * @param daysOfInterest Integer array containing the days be shown in graphs produced after the simulation.
         * @param additionalData Boolean value that configures the simulation to output the state of each agent after
         *                       each exchange and at the end of each day.
         * @param simulationRuns Integer value representing the number of simulations to be ran and averaged.
         * @param days Integer value representing the number of days to be simulated.
         * @param exchanges Integer value representing the number of times all agents perform pairwise exchanges per
         *                  day.
         * @param populationSize Integer value representing the size of the initial agent population.
         * @param maximumPeakConsumption Integer value representing how many agents can be allocated to each time slot.
         * @param uniqueTimeSlots Integer value representing the number of unique time slots available in the
         *                        simulation.
         * @param slotsPerAgent Integer value representing the number of time slots each agent requires.
         * @param numberOfAgentsToEvolve Integer value representing the number of Agents who's strategy will change at
         *                               the end of each day.
         * @param agentTypes Integer array containing the agent types that the simulation will begin with. The same type
         *                   can exist multiple times in the array where more agents of one type are required.
         * @exception IOException On input error.
         * @see IOException
         */

        // Create a directory to store the data output by all simulations being run.
        String dataOutputFolder = "results/" + FOLDER_NAME;
        Path dataOutputPath = Paths.get(dataOutputFolder);
        Files.createDirectories(dataOutputPath);

        // Stores the key data about the finished simulation.
        File allSimulationsData = new File(
                "results/" + FOLDER_NAME,"allSimulationsData.txt");

        FileWriter allSimulationsDataWriter = new FileWriter(allSimulationsData);

        allSimulationsDataWriter.append("Simulation Information (all runs): \n\n");
        allSimulationsDataWriter.append("Days of interest: ").append(Arrays.toString(DAYS_OF_INTEREST)).append("\n");
        allSimulationsDataWriter.append("Additional data: ").append(String.valueOf(ADDITIONAL_DATA)).append("\n");
        allSimulationsDataWriter.append("Simulation runs: ").append(String.valueOf(SIMULATION_RUNS)).append("\n");
        allSimulationsDataWriter.append("Days: ").append(String.valueOf(DAYS)).append("\n");
        allSimulationsDataWriter.append("Population size: ").append(String.valueOf(POPULATION_SIZE)).append("\n");
        allSimulationsDataWriter.append("Maximum peak consumption: ").append(String.valueOf(MAXIMUM_PEAK_CONSUMPTION))
                .append("\n");
        allSimulationsDataWriter.append("Unique time slots: ").append(String.valueOf(UNIQUE_TIME_SLOTS)).append("\n");
        allSimulationsDataWriter.append("Slots per agent: ").append(String.valueOf(SLOTS_PER_AGENT)).append("\n\n\n");
        allSimulationsDataWriter.append("Simulation Information (specific run details): \n\n");

        for (int EXCHANGES : EXCHANGES_ARRAY) {
            for (int NUMBER_OF_AGENTS_TO_EVOLVE : NUMBER_OF_AGENTS_TO_EVOLVE_ARRAY) {
                for (int[] AGENT_TYPES : AGENT_TYPES_ARRAY) {
                    new ArenaEnvironment(
                            FOLDER_NAME,
                            DAYS_OF_INTEREST,
                            ADDITIONAL_DATA,
                            SIMULATION_RUNS,
                            DAYS,
                            EXCHANGES,
                            POPULATION_SIZE,
                            MAXIMUM_PEAK_CONSUMPTION,
                            UNIQUE_TIME_SLOTS,
                            SLOTS_PER_AGENT,
                            NUMBER_OF_AGENTS_TO_EVOLVE,
                            AGENT_TYPES
                    );

                    allSimulationsDataWriter.append("Seed: ").append(String.valueOf(seed)).append("\n");
                    allSimulationsDataWriter.append("Exchanges: ").append(String.valueOf(EXCHANGES)).append("\n");
                    allSimulationsDataWriter.append("Number of agents to evolve: ").append(String.valueOf(NUMBER_OF_AGENTS_TO_EVOLVE))
                            .append("\n");
                    allSimulationsDataWriter.append("Starting ratio of agent types: ");
                    int typesListed = 0;
                    for (int type : AGENT_TYPES) {
                        if(typesListed != 0){
                            allSimulationsDataWriter.append(" : ");
                        }
                        typesListed++;
                        allSimulationsDataWriter.append(Inflect.getHumanReadableAgentType(type));
                    }
                    allSimulationsDataWriter.append("\n\n");
                }
            }
        }
        allSimulationsDataWriter.close();
    }
}
