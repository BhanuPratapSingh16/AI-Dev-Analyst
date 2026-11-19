package com.devanalyzer.backend.workspace.dto;

import com.devanalyzer.backend.workspace.entity.WorkspaceRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeMemberRoleRequest {
    private UUID userId;
    private WorkspaceRole role;
}
