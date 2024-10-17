
package acme.features.administrator.risk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.risk.Risk;

@Service
public class AdministratorRiskUpdateService extends AbstractService<Administrator, Risk> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorRiskRepository administratorRiskRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Risk risk;

		id = super.getRequest().getData("id", int.class);
		risk = this.administratorRiskRepository.findRiskById(id);
		status = risk != null && super.getRequest().getPrincipal().hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Risk object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.administratorRiskRepository.findRiskById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Risk object) {
		assert object != null;

		super.bind(object, "reference", "identificationDate", "impact", "probability", "description", "optionalLink");
	}

	@Override
	public void validate(final Risk object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("reference")) {
			Risk existing;

			existing = this.administratorRiskRepository.findRiskByReferece(object.getReference());
			super.state(existing == null || existing.equals(object), "reference", "administrator.risk.form.error.duplicated");
		}

	}

	@Override
	public void perform(final Risk object) {
		assert object != null;
		if (object.getImpact() == null)
			object.setImpact(0.0);
		if (object.getProbability() == null)
			object.setProbability(0.0);
		this.administratorRiskRepository.save(object);
	}

	@Override
	public void unbind(final Risk object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "reference", "identificationDate", "impact", "probability", "description", "optionalLink");

		super.getResponse().addData(dataset);
	}
}