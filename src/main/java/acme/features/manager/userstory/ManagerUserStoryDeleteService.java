
package acme.features.manager.userstory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.ProjectUserStory;
import acme.entities.project.UserStory;
import acme.entities.project.UserStoryPriority;
import acme.roles.Manager;

@Service
public class ManagerUserStoryDeleteService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		//Stories must me linked to the corresponded manager
		//Only UserStories in DraftMode can be deleted

		boolean status;
		UserStory us;
		Manager manager;

		us = this.repository.findOneUserStoryById(super.getRequest().getData("id", int.class));
		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		status = super.getRequest().getPrincipal().hasRole(Manager.class) && us.getManager().equals(manager) && us.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		UserStory object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneUserStoryById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;

		super.bind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link");
	}

	@Override
	public void validate(final UserStory object) {
		assert object != null;
	}

	@Override
	public void perform(final UserStory object) {

		//Delete Relations of the User Story

		assert object != null;

		Collection<ProjectUserStory> pus = this.repository.findProjectUserStoryByUserStoryId(object.getId());

		this.repository.deleteAll(pus);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;

		SelectChoices priorities;
		Dataset dataset;
		boolean isMine;
		UserStory us;
		Manager manager;

		us = this.repository.findOneUserStoryById(super.getRequest().getData("id", int.class));
		manager = this.repository.findOneManagerById(super.getRequest().getPrincipal().getActiveRoleId());

		isMine = us.getManager().equals(manager);

		priorities = SelectChoices.from(UserStoryPriority.class, object.getPriority());

		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link", "draftMode");
		dataset.put("priorities", priorities);
		dataset.put("isMine", isMine);

		super.getResponse().addData(dataset);
	}

}
