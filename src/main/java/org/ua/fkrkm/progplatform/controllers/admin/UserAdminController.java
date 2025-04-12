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
import org.ua.fkrkm.progplatform.services.UserServiceI;

/**
 * Керування користувачами
 */
@Tag(name = "Адмін. панель керування користувачами")
@RestController
@RequestMapping("/api/admin/user")
@AllArgsConstructor
public class UserAdminController {

    // Сервіс для адміністрування користувачів
    private final UserServiceI userService;

    /**
     * Отримання всіх користувачів
     *
     * @return Response<GetAllUsersResponse> відповідь API
     */
    @Operation(
            summary = "Отримати всіх користувачів"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllUsersResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/getAll")
    public Response<GetAllUsersResponse> getAllUser(@Parameter(description = "Кількість записів") @RequestParam(name = "recordLimit", required = false) Integer recordLimit) {
        return new Response<>(HttpStatus.OK, userService.getAllUsers(recordLimit));
    }

    /**
     * Оновлення ролі користувача
     *
     * @param updateUserRoleRequest запит
     * @return Response<UpdateUserRoleResponse> відповідь API
     */
    @Operation(
            summary = "Оновити роль користувачу"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdateUserRoleResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldValidResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/updateRole")
    public Response<UpdateUserRoleResponse> updateUserRole(@Valid @RequestBody UpdateUserRoleRequest updateUserRoleRequest) {
        return new Response<>(HttpStatus.OK, userService.updateUserRole(updateUserRoleRequest));
    }
}
