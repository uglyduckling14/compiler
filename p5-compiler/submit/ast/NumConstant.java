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
public class NumConstant implements Expression, Node {

  private final int value;

  public NumConstant(int value) {
    this.value = value;
  }

  public void toCminus(StringBuilder builder, final String prefix) {
    builder.append(Integer.toString(value));
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    code.append("li ");
    String t = regAllocator.getT();
    symbolTable.addSymbol(String.valueOf(value), new SymbolInfo(t, null, false));
    code.append(t).append(" ").append(value).append("\n");
    return MIPSResult.createRegisterResult(t, VarType.INT);
  }
}
