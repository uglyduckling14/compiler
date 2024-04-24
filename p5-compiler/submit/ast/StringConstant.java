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
    if(value.equals("\"\\n\"")&& symbolTable.find("\"\\n\"") == null && !data.toString().contains("newline:")){
      symbolTable.addSymbol(value, new SymbolInfo("newline", null, false));
      data.append(symbolTable.find(value).getId()).append(":").append(" .asciiz ").append(value).append("\n");
    }else if(!value.equals("\"\\n\"")) {
      symbolTable.addSymbol(value, new SymbolInfo(symbolTable.getUniqueLabel(), null, false));
      data.append(symbolTable.find(value).getId()).append(":").append(" .asciiz ").append(value).append("\n");
    }
    code.append("la ");

    if(value.equals("\"\\n\"")){
      code.append(regAllocator.getA()).append(" ").append("newline").append("\n");
      code.append("li ").append(regAllocator.getV()).append(" 4\n");
      return MIPSResult.createAddressResult("newline", VarType.STRING);
    }else{
      code.append(regAllocator.getA()).append(" ").append(symbolTable.find(value).getId()).append("\n");
      code.append("li ").append(regAllocator.getV()).append(" 4\n");
      return MIPSResult.createAddressResult(symbolTable.find(value).getId(), VarType.STRING);
    }

  }

}
