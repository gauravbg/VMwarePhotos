package com.chikeandroid.tutsplus_glide;

public class Constants {


    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQ_SIGN_IN_REQUIRED = 1003;
    /** 'Scopes' required to request Google permissions for our specific case */
    public final static String GPHOTOS_SCOPE
            = "oauth2:profile email https://www.googleapis.com/auth/drive.photos.readonly";

    /** Google API KEY */
    public static String URL_EXTN_API_KEY = "&key=631491753458-i8n8q3m80kc5najhk004jio3sr4eumbk.apps.googleusercontent.com";

    /** URLs and extensions required for the API calls */
    public static String URL_FILES = "https://photoslibrary.googleapis.com/v1/albums";


}
