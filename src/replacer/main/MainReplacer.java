package replacer.main;

import replacer.Replacer;

/**
 * @author crisstanza
 */
public final class MainReplacer {

	private MainReplacer() {
	}

	/**
	 * @param args
	 */
	public static final void main(final String[] args) {
		final MainReplacer mainReplacer = new MainReplacer();
		mainReplacer.start();
	}

	private final void start() {
		final Replacer replacer = new Replacer();
		replacer.start();
	}

}
