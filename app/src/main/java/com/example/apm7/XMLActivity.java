package com.example.apm7;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class XMLActivity extends Activity {
    private TextView link;
    private ListView superListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected); // XML : activity_connected.xml
        superListView = findViewById(R.id.listeFacture);

        // Vous pouvez ajouter ici tout autre code nécessaire pour initialiser votre vue
        link = findViewById(R.id.textViewLink);
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        link.setText("id : "+id);

        XMLActivity.ApiTask2 apiTask = new XMLActivity.ApiTask2(id);
        apiTask.execute();
        //link.setText("url");
        //link.setMovementMethod(LinkMovementMethod.getInstance());

        superListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = superListView.getAdapter().getItem(position);
                Uri uri = Uri.parse("http://192.168.1.57/facture/"+obj.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public class ApiTask2 extends AsyncTask<Void, Void, Boolean> {

        private String idProprio;
        public ApiTask2(String id) {
            idProprio = id;
        }

        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            String url = "http://192.168.1.57/API-MiseAuVert/facture.php";
            RequestBody requestBody = new FormBody.Builder()
                    .add("idProprio", idProprio)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseBody);
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i=0;i<jsonArray.length();i++){
                        String temp = jsonArray.getJSONObject(i).getString("PDF");
                        if(temp!="null"){
                            list.add(temp);
                        }
                        
                    }
                    superListView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list));

                    return true;
                } else {
                    // Erreur de réseau
                    Log.e("API Error", response.message());
                    return false;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {
            if (result) {

            } else {
                // Si les informations d'identification ne sont pas valides, afficher un message d'erreur à l'utilisateur
                Toast.makeText(XMLActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            }
        }
    }
}