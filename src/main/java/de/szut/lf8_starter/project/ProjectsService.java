package de.szut.lf8_starter.project;

import de.szut.lf8_starter.client.ClientService;
import de.szut.lf8_starter.exceptionHandling.ClientNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectsService {
    private final ProjectRepository projectRepository;
    private final ClientService clientService;

    public ProjectsService(ProjectRepository projectRepository, ClientService clientService) {
        this.projectRepository = projectRepository;
        this.clientService = clientService;
    }

    public void createProject(ProjectCreateDto projectCreateDto) {
        if (!clientService.isClientValid(projectCreateDto.getCustomerId())) {
            throw new ClientNotFoundException("Client with id " + projectCreateDto.getCustomerId() + " not found");
        }
        if (projectCreateDto.getTitle() == null || projectCreateDto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title darf nicht leer sein.");
        }
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
        if (optional.isEmpty()) {
            return false;
        }

        if (!clientService.isClientValid(dto.getCustomerId())) {
            throw new ClientNotFoundException("Client with id " + dto.getCustomerId() + " not found");
        }

        ProjectEntity project = optional.get();

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title darf nicht leer sein.");
        }

        if (dto.getStartDate() != null && dto.getEndDate() != null && dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Startdatum darf nicht nach Enddatum liegen.");
        }

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
