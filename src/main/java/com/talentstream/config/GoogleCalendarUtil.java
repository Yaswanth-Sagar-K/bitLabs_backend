package com.talentstream.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

@Component
public class GoogleCalendarUtil {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public Calendar getCalendarService(String accessToken) throws IOException, GeneralSecurityException {
        // Wrap the access token
        GoogleCredentials credentials = GoogleCredentials.create(
            new AccessToken(accessToken, new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour expiry
        );

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("GoogleMeetApp").build();
    }
}
