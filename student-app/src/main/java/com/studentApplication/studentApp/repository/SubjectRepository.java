package com.studentApplication.studentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApplication.studentApp.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
