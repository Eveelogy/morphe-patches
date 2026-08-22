package app.evee.extension.gboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import java.io.File;
import java.io.FileOutputStream;

public class MemeMaker {

    public static Bitmap renderMeme(Bitmap template, String topText, String bottomText) {
        if (template == null) return null;
        int width = template.getWidth();
        int height = template.getHeight();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(template, 0, 0, null);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint strokePaint = new Paint(textPaint);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.BLACK);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);

        float fontSize = width / 10f;
        textPaint.setTextSize(fontSize);
        strokePaint.setTextSize(fontSize);
        strokePaint.setStrokeWidth(fontSize / 8f);

        if (topText != null && !topText.trim().isEmpty()) {
            String top = topText.toUpperCase().trim();
            float x = width / 2f;
            float y = fontSize + 15f;
            canvas.drawText(top, x, y, strokePaint);
            canvas.drawText(top, x, y, textPaint);
        }

        if (bottomText != null && !bottomText.trim().isEmpty()) {
            String bottom = bottomText.toUpperCase().trim();
            float x = width / 2f;
            float y = height - 25f;
            canvas.drawText(bottom, x, y, strokePaint);
            canvas.drawText(bottom, x, y, textPaint);
        }

        return result;
    }

    public static File saveMemeToFile(Context context, Bitmap meme) {
        try {
            File dir = new File(context.getFilesDir(), "share_content");
            if (!dir.exists()) dir.mkdirs();
            File file = new File(dir, "meme_" + System.currentTimeMillis() + ".jpg");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                meme.compress(Bitmap.CompressFormat.JPEG, 95, fos);
            }
            return file;
        } catch (Throwable t) {
            return null;
        }
    }
}
