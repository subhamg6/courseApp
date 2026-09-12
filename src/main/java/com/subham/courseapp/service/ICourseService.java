package com.subham.courseapp.service;

import com.subham.courseapp.entity.Course;

import java.util.List;

public interface ICourseService {

    List<Course> getAllCourses();

    Course getCourseById(Integer Id);

    void registerCourse(Course course);

    void deleteCourse(Integer id);
}