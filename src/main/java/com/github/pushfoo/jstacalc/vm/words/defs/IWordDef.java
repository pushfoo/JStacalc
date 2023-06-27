package com.github.pushfoo.jstacalc.vm.words.defs;

import com.github.pushfoo.jstacalc.tokens.FilePosition;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.Objects.nonNull;


public interface IWordDef {
    int     UNDEFINED  = -1;
    int     getNumPushed();
    int     getNumPopped();
    String  getName();
    String  getHelpText();
    String  getOriginalName();

    /**
     * References to child word defs, it any.
     *
     * @return null or a List of word defs.
     */
    default List<IWordDef> getContents() {
        return null;
    }

    /**
     * The bound method which backs this, if any.
     *
     * @return a Method object
     */
    default Method         getBoundJavaMethod() {
        return null;
    }

    default boolean        isBuiltIn() {
        return nonNull(getBoundJavaMethod());
    }
    default FilePosition   getPosition() {
        return null;
    }
    default int            size() {
        return 1;
    }

    default boolean        isNumPushedKnown() {
        return getNumPushed() >= 0;
    }

    default boolean        isNumPoppedKnown() {
        return getNumPopped() >= 0;
    }

}
