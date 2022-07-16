package com.project.socialnetwork.repositories;

import com.project.socialnetwork.models.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {
}
