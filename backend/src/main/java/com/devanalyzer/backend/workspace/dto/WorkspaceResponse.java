package com.devanalyzer.backend.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class WorkspaceResponse {
    private UUID id;
    private String name;
    private UUID owner_id;
    private LocalDateTime createdAt;
}
