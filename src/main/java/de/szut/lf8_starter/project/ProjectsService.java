package de.szut.lf8_starter.project;

import de.szut.lf8_starter.client.ClientService;
import de.szut.lf8_starter.exceptionHandling.ClientNotFoundException;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectDto;
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

    public ProjectEntity create(ProjectDto dto) {
        if (!clientService.isClientValid(dto.getCustomerId())) {
            throw new ClientNotFoundException("Client with id " + dto.getCustomerId() + " not found");
        }
        ProjectEntity entity = new ProjectEntity();
        entity.setTitle(dto.getTitle());
        entity.setCustomerId(dto.getCustomerId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return projectRepository.save(entity);
    }

    public ProjectEntity update(long id, ProjectDto dto) {
        Optional<ProjectEntity> projectOptional = projectRepository.findById(id);
        if (projectOptional.isEmpty()) {
            throw new ResourceNotFoundException("Project with id " + id + " not found");
        }

        if (!clientService.isClientValid(dto.getCustomerId())) {
            throw new ClientNotFoundException("Client with id " + dto.getCustomerId() + " not found");
        }

        ProjectEntity entity = projectOptional.get();
        entity.setTitle(dto.getTitle());
        entity.setCustomerId(dto.getCustomerId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        return projectRepository.save(entity);
    }

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }

    public ProjectEntity getProjectById(long id) {
        return projectRepository.findById(id).orElse(null);
    }

}
