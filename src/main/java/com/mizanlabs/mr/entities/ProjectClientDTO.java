package com.mizanlabs.mr.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectClientDTO {
    private String projectName;
    private String clientName;

    public ProjectClientDTO() {
        // Constructeur par défaut
    }

    public ProjectClientDTO(String projectName, String clientName) {
        this.projectName = projectName;
        this.clientName = clientName;
    }
}
