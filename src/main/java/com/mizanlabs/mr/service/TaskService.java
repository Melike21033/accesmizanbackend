//TaskService
package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Devis;
import com.mizanlabs.mr.entities.ElementDevis;
import com.mizanlabs.mr.entities.Project;
import com.mizanlabs.mr.entities.Task;
import com.mizanlabs.mr.repository.TaskRepository;
import com.mizanlabs.mr.repository.ElementDevisRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final ProjectService projectService;
    private final  ElementDevisRepository elementDevisRepository;

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectService projectService, ElementDevisService elementDevisService, ElementDevisRepository elementDevisRepository) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.elementDevisRepository = elementDevisRepository;
    }
    public boolean taskContainsElements(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));

        // Force loading of elementsDevis if not loaded yet
        task.getElementDevis().size();

        Set<ElementDevis> elements = task.getElementDevis();

        return elements != null && !elements.isEmpty();
    }

    public Task getTaskDatesById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskByName(String taskname) {
        return taskRepository.findByTaskName(taskname);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findByTaskId(taskId);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(Long id, Task taskDetails) {
        return taskRepository.findById(id).map(task -> {
            // Check if updated name conflicts within the same project
            if (!task.getTaskName().equals(taskDetails.getTaskName()) &&
                    taskRepository.existsByTaskNameAndProjectId(taskDetails.getTaskName(), task.getProject().getProjectId())) {
                throw new IllegalStateException("A task with the same name already exists in this project.");
            }
            task.setTaskName(taskDetails.getTaskName());
            task.setStart(taskDetails.getStart());
            task.setDeadline(taskDetails.getDeadline());
            task.setPriority(taskDetails.getPriority());
            task.setStatus(taskDetails.getStatus());
            task.setNote(taskDetails.getNote());
            return taskRepository.save(task);
        });

    }
    public Optional<Task> addTaskToDevis(Long id, Devis devis) {
        try {
            return taskRepository.findById(id).map(task -> {
                task.setDevis(devis);
                System.out.println("//////////////////////////////////////////////////////////////////////////////////devis id"+devis.getDevisId());
// devis.setTotalDevis(this.calculateTotalTaskForDevis((long)12));
                return taskRepository.save(task);
            });
        } catch (Exception e) {
            // Gérer l'exception (par exemple, journalisation)
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Task> deleteTaskFromDevis(Long id) {
        try {
            return taskRepository.findById(id).map(task -> {
                task.setDevis(null);

                return taskRepository.save(task);
            });
        } catch (Exception e) {
            // Gérer l'exception (par exemple, journalisation)
            e.printStackTrace();
            return Optional.empty();
        }
    }


    @Transactional
    public boolean deleteTask(Long id) {
        // Vérifie si la tâche existe
        if (!taskRepository.existsById(id)) {
            return false;
        }

//        // Supprime d'abord tous les éléments de devis associés
//        List<ElementDevis> elements = elementDevisRepository.findByTask_TaskId(id);
//        elementDevisRepository.deleteAll(elements);

        // Puis supprime la tâche
        taskRepository.deleteById(id);
        return true;
    }
    public List<Task> getTasksByProject(Project project) {
        return taskRepository.findByProject(project);
    }
    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProject_ProjectId(projectId);
    }


    public String getTaskNameById(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        return taskOptional.map(Task::getTaskName).orElse(null);
    }

    public String getMaxRefTaskForProject(Long projectId) {
        String refProjet;
        int numSeqtsInt;
        String maxRefTask =  taskRepository.findMaxRefTaskByProjectId(projectId);
        String yearShort;
        if(maxRefTask == null || maxRefTask.length() < 8 || maxRefTask=="" ) {
            String refProjetcomp= projectService.getRefProjetByProjectId(projectId);
            refProjet=refProjetcomp.substring(2,5);
            numSeqtsInt=1;
            yearShort=refProjetcomp.substring(5);
            // yearShort="24";
        }
        else {
            String  numSeqts = maxRefTask.substring(5, 7);

            numSeqtsInt = Integer.parseInt(numSeqts) + 1;
            yearShort =maxRefTask.substring(7);
            refProjet =maxRefTask.substring(2,5);}

        String refTask = "TS" +refProjet+ String.format("%02d", numSeqtsInt)  + yearShort;
        return refTask;
    }


    public Task createTaskWithProjectId(Task task, Long projectId) {
        return projectService.getProjectById(projectId).map(project -> {
            task.setProject(project);
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID : " + projectId));
    }
//    4u ly3nuni

    public List<Task> getTasksWithNoDevisForProject(Project project) {
        return taskRepository.findTasksByProjectAndDevisIsNull(project);
    }


    public List<Task> getTasksByDevisId(Long devisId) {
        return taskRepository.findByDevisId(devisId);
    }
    //    yove hun
    public String findRefTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            return task.getRefTask();
        } else {
            return null; // Ou lancez une exception appropriée si la tâche n'est pas trouvée
        }
    }

    public boolean taskNameExists(String name, Long projectId) {
        // Supposons que vous avez un repository pour accéder aux données des tâches
        return taskRepository.existsByTaskNameAndProjectId(name, projectId);
    }
    @Autowired
    private ElementDevisService elementDevisService;

    public double calculateTaskTotal(Long taskId) {
        Task task = taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with name: " + taskId));
        double totalAmount = 0;
        List<ElementDevis> elements = elementDevisService.getElementDevisByTask(task);
        for (ElementDevis element : elements) {
            if (!"PM".equals(element.getStatus())) {
                totalAmount += element.getMontant();
            }
        }
        return totalAmount;
    }
    public String findByTask(String taskName) {
        // Utilisez la méthode findByTaskName pour obtenir la tâche correspondant au nom donné
        Optional<Task> optionalTask = taskRepository.findByTaskName(taskName);

        // Vérifiez si la tâche existe dans la base de données
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            // Vous pouvez retourner n'importe quelle propriété de la tâche ou un format spécifique ici
            return "Task found with name: " + task.getTaskName();
        } else {
            return "Task not found with name: " + taskName;
        }
    }
    public Set<ElementDevis> findElementDevisByTaskId(Long taskId) {
        return taskRepository.findElementDevisByTaskId(taskId);
    }

    public Integer calculateTotalTaskForDevis(Long devisId) {
        return taskRepository.calculateTotalTaskForDevis(devisId);
    }

    public Integer getTaskAmount(Long taskId) {
        // Utilisez le repository pour trouver le montant de la tâche
        return taskRepository.findTotalTaskById(taskId);
    }


    public Integer getTotalTasksByDevisId(Long devisId) {
        return taskRepository.sumTotalTaskByDevisId(devisId);
    }


    public Map<String, Long> getTaskStatusDistribution() {
        return convertToMap(taskRepository.getTaskStatusDistribution());
    }

    public Map<String, Long> getTaskStatusDistributionByStatus(String status) {
        return convertToMap(taskRepository.getTaskStatusDistributionByStatus(status));
    }

    public Map<String, Long> getTaskStatusDistributionByDateRange(Date startDate, Date endDate) {
        return convertToMap(taskRepository.getTaskStatusDistributionByDateRange(startDate, endDate));
    }

    private Map<String, Long> convertToMap(List<Object[]> results) {
        Map<String, Long> distributionMap = new HashMap<>();
        for (Object[] result : results) {
            String statusName = (String) result[0];
            Long count = (Long) result[1];
            distributionMap.put(statusName, count);
        }
        return distributionMap;
    }

    public List<Object[]> findAllTaskDurations() {
        return taskRepository.findAllTaskDurations();
    }
}