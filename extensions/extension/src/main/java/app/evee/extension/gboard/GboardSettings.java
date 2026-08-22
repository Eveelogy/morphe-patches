package app.evee.extension.gboard;

import android.app.Application;
import android.content.Context;
import android.view.inputmethod.EditorInfo;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class GboardSettings {

    public static Context getAppContext() {
        try {
            Application app = (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication").invoke(null);
            if (app != null) {
                return app.getApplicationContext();
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    public static Object getEmoticonNavbarItem(Object izqInstance, Context context, Object pquObj, EditorInfo editorInfo, boolean z) {
        try {
            Class<?> jjcClass = Class.forName("jjc");
            Constructor<?> ctor = jjcClass.getDeclaredConstructor(int.class);
            ctor.setAccessible(true);
            // 0 = Sticker / Static Image search
            Object jjcInstance = ctor.newInstance(0);

            for (Method m : jjcClass.getDeclaredMethods()) {
                if (m.getName().equals("a") && m.getParameterTypes().length == 4) {
                    m.setAccessible(true);
                    return m.invoke(jjcInstance, context, pquObj, editorInfo, z);
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }
}
