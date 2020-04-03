package logic;

public class QueryGenerator {
	
	/**
	 * Function to obtain, through rest API, all the tickets that have been fixed.
	 * @return the String from which the Json is derived. 
	 */
	public static String getFixedTickets(String projName, Integer i, Integer j) {
		
		String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                + projName + "%22AND(%22status%22=%22closed%22OR"
                + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate"
                + i.toString() + "&maxResults=" + j.toString();
		return url;
		
	}
	
	/**
	 * Function to obtain, through rest API, all the available tickets for the project.
	 * @return the String from which the Json is derived. 
	 */
	public static String getAllTickets(String projName, Integer i, Integer j) {
		String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                + projName + "%22&fields=key,resolutiondate"
                + i.toString() + "&maxResults=" + j.toString();
		
		return url;
		
	}
}
