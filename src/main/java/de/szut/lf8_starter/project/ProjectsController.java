package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ProjectsController {
    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @PostMapping("/api/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProject(@Valid @RequestBody ProjectCreateDto projectCreateDto) {
        this.projectsService.createProject(projectCreateDto);
    }

    @Operation(summary = "gets all projects")
    @ApiResponse(responseCode = "200", description = "list of projects")
    @GetMapping("/api/projects")
    public List<ProjectEntity> getAllProjects() {
        return this.projectsService.getAllProjects();
    }

    @Operation(summary = "gets a project by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the project"),
            @ApiResponse(responseCode = "404", description = "project not found")
    })
    @GetMapping("/api/projects/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@PathVariable long id) {
        ProjectEntity project = this.projectsService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();  //404
        }
        return ResponseEntity.ok(project); // 200
    }

    @PutMapping("/api/projects/{id}")
    public ResponseEntity<Void> updateProject(@PathVariable long id, @Valid @RequestBody ProjectCreateDto dto) {
        try {
            boolean updated = this.projectsService.updateProject(id, dto);
            if (updated) {
                return ResponseEntity.noContent().build(); // 204
            }
            return ResponseEntity.notFound().build(); //404

        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping("/api/projects/{id}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable long id) {
        boolean deleted = this.projectsService.deleteProjectById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
