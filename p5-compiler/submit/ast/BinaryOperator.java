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
    MIPSResult r = rhs.toMIPS(code, data, symbolTable, regAllocator);
    if(type == BinaryOperatorType.PLUS){
      code.append("add ").append(l.getRegister()).append(" ").append(l.getRegister()).append(" ").append(r.getRegister()).append("\n");
    } else if (type == BinaryOperatorType.DIVIDE) {
      code.append("div ").append(l.getRegister()).append(" ").append(r.getRegister()).append("\n");
      code.append("mflo ").append(l.getRegister()).append("\n");
      regAllocator.clear(r.getRegister());
    } else if (type == BinaryOperatorType.TIMES){
      code.append("mult ").append(l.getRegister()).append(" ").append(r.getRegister()).append("\n");
      code.append("mflo ").append(l.getRegister()).append("\n");
      regAllocator.clear(r.getRegister());
    }
    return MIPSResult.createRegisterResult(l.getRegister(), VarType.INT);
  }

}
