package com.github.pushfoo.jstacalc.vm.words.builtins;

import com.github.pushfoo.jstacalc.vm.VMCore;
import com.github.pushfoo.jstacalc.vm.VMRunStateEnum;
import com.github.pushfoo.jstacalc.vm.words.defs.BuiltIn;

public class ExecutionState {
    @BuiltIn(pops = 0, pushes = 0, helpText = "Exit the program")
    static void bye(VMCore core) {
        core.setRunState(VMRunStateEnum.HALTED);
    }
}
