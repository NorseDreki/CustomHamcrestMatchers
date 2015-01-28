# CustomHamcrestMatchers
Collection of custom Hamcrest matchers

## Matchers for Android's TableLayout include:
- views N rows below the row with the specified view
- views which are members of a view group with the specified child index
- views in the same row as the specified view
- views at crossing point of the specified row and column

## Android version of hasPropertyWithValue():
Due to Android-specific bug there happens a VerifyError when trying to use Hamcrest's built-in hasPropertyWithValue() (this is true as least for version 1.1). 
Look at https://groups.google.com/forum/#!topic/android-test-kit-discuss/5w1bhljm3ag for more details.