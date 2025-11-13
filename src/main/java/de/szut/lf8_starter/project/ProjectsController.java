package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
})
public class ProjectsController {
    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @Operation(summary = "Create a new project",
            description = "Creates a new project with the given data. The customer ID must be valid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProject(@Valid @RequestBody ProjectCreateDto projectCreateDto) {
        this.projectsService.createProject(projectCreateDto);
    }

    @Operation(summary = "Get all projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all projects")
    })
    @GetMapping
    public List<ProjectEntity> getAllProjects() {
        return this.projectsService.getAllProjects();
    }

    @Operation(summary = "Get a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found"),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@Parameter(description = "ID of the project to be retrieved") @PathVariable long id) {
        try {
            ProjectEntity project = this.projectsService.getProjectById(id);
            return ResponseEntity.ok(project); // 200
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404
        }
    }


    @Operation(summary = "Update a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project or customer not found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProject(@Parameter(description = "ID of the project to be updated") @PathVariable long id, @Valid @RequestBody ProjectCreateDto dto) {
        try {
            this.projectsService.updateProject(id, dto);
            return ResponseEntity.noContent().build(); // 204
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().build(); // 422
        }

    }


    @Operation(summary = "Delete a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectById(@Parameter(description = "ID of the project to be deleted") @PathVariable long id) {
        try {
            this.projectsService.deleteProjectById(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404
        }
    }

    @Operation(summary = "Get all employees of a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of employees"),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/{projectId}/employees")
    public ResponseEntity<Void> getProjectEmployees(@Parameter(description = "ID of the project") @PathVariable long projectId) {
        // TODO: Implementation
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Add an employee to a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee added successfully"),
            @ApiResponse(responseCode = "404", description = "Project or employee not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Employee already in project", content = @Content)
    })
    @PostMapping("/{projectId}/employees")
    public ResponseEntity<Void> addEmployeeToProject(
            @Parameter(description = "ID of the project") @PathVariable long projectId,
            @RequestBody Long employeeId) {
        // TODO: Implementation
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Remove an employee from a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee removed successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project or employee not found", content = @Content)
    })
    @DeleteMapping("/{projectId}/employees/{employeeId}")
    public ResponseEntity<Void> removeEmployeeFromProject(
            @Parameter(description = "ID of the project") @PathVariable long projectId,
            @Parameter(description = "ID of the employee to be removed") @PathVariable long employeeId) {
        // TODO: Implementation
        return ResponseEntity.notFound().build();
    }
}

@RestController
@RequestMapping("/api/employees")
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
})
class EmployeeProjectsController {

    @Operation(summary = "Get all projects for a given employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of projects"),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content)
    })
    @GetMapping("/{employeeId}/projects")
    public ResponseEntity<Void> getEmployeeProjects(
            @Parameter(description = "ID of the employee") @PathVariable long employeeId) {
        // TODO: Implementation
        return ResponseEntity.notFound().build();
    }
}
