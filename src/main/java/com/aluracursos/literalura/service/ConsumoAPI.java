package com.aluracursos.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {

    public String obtenerDatos(String url) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            return json;
        } catch (IOException e) {
            throw new RuntimeException("Error de red: No se pudo alcanzar la API. " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException("Operación interrumpida: " + e.getMessage());
        }
    }
}
