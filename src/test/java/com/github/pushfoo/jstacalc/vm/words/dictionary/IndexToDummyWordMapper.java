package com.github.pushfoo.jstacalc.vm.words.dictionary;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <p>A functional helper to build words for tests & an experiment in run-time configurability.</p>
 *
 * <p>Have you ever asked yourself any of the following?<p>
 * <ul>
 *  <li>How far can we bend functional practices in Java?</li>
 *  <li>Can we add monkey-patching without reflection or byte code manipulation?</li>
 *  <li>Can we add Python's self argument...to Java?</li>
 * </ul>
 * <p>The answer to all of the above is yes. Should we have? ¯\_(ツ)_/¯</p>
 */
@Builder @Getter @Setter
class IndexToDummyWordMapper implements Function<Integer, DummyWordDef> {

    // Helpers / defaults kept separate to allow their use in composition
    public static Function<Integer, String> alphabetIndexToAscii =  index -> String.valueOf((char) (index + 65));
    public static Function<Integer, String> defaultName = i -> "new_" + alphabetIndexToAscii.apply(i);
    public static Function<Integer, String> defaultOriginalName = i -> "original_" + alphabetIndexToAscii.apply(i);
    public static Function<Integer, Boolean> defaultBuiltIn = i -> i <= 1;
    public static Function<Integer, Integer> defaultNumPushed = i -> i + 1;
    public static Function<Integer, Integer> defaultNumPopped = i -> i;

    // absolutely disgusting, but let's see how far we can take run-time overrides :)
    public static BiFunction<IndexToDummyWordMapper, Integer, DummyWordDef> defaultInternalMapper =
            (self, index) -> DummyWordDef.builder()
                            .name(self.name.apply(index))
                            .originalName(self.originalName.apply(index))
                            .builtIn(self.builtIn.apply(index))
                            .numPushed(self.numPushed.apply(index))
                            .numPopped(self.numPopped.apply(index))
                            .build();

    // The one saving grace of this class: It's immutable, helping keep functions side effect free!
    @NonNull @Builder.Default
    final private  Function<Integer, String> name = defaultName;
    @NonNull @Builder.Default
    final private  Function<Integer, String> originalName = defaultOriginalName;
    @NonNull @Builder.Default
    final private  Function<Integer, Boolean> builtIn = defaultBuiltIn;
    @NonNull @Builder.Default
    final private  Function<Integer, Integer> numPushed = defaultNumPushed;
    @NonNull @Builder.Default
    final private  Function<Integer, Integer> numPopped = defaultNumPopped;

    @NonNull @Builder.Default
    final private  BiFunction<IndexToDummyWordMapper, Integer, DummyWordDef> internalMapper =
            defaultInternalMapper;

    public DummyWordDef apply(Integer index) {
        return internalMapper.apply(this, index);
    }
}
