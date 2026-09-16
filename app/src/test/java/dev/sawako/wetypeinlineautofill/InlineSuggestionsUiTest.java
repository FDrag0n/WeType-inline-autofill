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

    @Test
    public void occludesOnlyNativeChildrenUnderTheOverlay() {
        // overlay: [0, 40, 100, 80]  (候选栏底部 40px 高)
        // 完全覆盖、部分覆盖、被覆盖 —— 都算相交
        assertTrue(InlineSuggestionsUi.intersects(0, 40, 100, 80, 0, 40, 100, 80));
        assertTrue(InlineSuggestionsUi.intersects(0, 0, 100, 100, 0, 40, 100, 80));
        assertTrue(InlineSuggestionsUi.intersects(50, 60, 150, 100, 0, 40, 100, 80));
        // 完全在 overlay 上方 / 下方 / 左右之外 —— 不相交，不该被隐藏
        assertFalse(InlineSuggestionsUi.intersects(0, 0, 100, 39, 0, 40, 100, 80));
        assertFalse(InlineSuggestionsUi.intersects(0, 81, 100, 120, 0, 40, 100, 80));
        assertFalse(InlineSuggestionsUi.intersects(101, 0, 200, 100, 0, 40, 100, 80));
        // 仅边界相接不算相交
        assertFalse(InlineSuggestionsUi.intersects(0, 0, 100, 40, 0, 40, 100, 80));
        // 未布局（退化）矩形不触发遮挡
        assertFalse(InlineSuggestionsUi.intersects(0, 0, 0, 0, 0, 40, 100, 80));
    }
}
