package submit;

import submit.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Code formatter project
 * CS 4481
 */
/**
 *
 */
public class SymbolTable {

  private final HashMap<String, SymbolInfo> table;
  private SymbolTable parent;
  private int uniqueLabelCounter;
  private final List<SymbolTable> children;

  public SymbolTable() {
    table = new HashMap<>();
    parent = null;
    children = new ArrayList<>();
    uniqueLabelCounter = 0;
    addSymbol("println", new SymbolInfo("println", null, true));
  }

  public void addSymbol(String id, SymbolInfo symbol) {
    table.put(id, symbol);
  }

  /**
   * Returns null if no symbol with that id is in this symbol table or an
   * ancestor table.
   *
   * @param id
   * @return
   */
  public SymbolInfo find(String id) {
    if (table.containsKey(id)) {
      return table.get(id);
    }
    if (parent != null) {
      return parent.find(id);
    }
    return null;
  }

  /**
   * Returns the new child.
   *
   * @return
   */
  public SymbolTable createChild() {
    SymbolTable child = new SymbolTable();
    children.add(child);
    child.parent = this;
    return child;
  }

  public SymbolTable getParent() {
    return parent;
  }
  public List<SymbolInfo> getAllSymbols() {
    // Add symbols from this table
    List<SymbolInfo> allSymbols = new ArrayList<>(table.values());
    // Recursively add symbols from children
    for (SymbolTable child : children) {
      allSymbols.addAll(child.getAllSymbols());
    }
    return allSymbols;
  }
  public String getUniqueLabel(){
    String t = "datalabel"+uniqueLabelCounter;
    uniqueLabelCounter++;
    return t;
  }
}
