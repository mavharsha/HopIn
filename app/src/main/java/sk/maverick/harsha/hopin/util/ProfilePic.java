/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin.Util;

import sk.maverick.harsha.hopin.R;

/**
 * Created by Harsha on 3/10/2016.
 */
public class ProfilePic {

    public static int getAvatar(String value){

            switch (value){
                case "1" : return R.drawable.avatar_1;
                case "2" : return R.drawable.avatar_4;
                case "3" : return R.drawable.avatar_8;
                case "4" : return R.drawable.avatar_9;
                case "5" : return R.drawable.avatar_11;
                default  : return R.drawable.avatar_1;
            }
    }
}
