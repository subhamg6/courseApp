package com.subham.courseapp.controller;

import org.springframework.ui.Model;
import com.subham.courseapp.entity.Course;
import com.subham.courseapp.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CourseController {
    private ICourseService service;

    @Autowired
    public void setService(ICourseService service){
        this.service = service;
    }

    @GetMapping("/courselist")
    public String getAllCourses(Model model){
        List<Course> courses = service.getAllCourses();
        model.addAttribute("Courses", courses);
        courses.forEach(c -> System.out.println(c));
        return "courselist";
    }

    @GetMapping("/showCourseForm")
    public String showForm(@ModelAttribute("course") Course course){
        return "courseform";
    }

//    @GetMapping("/updateCourseForm")
//    public String updateForm(@ModelAttribute("course") Course course){
//        return "courseform";
//    }

    @GetMapping("/updateCourseForm")
    public String updateForm(@RequestParam Integer id, Model model) {
        Course course = service.getCourseById(id);
        model.addAttribute("course", course);
        return "courseform";
    }

    @PostMapping("/registerCourse")
    public String registerCourse(@ModelAttribute("course") Course course){
        service.registerCourse(course);
        return "redirect:/courselist";
    }

    @GetMapping("/deleteCourse")
    public String delete(@RequestParam Integer id){
        service.deleteCourse(id);
        return "redirect:/courselist";
    }
}
