package com.studentApplication.studentApp.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApplication.studentApp.model.Student;
import com.studentApplication.studentApp.model.Subject;
import com.studentApplication.studentApp.repository.StudentRepository;
import com.studentApplication.studentApp.repository.SubjectRepository;
import com.studentApplication.studentApp.utils.CustomUtils;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private SubjectRepository subjectRepo;
	
	@Transactional
	public Student save(Student student) {
		List<Subject> subjs = student.getSubjects();

		boolean status = true;

		for(Subject obj : subjs) {
			double marks = obj.getMarks();

			if(marks < 35) 
				status = false;

			obj.setStatus(marks > 35 ? "Pass" : "Fail");
		}

		student.setResult(status ? "Pass" : "Fail");

		Student savedObj = studentRepo.save(student);

		student.getSubjects().forEach(s -> subjectRepo.save(s));

		return savedObj;
	}
	
	@Transactional(readOnly = true)
	public Optional<Student> getStudent(long studentId) {
		return studentRepo.findById(studentId);
	}
	
	public List<Student> getAllStudents() {
		return studentRepo.findAll();
	}
	
	@Override
	public List<Student> queryByPassedStudents(){
		return studentRepo.queryByPassedStudents();
	}
	
	@Override
	@Transactional
	public Student update(long studentId, Student student) {
		Student updateObj = student;
		student.setStudentId(studentId);
		CustomUtils.copyNonNullProperties(student, updateObj);
		return studentRepo.save(updateObj);
	}
	
	@Override
	public List<Student> getFailedStudents() {
		return studentRepo.queryByFailedStudents();
	}

	@Override
	@Transactional
	public void deleteStudent(Long id) {
		 studentRepo.deleteById(id);
	}

	@Override
	public List<Student> getStudentsWithAverage(double requiredAverage) {
		List<Student> students = studentRepo.findAll();

		List<Student> filteredList = students.stream().filter(stud -> {
			DoubleAdder total = new DoubleAdder();
			List<Subject> subs = stud.getSubjects();

			subs.stream().forEach(s -> {
				total.add(s.getMarks());
			});

			double avg = total.doubleValue()/subs.size();

			if(avg > requiredAverage)
				return true;

			return false;
		}).collect(Collectors.toList());;
		return filteredList;
	}
	
	

}
