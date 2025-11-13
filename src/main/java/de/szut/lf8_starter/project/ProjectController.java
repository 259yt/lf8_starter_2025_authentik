package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exception.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.AssignEmployeeRequestDto;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectEmployeeDto;
import de.szut.lf8_starter.project.entity.ProjectEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
})
public class ProjectController {

    private final ProjectService projectsService;

    @Operation(summary = "Create a new project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProject(@Valid @RequestBody ProjectCreateDto projectCreateDto) {
        projectsService.createProject(projectCreateDto);
    }

    @Operation(summary = "Get all projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all projects")
    })
    @GetMapping
    public List<ProjectEntity> getAllProjects() {
        return projectsService.getAllProjects();
    }

    @Operation(summary = "Get a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found"),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(
            @Parameter(description = "ID of the project to be retrieved")
            @PathVariable long id) {
        try {
            ProjectEntity project = projectsService.getProjectById(id);
            return ResponseEntity.ok(project);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project or customer not found", content = @Content),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProject(
            @Parameter(description = "ID of the project to be updated") @PathVariable Long id,
            @Valid @RequestBody ProjectCreateDto dto) {
        try {
            boolean updated = projectsService.updateProject(id, dto);
            return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectById(
            @Parameter(description = "ID of the project to be deleted") @PathVariable long id) {
        boolean deleted = projectsService.deleteProjectById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // --- GET /api/projects/{id}/employees
    @Operation(summary = "Get all employees of a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of employees"),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/{id}/employees")
    public ResponseEntity<List<ProjectEmployeeDto>> getProjectEmployees(@PathVariable("id") Long projectId) {
        List<ProjectEmployeeDto> result = projectsService.getEmployeesForProject(projectId);
        return ResponseEntity.ok(result);
    }

    // --- POST /api/projects/{projectId}/employees
    @Operation(summary = "Assign an employee to a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Project or employee not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Employee already in project or overlapping", content = @Content),
            @ApiResponse(responseCode = "422", description = "Employee lacks required qualification", content = @Content)
    })
    @PostMapping("/{projectId}/employees")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addEmployeeToProject(
            @Parameter(description = "ID of the project") @PathVariable Long projectId,
            @Valid @RequestBody AssignEmployeeRequestDto body,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        String bearer = authorization != null ? authorization : "";
        projectsService.assignEmployee(projectId, body.getMaId(), body.getRoleId(), bearer);
    }

    // --- DELETE /api/projects/{projectId}/employees/{employeeId}
    @Operation(summary = "Remove an employee from a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee removed successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project or employee or assignment not found", content = @Content)
    })
    @DeleteMapping("/{projectId}/employees/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEmployeeFromProject(
            @PathVariable Long projectId,
            @PathVariable Long employeeId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        String bearer = authorization != null ? authorization : "";
        projectsService.removeEmployeeFromProject(projectId, employeeId, bearer);
    }
}
