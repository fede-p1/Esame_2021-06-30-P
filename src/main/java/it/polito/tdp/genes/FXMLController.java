package it.polito.tdp.genes;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnStatistiche;

    @FXML
    private Button btnRicerca;

    @FXML
    private ComboBox<String> boxLocalizzazione;

    @FXML
    private TextArea txtResult;

    @FXML
    void doRicerca(ActionEvent event) {

    	String local = boxLocalizzazione.getValue();
    	
    	List<String> sequenza = new ArrayList<>(model.ricercaCammino(local));
    	
    	this.txtResult.appendText('\n' + "Sequenza più lunga: \n\n");
    	
    	for (String s : sequenza) {
    		txtResult.appendText(s + " -> ");
    	}
    	
    	txtResult.appendText("Tot: " + model.getPesoTot(sequenza));

    }

    @FXML
    void doStatistiche(ActionEvent event) {
    	
    	if (boxLocalizzazione.getValue() == null) {
    		this.txtResult.appendText("Scegli una localization\n\n");
    		return;
    	}
    	
    	SimpleWeightedGraph<String,DefaultWeightedEdge> graph = this.model.getGraph();
    	
    	txtResult.setText("Grafo creato con " + graph.vertexSet().size() + " vertici e "
    			+ graph.edgeSet().size() + " archi.\n\n");
    	
    	
    	
    	String local = boxLocalizzazione.getValue();
    	
    	List<Arco> archi = new ArrayList<>(model.statistiche(local));
    	Collections.sort(archi);
    	
    	txtResult.appendText("Adiacenti a: " + local + '\n' + '\n');
    	
    	for (Arco a : archi) {
    		txtResult.appendText(a.toString(local)+'\n');
    	}
    	
    	this.btnRicerca.setDisable(false);

    }

    @FXML
    void initialize() {
        assert btnStatistiche != null : "fx:id=\"btnStatistiche\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRicerca != null : "fx:id=\"btnRicerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxLocalizzazione != null : "fx:id=\"boxLocalizzazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		
		model.creaGrafo();
		
		this.boxLocalizzazione.getItems().addAll(model.getGraph().vertexSet());
		
		this.btnRicerca.setDisable(true);
		
	}
}
