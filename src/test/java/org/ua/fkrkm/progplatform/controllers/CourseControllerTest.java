package org.ua.fkrkm.progplatform.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ua.fkrkm.progplatform.services.impl.CourseServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class CourseControllerTest {

    private CourseController courseController;

    @BeforeEach
    public void setUp() {
        this.courseController = new CourseController(mock(CourseServiceImpl.class));
    }

    @Test
    public void getAllCoursesTest() {
        assertNotNull(courseController.getAllCourses());
    }

    @Test
    public void getCourseByIdTest() {
        assertNotNull(courseController.getCourseById(1, 1));
    }
}
