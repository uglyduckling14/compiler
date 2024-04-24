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
public class FunDeclaration implements Declaration, Node {

  private final VarType returnType;
  private final String id;
  private final ArrayList<Param> params;
  private final Statement statement;

  public FunDeclaration(VarType returnType, String id, List<Param> params,
          Statement statement) {
    this.returnType = returnType;
    this.id = id;
    this.params = new ArrayList<>(params);
    this.statement = statement;
  }

  public void toCminus(StringBuilder builder, final String prefix) {
    String rt = (returnType != null) ? returnType.toString() : "void";
    builder.append("\n").append(rt).append(" ");
    builder.append(id);
    builder.append("(");

    for (Param param : params) {
      param.toCminus(builder, prefix);
      builder.append(", ");
    }
    if (!params.isEmpty()) {
      builder.delete(builder.length() - 2, builder.length());
    }
    builder.append(")\n");
    statement.toCminus(builder, prefix);
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    code.append("# code for ").append(id).append("\n");
    code.append(id).append(":").append("\n");
    code.append("# Entering a new scope.").append("\n");
    code.append("# Symbols in symbol table:\n");
    for (SymbolInfo info : symbolTable.getAllSymbols()) {
      code.append("# ").append(info.getId()).append("\n");
    }
    code.append("# Update the stack pointer.\n");
    code.append("addi $sp $sp -").append(symbolTable.find("$sp").getOffset()+1).append("\n");

    return statement.toMIPS(code, data, symbolTable,regAllocator);
  }
}
