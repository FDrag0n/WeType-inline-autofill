package dev.sawako.wetypeinlineautofill;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineSuggestionsUiTest {
    @Test
    public void keepsFirstPinnedSeparateAndOrdersTheRest() {
        assertArrayEquals(new int[]{1, 3, 0, 2},
                InlineSuggestionsUi.orderSuggestions(
                        new boolean[]{false, true, false, true}));
        assertArrayEquals(new int[]{-1, 0, 1},
                InlineSuggestionsUi.orderSuggestions(new boolean[]{false, false}));
        assertArrayEquals(new int[]{0},
                InlineSuggestionsUi.orderSuggestions(new boolean[]{true}));
    }

    @Test
    public void onlyClearsSuggestionsForANewInputTarget() {
        assertTrue(InlineSuggestionsUi.shouldClearOnStartInputView(false));
        assertFalse(InlineSuggestionsUi.shouldClearOnStartInputView(true));
    }

    @Test
    public void waitsForAtLeastOneRenderedSuggestionBeforeShowingShell() {
        assertFalse(InlineSuggestionsUi.hasReadyContent(false, false));
        assertTrue(InlineSuggestionsUi.hasReadyContent(true, false));
        assertTrue(InlineSuggestionsUi.hasReadyContent(false, true));
    }

    @Test
    public void followsCandidateListReplacementWithoutDroppingIncrementalState() {
        assertTrue(InlineSuggestionsUi.candidateActiveAfterUpdate(false, true, true));
        assertTrue(InlineSuggestionsUi.candidateActiveAfterUpdate(true, false, false));
        assertFalse(InlineSuggestionsUi.candidateActiveAfterUpdate(true, true, false));
    }

    @Test
    public void onlyOccludesPresentAndVisibleNativeIcons() {
        assertTrue(InlineSuggestionsUi.shouldOccludeNativeIcon(true, true));
        assertFalse(InlineSuggestionsUi.shouldOccludeNativeIcon(true, false));
        assertFalse(InlineSuggestionsUi.shouldOccludeNativeIcon(false, true));
        assertFalse(InlineSuggestionsUi.shouldOccludeNativeIcon(false, false));
    }
}
