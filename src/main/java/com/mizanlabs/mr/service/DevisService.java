package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.*;
import com.mizanlabs.mr.repository.DevisRepository;

import com.mizanlabs.mr.repository.ElementDevisRepository;
import com.mizanlabs.mr.repository.ProjectRepository;
import com.mizanlabs.mr.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DevisService {
	private final DevisRepository devisRepository;
	@Autowired
	private TaskService taskService;
	private ProjectRepository projectRepository;
	private TaskRepository taskRepository;
	private ElementDevisRepository elementDevisRepository;
	private ElementDevisService elementDevisService;
	@Autowired
	public DevisService(ElementDevisService elementDevisService,ElementDevisRepository elementDevisRepository,TaskRepository taskRepository,DevisRepository devisRepository,ProjectRepository projectRepository) {
		this.devisRepository = devisRepository;
		this.projectRepository = projectRepository;
		this.taskRepository = taskRepository;
		this.elementDevisRepository = elementDevisRepository;
		this.elementDevisService = elementDevisService;

	}

	// Renamed from findAllContacts to match the controller
	public List<Devis> getAllDevis() {
		return devisRepository.findAll();
	}



	public List<Devis> getDevisByProjectId(Long projectId) {
		return devisRepository.findByProject_ProjectId(projectId);
	}
	// Renamed from findContactById to match the controller
	public Optional<Devis> getDevisById(Long id) {
		return devisRepository.findById(id);
	}

	// Renamed from saveContact to match the controller
	public Devis createDevis(Devis devis) {
		return devisRepository.save(devis);
	}

	// Renamed from updateContact and changed return type to Optional<Contact> to match the controller
	public Optional<Devis> updateDevis(Long id, Devis devisDetails) {
		return devisRepository.findById(id).map(devis -> {
			devis.setDiscount(devisDetails.getDiscount());
			devis.setStatus(devisDetails.getStatus());
			devis.setCreationDate(devisDetails.getCreationDate());
			devis.setNote(devisDetails.getNote());
			devis.setDiscountp(devisDetails.getDiscountp());
			devis.setTVA(devisDetails.getTVA());
			devis.setTva_present(devisDetails.getTva_present());
			devis.setMontant(devisDetails.getMontant());
			devis.setMP1(devisDetails.getMP1());
			devis.setMP2(devisDetails.getMP2());
			devis.setMP3(devisDetails.getMP3());
			devis.setMP4(devisDetails.getMP4());
			devis.setMP5(devisDetails.getMP5());
			devis.setPMP1(devisDetails.getPMP1());
			devis.setPMP2(devisDetails.getPMP2());
			devis.setPMP3(devisDetails.getPMP3());
			devis.setPMP4(devisDetails.getPMP4());
			devis.setPMP5(devisDetails.getPMP5());
			devis.setCreationDatedemande(devisDetails.getCreationDatedemande());
//				devis.setMontant_ht(devisDetails.getMontant_ht());
//				devis.setMontant_remise(devisDetails.getMontant_ht());
			// Set other fields...
			return devisRepository.save(devis);
		});
	}
//	public Optional<Devis> updateDevismount(Long id, Devis devisDetails) {
//		return devisRepository.findById(id).map(devis -> {
//			devis.setMontant_TTC(devisDetails.getMontant_TTC());
//			devis.setMontant_ht(devisDetails.getMontant_ht());
//			devis.setMontant_remise(devisDetails.getMontant_remise());
//			// Set other fields...
//			return devisRepository.save(devis);
//		});
//	}

	// Changed to return a boolean to match the controller's expectation
	public boolean deleteDevis(Long id) {
		boolean exists = devisRepository.existsById(id);
		if (!exists) {
			return false;
		}
		devisRepository.deleteById(id);
		return true;
	}

	public double calculateDevisTotal(Long devisId) {
		Devis devis = devisRepository.findById(devisId)
				.orElseThrow(() -> new EntityNotFoundException("Devis not found with id: " + devisId));
		double devisTotal = 0;
		for (Task task : devis.getTasks()) {
			devisTotal += taskService.calculateTaskTotal(task.getTaskId());
		}
		return devisTotal;
	}

	public List<Devis> getDevisByProject(Project project) {
		return devisRepository.findByProject(project);
	}


	public String findMaxRefDevisByYear(String year) {
		return devisRepository.findMaxRefDevisByYear(year);
	}

	public String generateDevisReference(String annee) {
		// Votre logique pour générer refProjet, peut-être quelque chose comme ci-dessous
		String yearShort =annee.substring(2);
		String maxRefDevis = devisRepository.findMaxRefDevisByYear(annee);
		if (maxRefDevis == null || maxRefDevis.length()==0) {
			maxRefDevis = "DV000"+yearShort;

		}
		String numSeq = maxRefDevis.substring(2, 5);
		int numSeqInt = Integer.parseInt(numSeq) + 1;
		String refDevis = "DV" + String.format("%03d", numSeqInt) + '-' + yearShort;
		// project.setRefProjet(refProjet);
		return refDevis;
	}
	public Devis copierDevis(Long devisId) {
		Devis original = devisRepository.findById(devisId).orElseThrow(() -> new RuntimeException("Devis not found"));
		Devis copie = new Devis();
		copie.setCreationDate(original.getCreationDate());
		copie.setMontant(original.getMontant());
		copie.setDiscount(original.getDiscount());
		copie.setDiscountp(original.getDiscountp());
		copie.setTVA(original.getTVA());
		copie.setTva_present(original.getTva_present());
		copie.setNote(original.getNote());
		copie.setStatus(original.getStatus());
		copie.setAnnee(original.getAnnee());
		copie.setProject(original.getProject());
		copie.setMP1(original.getMP1());
		copie.setMP2(original.getMP2());
		copie.setMP3(original.getMP3());
		copie.setMP4(original.getMP4());
		copie.setMP5(original.getMP5());
		copie.setPMP1(original.getPMP1());
		copie.setPMP2(original.getPMP2());
		copie.setPMP3(original.getPMP3());
		copie.setPMP4(original.getPMP4());
		copie.setPMP5(original.getPMP5());
		copie.setDatedemarage(original.getDatedemarage());
		copie.setRemiserapport(original.getRemiserapport());
		copie.setCreationDatedemande(original.getCreationDatedemande());
//	        copie.setTotalDevis(original.getTotalDevis());
		return copie;
	}


	public ResponseEntity<Object> collerDevis(Long devisId, Long ProjetId) {

		Project nouveauProjet = projectRepository.findByProjectId(ProjetId);
		if (nouveauProjet == null) {
			throw new RuntimeException("Project not found");
		}

		Devis copie = copierDevis(devisId);

		copie.setProject(nouveauProjet);
		String nouvelleRef = generateDevisReference(copie.getAnnee());

		copie.setRef_devis(nouvelleRef);

		devisRepository.save(copie);
		Set<Task> tasksCopiees = copier_tache(devisId);
		coller_tache(copie.getDevisId(), tasksCopiees);
		return ResponseEntity.ok().body(Map.of("message", "Success"));
	}


	public Set<Task> copier_tache(Long devisId) {
		Devis original = devisRepository.findById(devisId)
				.orElseThrow(() -> new RuntimeException("Devis not found with id " + devisId));
		return new HashSet<>(original.getTasks());
	}public Set<Task> coller_tache(Long devisId, Set<Task> tasksCopiees) {
		Devis cible = devisRepository.findById(devisId)
				.orElseThrow(() -> new RuntimeException("Devis not found with id " + devisId));

		Set<Task> nouvellesTaches = new HashSet<>();
		Long projectId = cible.getProject().getProjectId();

		for (Task task : tasksCopiees) {
			Task nouvelleTask = new Task();
			nouvelleTask.setTaskName(task.getTaskName());
			String newRefTask = taskService.getMaxRefTaskForProject(projectId);
			nouvelleTask.setRefTask(newRefTask);
			nouvelleTask.setDeadline(task.getDeadline());
			nouvelleTask.setStart(task.getStart());
			nouvelleTask.setPriority(task.getPriority());
			nouvelleTask.setStatus(task.getStatus());
			nouvelleTask.setProject(cible.getProject());
//	        nouvelleTask.setMontant(task.getMontant());
			nouvelleTask.setNote(task.getNote());
			nouvelleTask.setDevis(cible);
			nouvelleTask.setTotalTask(task.getTotalTask());

			nouvellesTaches.add(nouvelleTask);
			taskRepository.save(nouvelleTask);

			Set<ElementDevis> elementsDevisCopies = taskService.findElementDevisByTaskId(task.getTaskId());
			if (!elementsDevisCopies.isEmpty()) {
				Set<ElementDevis> newelementsDevis = coller_Edevis(nouvelleTask.getTaskId(), elementsDevisCopies);
				nouvelleTask.setElementDevis(newelementsDevis);
			}
		}

		return nouvellesTaches;
	}

	public Set<ElementDevis> coller_Edevis(Long taskId, Set<ElementDevis> elementsDevisCopies) {
		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));

		Set<ElementDevis> nouveauxElementsDevis = new HashSet<>();

		for (ElementDevis elementDevis : elementsDevisCopies) {
			ElementDevis nouvelElementDevis = new ElementDevis();
			nouvelElementDevis.setName(elementDevis.getName());
			nouvelElementDevis.setElementNote(elementDevis.getElementNote());
			nouvelElementDevis.setPrix_unitaire(elementDevis.getPrix_unitaire());
			nouvelElementDevis.setMontant(elementDevis.getMontant());
			nouvelElementDevis.setUnite(elementDevis.getUnite());
			nouvelElementDevis.setRefEdevis(elementDevisService.getMaxRefEdevisForTask(taskId));
			nouvelElementDevis.setType(elementDevis.getType());
			nouvelElementDevis.setElementQty(elementDevis.getElementQty());
			nouvelElementDevis.setNbreLots(elementDevis.getNbreLots());
			nouvelElementDevis.setqteLots(elementDevis.getqteLots());
			nouvelElementDevis.setStatus(elementDevis.getStatus());
			nouvelElementDevis.setTask(task);

			nouveauxElementsDevis.add(nouvelElementDevis);
			elementDevisRepository.save(nouvelElementDevis);
		}

		return nouveauxElementsDevis;
	}



	public Integer getTotalTasksByDevisId(Long devisId) {
		return devisRepository.findTotalTasksByDevisId(devisId);
	}

//	    public void updateDevisMontant(Long devisId) {
//	        Integer totalTasks = getTotalTasksByDevisId(devisId);
//	        devisRepository.updateDevisMontant(devisId, totalTasks);
//	    }

	//	    public void updateDevisMontant(Long devisId, Integer taskAmount) {
//	        Devis devis = devisRepository.findById(devisId)
//	                .orElseThrow(() -> new RuntimeException("Devis not found"));
//
//	        Integer totalTaskAmount = taskRepository.sumTotalTaskByDevisId(devisId);
//	        if (totalTaskAmount == null) {
//	            totalTaskAmount = 0;
//	        }
//
//	        devis.setMontant(totalTaskAmount + taskAmount);
//
//	        devisRepository.save(devis);
//	    }
//	}
	@Transactional
	public void updateDevisMontant(Long devisId, Integer taskAmount) {
		Devis devis = devisRepository.findById(devisId)
				.orElseThrow(() -> new RuntimeException("Devis not found"));

		Integer totalTaskAmount = taskRepository.sumTotalTaskByDevisId(devisId);
		if (totalTaskAmount == null) {
			totalTaskAmount = 0;
		}

		Integer montant = totalTaskAmount + taskAmount;

		Integer montantTva = montant;
		if (devis.getTVA() != null && devis.getTVA() > 0) {
			montantTva = montant + (montant * devis.getTVA()) / 100;
		}

		Integer montantRemise ;
		if (devis.getDiscount() != null && devis.getDiscount()>0) {
			montantRemise = montant - devis.getDiscount();
			devis.setMontantRemise(montantRemise);

		}
		else if (devis.getDiscountp() != null && devis.getDiscountp()>0) {
			montantRemise = montant - (montant * devis.getDiscountp()) / 100;
			devis.setMontantRemise(montantRemise);

		}

		devis.setMontant(montant);
		devis.setMontantTva(montantTva);

		devisRepository.save(devis);
	}
	@Transactional
	public void updateDevisMontant1(Long devisId, Integer taskAmount) {
		Devis devis = devisRepository.findById(devisId)
				.orElseThrow(() -> new RuntimeException("Devis not found"));

		Integer totalTaskAmount = taskRepository.sumTotalTaskByDevisId(devisId);
		if (totalTaskAmount == null) {
			totalTaskAmount = 0;
		}

		Integer montant = totalTaskAmount - taskAmount;

		Integer montantTva = montant;
		if (devis.getTVA() != null && devis.getTVA() > 0) {
			montantTva = montant + (montant * devis.getTVA()) / 100;
		}

		Integer montantRemise ;
		if (devis.getDiscount() != null && devis.getDiscount()>0) {
			montantRemise = montant - devis.getDiscount();
			devis.setMontantRemise(montantRemise);

		}
		else if (devis.getDiscountp() != null && devis.getDiscountp()>0) {
			montantRemise = montant - (montant * devis.getDiscountp()) / 100;
			devis.setMontantRemise(montantRemise);

		}

		devis.setMontant(montant);
		devis.setMontantTva(montantTva);

		devisRepository.save(devis);
	}
//	public String getDevisDetails(Long devisId) {
//		Devis devis = devisRepository.findById(devisId).orElse(null);
//
//		if (devis == null) {
//			return null;
//		}
//
//		String refDevis = devis.getRef_devis();
//		String projectTitle = devis.getProject().getTitle();
//		String clientName = devis.getProject().getClient().getName();
//
//		return refDevis + " - " + projectTitle + " - " + clientName;
//	}
public DeviProjectClientrefDTO getDevisDetails(Long devisId) {
	Devis devis = devisRepository.findById(devisId).orElse(null);

	if (devis == null) {
		return null;
	}

	String refDevis = devis.getRef_devis();
	String projectTitle = devis.getProject().getTitle();
	String clientName = devis.getProject().getClient().getName();

	return new DeviProjectClientrefDTO(refDevis, projectTitle, clientName);
}
	public Map<String, Long> getDevisStatusDistribution() {
		List<Object[]> results = devisRepository.getDevisStatusDistribution();
		Map<String, Long> distributionMap = new HashMap<>();

		for (Object[] result : results) {
			String statusName = (String) result[0];
			Long count = (Long) result[1];
			distributionMap.put(statusName, count);
		}

		return distributionMap;
	}
	public ProjectClientDTO getProjectAndClientNames(Long devisId, Long projectId) {
		Devis devis = devisRepository.findById(devisId).orElseThrow(() -> new RuntimeException("Devis not found"));
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));

		if (!devis.getProject().getProjectId().equals(project.getProjectId())) {
			throw new RuntimeException("Devis does not belong to the specified project");
		}

		String projectName = project.getTitle();
		String clientName = project.getClient().getName();

		return new ProjectClientDTO(projectName, clientName);
	}
	public Project getProjectById(Long projectId) {
		return projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
	}
}