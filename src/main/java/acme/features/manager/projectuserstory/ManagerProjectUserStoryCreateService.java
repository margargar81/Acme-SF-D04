
package acme.features.manager.projectuserstory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.project.ProjectUserStory;
import acme.entities.project.UserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectUserStoryCreateService extends AbstractService<Manager, ProjectUserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		//Logged User must be a manager

		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProjectUserStory object;

		object = new ProjectUserStory();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final ProjectUserStory object) {
		assert object != null;

		super.bind(object, "project", "userStory");
	}

	@Override
	public void validate(final ProjectUserStory object) {

		//There must not be duplicate relationships, meaning the same project and user story
		//Assignments cannot be created for already published projects
		//Assignments cannot be created for projects belonging to other managers
		//Assignments cannot be created with unpublished user stories from other managers

		assert object != null;
		Project project;
		UserStory userStory;
		Manager manager;

		project = object.getProject();
		userStory = object.getUserStory();
		manager = this.repository.findManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		if (!super.getBuffer().getErrors().hasErrors("project")) {
			ProjectUserStory existing;

			if (userStory != null) {
				existing = this.repository.findProjectUserStoryByProjectAndUserStory(project.getId(), userStory.getId());
				super.state(existing == null, "*", "manager.project-user-story.form.error.existing-assignment");
			}

			super.state(project.isDraftMode(), "project", "manager.project-user-story.form.error.create-assignment-published-project");

			super.state(project.getManager().equals(manager), "*", "manager.project-user-story.form.error.wrong-manager-project");
		}

		if (!super.getBuffer().getErrors().hasErrors("userStory") && userStory != null)
			super.state(userStory.getManager().equals(manager) || !userStory.isDraftMode(), "userStory", "manager.project-user-story.form.error.wrong-manager-userStory");
	}

	@Override
	public void perform(final ProjectUserStory object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final ProjectUserStory object) {
		assert object != null;

		int managerId;
		Collection<Project> projects;
		Set<UserStory> userStories;
		Collection<UserStory> publishedUserStories;
		Collection<UserStory> myUserStories;
		SelectChoices projectChoices;
		SelectChoices userStoryChoices;
		Dataset dataset;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();

		projects = this.repository.findDraftProjectsByManagerId(managerId);

		publishedUserStories = this.repository.findAllPublishedUserStories();
		myUserStories = this.repository.findUserStoriesByManagerId(managerId);
		userStories = new HashSet<>(publishedUserStories);
		userStories.addAll(myUserStories);

		projectChoices = SelectChoices.from(projects, "title", object.getProject());
		userStoryChoices = SelectChoices.from(userStories, "title", object.getUserStory());

		dataset = super.unbind(object, "project", "userStory");
		dataset.replace("project", projectChoices.getSelected().getKey());
		dataset.put("projectChoices", projectChoices);
		dataset.replace("userStory", userStoryChoices.getSelected().getKey());
		dataset.put("userStoryChoices", userStoryChoices);

		super.getResponse().addData(dataset);
	}

}
