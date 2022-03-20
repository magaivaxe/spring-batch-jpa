package com.mpsg.student.batch.database.repository;

import com.mpsg.student.batch.database.entity.StudentDbo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<StudentDbo, UUID> {

  Page<StudentDbo> findByBirthDateBetween(Date start, Date end, Pageable pageable);

}