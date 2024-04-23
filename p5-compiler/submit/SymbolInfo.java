/*
 * Code formatter project
 * CS 4481
 */
package submit;

import submit.ast.VarType;

/**
 *
 * @author edwajohn
 */
public class SymbolInfo {

  private final String id;
  // In the case of a function, type is the return type
  private final VarType type;
  private final boolean function;

  private int offset;

  public SymbolInfo(String id, VarType type, boolean function) {
    this.id = id;
    this.type = type;
    this.function = function;
  }
  @Override
  public String toString() {
    return "<" + id + ", " + type + '>';
  }
  public String getId(){
    return id;
  }
  public int setOffset(int o){
    offset=o;
    return offset;
  }
  public int getOffset(){
    return offset;
  }
}
