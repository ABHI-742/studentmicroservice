package com.assginment.swe645.studentSurveyMicroservice.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assginment.swe645.studentSurveyMicroservice.Entities.StudentEntity;
import com.assginment.swe645.studentSurveyMicroservice.Repo.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	public List<StudentEntity> getAllSurveys() {
		return studentRepository.findAll();
	}

	public Optional<StudentEntity> getSurveyById(Long id) {
		return studentRepository.findById(id);
	}

	public StudentEntity saveSurvey(StudentEntity student) {
		return studentRepository.save(student);
	}

	public void deleteSurvey(Long id) {
		studentRepository.deleteById(id);
	}

	// Add more business logic or custom queries if needed
}