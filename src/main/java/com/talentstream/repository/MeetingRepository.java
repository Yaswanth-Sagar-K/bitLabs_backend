package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.MeetingEntity;

public interface MeetingRepository extends JpaRepository<MeetingEntity, String> {
	
	
}
