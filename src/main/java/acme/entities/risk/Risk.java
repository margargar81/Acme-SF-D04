
package acme.entities.risk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Risk extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "R-[0-9]{3}")
	protected String			reference;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	protected Date				identificationDate;

	@Min(0)
	protected Double			impact;

	@Digits(integer = 3, fraction = 2)
	@Min(0)
	@Max(1)
	protected Double			probability;

	@NotBlank
	@Length(max = 100)
	protected String			description;

	@URL
	protected String			optionalLink;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Double getValue() {

		return this.impact * this.probability;

	}

	// Relationships ----------------------------------------------------------

}
