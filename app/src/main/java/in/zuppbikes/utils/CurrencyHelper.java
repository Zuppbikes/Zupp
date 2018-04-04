package in.zuppbikes.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by riteshdubey on 3/7/17.
 */

public class CurrencyHelper {

    private static final Locale DEFAULT_LOCALE = new Locale("en_IN", "IN");


    public static String getFormattedPriceForLocale(float iPrice, Locale iLocale) {
        Format format = NumberFormat.getCurrencyInstance(iLocale);
        return format.format(new BigDecimal(iPrice));
    }

    public static String getFormattedPrice(double iPrice) {
//        AppUser aUser = (AppUser) GsonHelper.getGson(AppUserSession.getInstance().getString(AppConstants.KEY_USER), AppUser.class);
//        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
//        format.setCurrency(Currency.getInstance(aUser.currencyCode));
        DecimalFormat formatter = new DecimalFormat("##.###"/*"#,###,##0.00"*/);
        return formatter.format(iPrice);
    }

}
