
package acme.features.authenticated.risk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.risk.Risk;

@Service
public class AuthenticatedRiskShowService extends AbstractService<Authenticated, Risk> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedRiskRepository authenticatedRiskRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Risk risk;

		id = super.getRequest().getData("id", int.class);
		risk = this.authenticatedRiskRepository.findRiskById(id);
		status = risk != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Risk object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.authenticatedRiskRepository.findRiskById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Risk object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "reference", "identificationDate", "impact", "probability", "description", "optionalLink");
		dataset.put("estimatedValue", object.getValue());

		super.getResponse().addData(dataset);
	}

}
