package org.ua.fkrkm.progplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;
import org.ua.fkrkm.proglatformdao.dao.*;
import org.ua.fkrkm.proglatformdao.dao.impl.*;
import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatform.converters.CourseToCourseResponse;
import org.ua.fkrkm.progplatform.converters.CourseToCreateCourseResponse;
import org.ua.fkrkm.progplatform.converters.CreateCourseRequestToCourse;
import org.ua.fkrkm.progplatform.services.AuthUserServiceI;
import org.ua.fkrkm.progplatform.services.CourseServiceI;
import org.ua.fkrkm.progplatform.services.impl.AuthUserServiceImpl;
import org.ua.fkrkm.progplatform.services.impl.CourseServiceImpl;
import org.ua.fkrkm.progplatformclientlib.request.CreateCourseRequest;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;
import org.ua.fkrkm.progplatformclientlib.response.CreateCourseResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CourseServiceTest {

    private CourseServiceI courseService;
    private final CourseDaoI courseDao = mock(CourseDaoImpl.class);
    private final UserDaoI userDao = mock(UserDaoImpl.class);
    private final Converter<CreateCourseRequest, Course> createCourseRequestCourseConverter = new CreateCourseRequestToCourse();
    private final Converter<Course, CreateCourseResponse> createCourseResponseCourseConverter = new CourseToCreateCourseResponse();
    private final AuthUserServiceI authUserService = mock(AuthUserServiceImpl.class);
    private final Converter<Course, CourseResponse> courseResponseCourseConverter = mock(CourseToCourseResponse.class);
    private final ModuleDaoI moduleDao = mock(ModuleDaoImpl.class);
    private final TopicDaoI topicDao = mock(TopicDaoImpl.class);
    private final ModuleStatDaoI moduleStatDao = mock(ModuleStatImpl.class);
    private final TestDaoI testDao = mock(TestDaoImpl.class);

    @BeforeEach
    public void setUp() {
        this.courseService = new CourseServiceImpl(
                courseDao,
                userDao,
                createCourseRequestCourseConverter,
                createCourseResponseCourseConverter,
                authUserService,
                courseResponseCourseConverter,
                moduleDao,
                topicDao,
                moduleStatDao
        );
    }

    @Test
    public void createTest() {
        when(this.authUserService.getCurrentAuthUser())
                .thenReturn(User.builder().id(1).build());

        when(this.courseDao.create(any()))
                .thenReturn(1);

        assertNotNull(this.courseService.create(CreateCourseRequest.builder().name("name").description("desc").build()));
    }

    @Test
    public void updateTest() {

    }
}
