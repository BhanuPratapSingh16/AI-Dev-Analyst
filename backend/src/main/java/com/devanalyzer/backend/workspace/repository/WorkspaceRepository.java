package com.devanalyzer.backend.workspace.repository;

import com.devanalyzer.backend.user.entity.User;
import com.devanalyzer.backend.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    List<Workspace> findAllByMembersUserId(UUID userId);

    Optional<Workspace> findByIdAndMembersUserId(UUID id, UUID userId);

    void deleteByIdAndMembersUserId(UUID id, UUID userId);
}
