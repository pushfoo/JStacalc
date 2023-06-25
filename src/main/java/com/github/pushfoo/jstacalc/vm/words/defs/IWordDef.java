package com.github.pushfoo.jstacalc.vm.words.defs;

import com.github.pushfoo.jstacalc.tokens.FilePosition;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.Objects.nonNull;


public interface IWordDef {
    final   static int     UNKNOWN = -1;
    public  int            getNumRequired();
    public  int            getNumPushed();
    public  int            getNumPopped();
    public  String         getName();

    public  String         getOriginalName();

    /**
     * References to child word defs, it any.
     *
     * @return null or a List of word defs.
     */
    default  public  List<IWordDef> getContents() {
        return null;
    }

    /**
     * The bound method which backs this, if any.
     *
     * @return a Method object
     */
    default  public  Method         getBoundJavaMethod() {
        return null;
    }

    default  public  boolean        isBuiltIn()   { return nonNull(getBoundJavaMethod()); }
    default  public  FilePosition   getPosition() { return null; }
    default  public  int            size()        { return 1;    }

    default  public  boolean        isNumPushedKnown() {
        return getNumPushed() <= UNKNOWN;
    }

    default  public  boolean        isNumRequiredKnown() {
        return getNumRequired() <= UNKNOWN;
    }
    default  public  boolean        isNumPoppedKnown() {
        return getNumPopped() <= UNKNOWN;
    }
}
