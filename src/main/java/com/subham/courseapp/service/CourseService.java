package com.subham.courseapp.service;

import com.subham.courseapp.entity.Course;
import com.subham.courseapp.repo.ICourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements ICourseService{

    private ICourseRepo repo;

    @Autowired
    public void setRepo(ICourseRepo repo){
        this.repo = repo;
    }

    @Override
    public List<Course> getAllCourses(){
        return (List<Course>) repo.findAll();
    }

    @Override
    public Course getCourseById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void registerCourse(Course course){
        repo.save(course);
    }

    @Override
    public void deleteCourse(Integer id){
        repo.deleteById(id);
    }
}
