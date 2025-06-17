package com.talentstream.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.service.GoogleInviteeService;

@RestController
@RequestMapping("/api/invitees")
public class GoogleInviteeController {
	
	@Autowired
    private GoogleInviteeService inviteeService;

    @PostMapping("/{meetingId}")
    public ResponseEntity<String> addInvitees(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable Long meetingId,
            @RequestBody List<String> emails) throws Exception {
        String token = bearerToken.replace("Bearer ", "");
        inviteeService.addInvitees(token, meetingId, emails);
        return ResponseEntity.ok("Invitees added");
    }
}
