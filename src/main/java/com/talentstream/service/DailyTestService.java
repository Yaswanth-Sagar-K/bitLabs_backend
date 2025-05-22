package com.talentstream.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentstream.entity.ApplicantDailyTest;
import com.talentstream.repository.DailyTestRepo;


@Service
public class DailyTestService {

	private DailyTestRepo dailyTestRepo; 
	
	@Autowired
	public DailyTestService(DailyTestRepo dailyTestRepo) {
		this.dailyTestRepo = dailyTestRepo;
	}
	
	
	public boolean addQuestion(ApplicantDailyTest question) {
		
		dailyTestRepo.save(question);
		return true;
	}
	
	public boolean addAllQuestions(List<ApplicantDailyTest> questions) {
		dailyTestRepo.saveAll(questions);
		return true;
	}
	
	
//	public List<ApplicantDailyTest> getRandomQuestions(int count) {
//        List<ApplicantDailyTest> all = dailyTestRepo.findAll();
//        Collections.shuffle(all);
//        return all.stream().limit(count).collect(Collectors.toList());
//    }
	
	public List<ApplicantDailyTest> getRandomSkillQuestions(int count, List<String> skills){
		List<ApplicantDailyTest> all = dailyTestRepo.findByLanguageIn(skills);
		Collections.shuffle(all);
		return all.stream().limit(count).collect(Collectors.toList());
	}
	
	public List<ApplicantDailyTest> getAllQuestions(){
		return dailyTestRepo.findAll();
	}
}
