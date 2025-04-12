package org.ua.fkrkm.progplatform.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.CourseServiceI;

/**
 * Керування курсами
 */
@Tag(name = "Адмін. панель керування курсами")
@RestController
@RequestMapping("/api/admin/course")
@AllArgsConstructor
public class CourseAdminController {
    
    // Сервіс для роботи з курсами
    private final CourseServiceI courseService;

    /**
     * Створення курсу
     *
     * @param request запит
     * @return Response<CreateCourseResponse> відповідь API
     */
    @Operation(
            summary = "Створити курс"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateCourseResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldValidResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Response<CreateCourseResponse> create(@Valid @RequestBody CreateCourseRequest request) {
        return new Response<>(HttpStatus.CREATED, courseService.create(request));
    }

    /**
     * Оновити курс
     *
     * @param request запит
     * @return Response<UpdateCourseResponse> відповідь API
     */
    @Operation(
            summary = "Оновити курс"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCourseRequest.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/update")
    public Response<UpdateCourseResponse> update(@RequestBody UpdateCourseRequest request) {
        return new Response<>(HttpStatus.OK, courseService.update(request));
    }

    /**
     * Видалити курс
     *
     * @param id ID курсу
     * @return Response<DeleteCourseResponse> відповідь API
     */
    @Operation(
            summary = "Видалити курс"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeleteCourseResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/delete")
    public Response<DeleteCourseResponse> delete(@Parameter(description = "ID курсу") @RequestParam(name = "id") int id) {
        return new Response<>(HttpStatus.OK, courseService.delete(id));
    }

    /**
     * Додати користувача до курсу
     *
     * @param userId ID користувача
     * @param courseId ID курсу
     * @return Response<AddUserToCourseResponse> відповідь API
     */
//    @Operation(
//            summary = "Додати користувача до курсу"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AddUserToCourseResponse.class))}),
//            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//    })
//    @ResponseBody
//    @SecurityRequirement(name = "Bearer Authentication")
//    @PostMapping("/addUserToCourse")
//    public Response<AddUserToCourseResponse> addUserToCourse(@Parameter(description = "ID користувача") @RequestParam(name = "userId") int userId,
//                                                             @Parameter(description = "ID курса") @RequestParam(name = "courseId") int courseId) {
//        return new Response<>(HttpStatus.OK, courseService.addUserToCourse(userId, courseId));
//    }

    /**
     * Видалити користувача з курсу
     *
     * @param userId ID користувача
     * @param courseId ID курсу
     * @return Response<DeleteUserFromCourseResponse> відповідь API
     */
//    @Operation(
//            summary = "Видалити користувача з курсу"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeleteUserFromCourseResponse.class))}),
//            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//    })
//    @ResponseBody
//    @SecurityRequirement(name = "Bearer Authentication")
//    @DeleteMapping("/deleteUserFromCourse")
//    public Response<DeleteUserFromCourseResponse> deleteUserFromCourse(@Parameter(description = "ID користувача") @RequestParam(name = "userId") int userId,
//                                                                       @Parameter(description = "ID курса") @RequestParam(name = "courseId") int courseId) {
//        return new Response<>(HttpStatus.OK, courseService.deleteUserFromCourse(userId, courseId));
//    }
}
