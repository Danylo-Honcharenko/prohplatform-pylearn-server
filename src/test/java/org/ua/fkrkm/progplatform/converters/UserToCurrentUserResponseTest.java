package org.ua.fkrkm.progplatform.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.dao.TestResultDaoI;
import org.ua.fkrkm.proglatformdao.dao.impl.RoleDaoImpl;
import org.ua.fkrkm.proglatformdao.dao.impl.TestResultDaoImpl;
import org.ua.fkrkm.proglatformdao.entity.Role;
import org.ua.fkrkm.proglatformdao.entity.TestResult;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatformclientlib.response.CurrentUserResponse;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserToCurrentUserResponseTest {

    private Converter<User, CurrentUserResponse> currentUserResponseConverter;
    private final RoleDaoI roleDao = mock(RoleDaoImpl.class);
    private final TestResultDaoI resultDao = mock(TestResultDaoImpl.class);

    @BeforeEach
    public void setUp() {
        this.currentUserResponseConverter = new UserToCurrentUserResponse(roleDao, resultDao);
    }

    @Test
    public void convertTest() {
        User user = User.builder()
                .id(1)
                .first_name("John")
                .last_name("Smith")
                .email("test.test@gmail.com")
                .roleId(1)
                .password("123456789")
                .created(new Date())
                .build();


        TestResult testResult = TestResult.builder()
                .id(1)
                .testUuid("dfdsf3324234dasd")
                .userId(2)
                .maxAssessment(100)
                .assessment(15)
                .correct("correct")
                .incorrect("incorrect")
                .created(new Date())
                .build();

        when(resultDao.getTestResultsByUserId(anyInt()))
                .thenReturn(List.of(testResult));

        when(roleDao.getById(anyInt()))
                .thenReturn(Role.builder().name("ROLE_USER").build());

        CurrentUserResponse userResponse = currentUserResponseConverter.convert(user);
        assertEquals("Програміст-любитель", userResponse.getLevelAlias());
    }
}
