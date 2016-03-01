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

	private static final String $FIELDS$ = "$FIELDS$" + System.lineSeparator();
	private static final String $$FIELDS$ = "$$FIELDS$" + System.lineSeparator();

	private static final String $FIELD$ = "$FIELD$";
	private static final String $FIELD_TYPE$form$ = "$FIELD_TYPE_form";
	private static final String $FIELD_camel$ = "$FIELD_camel$";
	private static final String $FIELD_upper$ = "$FIELD_upper$";
	private static final String $FIELD_lower$ = "$FIELD_lower$";
	private static final String $FIELD_TYPE$ = "$FIELD_TYPE$";

	private static final String $ENTITIES$ = "$ENTITIES$" + System.lineSeparator();
	private static final String $$ENTITIES_END$ = "$$ENTITIES$" + System.lineSeparator();

	private static final String $ENTITY$ = "$ENTITY$";
	private static final String $ENTITY_lower$ = "$ENTITY_lower$";
	private static final String $ENTITY_upper$ = "$ENTITY_upper$";
	private static final String $ENTITY_camel$ = "$ENTITY_camel$";

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
				.replace($ENTITY$, entityName)//
				.replace($ENTITY_lower$, entityName.toLowerCase())//
				.replace($ENTITY_upper$, entityName.toUpperCase())//
				.replace($ENTITY_camel$, this.camel(entityName))//
				;
	}

	public final String replace(final String fileContents, final Class<?> clazz, final Class<?>[] classes) {
		String newFileContents = fileContents;
		while (true) {
			final int[] start_end = new int[2];
			final String entityTemplate = this.getEntityTemplate(newFileContents, start_end);
			if (start_end[0] >= 0) {
				final String[] parts = new String[2];
				parts[0] = newFileContents.substring(0, start_end[0]);
				parts[1] = newFileContents.substring(start_end[1], newFileContents.length());
				final StringBuilder fields = new StringBuilder();
				for (final Class<?> clazzAux : classes) {
					fields.append(entityTemplate//
							.replace($ENTITY$, clazzAux.getSimpleName())//
							.replace($ENTITY_lower$, clazzAux.getSimpleName().toLowerCase())//
							.replace($ENTITY_upper$, clazzAux.getSimpleName().toUpperCase())//
							.replace($ENTITY_camel$, this.camel(clazzAux.getSimpleName()))//
					);
				}
				newFileContents = parts[0] + fields.toString() + parts[1];
			} else {
				break;
			}
		}
		while (true) {
			final int[] start_end = new int[2];
			final String fieldTemplate = this.getFieldTemplate(newFileContents, start_end);
			if (start_end[0] >= 0) {
				final String[] parts = new String[2];
				parts[0] = newFileContents.substring(0, start_end[0]);
				parts[1] = newFileContents.substring(start_end[1], newFileContents.length());
				final StringBuilder fields = new StringBuilder();
				final Field[] classFields = clazz.getDeclaredFields();
				for (final Field classFieldAux : classFields) {
					if (!Modifier.isStatic(classFieldAux.getModifiers())) {
						final String classFieldName = classFieldAux.getName();
						fields.append(fieldTemplate//
								.replace($FIELD_lower$, classFieldName.toLowerCase())//
								.replace($FIELD_upper$, classFieldName.toUpperCase())//
								.replace($FIELD_camel$, this.camel(classFieldName))//
								.replace($FIELD_TYPE$form$, this.form(classFieldAux.getType().getSimpleName()))//
								.replace($FIELD_TYPE$, classFieldAux.getType().getSimpleName())//
								.replace($FIELD$, classFieldName)//
						);
					}
				}
				newFileContents = parts[0] + fields.toString() + parts[1];
			} else {
				break;
			}
		}
		final String entity = clazz.getSimpleName();
		newFileContents = newFileContents.replace($ENTITY$, entity);
		newFileContents = newFileContents.replace($ENTITY_lower$, entity.toLowerCase());
		newFileContents = newFileContents.replace($ENTITY_upper$, entity.toUpperCase());
		newFileContents = newFileContents.replace($ENTITY_camel$, this.camel(entity));
		return newFileContents;

	}

	private final String form(final String typeClassName) {
		return "text";
	}

	private final String camel(final String str) {
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
		start_end[0] = str.indexOf($FIELDS$);
		if (start_end[0] >= 0) {
			start_end[1] = str.indexOf($$FIELDS$, start_end[0]);
			final String template = str.substring(start_end[0] + $FIELDS$.length(), start_end[1]);
			start_end[1] += $$FIELDS$.length();
			return template;
		} else {
			return "";
		}
	}

	private final String getEntityTemplate(final String str, final int[] start_end) {
		start_end[0] = str.indexOf($ENTITIES$);
		if (start_end[0] >= 0) {
			start_end[1] = str.indexOf($$ENTITIES_END$, start_end[0]);
			final String template = str.substring(start_end[0] + $ENTITIES$.length(), start_end[1]);
			start_end[1] += $$ENTITIES_END$.length();
			return template;
		} else {
			return "";
		}
	}

}
