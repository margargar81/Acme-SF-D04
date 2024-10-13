
package acme.entities.objective;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Objective extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			description;

	@NotNull
	protected PriorityType		priority;

	@NotNull
	protected Boolean			criticalStatus;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				startDuration;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				finishDuration;

	@URL
	protected String			optionalLink;

	// Derived attributes ----------------------------------------------------


	@Transient
	public Double getDuration() {

		final Double duration;

		//en milisegundos
		final long diferencia = this.finishDuration.getTime() - this.startDuration.getTime();

		//en dias 
		duration = (double) (diferencia / (1000 * 60 * 60 * 24));

		return duration;
	}

	// Relationships ----------------------------------------------------------

}
