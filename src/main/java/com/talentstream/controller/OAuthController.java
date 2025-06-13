package com.talentstream.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Value("${webex.oauth.clientId}")
    private String clientId;

    @Value("${webex.oauth.clientSecret}")
    private String clientSecret;

    @Value("${webex.oauth.redirectUri}")
    private String redirectUri;

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String scopes = "spark:all spark:meetings_write spark:meetings_read";
        String authUrl = String.format("https://webexapis.com/v1/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=%s&state=xyz",
                clientId, URLEncoder.encode(redirectUri, StandardCharsets.UTF_8), URLEncoder.encode(scopes, StandardCharsets.UTF_8));
        response.sendRedirect(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam String code) {
        WebClient client = WebClient.create();
        return client.post()
                .uri("https://webexapis.com/v1/access_token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("code", code)
                        .with("redirect_uri", redirectUri))
                .retrieve()
                .bodyToMono(String.class)
                .map(token -> ResponseEntity.ok("Access Token Response:\n" + token))
                .block();
    }
}
