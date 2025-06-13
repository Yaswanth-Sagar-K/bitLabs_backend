package com.talentstream.dto;


public class AddInviteeRequest {
    private String meetingId;
    private String email;
    private String displayName;
    private boolean coHost;
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public boolean isCoHost() {
		return coHost;
	}
	public void setCoHost(boolean coHost) {
		this.coHost = coHost;
	}
    
    
}
