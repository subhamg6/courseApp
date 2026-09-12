package com.subham.courseapp.repo;

import com.subham.courseapp.entity.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICourseRepo extends CrudRepository<Course, Integer> {

}
