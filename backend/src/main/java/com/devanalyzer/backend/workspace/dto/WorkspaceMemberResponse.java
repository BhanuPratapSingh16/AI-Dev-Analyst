package com.devanalyzer.backend.workspace.dto;

import com.devanalyzer.backend.workspace.entity.WorkspaceRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class WorkspaceMemberResponse {
    private UUID userId;
    private String userName;
    private String userEmail;
    private WorkspaceRole role;
    private LocalDateTime joinedAt;
}
