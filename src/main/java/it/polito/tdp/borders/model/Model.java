package it.polito.tdp.borders.model;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
//	private Logger logger = LoggerFactory.getLogger(Model.class);
	
	private List<Country> countries;
	private Map<Integer, Country> countriesMap;
	
	private Graph<Country, DefaultEdge> graph;
	
	private BordersDAO dao;

	public Model() {
		dao = new BordersDAO();
		this.countries = dao.loadAllCountries();
		this.countriesMap = new HashMap<>();
		
		for(Country c : this.countries) {
			this.countriesMap.put(c.getCCode(), c);
		}
		
	}

	public void createGraph(int anno) {
		this.graph = new SimpleGraph<>(DefaultEdge.class);
		
//		Graphs.addAllVertices(graph, this.countries);
		
		List<Border> borders = dao.getCountryPairs(anno);
		//logger.info("Borders {}", borders);
		
		for(Border b : borders) {
			if(!this.graph.containsVertex(this.countriesMap.get(b.getState1no()))) {
				this.graph.addVertex(this.countriesMap.get(b.getState1no()));
			}
			if (!this.graph.containsVertex(this.countriesMap.get(b.getState2no())))
				this.graph.addVertex(this.countriesMap.get(b.getState2no()));
			
			this.graph.addEdge(this.countriesMap.get(b.getState1no()), this.countriesMap.get(b.getState2no()));
		}
	}
	
	public String printGraph() {
		String output = "Creato un grafo con:\n * " + this.graph.vertexSet().size() + " vertici;\n * " + this.graph.edgeSet().size() + " archi.\n";
		for(Country c : this.graph.vertexSet()) {
			output += "\n" + c + ": " + this.graph.edgesOf(c).size() + " stati confinanti;";
		}
		return output;
	}
	
	/**
	 * Utilizzo degli iteratori della libreria jgrapht per la visita in profondità.
	 * @param origin
	 * @return Una lista di nodi raggiungibili a partire da origin
	 */
	public List<Country> depthSearch(Country origin) {
		this.createGraph(2016);
		if(!this.graph.vertexSet().contains(origin)) {
			throw new RuntimeException("The graph does not contain this country!");
		}
		
		List<Country> reachables = new ArrayList<>();
		DepthFirstIterator<Country, DefaultEdge> iterator = new DepthFirstIterator<Country, DefaultEdge> (this.graph, origin);
		
		//reachables.add(origin);
		while(iterator.hasNext()) {
			reachables.add(iterator.next());
		}
		
		return reachables;
	}
	
	/**
	 * Versione ricorsiva della visita in profondità.
	 * @param origin
	 * @return una lista di nodi raggiungibili a partire da origin
	 */
	public List<Country> depthSearch_r(Country origin, int anno) {
		this.createGraph(anno);
		List<Country> reachables = new ArrayList<Country>();
		List<Border> borders = new ArrayList<>(dao.getCountryPairs(2016));
		reachables.add(origin);
		
		return (this.recursive_search(reachables, borders));		
	}
	
	private List<Country> recursive_search(List<Country> cs, List<Border> bs) {
		List<Country> btemp = this.getBoundaries(cs.get(cs.size()-1), bs, cs);
		for(Country cc : btemp) {
			if(!cs.contains(cc)) {
				cs.add(cc);
				this.recursive_search(cs, bs);
			}
		}
		return cs;
	}
	
	/**
	 * Versione iterativa della visita in ampiezza.
	 * @param origin Il Country di partenza
	 * @param anno L'anno di riferimento per creare il grafo
	 * @return una List di Country raggiungibili
	 */
	public List<Country> breadthSearch_iterative(Country origin, int anno) {
		this.createGraph(anno);
		List<Country> toVisit = new ArrayList<>();
		toVisit.add(origin);
		
		List<Country> visited = new ArrayList<>();
		List<Border> borders = dao.getCountryPairs(anno);
		
		for(int i=0; i<toVisit.size(); i++) { // il for( : ) utilizza gli iterator che lanciano la ConcurrentModificationException
			Country ctemp = toVisit.get(i);
			toVisit.addAll(toVisit.size(), this.getBoundaries(ctemp, borders, toVisit));
			visited.add(ctemp);
			//toVisit.remove(ctemp);
		}
		return visited;
	}
	
	/**
	 * Ritorna tutti i Country adiacenti al Country passato come parametro {@code c}.
	 * @param c country
	 * @param bs
	 * @param partial
	 * @return una List di Country adiacenti a {@code c}
	 */
	public List<Country> getBoundaries(Country c, List<Border> bs, List<Country> partial) {
		List<Country> res = new ArrayList<>();

		for(Country cc : this.graph.vertexSet()) {
			if(!partial.contains(cc) && graph.containsEdge(c, cc))
				res.add(cc);
		}
		return res;
	}

	public List<Country> getCountries() {
		return countries;
	}
	
	
}
