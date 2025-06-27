/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
/**
 *
 * @author KT
 */
public class DistanceCalculator {
    
    // 🔐 Replace with your actual Google Maps API key
    private static final String API_KEY = "AIzaSyCIdBNlNh_ohfysfv8JRFKfLHMR608p1xg";

    /**
     * 🌍 Calculates distance and duration between two locations
     * @param origin - sender address (e.g., "Colombo, Sri Lanka")
     * @param destination - receiver address (e.g., "Galle, Sri Lanka")
     * @return Map with keys: "distance" and "duration"
     */
    public static Map<String, String> getDistanceAndTime(String origin, String destination) {
        Map<String, String> result = new HashMap<>();

        try {
            // 🌐 Create the API request URL with proper encoding
            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric"
                    + "&origins=" + URLEncoder.encode(origin, "UTF-8")
                    + "&destinations=" + URLEncoder.encode(destination, "UTF-8")
                    + "&key=" + API_KEY;

            // 📡 Send the HTTP GET request using OkHttp
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            // 📥 Get the JSON response from the API
            String jsonData = response.body().string();
            JSONObject json = new JSONObject(jsonData);

            // 📏 Extract the distance string (e.g., "118 km")
            String distance = json.getJSONArray("rows").getJSONObject(0)
                                  .getJSONArray("elements").getJSONObject(0)
                                  .getJSONObject("distance").getString("text");

            // ⏱️ Extract the duration string (e.g., "2 hours 15 mins")
            String duration = json.getJSONArray("rows").getJSONObject(0)
                                  .getJSONArray("elements").getJSONObject(0)
                                  .getJSONObject("duration").getString("text");

            // 📌 Store in result map
            result.put("distance", distance);
            result.put("duration", duration);

        } catch (Exception e) {
            // ⚠️ Log any error that occurs
            e.printStackTrace();
        }

        return result;
    }
    
    public static void main(String[] args) {
        // 🔍 Replace with your test locations
        String origin = "Jaffna";
        String destination = "Uduvil";

        System.out.println("⏳ Requesting distance from " + origin + " to " + destination + "...");

        Map<String, String> info = getDistanceAndTime(origin, destination);

        if (!info.isEmpty()) {
            System.out.println("✅ Distance: " + info.get("distance"));
            System.out.println("🕐 Duration: " + info.get("duration"));
        } else {
            System.out.println("❌ Failed to get distance/duration");
        
    }
        
    }
    
}
