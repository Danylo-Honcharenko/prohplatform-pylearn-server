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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.TopicServiceI;

/**
 * Керування темами
 */
@Tag(name = "Адмін. панель керування темами курсу")
@RestController
@RequestMapping("/api/admin/topic")
@RequiredArgsConstructor
public class TopicAdminController {

    // Сервіс для роботи з темами курсу
    private final TopicServiceI topicService;

    /**
     * Створити тему
     *
     * @param request запит
     * @return Response<CreateTopicResponse> відповідь API
     */
    @Operation(
            summary = "Створити тему"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateTopicResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldValidResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Response<CreateTopicResponse> create(@Valid @RequestBody CreateTopicRequest request) {
        return new Response<>(HttpStatus.CREATED, topicService.create(request));
    }

    /**
     * Оновити тему
     *
     * @param request запит
     * @return Response<UpdateTopicResponse> відповідь API
     */
    @Operation(
            summary = "Оновити тему"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdateTopicResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/update")
    public Response<UpdateTopicResponse> update(@RequestBody UpdateTopicRequest request) {
        return new Response<>(HttpStatus.OK, topicService.update(request));
    }

    /**
     * Видалити тему
     *
     * @param id ID теми
     * @return Response<DeleteTopicResponse> відповідь API
     */
    @Operation(
            summary = "Видалити тему"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeleteTopicResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete")
    public Response<DeleteTopicResponse> delete(@Parameter(description = "ID теми") @RequestParam(name = "id") int id) {
        return new Response<>(HttpStatus.OK, topicService.delete(id));
    }
}
