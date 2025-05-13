package com.example.deliverable3_android_php_api;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.deliverable3_android_php_api.network.VolleySingleton;
import com.example.deliverable3_android_php_api.network.EndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUserId, editTextName, editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Make sure this matches your XML file name

        // Initialize views
        editTextUserId = findViewById(R.id.editTextUserId);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonCreate = findViewById(R.id.buttonCreate);
        Button buttonFetch = findViewById(R.id.buttonFetch);
        Button buttonUpdate = findViewById(R.id.buttonUpdate);
        Button buttonDelete = findViewById(R.id.buttonDelete);

        // Set click listeners
        buttonCreate.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            createUser(name, email, password);
        });

        buttonFetch.setOnClickListener(v -> fetchUsers());

        buttonUpdate.setOnClickListener(v -> {
            String idStr = editTextUserId.getText().toString();
            if (!idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                updateUser(id, name, email, password);
            } else {
                Toast.makeText(this, "Please enter User ID", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            String idStr = editTextUserId.getText().toString();
            if (!idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                deleteUser(id);
            } else {
                Toast.makeText(this, "Please enter User ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createUser(String name, String email, String password) {
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.CREATE,
                response -> Toast.makeText(this, "User Created: " + response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("password", password);
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue((Request<String>) request);
    }

    public void fetchUsers() {
        StringRequest request = new StringRequest(Request.Method.GET, EndPoints.READ_ALL,
                response -> {
                    try {
                        JSONArray users = new JSONArray(response);
                        for (int i = 0; i < users.length(); i++) {
                            JSONObject obj = users.getJSONObject(i);
                            String name = obj.getString("name");
                            String email = obj.getString("email");
                            // Add more handling if needed
                        }
                        Toast.makeText(this, "Users Fetched Successfully", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show());
        VolleySingleton.getInstance(this).addToRequestQueue((Request<String>) request);
    }

    public void updateUser(int id, String name, String email, String password) {
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.UPDATE,
                response -> Toast.makeText(this, "User Updated: " + response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                map.put("name", name);
                map.put("email", email);
                map.put("password", password);
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue((Request<String>) request);
    }

    public void deleteUser(int id) {
        String deleteUrlWithId = EndPoints.DELETE + "?id=" + String.valueOf(id);
        StringRequest request = new StringRequest(Request.Method.GET, deleteUrlWithId,
                response -> Toast.makeText(this, "User Deleted: " + response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show());
        VolleySingleton.getInstance(this).addToRequestQueue((Request<String>) request);
    }
}