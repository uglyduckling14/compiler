/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edwajohn
 */
public class Call implements Expression {

  private final String id;
  private final List<Expression> args;

  public Call(String id, List<Expression> args) {
    this.id = id;
    this.args = new ArrayList<>(args);
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(id).append("(");
    for (Expression arg : args) {
      arg.toCminus(builder, prefix);
      builder.append(", ");
    }
    if (!args.isEmpty()) {
      builder.setLength(builder.length() - 2);
    }
    builder.append(")");
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    //load args address
    for (Expression arg:args) {
      code.append("la ");
      arg.toMIPS(code, data, symbolTable, regAllocator);
      code.append("syscall\n");
      regAllocator.clearAll();
    }
    if(id.equals("println")){
      code.append("la ");
      StringConstant con = new StringConstant("\\n");
      con.toMIPS(code, data, symbolTable, regAllocator);
      code.append("syscall\n");
      regAllocator.clearAll();
    }
    return null;
  }

}
