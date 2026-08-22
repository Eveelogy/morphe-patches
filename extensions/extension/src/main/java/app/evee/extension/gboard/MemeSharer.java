package app.evee.extension.gboard;

import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import java.io.File;
import java.lang.reflect.Method;

public class MemeSharer {

    public static void shareMeme(Context context, File imageFile, InputConnection inputConnection, EditorInfo editorInfo) {
        if (context == null || imageFile == null || !imageFile.exists()) return;

        Uri contentUri = getFileUri(context, imageFile);
        if (contentUri == null) return;

        boolean committed = false;
        if (inputConnection != null && editorInfo != null) {
            try {
                ClipDescription description = new ClipDescription(imageFile.getName(), new String[]{"image/jpeg", "image/png"});
                InputContentInfoCompat infoCompat = new InputContentInfoCompat(contentUri, description, null);

                int flags = 0;
                if (Build.VERSION.SDK_INT >= 25) {
                    flags |= InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
                }

                committed = InputConnectionCompat.commitContent(inputConnection, editorInfo, infoCompat, flags, null);
            } catch (Throwable ignored) {
            }
        }

        if (!committed) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(shareIntent, "Share Meme").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } catch (Throwable t) {
                Toast.makeText(context, "Could not insert meme", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static Uri getFileUri(Context context, File file) {
        try {
            String authority = context.getPackageName() + ".fileprovider";
            return FileProvider.getUriForFile(context, authority, file);
        } catch (Throwable t) {
            try {
                // Try Gboard's internal ShareContentUtils
                Class<?> scu = Class.forName("com.google.android.apps.inputmethod.libs.expression.image.ShareContentUtils");
                Method a = scu.getMethod("a", Context.class, File.class);
                return (Uri) a.invoke(null, context, file);
            } catch (Throwable ignored) {
            }
            return Uri.fromFile(file);
        }
    }
}
