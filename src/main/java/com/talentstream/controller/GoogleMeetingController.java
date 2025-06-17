package com.talentstream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.dto.GoogleMeetingRequest;
import com.talentstream.entity.Meeting;
import com.talentstream.service.GoogleMeetingService;

@RestController
@RequestMapping("/api/meetings")
public class GoogleMeetingController {
	
	@Autowired
    private GoogleMeetingService meetingService;

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody GoogleMeetingRequest request) throws Exception {
        String token = bearerToken.replace("Bearer ", "");
        Meeting meeting = meetingService.createMeeting(token, request.getSummary(),
                request.getStartTime(), request.getEndTime());
        return ResponseEntity.ok(meeting);
    }
}
