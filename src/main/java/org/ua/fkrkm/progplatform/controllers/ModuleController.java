package org.ua.fkrkm.progplatform.controllers;

import io.swagger.v3.oas.annotations.Operation;
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

    private final ModuleServiceI moduleService;

    /**
     * Встановити пройдену тему модуля
     *
     * @param request інформація про модуль
     * @return Response<SetModuleTopicCompletedResponse> відповідь API
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
    public Response<SetModuleTopicCompletedResponse> setCompletedModuleTopic(@RequestBody SetModuleTopicCompletedRequest request) {
        return new Response<>(HttpStatus.CREATED, moduleService.setCompletedModuleTopic(request));
    }
}
