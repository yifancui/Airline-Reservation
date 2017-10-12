package GUI.controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import GUI.SceneSwitcher;
import Models.Flight;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class guiHelpers {
	private guiHelpers() { } //don't allow instantiation
	
	/**
	 * The public facing method for UI table population--call to recieve commonly-
	 * reused TableColumn objects
	 * @return columns ArrayList<TableColumn<ArrayList<Flight>, String>>
	 */
	public static ArrayList<TableColumn<ArrayList<Flight>, String>> getColumns() {
		ArrayList<TableColumn<ArrayList<Flight>, String>> toReturn = new ArrayList<TableColumn<ArrayList<Flight>, String>>();
		toReturn.add(makeFliColumn());
		toReturn.add(makeDepTimeColumn());
		toReturn.add(makeArrTimeColumn());
		toReturn.add(makeDurColumn());
		toReturn.add(makePriColumn());		
		return toReturn;
	}
	
	/**
	 * Displays a modal popup with the given string
	 * 
	 * @param toYell string to display
	 */
	public static void throwPopup(String toYell) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(SceneSwitcher.primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(toYell));
        Scene dialogScene = new Scene(dialogVbox, 400, 100);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	/**
	 * Helper method to create departure time column
	 * @return depTimeColumn
	 */
	private static TableColumn<ArrayList<Flight>,String> makeDepTimeColumn() {
        TableColumn<ArrayList<Flight>,String> depTimeColumn = new TableColumn<ArrayList<Flight>,String>("Departure Time (local)");
        depTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Flight>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Flight>, String> param) {
            	String s = "";
            	for(Flight f : param.getValue()) {
                	TimeZone.setDefault(TimeZone.getTimeZone("Etc/GMT+" + Math.abs(f.dep.gmtOffset)));     
                	s = s + f.depDate.toString() + "\n";
            	}
            	return new SimpleStringProperty(s);
        	}
        });
        return depTimeColumn;
	}
	
	/**
	 * Helper method to create arrival time column
	 * @return arrTimeColumn
	 */
	private static TableColumn<ArrayList<Flight>,String> makeArrTimeColumn() {
        TableColumn<ArrayList<Flight>,String> arrTimeColumn = new TableColumn<ArrayList<Flight>,String>("Arrival Time (local)");
        arrTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Flight>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Flight>, String> param) {
            	String s = "";
            	for(Flight f : param.getValue()) {
                	TimeZone.setDefault(TimeZone.getTimeZone("Etc/GMT+" + Math.abs(f.arr.gmtOffset)));     
                	s = s + f.arrDate.toString() + "\n";
            	}
            	return new SimpleStringProperty(s);
        	}
        });
        return arrTimeColumn;
	}
	
	/**
	 * Helper method to create flights column
	 * @return fliColumn
	 */
	private static TableColumn<ArrayList<Flight>,String> makeFliColumn() {
		TableColumn<ArrayList<Flight>,String> fliColumn = new TableColumn<ArrayList<Flight>,String>("Flights & Connections");
        fliColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Flight>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Flight>, String> param) {
            	ArrayList<Flight> path = param.getValue();
            	String s = "";
            	for(int i = 0; i < path.size(); i++) {
            		s = s + path.get(i).dep.code + " --> " + path.get(i).arr.code + "  |  " + path.get(i).num + "\n";
            	}	
                return new SimpleStringProperty(s);
            }
        });
        return fliColumn;
	}
	
	/**
	 * Helper method to create flight duration
	 * @return durColumn
	 */
	private static TableColumn<ArrayList<Flight>,String> makeDurColumn() {
        TableColumn<ArrayList<Flight>,String> durColumn = new TableColumn<ArrayList<Flight>,String>("Total Duration");
        durColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Flight>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Flight>, String> param) {
            	int tot = param.getValue().stream().filter(f -> f.getDuration() > 10).mapToInt(f -> f.getDuration()).sum();
            	return new SimpleStringProperty(tot + " minutes");
        	}
        });
        return durColumn;
	}
	
	/**
	 * Helper method to create price column
	 * @return priColumn
	 */
	private static TableColumn<ArrayList<Flight>,String> makePriColumn() {
        TableColumn<ArrayList<Flight>,String> priColumn = new TableColumn<ArrayList<Flight>,String>("Total Price");
        priColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<Flight>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<Flight>, String> param) {
            	double tot = param.getValue().stream().filter(f -> f.getPrice() > 10).mapToDouble(f -> f.getPrice()).sum();
            	return  new SimpleStringProperty("$" +  new DecimalFormat("#.00").format(tot));
            }
        });
        return priColumn;  
	}
}
