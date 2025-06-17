package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.Invitee;

public interface GoogleInviteeRepository extends JpaRepository<Invitee, Long>  {

}
