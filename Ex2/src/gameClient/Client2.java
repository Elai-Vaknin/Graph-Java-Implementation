package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Client2 implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static CL_Pokemon[] targetedPokes;

	public static void main(String[] a) {
		Thread client = new Thread(new EX2());
		client.start();
	}

	public static directed_weighted_graph json2graph(String fs) {
		directed_weighted_graph graph = new DWGraph_DS();

		try {
			JSONObject ttt = new org.json.JSONObject(fs);

			JSONArray ags1 = ttt.getJSONArray("Nodes");
			JSONArray ags2 = ttt.getJSONArray("Edges");

			for (int i = 0; i < ags1.length(); i++) {
				org.json.JSONObject pp = ags1.getJSONObject(i);

				int id = pp.getInt("id");
				String p = pp.getString("pos");

				geo_location loc = new GeoLocation(p);
				node_data node = new NodeData(id, loc);

				graph.addNode(node);
			}
			for (int i = 0; i < ags2.length(); i++) {
				org.json.JSONObject pp = ags2.getJSONObject(i);

				int src = pp.getInt("src");
				int dest = pp.getInt("dest");
				double w = pp.getDouble("w");

				graph.connect(src, dest, w);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return graph;
	}

	@Override
	public void run() {
		int scenario_num = 0;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
	//	int id = 999;
	//	game.login(id);
		String g = game.getGraph();
		String pks = game.getPokemons();
		directed_weighted_graph gg = json2graph(game.getGraph());
		init(game);
		
		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		long dt=100;
		
		while(game.isRunning()) {
			moveAgants(game, gg);
			try {
				if(ind%1==0) {_win.repaint();}
				Thread.sleep(dt);
				ind++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		String res = game.toString();

		System.out.println(res);
		System.exit(0);
	}
	/** 
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph gg) {
		String lg = game.move();
		List<CL_Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);
		for(int i=0;i<log.size();i++) {
			CL_Agent ag = log.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if(dest==-1) {
				//dest = nextNode(gg, src, ffs, ag, log);
				dest = nextNode(gg, src);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode3(directed_weighted_graph graph, int src, List<CL_Pokemon> pokemons, CL_Agent ag, List<CL_Agent> agents) {
		if(pokemons.isEmpty())
			return src;

		double maxWorthy = 0.0;
		int finalTarget = src;
		List<node_data> targetPath = null;
		CL_Pokemon target = null;
		dw_graph_algorithms algo = new DWGraph_Algo(graph);

		for(CL_Pokemon pokemon : pokemons) {
			boolean taken = false;

			for(CL_Agent agent : agents) {
				if(ag == agent)
					continue;

				if(targetedPokes[agent.getID()] == pokemon)
					taken = true;
			}

			if(taken)
				continue;

			Arena.updateEdge(pokemon, graph);

			edge_data pokeEdge = pokemon.get_edge();

			double distance1 = algo.shortestPathDist(src, pokeEdge.getSrc());
			double distance2 = algo.shortestPathDist(src, pokeEdge.getDest());

			int dest = distance1 > distance2 ? pokeEdge.getSrc() : pokeEdge.getDest();

			List<node_data> path = algo.shortestPath(src, dest);

			double distance = algo.shortestPathDist(src, dest);
			double time = distance / ag.getSpeed();
			double worthy = pokemon.getValue() / time;

			if (worthy > maxWorthy) {
				maxWorthy = worthy;
				targetPath = path;
				target = pokemon;
			}
		}

		System.out.println(targetPath + " Target: " + finalTarget + " Current: " + ag.get_curr_edge());

		boolean flag2 = false;

		for(int i = 0; i < targetPath.size(); i++) {
			if(flag2) {
				finalTarget = targetPath.get(i).getKey();
				break;
			}

			if(targetPath.get(i).getKey() == ag.getSrcNode()) {
				flag2 = true;
			}
		}

		ag.setNextNode(finalTarget);
		targetedPokes[ag.getID()] = target;

		return finalTarget;
	}

	private static int nextNode(directed_weighted_graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

	private void init(game_service game) {
		String g = game.getGraph();
		String fs = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);

	
		_win.show();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			targetedPokes = new CL_Pokemon[rs];
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
			for(int a = 0;a<rs;a++) {
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
				
				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
	}
}
