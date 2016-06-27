import java.util.*;

public class BasicLSemanticVisitor implements basicLVisitor
{
	private static HashMap<String, HashMap<String, STC>> symbolTable = new HashMap<>();
	private static String GLOBAL_SCOPE = "Global";
	private static String scope = GLOBAL_SCOPE;
	private static HashSet<String> Functions_Called = new HashSet<>();

	public Object visit(ASTProgram node, Object data)
	{
		symbolTable.put(scope, new HashMap<String, STC>());
		node.childrenAccept(this, data);

		System.out.println();
		System.out.println("------------------------------Symbol Table----------------------------------");
		System.out.println("----------------------------------------------------------------------------");

		/*for (HashMap.Entry<String, HashMap<String, STC>> entry : symbolTable.entrySet())
		{
			//HashMap<String, STC> sScope = symbolTable.get(scope);
			HashMap<String, STC> m = (HashMap<String, STC>) entry;
			for (String key : m.keySet())
			{

			}
			//STC value = (STC)entry.getValue();
			//if (m instanceof Map)
			//{
			//	STC s = m.getValue();
			//}
		}*/

        for (String scope : symbolTable.keySet())
		{
            HashMap<String, STC> stwithScope = symbolTable.get(scope);
            for (String key : stwithScope.keySet())
			{
                STC sytChild = stwithScope.get(key);

				System.out.println("=============================");
				System.out.println("SCOPE: " + sytChild.getScope());
				System.out.println("KIND:  " + sytChild.type());
				System.out.println("TYPE:  " + sytChild.getType());
				System.out.println("ID:    " + sytChild.getId());
			}
        }
		return null;
	} 

	public Object visit(ASTconst_decl node, Object data)
	{
        HashMap<String, STC> stwithScope = symbolTable.get(scope);
        if(stwithScope == null)
			stwithScope = new HashMap<String, STC>();

        for (int i = 0; i < node.jjtGetNumChildren(); i+=3)
		{
        	Token id = (Token) node.jjtGetChild(i).jjtAccept(this, data);
        	Token type = (Token) node.jjtGetChild(i+1).jjtAccept(this, data);
        	List<Token> att;
            if (node.jjtGetChild(i+2).jjtAccept(this, data) instanceof Token)
			{
                att = new ArrayList<>();
                att.add((Token) node.jjtGetChild(i).jjtAccept(this, data));
            }
			else
			{
                att = ((List<Token>) node.jjtGetChild(i).jjtAccept(this, data));
            }
            STC var = new STC(id, type, scope, "Const");

			checkForDuplicates(stwithScope, id, var);
        }
        return null;
    }
		

	public Object visit(ASTVarDecl node, Object data)
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i += 2)
		{
			List<Token> idList = (List<Token>) node.jjtGetChild(i).jjtAccept(this, data);
			Token type = (Token) node.jjtGetChild(i+1).jjtAccept(this, data);

			for (Token id : idList)
			{
				HashMap<String, STC> symtScoped = symbolTable.get(scope);
				if (symtScoped == null) symtScoped = new HashMap<>();
				STC var = new STC(id, type, scope, "Variable");
				HashMap<String, STC> globalScopeSymt = symbolTable.get(GLOBAL_SCOPE);

            	if (globalScopeSymt.get(id.image) != null)
				{
					System.out.print("!!!!!! SEMANTTIC ERROR !!!!!!!!");
            		System.out.println("Variable: " + id.image + " previously Defined");
            	}
				else checkForDuplicates(symtScoped,id,var);
			}
		}
		return null;
	}
	
	public Object visit(ASTFunction node, Object data)
	{
		Token type = (Token) node.jjtGetChild(0).jjtAccept(this, data);
		Token id = (Token) node.jjtGetChild(1).jjtAccept(this, data);
		List<Token> list = (List<Token>) node.jjtGetChild(2).jjtAccept(this, data);

		STC function = new STC(id, type, scope, "Function");

		HashMap<String, STC> sytAtGlobalScope = symbolTable.get(scope);

		checkForDuplicates(sytAtGlobalScope,id,function);
		scope = id.image;

		for (int i = 0; i < list.size(); i += 2)
		{
			Token pid = list.get(i);
			Token ptype = list.get(i + 1);
			STC var = new STC(pid, ptype, scope, "Variable");

			HashMap<String, STC> symtForFunction = symbolTable.get(scope);
			if (symtForFunction == null)
			{
				symtForFunction = new HashMap<>();
			}
			checkForDuplicatesB(symtForFunction,pid,var,ptype);
		}

		Token functionBody = (Token) node.jjtGetChild(3).jjtAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTFunctionCall node, Object data)
	{
		/*Token id = (Token) node.jjtGetChild(0).jjtAccept(this, data);
		List<Token> args = (List<Token>) node.jjtGetChild(1).jjtAccept(this, data);

		HashMap<String, STC> global_Scope_Sym_Tab = symbolTable.get(GLOBAL_SCOPE);
		STC func = global_Scope_Sym_Tab.get(id.image);

		if(func != null && function.type() == "Function")
		{
			Functions_Called.add(id.image);
			if(func.get("parameters") != null)
			{
				List<Token> params = (List<Token>) func.get("parameters");
				if(args.size() != params.size()/2)
				{
					System.out.println("!!!!!! SEMANTTIC ERROR !!!!!!!!");
					System.out.println();
					System.out.println("Arguments size for " + id.image + "  " + scope + "  incorrect");
				}
			}
		}
		else
		{
			System.out.println("Function " + id.image + " does not exist.");
		/*///}
		return null;
	}


	public Object visit(ASTIdList node, Object data)
	{
		List<Token> tokens = new ArrayList<>();
		for (int i = 0; i < node.jjtGetNumChildren(); i++)
		{
			SimpleNode oNode = (SimpleNode) node.jjtGetChild(i);
			Token id = (Token) oNode.jjtGetValue();
			tokens.add(id);
		}
		return tokens;
	}
	
	public Object visit(ASTparam_list node, Object data)
	{
		List<Token> tokens = new ArrayList<Token>();
		for (int i = 0; i < node.jjtGetNumChildren(); i += 1)
		{
			SimpleNode oNode = (SimpleNode) node.jjtGetChild(i);
			Token token = (Token) oNode.jjtGetValue();
			tokens.add(token);
		}
		return tokens;
	}
	
	public Object visit(ASTmain_prog node, Object data)
	{
		scope = "Main";
		node.childrenAccept(this, data);
		return null;
	}

	public Object visit(ASTId node, Object data) {	
		return node.jjtGetValue();
	}

	public Object visit(ASTNumber node, Object data) {
		return node.jjtGetValue();
	}
	
	public Object visit(ASTCondition node, Object data)
	{
		node.childrenAccept(this, data);
		return null;
	}

	public Object visit(ASTType node, Object data) {
		return node.jjtGetValue();
	}

	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	public static void checkForDuplicates(HashMap<String, STC> hm,Token id,STC var)
	{
		if (hm.get(id.image) == null)
		{
			hm.put(id.image, var);
			symbolTable.put(scope, hm);
		}
		else
		{
			System.out.println("!!!!!! SEMANTTIC ERROR !!!!!!!!");
			System.out.println();
			System.out.println("ID: " + id.image + " Previously Defined: ");
			System.out.println();
		}

	}
	public static void checkForDuplicatesB(HashMap<String, STC> hm,Token id, STC var,Token type)
	{
		if (hm.get(id.image) == null)
		{
			hm.put(id.image, var);
			symbolTable.put(scope, hm);
		}
		else
		{
			System.out.print("!!!!!! SEMANTTIC ERROR !!!!!!!!");
			System.out.println("Parameter " + id.image + " type: " + type.image + " previously Defined");
			System.out.println();
		}
	}
}	
