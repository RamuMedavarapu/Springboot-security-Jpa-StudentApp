package com.studentApplication.studentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.studentApplication.studentApp.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	
	@Query("Select s from Student s where s.result = 'Fail'")
	public List<Student> queryByFailedStudents();
	
	@Query("Select s from Student s where s.result = 'Pass'")
	public List<Student> queryByPassedStudents();

}
