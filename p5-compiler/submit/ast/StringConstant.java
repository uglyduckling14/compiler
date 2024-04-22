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
public class StringConstant implements Expression {

  private final String value;

  public StringConstant(String value) {
    this.value = value;
  }

  public void toCminus(StringBuilder builder, final String prefix) {
    builder.append("\"").append(value).append("\"");
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    if(value.equals("\\n")){
      symbolTable.addSymbol(value, new SymbolInfo("newline", null, false));
    }else {
      symbolTable.addSymbol(value, new SymbolInfo(symbolTable.getUniqueLabel(), null, false));
    }
    code.append(regAllocator.getA()).append(" ").append(symbolTable.find(value).getId()).append("\n");
    code.append("li ").append(regAllocator.getV()).append(" 4\n");
    data.append(symbolTable.find(value).getId()).append(" .asciiz ").append(value).append("\n");
    return MIPSResult.createAddressResult(symbolTable.find(value).getId(), VarType.STRING);
  }

}
