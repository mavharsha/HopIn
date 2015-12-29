/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator {

    public static boolean validateEmail (final String incomming) {

        final String email_pattern = "^[A-Za-z0-9+]+(.[A-Za-z0-9]+)*@"
                +
                "[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(email_pattern);
        Matcher matcher = pattern.matcher(incomming);
        return matcher.matches();

    }

    public static boolean validateName (final String incomming) {

        final String name_pattern = "^[a-zA-z]+([ '-][a-zA-Z]+)*$";
        Pattern   pattern = Pattern.compile(name_pattern);
        Matcher matcher = pattern.matcher(incomming);
        return matcher.matches();

    }


    public static boolean validatePhoneNumber (final String incomming) {

        final String phonenumber_pattern = "^[0-9]{10}$";
        Pattern   pattern = Pattern.compile(phonenumber_pattern);
        Matcher matcher = pattern.matcher(incomming);
        return matcher.matches();
    }

    public static boolean validateNumber (final String incomming) {

        final String number_pattern = "^[0-9]*$";
        Pattern   pattern = Pattern.compile(number_pattern);
        Matcher matcher = pattern.matcher(incomming);
        return matcher.matches();
    }

    public static boolean validatePassword (final String incomming) {

        final String password_pattern = "^[a-zA-z0-9]*$";
        Pattern   pattern = Pattern.compile(password_pattern);
        Matcher matcher = pattern.matcher(incomming);
        return matcher.matches();
    }
}
