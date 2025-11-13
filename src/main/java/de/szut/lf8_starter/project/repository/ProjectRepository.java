package de.szut.lf8_starter.project.repository;

import de.szut.lf8_starter.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

}
