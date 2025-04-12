package org.ua.fkrkm.progplatform.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.RoleServiceI;

/**
 * Керування ролями
 */
@Tag(name = "Адмін. панель керування ролями")
@RestController
@RequestMapping("/api/admin/role")
@AllArgsConstructor
public class RoleAdminController {

    // Сервіс для адміністрування ролів
    private final RoleServiceI roleService;

    /**
     * Отримати всі ролі
     *
     * @return Response<GetAllRoleResponse> відповідь API
     */
    @Operation(
            summary = "Отримати всі ролі"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllRoleResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/getAll")
    public Response<GetAllRoleResponse> getAllRole() {
        return new Response<>(HttpStatus.OK, roleService.getAll());
    }
}
