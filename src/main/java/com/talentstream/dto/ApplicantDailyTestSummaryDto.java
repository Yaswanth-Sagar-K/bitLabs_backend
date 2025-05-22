package com.talentstream.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ApplicantDailyTestSummaryDto {

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate testDate;
	private int score;
	private String performance;
	
	
	public ApplicantDailyTestSummaryDto(LocalDate testDate, int score, String performance) {
		super();
		this.testDate = testDate;
		this.score = score;
		this.performance = performance;
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getPerformance() {
		return performance;
	}
	public void setPerformance(String performance) {
		this.performance = performance;
	}
	
	public LocalDate getTestDate() {
		return testDate;
	}
	public void setTestDate(LocalDate testDate) {
		this.testDate = testDate;
	}
	
	
	
	
}
