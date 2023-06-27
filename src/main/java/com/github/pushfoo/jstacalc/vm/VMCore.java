package com.github.pushfoo.jstacalc.vm;

import com.github.pushfoo.jstacalc.vm.common.VMStack;
import com.github.pushfoo.jstacalc.vm.words.IExecutionPosition;
import com.github.pushfoo.jstacalc.vm.words.dictionary.IVMDictionary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@AllArgsConstructor
@SuperBuilder
public class VMCore {

    @Getter @Setter
    @Builder.Default
    VMRunStateEnum                           runState = VMRunStateEnum.PAUSED;
    private VMStack<Integer>                 dataStack;
    private VMStack<IExecutionPosition>       returnStack;
    IVMDictionary                            dictionary;

    public VMCore() {
        dataStack   = new VMStack<>();
        returnStack = new VMStack<>();
    }

    public Integer pop() {
        return dataStack.pop();
    }

    public void push(Integer i) {
        dataStack.push(i);
    }

    public VMStack<Integer> pop(int i) {
        return dataStack.pop(i);
    }
    public int dataStackSize() {
        return dataStack.size();
    }
    public void push(Collection<Integer> data) {
        dataStack.push(data);
    }
    public Integer peek() {
        return dataStack.peek();
    }

    public VMStack<Integer> peek(int n) {
        return dataStack.peek(n);
    }
}
