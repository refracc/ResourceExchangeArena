package resource_exchange_arena.parameters;

public abstract class UserParameters extends FixedParameters {
    /**
     * Contains all system parameters that can be changed by the user in order to experiment with various scenarios.
     */

    // The seed can be set to replicate previous simulations.
    public static long seed = System.currentTimeMillis();
    // Example: "1599767866160L"
    // Alternatively if no specific seed is required...
    // Example: "System.currentTimeMillis()"

    // Name of the folder that will contain the set of simulations currently being ran.
    public static final String FOLDER_NAME = "Simulation_Set:" + seed;

    // Conda env. location.
    public static final String PYTHON_EXE =
            "/home/nathan/anaconda3/envs/ResourceExchangeArena/bin/python";

    // Data visualiser location, most users will only need to change the username here.
    public static final String PYTHON_PATH =
            "/home/nathan/IdeaProjects/ResourceExchangeArena/src/data_analysis/DataVisualiser.py";

    // Summary data visualiser location, most users will only need to change the username here.
    public static final String SUMMARY_PYTHON_PATH =
            "/home/nathan/IdeaProjects/ResourceExchangeArena/src/data_analysis/DataVisualiserSummaryGraphs.py";

    // Configures the simulation to output the state of each agent after each exchange and at the end of each day.
    // DUE TO THE POTENTIAL VOLUME OF DATA THIS CAN GENERATE, IT IS HIGHLY RECOMMENDED THAT THIS REMAINS SET TO
    // 'false' OUTSIDE OF STATISTICAL TESTING OR WHERE OTHERWISE REQUIRED.
    public static final boolean ADDITIONAL_DATA = false;

    // Alter the length of time to be simulated.
    public static final int DAYS = 500;
    // Example: "500"

    // Increase the number of simulation runs for more consistent results.
    public static final int SIMULATION_RUNS = 50;
    // Example: "50"

    // Days that will have the Agents average satisfaction over the course of the day, and satisfaction distribution at
    // the end of the day visualised.
    public static final int[] DAYS_OF_INTEREST = {1, 25, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500};
    // Example: "{1, 25, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500}"

    // Specify whether only a single agent type should exist in the simulation, used for establishing baseline results.
    public static final boolean SINGLE_AGENT_TYPE = false;

    // Specify the single agent type to be simulated when 'SINGLE_AGENT_TYPE = true', e.g. 'SELFISH' or 'SOCIAL'.
    public static final int SELECTED_SINGLE_AGENT_TYPE = SELFISH;

    // In order to schedule multiple parameter combinations when performing a parameter sweep, add more items to
    // the following arrays. All possible combinations will be simulated.
    // ################################################################################################################
    // Number of exchange rounds per day.
    public static final int[] EXCHANGES_ARRAY = {1,25,50,75,100,125,150,175,200};
    // Example: "1{1,25,50,75,100,125,150,175,200}"

    // Number of agents that will evolve their strategy per day.
    public static final int[] NUMBER_OF_AGENTS_TO_EVOLVE_ARRAY = {0,10,19,29,38,48,58,67,77,86,96};
    // Example: "{0,10,19,29,38,48,58,67,77,86,96}"

    // Ratio of starting agent types, i.e. {SELFISH, SELFISH, SOCIAL} would cause the simulation to start with two
    // selfish agents for each social agent.
    // Note that both types of agents need to exist, for testing with a single agent type set 'SINGLE_AGENT_TYPE'
    // to 'true' and set the 'SELECTED_SINGLE_AGENT_TYPE' as required.
    public static final int[][] AGENT_TYPES_ARRAY = {{SELFISH, SOCIAL}};
    // Example: "{{SELFISH, SOCIAL}}"
    // ################################################################################################################
}
