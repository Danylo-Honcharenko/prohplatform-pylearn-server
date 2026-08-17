package org.ua.fkrkm.progplatform.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public Response<GetAllCoursesResponse> getAllCourses() {
        return new Response<>(HttpStatus.OK, courseService.getAllCourses());
    }

    /**
     * Отримати користувачів курсу
     *
     * @param id ID курсу
     * @return Response<CourseUsersResponse> відповідь API
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
    public Response<CourseUsersResponse> getCourseUsers(@Parameter(description = "ID курсу") @PathVariable int id) {
        return new Response<>(HttpStatus.OK, courseService.getCourseUsers(id));
    }

    /**
     * Отримати курс по ID
     *
     * @param id ID курсу
     * @return Response<CourseResponse> відповідь API
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
    public Response<CourseResponse> getCourseById(@Parameter(description = "ID курсу") @PathVariable int id) {
        return new Response<>(HttpStatus.OK, courseService.getCourseById(id));
    }

    /**
     * Отримати курс по ID зі статистикою проходження
     *
     * @param id ID курсу
     * @return Response<CourseResponse> відповідь API
     */
    @Operation(
            summary = "Отримати курс по ID зі статистикою проходження"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @GetMapping("/{id}/user/stat")
    public Response<CourseResponse> getCourseWithPassingStatistics(@Parameter(description = "ID курсу") @PathVariable int id) {
        return new Response<>(HttpStatus.OK, courseService.getCourseWithPassingStatistics(id));
    }

    /**
     * Отримати курси користувача
     *
     * @return Response<UserCourseResponse> відповідь API
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
    public Response<UserCourseResponse> getUserCourses() {
        return new Response<>(HttpStatus.OK, courseService.getUserCourses());
    }
}
