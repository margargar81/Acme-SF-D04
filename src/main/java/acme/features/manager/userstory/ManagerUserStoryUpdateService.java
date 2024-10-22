
package acme.features.manager.userstory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.UserStory;
import acme.entities.project.UserStoryPriority;
import acme.roles.Manager;

@Service
public class ManagerUserStoryUpdateService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {

		//Stories must me linked to the corresponded manager
		//Only UserStories in DraftMode can be updated

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

		//The cost must be > 0

		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("estimatedCost")) {
			int ec;
			ec = object.getEstimatedCost();
			super.state(ec > 0, "estimatedCost", "manager.user-story.form.error.negative-estimated-cost");
		}
	}

	@Override
	public void perform(final UserStory object) {
		assert object != null;

		this.repository.save(object);
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
