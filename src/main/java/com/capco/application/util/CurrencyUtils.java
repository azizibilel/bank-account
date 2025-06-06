package com.capco.application.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class CurrencyUtils {

    private CurrencyUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String currencyFormat(BigDecimal amount) {
        var dfs = DecimalFormatSymbols.getInstance(Locale.FRENCH);
        dfs.setDecimalSeparator('.');
        dfs.setGroupingSeparator(',');
        return new DecimalFormat("#,###.00", dfs).format(amount);
    }
}
