package com.saimir.gasa.releasevitals.repository;

import com.saimir.gasa.releasevitals.domain.Version;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Version entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {

}
