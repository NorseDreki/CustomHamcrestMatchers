package com.github.upelsin.moreHamcrestMatchers.test.mocks;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;

import static android.view.ViewGroup.LayoutParams;

/**
 * Activity with a sample 3-by-3 table for tests.
 *
 * Created by Alexey Dmitriev <mr.alex.dmitriev@gmail.com> on 01.05.2015.
 */
public class MockTableActivity extends Activity {

    public static final int NUM_COLUMNS = 3;

    public static final int NUM_ROWS = 3;

    public static final int ID_TABLE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TableLayout table = new TableLayout(this);
        table.setId(ID_TABLE);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        for (int i = 0; i < NUM_ROWS; i++) {
            addRow(table, i);
        }

        setContentView(table, params);
    }

    /**
     * Creates and adds text view to the row.
     *
     * @param row
     * @param text
     * @param colspan set to null if no column span needed
     */
    protected void addTextView(TableRow row, String text, Integer colspan) {
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        if (colspan != null)
            rowParams.span = colspan;

        // no way to set style (no function)

        TextView displayView = new TextView(row.getContext());
        displayView.setLayoutParams(rowParams);
        displayView.setText(text);

        row.addView(displayView);
    }

    public void addRow(TableLayout table, int rowIndex) {
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

        TableRow tableRow = new TableRow(table.getContext());
        tableRow.setLayoutParams(tableParams);

        for (int i = 0; i < NUM_COLUMNS; i++) {
            String text = String.format(Locale.US, "%d,%d", rowIndex, i);
            addTextView(tableRow, text, null);
        }

        table.addView(tableRow);
    }
}
