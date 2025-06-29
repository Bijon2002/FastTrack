/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import Backend.DBConnection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
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
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric"
                + "&origins=" + URLEncoder.encode(origin, "UTF-8")
                + "&destinations=" + URLEncoder.encode(destination, "UTF-8")
                + "&key=" + API_KEY;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        String jsonData = response.body().string();
        JSONObject json = new JSONObject(jsonData);

        JSONObject element = json.getJSONArray("rows")
                                 .getJSONObject(0)
                                 .getJSONArray("elements")
                                 .getJSONObject(0);

        String status = element.getString("status");

        if (!status.equals("OK")) {
            System.err.println("🚫 Google Maps API returned status: " + status);
            JOptionPane.showMessageDialog(null, "❌ Invalid address. Please check sender and receiver.");
            return result;
        }

        // ✅ Extract distance & duration
        String distance = element.getJSONObject("distance").getString("text"); // e.g., "118 km"
        String duration = element.getJSONObject("duration").getString("text"); // e.g., "2 hours 15 mins"

        result.put("distance", distance);
        result.put("duration", duration);

        // ✅ Clean distance for DB
        String cleanDistance = distance.replace("km", "").trim();
        double distanceValue = Double.parseDouble(cleanDistance);

        // ✅ Convert duration to HH:mm:ss
        int hours = 0, minutes = 0;
        if (duration.contains("hour")) {
            hours = Integer.parseInt(duration.split("hour")[0].trim());
            if (duration.contains("mins")) {
                String minsPart = duration.split("hour")[1].replace("mins", "").trim();
                minutes = Integer.parseInt(minsPart);
            }
        } else {
            minutes = Integer.parseInt(duration.replace("mins", "").trim());
        }

        String estimatedTimeFormatted = String.format("%02d:%02d:00", hours, minutes); // e.g., "02:15:00"

        // ✅ Insert into DB
        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO routes (destination, distance, estimated_time) VALUES (?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, destination);
        stmt.setDouble(2, distanceValue);
        stmt.setString(3, estimatedTimeFormatted);

        int rows = stmt.executeUpdate();
        if (rows > 0) {
            JOptionPane.showMessageDialog(null, "✅ Route added successfully!");
        }

    } catch (Exception e) {
        e.printStackTrace(); // ✅ For debugging
        JOptionPane.showMessageDialog(null, "❌ Error while calculating route.");
    }

    return result;
}
}

    
    /*public static void main(String[] args) {
        // 🔍 Replace with your test locations
        String origin = "";
        String destination = "";

        System.out.println("⏳ Requesting distance from " + origin + " to " + destination + "...");

        Map<String, String> info = getDistanceAndTime(origin, destination);

        if (!info.isEmpty()) {
            System.out.println("✅ Distance: " + info.get("distance"));
            System.out.println("🕐 Duration: " + info.get("duration"));
        } else {
            System.out.println("❌ Failed to get distance/duration");
        
    }
        
    }
    
}*/
