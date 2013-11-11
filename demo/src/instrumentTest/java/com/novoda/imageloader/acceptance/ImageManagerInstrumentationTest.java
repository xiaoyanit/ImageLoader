package com.novoda.imageloader.acceptance;

import android.test.InstrumentationTestCase;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.network.NetworkManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageManagerInstrumentationTest extends InstrumentationTestCase {
    private static final String IMAGE_URL = "http://thisurldontmatter.co.whaat";
    private static final int WIDTH = 0;
    private static final int HEIGHT = 0;

    public ImageManagerInstrumentationTest(String name) {
        super();
        setName(name);
    }

    public void testAfterImageIsCached_imageIsRetrievableFromCache() throws IOException {
        ImageManager imageManager = createImageManagerBackedByCustomNetworkManager();

        imageManager.cacheImage(IMAGE_URL, WIDTH, HEIGHT);

        assertNotNull("image should be in cache!", imageManager.getCacheManager().get(IMAGE_URL, WIDTH, HEIGHT));
    }

    private ImageManager createImageManagerBackedByCustomNetworkManager() {
        LoaderSettings settings = new LoaderSettings.SettingsBuilder()
                .withNetworkManager(new SingleUrlNetworkManager())
                .build(getInstrumentation().getTargetContext());
        return new ImageManager(getInstrumentation().getTargetContext(), settings);
    }

    private class SingleUrlNetworkManager implements NetworkManager {
        @Override
        public void retrieveImage(String url, File file) {
            if (!url.equals(IMAGE_URL)) {
                return;
            }
            InputStream imageStream = retrieveInputStream(url);
            file = createImageFileFromStream(file, imageStream);
        }

        @Override
        public InputStream retrieveInputStream(String url) {
            try {
                return getInstrumentation().getContext().getAssets().open("ic_launcher.png");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private File createImageFileFromStream(File file, InputStream imageStream) {
            if (imageStream == null) {
                return null;
            }
            int read;
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                if (outputStream == null) {
                    return null;
                }
                while ((read = imageStream.read()) != -1) {
                    outputStream.write(read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    imageStream.close();
                    outputStream.close();
                } catch (IOException ignore) {}
            }
            return file;
        }
    }
}
