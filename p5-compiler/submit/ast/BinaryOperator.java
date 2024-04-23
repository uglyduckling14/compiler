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
public class BinaryOperator implements Expression {

  private final Expression lhs, rhs;
  private final BinaryOperatorType type;

  public BinaryOperator(Expression lhs, BinaryOperatorType type, Expression rhs) {
    this.lhs = lhs;
    this.type = type;
    this.rhs = rhs;
  }

  public BinaryOperator(Expression lhs, String type, Expression rhs) {
    this.lhs = lhs;
    this.type = BinaryOperatorType.fromString(type);
    this.rhs = rhs;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    lhs.toCminus(builder, prefix);
    builder.append(" ").append(type).append(" ");
    rhs.toCminus(builder, prefix);
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    MIPSResult l = lhs.toMIPS(code, data, symbolTable, regAllocator);
    if(l.getRegister()==null){
      code.append("# Load the value of ").append(l.getAddress()).append(".\n");
      //symbolTable.find(l.getAddress()).getId();
      String t1 = regAllocator.getT();
      code.append("lw ").append(t1).append(" ").append(symbolTable.find(l.getAddress()).getOffset()+4).append("(").append(symbolTable.find("$sp").getId()).append(")\n");
      MIPSResult r = rhs.toMIPS(code, data, symbolTable, regAllocator);
      code.append("# Load the value of ").append(r.getAddress()).append(".\n");
      String t2 = regAllocator.getT();
      code.append("lw ").append(t2).append(" ").append(symbolTable.find(r.getAddress()).getOffset()+4+4).append("(").append(symbolTable.find("$sp").getId()).append(")\n");
      regAllocator.ops(code, t1, t2, type);
      return MIPSResult.createRegisterResult(t1, VarType.INT);
    }else {
      MIPSResult r = rhs.toMIPS(code, data, symbolTable, regAllocator);
      regAllocator.ops(code, l.getRegister(), r.getRegister(), type);
      return MIPSResult.createRegisterResult(l.getRegister(), VarType.INT);
    }
  }

}
