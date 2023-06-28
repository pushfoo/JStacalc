package com.github.pushfoo.jstacalc.vm.words.dictionary;

import com.github.pushfoo.jstacalc.vm.words.defs.IWordDef;

/**
 * Define this as an interface to make it easier to replace the linear dictionary.
 */
public interface IVMDictionary {
    public void define(IWordDef word);
    public int getNumBuiltIns();
    public IWordDef get(String word);
    public void forget(String word);
    public int size();
}
