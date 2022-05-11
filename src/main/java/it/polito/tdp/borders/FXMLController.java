
package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
	@FXML
    private ComboBox<Country> cmbCountry;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	
    	// controlli in input
    	Integer anno = null;
    	try {
    		anno = Integer.parseInt(this.txtAnno.getText());
    	} catch(NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.setText("Inserisci un valore numerico!");
    		return;
    	}
    	if(anno<1816 || anno>2016) {
    		txtResult.setText("L'anno deve essere compreso tra il 1816 e il 2016!");
    	}
    	
    	this.model.createGraph(anno);
    	
    	txtResult.appendText(this.model.printGraph());
    }
    
    @FXML
    void handleSearch(ActionEvent event) {
    	txtResult.clear();
    	Country origin = this.cmbCountry.getValue();
    	this.txtResult.appendText("Stati raggiungibili a partire da: " + origin.getStateNme());
    	for(Country c : this.model.depthSearch_r(origin)) {
    		txtResult.appendText("\n - " + c);
    	}
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbCountry.getItems().addAll(this.model.getCountries());
    }
}
