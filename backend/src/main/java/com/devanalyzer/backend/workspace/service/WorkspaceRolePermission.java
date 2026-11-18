package com.devanalyzer.backend.workspace.service;

import com.devanalyzer.backend.workspace.entity.WorkspacePermission;
import com.devanalyzer.backend.workspace.entity.WorkspaceRole;

import java.util.EnumSet;
import java.util.Set;

public class WorkspaceRolePermission {
    public static Set<WorkspacePermission> getPermissions(WorkspaceRole role){
        return switch (role) {
            case OWNER -> EnumSet.allOf(WorkspacePermission.class);
            case ADMIN -> EnumSet.of(
                    WorkspacePermission.VIEW_WORKSPACE,
                    WorkspacePermission.UPDATE_WORKSPACE,
                    WorkspacePermission.VIEW_MEMBERS,
                    WorkspacePermission.ADD_MEMBERS,
                    WorkspacePermission.REMOVE_MEMBERS,
                    WorkspacePermission.CHANGE_MEMBER_ROLES,
                    WorkspacePermission.VIEW_ANALYTICS
            );
            case DEVELOPER -> EnumSet.of(
                    WorkspacePermission.VIEW_WORKSPACE,
                    WorkspacePermission.VIEW_MEMBERS,
                    WorkspacePermission.VIEW_ANALYTICS
            );
        };
    }
}
