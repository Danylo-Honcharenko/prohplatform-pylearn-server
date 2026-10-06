package org.ua.fkrkm.progplatform.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.CourseServiceI;

/**
 * Класс для взаємодій з курсами
 */
@Tag(name = "Курс")
@RestController
@RequestMapping("/api/course")
@AllArgsConstructor
public class CourseController {

    // Сервіс для роботи з курсами
    private final CourseServiceI courseService;

    @Operation(
            summary = "Отримати всі курси"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllCoursesResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @GetMapping("/getAll")
    public GetAllCoursesResponse getAllCourses() {
        return this.courseService.getAllCourses();
    }

    /**
     * Отримати користувачів курсу
     *
     * @param id ID курсу
     * @return CourseUsersResponse відповідь API
     */
    @Operation(
            summary = "Отримати користувачів курсу",
            description = "Можна отримати лише користувачів курсу в яких ви вже перебуваєте"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CourseUsersResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @GetMapping("/{id}/getUsers")
    public CourseUsersResponse getCourseUsers(@Parameter(description = "ID курсу") @PathVariable Long id) {
        return courseService.getCourseUsers(id);
    }

    /**
     * Отримати курс по ID
     *
     * @param id ID курсу
     * @return CourseResponse відповідь API
     */
    @Operation(
            summary = "Отримати курс по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @GetMapping("/{id}")
    public CourseResponse getCourseById(@Parameter(description = "ID курсу") @PathVariable Long id) {
        return this.courseService.getCourseById(id);
    }

    /**
     * Отримати курси користувача
     *
     * @return UserCourseResponse відповідь API
     */
    @Operation(
            summary = "Отримати курси користувача"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserCourseResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @GetMapping("/getUserCourse")
    public UserCourseResponse getUserCourses() {
        return this.courseService.getUserCourses();
    }
}
