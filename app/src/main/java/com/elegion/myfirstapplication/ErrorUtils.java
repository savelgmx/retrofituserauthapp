package com.elegion.myfirstapplication;

import android.support.annotation.StringRes;

import retrofit2.Response;

/**
 * Created by andrew on 07.04.2019.
 */

public class ErrorUtils {


    @StringRes
    static
    int stringCode;

    public static int parseError(Response<?> response) {
        switch (response.code()) {
            case 200:
                stringCode = R.string.response_code_200;
               // return stringCode;
                break;
            case 204:
                stringCode = R.string.response_code_204;
                // return stringCode;
                break;
            case 400: //400 Bad Request
                stringCode = R.string.response_code_400;
                // return stringCode;
                break;
            case 500: //Server error
                stringCode = R.string.server_error;
                //   return stringCode;
               break;
            default:
                stringCode = R.string.unknown_error;
               // return stringCode;
                break;

        }
    return stringCode;
    }
}

