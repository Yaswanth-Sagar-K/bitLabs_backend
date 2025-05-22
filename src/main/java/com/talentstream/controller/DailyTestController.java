package com.talentstream.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.entity.ApplicantDailyTest;
import com.talentstream.service.DailyTestService;


@RestController
@RequestMapping("/DailyTest")
@CrossOrigin(origins = "*")
public class DailyTestController {
	@Autowired
	private DailyTestService dailyTestService;
	
//	@Autowired
//	public DailyTestController(DailyTestService dailyTestService) {
//		this.dailyTestService = dailyTestService;
//	}

	@PostMapping("/addQuestion")
	public ResponseEntity<String> addQuestion(@RequestBody ApplicantDailyTest question) {

		dailyTestService.addQuestion(question);
		return ResponseEntity.status(HttpStatus.CREATED).body("question added successfully.");

	}
	
	@PostMapping("/addAllQuestions")
	public ResponseEntity<String> addAllQuestions(@RequestBody List<ApplicantDailyTest> questions){
		dailyTestService.addAllQuestions(questions);
		return ResponseEntity.status(HttpStatus.CREATED).body("all questions added successfully.");

	}

//	@GetMapping("/dailyQuestion")
//	public ResponseEntity<List<ApplicantDailyTest>> getDailyQuestions() {
//	    List<ApplicantDailyTest> questions = dailyTestService.getRandomQuestions(5);
//	    return ResponseEntity.ok(questions);
//	}
	
	@PostMapping("/getSkillBasedQuestions")
	public ResponseEntity<List<ApplicantDailyTest>> getQuestionsBySkills(@RequestBody List<String> skills){
		List<ApplicantDailyTest> questions = dailyTestService.getRandomSkillQuestions(10, skills);
		return ResponseEntity.ok(questions);
	}
	
	@GetMapping("/allQuestions")
	public ResponseEntity<List<ApplicantDailyTest>> getAllQuestions(){
		 List<ApplicantDailyTest> questions = dailyTestService.getAllQuestions();
		    return ResponseEntity.ok(questions);
	}
	
}
