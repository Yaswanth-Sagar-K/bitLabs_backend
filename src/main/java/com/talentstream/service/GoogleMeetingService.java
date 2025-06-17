package com.talentstream.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.client.util.DateTime;

import java.util.UUID;

import com.talentstream.config.GoogleCalendarUtil;
import com.talentstream.entity.Meeting;
import com.talentstream.repository.GoogleMeetingRepository;


@Service
public class GoogleMeetingService {
	
	@Autowired
    private GoogleMeetingRepository meetingRepository;
	
	@Autowired
    private GoogleCalendarUtil googleUtil;

    public Meeting createMeeting(String accessToken, String summary,
                                 LocalDateTime startTime, LocalDateTime endTime) throws Exception {

        Calendar service = googleUtil.getCalendarService(accessToken);

        Event event = new Event().setSummary(summary);

        DateTime start = new DateTime(java.util.Date.from(startTime.atZone(ZoneId.of("Asia/Kolkata")).toInstant()));

        event.setStart(new EventDateTime().setDateTime(start));

        DateTime end = new DateTime(java.util.Date.from(startTime.atZone(ZoneId.of("Asia/Kolkata")).toInstant()));
        event.setEnd(new EventDateTime().setDateTime(end));

        ConferenceData conferenceData = new ConferenceData()
                .setCreateRequest(new CreateConferenceRequest()
                        .setRequestId(UUID.randomUUID().toString()));
        event.setConferenceData(conferenceData);

        Event createdEvent = service.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        Meeting meeting = new Meeting();
        meeting.setCalendarEventId(createdEvent.getId());
        meeting.setMeetLink(createdEvent.getHangoutLink());
        meeting.setSummary(summary);
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);

        return meetingRepository.save(meeting);
    }
}
