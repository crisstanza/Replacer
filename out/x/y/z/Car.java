package x.y.z;

/**
 * @author 
 */
public final class Car {

	private Integer id;
	private Date date;

	private Car() {
	}

	public final Integer getId() {
		return this.id;
	}
	public final void setId(final Integer id) {
		this.id = id;
	}
	public final Date getDate() {
		return this.date;
	}
	public final void setDate(final Date date) {
		this.date = date;
	}

}
