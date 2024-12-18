package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FancodeUtils {

    public static boolean isFancodeUser(double lat, double lng) {
        return lat >= -40 && lat <= 5 && lng >= 5 && lng <= 100;
    }

    public static double calculateTaskCompletion(JSONArray todos, int userId) {
        List<JSONObject> userTodos = new ArrayList<>();
        int completedTasks = 0;

        for (int i = 0; i < todos.length(); i++) {
            JSONObject todo = todos.getJSONObject(i);
            if (todo.getInt("userId") == userId) {
                userTodos.add(todo);
                if (todo.getBoolean("completed")) {
                    completedTasks++;
                }
            }
        }

        int totalTasks = userTodos.size();
        return totalTasks > 0 ? (completedTasks * 100.0) / totalTasks : 0.0;
    }
}
