package com.project.socialnetwork.repositories;

import com.project.socialnetwork.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Post,Long> {
}
