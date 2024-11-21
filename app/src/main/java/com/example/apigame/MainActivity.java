package com.example.apigame;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView responseTextView;
    private Button requestButton;
    private String authToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        responseTextView = findViewById(R.id.responseTextView);
        requestButton = findViewById(R.id.apiRequestButton);

        ApiClient.getInstance().setToken(authToken);

        requestButton.setOnClickListener(v -> makeApiRequest());
    }

    private void makeApiRequest(){
        //Hacer una solicitud de tipo Get utilizando ApiClient
        ApiClient.getInstance().get("/leaderboard", new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String result) {
                runOnUiThread(() -> responseTextView.setText(result));
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> responseTextView.setText("Error: " + error));
            }
        });
    }
}