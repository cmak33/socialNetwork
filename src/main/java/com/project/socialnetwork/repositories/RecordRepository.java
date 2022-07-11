package com.project.socialnetwork.repositories;

import com.project.socialnetwork.models.entities.PostedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<PostedRecord,Long> {
}
