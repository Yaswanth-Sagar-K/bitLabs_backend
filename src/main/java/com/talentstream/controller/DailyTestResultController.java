package com.talentstream.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.dto.ApplicantDailyTestResultDto;
import com.talentstream.dto.ApplicantDailyTestSubmitDto;
import com.talentstream.dto.ApplicantDailyTestSummaryDto;
import com.talentstream.service.ApplicantDailyTestResultService;



@RestController
@RequestMapping("/dailyTest/result")
public class DailyTestResultController {

	@Autowired
	private ApplicantDailyTestResultService applicantDailyTestResultService;
	
	
	@PostMapping("/submit")
    public ResponseEntity<String> submitTest(@RequestBody ApplicantDailyTestSubmitDto dto) {
		try {
			applicantDailyTestResultService.submitDailyTest(dto);
	        return ResponseEntity.ok("Test submitted successfully");
			
		}catch (Exception e) {
			System.out.println("Controller Errror"+e.getMessage());
			return new ResponseEntity<String>("Failed",HttpStatus.BAD_REQUEST);
				
		}
    }
	
	
	@GetMapping("/summary/{applicantId}")
    public ResponseEntity<List<ApplicantDailyTestSummaryDto>> getTestSummaries(@PathVariable Long applicantId) {
        return ResponseEntity.ok(applicantDailyTestResultService.getTestSummaries(applicantId));
    }
	
	@GetMapping("/testResult/{applicantId}")
    public ResponseEntity<List<ApplicantDailyTestResultDto>> getTestDetails(
            @PathVariable Long applicantId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(applicantDailyTestResultService.getTestDetails(applicantId, date));
    }
	
	@DeleteMapping("/delete-result/{applicantId}/{testId}")
	public ResponseEntity<String> deleteResult(@PathVariable Long applicantId, @PathVariable Long testId){
		
		applicantDailyTestResultService.deleteDailyTestResult(applicantId, testId);
		
		return ResponseEntity.ok("deleted");
	}

}
