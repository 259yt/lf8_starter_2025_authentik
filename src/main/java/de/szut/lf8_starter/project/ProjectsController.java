package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.ProjectDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/projects")
public class ProjectsController {
    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @Operation(summary = "creates a new project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "project created"),
            @ApiResponse(responseCode = "400", description = "invalid JSON posted"),
            @ApiResponse(responseCode = "422", description = "validation of field failed")
    })
    @PostMapping
    public ResponseEntity<ProjectEntity> createProject(@Valid @RequestBody ProjectDto projectDto) {
        ProjectEntity createdProject = this.projectsService.create(projectDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @Operation(summary = "updates a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "project updated"),
            @ApiResponse(responseCode = "400", description = "invalid JSON posted"),
            @ApiResponse(responseCode = "404", description = "project not found"),
            @ApiResponse(responseCode = "422", description = "validation of field failed")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProjectEntity> updateProject(@PathVariable long id, @Valid @RequestBody ProjectDto projectDto) {
        ProjectEntity updatedProject = this.projectsService.update(id, projectDto);
        return new ResponseEntity<>(updatedProject, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "gets all projects")
    @ApiResponse(responseCode = "200", description = "list of projects")
    @GetMapping
    public List<ProjectEntity> getAllProjects() {
        return this.projectsService.getAllProjects();
    }

    @Operation(summary = "gets a project by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the project"),
            @ApiResponse(responseCode = "404", description = "project not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@PathVariable long id) {
        ProjectEntity project = this.projectsService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();  //404

        }
        return ResponseEntity.ok(project); // 200
    }

    @Operation(summary = "deletes a project by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "project deleted"),
            @ApiResponse(responseCode = "404", description = "project not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable long id) {
        projectsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "get all employees of a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list of employees"),
            @ApiResponse(responseCode = "404", description = "project not found")
    })
    @GetMapping("/{id}/employees")
    public ResponseEntity<Set<Long>> getEmployeesOfProject(@PathVariable long id) {
        return ResponseEntity.ok(projectsService.getEmployees(id));
    }

    @Operation(summary = "add an employee to a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "employee added"),
            @ApiResponse(responseCode = "404", description = "project or employee not found")
    })
    @PostMapping("/{id}/employees")
    public ResponseEntity<Void> addEmployeeToProject(@PathVariable long id, @RequestBody Long employeeId) {
        projectsService.addEmployee(id, employeeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "remove an employee from a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "employee removed"),
            @ApiResponse(responseCode = "404", description = "project or employee not found")
    })
    @DeleteMapping("/{id}/employees/{employeeId}")
    public ResponseEntity<Void> removeEmployeeFromProject(@PathVariable long id, @PathVariable Long employeeId) {
        projectsService.removeEmployee(id, employeeId);
        return ResponseEntity.noContent().build();
    }
}
