package com.github.pushfoo.jstacalc.vm.words.dictionary;

import com.github.pushfoo.jstacalc.vm.exceptions.VMBuiltInDefinitionException;
import com.github.pushfoo.jstacalc.vm.words.defs.IWordDef;

import java.lang.reflect.Method;

/**
 * Define this as an interface to make it easier to replace the linear dictionary.
 */
public interface IVMDictionary {
    public void defineSingleBuiltIn(Method method) throws VMBuiltInDefinitionException;
    public void defineBuiltIns(Class<?> clazz) throws VMBuiltInDefinitionException;
    public void defineBuiltIns(Class<?>... classes) throws VMBuiltInDefinitionException;
    public void defineWords(Iterable<? extends IWordDef> words);
    public void defineSingleWord(IWordDef word);
    public IWordDef get(String word);
    public int size();
}
