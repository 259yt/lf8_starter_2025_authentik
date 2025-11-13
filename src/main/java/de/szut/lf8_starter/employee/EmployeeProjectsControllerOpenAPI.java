package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.project.dto.EmployeeProjectsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

public interface EmployeeProjectsControllerOpenAPI {

    @Operation(summary = "list projects for an employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeProjectsResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "employee not found", content = @Content)
    })
    List<EmployeeProjectsResponseDto> getEmployeeProjects(Long employeeId, String authorization);
}
