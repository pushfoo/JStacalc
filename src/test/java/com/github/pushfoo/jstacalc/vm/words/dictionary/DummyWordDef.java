package com.github.pushfoo.jstacalc.vm.words.dictionary;

import com.github.pushfoo.jstacalc.vm.words.defs.IWordDef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
class DummyWordDef implements IWordDef {
    protected String name;
    protected String originalName;

    @Builder.Default
    protected int numPopped = UNDEFINED;

    @Builder.Default
    protected int numPushed = UNDEFINED;

    @Builder.Default
    boolean builtIn = false;

    @Builder.Default
    String helpText = "";

    @Builder.Default
    List<IWordDef> contents = null;
}
