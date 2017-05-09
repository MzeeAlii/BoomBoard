package creationsofali.boomboard.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ali on 5/4/17.
 */

public class EmailHelper {

    @SuppressWarnings("SpellCheckingInspection")
    public static void launchComposeEmail(String recipient, Context context) {
        String[] recipients = new String[]{recipient};
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps

        if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(emailIntent,
                    "Send email to Ali using:"));
        }
    }
}
