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
    MIPSResult m = mutable.toMIPS(code, data, symbolTable, regAllocator);
    code.append("# Compute rhs for assignment ").append(type).append("\n");
    MIPSResult r = rhs.toMIPS(code, data, symbolTable, regAllocator);
    code.append("# complete assignment statement with store\n");
    code.append("sw ").append(r.getRegister()).append(" ").append(0).append("(").append(symbolTable.find("$sp").getId()).append(")\n");
    regAllocator.clearAll();
    return r;
  }

}
