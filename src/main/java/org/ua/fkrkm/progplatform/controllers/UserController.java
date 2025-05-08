package org.ua.fkrkm.progplatform.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ua.fkrkm.progplatform.services.UserServiceI;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;

/**
 * Контролер для користувачів
 */
@Tag(name = "Користувач")
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    // Сервіс для роботи з користувачами
    private final UserServiceI userService;

    /**
     * Створення користувача
     *
     * @param request запит
     * @return Response<CreateUserResponse> відповідь API
     */
    @Operation(
            summary = "Зареєструватися"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldValidResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public Response<CreateUserResponse> registration(@Valid @RequestBody UserRegistrationRequest request) {
        return new Response<>(HttpStatus.CREATED, userService.registration(request));
    }

    /**
     * Авторизація
     *
     * @param request запит
     * @param response відповідь
     * @return Response<LoginUserResponse> відповідь API
     */
    @Operation(
            summary = "Авторизуватися"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginUserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldValidResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @PostMapping("/login")
    public Response<LoginUserResponse> login(@Valid @RequestBody UserLoginRequest request,
                                             HttpServletResponse response) {
        return new Response<>(HttpStatus.OK, userService.login(request, response));
    }

    /**
     * Вийти із системи
     *
     * @param request запит
     * @return Response<LogoutResponse> відповідь API
     */
    @Operation(
            summary = "Вийти із системе"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LogoutResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @PostMapping("/logout")
    public Response<LogoutResponse> logout(HttpServletRequest request) {
        return new Response<>(HttpStatus.OK, userService.logout(request));
    }

    /**
     * Оновити дані користувача
     *
     * @param request запит
     * @return Response<UpdateUserResponse> відповідь API
     */
    @Operation(
            summary = "Оновити дані"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdateUserResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/auth/update")
    public Response<UpdateUserResponse> update(@RequestBody UpdateUserRequest request) {
        return new Response<>(HttpStatus.OK, userService.update(request));
    }

    /**
     * Оновити пароль користувача
     *
     * @param request запит
     * @return Response<ChangePasswordResponse> відповідь API
     */
    @Operation(
            summary = "Оновити пароль користувача"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdateUserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldValidResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @PutMapping("/updatePassword")
    public Response<ChangePasswordResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return new Response<>(HttpStatus.OK, userService.changePassword(request));
    }

    /**
     * Отримати інформацію про поточного користувача в системі
     *
     * @return Response<CurrentUserResponse> відповідь API
     */
    @Operation(
            summary = "Отримати інформацію про поточного користувача в системі"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CurrentUserResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/auth/me")
    public Response<CurrentUserResponse> getCurrentUser() {
        return new Response<>(HttpStatus.OK, userService.getCurrentUser());
    }

    /**
     * Отримати користувача по заданим параметрам
     *
     * @param id ID користувача
     * @return Response<CurrentUserResponse> відповідь API
     */
    @Operation(
            summary = "Отримати користувача по заданим параметрам"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/auth/getUser")
    public Response<UserResponse> getUserByParams(@Parameter(description = "ID") @RequestParam(name = "id", required = false) Integer id,
                                                  @Parameter(description = "Ім'я") @RequestParam(name = "firstName", required = false) String firstName,
                                                  @Parameter(description = "Фамілія") @RequestParam(name = "lastName", required = false) String lastName,
                                                  @Parameter(description = "Email") @RequestParam(name = "email", required = false) String email) {
        return new Response<>(HttpStatus.OK, userService.getUserByParams(id, firstName, lastName, email));
    }

    /**
     * Видалити користувача по ID
     *
     * @param id ID користувача
     * @return Response<DeleteUserResponse> відповідь API
     */
    @Operation(
            summary = "Видалити користувача по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeleteUserResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/auth/delete")
    public Response<DeleteUserResponse> delete(@Parameter(description = "ID користувача") @RequestParam(name = "id") int id) {
        return new Response<>(HttpStatus.OK, userService.delete(id));
    }
}
