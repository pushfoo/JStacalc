package com.github.pushfoo.jstacalc.vm.words;

import com.github.pushfoo.jstacalc.vm.words.defs.IWordDef;

public interface IExecutionPosition {
    public IWordDef wordDef();
    public int      index();
}
