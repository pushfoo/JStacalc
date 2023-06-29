package com.github.pushfoo.jstacalc.vm.words.builtins;

import com.github.pushfoo.jstacalc.vm.VMCore;
import com.github.pushfoo.jstacalc.vm.VMRunStateEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ExecutionStateTest {

    @ParameterizedTest
    @EnumSource(VMRunStateEnum.class)
    void whenByeBuiltInCalled_On_stateIsPassedValueAfterward(VMRunStateEnum initialState) {
        VMCore core = new VMCore(initialState);
        ExecutionState.bye(core);
        assertEquals(VMRunStateEnum.HALTED, core.getRunState());
    }

}
