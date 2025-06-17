package com.talentstream.service;

import java.util.List;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentstream.config.GoogleCalendarUtil;
import com.talentstream.entity.Invitee;
import com.talentstream.entity.Meeting;
import com.talentstream.repository.GoogleInviteeRepository;
import com.talentstream.repository.GoogleMeetingRepository;

@Service
public class GoogleInviteeService {
	
	@Autowired
    private GoogleInviteeRepository inviteeRepository;
	
	@Autowired
    private GoogleMeetingRepository meetingRepository;
	
	@Autowired
    private GoogleCalendarUtil googleUtil;

    public void addInvitees(String accessToken, Long meetingId, List<String> emails) throws Exception {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        Calendar service = googleUtil.getCalendarService(accessToken);

        Event event = service.events().get("primary", meeting.getCalendarEventId()).execute();

        List<EventAttendee> attendees = new ArrayList<>();
        for (String email : emails) {
            attendees.add(new EventAttendee().setEmail(email));
            inviteeRepository.save(new Invitee(meetingId, email));
        }

        event.setAttendees(attendees);
        service.events().update("primary", event.getId(), event).execute();
    }
}
