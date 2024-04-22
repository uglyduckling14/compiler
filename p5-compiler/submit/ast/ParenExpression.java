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
public class ParenExpression implements Expression {

  private final Expression expression;

  public ParenExpression(Expression expression) {
    this.expression = expression;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append("(");
    expression.toCminus(builder, prefix);
    builder.append(")");
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {

    return null;
  }

}
