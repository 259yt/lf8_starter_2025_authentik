package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectsController {
    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @PostMapping("/api/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProject(@Valid @RequestBody ProjectCreateDto projectCreateDto ) {
        this.projectsService.createProject(projectCreateDto);
    }

    @GetMapping("/api/projects")
    public List<ProjectEntity> getAllProjects() {
        return this.projectsService.getAllProjects();
    }

    @GetMapping("/api/projects/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@PathVariable long id) {
        ProjectEntity project = this.projectsService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();  //404

        }
        return ResponseEntity.ok(project); // 200
    }




}
