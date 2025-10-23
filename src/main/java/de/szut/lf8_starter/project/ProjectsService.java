package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import org.springframework.stereotype.Service;

@Service
public class ProjectsService {
    private final ProjectRepository projectRepository;

    public ProjectsService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    // this is only for Check Dummy Validation for Customer-ID
    public boolean validateCustomerId(long customerId) {
        return customerId ==42 || customerId == 101 ;
    }


    public void createProject(ProjectCreateDto projectCreateDto) {
        // Check the Title
        if (projectCreateDto.getTitle() == null || projectCreateDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("500 Title darf nicht leer sein.");
        }

        // Check Customer-ID (dummy)
        if (!(projectCreateDto.getCustomerId() == 42 || projectCreateDto.getCustomerId() == 101)) {
            throw new IllegalArgumentException("500 Ungültige Kunden-ID.");
        }

        //  Check the date
        if (projectCreateDto.getStartDate().isAfter(projectCreateDto.getEndDate())) {
            throw new IllegalArgumentException("500 Startdatum darf nicht nach Enddatum liegen.");
        }

        //  Create Project
        ProjectEntity entity = new ProjectEntity(
                0,
                projectCreateDto.getTitle(),
                projectCreateDto.getCustomerId(),
                projectCreateDto.getStartDate(),
                projectCreateDto.getEndDate()
        );

        projectRepository.save(entity);
    }

}
