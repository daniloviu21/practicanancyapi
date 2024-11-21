package com.example.apigame;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class ApiClient {
    private static final String BASE_URL = "https://api-adivinanza.onrender.com";
    private static ApiClient instance;
    private final OkHttpClient client;
    private String token; // Variable para almacenar el token de autorización

    // Constructor privado para Singleton
    private ApiClient() {
        client = new OkHttpClient.Builder().build();
    }

    // Método para obtener la instancia de ApiClient (Singleton)
    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    // Método para establecer el token de autorización
    public void setToken(String token) {
        this.token = token;
    }

    // Método para hacer una solicitud GET con el token de autorización
    public void get(String endpoint, ApiCallback callback) {
        String url = BASE_URL + endpoint;

        // Construir la solicitud con el encabezado de autorización si el token no es nulo
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get();

        if (token != null && !token.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure("Error: " + response.code());
                }
            }
        });
    }

    // Interfaz de Callback para manejar respuestas
    public interface ApiCallback {
        void onSuccess(String result);
        void onFailure(String error);
    }
}