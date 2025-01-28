package com.dnamaster10.traincartsticketshop.util;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MojangApiAccessor {
    /**
     * Queries the Mojang API for the specified username.
     *
     * @param username The username to search for
     * @return A String[] containing the player's username at the first index, and their UUID at the second index
     * @throws IOException Thrown if an error occurred creating or sending the HTTP request.
     */
    public String[] getPlayerFromUsername(String username) throws IOException {
        //Returns an array containing 2 strings, looks like:
        //["username", "uuid"]
        //Should be executed async or risk server performance loss
        StringBuilder result = new StringBuilder();
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null;) {
                result.append(line);
            }
        }

        //Result is in json format, so we must extract data from it
        JSONObject jsonObject = new JSONObject(result.toString());
        String uuid = jsonObject.getString("id");
        uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
        String name = jsonObject.getString("name");

        return new String[]{name, uuid};
    }
}
