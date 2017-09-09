package com.infra.cli;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jline.console.ConsoleReader;
import jline.console.CursorBuffer;
import jline.console.completer.CandidateListCompletionHandler;
import jline.console.completer.CompletionHandler;

public class CmdCompletionHandler implements CompletionHandler {

	private static final Logger log = LogManager.getLogger(CmdCompletionHandler.class.getName());

	/**
	 * Returns a root that matches all the {@link String} elements of the
	 * specified {@link List}, or null if there are no commonalities. For
	 * example, if the list contains <i>foobar</i>, <i>foobaz</i>, <i>foobuz</i>
	 * , the method will return <i>foob</i>.
	 */
	private String getUnambiguousCompletions(final List<CharSequence> candidates) {
		if (candidates == null || candidates.isEmpty()) {
			return null;
		}

		// convert to an array for speed
		String[] strings = candidates.toArray(new String[candidates.size()]);

		String first = strings[0];
		StringBuilder candidate = new StringBuilder();

		for (int i = 0; i < first.length(); i++) {
			if (startsWith(first.substring(0, i + 1), strings)) {
				candidate.append(first.charAt(i));
			} else {
				break;
			}
		}

		return candidate.toString();
	}

	/**
	 * @return true is all the elements of <i>candidates</i> start with
	 *         <i>starts</i>
	 */
	private boolean startsWith(final String starts, final String[] candidates) {
		for (String candidate : candidates) {
			if (!candidate.startsWith(starts)) {
				return false;
			}
		}

		return true;
	}

	public boolean complete(ConsoleReader reader, List<CharSequence> candidates, int pos) throws IOException {

		CursorBuffer buf = reader.getCursorBuffer();
		log.info("handler/candidates  = [" + buf + "] " + candidates + "  pos " + pos);

		StringTokenizer tokenizer = new StringTokenizer(buf.buffer.toString());
		String last_token = null;
		String cmd_prefix = "";

		while (tokenizer.hasMoreTokens()) {
			if (last_token != null) {
				cmd_prefix += (last_token + " ");
			}
			last_token = tokenizer.nextToken();

			log.info("cmd_prefix " + cmd_prefix);
			log.info("last_token " + last_token);
		}

		log.info("handler/candidates  = [" + buf + "] " + candidates);

		CharSequence value = null;
		if (candidates.size() == 1) {
			value = candidates.get(0);

			if (last_token == null) {
				CandidateListCompletionHandler.setBuffer(reader, value, 0);
				return true;
			}

			// check for overlap with the next candidate
			if (cmd_prefix != null && cmd_prefix.length() >= pos - 1) {
				if (value.equals(last_token.trim())) {
					log.info("handler/candidates 0 " + candidates);
					log.info("handler/buffer 0 " + reader.getCursorBuffer());
					return false;
				}

				if (last_token.endsWith(value.toString())) {
					log.info("handler/candidates 0' " + candidates);
					log.info("handler/buffer 0' " + reader.getCursorBuffer());
					log.info("handler/last_token 0' " + last_token);
					return false;
				}

				if (value.toString().startsWith(last_token)) {
					log.info("handler/candidates 0'' " + candidates);
					log.info("handler/buffer 0'' " + reader.getCursorBuffer());
					log.info("handler/last_token 0'' " + last_token);
					log.info("handler/new buffer 0'' " + (cmd_prefix + value));

					CandidateListCompletionHandler.setBuffer(reader, (cmd_prefix + value), 0);
					return false;
				}
			}

			/*
			 * This is for appending the next folder name for a matching file
			 * token properly.
			 */
			String last_token_prefix = "";
			if (last_token.lastIndexOf('/') != -1) {
				last_token_prefix = last_token.substring(0, last_token.lastIndexOf('/'));
				CandidateListCompletionHandler.setBuffer(reader, (cmd_prefix + last_token_prefix + "/" + value), 0);
			} else {
				CandidateListCompletionHandler.setBuffer(reader, (cmd_prefix + last_token + " " + value), 0);
			}

			log.info("handler/candidates 1 " + candidates);
			log.info("handler/buffer 1 " + reader.getCursorBuffer());
			return true;
		} else if (candidates.size() > 1) {
			value = getUnambiguousCompletions(candidates);
			log.info("handler/candidates 2 " + candidates);
			log.info("handler/buffer 2 " + reader.getCursorBuffer());

			if (value != null && value.length() > 0) {
				if (value.toString().startsWith(last_token)) {
					CandidateListCompletionHandler.setBuffer(reader, (cmd_prefix + value), 0);
				} else {
					String last_token_prefix = "";
					if (last_token.lastIndexOf('/') != -1) {
						last_token_prefix = last_token.substring(0, last_token.lastIndexOf('/'));
						if (last_token_prefix.length() < last_token.length()) {
							String last_token_end = last_token.substring(last_token.lastIndexOf('/') + 1);
							if (last_token_end != null) {
								if (value.toString().startsWith(last_token_end)) {
									log.info("handler/last_token_prefix 0 " + last_token_prefix);
									log.info("handler/last_token_end 0 " + last_token_end);

									CandidateListCompletionHandler.setBuffer(reader,
											(cmd_prefix + last_token_prefix + "/" + value), 0);
								} else {
									// incorrect to be here..
									log.info("handler/last_token_prefix 1 " + last_token_prefix);
									log.info("handler/last_token_end 1 " + last_token_end);

									CandidateListCompletionHandler.setBuffer(reader,
											(cmd_prefix + last_token + "/" + value), 0);
								}
							} else {
								// incorrect to be here..
								log.info("handler/last_token_prefix 2 " + last_token_prefix);

								CandidateListCompletionHandler.setBuffer(reader,
										(cmd_prefix + last_token + "/" + value), 0);
							}
						} else {
							CandidateListCompletionHandler.setBuffer(reader, (cmd_prefix + last_token + value), 0);
						}
					} else {
						CandidateListCompletionHandler.setBuffer(reader, (cmd_prefix + last_token + " " + value), 0);
					}
				}
			} else {
				CandidateListCompletionHandler.setBuffer(reader, (cmd_prefix + last_token + " "), 0);
			}
			log.info("handler/candidates 2' " + candidates);
			log.info("handler/buffer 2' " + reader.getCursorBuffer());
		}

		CandidateListCompletionHandler.printCandidates(reader, candidates);

		// redraw the current console buffer
		reader.redrawLine();

		log.info("handler/returnVal 3 " + true + "pos = " + pos);

		return true;
	}

}
