package de.szut.lf8_starter.project;


import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository // For DB
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

}