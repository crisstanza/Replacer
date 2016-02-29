package replacer.utils;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

	private static final String PACKAGE_space = "package ";

	private static final String _FIELDS_ = "_CAMPOS_" + System.lineSeparator();
	private static final String _FIELDS_END_ = "_\\CAMPOS_" + System.lineSeparator();

	private static final String _FIELD_ = "_CAMPO_";
	private static final String _FIELD_TYPE_form = "_CAMPO_TIPO_form";
	private static final String _FIELD_camel_ = "_CAMPO-camel_";
	private static final String _FIELD_upper_ = "_CAMPO-upper_";
	private static final String _FIELD_lower_ = "_CAMPO-lower_";
	private static final String _FIELD_TYPE_ = "_CAMPO_TIPO_";

	private static final String _ENTITY_ = "_ENTIDADE_";
	private static final String _ENTITY_lower = "_ENTIDADE-lower_";
	private static final String _ENTITY_upper = "_ENTIDADE-upper_";
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
			return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8) + System.lineSeparator();
		} catch (final Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	private final String fixName(final String name, final String entityName, final String contents) {
		final String packageName;
		if (contents.startsWith(PACKAGE_space)) {
			final int comma = contents.indexOf(COMMA);
			final String packageNameWithDots = contents.substring(PACKAGE_space.length(), comma);
			packageName = packageNameWithDots.replace(DOT, File.separatorChar) + File.separatorChar;
		} else {
			packageName = "";
		}
		return packageName + replaceFileName(entityName, name);
	}

	private final String replaceFileName(final String entityName, final String name) {
		return name.replace(template_dot, "")//
				.replace(_ENTITY_, entityName)//
				.replace(_ENTITY_lower, entityName.toLowerCase())//
				.replace(_ENTITY_upper, entityName.toUpperCase())//
				.replace(_ENTITY_camel, this.firstUpper(entityName))//
				;
	}

	public final String replace(final String fileContents, final Class<?> clazz) {
		String newFileContents = fileContents;
		final String entity = clazz.getSimpleName();
		newFileContents = newFileContents.replace(_ENTITY_, entity);
		newFileContents = newFileContents.replace(_ENTITY_lower, entity.toLowerCase());
		newFileContents = newFileContents.replace(_ENTITY_upper, entity.toUpperCase());
		newFileContents = newFileContents.replace(_ENTITY_camel, this.firstUpper(entity));
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
					if (!Modifier.isStatic(classField.getModifiers())) {
						final String classFieldName = classField.getName();
						fields.append(fieldTemplate//
								.replace(_FIELD_lower_, classFieldName.toLowerCase())//
								.replace(_FIELD_upper_, classFieldName.toUpperCase())//
								.replace(_FIELD_camel_, this.firstUpper(classFieldName))//
								.replace(_FIELD_TYPE_form, this.form(classField.getType().getSimpleName()))//
								.replace(_FIELD_TYPE_, classField.getType().getSimpleName())//
								.replace(_FIELD_, classFieldName)//
						);
					}
				}
				newFileContents = parts[0] + fields.toString() + parts[1];
			} else {
				break;
			}
		}
		return newFileContents;
	}

	private final String form(final String typeClassName) {
		return "text";
	}

	private final String firstUpper(final String str) {
		if (str == null) {
			return null;
		} else if (str.length() <= 0) {
			return "";
		} else if (str.length() == 1) {
			return str.toUpperCase();
		} else {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
	}

	private final String getFieldTemplate(final String str, final int[] start_end) {
		start_end[0] = str.indexOf(_FIELDS_);
		if (start_end[0] >= 0) {
			start_end[1] = str.indexOf(_FIELDS_END_);
			final String template = str.substring(start_end[0] + _FIELDS_.length(), start_end[1]);
			start_end[1] += _FIELDS_END_.length();
			return template;
		} else {
			return "";
		}
	}

}
