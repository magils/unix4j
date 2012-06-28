package org.unix4j.unix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Input;
import org.unix4j.io.Output;

/**
 * Non-instantiable module with inner types making up the sort command.
 */
public final class Sort {

	/**
	 * The "sort" command name.
	 */
	public static final String NAME = "sort";

	/**
	 * Interface defining all method signatures for the sort command.
	 * 
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command builder providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * Sorts the input in ascending order. Equivalent to
		 * <tt>sort(Sort.Option.ascending}</tt>.
		 * 
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link Factory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R sort();

		/**
		 * Sorts the input using the specified sort {@link Option option}.
		 * 
		 * @param options
		 *            the sort options
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link Factory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R sort(Option... options);
	}

	/**
	 * Option flags for the sort command.
	 */
	public static enum Option {
		/**
		 * Sort in ascending order (the default).
		 */
		ascending,
		/**
		 * Sort in descending order.
		 */
		descending
	}

	/**
	 * Arguments and options for the sort command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		public Args() {
			super(Option.class);
		}

		public Args(Option... options) {
			this();
			setOpts(options);
		}
	}

	/**
	 * Singleton {@link Factory} for the sort command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {
		@Override
		public Command sort() {
			return new Command(new Args());
		}

		@Override
		public Command sort(Option... options) {
			return new Command(new Args(options));
		}
	};

	/**
	 * Sort command implementation.
	 */
	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, Type.CompleteInput, arguments);
		}

		@Override
		public Command withArgs(Args arguments) {
			return new Command(arguments);
		}

		@Override
		public void executeBatch(Input input, Output output) {
			final boolean isAsc = getArguments().hasOpt(Option.ascending);
			final boolean isDesc = getArguments().hasOpt(Option.descending);
			if (isAsc && isDesc) {
				throw new IllegalArgumentException("Options " + Option.ascending + " and " + Option.descending + " cannot be specified at the same time");
			}
			final List<String> lines = new ArrayList<String>();
			while (input.hasMoreLines()) {
				lines.add(input.readLine());
			}
			Collections.sort(lines);
			if (isDesc) {
				for (int i = lines.size() - 1; i >= 0; i--) {
					output.writeLine(lines.get(i));
				}
			} else {
				for (final String line : lines) {
					output.writeLine(line);
				}
			}
		}
	}

	// no instances
	private Sort() {
		super();
	}
}