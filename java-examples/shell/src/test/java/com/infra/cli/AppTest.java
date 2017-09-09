package com.infra.cli;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.infra.cli.CmdNode;
import com.infra.cli.CmdToken;
import com.infra.cli.TokenType;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	
	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {

		/*
		 * S -> ABCd A -> e | f | null B -> g | h | null C -> p | q
		 * 
		 */

		CmdNode e = new CmdNode(new CmdToken("e", TokenType.KEYWORD));
		CmdNode f = new CmdNode(new CmdToken("f", TokenType.KEYWORD));
		CmdNode g = new CmdNode(new CmdToken("g", TokenType.KEYWORD));
		CmdNode h = new CmdNode(new CmdToken("h", TokenType.KEYWORD));
		CmdNode p = new CmdNode(new CmdToken("p", TokenType.KEYWORD));
		CmdNode q = new CmdNode(new CmdToken("q", TokenType.KEYWORD));
		CmdNode d = new CmdNode(new CmdToken("d", TokenType.KEYWORD));

		CmdNode A = new CmdNode();
		A.merge(e).merge(f).merge(CmdNode.nodeNull);

		CmdNode B = new CmdNode();
		B.merge(g).merge(h).merge(CmdNode.nodeNull);

		CmdNode C = new CmdNode();
		C.merge(p).merge(q);

		CmdNode S = new CmdNode();
		S.append(A);
		S.append(B);
		S.append(C);
		S.append(d);

		S.print(0);
		assertTrue(true);

		/*
		 * show -> job -> jobid -> log (CMD1) show -> job -> jobid -> history
		 * (CMD2)
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
	}
}
