package com.github.upelsin.moreHamcrestMatchers.tableLayout;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.assertj.core.util.Preconditions.checkNotNull;

/**
 * Created by upelsin on 26.12.2014.
 */
public class TableMatchers {

    /**
     * Matches a view which is in the next row below row containing {@code #viewInRowAbove}.
     *
     * @param viewInRowAbove
     * @return
     */
    static Matcher<View> oneRowBelow(final Matcher<View> viewInRowAbove) {
        checkNotNull(viewInRowAbove);
        return new TypeSafeMatcher<View>(){

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
            }};
    }

    /**
     * Matches a view which is strictly {@code #numRowsBelow} the specified {@code #viewInRowAbove}.
     * It doesn't have to be exactly in the same column as {@code #viewInRowAbove},
     * any column is accepted.
     *
     * @param viewInRowAbove
     * @param numRowsBelow
     * @return
     */
    static Matcher<View> severalRowsBelow(final Matcher<View> viewInRowAbove, final int numRowsBelow) {
        checkNotNull(viewInRowAbove);
        if (numRowsBelow < 1) throw new IllegalArgumentException("Should be below");

        return new TypeSafeMatcher<View>(){

            @Override
            public void describeTo(Description description) {
                description.appendText("is ");
                description.appendValue(numRowsBelow);
                description.appendText(" below a: ");
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
                if (currentRowIndex < numRowsBelow) {
                    return false;
                }
                TableRow rowAbove = (TableRow) table.getChildAt(currentRowIndex - numRowsBelow);
                // Does the row above contains at least one view that matches viewInRowAbove?
                for (int i = 0 ; i < rowAbove.getChildCount() ; i++) {
                    if (viewInRowAbove.matches(rowAbove.getChildAt(i))) {
                        return true;
                    }
                }
                return false;
            }};
    }

    /**
     * Matches if view is a child of view group and has child position which is {@code #i}
     *
     * @param i zero-based child position of view in a view group
     * @return
     */
    static Matcher<View> hasChildPosition(final int i) {
        return new TypeSafeMatcher<View>(){

            @Override
            public void describeTo(Description description) {
                description.appendText("is child #" + i);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent viewParent = view.getParent();
                if (!(viewParent instanceof ViewGroup)) {
                    return false;
                }
                ViewGroup viewGroup = (ViewGroup) viewParent;
                return (viewGroup.indexOfChild(view) == i);
            }};
    }

    /**
     * Matches a view which is in the same row as {@code #viewInThisRow}.
     *
     * @param viewInThisRow
     * @return
     */
    static Matcher<View> inSameRowAs(final Matcher<View> viewInThisRow) {
        checkNotNull(viewInThisRow);
        return new TypeSafeMatcher<View>(){

            @Override
            public void describeTo(Description description) {
                description.appendText("in same row as: ");
                viewInThisRow.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                // Find the current row
                ViewParent viewParent = view.getParent();
                if (!(viewParent instanceof TableRow)) {
                    return false;
                }
                TableRow currentRow = (TableRow) viewParent;

                // / Does the row above contains at least one view that matches viewInRowAbove?
                for(int i = 0 ; i < currentRow.getChildCount() ; i++) {
                    if (viewInThisRow.matches(currentRow.getChildAt(i))) {
                        return true;
                    }
                }
                return false;
            }};
    }

    /**
     * WARNING: works only for tables in which column spans are not used.
     *
     * Matches a view which is at crossing point of row specified by {@code viewInThisRow}
     * and column specified by {@code viewInThisColumn}.
     *
     * @param viewInThisRow
     * @param viewInThisColumn
     * @return
     */
    static Matcher<View> atRowColumnIntersection(final Matcher<View> viewInThisRow,
                                                 final Matcher<View> viewInThisColumn) {

        checkNotNull(viewInThisRow);
        return new TypeSafeMatcher<View>(){

            @Override
            public void describeTo(Description description) {
                description.appendText("at intersection of row, column: ");
                viewInThisRow.describeTo(description);
                description.appendText(" , ");
                viewInThisColumn.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                // Find the current row
                ViewParent viewParent = view.getParent();
                if (!(viewParent instanceof TableRow)) {
                    return false;
                }
                TableRow currentRow = (TableRow) viewParent;
                TableLayout table = (TableLayout) currentRow.getParent();

                //TODO doesn't work properly for tables with cells spanning several columns
                // getColumnIndex should sum up all span counts for the columns before current;
                // it should also return not a single index but a set of indices which
                // current column spans on.

                // Does the row above contains at least one view that matches viewInRowAbove?
                for (int i = 0 ; i < currentRow.getChildCount() ; i++) {
                    if (viewInThisRow.matches(currentRow.getChildAt(i))) {
                        Integer columnIndex = getColumnIndex(table, viewInThisColumn);
                        if (columnIndex == null) {
                            return false;
                        }

                        View viewAtIndex = currentRow.getChildAt(columnIndex);
                        if (view.equals(viewAtIndex)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                return false;
            }

            //TODO fix, see comment above
            private Integer getColumnIndex(TableLayout table, final Matcher<View> viewInThisColumn) {
                for (int i = 0 ; i < table.getChildCount() ; i++) {
                    TableRow row = (TableRow) table.getChildAt(i);

                    for (int j = 0 ; j < row.getChildCount() ; j++) {
                        if (viewInThisColumn.matches(row.getChildAt(j))) {
                            return j;
                        }
                    }
                }
                return null;
            }
        };
    }
}
