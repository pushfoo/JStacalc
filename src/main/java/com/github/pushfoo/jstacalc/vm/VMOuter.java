package com.github.pushfoo.jstacalc.vm;

import com.github.pushfoo.jstacalc.vm.exceptions.*;
import com.github.pushfoo.jstacalc.vm.words.defs.AtomicBuiltInWordDef;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static com.github.pushfoo.jstacalc.vm.words.defs.Predicates.hasBuiltInAnnotation;

public class VMOuter {

    private final VMCore core;

    public VMOuter() {
        core = new VMCore();
    }

    public List<Integer> getDataStackState() {
        return core.peek(core.dataStackSize())
                .stream()
                .toList();
    }
    public void defineBuiltInFromMethod(Method method) throws VMBuiltInAnnotationException, VMBuiltInSignatureException {
        AtomicBuiltInWordDef def = AtomicBuiltInWordDef.fromAnnotatedStaticMethod(method);
        core.dictionary.define(def);
    }

    public void defineBuiltInsFromClasses(Class<?>... classes)
            throws VMBuiltInAnnotationException, VMBuiltInSignatureException {
        Iterator<Method> methodIterator = Stream.of(classes)
                .flatMap(c -> Arrays.stream(c.getMethods()))
                .filter(hasBuiltInAnnotation)
                .iterator();
        while ( methodIterator.hasNext() ) {
            defineBuiltInFromMethod(methodIterator.next());
        }
    }

}
