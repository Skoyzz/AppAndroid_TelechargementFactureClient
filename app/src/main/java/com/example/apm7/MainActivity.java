package com.example.apm7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    public final static String EXTRA_MESSAGE = "com.ltm.ltmactionbar.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Vérifier les entrées de l'utilisateur
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    // Si les informations d'identification sont valides, appeler la méthode authenticate pour vérifier les identifiants sur le serveur
                    ApiTask apiTask = new ApiTask(username, password);
                    apiTask.execute();
                } else {
                    // Si les entrées de l'utilisateur ne sont pas valides, afficher un message d'erreur à l'utilisateur
                    Toast.makeText(MainActivity.this, "Entrez un nom d'utilisateur et un mot de passe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class ApiTask extends AsyncTask<Void, Void, Boolean> {
        private String mUsername;
        private String mPassword;
        private String idProprio;
        public ApiTask(String login, String mdp) {
            mUsername = login;
            mPassword = mdp;
        }

        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            String url = "http://192.168.1.57/API-MiseAuVert/user.php";
            RequestBody requestBody = new FormBody.Builder()
                    .add("login", mUsername)
                    .add("mdp", mPassword)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("API Response", responseBody);
                    JSONObject jsonObject = new JSONObject(responseBody);
                    int status = jsonObject.getInt("status");
                    idProprio = jsonObject.getString("proprietaire");
                    if (status == 1) {
                        // Les informations d'identification sont valides
                        return true;
                    } else {
                        // Les informations d'identification ne sont pas valides
                        return false;
                    }
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
                // Si les informations d'identification sont valides, ouvrir une nouvelle activité pour la page d'accueil de l'utilisateur
                Intent intent = new Intent(MainActivity.this, XMLActivity.class); // Le nouveaux XMl s'ouvre avec la class XML Activity
                intent.putExtra("id",idProprio);
                startActivity(intent);
                finish();
            } else {
                // Si les informations d'identification ne sont pas valides, afficher un message d'erreur à l'utilisateur
                Toast.makeText(MainActivity.this, "Identifiant ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }
}