
package acme.features.manager.project;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.project.ProjectUserStory;
import acme.entities.project.UserStory;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Manager;

@Repository
public interface ManagerProjectRepository extends AbstractRepository {

	// Project Queries
	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select p from Project p where p.manager.id = :managerId")
	Collection<Project> findProjectsByManagerId(int managerId);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select p from Project p where p.code = :code")
	Optional<Project> findOneProjectByCode(String code);

	// Manager Queries
	@Query("select m from Manager m where m.id = :managerId")
	Manager findOneManagerById(int managerId);

	// User Story Queries
	@Query("select pus.userStory from ProjectUserStory pus where pus.project.id = :projectId")
	Collection<UserStory> findUserStoriesByProjectId(int projectId);

	@Query("select us from UserStory us where us.manager.id = :managerId")
	Collection<UserStory> findUserStoriesByManagerId(int managerId);

	// Project-User Story Relationship Queries
	@Query("select pus from ProjectUserStory pus where pus.project.id = :projectId")
	Collection<ProjectUserStory> findProjectUserStoryByProjectId(int projectId);

	// System Configuration Queries
	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration findActualSystemConfiguration();
}
