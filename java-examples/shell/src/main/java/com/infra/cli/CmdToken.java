package com.infra.cli;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jline.console.completer.FileNameCompleter;

public class CmdToken implements Comparable<CmdToken> {
    
    private static final Logger log = LogManager.getLogger(CmdToken.class.getName());

    private String       	name;
    private TokenType    	tokenType;
      
    private Class<?>     	enumClass;
    private String 			enumClassName;
  
    private boolean		 	isDynamic;

    FileNameCompleter    	filenameCompleter;
    
    private boolean      	isOptional;
    private boolean      	isRepeatable;
    private String       	helpStr;

    protected CmdToken() {
    	tokenType = TokenType.UNKNOWN;
    }
       
    public CmdToken(String name) {
    	this.name = name;
    	tokenType = TokenType.UNKNOWN;
    }
    
    public CmdToken(String name, TokenType tokenType) {
        this.name = name;
        this.setTokenType(tokenType);
    }

    public CmdToken(String name, TokenType tokenType, String enumClassName) {
        this.name = name;
        this.setTokenType(tokenType);
        try {
			enumClass = ClassLoader.getSystemClassLoader().loadClass(enumClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
        
    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
        if (tokenType == TokenType.FILE) {
            filenameCompleter = new FileNameCompleter();
        }
    }

    public int compareTo(CmdToken to) {
        int retVal = name.compareTo(to.name);
        // just make sure the tokenTypes are same if the names are equal
        if (retVal == 0 && tokenType != to.tokenType) {
            throw new RuntimeException("tokenTypes for " +name + " are not same!!");
        }   else if (retVal == 0 && tokenType == TokenType.ENUM) {
            if (enumClass != to.enumClass) {
                throw new RuntimeException("enumClass for " +name + " are not same!!");
            }
        }
        return retVal;
    }    
    
    public boolean match(String token) {
        switch (tokenType) {
        case KEYWORD:
            if (name.matches(token)) {
                return true;
            }
            return false;
        case ARGUMENT:
            /* match any value */
            return true;
        case ENUM:
            Object[] enumValues = (Object[]) getEnumClass().getEnumConstants();
            for (int i = 0; i < enumValues.length; i++) {
                if (enumValues[i].toString().equalsIgnoreCase(token)) {
                    return true;
                }
            }
            return false;
        case FILE :
            List<CharSequence> candidates = new ArrayList<CharSequence>(1);
            if (filenameCompleter.complete(token, 0, candidates) != -1) {
                return true;
            } else {
                return false;
            }
            
        default :
            return false;
        }
    }
    
    public boolean startsWith(String token) {
        switch (tokenType) {
        case KEYWORD:
            if (name.startsWith(token)) {
                return true;
            }
            return false;
        case ARGUMENT:
            return true;
        case ENUM :
            Object[] enumValues = (Object[]) getEnumClass().getEnumConstants();
            for (int i = 0; i < enumValues.length; i++) {
                if (enumValues[i].toString().toLowerCase().startsWith(token.toLowerCase())) {
                    return true;
                }
            }
            return false;
        case FILE :
            List<CharSequence> candidates = new ArrayList<CharSequence>(1);
            log.info("file match : tok " + token);
            filenameCompleter.complete(token, 0, candidates);

            if (candidates.size() != 0) {
                return true;
            }
            return false;

        default :
            return false;
        }
    }
    
    public String toString() {
    	String suffix = "";
    	String prefix = "";
    	
    	if (isOptional()) {
    		prefix = "[ ";
    		suffix = " ]";
    	}
    	if (isRepeatable()) {
    		prefix = "{ " + prefix;
    		suffix = suffix + " }+";
    	}
    	
        if (tokenType == TokenType.ARGUMENT ||
                tokenType == TokenType.ENUM ||
                tokenType == TokenType.FILE) {

            return prefix + "<" + name + ">" + suffix;
        }
        return  prefix + name + suffix;
    }
    
    
    public String getName() {
        return name;
    }

    public boolean isOptional() {
		return isOptional;
	}


	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}


	public boolean isRepeatable() {
		return isRepeatable;
	}


	public void setRepeatable(boolean isRepeatable) {
		this.isRepeatable = isRepeatable;
	}


	public String getHelpStr() {
		return helpStr;
	}


	public void setHelpStr(String helpStr) {
		this.helpStr = helpStr;
	}


	public Class<?> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<?> enumClass) {
        this.enumClass = enumClass;
    }
    
	public String getEnumClassName() {
		return enumClassName;
	}

	public void setEnumClassName(String enumClassName) {
		this.enumClassName = enumClassName;
	}

	public boolean isDynamic() {
		return isDynamic;
	}

	public void setDynamic(boolean isDynamic) {
		this.isDynamic = isDynamic;
	}

	public CmdNode getCliCmdNodeExp() {
		if (isOptional()) {
			 CmdNode newRoot = new CmdNode();
             newRoot.addChoice(CmdNode.nodeNull);
             newRoot.addChoice(new CmdNode(this));
             return newRoot;
		}
		
		return new CmdNode(this);
	}


	public String toStringToken() {
		return name;
	}
}
