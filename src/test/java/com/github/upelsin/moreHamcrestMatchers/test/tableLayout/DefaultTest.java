package com.github.upelsin.moreHamcrestMatchers.test.tableLayout;

import android.view.View;

import com.github.upelsin.moreHamcrestMatchers.test.mocks.MockTableActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

/**
 * Created by upelsin on 16.04.2015.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DefaultTest {

    @Test
    public void should() {
        MockTableActivity tableActivity = Robolectric.buildActivity(MockTableActivity.class)
                .create()
                .start()
                .resume()
                .visible()
                .get();

        View table = tableActivity.findViewById(MockTableActivity.ID_TABLE);

        assertTrue(true);
    }
}
