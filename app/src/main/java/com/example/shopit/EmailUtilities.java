package com.example.shopit;

import android.content.Context;
import android.content.Intent;

public interface EmailUtilities {



    default void sendRegisterMail(Context context, String emailTo){
        emailTo = emailTo + "@wp.pl";
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, emailTo);
        email.putExtra(Intent.EXTRA_SUBJECT, "Rejestracja - ShopIT");
        email.putExtra(Intent.EXTRA_TEXT, "Witamy w naszej cudownej aplikacji ShopIT. Cieszymy się, że do nas dołączyłeś");
        email.setType("message/rfc822");
        context.startActivity(email);
    }
}
