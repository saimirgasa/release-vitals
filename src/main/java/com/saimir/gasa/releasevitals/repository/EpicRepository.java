package com.saimir.gasa.releasevitals.repository;

import com.saimir.gasa.releasevitals.domain.Epic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Epic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EpicRepository extends JpaRepository<Epic, Long> {

    @Query(value = "select distinct epic from Epic epic left join fetch epic.projects left join fetch epic.unestimatedIssues",
        countQuery = "select count(distinct epic) from Epic epic")
    Page<Epic> findAllWithEagerRelationships(Pageable pageable);

//    @Query(value = "select distinct epic from Epic epic left join fetch epic.projects")
    @Query(value = "select distinct epic from Epic epic left join fetch epic.projects left join fetch epic.unestimatedIssues")
    List<Epic> findAllWithEagerRelationships();

    @Query("select epic from Epic epic left join fetch epic.projects left join fetch epic.unestimatedIssues where epic.id =:id")
    Optional<Epic> findOneWithEagerRelationships(@Param("id") Long id);

}
