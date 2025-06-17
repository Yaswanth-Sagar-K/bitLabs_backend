package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.Meeting;

public interface GoogleMeetingRepository extends JpaRepository<Meeting, Long> {

}
