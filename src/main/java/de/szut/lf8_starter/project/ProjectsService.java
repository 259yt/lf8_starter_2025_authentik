package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectsService {
    private final ProjectRepository projectRepository;

    public ProjectsService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    // this is only for Check Dummy Validation for Customer-ID
    public boolean validateCustomerId(long customerId) {
        return customerId == 42 || customerId == 101;
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
        ProjectEntity entity = new ProjectEntity();
        entity.setTitle(projectCreateDto.getTitle());
        entity.setCustomerId(projectCreateDto.getCustomerId());
        entity.setStartDate(projectCreateDto.getStartDate());
        entity.setEndDate(projectCreateDto.getEndDate());

        projectRepository.save(entity);
    }

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }

    public ProjectEntity getProjectById(long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public boolean deleteProjectById(long id) {
        if (!projectRepository.existsById(id)) {
            return false;
        }
        projectRepository.deleteById(id);
        return true;
    }

    public boolean updateProject(long id, ProjectCreateDto dto) {
        Optional<ProjectEntity> optional = projectRepository.findById(id);
        if (optional.isEmpty())
            return false;

        ProjectEntity project = optional.get();


        if (dto.getTitle() == null || dto.getTitle().isBlank())
            throw new IllegalArgumentException("Title darf nicht leer sein.");

        if (!(dto.getCustomerId() == 42 || dto.getCustomerId() == 101))
            throw new IllegalArgumentException("Ungültige Kunden-ID.");

        if (dto.getStartDate().isAfter(dto.getEndDate()))
            throw new IllegalArgumentException("Startdatum darf nicht nach Enddatum liegen.");


        project.setTitle(dto.getTitle());
        project.setCustomerId(dto.getCustomerId());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());

        projectRepository.save(project);
        return true;
    }

    public List<ProjectEntity> getProjectsByEmployeeId(long employeeId) {
        return projectRepository.findByEmployeesContains(employeeId);
    }
}
