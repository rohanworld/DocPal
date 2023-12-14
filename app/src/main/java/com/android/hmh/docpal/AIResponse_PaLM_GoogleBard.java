package com.android.hmh.docpal;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class AIResponse_PaLM_GoogleBard {
    String apiKey = "ADontIzaSyUseBdYThisrFbOVdApihQZLrKeyEuwnk1FjThismoCUWisquNotWorkingHjxQ";
    String apiCallUrlEndPoint = "https://generativelanguage.googleapis.com/v1beta3/models/text-bison-001:generateText?key="+apiKey;

    private void askAI(){
        // Inside the askAI method
        RequestQueue queue;
        String whatToAskAI = "If a patient says, I have Cold and cough and sneezing for a doctor, then what can be the possible diagnosis for the patient, Act as a doctor and tell me some rough idea";


        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectText = new JSONObject();
        try {
            jsonObjectText.put("text", whatToAskAI);
            jsonObject.put("prompt", jsonObjectText);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiCallUrlEndPoint, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String gotResponse = response.getJSONArray("candidates")
                                    .getJSONObject(0)
                                    .getString("output");
//                            AIResponseTextView.setText(gotResponse);
//                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this, "IDK Wats tht error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> apiHeader = new HashMap<>();
                apiHeader.put("Content-Type", "application/json");
                return apiHeader;
            }
        };
    }
}


