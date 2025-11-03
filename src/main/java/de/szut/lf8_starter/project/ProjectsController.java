package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.ProjectDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectsController {
    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @PostMapping
    public ResponseEntity<ProjectEntity> createProject(@Valid @RequestBody ProjectDto projectDto) {
        ProjectEntity createdProject = this.projectsService.create(projectDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectEntity> updateProject(@PathVariable long id, @Valid @RequestBody ProjectDto projectDto) {
        ProjectEntity updatedProject = this.projectsService.update(id, projectDto);
        return new ResponseEntity<>(updatedProject, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public List<ProjectEntity> getAllProjects() {
        return this.projectsService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@PathVariable long id) {
        ProjectEntity project = this.projectsService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();  //404

        }
        return ResponseEntity.ok(project); // 200
    }

}
