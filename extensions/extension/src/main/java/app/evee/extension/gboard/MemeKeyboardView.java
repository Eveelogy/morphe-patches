package app.evee.extension.gboard;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MemeKeyboardView extends FrameLayout {

    private final Context context;
    private final List<ImgflipMeme> allMemes = new ArrayList<>();
    private final List<ImgflipMeme> filteredMemes = new ArrayList<>();
    private MemeGridAdapter adapter;
    private GridView gridView;
    private EditText searchInput;
    private ProgressBar progressBar;
    private InputConnection currentInputConnection;
    private EditorInfo currentEditorInfo;

    public MemeKeyboardView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public void setInputContext(InputConnection ic, EditorInfo info) {
        this.currentInputConnection = ic;
        this.currentEditorInfo = info;
    }

    private void initView() {
        setBackgroundColor(0xFF1E1E1E); // Sleek dark keyboard background

        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // Search Header Bar
        LinearLayout searchBar = new LinearLayout(context);
        searchBar.setOrientation(LinearLayout.HORIZONTAL);
        searchBar.setPadding(dp(8), dp(6), dp(8), dp(6));
        searchBar.setGravity(Gravity.CENTER_VERTICAL);

        searchInput = new EditText(context);
        searchInput.setHint("🔍 Search memes (e.g. drake, doge)...");
        searchInput.setHintTextColor(0xFF888888);
        searchInput.setTextColor(Color.WHITE);
        searchInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        searchInput.setSingleLine(true);
        searchInput.setBackground(createSearchBackground());
        searchInput.setPadding(dp(12), dp(6), dp(12), dp(6));
        LinearLayout.LayoutParams searchParams = new LinearLayout.LayoutParams(0, dp(38), 1.0f);
        searchInput.setLayoutParams(searchParams);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMemes(s != null ? s.toString() : "");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        searchBar.addView(searchInput);
        mainLayout.addView(searchBar);

        // Content Area with Grid & Progress
        FrameLayout contentFrame = new FrameLayout(context);
        contentFrame.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f));

        gridView = new GridView(context);
        gridView.setNumColumns(3);
        gridView.setHorizontalSpacing(dp(6));
        gridView.setVerticalSpacing(dp(6));
        gridView.setPadding(dp(8), dp(4), dp(8), dp(8));
        gridView.setClipToPadding(false);
        gridView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        adapter = new MemeGridAdapter();
        gridView.setAdapter(adapter);

        // Tap: Send blank meme immediately
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            ImgflipMeme meme = filteredMemes.get(position);
            Toast.makeText(context, "Sending " + meme.name + "...", Toast.LENGTH_SHORT).show();
            ImgflipService.downloadMemeImage(context, meme, (bitmap, file) -> {
                MemeSharer.shareMeme(context, file, currentInputConnection, currentEditorInfo);
            });
        });

        // Long Press: Open Meme Creator
        gridView.setOnItemLongClickListener((parent, view, position, id) -> {
            ImgflipMeme meme = filteredMemes.get(position);
            openMemeCreator(meme);
            return true;
        });

        progressBar = new ProgressBar(context);
        LayoutParams progressParams = new LayoutParams(dp(40), dp(40));
        progressParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(progressParams);
        progressBar.setVisibility(View.GONE);

        contentFrame.addView(gridView);
        contentFrame.addView(progressBar);
        mainLayout.addView(contentFrame);

        addView(mainLayout);

        loadMemes();
    }

    public void loadMemes() {
        List<ImgflipMeme> cached = ImgflipService.getCachedMemes();
        if (!cached.isEmpty()) {
            allMemes.clear();
            allMemes.addAll(cached);
            filterMemes(searchInput.getText().toString());
        }

        if (allMemes.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
        }

        ImgflipService.fetchMemes(context, memes -> {
            progressBar.setVisibility(View.GONE);
            allMemes.clear();
            allMemes.addAll(memes);
            filterMemes(searchInput.getText().toString());
        });
    }

    private void filterMemes(String query) {
        filteredMemes.clear();
        if (TextUtils.isEmpty(query)) {
            filteredMemes.addAll(allMemes);
        } else {
            String q = query.toLowerCase().trim();
            for (ImgflipMeme meme : allMemes) {
                if (meme.name.toLowerCase().contains(q)) {
                    filteredMemes.add(meme);
                }
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void openMemeCreator(ImgflipMeme meme) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(16), dp(12), dp(16), dp(8));

        TextView title = new TextView(context);
        title.setText("Meme Creator: " + meme.name);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setPadding(0, 0, 0, dp(12));
        layout.addView(title);

        final EditText topInput = new EditText(context);
        topInput.setHint("TOP TEXT");
        topInput.setSingleLine(true);
        layout.addView(topInput);

        final EditText bottomInput = new EditText(context);
        bottomInput.setHint("BOTTOM TEXT");
        bottomInput.setSingleLine(true);
        layout.addView(bottomInput);

        builder.setView(layout);
        builder.setPositiveButton("Send Meme", (dialog, which) -> {
            String top = topInput.getText().toString();
            String bottom = bottomInput.getText().toString();
            Toast.makeText(context, "Generating meme...", Toast.LENGTH_SHORT).show();

            ImgflipService.downloadMemeImage(context, meme, (bitmap, file) -> {
                Bitmap rendered = MemeMaker.renderMeme(bitmap, top, bottom);
                File saved = MemeMaker.saveMemeToFile(context, rendered != null ? rendered : bitmap);
                MemeSharer.shareMeme(context, saved, currentInputConnection, currentEditorInfo);
            });
        });

        builder.setNeutralButton("Send Blank", (dialog, which) -> {
            ImgflipService.downloadMemeImage(context, meme, (bitmap, file) -> {
                MemeSharer.shareMeme(context, file, currentInputConnection, currentEditorInfo);
            });
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private GradientDrawable createSearchBackground() {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFF2C2C2C);
        gd.setCornerRadius(dp(19));
        return gd;
    }

    private int dp(int val) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, context.getResources().getDisplayMetrics());
    }

    private class MemeGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return filteredMemes.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredMemes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout cell;
            ImageView thumb;
            TextView label;

            if (convertView == null) {
                cell = new LinearLayout(context);
                cell.setOrientation(LinearLayout.VERTICAL);
                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                cell.setPadding(dp(4), dp(4), dp(4), dp(4));

                GradientDrawable cellBg = new GradientDrawable();
                cellBg.setColor(0xFF282828);
                cellBg.setCornerRadius(dp(8));
                cell.setBackground(cellBg);

                thumb = new ImageView(context);
                thumb.setLayoutParams(new LinearLayout.LayoutParams(dp(96), dp(96)));
                thumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
                cell.addView(thumb);

                label = new TextView(context);
                label.setTextColor(Color.WHITE);
                label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                label.setMaxLines(1);
                label.setEllipsize(TextUtils.TruncateAt.END);
                label.setGravity(Gravity.CENTER);
                label.setPadding(0, dp(4), 0, 0);
                cell.addView(label);
            } else {
                cell = (LinearLayout) convertView;
                thumb = (ImageView) cell.getChildAt(0);
                label = (TextView) cell.getChildAt(1);
            }

            ImgflipMeme meme = filteredMemes.get(position);
            label.setText(meme.name);
            thumb.setImageDrawable(null);
            thumb.setTag(meme.id);

            ImgflipService.downloadMemeImage(context, meme, (bitmap, file) -> {
                if (meme.id.equals(thumb.getTag())) {
                    thumb.setImageBitmap(bitmap);
                }
            });

            return cell;
        }
    }
}
