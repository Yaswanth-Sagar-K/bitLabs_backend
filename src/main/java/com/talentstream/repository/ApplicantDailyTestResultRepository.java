package com.talentstream.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.ApplicantDailyTestResult;

public interface ApplicantDailyTestResultRepository extends JpaRepository<ApplicantDailyTestResult, Long> {

	List<ApplicantDailyTestResult> findByApplicantId(Long applicantId);
	Optional<ApplicantDailyTestResult> findByApplicantIdAndTestDate(Long applicantId, LocalDate testDate);
	List<ApplicantDailyTestResult> findByApplicantIdOrderByTestDateDesc(Long applicantId);

}
