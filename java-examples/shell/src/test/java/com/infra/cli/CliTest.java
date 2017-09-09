package com.infra.cli;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.infra.cli.CmdIf;
import com.infra.cli.CmdNode;
import com.infra.cli.CmdToken;
import com.infra.cli.TokenType;
import com.infra.cli.Tokenizer;


public class CliTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public CliTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(CliTest.class);
	}

	public void testCli() {
		CmdToken show = new CmdToken("show", TokenType.KEYWORD);
		
		CmdNode cmd1 = new CmdNode(show);
		String cmd1Str  = "job jobid log";
    
		Tokenizer tk = new Tokenizer(cmd1Str);
		while (tk.hasNext()) {
			CmdNode cn = new CmdNode(new CmdToken(tk.nextToken(), TokenType.KEYWORD));
			cmd1.append(cn);	
		}
		
		cmd1.getLast().setCmdHandler(new CmdIf() {

			public void handleCmd(Properties props) {
				System.out.println("exec show job jobid log");		
			}
			
		});
			
		CmdNode cmd2 = new CmdNode(show);
		String cmd2Str  = "job jobid jobhistory";
    
		tk = new Tokenizer(cmd2Str);
		while (tk.hasNext()) {
			CmdNode cn = new CmdNode(new CmdToken(tk.nextToken(), TokenType.KEYWORD));
			cmd2.append(cn);	
		}
		
		cmd2.getLast().setCmdHandler(new CmdIf() {

			public void handleCmd(Properties props) {
				System.out.println("exec show job jobid jobhistory");		
			}
			
		});
		
		CmdNode tree = cmd1.merge(cmd2);
			
		CmdNode cmd3 = new CmdNode(show);    
		CmdNode cn = new CmdNode(new CmdToken("tech-support", TokenType.KEYWORD));
		cmd3.append(cn);	
		
		cn = new CmdNode();
		CmdNode cn1 = new CmdNode(new CmdToken("local", TokenType.KEYWORD));
		CmdNode cn2 = new CmdNode(new CmdToken("global", TokenType.KEYWORD));
		cn.addChoice(cn1);
		cn.addChoice(cn2);
		cmd3.append(cn);
		
		cmd3.getLast().setCmdHandler(new CmdIf() {

			public void handleCmd(Properties props) {
				System.out.println("exec show tech-support [local | global]");		
			}
			
		});
		
		tree = tree.merge(cmd3);
		
		tree.print(0);
		
//		@SuppressWarnings("unused")
//		CmdLineParser parser = new CmdLineParser(tree);
		
	}
}
