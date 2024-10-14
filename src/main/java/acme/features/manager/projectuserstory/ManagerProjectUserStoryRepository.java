
package acme.features.manager.projectuserstory;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.project.ProjectUserStory;
import acme.entities.project.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerProjectUserStoryRepository extends AbstractRepository {

	// ----- ProjectUserStory Queries -----

	@Query("select pus from ProjectUserStory pus where pus.id = :projectUserStoryId")
	ProjectUserStory findProjectUserStoryById(int projectUserStoryId);

	@Query("select pus from ProjectUserStory pus where pus.project.id = :projectId and pus.userStory.id = :userStoryId")
	ProjectUserStory findProjectUserStoryByProjectAndUserStory(int projectId, int userStoryId);

	@Query("select pus from ProjectUserStory pus where pus.project.manager.id = :managerId")
	Collection<ProjectUserStory> findProjectUserStoriesByManagerId(int managerId);

	// ----- Project Queries -----

	@Query("select p from Project p where p.id = :projectId")
	Project findProjectById(int projectId);

	@Query("select p from Project p where p.manager.id = :managerId")
	Collection<Project> findProjectsByManagerId(int managerId);

	@Query("select p from Project p where p.manager.id = :managerId and p.draftMode = true")
	Collection<Project> findDraftProjectsByManagerId(int managerId);

	// ----- UserStory Queries -----

	@Query("select us from UserStory us where us.id = :userStoryId")
	UserStory findUserStoryById(int userStoryId);

	@Query("select us from UserStory us where us.manager.id = :managerId")
	Collection<UserStory> findUserStoriesByManagerId(int managerId);

	@Query("select us from UserStory us where us.draftMode = false")
	Collection<UserStory> findAllPublishedUserStories();

	// ----- Manager Queries -----

	@Query("select m from Manager m where m.id = :managerId")
	Manager findManagerById(int managerId);
}
