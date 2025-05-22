package com.talentstream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.ApplicantDailyTest;


public interface DailyTestRepo extends JpaRepository<ApplicantDailyTest, Long> {
	List<ApplicantDailyTest> findByLanguageIn(List<String> language);
	
}
