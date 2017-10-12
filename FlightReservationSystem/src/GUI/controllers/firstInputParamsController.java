package GUI.controllers;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import GUI.SceneSwitcher;
import GUI.StateMachine;
import Models.Airport;
import Models.Order;
import QueryManager.queryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class firstInputParamsController{
	@FXML RadioButton first;
	@FXML RadioButton coach;
	@FXML RadioButton oneway;
	@FXML RadioButton roundway;
	@FXML ComboBox depart;
	@FXML ComboBox arrive;
	
	@FXML ComboBox stop;
	@FXML DatePicker datepicker;
	List<Airport> airports = queryManager.getAllAirports();	
	static Stage stage=new Stage();

    /**
     * 
     * An initialization method of the controller, used to handle state transitions
     * and populate GUI elements
     * 
     */
	@FXML
	public void initialize() {		
		List<String> airportStrings = airports.stream().map(Airport::getName).collect(Collectors.toList());
		ObservableList<String> list = FXCollections.observableArrayList(airportStrings);
		depart.setItems(list);
		arrive.setItems(list);
		
		List<Integer> intArrList = new ArrayList<Integer>();
		intArrList.add(0); intArrList.add(1); intArrList.add(2);
		ObservableList<Integer> intObsList = FXCollections.observableArrayList(intArrList);
		stop.setItems(intObsList);
	}
	
    /**
     * 
     * A callback to be run when the "select flight" button is pressed
     * 
     */
	public void btnclick() throws IOException
	{		
		if(!verifyInputs()) { return; }
		

		Airport dep = airports.get(depart.getSelectionModel().getSelectedIndex());
		Airport arr = airports.get(arrive.getSelectionModel().getSelectedIndex());
		boolean isFirst = first.isSelected();
		boolean roundtrip = roundway.isSelected();
		int stopovers = stop.getSelectionModel().getSelectedIndex();
		
		LocalDate day=datepicker.getValue();		
		ZoneId zoneId = ZoneId.of("Etc/GMT+" + Math.abs(dep.gmtOffset));
		Instant instant = Instant.from(day.atStartOfDay(zoneId));
		Date date = Date.from(instant);
				
		StateMachine sm = StateMachine.getInstance();
		sm.order = new Order(dep, arr, date, isFirst, roundtrip, stopovers);
		sm.switchState(StateMachine.state.display_flights);
	}
	
	
    /**
     * A helper method used to verify GUI inputs
     * @return	 Boolean validInputs
     */
	private boolean verifyInputs() {
		int departIndex = depart.getSelectionModel().getSelectedIndex();
		int arriveIndex = arrive.getSelectionModel().getSelectedIndex();
		LocalDate day   = datepicker.getValue();
		int stopIndex   = stop.getSelectionModel().getSelectedIndex();
		
		// check for failed inputs
		if(departIndex == -1 || arriveIndex == -1 || day == null || stopIndex == -1) {
			guiHelpers.throwPopup("You must input all search parameters before proceeding");
			return false;
		}
		
		// check for repeated airports
		if(departIndex == arriveIndex) {
			guiHelpers.throwPopup("If you're flying to the same place you already are,\nyou don't need our help, now do ye?");
			return false;
		}
		
		// check for bookings in the past
		Airport dep = airports.get(depart.getSelectionModel().getSelectedIndex());
		ZoneId zoneId = ZoneId.of("Etc/GMT+" + Math.abs(dep.gmtOffset));
		Instant instant = Instant.from(day.atStartOfDay(zoneId));
		if(Date.from(instant).getTime() < new Date().getTime()) {
			guiHelpers.throwPopup("If you're aiming to travel in the past,\nyou'll need the help of a time machine, not an airplane.");
			return false;
		}
		return true;
	}

}
