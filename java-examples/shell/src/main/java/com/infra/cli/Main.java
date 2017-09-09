package com.infra.cli;

import java.util.Properties;


public class Main {

	public static void main(String[] args) {
	
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
		
		@SuppressWarnings("unused")
		CmdLineParser parser = new CmdLineParser(tree);
		
	}
		

}
