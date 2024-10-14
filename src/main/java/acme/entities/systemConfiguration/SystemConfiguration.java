
package acme.entities.systemConfiguration;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SystemConfiguration extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	/**
	 * A comma-separated list of accepted currencies, in ISO 4217 format.
	 * Example: "USD,EUR,GBP"
	 */
	@NotNull
	@Pattern(regexp = "^([A-Z]{3},)*[A-Z]{3}$")
	private String				acceptedCurrencies;

	/**
	 * The default system currency in ISO 4217 format.
	 * Example: "USD"
	 */
	@NotNull
	@Pattern(regexp = "^[A-Z]{3}$")
	private String				systemCurrency;
}
