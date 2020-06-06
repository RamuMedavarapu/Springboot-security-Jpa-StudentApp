package com.studentApplication.studentApp.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.studentApplication.studentApp.model.Student;
import com.studentApplication.studentApp.service.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	
	public static final Logger log = LoggerFactory.getLogger(StudentController.class);
	
	@Autowired
	private StudentService studentService; 
	
	@PostMapping("/createStudent")
	public ResponseEntity<?> createStudent(@RequestBody Student student) {
		try {
			System.out.println("request received to created student");
			if(student != null && student.getSname() != null) {
				Student studentEntity = studentService.save(student);
				return new ResponseEntity<Student>(studentEntity, HttpStatus.CREATED);
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
			}
		} catch (Exception e) {
			log.error("Error found during Student creation", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to process request");
		}
	}
	
	@GetMapping("/getStudent/{studentId}")
	public ResponseEntity<?> getStudent(@PathVariable(required = true) long studentId) {
		Optional<Student> obj = studentService.getStudent(studentId);
		if(obj.isPresent()) {
			return new ResponseEntity<Student>(obj.get(), HttpStatus.FOUND);
		}else {
			//return ResponseEntity.notFound().build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not Found with geven Id");
		}
	}
	
	@GetMapping("/getAllStudents")
	public ResponseEntity<?> getAllStudents() {
		List<Student> students = studentService.getAllStudents();
		return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
	}
	
	@GetMapping("/getFailedStudents")
	public ResponseEntity<?> getFailedStudents() {
		List<Student> failedStudentsList = studentService.getFailedStudents();
		return new ResponseEntity<List<Student>>(failedStudentsList, HttpStatus.OK);
	}
	
	@GetMapping("/getPassedStudents")
	public ResponseEntity<?> getPassedStudents() {
		List<Student> failedStudentsList = studentService.queryByPassedStudents();
		return new ResponseEntity<List<Student>>(failedStudentsList, HttpStatus.OK);
	}
	
	@PutMapping("/updateStudent/{studentId}")
	public ResponseEntity<?> updateStudent(@PathVariable("studentId") long id, @RequestBody Student student) {
		Optional<Student> obj = studentService.getStudent(id);
		if(obj.isPresent()) {
			Student studentEntity = studentService.update(id, student);
			return new ResponseEntity<Student>(studentEntity, HttpStatus.OK);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not Found with geven Id");
		}
	}
	
	@DeleteMapping("/removeStudent/{studentId}")
	public ResponseEntity<?> removestudent(@PathVariable (value = "studentId", required = true) Long id) {
		try {
			studentService.deleteStudent(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Student removed");
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not Found with geven Id");
		}
	}
	
	@GetMapping("/getStudents")
	public ResponseEntity<?> getStudentsWithAverage(@RequestParam (name = "average") double average) {
		try {
			List<Student> studentsList = studentService.getStudentsWithAverage(average);
			return new ResponseEntity<List<Student>>(studentsList, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error found while fetching students with Average", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to process request");
		}
	}

}
