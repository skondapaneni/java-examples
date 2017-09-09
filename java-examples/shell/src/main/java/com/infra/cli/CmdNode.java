package com.infra.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.infra.util.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import jline.console.completer.Completer;

public class CmdNode implements Comparable<CmdNode>, Completer {

	private static final Logger log = LogManager.getLogger(CmdNode.class.getName());

	/* cmd tokens at this level */
	private final SortedSet<CmdNode> nodes = new TreeSet<CmdNode>();
	private CmdNode next;
	private Object cmdHandler;
	private CmdToken cmdToken;
	private boolean isRepeatable;

	public static CmdNode nodeNull = new CmdNode();

	public CmdNode(CmdToken cmdToken) {
		this.cmdToken = cmdToken;
		setRepeatable(cmdToken.isRepeatable());
	}

	public CmdNode(CmdToken cmdToken, Object cmdHandler) {
		this.cmdToken = cmdToken;
		this.cmdHandler = cmdHandler;
	}

	public CmdNode() {

	}

	public boolean isTerminal() {
		return (cmdToken != null);
	}

	public boolean hasNullableChoice() {
		if (nodes.size() != 0) {
			Iterator<CmdNode> iter = nodes.iterator();
			while (iter.hasNext()) {
				CmdNode cmdNode = iter.next();
				boolean nullable = cmdNode.isNullable();
				if (nullable) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isNullable() {
		if (this == nodeNull) {
			return true;
		}

		if (isTerminal()) {
			return false;
		}

		TreeSet<CmdNode> firstSet = first();
		if (firstSet.size() != 0 && firstSet.contains(nodeNull)) {
			return true;
		}

		// if (hasNullableChoice()) {
		// return true;
		// }
		//
		// if (next != null) {
		// return next.isNullable();
		// }

		return false;
	}

	public CmdNode getLast() {
		CmdNode prev = this;
		while (prev.next != null) {
			prev = prev.next;
		}

		return prev;
	}

	public CmdNode getPrev(CmdNode node) {
		CmdNode prev = this;
		while (prev.next != null && prev.next != node) {
			prev = prev.next;
		}
		if (prev.next == node) {
			return prev;
		} else {
			return null;
		}
	}

	public TreeSet<CmdNode> follow() {
		if (next != null) {
			TreeSet<CmdNode> firstSet = next.first();
			if (firstSet.size() != 0 && firstSet.contains(nodeNull)) {
				firstSet.remove(nodeNull);
			}
			return firstSet;
		}
		return null;
	}

	public TreeSet<CmdNode> first(CmdNode seenNode) {
		TreeSet<CmdNode> firstSet = new TreeSet<CmdNode>();

		if (this == nodeNull) {
			firstSet.add(nodeNull);
			return firstSet;
		}

		if (isTerminal()) {
			firstSet.add(this);
			return firstSet;
		}

		if (nodes.size() != 0) {
			Iterator<CmdNode> iter = nodes.iterator();
			while (iter.hasNext()) {
				CmdNode alternateNode = iter.next();
				firstSet.addAll(alternateNode.first(seenNode));
			}
		}

		/* this production is nullable, let's go to the next (follow set) */
		if (nodes.size() == 0 || firstSet.contains(nodeNull)) {
			if (next != null) {
				if (next != seenNode) {
					firstSet.remove(nodeNull);
					firstSet.addAll(next.first(this));
				}
			}
		}

		return firstSet;
	}

	public TreeSet<CmdNode> first() {
		TreeSet<CmdNode> firstSet = new TreeSet<CmdNode>();

		if (this == nodeNull) {
			firstSet.add(nodeNull);
			return firstSet;
		}

		if (isTerminal()) {
			//System.out.println("first .. " + cmdToken + "this.. " + this);
			firstSet.add(this);
			return firstSet;
		}

		if (nodes.size() != 0) {
			Iterator<CmdNode> iter = nodes.iterator();
			while (iter.hasNext()) {
				CmdNode alternateNode = iter.next();
				firstSet.addAll(alternateNode.first());
			}
		}

		/* this production is nullable, let's go to the next (follow set) */
		if (nodes.size() == 0 || firstSet.contains(nodeNull)) {
			if (next != null) {
				firstSet.remove(nodeNull);
				firstSet.addAll(next.first(this));
			}
		}

		return firstSet;
	}

	public CmdNode getNext() {
		return next;
	}

	public void setNext(CmdNode next) {
		this.next = next;
	}

	public void setCmdHandler(Object cmdHandler) {

		if (!isTerminal()) {
			TreeSet<CmdNode> firstSet = first();
			if (firstSet != null) {
				Iterator<CmdNode> iter = firstSet.iterator();
				while (iter.hasNext()) {
					CmdNode node = iter.next();
					node.getLast().cmdHandler = cmdHandler;
				}
			}
		}
		this.cmdHandler = cmdHandler;
	}

	public Object getCmdHandler() {
		return this.cmdHandler;
	}

	public boolean isRepeatable() {
		return isRepeatable;
	}

	public void setRepeatable(boolean isRepeatable) {
		this.isRepeatable = isRepeatable;
	}

	public boolean startsWith(String token, Properties props) throws Exception {

		if (this == nodeNull) {
			return false;
		}

		if (isTerminal()) {
			return (cmdToken.startsWith(token));
		}

		throw new Exception("CmdNode is not a terminal!!!");
	}

	public boolean match(String nextToken, Properties props) throws Exception {

		if (this == nodeNull) {
			return false;
		}

		if (isTerminal()) {
			if (nextToken != null) {
				if (cmdToken.match(nextToken)) {
					if (props != null && (cmdToken.getTokenType() == TokenType.ARGUMENT
							|| cmdToken.getTokenType() == TokenType.FILE)) {
						props.setProperty(cmdToken.getName(), nextToken);
					}
					if (props != null && cmdToken.getTokenType() == TokenType.ENUM) {
						if (props.containsKey(cmdToken.getName())) {
							@SuppressWarnings("unchecked")
							ArrayList<String> arrayList = (ArrayList<String>) props.get(cmdToken.getName());
							if (arrayList != null) {
								arrayList.add(nextToken);
							}
						} else {
							ArrayList<String> arrayList = new ArrayList<String>();
							arrayList.add(nextToken);
							props.put(cmdToken.getName(), arrayList);
						}
					}
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		throw new Exception("CmdNode is not a terminal!!!");

	}

	/*
	 * S -> ABCd A -> e | f | null B -> g | h | null C -> p | q
	 * 
	 * e = new CmdNode(e) f = new CmdNode(f) g = new CmdNode(g) h = new
	 * CmdNode(h) p = new CmdNode(p) q = new CmdNode(q) d = new CmdNode(d)
	 * 
	 * A = new CmdNode(); A.merge(e).merge(f).merge(nodeNull);
	 * 
	 * B = new CmdNode(); B.merge(g).merge(h).merge(nodeNull);
	 * 
	 * C = new CmdNode(); C.merge(p).merge(q);
	 * 
	 * S = new CmdNode(); S.append(A); S.append(B); S.append(C); S.append(d);
	 * 
	 * 
	 * show -> job -> jobid -> log (CMD1) show -> job -> jobid -> history (CMD2)
	 * 
	 * show -> next of CMD1.job.merge(CMD2.job).
	 * 
	 * show | job | jobid { log | history }
	 * 
	 * list (CMD1) list -> job -> jobid (CMD2)
	 * 
	 * list <CmdHandler> | job | jobid
	 * 
	 */

	public CmdNode merge(CmdNode subtree) {

		/* Both are terminals and match */
		if (isTerminal() && subtree.isTerminal() && subtree.compareTo(this) == 0) {

			if (subtree.getCmdHandler() != null && getCmdHandler() != null) {
				/*
				 * we can't have 2 terminal nodes which have a handler, leads to
				 * ambiguous resolving
				 */
				subtree.print(0);
				throw new RuntimeException("Grammar invalid !!" + this.cmdToken.toString());
			}

			/* eliminate subtree and merge it's siblings */
			if (next != null && subtree.next != null) {
				next = next.merge(subtree.next);
			} else if (next == null && subtree.next != null) {
				if (cmdHandler != null) {
					CmdNode newRoot = new CmdNode();
					newRoot.addChoice(nodeNull);
					newRoot.addChoice(subtree.next);
					next = newRoot;
				} else {
					next = subtree.next;
					cmdHandler = subtree.getCmdHandler();
				}
			} else if (next != null && subtree.next == null) {
				if (subtree.getCmdHandler() != null) {
					CmdNode newRoot = new CmdNode();
					newRoot.addChoice(nodeNull);
					newRoot.addChoice(next);
					next = newRoot;
					cmdHandler = subtree.getCmdHandler();
				}
			}

			return this;
		}

		/* Both are terminals and do not match */
		if (isTerminal() && subtree.isTerminal()) {
			// create a new root and add the 2 nodes as choices.
			CmdNode newRoot = new CmdNode();
			// Resolve ambiguous productions
			if (cmdToken.getTokenType() == TokenType.ARGUMENT
					&& subtree.cmdToken.getTokenType() == TokenType.ARGUMENT) {
				CmdNode node1 = new CmdNode(new CmdToken(cmdToken.getName(), TokenType.KEYWORD));
				CmdNode node2 = new CmdNode(new CmdToken(subtree.cmdToken.getName(), TokenType.KEYWORD));
				node1.next = this;
				node2.next = subtree;
				newRoot.nodes.add(node1);
				newRoot.nodes.add(node2);
			} else {
				newRoot.nodes.add(this);
				newRoot.nodes.add(subtree);
			}
			return newRoot;
		}

		/* if current node is a non-terminal and the subtree is a terminal */
		if (!isTerminal() && subtree.isTerminal()) {
			if (nodes.contains(subtree)) {
				SortedSet<CmdNode> subset = nodes.tailSet(subtree);
				CmdNode orig = subset.first();

				if (subtree.getCmdHandler() != null) {
					/* we can't have 2 terminal nodes */
					if (orig.getCmdHandler() != null) {
						throw new RuntimeException("Grammar invalid !!" + this);
					}
				}

				if (orig.next != null && subtree.next != null) {
					orig.next = orig.next.merge(subtree.next);
				} else if (orig.next == null && subtree.next != null) {
					if (orig.cmdHandler != null) {
						CmdNode newRoot = new CmdNode();
						newRoot.addChoice(nodeNull);
						newRoot.addChoice(subtree.next);
						orig.next = newRoot;
					} else {
						orig.next = subtree.next;
						orig.cmdHandler = subtree.getCmdHandler();
					}
				} else if (orig.next != null && subtree.next == null) {
					if (subtree.getCmdHandler() != null) {
						CmdNode newRoot = new CmdNode();
						newRoot.addChoice(nodeNull);
						newRoot.addChoice(orig.next);
						orig.next = newRoot;
						orig.cmdHandler = subtree.getCmdHandler();
					}
				}
			} else {
				nodes.add(subtree);
			}
			return this;
		}

		/* current node is a terminal and subtree is a non-terminal */
		if (isTerminal() && !subtree.isTerminal()) {
			if (subtree.nodes.contains(this)) {
				SortedSet<CmdNode> subset = subtree.nodes.tailSet(this);
				CmdNode subtreeNode = subset.first();

				if (getCmdHandler() != null) {
					/* we can't have 2 terminal nodes */
					if (subtreeNode.getCmdHandler() != null) {
						throw new RuntimeException("Grammar invalid !!" + subtreeNode);
					}
				}

				if (subtreeNode.next != null && next != null) {
					subtreeNode.next = subtreeNode.next.merge(next);
				} else if (subtreeNode.next == null && next != null) {
					if (subtreeNode.cmdHandler != null) {
						CmdNode newRoot = new CmdNode();
						newRoot.addChoice(nodeNull);
						newRoot.addChoice(next);
						subtreeNode.next = newRoot;
					} else {
						subtreeNode.next = next;
						subtreeNode.cmdHandler = getCmdHandler();
					}
				} else if (subtreeNode.next != null && next == null) {
					if (getCmdHandler() != null) {
						CmdNode newRoot = new CmdNode();
						newRoot.addChoice(nodeNull);
						newRoot.addChoice(subtreeNode.next);
						subtreeNode.next = newRoot;
						subtreeNode.cmdHandler = getCmdHandler();
					}
				}
			} else {
				subtree.nodes.add(this);
			}
			return subtree;
		}

		/* Both are non-terminals and have next productions */
		if (next != null || subtree.next != null) {
			// create a new root and add the 2 nodes as choices.
			CmdNode newRoot = new CmdNode();
			newRoot.nodes.add(this);
			newRoot.nodes.add(subtree);
			return newRoot;
		}

		// Merge all the choices from the subtree into this node.
		Iterator<CmdNode> subtreeIter = subtree.nodes.iterator();
		while (subtreeIter.hasNext()) {
			CmdNode subtreeCmdNode = subtreeIter.next();
			if (nodes.contains(subtreeCmdNode)) {
				SortedSet<CmdNode> subset = nodes.tailSet(subtreeCmdNode);
				CmdNode orig = subset.first();
				if (subtreeCmdNode.next != null) {
					if (orig.next == null) {
						orig.next = subtreeCmdNode.next;
					} else {
						orig.next.merge(subtreeCmdNode.next);
					}
				}
				if (subtreeCmdNode.getCmdHandler() != null) {
					/* we can't have 2 terminal nodes */
					if (orig.getCmdHandler() != null) {
						throw new RuntimeException("Grammar invalid !!" + orig);
					}
					orig.cmdHandler = subtreeCmdNode.getCmdHandler();
				}
			} else {
				nodes.add(subtreeIter.next());
			}
		}

		return this;
	}

	public void prependNode(CmdNode newNode, CmdNode child) {
		nodes.remove(child);
		newNode.next = child;
		nodes.add(newNode);
	}

	public CmdNode addChoice(CmdNode node) {
		if (isTerminal()) {
			CmdNode parent = new CmdNode();
			parent.addChoice(this).addChoice(node);
			return parent;
		}

		TreeSet<CmdNode> firstSet1 = first();
		TreeSet<CmdNode> firstSet2 = node.first();

		if (firstSet1 != null && firstSet1.size() != 0 && firstSet2 != null && firstSet2.size() != 0) {
			/* more work needed to resolve ambiguity */
			CmdNode one = firstSet1.first();
			CmdNode two = firstSet2.first();

			if (one != null && one.isTerminal() && two != null && two.isTerminal()) {

				if (one.cmdToken.getTokenType() == TokenType.ARGUMENT
						&& two.cmdToken.getTokenType() == TokenType.ARGUMENT) {

					CmdNode node1 = new CmdNode(new CmdToken(one.cmdToken.getName(), TokenType.KEYWORD));
					CmdNode node2 = new CmdNode(new CmdToken(two.cmdToken.getName(), TokenType.KEYWORD));

					this.prependNode(node1, one);
					node.prependNode(node2, two);

				}
			}
		}

		nodes.add(node);
		return this;
	}

	public CmdNode optional(CmdNode node) {
		if (node.isTerminal()) {
			return (node.addChoice(nodeNull));
		} else {
			CmdNode parent = new CmdNode();
			parent.addChoice(node).addChoice(nodeNull);
			return parent;
		}
	}

	public CmdNode append(CmdNode node) {
		if (next == null) {
			next = node;
			return this;
		}

		CmdNode prev = next;
		CmdNode cur = next;
		while (cur != null) {
			prev = cur;
			cur = cur.next;
		}
		prev.next = node;

		return this;
	}

	public int compareTo(CmdNode o) {
		if (this != nodeNull && o != nodeNull) {
			if (cmdToken != null && o.cmdToken != null) {
				return cmdToken.compareTo(o.cmdToken);
			} else {
				return -1;
			}
		} else {
			if (this == o) {
				return 0;
			}

			if (o != nodeNull) {
				return 1;
			}

			return -1;
		}
	}

	public String toString() {
		if (cmdHandler != null && cmdToken != null) {
			return cmdToken.toString() + "@" + cmdHandler;
		}

		if (cmdToken != null) {
			log.info(cmdToken.toString());
			return cmdToken.toString();
		}

		return Integer.toString(this.hashCode());
	}

	public String toStringToken() {
		if (cmdHandler != null && cmdToken != null) {
			return cmdToken.toStringToken() + "@" + cmdHandler;
		}

		if (cmdToken != null) {
			log.info(cmdToken.toStringToken());
			return cmdToken.toStringToken();
		}

		return Integer.toString(this.hashCode());
	}

	public String toCandidateString() {

		if (cmdToken != null) {
			if (cmdToken.getTokenType() == TokenType.ENUM) {
				String candidateStr = "";
				Object[] enumValues = (Object[]) cmdToken.getEnumClass().getEnumConstants();
				for (int i = 0; i < enumValues.length; i++) {
					candidateStr += enumValues[i].toString();
					if (i + 1 != enumValues.length) {
						candidateStr += " ";
					}
				}
				return candidateStr;
			} else if (cmdToken.getTokenType() == TokenType.FILE) {
				List<CharSequence> candidates = new ArrayList<CharSequence>(1);
				cmdToken.filenameCompleter.complete("", 0, candidates);
				String candidateStr = "";
				if (candidates.size() != 0) {
					for (int i = 0; i < candidates.size(); i++) {
						candidateStr += candidates.get(i);
						if (i + 1 != candidates.size()) {
							candidateStr += " ";
						}
					}
				}
				return candidateStr;
			}
			return cmdToken.toStringToken();
		}

		if (this == nodeNull) {
			return "<cr>";
		}

		String candidateStr = "";

		TreeSet<CmdNode> treeSet = first();
		if (treeSet != null) {
			Iterator<CmdNode> iter = treeSet.iterator();
			while (iter.hasNext()) {
				CmdNode nextNode = iter.next();
				candidateStr += nextNode.toCandidateString();
				if (iter.hasNext()) {
					candidateStr += " ";
				}
			}
		}

		return candidateStr;
	}

	public String toCandidateString(String startsWith) {

		if (cmdToken != null) {
			if (cmdToken.getTokenType() == TokenType.ENUM) {
				String candidateStr = "";
				Object[] enumValues = (Object[]) cmdToken.getEnumClass().getEnumConstants();
				for (int i = 0; i < enumValues.length; i++) {
					if (enumValues[i].toString().toLowerCase().startsWith(startsWith.toLowerCase())) {
						candidateStr += enumValues[i].toString();
						if (i + 1 != enumValues.length) {
							candidateStr += " ";
						}
					}
				}
				return candidateStr;
			} else if (cmdToken.getTokenType() == TokenType.FILE) {
				List<CharSequence> candidates = new ArrayList<CharSequence>(1);
				cmdToken.filenameCompleter.complete(startsWith, 0, candidates);
				String candidateStr = "";
				if (candidates.size() != 0) {
					for (int i = 0; i < candidates.size(); i++) {
						candidateStr += candidates.get(i);
						if (i + 1 != candidates.size()) {
							candidateStr += " ";
						}
					}
					if (candidates.size() == 1) {
						String file_path_prefix = "";
						if (startsWith.lastIndexOf('/') != -1) {
							file_path_prefix = startsWith.substring(0, startsWith.lastIndexOf('/'));
						}

						if (file_path_prefix != null && file_path_prefix.length() > 0) {
							CharSequence candidate = candidates.get(0);
							log.info("candidate char 0, 1 " + candidate.charAt(0) + " " + candidate.charAt(1) + "  "
									+ file_path_prefix);
							log.info("startsWith " + startsWith);
							if (ThreadContext.peek() == null || ThreadContext.peek().equals("HELP") != true) {
								candidates.set(0, (file_path_prefix + "/" + candidates.get(0)));
							}
							candidateStr = (String) candidates.get(0);
						}
					}
				}
				return candidateStr;
			}
			return cmdToken.toStringToken();
		}

		if (this == nodeNull) {
			return "<cr>";
		}

		String candidateStr = "";

		TreeSet<CmdNode> treeSet = first();
		if (treeSet != null) {
			Iterator<CmdNode> iter = treeSet.iterator();
			while (iter.hasNext()) {
				CmdNode nextNode = iter.next();
				candidateStr += nextNode.toCandidateString(startsWith);
				if (iter.hasNext()) {
					candidateStr += " ";
				}
			}
		}

		return candidateStr;
	}

	public void printTab(int level) {
		for (int i = 0; i < level; i++) {
			System.out.print("\t");
		}
	}

	public void printTabInLog(int level) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < level; i++) {
			buf.append('\t');
		}
		log.info(buf);
	}

	public void print(Set<CmdNode> seenNodeSet, int level) {
		// save for debugging
		// log.info("this = " + Integer.toHexString(this.hashCode()));
		// Iterator<CmdNode> setIter = seenNodeSet.iterator();
		// int i = 0;
		// while (setIter.hasNext()) {
		// log.info("seenNodeSet member " + i + " " +
		// setIter.next().toString());
		// i += 1;
		// }
		printTab(level);

		if (isTerminal()) {
			log.info(toString());
			System.out.print(toString() + " ");
		}

		if (nodeNull == this) {
			log.info("null");
			System.out.print("null");
		}

		if (nodes != null && nodes.size() != 0) {
			log.info(" [ ");
			System.out.println(" [ ");
			Iterator<CmdNode> iter = nodes.iterator();
			while (iter.hasNext()) {
				CmdNode cmdNode = iter.next();
				cmdNode.print(seenNodeSet, level + 1);
				if (iter.hasNext()) {
					log.info(" | ");
					System.out.println(" |");
				}
			}
			System.out.println("");

			printTab(level);
			System.out.print(" ] ");
			log.info(" ] ");
		}

		if (next != null && !seenNodeSet.contains(this)) {
			log.info(" ");
			System.out.print(" ");
			seenNodeSet.add(this);
			next.print(seenNodeSet, level);

		}

	}

	public void printToLog(Set<CmdNode> seenNodeSet, int level) {
		printTabInLog(level);

		if (isTerminal()) {
			log.info(toString());
		}

		if (nodeNull == this) {
			log.info("null");
		}

		if (nodes != null && nodes.size() != 0) {
			log.info(" [ ");
			Iterator<CmdNode> iter = nodes.iterator();
			while (iter.hasNext()) {
				CmdNode cmdNode = iter.next();
				cmdNode.print(seenNodeSet, level + 1);
				if (iter.hasNext()) {
					log.info(" | ");
				}
			}

			printTabInLog(level);
			log.info(" ] ");
		}

		if (next != null && !seenNodeSet.contains(this)) {
			log.info(" ");
			seenNodeSet.add(this);
			next.printToLog(seenNodeSet, level);

		}

	}

	public void print(int level) {

		printTab(level);

		if (isTerminal()) {
			log.info(toString());
			System.out.print(toString());
		}

		if (nodeNull == this) {
			log.info("null");
			System.out.print("null");

		}

		if (nodes != null && nodes.size() != 0) {
			log.info(" [ ");
			System.out.print(" [ ");

			Iterator<CmdNode> iter = nodes.iterator();
			while (iter.hasNext()) {
				CmdNode cmdNode = iter.next();
				cmdNode.print(level + 1);
				if (iter.hasNext()) {
					log.info(" | ");
					System.out.print(" | ");
				}
			}
			System.out.println("");
			printTab(level);
			log.info(" ] ");
			System.out.print(" ] ");

		}

		if (next != null) {
			log.info(" ");
			System.out.print(" ");
			Set<CmdNode> nodeSet = new LinkedHashSet<CmdNode>();
			nodeSet.add(this);
			next.print(nodeSet, level);
		}

	}

	public static String separator = "\\s*\\t*\\r";

	public int complete(final String buffer, final int cursor, final List<CharSequence> candidates) {

		// buffer could be null
		Util.checkNotNull(candidates);
		Properties props = new Properties();

		if (buffer == null) {
			TreeSet<CmdNode> treeSet = first();
			Iterator<CmdNode> iter = treeSet.iterator();
			while (iter.hasNext()) {
				candidates.add(iter.next().toString());
			}
			return 0;
		}

		List<CmdNode> cmdCandidateList = new ArrayList<CmdNode>();
		List<CmdNode> matchingNodes = new ArrayList<CmdNode>();
		CmdNode matchingNode = null;

		Tokenizer tokenizer = new Tokenizer(buffer);
		cmdCandidateList.add(this);

		while (tokenizer.hasNext()) {
			/* A top down recursive descent parsing */
			candidates.clear();
			matchingNodes.clear();

			String token = tokenizer.nextToken();
			Iterator<CmdNode> cmdCandidateIter = cmdCandidateList.iterator();

			log.info("token/candidate cmd nodes 1 = [ token=" + token + "] " + cmdCandidateList.toArray());
			int nmatch = 0;
			while (cmdCandidateIter.hasNext()) {
				/*
				 * Get the list of all possible completions from this cmd node
				 */
				CmdNode cur = cmdCandidateIter.next();
				TreeSet<CmdNode> treeSet = cur.first();
				if (cur.isRepeatable()) {
					treeSet.add(cur);
				}
				Iterator<CmdNode> iter = treeSet.iterator();
				while (iter.hasNext()) {
					CmdNode nextNode = iter.next();
					boolean match = false;
					boolean skipThis = false;
					try {
						match = nextNode.startsWith(token, props);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (match) {
						if (matchingNode != null && nmatch != 0) {
							/* we have an ambiguous match */
							if (matchingNode.cmdToken.getTokenType() == TokenType.KEYWORD
									&& nextNode.cmdToken.getTokenType() == TokenType.ARGUMENT) {
								skipThis = true;
							}

							if (nextNode.cmdToken.getTokenType() == TokenType.KEYWORD
									&& matchingNode.cmdToken.getTokenType() == TokenType.ARGUMENT) {
								candidates.removeAll(
										Arrays.asList(matchingNode.toCandidateString(token).split(separator)));
							}

						}

						if (!skipThis) {
							candidates.addAll(Arrays.asList(nextNode.toCandidateString(token).split(separator)));

							matchingNode = nextNode;
							nmatch += 1;
							log.info("matchingNode " + matchingNode);
							if (nextNode.next != null) {
								matchingNodes.add(nextNode.next);
								log.info("matchingNode.next = " + nextNode.next);
							}
						}
					}
				}
			}

			log.info("completer/candidates 1 = [buffer=" + buffer + "] " + "[ token=" + token + " ] candidates= "
					+ candidates);
			cmdCandidateList.clear();
			cmdCandidateList.addAll(matchingNodes);
		}

		if (candidates.size() == 0 || candidates.size() > 1) {
			log.info("completer/candidates 2 = [" + buffer + "] candidates= " + candidates);
			if (matchingNode != null) {
				matchingNode.print(0);
			}
			return candidates.isEmpty() ? -1 : 0;
		}

		tokenizer.pushBack(1);
		String lastToken = tokenizer.nextToken();
		log.info("lastToken 3 [" + lastToken + "]");
		log.info("completer/matching nodes 3 = [" + candidates.get(0) + "] " + cmdCandidateList.toArray());
		log.info("completer/matching nodes 3 = [" + candidates.size() + "] " + candidates.get(0));

		int returnVal;

		/* is it a complete match, then present the next tokens to the user */
		// if (lastToken.contains(candidates.get(0)) ||
		if (lastToken.contentEquals(candidates.get(0))
				|| matchingNode.cmdToken.getTokenType() == TokenType.ARGUMENT) {
			Iterator<CmdNode> cmdCandidateIter = cmdCandidateList.iterator();
			log.info("completer/matching nodes 4 = [ lastTok= " + lastToken + "] " + cmdCandidateList.toArray());
			candidates.clear();
			matchingNodes.clear();
			while (cmdCandidateIter.hasNext()) {
				TreeSet<CmdNode> treeSet = cmdCandidateIter.next().first();
				Iterator<CmdNode> iter = treeSet.iterator();
				while (iter.hasNext()) {
					CmdNode nextNode = iter.next();
					log.info("nextNode.type "
							+ (nextNode.isTerminal() ? nextNode.cmdToken.getTokenType() : "NT :" + nextNode));
					/* nextNode.candidateString can return a list */
					candidates.addAll(Arrays.asList(nextNode.toCandidateString().split(separator)));
				}
			}

			if (matchingNode.cmdHandler != null) {
				if (candidates.size() == 0) {
					candidates.add(new String("<cr>"));
				} else if (!candidates.contains(new String("<cr>"))) {
					candidates.add(new String("<cr>"));
				}
			}

			returnVal = tokenizer.getLine().lastIndexOf(lastToken) + lastToken.length();

			if (candidates.size() == 1) {
				if (!tokenizer.getLine().endsWith(" ")) {
					candidates.set(0, " " + candidates.get(0));
				} else {
					returnVal += 1;
				}
			}
		} else {
			if (matchingNode.cmdToken.getTokenType() != TokenType.FILE) {
				candidates.set(0, candidates.get(0) + " ");
			}
			returnVal = tokenizer.getLine().lastIndexOf(lastToken);
		}

		log.info("completer/matching nodes 5 = [" + lastToken + "] " + candidates);
		log.info("returnVal 5 = [" + tokenizer.getLine() + "] " + returnVal);

		return candidates.isEmpty() ? -1 : returnVal;
	}

	public CmdNode match(final String buffer) {

		if (buffer == null || buffer.length() == 0) {
			return null;
		}

		Properties props = new Properties();
		List<CmdNode> cmdCandidateList = new ArrayList<CmdNode>();
		List<CmdNode> matchingNodes = new ArrayList<CmdNode>();
		CmdNode matchingNode = null;
		String lastToken = null;

		Tokenizer tokenizer = new Tokenizer(buffer);
		cmdCandidateList.add(this);

		/* A top down recursive descent parsing */
		while (tokenizer.hasNext()) {
			matchingNode = null;
			matchingNodes.clear();

			String token = tokenizer.nextToken();
			lastToken = token;
			Iterator<CmdNode> cmdCandidateIter = cmdCandidateList.iterator();
			log.info("completer/matching nodes 1 = [" + token + "] " + cmdCandidateList.toArray());
			while (cmdCandidateIter.hasNext()) {
				TreeSet<CmdNode> treeSet = cmdCandidateIter.next().first();
				Iterator<CmdNode> iter = treeSet.iterator();
				int nmatch = 0;
				while (iter.hasNext()) {
					CmdNode nextNode = iter.next();
					boolean match = false;
					boolean skipThis = false;

					try {
						match = nextNode.match(token, props);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (match) {
						if (matchingNode != null && nmatch != 0) {
							/* we have an ambiguous match */
							if (matchingNode.cmdToken.getTokenType() == TokenType.KEYWORD
									&& nextNode.cmdToken.getTokenType() == TokenType.ARGUMENT) {
								props.remove(nextNode.cmdToken.getName());
								skipThis = true;
							}

							if (nextNode.cmdToken.getTokenType() == TokenType.KEYWORD
									&& matchingNode.cmdToken.getTokenType() == TokenType.ARGUMENT) {
								matchingNodes.remove(matchingNode.next);
								props.remove(matchingNode.cmdToken.getName());
							}
						}

						if (!skipThis) {
							matchingNode = nextNode;
							log.info("matchingNode = " + matchingNode + " token = " + token);
							nmatch += 1;
							if (nextNode.next != null) {
								matchingNodes.add(nextNode.next);
								log.info("nextNode.next = " + nextNode.next);
							}
						}
					}
				}
			}
			cmdCandidateList.clear();
			cmdCandidateList.addAll(matchingNodes);
		}

		log.info("tokenizer.hasNext " + tokenizer.hasNext());
		log.info("matchingNode " + matchingNode);
		log.info("completer/matching nodes 2 = [" + lastToken + "] " + cmdCandidateList.toArray() + " "
				+ cmdCandidateList.size());
		log.info("buffer " + buffer);

		if (tokenizer.hasNext() || matchingNode == null) {
			return null;
		}

		if (matchingNode.cmdHandler != null) {
			((CmdIf) matchingNode.cmdHandler).handleCmd(props);
			return matchingNode;
		}

		return null;
	}

}
