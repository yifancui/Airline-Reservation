package QueryManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import Models.Airplane;
import Models.Airport;
import Models.Flight;

public class queryManager {
	private static String baseURL = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem?team=SSR&action=list";
	private static String mUrlBase = "http://cs509.cs.wpi.edu:8181/CS509.server/ReservationSystem";

	queryManager() { } // prevent this class from being instantiated
	
	/**
	 * @return All viable airports within the WPI database
	 */
	public static ArrayList<Airport> getAllAirports() {
		File file = new File("src/Data/airports.xml");
		
		ArrayList<Airport> airports = XMLParser.parseAirports(file);
		return airports;
	}
	
	/**
	 * @return All viable Airplanes within the WPI database
	 */
	public static ArrayList<Airplane> getAllAirplanes() {
		File file = new File("src/Data/Airplanes.xml");
		ArrayList<Airplane> airplanes = XMLParser.parseAirplanes(file);
		return airplanes;
	}
	
	/**
	 * 
	 * @param airportCode airport code of the flights
	 * @param date date of flights
	 * @return flights FROM a given airport on a given date
	 */
	public static ArrayList<Flight> getDepFlights(String airportCode, Date date) {

		   String modifiedDate= new SimpleDateFormat("yyyy_MM_dd").format(date);		   
		   String query = baseURL + "&list_type=departing&airport=" + airportCode + "&day=" + modifiedDate;

		   return XMLParser.parseFlights(getXMLFromServer(query));

	}
	
	/**
	 * @param airportCode Airport code of the flights
	 * @param date Date of flights
	 * @return flights TO a given airport on a given date
	 */
	public static ArrayList<Flight> getArrFlights(String airportCode, Date date) {
		   String modifiedDate= new SimpleDateFormat("yyyy_MM_dd").format(date);		   
		   String query = baseURL + "&list_type=arriving&airport=" + airportCode + "&day=" + modifiedDate;
		   return XMLParser.parseFlights(getXMLFromServer(query));
	}
	
	/**
	 * Helper method within class--takes away the silly amounts of 
	 * try/catch necessary for an API call.
	 * @param query 
	 * @return String of XML file
	 */
	
	public static String getXMLFromServer (String query) {
		  URL url;
		  HttpURLConnection connection;
		  BufferedReader reader;
		  String line;
		  StringBuffer result = new StringBuffer();

		  try {
		   /**
		    * Create an HTTP connection to the server for a GET 
		    */			  			  
		   url = new URL(query);
		   
//		   System.out.println(url.toString());
		   connection = (HttpURLConnection) url.openConnection();
		   connection.setRequestMethod("GET");

		   /**
		    * If response code of SUCCESS read the XML string returned
		    * line by line to build the full return string
		    */
		   int responseCode = connection.getResponseCode();
		   if ((responseCode >= 200) && (responseCode <= 299)) {
		    InputStream inputStream = connection.getInputStream();
		    String encoding = connection.getContentEncoding();
		    encoding = (encoding == null ? "URF-8" : encoding);

		    reader = new BufferedReader(new InputStreamReader(inputStream));
		    while ((line = reader.readLine()) != null) {
		     result.append(line);
		    }
		    reader.close();
		   }
		  } catch (Exception e) {
		   e.printStackTrace();
		  }

		  return result.toString();
		 }

	/**
	 * Lock the server database in preparation to making a reservation
	 * 
	 * @param team identifies the team locking the database
	 * 
	 * @return true if database locked successfully
	 */
	public static boolean lock (String team) {
		URL url;
		HttpURLConnection connection;

		try {
			url = new URL(mUrlBase);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", team);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			String params = QueryFactory.lock(team);
			
			connection.setDoOutput(true);
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(params);
			writer.flush();
			writer.close();
			
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' to lock database");
			System.out.println(("\nResponse Code : " + responseCode));
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuffer response = new StringBuffer();
			
			while ((line = in.readLine()) != null) {
				response.append(line);
			}
			in.close();
			
			System.out.println(response.toString());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Unlock the server database previously locked
	 * 
	 * @param team identifies the team requestiong the server database be unlocked
	 * 
	 * @return true if server database successfully unlocked
	 */
	public static boolean unlock (String team) {
		URL url;
		HttpURLConnection connection;
		
		try {
			url = new URL(mUrlBase);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			
			String params = QueryFactory.unlock(team);
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(params);
			writer.flush();
			writer.close();
		    
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' to unlock database");
			System.out.println(("\nResponse Code : " + responseCode));

			if ((responseCode >= 200) && (responseCode <= 299)) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response = new StringBuffer();

				while ((line = in.readLine()) != null) {
					response.append(line);
				}
				in.close();

				System.out.println(response.toString());
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Reserve a seat according to the flight number and seating type
	 * @param listFlights list of flights
	 * @param isFirstClass seating type
	 * @return true if SUCCESS code return from server
	 */
	public static boolean reserveFlights(ArrayList<Flight> listFlights, boolean isFirstClass) {
		String flightClass = "";				
		if(isFirstClass) {
			flightClass = "FirstClass";
		} else {
			flightClass = "Coach";
		}
		String ans ="";
		ans=ans+"<Flights>";
		for(Flight flight : listFlights){
			ans=ans+"<Flight number=\""+flight.num+"\" seating=\""+flightClass+"\"/>";
			}
		ans=ans+"</Flights>";
		lock("SSR");
		boolean success = buyTickets("SSR", ans);
		unlock("SSR");
		return success;
	}
	/**
	 * Reserve a seat on one or more connecting flights
	 * 
	 * The XML string identifying the reserveation is created by the calling client. 
	 * This method creates the HTTP POST request to reserve the fligt(s) as specified
	 * 
	 * @param team identifying the team making the reservation
	 * @param xmlReservation is the string identifying the reservation to make
	 * 
	 * @return true if SUCCESS code returned from server
	 */
	private static boolean buyTickets(String team, String xmlReservation) {
		URL url;
		HttpURLConnection connection;

		try {
			url = new URL(mUrlBase);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			String params = QueryFactory.reserve(team, xmlReservation);

			System.out.println("\nSending 'POST' to ReserveFlights");
			System.out.println("\nSending " + params);
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes(params);
			writer.flush();
			writer.close();
			
			int responseCode = connection.getResponseCode();
			System.out.println("\nSending 'POST' to ReserveFlights");
			System.out.println(("\nResponse Code : " + responseCode));

			if ((responseCode >= 200) && (responseCode <= 299)) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response = new StringBuffer();

				while ((line = in.readLine()) != null) {
					response.append(line);
				}
				in.close();

				System.out.println(response.toString());
				return true;
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response = new StringBuffer();

				while ((line = in.readLine()) != null) {
					response.append(line);
				}
				in.close();

				System.out.println(response.toString());
				return false;
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
