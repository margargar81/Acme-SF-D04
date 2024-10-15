
package acme.features.manager.managerdashboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.entities.project.UserStory;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;

@Service
public class ManagerDashboardShowService extends AbstractService<Manager, ManagerDashboard> {

	// Internal state
	@Autowired
	private ManagerDashboardRepository repository;


	// AbstractService interface
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int managerId = super.getRequest().getPrincipal().getActiveRoleId();
		Collection<Project> projects = this.repository.findAllPublishedProjectsByManagerId(managerId);
		Collection<UserStory> userStories = this.repository.findAllPublishedUserStoriesByManagerId(managerId);

		// Initialize statistics collections for project costs
		Map<String, Double> avgProjectCost = new HashMap<>();
		Map<String, Double> devProjectCost = new HashMap<>();
		Map<String, Double> minProjectCost = new HashMap<>();
		Map<String, Double> maxProjectCost = new HashMap<>();

		// Populate project cost statistics if projects exist
		if (!projects.isEmpty()) {
			avgProjectCost = this.convertToMap(this.repository.avgProjectCost(managerId));
			devProjectCost = this.convertToMap(this.repository.devProjectCost(managerId));
			minProjectCost = this.convertToMap(this.repository.minProjectCost(managerId));
			maxProjectCost = this.convertToMap(this.repository.maxProjectCost(managerId));
		}

		// Initialize user story statistics
		int mustUserStories = 0;
		int shouldUserStories = 0;
		int couldUserStories = 0;
		int wontUserStories = 0;

		double avgUserStoryEstimatedCost = Double.NaN;
		double devUserStoryEstimatedCost = Double.NaN;
		double minUserStoryEstimatedCost = Double.NaN;
		double maxUserStoryEstimatedCost = Double.NaN;

		// Populate user story statistics if user stories exist
		if (!userStories.isEmpty()) {
			mustUserStories = this.repository.mustUserStories(managerId);
			shouldUserStories = this.repository.shouldUserStories(managerId);
			couldUserStories = this.repository.couldUserStories(managerId);
			wontUserStories = this.repository.wontUserStories(managerId);

			avgUserStoryEstimatedCost = this.repository.avgUserStoryEstimatedCost(managerId);
			devUserStoryEstimatedCost = this.repository.devUserStoryEstimatedCost(managerId);
			minUserStoryEstimatedCost = this.repository.minUserStoryEstimatedCost(managerId);
			maxUserStoryEstimatedCost = this.repository.maxUserStoryEstimatedCost(managerId);
		}

		// Create and populate the ManagerDashboard object
		ManagerDashboard dashboard = new ManagerDashboard();
		dashboard.setMustUserStories(mustUserStories);
		dashboard.setShouldUserStories(shouldUserStories);
		dashboard.setCouldUserStories(couldUserStories);
		dashboard.setWontUserStories(wontUserStories);
		dashboard.setAvgUserStoryEstimatedCost(avgUserStoryEstimatedCost);
		dashboard.setDevUserStoryEstimatedCost(devUserStoryEstimatedCost);
		dashboard.setMinUserStoryEstimatedCost(minUserStoryEstimatedCost);
		dashboard.setMaxUserStoryEstimatedCost(maxUserStoryEstimatedCost);
		dashboard.setAvgProjectCost(avgProjectCost);
		dashboard.setDevProjectCost(devProjectCost);
		dashboard.setMinProjectCost(minProjectCost);
		dashboard.setMaxProjectCost(maxProjectCost);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard object) {
		Dataset dataset = super.unbind(object, //
			"mustUserStories", "shouldUserStories", "couldUserStories", "wontUserStories",  // 
			"avgUserStoryEstimatedCost", "devUserStoryEstimatedCost", "minUserStoryEstimatedCost", "maxUserStoryEstimatedCost", //
			"avgProjectCost", "devProjectCost", "minProjectCost", "maxProjectCost");

		super.getResponse().addData(dataset);
	}

	// Auxiliary methods
	private Map<String, Double> convertToMap(final Collection<Object[]> objectSet) {
		Map<String, Double> result = new HashMap<>();
		for (Object[] keyValuePair : objectSet) {
			String currency = (String) keyValuePair[0];
			Double statistic = (Double) keyValuePair[1];
			result.put(currency, statistic);
		}
		return result;
	}
}
