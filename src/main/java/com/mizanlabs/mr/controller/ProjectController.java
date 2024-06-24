package com.mizanlabs.mr.controller;
import com.mizanlabs.mr.entities.Client;
import com.mizanlabs.mr.entities.Project;
import com.mizanlabs.mr.service.ClientService;
import com.mizanlabs.mr.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/Project")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ProjectController {
    private final ProjectService ProjectService ;
    @Autowired
    private ClientService clientService;
    @Autowired
    public ProjectController(ProjectService ProjectService) {
        this.ProjectService = ProjectService;
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        // Check if the project has a client with an ID
    	boolean titleAvailable = ProjectService.isProjectTitleAvailable(project.getTitle());
        boolean refProjetAvailable = ProjectService.isRefProjetAvailable(project.getRefProjet());

        // Combinez les vérifications pour générer un message d'erreur plus spécifique
        if (!titleAvailable && !refProjetAvailable) {
            return ResponseEntity
                .badRequest()
                .body("Le titre et la référence du projet sont déjà utilisés. Veuillez choisir d'autres valeurs.");
        } else if (!titleAvailable) {
            return ResponseEntity
                .badRequest()
                .body("Un projet avec ce nom existe déjà. Veuillez choisir un autre nom.");
        } else if (!refProjetAvailable) {
            return ResponseEntity
                .badRequest()
                .body("Cette référence de projet est déjà utilisée. Veuillez en choisir une autre.");
        }
        if (project.getClient() != null && project.getClient().getId() != null) {
            // If so, fetch the client by ID and set it to the project
            clientService.getClientById(project.getClient().getId())
                    .ifPresent(project::setClient);
        }
      
     // Assume generateProjectReference returns the new reference directly
        String newRefProjet = ProjectService.generateProjectReference(project.getAnnee());
        project.setRefProjet(newRefProjet); // Set the new unique reference to the project

        // Now, create the project with the unique reference
        Project savedProject = ProjectService.createProject(project);

        return ResponseEntity.ok(savedProject);

    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {

        Project updatedProject = ProjectService.updateProject(id, projectDetails);
        return ResponseEntity.ok(updatedProject);
    }



    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@GetMapping
    public List<Project> getAllProject() {
        return ProjectService.getAllProject();
    }
    
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
    	
        return ProjectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            boolean isDeleted = ProjectService.deleteProject(id); // Assurez-vous que cette méthode renvoie false en cas d'échec.
            if (isDeleted) {
                return ResponseEntity.ok().build();
            } else {
                // Retournez un BadRequest si le projet ne peut pas être supprimé.
                return ResponseEntity
                        .badRequest()
                        .body("La suppression du projet a échoué. Veuillez d'abord supprimer les tâches et les devis associés au projet.");
            }
        } catch (Exception e) {
            // Gérer d'autres exceptions potentielles ici
            return ResponseEntity
                    .badRequest()
                    .body("La suppression du projet a échoué. Veuillez d'abord supprimer les tâches et les devis associés au projet.");
        }
    }

    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/maxRefProjet/{year}")
    public ResponseEntity<String> getMaxRefProjetByYear(@PathVariable String year) {
        String maxRefProjet = ProjectService.findMaxRefProjetByYear(year);
        return ResponseEntity.ok(maxRefProjet);
    }

//    @PostMapping("/generate-reference")
//    public ResponseEntity<String> generateProjectReference(@RequestBody Project project) {
//        String generatedReference = ProjectService.generateProjectReference(project);
//        return ResponseEntity.ok(generatedReference);
//    }
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/generate-reference/{annee}")
    public String generateProjectReference(@PathVariable String  annee) {
        String generatedReference = ProjectService.generateProjectReference(annee);
        return generatedReference;
    }
    
    
    
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @GetMapping("/{projectId}/client")
    public ResponseEntity<Client> getClientByProjectId(@PathVariable Long projectId) {
        Client client = ProjectService.getClientByProjectId(projectId);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @GetMapping("/contacts/{contactId}/projects")
    public List<Project> getProjectsByContactId(@PathVariable Long contactId) {
        List<Project> projects = ProjectService.findProjectsByContactId(contactId);
      
        return projects;
    }
    
    @GetMapping("/clients/{clientId}/projects")
    public List<Project> getProjectsByClientId(@PathVariable Long clientId) {
        return ProjectService.getProjectsByClientId(clientId);
    }
    
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects(
            @RequestParam(required = false) Long contactId, 
            @RequestParam(required = false) Long clientId) {
        List<Project> projects;
        if (contactId != null) {
            projects = ProjectService.findProjectsByContactId(contactId);
        } else if (clientId != null) {
            projects = ProjectService.getProjectsByClientId(clientId);
        } else {
            projects = ProjectService.getAllProject();
        }
        return ResponseEntity.ok(projects);
    }
    @GetMapping("/status-distribution")
    public Map<String, Long> getProjetStatusDistribution() {
        return ProjectService.getProjetStatusDistribution();
    }
}