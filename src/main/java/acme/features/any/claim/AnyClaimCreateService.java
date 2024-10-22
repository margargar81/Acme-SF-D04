
package acme.features.any.claim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.claim.Claim;

@Service
public class AnyClaimCreateService extends AbstractService<Any, Claim> {

	@Autowired
	protected AnyClaimRepository repository;


	// Authorisation method, allows the action
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	// Loads a new Claim object with the current instantiation moment
	@Override
	public void load() {
		Claim newClaim = new Claim();
		newClaim.setInstantiationMoment(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(newClaim);
	}

	// Binds request data to the Claim object
	@Override
	public void bind(final Claim object) {
		assert object != null;
		super.bind(object, "code", "heading", "description", "department", "email", "link");
	}

	// Validates the Claim object
	@Override
	public void validate(final Claim object) {
		assert object != null;

		// Validates the confirmation checkbox
		if (!super.getBuffer().getErrors().hasErrors("confirm")) {
			boolean confirm = super.getRequest().getData("confirm", boolean.class);
			super.state(confirm, "confirm", "any.claim.form.error.not-confirmed");
		}

		// Validates that the code is unique
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean duplicatedCode = this.repository.findPublishedClaims().stream().anyMatch(e -> e.getCode().equals(object.getCode()));
			super.state(!duplicatedCode, "code", "any.claim.form.error.duplicated-code");
		}
	}

	// Saves the Claim object to the repository
	@Override
	public void perform(final Claim object) {
		assert object != null;
		this.repository.save(object);
	}

	// Unbinds data to display in the response, including a default false for confirmation
	@Override
	public void unbind(final Claim object) {
		assert object != null;
		Dataset dataset = super.unbind(object, "code", "heading", "description", "department", "email", "link");
		dataset.put("confirm", false);
		super.getResponse().addData(dataset);
	}
}
