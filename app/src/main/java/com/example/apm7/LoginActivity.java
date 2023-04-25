package com.example.apm7;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

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
                    // Si les informations d'identification sont valides, appeler une méthode pour vérifier les identifiants sur le serveur
                    if (authenticate(username, password)) {
                        // Si les informations d'identification sont valides, ouvrir une nouvelle activité pour la page d'accueil de l'utilisateur
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Si les informations d'identification ne sont pas valides, afficher un message d'erreur à l'utilisateur
                        Toast.makeText(LoginActivity.this, "Identifiant ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si les entrées de l'utilisateur ne sont pas valides, afficher un message d'erreur à l'utilisateur
                    Toast.makeText(LoginActivity.this, "Entrez un nom d'utilisateur et un mot de passe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean authenticate(String username, String password) {
        // Appeler une méthode pour vérifier les identifiants sur le serveur
        // Retourner vrai si les identifiants sont valides, faux sinon
        return false;
    }
}