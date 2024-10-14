
package acme.features.any.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;

@Service
public class AnyProjectShowService extends AbstractService<Any, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	/**
	 * Authorises the request to view a project.
	 * The project must exist and not be in draft mode.
	 */
	@Override
	public void authorise() {
		boolean isAuthorised = false;
		Integer projectId = super.getRequest().getData("id", Integer.class);
		Project project = this.repository.findProjectById(projectId);

		if (project != null && !project.isDraftMode())
			isAuthorised = true;

		super.getResponse().setAuthorised(isAuthorised);
	}

	/**
	 * Loads the project data into the buffer.
	 */
	@Override
	public void load() {
		Project project;
		Integer projectId = super.getRequest().getData("id", Integer.class);
		project = this.repository.findProjectById(projectId);

		super.getBuffer().addData(project);
	}

	/**
	 * Unbinds the project data and prepares it for the response.
	 *
	 * @param project
	 *            The project to unbind.
	 */
	@Override
	public void unbind(final Project project) {
		assert project != null;

		Dataset dataset;
		dataset = super.unbind(project, "code", "title", "projectAbstract", "cost", "link");

		super.getResponse().addData(dataset);
	}

}
