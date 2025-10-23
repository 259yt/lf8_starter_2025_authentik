package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
