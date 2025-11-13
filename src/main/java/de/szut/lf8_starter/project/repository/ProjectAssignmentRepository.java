package de.szut.lf8_starter.project.repository;

import de.szut.lf8_starter.project.entity.ProjectAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignmentEntity, Long> {

    boolean existsByProject_IdAndEmployeeId(Long projectId, Long employeeId);

    @Query("""
        select case when count(a) > 0 then true else false end
        from ProjectAssignmentEntity a
        where a.employeeId = :employeeId
          and not (a.project.plannedEndDate < :targetStart or a.project.startDate > :targetEnd)
    """)
    boolean existsOverlappingAssignment(Long employeeId, LocalDate targetStart, LocalDate targetEnd);

    List<ProjectAssignmentEntity> findAllByProject_Id(Long projectId);

    Optional<ProjectAssignmentEntity> findByProject_IdAndEmployeeId(Long projectId, Long employeeId);

    @Query("""
        select a
        from ProjectAssignmentEntity a
        join fetch a.project p
        where a.employeeId = :employeeId
    """)
    List<ProjectAssignmentEntity> findAllByEmployeeIdWithProject(Long employeeId);
}
