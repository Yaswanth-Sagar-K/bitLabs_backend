package com.talentstream.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.talentstream.dto.AddInviteeRequest;
import com.talentstream.dto.CreateMeetingRequest;
import com.talentstream.entity.InviteeEntity;
import com.talentstream.entity.MeetingEntity;
import com.talentstream.repository.InviteeRepository;
import com.talentstream.repository.MeetingRepository;


@Service
public class WebexService {

    @Value("${webex.api.url}")
    private String baseUrl;

    @Value("${webex.access.token}")
    private String token;
    
    @Value("${webex.client.id}")
    private String clientId;

    @Value("${webex.client.secret}")
    private String clientSecret;

    @Value("${webex.redirect.uri}")
    private String redirectUri;

    @Autowired
    private WebClient webClient;
    
    @Autowired
    private MeetingRepository meetingRepo;
    
    @Autowired
    private InviteeRepository inviteeRepo;
    
    

    public Map<String, Object> exchangeCodeForToken(String code) {
        return webClient.post()
                .uri("/access_token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("code", code)
                        .with("redirect_uri", redirectUri))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public MeetingEntity createMeeting(CreateMeetingRequest req) {
        Map<String, Object> payload = Map.of(
            "title", req.getTitle(),
            "start", req.getStart(),
            "end", req.getEnd(),
            "enabledAutoRecordMeeting", true
        );

        Map<String, Object> response = webClient.post()
                .uri("/meetings")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        MeetingEntity meeting = new MeetingEntity();
        meeting.setId((String) response.get("id"));
        meeting.setTitle((String) response.get("title"));
        meeting.setStartTime((String) response.get("start"));
        meeting.setEndTime((String) response.get("end"));
        meeting.setWebLink((String) response.get("webLink"));

        return meetingRepo.save(meeting);
    }

    public InviteeEntity addInvitee(AddInviteeRequest req) {
        Map<String, Object> payload = Map.of(
            "meetingId", req.getMeetingId(),
            "email", req.getEmail(),
            "displayName", req.getDisplayName(),
            "coHost", req.isCoHost()
        );

        webClient.post()
                .uri("/meetingInvitees")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        MeetingEntity meeting = meetingRepo.findById(req.getMeetingId()).orElseThrow();

        InviteeEntity invitee = new InviteeEntity();
        invitee.setEmail(req.getEmail());
        invitee.setDisplayName(req.getDisplayName());
        invitee.setCoHost(req.isCoHost());
        invitee.setMeeting(meeting);

        return inviteeRepo.save(invitee);
    }
}

