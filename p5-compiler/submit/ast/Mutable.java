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
    int offset = symbolTable.find("$sp").getOffset();
    if(symbolTable.find(id).getOffset()==-1){
      symbolTable.find(id).setOffset((offset)+1);
      symbolTable.find("$sp").setOffset(offset-4);
    }
    String t = regAllocator.getT();
    code.append("li ").append(t).append(" ").append(symbolTable.find(id).getOffset()).append("\n");
    code.append("# Add the stack pointer address to the offset.\n");
    regAllocator.ops(code, t, "$sp", BinaryOperatorType.PLUS);
    symbolTable.addSymbol("$sp", new SymbolInfo(t, null, false));

    return MIPSResult.createAddressResult(id, null);
  }

}
