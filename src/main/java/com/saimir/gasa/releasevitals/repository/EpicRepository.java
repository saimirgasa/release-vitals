package com.saimir.gasa.releasevitals.repository;

import com.saimir.gasa.releasevitals.domain.Epic;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Epic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EpicRepository extends JpaRepository<Epic, Long> {

}
