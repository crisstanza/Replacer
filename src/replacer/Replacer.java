package replacer;

import java.io.File;

import model.Car;
import model.Pessoa;
import replacer.utils.UtilsReplacer;

/**
 * @author crisstanza
 */
public final class Replacer {

	private final Class<?>[] entities = { //
			Pessoa.class, Car.class //
	};

	private final UtilsReplacer utils;
	private final File[] filesIn;

	public Replacer() {
		this.utils = UtilsReplacer.getInstance();
		this.filesIn = UtilsReplacer.getInstance().filesIn();
	}

	public final void start() {
		for (final File file : this.filesIn) {
			for (final Class<?> entity : entities) {
				final String fileContents = utils.readFrom(file);
				final String fileContentsReplaced = utils.replace(fileContents, entity);
				utils.writeTo(file, entity, fileContentsReplaced);
			}
		}
	}

}
