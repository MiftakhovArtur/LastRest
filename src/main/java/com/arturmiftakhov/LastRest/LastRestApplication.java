package com.arturmiftakhov.LastRest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@SpringBootApplication
public class LastRestApplication {

    private static final String BASE_URL = "http://94.198.50.185:7081/api/users";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static String sessionId = null;

    public static void main(String[] args) {
        SpringApplication.run(LastRestApplication.class, args);

        StringBuilder finalCode = new StringBuilder();

        System.out.println("=== ШАГ 1: GET ===");
        ResponseEntity<String> getResponse = restTemplate.exchange(
                BASE_URL, HttpMethod.GET, null, String.class
        );

        List<String> cookies = getResponse.getHeaders().get("set-cookie");
        if (cookies != null && !cookies.isEmpty()) {
            sessionId = cookies.get(0).split(";")[0];
            System.out.println("Session: " + sessionId);
        }
        System.out.println("Статус: " + getResponse.getStatusCode());

        System.out.println("\n=== ШАГ 2: POST ===");
        User newUser = new User(3L, "James", "Brown", (byte) 25);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", sessionId);

        ResponseEntity<String> postResponse = restTemplate.exchange(
                BASE_URL, HttpMethod.POST,
                new HttpEntity<>(newUser, headers), String.class
        );
        System.out.println("Статус: " + postResponse.getStatusCode());
        System.out.println("Код часть 1: " + postResponse.getBody());
        finalCode.append(postResponse.getBody());

        System.out.println("\n=== ШАГ 3: PUT ===");
        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 25);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", sessionId);

        ResponseEntity<String> putResponse = restTemplate.exchange(
                BASE_URL, HttpMethod.PUT,
                new HttpEntity<>(updatedUser, headers), String.class
        );
        System.out.println("Статус: " + putResponse.getStatusCode());
        System.out.println("Код часть 2: " + putResponse.getBody());
        finalCode.append(putResponse.getBody());

        System.out.println("\n=== ШАГ 4: DELETE ===");
        headers = new HttpHeaders();
        headers.add("Cookie", sessionId);

        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                BASE_URL + "/3", HttpMethod.DELETE,
                new HttpEntity<>(headers), String.class
        );
        System.out.println("Статус: " + deleteResponse.getStatusCode());
        System.out.println("Код часть 3: " + deleteResponse.getBody());
        finalCode.append(deleteResponse.getBody());

        // ИТОГ
        System.out.println("\n==========================================");
        System.out.println("ИТОГОВЫЙ КОД: " + finalCode);
        System.out.println("Длина: " + finalCode.length() + " символов");
        System.out.println("==========================================");
    }
}
