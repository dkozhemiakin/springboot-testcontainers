package com.example.springboottest.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentDao {

  private final StudentRepository repository;

  public Student getStudent(String studentId) {
    return repository.findOne(studentId);
  }

  public List<Student> getStudents() {
    return (List<Student>) repository.findAll();
  }

  public Student addStudent(Student student) {
    return repository.save(student);
  }

}
