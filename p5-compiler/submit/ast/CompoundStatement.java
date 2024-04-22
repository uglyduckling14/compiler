/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.*;

import java.util.List;

/**
 *
 * @author edwajohn
 */
public class CompoundStatement implements Statement {

  private final List<Statement> statements;

  public CompoundStatement(List<Statement> statements) {
    this.statements = statements;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(prefix).append("{\n");
    for (Statement s : statements) {
      s.toCminus(builder, prefix + "  ");
    }
    builder.append(prefix).append("}\n");
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    for (Statement statement:statements) {
      statement.toMIPS(code,data,symbolTable,regAllocator);
    }
    return MIPSResult.createVoidResult();
  }

}
