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
    code.append("# Entering a new scope.").append("\n");
    code.append("# Symbols in symbol table:\n");
//    for (SymbolInfo info : symbolTable.getAllSymbols()) {
//      code.append("# ").append(info.getId()).append("\n");
//    }
    code.append("# Update the stack pointer.\n");
    code.append("addi $sp $sp ").append(symbolTable.find("$sp").getOffset()+1).append("\n");
    symbolTable.find("$sp").setOffset(symbolTable.find("$sp").getOffset()-4);
    for (Statement statement:statements) {
      statement.toMIPS(code,data,symbolTable,regAllocator);
    }
    code.append("# Exiting scope.\n").append("addi $sp $sp ").append(symbolTable.find("$sp").getOffset()+1).append("\n");
    symbolTable.find("$sp").setOffset(symbolTable.find("$sp").getOffset()+4);
    return MIPSResult.createVoidResult();
  }

}
