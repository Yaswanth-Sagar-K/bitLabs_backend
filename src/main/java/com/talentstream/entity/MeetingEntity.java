package com.talentstream.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class MeetingEntity {
    @Id
    private String id; 

    private String title;
    private String startTime;
    private String endTime;
    private String webLink;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<InviteeEntity> invitees = new ArrayList<>();

    


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

	public List<InviteeEntity> getInvitees() {
		return invitees;
	}

	public void setInvitees(List<InviteeEntity> invitees) {
		this.invitees = invitees;
	}}
