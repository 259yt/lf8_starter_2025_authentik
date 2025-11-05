package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final ProjectsService projectsService;

    public EmployeeController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @Operation(summary = "gets all projects for an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list of projects"),
            @ApiResponse(responseCode = "404", description = "employee not found")
    })
    @GetMapping("/{id}/projects")
    public ResponseEntity<List<ProjectEntity>> getProjectsByEmployee(@PathVariable long id) {
        List<ProjectEntity> projects = projectsService.getProjectsByEmployeeId(id);
        return ResponseEntity.ok(projects);
    }
}

