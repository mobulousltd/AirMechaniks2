package mobulous12.airmechanics.fonts;


import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mobulous12.airmechanics.utils.MyApplication;

/**
 * A simple font cache that makes a font once when it's first asked for and keeps it for the
 * life of the application.
 * <p/>
 * To use it, put your fonts in /assets/fonts.  You can access them in XML by their filename, minus
 * the extension (e.g. "Roboto-BoldItalic" or "roboto-bolditalic" for Roboto-BoldItalic.ttf).
 * <p/>
 * To set custom names for fonts other than their filenames, call addFont().
 */
public class FontCache {

    private static final String FONT_DIR = "fonts";
    private static String TAG = "FontCache";
    private static Map<String, Typeface> cache = new HashMap<>();
    private static Map<String, String> fontMapping = new HashMap<>();
    private static FontCache instance;

    private FontCache() {
        AssetManager am = MyApplication.getInstance().getResources().getAssets();
        String fileList[];
        try {
            fileList = am.list(FONT_DIR);
        } catch (IOException e) {
            Log.e(TAG, "Error loading fonts from assets/fonts.");
            return;
        }

        for (String filename : fileList) {
            String alias = filename.substring(0, filename.lastIndexOf('.'));
            fontMapping.put(alias, filename);
            fontMapping.put(alias.toLowerCase(), filename);
        }
    }

    public static FontCache getInstance() {
        if (instance == null) {
            instance = new FontCache();
        }
        return instance;
    }

    public void addFont(String name, String fontFilename) {
        fontMapping.put(name, fontFilename);
    }

    public Typeface get(String fontName) {
        String fontFilename = fontMapping.get(fontName);
        if (fontFilename == null) {
            Log.e(TAG, "Couldn't find font " + fontName + ". Maybe you need to call addFont() first?");
            return null;
        }
        if (cache.containsKey(fontName)) {
            return cache.get(fontName);
        } else {
            Typeface typeface = Typeface.createFromAsset(MyApplication.getInstance().getAssets(),fontFilename);
            cache.put(fontFilename, typeface);
            return typeface;
        }
    }
}
