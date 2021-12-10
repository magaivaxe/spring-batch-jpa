package com.mpsg.student.batch.database.repository;

import com.mpsg.student.batch.database.entity.StudentDbo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<StudentDbo, UUID> {
}