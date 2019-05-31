package com.example.springboottest.controller;

import com.example.springboottest.model.Student;
import com.example.springboottest.model.StudentDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StudentController {

  private final StudentDao studentDao;

  @PostMapping("/students")
  public Student addStudents(@RequestBody Student student) {
    log.info("Saving student {}", student);
    return studentDao.addStudent(student);
  }

  @GetMapping("/students")
  public List<Student> getStudents() {
    return studentDao.getStudents();
  }

  @GetMapping("/students/{studentId}")
  public Student getStudent(@PathVariable String studentId) {
    log.info("Getting student by id {}", studentId);
    return studentDao.getStudent(studentId);
  }

}
