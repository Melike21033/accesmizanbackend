package com.mizanlabs.mr.entities;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long taskId;
    private Integer totalTask;
    private LocalDate deadline;
    private String taskName;
    private String note;
    private Long priorityId;
    private LocalDate start;
    private Long statusId;
    private String refTask;
    private Long projectId;
    private Long devisId;
}
