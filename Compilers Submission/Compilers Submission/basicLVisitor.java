/* Generated By:JavaCC: Do not edit this line. basicLVisitor.java Version 5.0 */
public interface basicLVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTProgram node, Object data);
  public Object visit(ASTVarDecl node, Object data);
  public Object visit(ASTconst_decl node, Object data);
  public Object visit(ASTFunction node, Object data);
  public Object visit(ASTparam_list node, Object data);
  public Object visit(ASTType node, Object data);
  public Object visit(ASTmain_prog node, Object data);
  public Object visit(ASTFunctionCall node, Object data);
  public Object visit(ASTCondition node, Object data);
  public Object visit(ASTIdList node, Object data);
  public Object visit(ASTNumber node, Object data);
  public Object visit(ASTId node, Object data);
}
/* JavaCC - OriginalChecksum=1621abc4dfa85b8436245aa87b18e063 (do not edit this line) */