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
