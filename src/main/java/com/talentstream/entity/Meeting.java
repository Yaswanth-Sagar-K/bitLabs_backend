package com.talentstream.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String calendarEventId;
    private String meetLink;
    private String summary;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCalendarEventId() {
		return calendarEventId;
	}
	public void setCalendarEventId(String calendarEventId) {
		this.calendarEventId = calendarEventId;
	}
	public String getMeetLink() {
		return meetLink;
	}
	public void setMeetLink(String meetLink) {
		this.meetLink = meetLink;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
    
    
}