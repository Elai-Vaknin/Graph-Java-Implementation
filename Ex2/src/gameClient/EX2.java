package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EX2 implements Runnable {
    private static MyFrame _win;
    private static Arena _ar;
    private static int[] targetedPokes;
    private static long miliseconds;
    private static int scenario_num;
    private static int id;

    public static void main(String[] args) {
        id = Integer.parseInt(args[0]);
        scenario_num = Integer.parseInt(args[1]);

        game_service game = Game_Server_Ex2.getServer(scenario_num);

        game.login(id);

        String g = game.getGraph();
        String pks = game.getPokemons();

        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        init(game);

        game.startGame();

        Thread client = new Thread(new EX2());
        client.start();

        while (game.isRunning()) {
            moveAgents(game, gg);
        }

        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    @Override
    public void run() {
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) ");
        int ind = 0;
        long dt = 100;

        while(true) {
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     *
     * @param game
     * @param gg
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        List<CL_Agent> agents = Arena.getAgents(game.move(), gg);

        String fs = game.getPokemons();

        List<CL_Pokemon> pokemons = Arena.json2Pokemons(fs);

        _ar.setPokemons(pokemons);
        _ar.setAgents(agents);

        for (int i = 0; i < agents.size(); i++) {
            CL_Agent ag = agents.get(i);

            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();

            double v = ag.getValue();

            if (dest == -1) {
                dest = nextNode(gg, src, pokemons, ag, agents);
                ag.setCurrNode(dest);
                game.chooseNextEdge(ag.getID(), dest);
                edge_data edge = targetedPokes[ag.getID()] >= 0 ? pokemons.get(targetedPokes[ag.getID()]).get_edge() : null;
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest + " Fruit: " + edge);
            }
        }
    }

    /**
     * choosing the next node for the agent
     *
     * @param graph - game graph
     * @param src - agent source node
     * @param pokemons - list of pokemons
     * @param ag - current agent
     * @param agents - list of agents
     * @return int
     */

    private static int nextNode(directed_weighted_graph graph, int src, List<CL_Pokemon> pokemons, CL_Agent ag, List<CL_Agent> agents) {
        if(pokemons.isEmpty())
            return src;

        for(CL_Pokemon pokemon : pokemons) {
            Arena.updateEdge(pokemon, graph);
        }

        CL_Pokemon currPokemon = getNewTarget(graph, ag, pokemons, agents);

        /********** Swap not working **************/
        for(CL_Agent agent : agents) {
            if(ag == agent || targetedPokes[agent.getID()] < 0)
                continue;

            CL_Pokemon agentPokemon = pokemons.get(targetedPokes[agent.getID()]);

            long time = System.currentTimeMillis();

            if(time - miliseconds > 250 && getDistance(graph, ag, agentPokemon) < getDistance(graph, agent, agentPokemon) &&
                    getDistance(graph, agent, currPokemon) < getDistance(graph, ag, currPokemon)) {
                int temp = targetedPokes[agent.getID()];
                targetedPokes[agent.getID()] = targetedPokes[ag.getID()];
                targetedPokes[ag.getID()] = temp;
                miliseconds = System.currentTimeMillis();
            }
        }

        currPokemon = pokemons.get(targetedPokes[ag.getID()]);

        List<node_data> path = findPath(graph, src, currPokemon);

        return findNextMove(src, ag, path);
    }

    /**
     * calculating distance between agent to pokemon
     *
     * @param graph - game graph
     * @param pokemon - chosen pokemon
     * @param agent - chosen agent
     * @return double
     */

    private static double getDistance(directed_weighted_graph graph, CL_Agent agent, CL_Pokemon pokemon) {
        edge_data edge = pokemon.get_edge();

        int dest = pokemon.getType() < 0 ? edge.getSrc() : edge.getDest();
        int src = agent.getSrcNode();

        dw_graph_algorithms algo = new DWGraph_Algo(graph);

        return algo.shortestPathDist(src, dest) + edge.getWeight();
    }

    /**
     * getting a new target for a specific agent
     *
     * @param graph - game graph
     * @param pokemons - list of pokemons
     * @param ag - current agent
     * @param agents - list of agents
     * @return CL_Pokemon
     */

    private static CL_Pokemon getNewTarget(directed_weighted_graph graph, CL_Agent ag, List<CL_Pokemon> pokemons, List<CL_Agent> agents) {
        double distance;
        double minDistance = Double.MAX_VALUE;

        dw_graph_algorithms algo = new DWGraph_Algo(graph);

        CL_Pokemon chosen = null;
        int chosenID = 0;

        for(int i = 0; i < pokemons.size(); i++) {
            CL_Pokemon pokemon = pokemons.get(i);

            boolean taken = false;

            for(CL_Agent agent : agents) {
                if(ag == agent)
                    continue;

                if(targetedPokes[agent.getID()] == i) {
                    taken = true;
                }
            }

            if(taken)
                continue;

            distance = getDistance(graph, ag, pokemon);

            if(distance < minDistance) {
                minDistance = distance;
                chosen = pokemon;
                chosenID = i;
            }
        }

        targetedPokes[ag.getID()] = chosenID;

        return chosen;
    }

    /**
     * returning a path from source node to a specific pokemon
     *
     * @param graph - game graph
     * @param src - agent source node
     * @param pokemon - chosen pokemon
     * @return List<node_data>
     */

    private static List<node_data> findPath(directed_weighted_graph graph, int src, CL_Pokemon pokemon) {
        dw_graph_algorithms algo = new DWGraph_Algo(graph);

        edge_data pokeEdge = pokemon.get_edge();

        int pokeSrc = pokeEdge.getSrc();
        int pokeDest = pokeEdge.getDest();
        int pokeType = pokemon.getType();

        int dest = pokeType < 0 ? pokeDest : pokeSrc;

        List<node_data> path = algo.shortestPath(src, dest);

        if(path == null) {
            path = new ArrayList<>();
            path.add(pokeType < 0 ? graph.getNode(pokeDest) : graph.getNode(pokeSrc));
        }

        path.add(pokeType < 0 ? graph.getNode(pokeSrc) : graph.getNode(pokeDest));

        return path;
    }

    /**
     * finding the next move for the agent out of the path
     *
     * @param src - agent source node
     * @param ag - current agent
     * @param path - list of nodes from source to pokemon
     * @return int
     */

    public static int findNextMove(int src, CL_Agent ag, List<node_data> path) {
        int result = src;

        if(path == null)
            return result;

        for(int i = 0; i < path.size(); i++) {
            node_data current = path.get(i);

            if(current.getKey() == src) {
                if(i < path.size() - 1) {
                    result = path.get(i+1).getKey();
                }
            }
        }

        return result;
    }

    /**
     * Initializing the game, spawning agents, UI setup
     *
     * @param game - game service object
     */
    private static void init(game_service game) {
        String s_pokemon = game.getPokemons();

        directed_weighted_graph graph = game.getJava_Graph_Not_to_be_used();

        _ar = new Arena();
        _ar.setGraph(graph);
        _ar.setPokemons(Arena.json2Pokemons(s_pokemon));

        _win = new MyFrame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);
        _win.show();

        String info = game.toString();

        JSONObject line;

        try {

            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");

            int agentsNum = ttt.getInt("agents");

            targetedPokes = new int[agentsNum];

            int src_node = 0;

            ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());

            for (int a = 0; a < pokemons.size(); a++)
                Arena.updateEdge(pokemons.get(a), graph);

            spawnAgents(game, agentsNum, pokemons);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * choosing the next node for the agent
     *
     * @param game - game service object
     * @param agentsNum - number of agents
     * @param pokemons - list of pokemons
     */

    private static void spawnAgents(game_service game, int agentsNum, List<CL_Pokemon> pokemons) {
        List<CL_Pokemon> copy = new ArrayList<>();

        for(CL_Pokemon pokemon : pokemons) {
            copy.add(pokemon);
        }

        double maxValue;

        for (int a = 0; a < agentsNum; a++) {
            maxValue = 0;

            int ind = a % pokemons.size();

            CL_Pokemon c = null;

            for(CL_Pokemon pokemon : copy) {
                double value = calculateRadius(game, pokemon, copy, 10);

                if(value > maxValue) {
                    maxValue = value;
                    c = pokemon;
                }
            }

            int agent = c.get_edge().getDest();

            if (c.getType() < 0) {
                agent = c.get_edge().getSrc();
            }

            game.addAgent(agent);
            copy.remove(c);
        }
    }

    /**
     * calculating the values of pokemons in a specific radius
     *
     * @param game - game service object
     * @param pokemon - a specific pokemon to calculate his area of values
     * @param pokemons - list of pokemons
     * @param distance - radius
     * @return double
     */

    private static double calculateRadius(game_service game, CL_Pokemon pokemon, List<CL_Pokemon> pokemons, double distance) {
        double sum = pokemon.getValue();

        dw_graph_algorithms algo = new DWGraph_Algo(game.getJava_Graph_Not_to_be_used());

        for(CL_Pokemon poke : pokemons) {
            if(poke == pokemon)
                continue;

            edge_data edgePoke = poke.get_edge();
            edge_data edgePokemon = pokemon.get_edge();

            int dest = poke.getType() < 0 ? edgePoke.getSrc() : edgePoke.getDest();
            int src = pokemon.getType() < 0 ? edgePokemon.getSrc() : edgePokemon.getDest();

            if(algo.shortestPathDist(src, dest) < distance) {
                sum += poke.getValue();
            }
        }

        return sum;
    }
}
