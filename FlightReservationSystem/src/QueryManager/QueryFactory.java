package QueryManager;

/**
 * A lazy QueryHelper used to help build server query string
 * @author gunna
 *
 */
public class QueryFactory {
	/**
	 * help build lock string
	 * @param ticketAgency
	 * @return
	 */
	public static String lock (String ticketAgency) {
		return "team=" + ticketAgency + "&action=lockDB";
	}
	
	/**
	 * help build unlock string
	 * @param ticketAgency
	 * @return
	 */
	public static String unlock (String ticketAgency) {
		return "team=" + ticketAgency + "&action=unlockDB";
	}
	
	/**
	 * help build reserver string
	 * @param ticketAgency
	 * @param xmlFlights
	 * @return
	 */
	public static String reserve (String ticketAgency, String xmlFlights) {
		String query = "team=" + ticketAgency;
		query = query + "&action=buyTickets";
		query = query + "&flightData=" + xmlFlights;
		return query;
	}
}
