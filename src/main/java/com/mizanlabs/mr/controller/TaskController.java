//TaskController
package com.mizanlabs.mr.controller;
import com.mizanlabs.mr.repository.ElementDevisRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import com.mizanlabs.mr.entities.Devis;
import com.mizanlabs.mr.entities.ElementDevis;
import com.mizanlabs.mr.entities.Project;
import com.mizanlabs.mr.entities.Task;
import com.mizanlabs.mr.service.DevisService;
import com.mizanlabs.mr.service.ElementDevisService;
import com.mizanlabs.mr.service.ProjectService;
import com.mizanlabs.mr.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final ElementDevisService elementDevisService;
    private final DevisService devisService;
    private ElementDevisRepository elementDevisRepository;

    @Autowired
    public TaskController(TaskService taskService, ProjectService projectService, ElementDevisService elementDevisService,DevisService devisService,ElementDevisRepository elementDevisRepository) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.elementDevisService = elementDevisService;
        this.devisService = devisService;
        this.elementDevisRepository=elementDevisRepository;
    }

    // Create a new task
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/task/{taskId}/hasElementDevis")
    public boolean hasElementDevis(@PathVariable Long taskId) {
        // Rechercher un élément de devis avec l'ID de la tâche spécifiée
        Task task = new Task();
        task.setTaskId(taskId);

        // Vérifier si un élément de devis existe pour cette tâche
        return !elementDevisRepository.findByTask(task).isEmpty();
    }
    // Get all tasks
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping

    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // Get a single task by ID
    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a task
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //    ya3nini
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("addTask/{id}/toDevis")
    public ResponseEntity<Task> addTasktoDevis(@PathVariable Long id, @RequestBody Devis devis) {


        return taskService.addTaskToDevis(id, devis)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PutMapping("deleteTask/{id}/FromDevis")
    public ResponseEntity<Task> deleteTaskFromDevis(@PathVariable Long id) {


        return taskService.deleteTaskFromDevis(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //may3nini
    // Delete a task
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    // get elements by task
//    @GetMapping("/{taskName}/elementsdevis")
//    public ResponseEntity<List<ElementDevis>> getElementsDevisByTaskName(@PathVariable String taskName) {
//        Optional<Task> taskOptional = taskService.getTaskByName(taskName);
//
//        if (taskOptional.isPresent()) {
//            List<ElementDevis> elementDevisList = elementDevisService.getElementDevisByTask(taskOptional.get());
//            return ResponseEntity.ok(elementDevisList);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/byProject/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProject(@PathVariable Long projectId) {
        Optional<Project> projectOptional = projectService.getProjectById(projectId);

        if (projectOptional.isPresent()) {
            List<Task> taskList = taskService.getTasksByProject(projectOptional.get());
            return ResponseEntity.ok(taskList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/project/{projectId}")
    public List<Task> getTasksByProjectId(@PathVariable Long projectId) {
        return taskService.getTasksByProjectId(projectId);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/find")
    public ResponseEntity<String> findByTask(@RequestParam String taskName) {
        String result = taskService.findByTask(taskName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/maxRefTask/{projectId}")
    public ResponseEntity<String> getMaxRefTaskForProject(@PathVariable Long projectId) {
        String maxRefTask = taskService.getMaxRefTaskForProject(projectId);
        if (maxRefTask != null) {
            return ResponseEntity.ok(maxRefTask);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/generateRefTask/{projectId}")
    public ResponseEntity<?> generateNextRefTaskForProject(@PathVariable Long projectId) {
        try {
            String refTask = taskService.getMaxRefTaskForProject(projectId);
            return ResponseEntity.ok(refTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }}


    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/{taskId}/maxRefEdevis")
    public ResponseEntity<String> getMaxRefEdevis(@PathVariable Long taskId) {
        String maxRefEdevis = elementDevisService.getMaxRefEdevisForTask(taskId);
        return ResponseEntity.ok(maxRefEdevis);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping("/add")
    public ResponseEntity<?> createTaskWithProjectId(@RequestBody Task task, @RequestParam("projectid") Long projectId) {
        try {
            String taskref = taskService.getMaxRefTaskForProject(projectId);
            task.setRefTask(taskref);

            Project project = projectService.findById(projectId);
            task.setProject(project);

            Task savedTask = taskService.createTaskWithProjectId(task, projectId);

            return ResponseEntity.ok(savedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout de la tâche");
        }
    }
    // @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
//    @GetMapping("/{taskId}/dates")
//    public ResponseEntity<Map<LocalDate, LocalDate>> getTaskDatesById(@PathVariable Long taskId) {
//        Task task = taskService.getTaskDatesById(taskId);
//        Map<LocalDate, LocalDate> dates = new HashMap<>();
//        dates.put("start", task.getStart() != null ? task.getStart() : "Not available");
//        dates.put("deadline", task.getDeadline() != null ? task.getDeadline() : "Not available");
//        return ResponseEntity.ok(dates);
//    }

    //    4u lahume ly3nuni
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/{projectId}/tasks/no-devis")
    public ResponseEntity<List<Task>> getTasksWithNoDevis(@PathVariable Long projectId) {
        Optional<Project> projectOptional = projectService.getProjectById(projectId); // Supposons que cette méthode retourne Optional<Project>

        if (!projectOptional.isPresent()) {
            return ResponseEntity.notFound().build(); // Retourne 404 si le projet n'existe pas
        }

        Project project = projectOptional.get(); // Extrait le projet de l'Optional
        List<Task> tasks = taskService.getTasksWithNoDevisForProject(project);
        return ResponseEntity.ok(tasks); // Retourne la liste des tâches sans devis pour le projet
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("devis/{devisId}/tasks")
    public ResponseEntity<List<Task>> getTasksByDevisId(@PathVariable Long devisId) {
        List<Task> tasks = taskService.getTasksByDevisId(devisId);
        if (tasks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tasks);
    }



//    @GetMapping("/{taskId}/refTask")
//    public String getRefTask(@PathVariable Long taskId) {
//        return taskService.findRefTaskById(taskId);
//    }

//    @GetMapping("/calculateTotalTaskForDevis/{devisId}")
//    public ResponseEntity<Integer> calculateTotalTaskForDevis(@PathVariable Long devisId) {
//        Integer totalTask = taskService.calculateTotalTaskForDevis(devisId);
//        return ResponseEntity.ok(totalTask);
//    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/{taskId}/elementsdevis")
    public ResponseEntity<Set<ElementDevis>> getElementDevisByTaskId(@PathVariable Long taskId) {
        Set<ElementDevis> elementDevisSet = taskService.findElementDevisByTaskId(taskId);
        if (elementDevisSet == null || elementDevisSet.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(elementDevisSet);
        }
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/{taskId}/amount")
    public ResponseEntity<Integer> getTaskAmount(@PathVariable Long taskId) {
        Integer amount = taskService.getTaskAmount(taskId);
        return ResponseEntity.ok(amount);
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/status-distribution")
    public Map<String, Long> getTaskStatusDistribution() {
        return taskService.getTaskStatusDistribution();
    }

    @GetMapping("/status-distribution/by-status")
    public Map<String, Long> getTaskStatusDistributionByStatus(@RequestParam String status) {
        return taskService.getTaskStatusDistributionByStatus(status);
    }

    @GetMapping("/status-distribution/by-date")
    public Map<String, Long> getTaskStatusDistributionByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return taskService.getTaskStatusDistributionByDateRange(startDate, endDate);
    }
    @GetMapping("/durations/taches")
    public ResponseEntity<List<Object[]>> getAllTaskDurations() {
        List<Object[]> taskDurations = taskService.findAllTaskDurations();
        return ResponseEntity.ok().body(taskDurations);
    }
}