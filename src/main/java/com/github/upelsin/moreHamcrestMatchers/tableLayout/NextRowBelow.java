package com.github.upelsin.moreHamcrestMatchers.tableLayout;

import android.view.View;
import android.view.ViewParent;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.assertj.core.util.Preconditions.checkNotNull;

/**
 * Created by Alexey Dmitriev <mr.alex.dmitriev@gmail.com> on 01.05.2015.
 */
public class NextRowBelow extends TypeSafeMatcher<View> {

    /**
     * Matches a view which is in the next row below row containing {@code #viewInRowAbove}.
     * A view does not have to be strictly below, e.g. in the same column.
     *
     * @param viewInRowAbove
     * @return
     */
    public static Matcher<View> nextRowBelow(final Matcher<View> viewInRowAbove) {
        checkNotNull(viewInRowAbove);
        return new NextRowBelow(viewInRowAbove);
    }

    private final Matcher<View> viewInRowAbove;

    private NextRowBelow(Matcher<View> viewInRowAbove) {
        this.viewInRowAbove = viewInRowAbove;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is below a: ");
        viewInRowAbove.describeTo(description);
    }

    @Override
    public boolean matchesSafely(View view) {
        // Find the current row
        ViewParent viewParent = view.getParent();
        if (!(viewParent instanceof TableRow)) {
            return false;
        }
        TableRow currentRow = (TableRow) viewParent;
        // Find the row above
        TableLayout table = (TableLayout) currentRow.getParent();
        int currentRowIndex = table.indexOfChild(currentRow);
        if (currentRowIndex < 1) {
            return false;
        }
        TableRow rowAbove = (TableRow) table.getChildAt(currentRowIndex - 1);
        // Does the row above contains at least one view that matches viewInRowAbove?
        for(int i = 0 ; i < rowAbove.getChildCount() ; i++) {
            if (viewInRowAbove.matches(rowAbove.getChildAt(i))) {
                return true;
            }
        }
        return false;
    };
}
