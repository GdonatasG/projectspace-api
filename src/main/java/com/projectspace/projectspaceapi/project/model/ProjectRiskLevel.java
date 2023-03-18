package com.projectspace.projectspaceapi.project.model;

public enum ProjectRiskLevel {
    UNKNOWN("UNKNOWN"), NO_RISK("NO_RISK"), LOW("LOW"), MEDIUM("MEDIUM"), HIGH("HIGH");

    private final String name;

    ProjectRiskLevel(String name) {
        this.name = name;
    }

    public String getVisualName() {
        return name;
    }
}
