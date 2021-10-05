package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 */

@SpringBootTest
public class EndToEndTestAddAssignmemt {
	
	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/ejrollo/WebDriver/chromedriver";

	public static final String URL = "https://cst438-gradebook-front-rollo.herokuapp.com/";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // 1 second.

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentGradeRepository assignnmentGradeRepository;

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Test
	public void addAssignmentTest() throws Exception {

//		Database setup:  create course		
		Course c = new Course();
		c.setCourse_id(99999);
		c.setInstructor(TEST_INSTRUCTOR_EMAIL);
		c.setSemester("Fall");
		c.setYear(2021);
		c.setTitle("Test Course");

//	    add an assignment that needs grading for course 99999
		Assignment a = new Assignment();
		a.setCourse(c);
		// set assignment due date to 24 hours ago
		a.setDueDate(new java.sql.Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
		a.setName("TEST ASSIGNMENT");
		a.setNeedsGrading(1);

//	    add a student TEST into course 99999
		Enrollment e = new Enrollment();
		e.setCourse(c);
		e.setStudentEmail(TEST_USER_EMAIL);
		e.setStudentName("Test");

		courseRepository.save(c);
		a = assignmentRepository.save(a);
		e = enrollmentRepository.save(e);

		Assignment b = null;

		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);

		try {			
			// Locate and click Add Assignment button
			WebElement we = driver.findElement(By.xpath("//button[span='Add']"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			// enter data into input fields
			driver.findElement(By.xpath("//input[@name='assign']")).
				sendKeys("Assignment100");
			driver.findElement(By.xpath("//input[@name='id']")).
				sendKeys("99999");
			driver.findElement(By.xpath("//input[@name='date']")).
				sendKeys("2022-01-01");
			
			// Locate submit button and click
			driver.findElement(By.xpath("//button['Submit']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Locate Assignments button and click
			driver.findElement(By.xpath("//button['Assignments']")).click();
			Thread.sleep(SLEEP_DURATION);

			// verify that score show up
			//we = driver.findElement(By.xpath("//div[@data-field='name' and @data-value='Test']"));
			//we =  we.findElement(By.xpath("following-sibling::div[@data-field='grade']"));
			//assertEquals("99.9", we.getAttribute("data-value"));

			// verify that assignment has been added to repo with name Assignment100
			b = assignmentRepository.findById(a.getId());
			assertEquals("Assignment100", b.getName());

		} catch (Exception ex) {
			throw ex;
		} finally {

			// clean up database.
			//ag = assignnmentGradeRepository.findByAssignmentIdAndStudentEmail(a.getId(), TEST_USER_EMAIL);
			//if (ag!=null) assignnmentGradeRepository.delete(ag);
			enrollmentRepository.delete(e);
			assignmentRepository.delete(a);
			courseRepository.delete(c);

			driver.quit();
		}

	}

}












