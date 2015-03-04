package com.avenwu.deepinandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import net.avenwu.support.widget.FlatTabGroup;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by chaobin on 3/4/15.
 */
public class ExifViewerFragment extends Fragment {
    ImageView mImageView;
    TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exif_layout, null);
        mImageView = (ImageView) view.findViewById(R.id.image);
        mTextView = (TextView) view.findViewById(R.id.text);
        FlatTabGroup tabs = (FlatTabGroup) view.findViewById(R.id.tabs);
        tabs.setSelection(0);
        tabs.setOnTabCheckedListener(new FlatTabGroup.OnTabCheckedListener() {
            @Override
            public void onChecked(FlatTabGroup group, int position) {
                switch (position) {
                    case 0:
                        decodeWith(new AssetsImageExifDecoder());
                        break;
                    case 1:
                        decodeWith(new ExternalImageExifDecoder());
                        break;
                }
            }
        });
        return view;
    }

    private void decodeWith(ExifDecoder decoder) {
        AsyncTaskCompat.executeParallel(new AsyncTask<ExifDecoder, Void, ExifBean>() {
            @Override
            protected ExifBean doInBackground(ExifDecoder... params) {
                return new ExifBean(params[0].decodeBitmap(), params[0].extractExif());
            }

            @Override
            protected void onPostExecute(ExifBean exifBean) {
                if (exifBean.bitmap != null) {
                    mImageView.setImageBitmap(exifBean.bitmap);
                }
                if (exifBean.value != null) {
                    mTextView.setText(exifBean.value);
                }
            }
        }, decoder);
    }

    interface ExifDecoder {
        Bitmap decodeBitmap();

        String extractExif();
    }

    /**
     * ExifInterface seems not support image in assets
     */
    class AssetsImageExifDecoder implements ExifDecoder {
        final String ASSETS_IMAGE_PATH = "image2.jpg";

        @Override
        public Bitmap decodeBitmap() {
            try {
                return BitmapFactory.decodeStream(getResources().getAssets().open(ASSETS_IMAGE_PATH));
            } catch (IOException e) {
                Log.d("ExifViewerFragment", "AssetsImageExifDecoder decodeBitmap failed");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String extractExif() {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(getResources().getAssets().open(ASSETS_IMAGE_PATH));
                Iterator<Directory> iterator = metadata.getDirectories().iterator();
                final String output = "%s=%s";
                StringBuilder builder = new StringBuilder();
                while (iterator.hasNext()) {
                    Directory d = iterator.next();
                    for (Tag tag : d.getTags()) {
                        builder.append(String.format(output, tag.getTagName(), tag.getDescription()))
                                .append("\n");
                    }
                }
                return builder.toString();
            } catch (ImageProcessingException e) {
                e.printStackTrace();
                Log.d("ExifViewerFragment", "get attributes failed");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ExifViewerFragment", "get attributes failed");
            }

            return null;
        }
    }

    class ExternalImageExifDecoder implements ExifDecoder {
        //TODO replace with your own image path
        final String EXTRANAL_IMAGE_PATH = "/storage/sdcard1/DCIM/Camera/IMG_20150304_162932.jpg";

        @Override
        public Bitmap decodeBitmap() {
            return BitmapFactory.decodeFile(EXTRANAL_IMAGE_PATH);
        }

        @Override
        public String extractExif() {
            try {
                ExifInterface exifInterface = new ExifInterface(EXTRANAL_IMAGE_PATH);
                StringBuilder builder = new StringBuilder();
                final String output = "%s=%s";
                try {
                    Field field = exifInterface.getClass().getDeclaredField("mAttributes");
                    field.setAccessible(true);
                    HashMap<String, String> attrs = (HashMap<String, String>) field.get(exifInterface);
                    for (Map.Entry<String, String> entry : attrs.entrySet()) {
                        builder.append(String.format(output, entry.getKey(), entry.getValue())).append("\n");
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return builder.toString();
            } catch (IOException e) {
                Log.d("ExifViewerFragment", "get attributes failed");
                e.printStackTrace();
            }
            return null;
        }
    }

    class ExifBean {
        Bitmap bitmap;
        String value;

        public ExifBean(Bitmap bitmap, String value) {
            this.bitmap = bitmap;
            this.value = value;
        }
    }
}
