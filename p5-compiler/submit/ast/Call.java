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
    code.append("# ").append(id).append("\n");
    for (Expression arg:args) {
      MIPSResult a = arg.toMIPS(code, data, symbolTable, regAllocator);
      if(a.getRegister() != null) {
        code.append("move ").append(regAllocator.getA()).append(" ").append(a.getRegister()).append("\n");
        code.append("li ").append(regAllocator.getV()).append(" 1\n");
      }
      code.append("syscall\n");
      regAllocator.clearAll();
    }
    if(id.equals("println")){
      StringConstant con = new StringConstant("\"\\n\"");
      con.toMIPS(code, data, symbolTable, regAllocator);
      code.append("syscall\n");
      regAllocator.clearAll();
    }
    return MIPSResult.createVoidResult();
  }

}
