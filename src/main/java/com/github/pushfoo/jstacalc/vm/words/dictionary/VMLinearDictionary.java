package com.github.pushfoo.jstacalc.vm.words.dictionary;

import com.github.pushfoo.jstacalc.vm.common.VMStack;
import com.github.pushfoo.jstacalc.vm.words.defs.IWordDef;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Predicate;

import static java.util.Objects.isNull;

// TODO: add support for a unique word defs view?
/**
 * <p>A traditional, inefficient Forthy O(N) linear dictionary.</p>
 *
 * <p>This is worth doing because it's less complicated than the
 * alternatives, </p>
 */
public class VMLinearDictionary implements IVMDictionary {
    protected VMStack<IWordDef> definitions;
    @Getter
    private int numBuiltIns;
    @Getter
    private int indexOfLastBuiltIn;
    public VMLinearDictionary() {
        definitions = new VMStack<>();
        numBuiltIns = 0;
        indexOfLastBuiltIn = -1;
    }

    public void define(@NonNull IWordDef word) {
        if ( isNull(word) ) {
            throw new NullPointerException("Can't add null word!");
        }
        if (word.isBuiltIn() ) {
            numBuiltIns += 1;
            indexOfLastBuiltIn += 1;
        }
        definitions.push(word);
    }

    /**
     * Directly define words from an iterable.
     *
     * @param words An iterable of words to push.
     */
    public void define(Iterable<? extends IWordDef> words) {
        for (IWordDef word : words ) define(word);
    }

    private Predicate<IWordDef> wordMatchesName(String wordName) {
        return (IWordDef def) -> def.getName().equals(wordName);
    }

    public IWordDef get(String wordName) {
        return definitions.lastItemMatching(wordMatchesName(wordName));
    }

    public IWordDef get(int index) {
        return definitions.get(index);
    }

    @Override
    public void forget(@NonNull String word) {

        int lastIndex = definitions.lastIndexMatching(wordMatchesName(word));
        if ( lastIndex < 0 ) {
            throw new IndexOutOfBoundsException("No word matching this word is in the dictionary");
        }
        if (lastIndex <= indexOfLastBuiltIn) {
            throw new IndexOutOfBoundsException("Forgetting built-ins is not allowed.");
        }
        definitions.clear(definitions.size() - lastIndex);
    }

    public int size() {
        return definitions.size();
    }

}
