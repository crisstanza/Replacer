package x.y.z;

/**
 * @author 
 */
public final class Pessoa {

	private Integer intValue;
	private String stringValue;
	private Date dateValue;
	private Boolean booleanValue;

	private Pessoa() {
	}

	public final Integer getIntValue() {
		return this.intValue;
	}
	public final void setIntValue(final Integer intValue) {
		this.intValue = intValue;
	}
	public final String getStringValue() {
		return this.stringValue;
	}
	public final void setStringValue(final String stringValue) {
		this.stringValue = stringValue;
	}
	public final Date getDateValue() {
		return this.dateValue;
	}
	public final void setDateValue(final Date dateValue) {
		this.dateValue = dateValue;
	}
	public final Boolean getBooleanValue() {
		return this.booleanValue;
	}
	public final void setBooleanValue(final Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

}

