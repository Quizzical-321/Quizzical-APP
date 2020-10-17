package com.cpen321.quizzical.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;
import android.util.Log;

import com.cpen321.quizzical.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class OtherUtils {

    public static boolean stringIsNullOrEmpty(String str) {
        return (str == null || str.equals(""));
    }

    public static boolean checkUserName(String username) {
        //check if the username is valid or not
        //TODO: need to check if this username is registered on server or not
        if (!Pattern.matches("^[aA-zZ0-9_-]{3,15}$", username)) {
            return false;
        }

        return true;
    }

    public static boolean checkEmail(String email) {
        //check if the email is valid or not
        //TODO: and upload this email to the server
        //TODO: the server should push an authentication email to the email address

        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

        if (!Pattern.matches(regex, email)) {
            //the email entered is not a valid format
            return false;
        }

        return true;
    }

    public static boolean checkClassCode(int class_code) {
        //TODO: we need to check that the code is valid on the server
        //TODO: use POST request with content: username=xxx;class_code=xxx
        return class_code != 111;
    }

    /*
     * Encoding and decoding images for storing in shared preferences and send to server
     * */
    public static String encodeImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeImage(String encodedImg) {
        byte[] decoded = Base64.decode(encodedImg, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
    }

    public static Bitmap scaleImage(Bitmap image) {
        //used for scaling image and making the image into a circle shape to fit in the profile image
        int radius = 200;
        Bitmap scaled = Bitmap.createScaledBitmap(image, 2 * radius, 2 * radius, true);
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(2 * radius, 2 * radius, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 2 * radius, 2 * radius);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(radius, radius, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(scaled, rect, rect, paint);
        } catch (Exception e) {
            Log.d("Image", "Image scaling failed");
        }
        return result;
    }

    /*
     * upload strings, bitmap to the server
     * returns true if success, false otherwise
     * */
    public static boolean uploadStringToServer(String string) {
        String serverLink = "http://193.122.108.23:9090/";
        try {
            URL url = new URL(serverLink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(1000);
            conn.setDoOutput(true);
            conn.connect();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(string);
            wr.flush();
            wr.close();


            int responseCode = conn.getResponseCode();
            Log.d("HTTP POST", "response code: " + responseCode);
        } catch (Exception e) {
            Log.d("error message", e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean uploadBitmapToServer(Bitmap image) {
        String serverLink = "http://193.122.108.23:7070";
        try {
            URL url = new URL(serverLink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setConnectTimeout(1000);
            conn.setDoOutput(true);
            conn.connect();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(encodeImage(image));
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            Log.d("HTTP POST", "response code: " + responseCode);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /*
     * function used for downloading images from url link
     * mostly used in creating quiz
     * may also be used for downloading user profile image
     * */
    public static Bitmap getBitmapFromUrl(String urlLink) {

        try {
            URL url = new URL(urlLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * get string content from the server,
     * on success, return the content retrieved from the server
     * on fail, return an empty string
     */
    public static String readFromURL(String urlLink) {
        String result = "";
        try {
            URL url = new URL(urlLink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1000);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder stb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stb.append(line);
            }
            result = stb.toString();
        } catch (Exception e) {
            String eMessage = e.getMessage() + "";
            Log.d("html exception", eMessage);
        }
        return result;
    }
}
