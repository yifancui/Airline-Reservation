package GUI.controllers;

import java.util.ArrayList;

import GUI.StateMachine;
import GUI.StateMachine.state;
import Models.Flight;
import QueryManager.queryManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class confirmcontroller {
	@FXML TableView<ArrayList<Flight>> table;
	@FXML Button select;
	@FXML Button cancel;
	
    /**
     * 
     * An initialization method of the controller, used to handle state transitions
     * and populate GUI elements
     * 
     */
	@FXML
	public void initialize(){
		StateMachine sm = StateMachine.getInstance();
		
		// create an observable list of all necessary flight paths
		ArrayList<ArrayList<Flight>> tmp = new ArrayList<ArrayList<Flight>>();
		tmp.add(sm.order.firstFlightPath);
		if(sm.order.roundtrip) { tmp.add(sm.order.secondFlightPath); }
		ObservableList<ArrayList<Flight>> list = FXCollections.observableArrayList(tmp);
		
		table.getColumns().addAll(guiHelpers.getColumns());
		table.setItems(list);
	}
	
    /**
     * A callback to be run when the "confirm" button is pressed
     */
	public void select(){
		StateMachine sm = StateMachine.getInstance();
		ArrayList<Flight> toReserve = new ArrayList<Flight>();
		toReserve.addAll(sm.order.firstFlightPath);
		toReserve.addAll(sm.order.secondFlightPath);
		queryManager.reserveFlights(toReserve, sm.order.firstClass);
		StateMachine.getInstance().switchState(state.finish);		
	}
	
	/**
	 * A callback to be run when the "cancel" button is pressed
	 */
	public void cancel(){
		StateMachine sm = StateMachine.getInstance();
		sm.switchState(state.finish);
	}
	
}
