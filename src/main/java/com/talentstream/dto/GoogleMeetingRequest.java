package com.talentstream.dto;

import java.time.LocalDateTime;

public class GoogleMeetingRequest {
    private String summary;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
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

