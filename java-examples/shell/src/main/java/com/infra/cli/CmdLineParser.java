package com.infra.cli;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
//import org.springframework.context.annotation.PropertySource;

import jline.TerminalFactory;
import jline.console.ConsoleReader;
import jline.console.KeyMap;
import jline.console.history.FileHistory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@SuppressWarnings("restriction")
//@PropertySource(value = { "classpath:application.properties" })
public class CmdLineParser {
	private static final Logger log = LogManager.getLogger(CmdLineParser.class.getName());

	public CmdLineParser(CmdNode parseTree) {
		System.setProperty("jline.shutdownhook", "true");
		ConsoleThread ct = new ConsoleThread(parseTree);
		Thread t = new Thread(ct);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class ConsoleThread implements Runnable {

		CmdNode parseTree;

		private void attachSignalHandler() {
			Signal.handle(new Signal("INT"), new SignalHandler() {
				public void handle(Signal sig) {
					System.out.println("\n### Thanks for using Shell ####");
					System.exit(1);
				}
			});
		}

		public ConsoleThread(CmdNode parseTree) {
			this.parseTree = parseTree;
		}

		public void run() {

			FileHistory fileHistory = null;
			try {
				fileHistory = new FileHistory(new File(System.getProperty("user.home") + "/.shellhistory.txt"));

			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			attachSignalHandler();
			final FileHistory history = fileHistory;
			try {
				final ConsoleReader console = new ConsoleReader();
				console.setHistoryEnabled(true);
				String prompt = PropertiesCache.getInstance().getProperty("shell.cli.prompt");
				if (prompt == null)
					prompt = "myshell#> ";
				console.setPrompt(prompt);

				// CandidateListCompletionHandler handler;
				// handler = new CandidateListCompletionHandler();

				CmdCompletionHandler handler;
				handler = new CmdCompletionHandler();
				console.addCompleter(parseTree);
				console.setCompletionHandler(handler);
				console.setBellEnabled(true);
				console.setHistory(fileHistory);

				KeyMap keyMap = console.getKeys();
				ActionListener quitAction = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("ActionEvent : quit");
						try {
							history.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.exit(0);
					};
				};

				ActionListener helpAction = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						List<CharSequence> candidates = new ArrayList<CharSequence>(1);

						ThreadContext.push("HELP");
						parseTree.complete(console.getCursorBuffer().upToCursor(), console.getCursorBuffer().cursor,
								candidates);
						ThreadContext.pop();

						try {

							if (candidates.size() == 0) {
								log.info("console beep " + candidates.size());
								console.beep();
							} else {
								/*
								 * console is not echoing the '?' character, so
								 * print the char
								 */
								console.getCursorBuffer().write('?');
								console.redrawLine();
							}

							if (candidates.size() != 0) {
								console.println();
								console.printColumns(candidates);
								console.flush();
								console.backspace();
								console.redrawLine();
								console.flush();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				};

				CharSequence keySeq = new String("?");
				keyMap.bind(keySeq, helpAction);

				CharSequence keySeq2 = new String("q");
				keyMap.bind(keySeq2, quitAction);

				String line = null;

				// while ((line = console.readLine(prompt + " ")) != null) {
				while ((line = console.readLine()) != null) {
					String command = line.split(" ")[0];
					if ("help".equalsIgnoreCase(command)) {
						List<CharSequence> candidates = new ArrayList<CharSequence>(1);
						parseTree.complete(console.getCursorBuffer().upToCursor(), console.getCursorBuffer().cursor,
								candidates);
						if (candidates.size() != 0) {
							log.info("candidates.size " + candidates.size());
							console.printColumns(candidates);
							console.flush();
						} else {
							log.info("console beep " + candidates.size());
							console.beep();
							if ("?".equalsIgnoreCase(command)) {
								console.backspace();
							}
						}
					} else if ("exit".equalsIgnoreCase(command)) {
						history.flush();
						System.exit(0);
					} else {
						CmdNode matchingNode = parseTree.match(line);
						if (matchingNode != null) {
							log.info("Match success " + line);
							history.flush();
						} else if (line != null && line.length() > 0) {
							log.info("Match failed : " + line);
						}
						continue;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					TerminalFactory.get().restore();
					history.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
