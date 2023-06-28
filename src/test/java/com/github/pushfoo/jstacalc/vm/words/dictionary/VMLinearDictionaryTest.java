package com.github.pushfoo.jstacalc.vm.words.dictionary;

import org.jooq.lambda.Seq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.List;
import java.util.stream.Stream;

import static org.jooq.lambda.Seq.crossJoin;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VMLinearDictionaryTest {

    abstract static class BaseDictionaryTest {
        VMLinearDictionary dictionary;

        @BeforeEach
        void setup() {
            dictionary = new VMLinearDictionary();
        }
    }

    @Nested
    public class NoArgsCreation extends BaseDictionaryTest {

        @Test
        void givenFreshlyCreated_whenSizeIsCalled_returns0() {
            assertEquals(0, dictionary.size());
        }

        @Test
        void givenFreshlyCreated_whenGetNumBuiltInsCalled_returns0() {
            assertEquals(0, dictionary.getNumBuiltIns());
        }

        @Test
        void givenFreshlyCreated_whenIndexOfLastBuiltInCalled_returnsNegative1() {
            assertEquals(-1, dictionary.getIndexOfLastBuiltIn());
        }

        @Test
        void givenFreshlyCreated_whenForgetCalled_throwsIndexOutOfBoundsException() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                dictionary.forget("a");
            });
        }
    }

    @Nested
    public class AfterEntriesAdded extends BaseDictionaryTest{

        public static List<DummyWordDef> nBuiltIns(Integer dictSize, Integer nBuiltIns) {
                // Keep functional purity at the cost of some efficiency
                return Seq.range(0, dictSize)
                        .map(IndexToDummyWordMapper.builder().builtIn(i -> i < nBuiltIns).build())
                        .toList();
        }

        static Stream<? extends Arguments> rangeOfBuiltInFills() {
            return crossJoin(
                    Seq.of(1, 3, 100), // how many elements in total
                    Seq.of(1, 3, 100))  // how many are built-ins
                    .filter(t -> t.v2 <= t.v1)
                    .map(tuple2 ->
                            Arguments.of(tuple2.map(AfterEntriesAdded::nBuiltIns), tuple2.v2)
                    );
        }

        @ParameterizedTest
        @MethodSource("rangeOfBuiltInFills")
        void givenDictionaryFilledWithWords_whenSizeCalled_returnsCorrectNumberOfDefs(List<DummyWordDef> words) {
            dictionary.define(words);
            assertEquals(words.size(), dictionary.size());
        }

        @ParameterizedTest
        @MethodSource("rangeOfBuiltInFills")
        void givenDictionaryFilledWithWords_whenGetNumBuiltCalled_returnsCorrectNumberOfBuiltIns(
                List<DummyWordDef> words, Integer numBuiltIns) {
            dictionary.define(words);
            assertEquals(numBuiltIns, dictionary.getNumBuiltIns());
        }

        @ParameterizedTest
        @MethodSource("rangeOfBuiltInFills")
        void givenDictionaryFilledWithWords_whenGetIndexOfLastBuiltInCalled_returnsCorrectLastIndex(
                List<DummyWordDef> words, Integer numBuiltIns
        ) {
            dictionary.define(words);
            assertEquals(numBuiltIns - 1, dictionary.getIndexOfLastBuiltIn());
        }

        @ParameterizedTest
        @MethodSource("rangeOfBuiltInFills")
        void givenDictionaryFilledWithWords_whenForgetBuiltIns_throwsIndexOutOfBoundsException(
                List<DummyWordDef> words, Integer numBuiltIns) {
            dictionary.define(words);
            String lastBuiltInName =  words.get(numBuiltIns - 1).name;
            assertThrows(
                    IndexOutOfBoundsException.class, () -> dictionary.forget(lastBuiltInName));
        }

        @ParameterizedTest
        @MethodSource("rangeOfBuiltInFills")
        void givenDictionarFilledwithWords_whenGetIsCalledOnIndex_resultMatchesListPosition(
                List<DummyWordDef> words, Integer numBuiltIns) {
            dictionary.define(words);
            assertEquals(words.get(numBuiltIns - 1), dictionary.get(numBuiltIns - 1));
        }
    }

}