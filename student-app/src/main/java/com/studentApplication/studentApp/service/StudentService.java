package com.studentApplication.studentApp.service;

import java.util.List;
import java.util.Optional;

import com.studentApplication.studentApp.model.Student;

public interface StudentService {
	
	public Student save(Student student) ;
	
	public Optional<Student> getStudent(long studentId) ;
	
	public List<Student> getAllStudents();

	public List<Student> getFailedStudents();
	
	public List<Student> queryByPassedStudents();

	public Student update(long studentId, Student student);

	public void deleteStudent(Long id);

	public List<Student> getStudentsWithAverage(double average);

}