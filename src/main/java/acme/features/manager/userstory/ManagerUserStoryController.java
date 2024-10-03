
package acme.features.manager.userstory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.project.UserStory;
import acme.roles.Manager;

@Controller
public class ManagerUserStoryController extends AbstractController<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryShowService				showService;

	@Autowired
	private ManagerUserStoryListByProjectService	listByProjectService;

	@Autowired
	private ManagerUserStoryListMineService			listMineService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);

		super.addCustomCommand("list-by-project", "list", this.listByProjectService);
		super.addCustomCommand("list-mine", "list", this.listMineService);

	}
}
