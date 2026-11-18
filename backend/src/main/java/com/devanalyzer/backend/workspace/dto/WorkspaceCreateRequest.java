package com.devanalyzer.backend.workspace.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkspaceCreateRequest {
    private String name;
}
