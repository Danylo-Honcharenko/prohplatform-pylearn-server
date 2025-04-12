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
     * @param courseId ID курсу
     * @return Response<CourseUsersResponse> відповідь API
     */
//    @Operation(
//            summary = "Отримати користувачів курсу",
//            description = "Можна отримати лише користувачів курсу в яких ви вже перебуваєте"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CourseUsersResponse.class))}),
//            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//    })
//    @ResponseBody
//    @GetMapping("/getCourseUsers")
//    public Response<CourseUsersResponse> getCourseUsers(@Parameter(description = "ID курсу") @RequestParam(name = "courseId") int courseId) {
//        return new Response<>(HttpStatus.OK, courseService.getCourseUsers(courseId));
//    }

    /**
     * Отримати курс по ID
     *
     * @param courseId ID курсу
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
    @GetMapping("/get")
    public Response<CourseResponse> getCourseById(@Parameter(description = "ID курсу") @RequestParam(name = "courseId") int courseId,
                                                  @Parameter(description = "ID користувача") @RequestParam(name = "userId", required = false) Integer userId) {
        return new Response<>(HttpStatus.OK, courseService.getCourseById(courseId, userId));
    }

    /**
     * Отримати курси користувача
     *
     * @param userId ID користувача
     * @return Response<UserCourseResponse> відповідь API
     */
//    @Operation(
//            summary = "Отримати курси користувача"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserCourseResponse.class))}),
//            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//    })
//    @ResponseBody
//    @GetMapping("/getUserCourse")
//    public Response<UserCourseResponse> getUserCourse(@Parameter(description = "ID користувача") @RequestParam(name = "userId") int userId) {
//        return new Response<>(HttpStatus.OK, courseService.getCourseByUserId(userId));
//    }
}
