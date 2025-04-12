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
import org.ua.fkrkm.progplatform.services.TopicServiceI;
import org.ua.fkrkm.progplatformclientlib.response.*;

/**
 * Клас для взаємодії з темами
 */
@Tag(name = "Теми курсу")
@RestController
@RequestMapping("/api/topic")
@RequiredArgsConstructor
public class TopicController {

    // Сервіс для роботи з темами
    private final TopicServiceI topicService;

    /**
     * Отримати всі теми модуля
     *
     * @param moduleId ID модуля
     * @return Response<GetAllCourseTopics> відповідь API
     */
    @Operation(
            summary = "Отримати всі теми модуля"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllCourseModules.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAllModuleTopics")
    public Response<GetAllCourseModules> getAllModuleTopics(@Parameter(description = "ID модуля") @RequestParam(name = "moduleId") int moduleId) {
        return new Response<>(HttpStatus.OK, topicService.getAllModuleTopics(moduleId));
    }

    /**
     * Отримати тему по ID
     *
     * @param topicId ID теми
     * @return Response<TopicResponse> відповідь API
     */
    @Operation(
            summary = "Отримати тему по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TopicResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get")
    public Response<TopicResponse> getTopicById(@Parameter(description = "ID теми") @RequestParam(name = "topicId") int topicId) {
        return new Response<>(HttpStatus.OK, topicService.getTopicById(topicId));
    }
}
