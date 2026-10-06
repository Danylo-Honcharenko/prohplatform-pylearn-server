package org.ua.fkrkm.progplatform.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.TestServiceI;

/**
 * Клас для взаємодії з тестами
 */
@Tag(name = "Тести")
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    // Сервіс для роботи з тестами
    private final TestServiceI testService;

    /**
     * Перевірити тест
     *
     * @param request тест для перевірки
     * @return CheckTestAnswersResultResponse відповідь API
     */
    @Operation(
            summary = "Перевірити тест"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CheckTestAnswersResultResponse.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FieldValidResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @PostMapping("/check")
    public CheckTestAnswersResultResponse check(@RequestBody CheckTestAnswersRequest request) {
        return testService.check(request);
    }

    /**
     * Отримати тест по UUID
     *
     * @param uuid UUID тесту
     * @return GetTestResponse відповідь API
     */
    @Operation(
            summary = "Отримати тест по UUID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetTestResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @GetMapping("/get")
    public GetTestResponse getTestByUUID(@Parameter(description = "ID тесту") @RequestParam(name = "uuid") String uuid) {
        return testService.getTestByUUID(uuid);
    }

    /**
     * Отримати всі тести
     *
     * @return GetAllTestResponse відповідь API
     */
    @Operation(
            summary = "Отримати всі тести"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllTestResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @GetMapping("/getAll")
    public GetAllTestResponse getAll() {
        return testService.getAll();
    }

    /**
     * Отримати всі результати тестування користувача
     *
     * @return TestResultsResponse відповідь API
     */
    @Operation(
            summary = "Отримати всі результати тестування по ID користувача"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TestResultsResponse.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseBody
    @GetMapping("/getTestsResults")
    public TestResultsResponse getAllUserTestResult() {
        return testService.getAllUserTestResult();
    }
}
