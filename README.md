# Replacer
File contents replacer based on Java classes.


## Turn this (\_ENTIDADE-camel\_.template.java):

	package x.y.z;

	/**
	 * @author
	 */
	public final class {ENTIDADE} {

	{CAMPOS}
		private {CAMPO_TIPO} {CAMPO};
	{\CAMPOS}

		private {ENTIDADE}() {
		}

	{CAMPOS}
		public final {CAMPO_TIPO} get{CAMPO:camel}() {
			return this.{CAMPO};
		}
		public final void set{CAMPO:camel}(final {CAMPO_TIPO} {CAMPO}) {
			this.{CAMPO} = {CAMPO};
		}
	{\CAMPOS}

	}

### and this (Car.java):

	package model;

	import java.util.Date;

	public final class Car {

		public Integer id;
		public Date date;

	}

## Into this (Car.java):

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
