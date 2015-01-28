package com.compassrosetech.ccs.android.test.espresso;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.google.android.apps.common.testing.testrunner.util.Checks.checkNotNull;

/**
 * Custom implementation of a HasPropertyWithValue matcher (copied from
 * https://github.com/hamcrest/JavaHamcrest/blob/master/hamcrest-library/src/main/java/org/hamcrest/beans/HasPropertyWithValue.java)
 * using pure reflection instead java beans PropertyUtils.
 *
 * @author Brian Saville
 * @author Iain McGinniss
 * @author Nat Pryce
 * @author Steve Freeman
 */
public class AndroidHasPropertyWithValue<T> extends TypeSafeMatcher<T> {

    public static <T> Matcher<T> androidHasPropertyWithValue(String propertyName, Matcher<?> valueMatcher) {
        checkNotNull(valueMatcher);
        return new AndroidHasPropertyWithValue<T>(propertyName, valueMatcher);
    }

    private final String propertyName;
    private final Matcher<Object> valueMatcher;

    public AndroidHasPropertyWithValue(String propertyName, Matcher<?> valueMatcher) {
        this.propertyName = propertyName;
        this.valueMatcher = nastyGenericsWorkaround(valueMatcher);
    }

    @Override
    public boolean matchesSafely(T bean) {
        try {
            Method readMethod = bean.getClass().getMethod("get" + capitalize(propertyName));
            Object value = readMethod.invoke(bean);
            return valueMatcher.matches(value);
        } catch(NoSuchMethodException e) {
            System.out.println("No such method available");
        } catch(IllegalAccessException e) {
            System.out.println("Could not access method: " + e.getMessage());
        } catch(InvocationTargetException e) {
            System.out.println("Could not access method: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("hasPropertyAndroid(").appendValue(propertyName).appendText(", ")
                .appendDescriptionOf(valueMatcher).appendText(")");
    }

    @SuppressWarnings("unchecked")
    private static Matcher<Object> nastyGenericsWorkaround(Matcher<?> valueMatcher) {
        return (Matcher<Object>) valueMatcher;
    }

    private String capitalize(String str) {
        if (str==null)
            return "";
        if (str.length() < 2)
            return str.toUpperCase();
        return str.substring(0, 1).toUpperCase()+str.substring(1);
    }
}
