CREATE TABLE github_repositories (
    github_repo_id BIGINT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    clone_url TEXT NOT NULL,
    default_branch VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE workspace_repositories (
    workspace_id UUID NOT NULL
        REFERENCES workspaces(id) ON DELETE CASCADE,

    github_repo_id BIGINT NOT NULL
        REFERENCES github_repositories(github_repo_id) ON DELETE CASCADE,

    connected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (workspace_id, github_repo_id)
);

CREATE INDEX idx_workspace_repositories_repo_id
    ON workspace_repositories(github_repo_id);
