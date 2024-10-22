
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.entities.project.ProjectUserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		/*
		 * The logged user must have the Manager role.
		 * The project must belong to the logged manager.
		 * The project must be in draft mode.
		 */
		boolean status;
		Project project;
		Manager manager;

		int projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(projectId);
		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		status = super.getRequest().getPrincipal().getActiveRole() == Manager.class && project.getManager().equals(manager) && project.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int projectId = super.getRequest().getData("id", int.class);
		Project project = this.repository.findOneProjectById(projectId);
		super.getBuffer().addData(project);
	}

	@Override
	public void bind(final Project project) {
		assert project != null;

		super.bind(project, "code", "title", "projectAbstract", "link", "cost", "indication");
	}

	@Override
	public void validate(final Project project) {
		assert project != null;
		// No additional validations required.
	}

	@Override
	public void perform(final Project project) {
		assert project != null;

		// Fetch and delete all user stories linked to the project before deleting the project itself.
		int projectId = project.getId();
		Collection<ProjectUserStory> userStories = this.repository.findProjectUserStoryByProjectId(projectId);

		this.repository.deleteAll(userStories);
		this.repository.delete(project);
	}

	@Override
	public void unbind(final Project project) {
		assert project != null;

		Dataset dataset = super.unbind(project, "code", "title", "projectAbstract", "link", "cost", "indication", "draftMode");
		super.getResponse().addData(dataset);
	}
}
