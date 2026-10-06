package org.ua.fkrkm.progplatform.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.ModuleServiceI;

/**
 * Клас для взаємодії з модулями
 */
@Tag(name = "Модулі")
@RestController
@RequestMapping("/api/module")
@RequiredArgsConstructor
public class ModuleController {

    // Сервіс для роботи з модулями
    private final ModuleServiceI moduleService;

    /**
     * Отримати модулі по ID курсу
     *
     * @param courseId ID курсу
     * @return ModulesResponse відповідь API
     */
    @Operation(
            summary = "Отримати модулі по ID курсу"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ModulesResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get")
    public ModulesResponse getModulesByCourseId(@Parameter(description = "ID курсу") @RequestParam(name = "courseId") Long courseId) {
        return this.moduleService.getModulesByCourseId(courseId);
    }

    /**
     * Отримати модуль по ID
     *
     * @param id ID модуля
     * @return ModuleResponse відповідь API
     */
    @Operation(
            summary = "Отримати модуль по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ModuleResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ModuleResponse getModuleById(@Parameter(description = "ID модуля") @PathVariable Long id) {
        return this.moduleService.getModuleById(id);
    }

    /**
     * Встановити пройдену тему модуля
     *
     * @param request інформація про модуль
     * @return SetModuleTopicCompletedResponse відповідь API
     */
    @Operation(
            summary = "Встановити пройдену тему модуля"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SetModuleTopicCompletedResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldValidResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/setCompletedTopic")
    public SetModuleTopicCompletedResponse setCompletedModuleTopic(@RequestBody SetModuleTopicCompletedRequest request) {
        return moduleService.setCompletedModuleTopic(request);
    }
}
