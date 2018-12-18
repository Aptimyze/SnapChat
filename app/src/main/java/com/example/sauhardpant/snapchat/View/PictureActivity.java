package com.example.sauhardpant.snapchat.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sauhardpant.snapchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureActivity extends AppCompatActivity {
    private static final String TAG = PictureActivity.class.getSimpleName();

    Bitmap bitmap;
    @BindView(R.id.save_to_story_button)
    Button storyBtn;
    @BindView(R.id.activity_picture_image_view)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);

        byte[] data = ImageHelper.data;

        if (data != null) {
            bitmap = BitmapFactory.decodeByteArray(data, 0 , data.length);
            // rotate image - appears rotated 90 degrees
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    matrix,
                    true);
            imageView.setImageBitmap(bitmap);
        }

        storyBtn.setOnClickListener(clicked -> {
            saveToStory();
        });

    }

    private void saveToStory() {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid()).child("stories");
        final String uniqueKey = dbRef.push().getKey();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("images").child(uniqueKey);
        byte[] data = convertToBytes();
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Uri imageUrl = taskSnapshot.getUploadSessionUri();
            long startMilli = System.currentTimeMillis();
            long endMilli = startMilli + (24*60*60*1000); // stories last for 24 hrs

            Map<String, Object> storyData = new HashMap<>();
            storyData.put("imageUrl", imageUrl.toString()); // not using toString gives stackOverflow
            storyData.put("startMilli", startMilli);
            storyData.put("endMilli", endMilli);

            dbRef.child(uniqueKey).setValue(storyData);
            finish();
        });
        uploadTask.addOnFailureListener(e -> {
            Log.d(TAG, "There was an error uploading task", e);
            finish(); // if it fails go back to the camera activity
        });
    }

    private byte[] convertToBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        return stream.toByteArray();
    }
}
