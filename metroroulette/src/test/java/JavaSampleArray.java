import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cob3218.metroroulette.model.Station;


public class JavaSampleArray {
    
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create("https://api.wmata.com/Rail.svc/json/jStations?LineCode=RD")).header("api_key", "9960c138fb2e48fcbb0e6bf41ce573db")
         .build();
        String str = client.sendAsync(request, BodyHandlers.ofString())
         .join()
         .body(); 
        
        //removes leading and end text/brackets to create json array with [] at start/end
        while(str.charAt(0) != '[') {
            str = str.substring(1, str.length());
        }
        str = str.substring(0, str.length() - 1);

        JSONArray jsonArr = new JSONArray(str);
        System.out.println(jsonArr.toString());

        for(Object j : jsonArr) {
            if(j.getClass().equals(JSONObject.class)) {
                System.out.println(new Station((JSONObject) j));
            }
        }
    }
}
