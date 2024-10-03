
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

	@Autowired
	private ManagerUserStoryDeleteService			deleteService;

	@Autowired
	private ManagerUserStoryCreateService			createService;

	@Autowired
	private ManagerUserStoryUpdateService			updateService;

	@Autowired
	private ManagerUserStoryPublishService			publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("list-by-project", "list", this.listByProjectService);
		super.addCustomCommand("list-mine", "list", this.listMineService);
		super.addCustomCommand("publish", "update", this.publishService);

	}
}
