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
public class Assignment implements Expression, Node {

  private final Mutable mutable;
  private final AssignmentType type;
  private final Expression rhs;

  public Assignment(Mutable mutable, String assign, Expression rhs) {
    this.mutable = mutable;
    this.type = AssignmentType.fromString(assign);
    this.rhs = rhs;
  }

  public void toCminus(StringBuilder builder, final String prefix) {
    mutable.toCminus(builder, prefix);
    if (rhs != null) {
      builder.append(" ").append(type.toString()).append(" ");
      rhs.toCminus(builder, prefix);
    } else {
      builder.append(type.toString());

    }
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    MIPSResult a = mutable.toMIPS(code, data, symbolTable, regAllocator);
    code.append("# Compute rhs for assignment ").append(type).append("\n");
    MIPSResult r = rhs.toMIPS(code, data, symbolTable, regAllocator);
    code.append("# complete assignment statement with store\n");
    //regAllocator.saveT(code, symbolTable.find(a.getRegister()).getOffset());
    code.append("sw").append(" ").append(r.getRegister()).append(" ").append(symbolTable.find(a.getRegister()).getOffset()).append("(").append(a.getRegister()).append(")\n");
    regAllocator.clearAll();
    return MIPSResult.createVoidResult();
  }

}
