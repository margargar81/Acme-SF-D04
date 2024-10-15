
package acme.features.manager.managerdashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.project.Project;
import acme.entities.project.UserStory;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	// Queries for project cost indicators
	@Query("select p.cost.currency, avg(p.cost.amount) from Project p where p.manager.id = :managerId and p.draftMode = false group by p.cost.currency")
	Collection<Object[]> avgProjectCost(int managerId); // Average project cost by manager

	@Query("select p.cost.currency, stddev(p.cost.amount) from Project p where p.manager.id = :managerId and p.draftMode = false group by p.cost.currency")
	Collection<Object[]> devProjectCost(int managerId); // Standard deviation of project cost by manager

	@Query("select p.cost.currency, min(p.cost.amount) from Project p where p.manager.id = :managerId and p.draftMode = false group by p.cost.currency")
	Collection<Object[]> minProjectCost(int managerId); // Minimum project cost by manager

	@Query("select p.cost.currency, max(p.cost.amount) from Project p where p.manager.id = :managerId and p.draftMode = false group by p.cost.currency")
	Collection<Object[]> maxProjectCost(int managerId); // Maximum project cost by manager

	// Queries for user story priority counts
	@Query("select count(us) from UserStory us where us.manager.id = :managerId and us.draftMode = false and us.priority = acme.entities.project.UserStoryPriority.MUST")
	int mustUserStories(int managerId); // Count of user stories with 'MUST' priority

	@Query("select count(us) from UserStory us where us.manager.id = :managerId and us.draftMode = false and us.priority = acme.entities.project.UserStoryPriority.SHOULD")
	int shouldUserStories(int managerId); // Count of user stories with 'SHOULD' priority

	@Query("select count(us) from UserStory us where us.manager.id = :managerId and us.draftMode = false and us.priority = acme.entities.project.UserStoryPriority.COULD")
	int couldUserStories(int managerId); // Count of user stories with 'COULD' priority

	@Query("select count(us) from UserStory us where us.manager.id = :managerId and us.draftMode = false and us.priority = acme.entities.project.UserStoryPriority.WONT")
	int wontUserStories(int managerId); // Count of user stories with 'WONT' priority

	// Queries for user story cost indicators
	@Query("select avg(us.estimatedCost) from UserStory us where us.manager.id = :managerId and us.draftMode = false")
	double avgUserStoryEstimatedCost(int managerId); // Average estimated cost of user stories

	@Query("select stddev(us.estimatedCost) from UserStory us where us.manager.id = :managerId and us.draftMode = false")
	double devUserStoryEstimatedCost(int managerId); // Standard deviation of user story estimated cost

	@Query("select min(us.estimatedCost) from UserStory us where us.manager.id = :managerId and us.draftMode = false")
	double minUserStoryEstimatedCost(int managerId); // Minimum estimated cost of user stories

	@Query("select max(us.estimatedCost) from UserStory us where us.manager.id = :managerId and us.draftMode = false")
	double maxUserStoryEstimatedCost(int managerId); // Maximum estimated cost of user stories

	// Queries for retrieving projects and user stories
	@Query("select p from Project p where p.manager.id = :managerId and p.draftMode = false")
	Collection<Project> findAllPublishedProjectsByManagerId(int managerId); // Retrieve all published projects by manager

	@Query("select us from UserStory us where us.manager.id = :managerId and us.draftMode = false")
	Collection<UserStory> findAllPublishedUserStoriesByManagerId(int managerId); // Retrieve all published user stories by manager

}
