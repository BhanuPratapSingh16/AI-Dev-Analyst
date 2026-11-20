package com.devanalyzer.backend.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubRepositoryResponse {
    private Long id;

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    private String description;

    @JsonProperty("clone_url")
    private String cloneUrl;

    @JsonProperty("default_branch")
    private String defaultBranch;

    @JsonProperty("stargazers_count")
    private Integer stars;

    @JsonProperty("forks_count")
    private Integer forks;

    @JsonProperty("open_issues_count")
    private Integer openIssues;

    private String language;

    private String visibility;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;
}