package com.project.socialnetwork.repositories;

import com.project.socialnetwork.models.entities.RateableRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateableRecordRepository extends JpaRepository<RateableRecord,Long> {
}
