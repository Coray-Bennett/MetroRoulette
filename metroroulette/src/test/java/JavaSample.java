
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONException;
import org.json.JSONObject;

import com.cob3218.metroroulette.model.Station;

public class JavaSample 
{
    public static void main(String[] args) throws JSONException 
    {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create("https://api.wmata.com/Rail.svc/json/jStationInfo?StationCode=C02")).header("api_key", "9960c138fb2e48fcbb0e6bf41ce573db")
         .build();
        String str = client.sendAsync(request, BodyHandlers.ofString())
         .join()
         .body(); 

        JSONObject json = new JSONObject(str);
        Station station = new Station(json);

        System.out.println(station.toString());
    }
}