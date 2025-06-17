package com.talentstream.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class InviteeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String displayName;
    private boolean coHost;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    @JsonBackReference
    private MeetingEntity meeting;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public MeetingEntity getMeeting() {
		return meeting;
	}

	public void setMeeting(MeetingEntity meeting) {
		this.meeting = meeting;
	}

    
    
    
}
