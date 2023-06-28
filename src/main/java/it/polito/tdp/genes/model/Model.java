package it.polito.tdp.genes.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.genes.db.GenesDao;

public class Model {

	GenesDao dao;
	SimpleWeightedGraph<String,DefaultWeightedEdge> graph;
	List<Arco> archi;
	
	public Model() {
		dao = new GenesDao();
	}
	
	public SimpleWeightedGraph<String,DefaultWeightedEdge> creaGrafo(){
		
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		List<String> vertex = new ArrayList<>(dao.getAllLocalizations());
		
		Graphs.addAllVertices(graph, vertex);
		
		archi = new ArrayList<>(dao.getArchi());
		
		System.out.println(archi);
		
		for (Arco arco : archi) {
			if (graph.vertexSet().contains(arco.getLoc1()) && graph.vertexSet().contains(arco.getLoc2()))
			{
				Graphs.addEdge(graph, arco.getLoc1(), arco.getLoc2(), arco.getTipi().size());
			}
		}
		
		return graph;
	}
	
	public List<Arco> statistiche(String local){
		
		List<Arco> vicini = new ArrayList<Arco>();
		
		for (Arco arco : archi) {
			if ((arco.getLoc1().compareTo(local) == 0) || (arco.getLoc2().compareTo(local) == 0))
					vicini.add(arco);
		}
		
		return vicini;
		
	}
	
	List<String> soluzione;
	int pesoMax;
	
	public List<String> ricercaCammino(String local){
		
		List<String> parziale = new ArrayList<>();
		
		DepthFirstIterator<String,DefaultWeightedEdge> dfi = new DepthFirstIterator<>(graph,local);
		
		pesoMax = 0;
		
		soluzione = new ArrayList<>();
		
		ricorsiva(dfi, parziale);
		
		return soluzione;
		
	}
	
	private void ricorsiva(DepthFirstIterator<String,DefaultWeightedEdge> dfi,List<String> parziale) {
		
		if (getPesoTot(parziale) > pesoMax) {
			pesoMax = getPesoTot(parziale);
			soluzione = new ArrayList<>(parziale);
		}
		
		while(dfi.hasNext()) {
			String next = dfi.next();
			parziale.add(next);
			ricorsiva(dfi,parziale);
			parziale.remove(parziale.size()-1);
		}
		
	}
	
	public int getPesoTot(List<String> parziale) {
		int pesoTot = 0;
		for (int i=0; i<parziale.size()-1; i++) {
			DefaultWeightedEdge edge = graph.getEdge(parziale.get(i), parziale.get(i+1));
			double pesoArco = graph.getEdgeWeight(edge);
			pesoTot+=pesoArco;
		}
		return pesoTot;
	}
	
	

	public GenesDao getDao() {
		return dao;
	}

	public void setDao(GenesDao dao) {
		this.dao = dao;
	}

	public SimpleWeightedGraph<String, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public void setGraph(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	public List<Arco> getArchi() {
		return archi;
	}

	public void setArchi(List<Arco> archi) {
		this.archi = archi;
	}
	
	
}