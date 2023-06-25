package com.github.pushfoo.jstacalc.vm;

import com.github.pushfoo.jstacalc.vm.common.VMStack;
import com.github.pushfoo.jstacalc.vm.words.ExecutionPositionRecord;
import com.github.pushfoo.jstacalc.vm.words.dictionary.IVMDictionary;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter @Setter
public class VMCore {
    VMRunStateEnum                   runState;
    VMStack<Integer>                 dataStack;
    VMStack<ExecutionPositionRecord> returnStack;
    IVMDictionary                    dictionary;

    public VMCore() {
        dataStack = new VMStack<>();
    }

    public void step() {

    }
}
