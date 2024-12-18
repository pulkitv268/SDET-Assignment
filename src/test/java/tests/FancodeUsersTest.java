package tests;

import api.ApiClient;
import base.TestBase;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.FancodeUtils;

import java.util.ArrayList;
import java.util.List;

public class FancodeUsersTest extends TestBase {

    @Test
    public void testFancodeUsersTaskCompletion() {
        test = extent.createTest("Test Fancode Users Task Completion",
                "Validates that all users in FanCode city have more than 50% of their todos completed");

        // Get users and todos
        Response usersResponse = ApiClient.getUsers();
        Response todosResponse = ApiClient.getTodos();

        test.info("Fetching users and todos from API");

        Assert.assertEquals(usersResponse.getStatusCode(), 200, "Users API call failed!");
        Assert.assertEquals(todosResponse.getStatusCode(), 200, "Todos API call failed!");

        JSONArray users = new JSONArray(usersResponse.getBody().asString());
        JSONArray todos = new JSONArray(todosResponse.getBody().asString());

        // Filter users in FanCode city
        List<JSONObject> fancodeUsers = new ArrayList<>();
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            JSONObject geo = user.getJSONObject("address").getJSONObject("geo");
            double lat = Double.parseDouble(geo.getString("lat"));
            double lng = Double.parseDouble(geo.getString("lng"));

            if (FancodeUtils.isFancodeUser(lat, lng)) {
                fancodeUsers.add(user);
            }
        }

        test.info("Filtered users belonging to FanCode city");

        Assert.assertFalse(fancodeUsers.isEmpty(), "No users found in FanCode city!");

        // Check task completion percentage for each user
        for (JSONObject user : fancodeUsers) {
            int userId = user.getInt("id");
            double completionPercentage = FancodeUtils.calculateTaskCompletion(todos, userId);

            test.info(String.format("User: %s, Completion: %.2f%%", user.getString("name"), completionPercentage));

            Assert.assertTrue(completionPercentage > 50,
                    String.format("User %s has only %.2f%% tasks completed.", user.getString("name"), completionPercentage));
        }

        test.pass("All users in FanCode city have completed more than 50% of their tasks.");
    }
}
