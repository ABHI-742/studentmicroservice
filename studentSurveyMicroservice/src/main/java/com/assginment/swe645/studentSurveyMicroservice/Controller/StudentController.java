package com.assginment.swe645.studentSurveyMicroservice.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assginment.swe645.studentSurveyMicroservice.Entities.StudentEntity;
import com.assginment.swe645.studentSurveyMicroservice.Service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/survey/test")
//@CrossOrigin(origins={"https://35.168.73.243/home","http://localhost:8082/"})
public class StudentController {

    @Autowired
    private StudentService studentService;

    // GET all surveys
    @GetMapping("/all")
    public ResponseEntity<List<StudentEntity>> getAllSurveys() {
        List<StudentEntity> surveys = studentService.getAllSurveys();
        if (surveys.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content if no surveys found
        }
        return new ResponseEntity<>(surveys, HttpStatus.OK); // 200 OK with survey list
    }

    // GET a specific survey by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentEntity> getSurveyById(@PathVariable Long id) {
        Optional<StudentEntity> survey = studentService.getSurveyById(id);
        return survey.map(s -> new ResponseEntity<>(s, HttpStatus.OK)) // 200 OK if survey is found
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found if survey doesn't exist
    }

    // POST a new survey with validation
    @PostMapping("/create")
    public ResponseEntity<StudentEntity> createSurvey(@Valid @RequestBody StudentEntity student) {
        try {
            StudentEntity savedSurvey = studentService.saveSurvey(student);
            return new ResponseEntity<>(savedSurvey, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error for unexpected issues
        }
    }

    // PUT (update) a survey by ID with validation
    @PutMapping("/update/{id}")
    public ResponseEntity<StudentEntity> updateSurvey(@PathVariable Long id, @Valid @RequestBody StudentEntity updatedSurvey) {
        Optional<StudentEntity> existingSurvey = studentService.getSurveyById(id);
        if (existingSurvey.isPresent()) {
            StudentEntity survey = existingSurvey.get();
            // Update survey fields as necessary
            survey.setFirstName(updatedSurvey.getFirstName());
            survey.setLastName(updatedSurvey.getLastName());
            survey.setStreetAddress(updatedSurvey.getStreetAddress());
            survey.setCity(updatedSurvey.getCity());
            survey.setState(updatedSurvey.getState());
            survey.setZip(updatedSurvey.getZip());
            survey.setPhoneNumber(updatedSurvey.getPhoneNumber());
            survey.setEmail(updatedSurvey.getEmail());
            survey.setDateOfSurvey(updatedSurvey.getDateOfSurvey());
            survey.setLikedMost(updatedSurvey.getLikedMost());
            survey.setInterestSource(updatedSurvey.getInterestSource());
            survey.setRecommendLikelihood(updatedSurvey.getRecommendLikelihood());

            StudentEntity savedSurvey = studentService.saveSurvey(survey);
            return new ResponseEntity<>(savedSurvey, HttpStatus.OK); // 200 OK for successful update
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if the survey doesn't exist
        }
    }

    // DELETE a survey by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        Optional<StudentEntity> existingSurvey = studentService.getSurveyById(id);
        if (existingSurvey.isPresent()) {
            studentService.deleteSurvey(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content for successful deletion
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if the survey doesn't exist
        }
    }

    // Exception handler for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // 400 Bad Request for validation errors
    }
}
