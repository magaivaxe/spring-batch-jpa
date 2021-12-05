package com.mpsg.student.batch.repository;

import com.mpsg.student.batch.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
}