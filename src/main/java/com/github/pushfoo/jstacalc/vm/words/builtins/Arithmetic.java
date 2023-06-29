package com.github.pushfoo.jstacalc.vm.words.builtins;

import com.github.pushfoo.jstacalc.vm.VMCore;
import com.github.pushfoo.jstacalc.vm.words.defs.*;


/**
 * Default math built-ins
 *
 */
public class Arithmetic {

    @BuiltIn(name="+", pops = 2, pushes = 1, helpText =
            """
            ( a b -- sum )
            Pop b, pop a, then push a + b
            """)
    public static void add(VMCore core) {
        int b = core.pop();
        int a = core.pop();
        core.push(a + b);
    }

    @BuiltIn(name="-", pops = 2, pushes = 1, helpText =
            """
            (a b -- difference )
            Pop b, pop a, then push a - b
            """)
    public static void sub(VMCore core) {
        int b = core.pop();
        int a = core.pop();
        core.push(a - b);
    }

    @BuiltIn(name="*", pops = 2, pushes = 1, helpText =
            """
            ( a b -- product )
            pop b, pop a, then push a * b"
            """)
    public static void mul(VMCore core) {
        int b = core.pop();
        int a = core.pop();
        core.push(a * b);
    }

    @BuiltIn(name="/", pops = 2, pushes = 1, helpText =
            """
            ( a b -- quotient )
            Pop b, pop a, then push a / b
            """)
    public static void div(VMCore core) {
        if ( core.peek() == 0 ) throw new ArithmeticException("Can't divide by zero");
        int b = core.pop();
        int a = core.pop();
        core.push(a / b);
    }

}
