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
            changed = agenticImitation(agents, m) || changed;
            changed = eliminateAgents(agents, T, isK) || changed;
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

    private static boolean agenticImitation(ArrayList<Agent> agents, double m) {
        boolean changed = false;
        Random rand = new Random();
        HashMap<Agent, Boolean> strategyUpdates = new HashMap<>();

        for (Agent a : agents) {
            if (!a.isAlive) continue;

            List<Agent> betterNeighbors = new ArrayList<>();
            for (Agent neighbor : a.neighbors) {
                if (!neighbor.isAlive) continue;
                if (neighbor.R > a.R) {
                    betterNeighbors.add(neighbor);
                }
            }

            if (!betterNeighbors.isEmpty() && rand.nextDouble() < m) {
                Agent chosenNeighbor = betterNeighbors.get(rand.nextInt(betterNeighbors.size()));
                strategyUpdates.put(a, chosenNeighbor.isCooperator);
            }
        }

        for (Map.Entry<Agent, Boolean> entry : strategyUpdates.entrySet()) {
            Agent agent = entry.getKey();
            boolean newStrategy = entry.getValue();
            if (agent.isCooperator != newStrategy) {
                agent.isCooperator = newStrategy;
                changed = true;
            }
        }

        return changed;
    }

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
