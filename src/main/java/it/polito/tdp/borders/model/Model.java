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
		
		List<Border> borders = new ArrayList<>(dao.getCountryPairs(anno));
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
	
	public List<Country> depthSearch(Country origin) {
		this.createGraph(2016);
		if(!this.graph.vertexSet().contains(origin)) {
			throw new RuntimeException("The graph does not contains this country!");
		}
		
		List<Country> reachables = new ArrayList<>();
		DepthFirstIterator<Country, DefaultEdge> iterator = new DepthFirstIterator<Country, DefaultEdge> (this.graph, origin);
		
		//reachables.add(origin);
		while(iterator.hasNext()) {
			reachables.add(iterator.next());
		}
		
		return reachables;
	}

	public List<Country> getCountries() {
		return countries;
	}
	
	
}
