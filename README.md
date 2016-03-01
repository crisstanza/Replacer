# Replacer
File contents replacer based on Java classes.

## Usage:

	./Replacer.sh (compileIt | clean[Bin|Classes|Out] | runIt | testIt | Apple(Show|Hide)AllFiles)

## Turn this ($ENTITY$.template.java):

	package x.y.z;
	
	/**
	 * @author 
	 */
	public final class $ENTITY$ {
	
	$FIELDS$
		private $FIELD_TYPE$ $FIELD$;
	$\FIELDS$
	
		private $ENTITY$() {
		}
	
	$FIELDS$
		public final $FIELD_TYPE$ get$FIELD-camel$() {
			return this.$FIELD$;
		}
		public final void set$FIELD-camel$(final $FIELD_TYPE$ $FIELD$) {
			this.$FIELD$ = $FIELD$;
		}
	$\FIELDS$
	
	}

### and this (Car.java):

	package model;
	
	import java.util.Date;
	
	public final class Car {
	
		public static final String ID = "id";
	
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

## Or this ($ENTITY_lower$.template.html):

	<html>
	<body>
	
	<h1>$ENTITY$</h1>
	
	<form>
	$FIELDS$
		<input type="$FIELD_TYPE_form$" name="$FIELD$" value="${$ENTITY_lower$.$FIELD$}" />
	$$FIELDS$
	</form>
	
	</body>
	</html>

### and this (Pessoa.java):

	package model;
	
	import java.util.Date;
	
	public final class Pessoa {
	
		protected Integer intValue;
		protected String stringValue;
		protected Date dateValue;
		protected Boolean booleanValue;
	
	}

## Into this (pessoa.html):

	<html>
	<body>
	
	<h1>Pessoa</h1>
	
	<form>
		<input type="text" name="intValue" value="${pessoa.intValue}" />
		<input type="text" name="stringValue" value="${pessoa.stringValue}" />
		<input type="text" name="dateValue" value="${pessoa.dateValue}" />
		<input type="text" name="booleanValue" value="${pessoa.booleanValue}" />
	</form>
	
	</body>
	</html>

### Initial setup:

	ssh-keygen -t rsa -b 4096 -C "Replacer@github"
	cat ~/.ssh/id_rsa.pub

Add the key to GitHub and then...

	mkdir ~/GitHub
	cd ~/GitHub
	git clone git@github.com:crisstanza/Replacer.git
