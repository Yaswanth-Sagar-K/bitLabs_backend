package com.talentstream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.dto.AddInviteeRequest;
import com.talentstream.dto.CreateMeetingRequest;
import com.talentstream.entity.InviteeEntity;
import com.talentstream.entity.MeetingEntity;
import com.talentstream.service.WebexService;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

	@Autowired
    private WebexService webexService;

    @PostMapping("/create")
    public ResponseEntity<MeetingEntity> createMeeting(@RequestBody CreateMeetingRequest request) {
        MeetingEntity meeting = webexService.createMeeting(request);
        return ResponseEntity.ok(meeting);
    }

    @PostMapping("/add-invitee")
    public ResponseEntity<InviteeEntity> addInvitee(@RequestBody AddInviteeRequest request) {
        InviteeEntity invitee = webexService.addInvitee(request);
        return ResponseEntity.ok(invitee);
    }
    
    @PostMapping("/token")
    public ResponseEntity<?> exchangeCode(@RequestParam("code") String code) {
        try {
            return ResponseEntity.ok(webexService.exchangeCodeForToken(code));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Token exchange failed: " + e.getMessage());
        }
    }
}

