package com.test.service;

import com.test.model.Student;

import java.util.List;

public interface IStudentService extends IGenerateService<Student> {
    List<Student> findByName(String name);
}
