package com.github.pushfoo.jstacalc.vm;

import com.github.pushfoo.jstacalc.vm.common.VMStack;
import com.github.pushfoo.jstacalc.vm.words.IExecutionPosition;
import com.github.pushfoo.jstacalc.vm.words.dictionary.IVMDictionary;
import com.github.pushfoo.jstacalc.vm.words.dictionary.VMLinearDictionary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
public class VMCore {

    @Getter @Setter
    VMRunStateEnum                        runState;
    protected VMStack<Integer>            dataStack;
    protected VMStack<IExecutionPosition> returnStack;
    protected IVMDictionary               dictionary;

    public VMCore(VMRunStateEnum runState) {
        dataStack   = new VMStack<>();
        returnStack = new VMStack<>();
        dictionary  = new VMLinearDictionary();
    }

    public VMCore() {
        this(VMRunStateEnum.PAUSED);
    }

    /* TODO: refactor this ugliness once we have something running...
       it's only here because it makes the logic for built-in methods much simpler. */
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
