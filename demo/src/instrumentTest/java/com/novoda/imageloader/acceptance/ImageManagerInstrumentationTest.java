package com.novoda.imageloader.acceptance;

import android.graphics.Bitmap;
import android.test.InstrumentationTestCase;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.file.FileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageManagerInstrumentationTest extends InstrumentationTestCase {

    public ImageManagerInstrumentationTest(String name) {
        super();
        setName(name);
    }

    public void testAfterImageIsCached_imageIsRetrievableFromCache() throws IOException {
        final File file = createImageFile();

        ImageManager imageManager = createImageManagerBackedByFile(file);

        String url = "http://any_url_should_do_here.com";
        imageManager.cacheImage(url, 64, 64);

        assertNotNull("image should be in cache!", imageManager.getCacheManager().get(url, 64, 64));
    }

    private ImageManager createImageManagerBackedByFile(File file) {
        LoaderSettings settings = new LoaderSettings.SettingsBuilder()
                .withFileManager(new SingleFileManager(file))
                .build(getInstrumentation().getTargetContext());
        return new ImageManager(getInstrumentation().getTargetContext(), settings);
    }

    private File createImageFile() throws IOException {
        InputStream imageStream = getInstrumentation().getContext().getAssets().open("ic_launcher.png");
        int read;
        final File file = new File(getInstrumentation().getTargetContext().getCacheDir(), "image.png");
        FileOutputStream outputStream = new FileOutputStream(file);
        while ((read = imageStream.read()) != -1) {
            outputStream.write(read);
        }
        imageStream.close();
        outputStream.close();
        return file;
    }

    private static class SingleFileManager implements FileManager {
        private final File file;

        public SingleFileManager(File file) {
            this.file = file;
        }

        @Override
        public void clean() {
        }

        @Override
        public void cleanOldFiles() {
        }

        @Override
        public String getFilePath(String url) {
            return null;
        }

        @Override
        public File getFile(String url) {
            return file;
        }

        @Override
        public void saveBitmap(String fileName, Bitmap b, int width, int height) {
        }

        @Override
        public File getFile(String url, int width, int height) {
            return file;
        }
    }
}
