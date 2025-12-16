package com.mycompany.finalproject2;

import java.io.*;
import java.util.*;

public class PartBDriver {

    /* =======================
       PUBLIC ENTRY (no main)
       ======================= */
    public static void runGame(
            String filename,
            double b,
            int h,
            double m,
            double T,
            boolean isK
    ) {

        ArrayList<Agent> agents = readNetwork(filename);
        if (agents == null) return;

        removeIsolatedAgents(agents);
        initializeStrategies(agents, h);

        int rounds = runSimulation(agents, b, m, T, isK);

        printResults(agents, rounds);
    }

    /* =======================
       NETWORK SETUP
       ======================= */
    private static ArrayList<Agent> readNetwork(String filename) {

        ArrayList<Agent> agents = new ArrayList<>();
        HashMap<Integer, Agent> agentMap = new HashMap<>();

        try {
            Scanner fileScanner = new Scanner(new File(filename));

            // Skip header
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.startsWith("Edges")) break;
            }

            // Read edges
            while (fileScanner.hasNextInt()) {
                int id1 = fileScanner.nextInt();
                int id2 = fileScanner.nextInt();

                agentMap.putIfAbsent(id1, new Agent(id1, 0.0));

                if (id2 != -1) {
                    agentMap.putIfAbsent(id2, new Agent(id2, 0.0));

                    Agent a1 = agentMap.get(id1);
                    Agent a2 = agentMap.get(id2);
                    a1.addNeighbor(a2);
                    a2.addNeighbor(a1);
                }
            }

            fileScanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return null;
        }

        agents.addAll(agentMap.values());
        agents.sort(Comparator.comparingInt(Agent::getNum));
        return agents;
    }

    private static void removeIsolatedAgents(ArrayList<Agent> agents) {
        for (Agent a : agents) {
            if (a.k == 0) {
                a.isAlive = false;
            }
        }
    }

    private static void initializeStrategies(ArrayList<Agent> agents, int h) {
        boolean foundH = false;

        for (Agent a : agents) {
            if (a.getNum() == h && a.k > 0) {
                a.isCooperator = false;
                foundH = true;
                break;
            }
        }

        if (!foundH) {
            System.out.println("Warning: initial defector h not found or isolated.");
        }
    }

    /* =======================
       SIMULATION LOOP
       ======================= */
    private static int runSimulation(
            ArrayList<Agent> agents,
            double b,
            double m,
            double T,
            boolean isK
    ) {

        int rounds = 0;
        boolean changed = true;

        while (changed && rounds < 500) {
            changed = false;
            rounds++;

            computePayoffs(agents, b);
            changed = eliminateAgents(agents, T, isK) || changed;

            // imitation step intentionally omitted / TODO
        }

        return rounds;
    }

    /* =======================
       PAYOFF & ELIMINATION
       ======================= */
    private static void computePayoffs(ArrayList<Agent> agents, double b) {
    for (Agent a : agents) {
        if (!a.isAlive) continue;

        a.R = 0.0;   // ← reset Big R each round

        for (Agent n : a.neighbors) {
            if (!n.isAlive) continue;

            if (a.isCooperator && n.isCooperator) {
                a.R += 1.0;
            } else if (!a.isCooperator && n.isCooperator) {
                a.R += b;
            }
        }
    }
}

    

    private static boolean eliminateAgents(
            ArrayList<Agent> agents,
            double T,
            boolean isK
    ) {

        boolean changed = false;
        ArrayList<Agent> toEliminate = new ArrayList<>();

        for (Agent a : agents) {
            if (!a.isAlive || a.k <= 0) continue;

            double threshold = isK ? (T / a.k) : T;

            if (a.R < threshold) {
                toEliminate.add(a);
                changed = true;
            }
        }

        for (Agent a : toEliminate) {
            a.isAlive = false;
        }

        return changed;
    }
    
    private static void agenticImitation(){
        
    }
    
    // C. TODO: Imitation step based on m
            // For each alive agent:
            //   - look at neighbors with higher r
            //   - with probability m, copy that neighbor's strategy (isCooperator)
            // This should be done using a temporary structure so all updates
            // happen simultaneously after checking everyone.
    
            // Variable definition: 
            // Notes: m - possibility
            // p - Probability of an edge between any two agents in a random network.
            // m - Probability that an agent imitates a neighbor’s strategy after a round.
            // k - initial number of neighbors.
            /* rᵢ - The payoff agent i gets from a single interaction with one neighbor. 
            Determined by: 
            -agent i’s strategy 
            -neighbor’s strategy 
            -payoff parameter b 
            -agent i’s initial degree kᵢ*/
    
            // NOTE: I left this unimplemented so we can finalize the exact
            // imitation rule and ordering (imitate before/after elimination)
            // to match Eliott's spec.
            // TODO: per-round logging of percent eliminated (if required by assignment)

    /* =======================
       OUTPUT
       ======================= */
    private static void printResults(ArrayList<Agent> agents, int rounds) {

        int alive = 0, coop = 0, defect = 0;

        for (Agent a : agents) {
            if (a.isAlive) {
                alive++;
                if (a.isCooperator) coop++;
                else defect++;
            }
        }

        double eliminatedPercent =
                agents.isEmpty() ? 0 :
                100.0 * (agents.size() - alive) / agents.size();

        System.out.println("Simulation ended after " + rounds + " rounds.");
        System.out.printf("Eliminated: %.2f%%%n", eliminatedPercent);
        System.out.println("Alive: " + alive);
        System.out.println("Cooperators: " + coop);
        System.out.println("Defectors: " + defect);
    }
}
