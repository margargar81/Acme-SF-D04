
package acme.features.any.project;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;

@Repository
public interface AnyProjectRepository extends AbstractRepository {

	/**
	 * Retrieves all projects that are published (i.e., not in draft mode).
	 * 
	 * @return a collection of published projects.
	 */
	@Query("select p from Project p where p.draftMode = false")
	Collection<Project> findPublishedProjects();

	/**
	 * Retrieves a project by its unique identifier.
	 * 
	 * @param id
	 *            the ID of the project.
	 * @return the project entity, or null if not found.
	 */
	@Query("select p from Project p where p.id = :id")
	Project findProjectById(Integer id);

}
