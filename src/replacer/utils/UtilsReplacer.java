package replacer.utils;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class UtilsReplacer {

	private static final String IN = "in/";
	private static final String OUT = "out/";

	private static final String template_dot = "template.";
	private static final char DOT = '.';
	private static final char COMMA = ';';

	private static final String PACKAGE_ = "package ";

	private static final String FIELDS = "{CAMPOS}" + System.lineSeparator();
	private static final String FIELDS_END = "{\\CAMPOS}" + System.lineSeparator();
	private static final String FIELD = "{CAMPO}";
	private static final String FIELD_type = "{CAMPO_TIPO}";
	private static final String FIELD_camel = "{CAMPO:camel}";
	private static final String FIELD_upper = "{CAMPO:upper}";
	private static final String FIELD_lower = "{CAMPO:lower}";

	private static final String ENTIDADE = "{ENTIDADE}";
	private static final String ENTIDADE_camel = "{ENTIDADE:camel}";
	private static final String ENTIDADE_upper = "{ENTIDADE:upper}";
	private static final String ENTIDADE_lower = "{ENTIDADE:lower}";

	private static final String _ENTITY_camel = "_ENTIDADE-camel_";

	private static final UtilsReplacer instance = new UtilsReplacer();

	private UtilsReplacer() {
	}

	public static final UtilsReplacer getInstance() {
		return instance;
	}

	public final File[] filesIn() {
		return new File(UtilsReplacer.IN).listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return !file.isHidden() && file.getName().contains(template_dot);
			}
		});
	}

	public final void writeTo(final File file, Class<?> clazz, final String contents) {
		try {
			final Path path = Paths.get(UtilsReplacer.OUT + this.fixName(file.getName(), clazz.getSimpleName(), contents));
			Files.createDirectories(path.getParent());
			Files.write(path, contents.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (final Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	public String readFrom(final File file) {
		try {
			return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
		} catch (final Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	private final String fixName(final String name, final String entityName, final String contents) {
		final String packageName;
		if (contents.startsWith(PACKAGE_)) {
			final int comma = contents.indexOf(COMMA);
			final String packageNameWithDots = contents.substring(PACKAGE_.length(), comma);
			packageName = packageNameWithDots.replace(DOT, File.separatorChar) + File.separatorChar;
		} else {
			packageName = "";
		}
		return packageName + replaceFileName(entityName, name);
	}

	private final String replaceFileName(final String entityName, final String name) {
		return name.replace(template_dot, "").replace(_ENTITY_camel, this.camelCase(entityName));
	}

	public final String replace(final String fileContents, final Class<?> clazz) {
		String newFileContents = fileContents;
		String entity = clazz.getSimpleName();
		newFileContents = newFileContents.replace(ENTIDADE, entity);
		newFileContents = newFileContents.replace(ENTIDADE_lower, entity.toLowerCase());
		newFileContents = newFileContents.replace(ENTIDADE_upper, entity.toUpperCase());
		newFileContents = newFileContents.replace(ENTIDADE_camel, this.camelCase(entity));
		while (true) {
			final int[] start_end = new int[2];
			final String fieldTemplate = this.getFieldTemplate(newFileContents, start_end);
			if (start_end[0] >= 0) {
				final String[] parts = new String[2];
				parts[0] = newFileContents.substring(0, start_end[0]);
				parts[1] = newFileContents.substring(start_end[1], newFileContents.length());
				final StringBuilder fields = new StringBuilder();
				final Field[] classFields = clazz.getDeclaredFields();
				for (final Field classField : classFields) {
					final String classFieldName = classField.getName();
					fields.append(fieldTemplate//
							.replace(FIELD, classFieldName)//
							.replace(FIELD_lower, classFieldName.toLowerCase())//
							.replace(FIELD_upper, classFieldName.toUpperCase())//
							.replace(FIELD_camel, this.camelCase(classFieldName))//
							.replace(FIELD_type, classField.getType().getSimpleName())//
					);
				}
				newFileContents = parts[0] + fields.toString() + parts[1];
			} else {
				break;
			}
		}
		return newFileContents;
	}

	private final String camelCase(final String str) {
		if (str == null) {
			return null;
		} else if (str.length() <= 0) {
			return "";
		} else if (str.length() == 1) {
			return str.toUpperCase();
		} else {
			return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
		}
	}

	private final String getFieldTemplate(final String str, final int[] start_end) {
		start_end[0] = str.indexOf(FIELDS);
		if (start_end[0] >= 0) {
			start_end[1] = str.indexOf(FIELDS_END);
			final String template = str.substring(start_end[0] + FIELDS.length(), start_end[1]);
			start_end[1] += FIELDS_END.length();
			return template;
		} else {
			return "";
		}
	}

}
