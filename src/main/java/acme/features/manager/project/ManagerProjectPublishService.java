
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.entities.project.UserStory;
import acme.entities.systemConfiguration.SystemConfiguration;
import acme.roles.Manager;

@Service
public class ManagerProjectPublishService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		/*
		 * Authorise only if:
		 * - The logged user is a Manager.
		 * - The project belongs to the logged manager.
		 * - The project is in draft mode.
		 */
		boolean status;
		Project project;
		Manager manager;

		int projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(projectId);
		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		status = super.getRequest().getPrincipal().hasRole(Manager.class) && project.getManager().equals(manager) && project.isDraftMode();

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

		// Ensure project code is unique.
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			int projectId = super.getRequest().getData("id", int.class);
			boolean isDuplicatedCode = this.repository.findAllProjects().stream().filter(existingProject -> existingProject.getId() != projectId).anyMatch(existingProject -> existingProject.getCode().equals(project.getCode()));

			super.state(!isDuplicatedCode, "code", "manager.project.form.error.duplicated-code");
		}

		// Ensure cost is non-negative and uses an accepted currency.
		if (!super.getBuffer().getErrors().hasErrors("cost")) {
			Double amount = project.getCost().getAmount();
			super.state(amount >= 0, "cost", "manager.project.form.error.negative-cost");

			SystemConfiguration systemConfig = this.repository.findActualSystemConfiguration();
			String currency = project.getCost().getCurrency();
			super.state(systemConfig.getAcceptedCurrencies().contains(currency), "cost", "manager.project.form.error.not-accepted-currency");
		}

		// Check if the project has a fatal error (indication).
		if (!super.getBuffer().getErrors().hasErrors("indication"))
			super.state(!project.isIndication(), "indication", "manager.project.form.error.has-fatal-error");

		// Validate if the project has at least one user story and all are published.
		Collection<UserStory> userStories = this.repository.findUserStoriesByProjectId(project.getId());
		int totalUserStories = userStories.size();
		boolean allUserStoriesPublished = userStories.stream().allMatch(userStory -> !userStory.isDraftMode());

		super.state(totalUserStories >= 1, "*", "manager.project.form.error.not-enough-user-stories");
		super.state(allUserStoriesPublished, "*", "manager.project.form.error.not-all-user-stories-published");
	}

	@Override
	public void perform(final Project project) {
		assert project != null;
		project.setDraftMode(false);
		this.repository.save(project);
	}

	@Override
	public void unbind(final Project project) {
		assert project != null;
		Dataset dataset = super.unbind(project, "code", "title", "projectAbstract", "link", "cost", "indication", "draftMode");
		super.getResponse().addData(dataset);
	}
}
