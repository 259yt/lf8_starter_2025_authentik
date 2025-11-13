package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.AssignEmployeeRequestDto;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectEmployeeDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface ProjectsControllerOpenAPI {

    @Operation(summary = "create a new project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "400", description = "invalid body", content = @Content),
            @ApiResponse(responseCode = "404", description = "customer not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    ProjectGetDto createProject(ProjectCreateDto dto);

    @Operation(summary = "list all projects")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "ok",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProjectGetDto.class)))}))
    List<ProjectGetDto> getAllProjects();

    @Operation(summary = "get project by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok",
                    content = @Content(schema = @Schema(implementation = ProjectGetDto.class))),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)
    })
    ProjectGetDto getProjectById(Long id);

    @Operation(summary = "update project")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateProject(Long id, ProjectCreateDto dto);

    @Operation(summary = "delete project")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProjectById(Long id);

    @Operation(summary = "assign employee to project")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "assigned", content = @Content),
            @ApiResponse(responseCode = "404", description = "project/employee not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "duplicate/overlap", content = @Content),
            @ApiResponse(responseCode = "422", description = "lacks qualification", content = @Content),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void addEmployeeToProject(Long projectId, AssignEmployeeRequestDto body, String authorization);

    @Operation(summary = "remove employee from project")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "removed", content = @Content),
            @ApiResponse(responseCode = "404", description = "project/employee/assignment not found", content =
            @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeEmployeeFromProject(Long projectId, Long employeeId, String authorization);

    @Operation(summary = "list employees of a project")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "ok",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProjectEmployeeDto.class)))}))
    List<ProjectEmployeeDto> getProjectEmployees(Long projectId);
}