/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.*;

/**
 *
 * @author edwajohn
 */
public class Mutable implements Expression, Node {

  private final String id;
  private final Expression index;

  public Mutable(String id, Expression index) {
    this.id = id;
    this.index = index;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(id);
    if (index != null) {
      builder.append("[");
      index.toCminus(builder, prefix);
      builder.append("]");
    }
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    code.append("# Get ").append(id).append("'s offset from $sp from the symbol table and initialize ").append(id).append("'s address with it. We'll add $sp later.\n");
    String t = regAllocator.getT();
    symbolTable.addSymbol(t, new SymbolInfo(t, VarType.INT, false));
    regAllocator.li(code, symbolTable.find("$sp").getOffset());
    code.append("# Add the stack pointer address to the offset.\n");
    code.append("add ").append(t).append(" ").append(t).append(" ").append("$sp\n");
    return MIPSResult.createRegisterResult(t,VarType.INT);
  }
}
