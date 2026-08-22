package app.evee.extension.gboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;

public class ImgflipService {

    private static final String API_URL = "https://api.imgflip.com/get_memes";
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(4);
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private static final List<ImgflipMeme> CACHED_MEMES = new ArrayList<>();
    private static boolean isFetching = false;

    static {
        // Built-in offline fallback templates
        CACHED_MEMES.add(new ImgflipMeme("181913649", "Drake Hotline Bling", "https://i.imgflip.com/30b1gx.jpg", 1200, 1200));
        CACHED_MEMES.add(new ImgflipMeme("112126428", "Distracted Boyfriend", "https://i.imgflip.com/1ur9b0.jpg", 1200, 800));
        CACHED_MEMES.add(new ImgflipMeme("87743020", "Two Buttons", "https://i.imgflip.com/1g8my4.jpg", 600, 908));
        CACHED_MEMES.add(new ImgflipMeme("247719600", "Buff Doge vs. Cheems", "https://i.imgflip.com/438680.jpg", 937, 720));
        CACHED_MEMES.add(new ImgflipMeme("93895088", "Expanding Brain", "https://i.imgflip.com/1jwhww.jpg", 857, 1202));
        CACHED_MEMES.add(new ImgflipMeme("188390779", "Woman Yelling At Cat", "https://i.imgflip.com/345v97.jpg", 680, 438));
        CACHED_MEMES.add(new ImgflipMeme("129242436", "Change My Mind", "https://i.imgflip.com/24y43o.jpg", 482, 361));
        CACHED_MEMES.add(new ImgflipMeme("252601556", "Always Has Been", "https://i.imgflip.com/46e43q.jpg", 960, 540));
        CACHED_MEMES.add(new ImgflipMeme("217743513", "UNO Draw 25 Cards", "https://i.imgflip.com/3lmzyx.jpg", 500, 494));
        CACHED_MEMES.add(new ImgflipMeme("226297822", "Panik Kalm Panik", "https://i.imgflip.com/3qqcim.png", 640, 884));
        CACHED_MEMES.add(new ImgflipMeme("131087930", "Gru's Plan", "https://i.imgflip.com/26amhg.jpg", 700, 449));
        CACHED_MEMES.add(new ImgflipMeme("438680", "Batman Slapping Robin", "https://i.imgflip.com/9ehk.jpg", 400, 387));
        CACHED_MEMES.add(new ImgflipMeme("97984", "Disaster Girl", "https://i.imgflip.com/23ls.jpg", 500, 375));
        CACHED_MEMES.add(new ImgflipMeme("61579", "Ceo Meeting Suggestion", "https://i.imgflip.com/m78d.jpg", 500, 649));
        CACHED_MEMES.add(new ImgflipMeme("102156234", "Mocking Spongebob", "https://i.imgflip.com/1ot296.jpg", 502, 353));
        CACHED_MEMES.add(new ImgflipMeme("222403160", "Bernie I Am Once Again Asking", "https://i.imgflip.com/3oevdk.jpg", 750, 750));
        CACHED_MEMES.add(new ImgflipMeme("178591710", "They're The Same Picture", "https://i.imgflip.com/2za3u1.jpg", 1363, 1524));
        CACHED_MEMES.add(new ImgflipMeme("124822590", "Left Exit 12 Off Ramp", "https://i.imgflip.com/22bdq6.jpg", 804, 767));
        CACHED_MEMES.add(new ImgflipMeme("195512495", "Waiting Skeleton", "https://i.imgflip.com/32har3.jpg", 298, 403));
        CACHED_MEMES.add(new ImgflipMeme("100777631", "Is This A Pigeon", "https://i.imgflip.com/1o00in.jpg", 1587, 1425));
    }

    public interface MemeCallback {
        void onLoaded(List<ImgflipMeme> memes);
    }

    public interface ImageCallback {
        void onLoaded(Bitmap bitmap, File file);
    }

    public static List<ImgflipMeme> getCachedMemes() {
        return Collections.unmodifiableList(CACHED_MEMES);
    }

    public static void fetchMemes(Context context, MemeCallback callback) {
        if (!CACHED_MEMES.isEmpty() && callback != null) {
            callback.onLoaded(new ArrayList<>(CACHED_MEMES));
        }

        if (isFetching) return;
        isFetching = true;

        EXECUTOR.execute(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject root = new JSONObject(sb.toString());
                    if (root.optBoolean("success")) {
                        JSONArray array = root.getJSONObject("data").getJSONArray("memes");
                        List<ImgflipMeme> fetched = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            fetched.add(new ImgflipMeme(
                                    obj.getString("id"),
                                    obj.getString("name"),
                                    obj.getString("url"),
                                    obj.optInt("width", 500),
                                    obj.optInt("height", 500)
                            ));
                        }

                        synchronized (CACHED_MEMES) {
                            CACHED_MEMES.clear();
                            CACHED_MEMES.addAll(fetched);
                        }

                        if (callback != null) {
                            MAIN_HANDLER.post(() -> callback.onLoaded(new ArrayList<>(CACHED_MEMES)));
                        }
                    }
                }
            } catch (Throwable ignored) {
            } finally {
                isFetching = false;
            }
        });
    }

    public static void downloadMemeImage(Context context, ImgflipMeme meme, ImageCallback callback) {
        EXECUTOR.execute(() -> {
            try {
                File dir = new File(context.getCacheDir(), "meme_templates");
                if (!dir.exists()) dir.mkdirs();
                File cachedFile = new File(dir, meme.id + ".jpg");

                if (cachedFile.exists() && cachedFile.length() > 0) {
                    Bitmap bmp = BitmapFactory.decodeFile(cachedFile.getAbsolutePath());
                    if (bmp != null) {
                        MAIN_HANDLER.post(() -> callback.onLoaded(bmp, cachedFile));
                        return;
                    }
                }

                URL url = new URL(meme.url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    try (InputStream in = conn.getInputStream();
                         FileOutputStream fos = new FileOutputStream(cachedFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }

                    Bitmap bmp = BitmapFactory.decodeFile(cachedFile.getAbsolutePath());
                    if (bmp != null) {
                        MAIN_HANDLER.post(() -> callback.onLoaded(bmp, cachedFile));
                    }
                }
            } catch (Throwable ignored) {
            }
        });
    }
}
