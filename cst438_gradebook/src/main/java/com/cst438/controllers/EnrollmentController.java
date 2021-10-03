package com.cst438.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;

@RestController
public class EnrollmentController {

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;

	/*
	 * endpoint used by registration service to add an enrollment to an existing
	 * course.
	 */
	@PostMapping("/enrollment")
	@Transactional
	public EnrollmentDTO addEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
		
		//TODO  complete this method in homework 4
		System.out.println("Received http message: "+enrollmentDTO);		
		Enrollment enrollment = new Enrollment();
		enrollment.setStudentEmail(enrollmentDTO.studentEmail);
		enrollment.setStudentName(enrollmentDTO.studentName);
		
		Course c = courseRepository.findByCourse_id(enrollmentDTO.course_id);	
		enrollment.setCourse(c);
		enrollmentRepository.save(enrollment);	
		c.getEnrollments().add(enrollment);
		System.out.println("CourseID from DTO: " + enrollmentDTO.course_id);
		return enrollmentDTO;		
	}

}
