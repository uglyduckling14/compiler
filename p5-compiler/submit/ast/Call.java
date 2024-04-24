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
    if(id.equals("println")){
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
      StringConstant con = new StringConstant("\"\\n\"");
      con.toMIPS(code, data, symbolTable, regAllocator);
      code.append("syscall\n");
      regAllocator.clearAll();
    }else{
      code.append("# Calling function ").append(id).append("\n");
      String ra = regAllocator.getRa();
      code.append("# Save $ra to register\n");
      code.append("move ").append(regAllocator.getT()).append(" ").append(ra).append("\n");
      code.append("# Save $t0-9 registers").append("\n");
      regAllocator.saveT(code, 0);

      code.append("# Evaluate parameters and save to stack\n");
      code.append("# Update the stack pointer.\n");
      if(symbolTable.find("$sp")!=null) {
        code.append("addi $sp $sp ").append(symbolTable.find("$sp").getOffset()-4).append("\n");
        symbolTable.find("$sp").setOffset(-4);
      }else{
        code.append("addi $sp $sp -").append(4).append("\n");
        SymbolInfo info = new SymbolInfo("", null, false);
        symbolTable.addSymbol("$sp", info);
      }

      for (Expression arg:args) {
        MIPSResult a = arg.toMIPS(code, data, symbolTable, regAllocator);
        if(a.getRegister() != null) {
          code.append("move ").append(regAllocator.getA()).append(" ").append(a.getRegister()).append("\n");
          code.append("li ").append(regAllocator.getV()).append(" 1\n");
        }
        code.append("syscall\n");
        regAllocator.clearAll();
      }

      code.append("# Call the function\n");
      code.append("jal ").append(id).append("\n");
      code.append("# Restore the stack pointer\n");
      regAllocator.ops(code, "$sp", String.valueOf(symbolTable.find("$sp").getOffset()*-1),BinaryOperatorType.PLUS);
      code.append("# Restore $t0-9 registers\n");
      regAllocator.restoreT(code, 0);
      code.append("# Restore $ra\n");
      code.append("move $ra ").append("$t0").append("\n");
      regAllocator.clearAll();
    }
    return MIPSResult.createVoidResult();
  }

}
