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
    code.append("# Get ").append(id).append("'s offset from $sp from the symbol table and initialize ").append(id).append("'s address with it. We'll add $sp later.").append("\n");
    String t = regAllocator.getT();
    if(symbolTable.find(id)==null) {

      SymbolInfo info = new SymbolInfo(id, null, false);
      info.setOffset(-4+symbolTable.getSize()*4);
      symbolTable.addSymbol(id, info);
      code.append("li ").append(t).append(" ").append(info.getOffset()).append("\n");

    }
    else{
      code.append("li ").append(t).append(" ").append(symbolTable.find(id).getOffset()).append("\n");
    }
    code.append("# Add the stack pointer address to the offset.\n");
    regAllocator.ops(code, t, "$sp", BinaryOperatorType.PLUS);
    SymbolInfo info = new SymbolInfo(t, null, false);
    symbolTable.addSymbol("$sp", info);
    return MIPSResult.createAddressResult(id, VarType.INT);
  }

}
