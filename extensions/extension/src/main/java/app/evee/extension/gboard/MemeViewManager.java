package app.evee.extension.gboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;

public class MemeViewManager {

    private static MemeKeyboardView currentMemeView;

    public static void onKeyboardViewCreated(Object keyboardInstance, View softKeyboardView, Object keyboardViewDef) {
        if (!(softKeyboardView instanceof ViewGroup)) return;

        try {
            ViewGroup bodyContainer = (ViewGroup) softKeyboardView;
            Context context = softKeyboardView.getContext();

            currentMemeView = new MemeKeyboardView(context);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            currentMemeView.setLayoutParams(params);

            // Add the meme keyboard view over the body container
            bodyContainer.addView(currentMemeView);
        } catch (Throwable ignored) {
        }
    }

    public static void onActivate(Object keyboardInstance, EditorInfo editorInfo, Object obj) {
        if (currentMemeView == null) return;

        try {
            Context context = currentMemeView.getContext();
            InputConnection ic = null;
            if (context instanceof InputMethodService) {
                ic = ((InputMethodService) context).getCurrentInputConnection();
            }
            currentMemeView.setInputContext(ic, editorInfo);
            currentMemeView.loadMemes();
        } catch (Throwable ignored) {
        }
    }
}
