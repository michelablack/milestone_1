package logic;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class RetrieveTicketsID {




   private static String readAll(Reader rd) throws IOException {
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	         sb.append((char) cp);
	      }
	      return sb.toString();
	   }

   public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
         String jsonText = readAll(rd);
         JSONArray json = new JSONArray(jsonText);
         return json;
       } finally {
         is.close();
       }
   }

   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
         String jsonText = readAll(rd);
         JSONObject json = new JSONObject(jsonText);
         return json;
       } finally {
         is.close();
       }
   }

   public static void csvWriter(String month, String numName, int num, File file) {
	   
   }

  
   public static void main(String[] args) throws IOException, JSONException {
	   File file = new File("file.csv");
	   String s =null;
	   PrintWriter writer = new PrintWriter(file);
	   List<String> monthList= Arrays.asList("Jan","Feb","Mar","Apr","May","Jun",
			   "Jul","Aug","Sep","Oct","Nov","Dec");
	   int[] count = new int[12];
	   StringBuilder sb = new StringBuilder();
	   String projName ="STDCXX";
	   String folder = "C:\\Users\\miche\\"+projName;
	   Integer j = 0, i = 0, total = 1;
      //Get JSON API for closed bugs w/ AV in the project
	 
	   sb.append("Month");
	   sb.append(';');
	   sb.append("NumFixed");
	   sb.append('\n');
	
      do {
         //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
         j = i + 1000;
         String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                + projName + "%22AND(%22status%22=%22closed%22OR"
                + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate"
                + i.toString() + "&maxResults=" + j.toString();
         JSONObject json = readJsonFromUrl(url);
         JSONArray issues = json.getJSONArray("issues");
         total = json.getInt("total");
         for (; i < total && i < j; i++) {
            //Iterate through each bug
            String key = issues.getJSONObject(i%1000).get("key").toString();
            Process p = Runtime.getRuntime().exec("git -C " +folder+ " log\r\n" + 
            		" --pretty=format:'%cd' --grep="+key);
            BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));
        	List<String> list = new ArrayList<String>();
            while ((s = stdInput.readLine()) != null) {
            	list.add(s);
            }
            if (!list.isEmpty()){
                //System.out.println(key);
                String month = list.get(0).substring(5, 8);
                //System.out.println(month);
            	int ind = monthList.indexOf(month);
            	count[ind] += 1;
            }
            
         }  
         
         
      } while (i < total);
      
      for (int k=0; k<12; k++) {
     	 
     	 sb.append(monthList.get(k));
     	 sb.append(';');
		 sb.append(count[k]);
		 sb.append('\n');
		     
		     
      }
      writer.write(sb.toString());
      writer.close();
      return;
   }

 
}