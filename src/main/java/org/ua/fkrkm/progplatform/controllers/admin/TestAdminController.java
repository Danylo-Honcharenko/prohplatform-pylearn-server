package org.ua.fkrkm.progplatform.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.TestServiceI;

/**
 * Керування тестами
 */
@Tag(name = "Адмін. панель керування тестами")
@RestController
@RequestMapping("/api/admin/test")
@RequiredArgsConstructor
public class TestAdminController {
    // Сервіс для роботи з тестами
    private final TestServiceI testService;

    /**
     * Створити тест
     *
     * @param request запит для створення тесту
     * @return Response<CreateTestResponse> відповідь API
     */
    @Operation(
            summary = "Створити тест"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateTestResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldValidResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Response<CreateTestResponse> create(@Valid @RequestBody CreateTestRequest request) {
        return new Response<>(HttpStatus.CREATED, testService.create(request));
    }
}
