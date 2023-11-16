package com.test.repository;

import com.test.model.Student;

import java.util.List;

public interface IStudentRepository extends IGenerateRepository<Student>{
    List<Student> findByName(String name);
}
