package com.github.pushfoo.jstacalc.vm.words.defs;

import com.github.pushfoo.jstacalc.vm.VMCore;
import com.github.pushfoo.jstacalc.vm.exceptions.VMBuiltInAnnotationException;
import com.github.pushfoo.jstacalc.vm.exceptions.VMBuiltInSignatureException;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.*;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * <p>A word defined purely in java, indivisible from the VM's perspective.</p>
 *
 * <p>These words have the following properties:</p>
 * <ol>
 *     <li>They are indivisible, and report null contents via getContents</li>
 *     <li>
 *         They know how many operands they will consume ahead of time, allowing
 *         the VM to throw a VMStackUnderflowException before the stack is altered.
 *     </li>
 * </ol>
 */
@Getter
@SuperBuilder
public class AtomicBuiltInWordDef implements IWordDef {

    private String       name;
    // Negative values are used to stand in for unknown values.
    // Use the isNum*Known methods to check whether these hold valid data.
    @Builder.Default
    private int          numPushed   = UNDEFINED;
    @Builder.Default
    private int          numPopped   = UNDEFINED;
    @Builder.Default
    private String       helpText    = "";
    private String       originalName;
    private Method       boundJavaMethod;

    public List<IWordDef> getContents() {
        return null;
    }

    @Override
    public boolean isBuiltIn() {
        return true;
    }

    public static String getRegisteredName(Member member) {
        String nameOverrideString = ((AccessibleObject) member).getAnnotation(BuiltIn.class).name();
        return nameOverrideString.equals("") ? member.getName() : nameOverrideString;
    }

    /**
     * <p>Validate & extract the data from the @BuiltIn annotation to the AtomicBuiltInWordDefBuilder</p>
     *
     * <p>If there is no validation present or its arguments are not valid, an IllegalArgumentException
     * will be raised.</p>
     *
     * @param builder A pre-existing word builder instance.
     * @param member  A Field or static Method obtained through reflection.
     * @param <E>     A field or static method
     * @throws IllegalArgumentException When the annotation is missing or invalid.
     */
    public static <E extends Member> void processAnnotation(
            AtomicBuiltInWordDefBuilder<?, ?> builder, E member
    ) throws VMBuiltInAnnotationException {
        try {
            // Casting trick: both the Field and Method types implement both of these interfaces.
            BuiltIn annotation = ((AccessibleObject) member).getAnnotation(BuiltIn.class);
            if (isNull(annotation)) {
                throw new IllegalArgumentException("No @BuiltIn annotation with pushes and pops arguments.");
            }
            // Not setting the name is ok, we'll just us the method name
            String overrideString = annotation.name();
            String originalName = member.getName();
            builder.name(overrideString.equals("") ? originalName : overrideString);
            builder.originalName(originalName);
            builder.helpText(annotation.helpText());

            // The pops and pushes annotations values must be set, however.
            if (annotation.pops() <= UNDEFINED || annotation.pushes() <= UNDEFINED) {
                throw new IllegalArgumentException("@BuiltIn's pops and pushes must be set to NUM_VARIES or an int >= 0");
            }
        } catch (IllegalArgumentException e) {
            throw new VMBuiltInAnnotationException(builder.name, member, e);
        }
    }

    /**
     * <p>Return true if the method has signature public static void name(VMCore core)</p>
     *
     * <p>Although the method name can be anything, the VMCore's name may be checked in the
     * future.</p>
     *
     * @param method A Method to check, obtained through reflection.
     * @return Whether the method has the signature public static void name(VMCore core).
     */
    public static boolean isValidMethodSignature(Method method) {
        int modifiers = method.getModifiers();
        Parameter[] parameters = method.getParameters();
        return Modifier.isPublic(modifiers)
                // no need to check for abstract because static is mutually exclusive with it
                && Modifier.isStatic(modifiers)
                && method.getReturnType().equals(void.class)
                && parameters.length == 1
                && parameters[0].getType() == VMCore.class;
    }

    /**
     * <p>Build a definition from a static method.</p>
     *
     * <p>The following rules apply to method built-ins:</p>
     * <ol>
     *     <li>The method must have a signature of public static void name(VMCore core)</li>
     *     <li>It must use the @BuiltIn annotation with pops and pushes set to an int value >= 0.</li>
     * </ol>
     *
     *
     * @param method a Method object to build
     * @return An AtomicBuiltinWordDef instance.
     * @throws VMBuiltInSignatureException When the method's signature is not of the form described above
     * @throws VMBuiltInAnnotationException When the method lacks a @BuiltIn annotation of the form described above
     */
    public static AtomicBuiltInWordDef fromAnnotatedStaticMethod(Method method)
            throws VMBuiltInAnnotationException, VMBuiltInSignatureException {
        AtomicBuiltInWordDefBuilder<?, ?> defBuilder = AtomicBuiltInWordDef.builder();
        processAnnotation(defBuilder, method);

        if (! isValidMethodSignature(method)) {
           throw new VMBuiltInSignatureException(
                   defBuilder.name, method,
                   "Method must follow the pattern public static void name(VMCore core)");
        }
        return defBuilder.build();
    }
}
