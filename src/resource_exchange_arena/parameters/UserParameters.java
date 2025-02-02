package resource_exchange_arena.parameters;

public abstract class UserParameters extends FixedParameters {
    // Conda env. location.
    public static final String PYTHON_EXE =
            "/home/brooks/anaconda3/envs/rea/bin/python3";
    // Example: "1599767866160L"
    // Alternatively if no specific seed is required...
    // Example: "System.currentTimeMillis()"
    // Location of data visualiser python scripts on your machine.
    public static final String PYTHON_PATH =
            "/home/brooks/code/ResourceExchangeArena/src/data_analysis/";
    // Alter the population size.
    public static final int POPULATION_SIZE = 96;
    // Alter the number of timeslots that each agent requests each day.
    public static final int SLOTS_PER_AGENT = 4;
    // Example: "/home/nathan/IdeaProjects/ResourceExchangeArena/src/data_analysis/"
    // Alter the length of time to be simulated once the simulation has reached a steady population state.
    public static final int DAYS = 100;
    // Increase the number of simulation runs for more consistent results.
    public static final int SIMULATION_RUNS = 100;
    // Arrays of demand used by the agents, when multiple curves are used the agents are split equally between the curves.
    // The arrays should have 1 value for each 10 minute segment of the day.
    public static final double[][] DEMAND_CURVES = {{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}};
    // Example: "500"
    // The proportion of energy available for each hour of the of day.
    // The arrays should have 1 value for each 30 minute segment of the day.
    public static final int[] AVAILABILITY_CURVE = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    // Example: "50"
    // Percentage of agents that will evolve their strategy per day.
    public static final int[] PERCENTAGE_OF_AGENTS_TO_EVOLVE_ARRAY = {100};
    // Ratio of starting agent types, i.e. {SELFISH, SELFISH, SOCIAL} would cause the simulation to start with two
    // selfish agents for each social agent.
    // Note that both types of agents need to exist, for testing with a single agent type set 'SINGLE_AGENT_TYPE'
    // to 'true' and set the 'SELECTED_SINGLE_AGENT_TYPE' as required.
    public static final int[][] AGENT_TYPES_ARRAY = {{SELFISH, SOCIAL}};
    /**
     * Contains all system parameters that can be changed by the user in order to experiment with various scenarios.
     */

    // The seed can be set to replicate previous simulations.
    public static long seed = System.currentTimeMillis();
    // Name of the folder that will contain the set of simulations currently being ran.
    public static final String FOLDER_NAME = "/home/brooks/code/ResourceExchangeArena/results/Set_" + seed;

    //Demand curves to experiment with
    //Base
    //1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0

    //All energy uses
    //375.4,374.6,350.2,334.5,305.0,285.5,277.2,275.1,273.1,268.1,264.4,262.4,255.3,251.0,244.3,242.1,237.1,236.4,230.3,230.7,223.6,222.5,232.9,241.9,251.2,250.6,239.9,231.6,228.2,220.0,224.8,231.1,231.8,232.1,248.1,253.3,262.5,276.8,283.1,289.0,320.0,361.1,394.1,415.2,432.8,449.4,459.5,480.8,496.4,498.9,506.7,497.4,491.4,497.0,494.0,494.8,495.1,498.0,490.6,488.5,482.0,485.3,480.0,476.2,464.6,468.2,468.4,473.6,482.2,471.9,471.2,476.5,490.3,498.8,490.7,496.9,496.1,490.4,474.6,475.4,472.7,464.2,462.4,458.0,449.8,447.0,440.9,445.5,451.3,455.5,463.9,462.1,470.7,472.6,483.3,489.9,502.5,528.9,543.1,568.6,593.5,610.7,643.0,655.4,664.9,683.1,696.4,711.7,716.5,725.5,712.5,707.1,714.1,709.9,713.5,720.1,705.0,709.0,696.0,701.3,691.9,689.3,681.0,675.8,664.7,656.6,652.1,646.8,631.8,622.3,606.0,594.2,579.6,571.0,553.3,538.4,538.1,518.4,500.0,473.9,445.3,429.2,423.7,406.1

    //Washing/Drying/Dishwasher
    //31.6,32.9,27.7,24.6,24.0,19.5,16.4,14.6,15.2,11.5,9.3,8.6,7.4,6.4,7.4,10.4,9.9,7.4,6.7,10.1,6.2,7.8,7.0,7.6,5.5,6.7,5.2,4.7,4.9,4.2,5.3,4.3,3.8,4.7,5.5,6.0,7.0,5.4,6.7,13.2,17.9,21.2,23.8,28.7,28.8,36.4,41.3,49.0,55.0,58.1,59.5,66.2,72.7,78.3,79.6,78.7,81.4,82.2,84.0,86.0,82.9,87.4,83.1,88.1,85.1,83.1,82.1,82.9,81.2,81.9,81.2,79.2,77.8,79.1,77.6,76.7,73.8,71.9,72.6,73.0,70.4,69.9,71.7,67.6,65.7,68.2,63.8,68.0,67.1,68.5,68.3,67.7,70.1,67.7,68.2,65.2,64.1,65.8,68.4,64.4,61.0,57.4,64.2,66.1,57.3,55.9,55.7,58.9,61.6,64.4,63.3,61.3,58.4,62.8,70.3,74.3,75.2,76.7,71.9,73.5,73.9,69.3,67.9,68.2,69.2,65.5,61.1,63.3,60.3,53.6,51.3,44.4,45.1,42.3,44.1,44.9,46.3,42.1,41.8,39.7,36.3,37.1,35.7,32.1

    //WDD Single Pensioners
    //5.2,2.6,0.7,1.5,3.4,2.6,0.4,0.2,0.3,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.3,0.2,0.2,0.1,0.2,0.3,0.3,0.3,0.4,0.6,1.0,1.0,3.4,7.9,4.0,5.8,14.3,21.6,20.1,24.9,26.1,18.0,18.8,13.2,23.4,20.7,13.3,19.0,15.5,21.7,39.4,42.4,32.4,22.7,27.9,28.5,42.4,36.6,40.6,45.2,49.8,52.3,54.7,37.1,38.8,23.6,29.1,30.8,27.5,18.1,18.8,21.2,18.6,29.9,21.6,17.1,18.0,17.1,16.6,21.3,17.3,9.7,11.1,8.7,10.8,19.0,23.1,20.1,19.8,15.2,12.5,14.9,24.2,24.7,17.7,13.7,8.8,8.0,8.2,4.7,6.4,7.8,7.7,8.4,7.8,8.2,12.6,12.7,10.2,12.5,26.7,21.0,13.7,7.1,8.1,5.3,19.3,25.8,11.0,7.4,6.2,11.7,9.5,7.6,2.6,4.4,10.0,12.2,7.5,4.8,2.3,6.9,10.8,13.1,7.1,2.8,8.3,7.4,6.7,10.6,4.8

    //WDD Single Non-Pen
    //8.1,10.2,7.8,7.0,13.3,14.9,12.4,11.0,10.7,8.8,6.7,7.8,10.0,8.2,20.3,36.0,29.9,26.8,21.6,46.0,23.7,19.5,13.7,7.5,6.1,5.6,7.3,8.0,5.3,4.0,3.4,4.5,2.8,6.6,8.0,3.2,8.1,7.2,3.7,5.2,7.9,18.0,16.8,22.4,19.3,18.3,17.2,13.8,21.3,22.9,19.0,32.4,31.6,25.7,23.0,22.6,29.9,30.4,27.3,33.7,27.2,29.0,28.1,31.7,35.5,27.9,21.1,22.3,24.0,21.4,16.7,14.4,21.1,20.3,21.6,22.4,16.7,20.3,12.1,8.6,19.7,24.3,20.3,17.4,13.2,21.3,21.6,16.7,14.4,18.7,20.9,16.6,10.0,7.2,7.3,9.1,10.8,14.3,19.2,18.4,25.8,16.3,14.0,18.2,12.7,17.2,20.6,17.8,24.2,30.3,32.2,24.4,15.6,15.9,17.5,19.5,29.5,24.6,16.3,26.0,20.8,19.2,21.1,27.6,21.5,27.0,24.9,37.4,25.7,29.0,21.1,14.1,21.0,17.6,21.1,15.0,9.7,6.7,9.3,6.8,11.0,11.2,11.2,6.9
    // Specify whether only a single agent type should exist in the simulation, used for establishing baseline results.
    public static boolean SINGLE_AGENT_TYPE = false;
    // Specify the single agent type to be simulated when 'SINGLE_AGENT_TYPE = true', e.g. 'SELFISH' or 'SOCIAL'.
    public static int SELECTED_SINGLE_AGENT_TYPE = SOCIAL;
    // Example: "{0,50,100}" {0, 10, 25, 50,100}
    // Specify whether social capital  should be used by the social agents.
    public static boolean USE_SOCIAL_CAPITAL = true;
    // Example: "{{SELFISH, SOCIAL}}"
    // ################################################################################################################
    // Sets the level of comparisons that will be made:
    // Note that this overrides some of the previously set parameters and can result in much longer compute times.
    // 0 = Only the parameter combinations set will be made.
    // 1 = The above combinations will be ran both with and without social capital enabled so that the results can
    //     be compared.
    // 2 = The above combinations will be ran both with and without social capital enabled and with only selfish agents
    //     and with only social agents so that a baseline comparison can be made between the agents types.
    public static int COMPARISON_LEVEL = 0;
}
