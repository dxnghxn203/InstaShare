package com.example.instashare.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;

import com.example.instashare.Model.User;
import com.example.instashare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private ImageButton btnUpload;
    private ImageView imageView;
    private Uri imageUri;
    private StorageReference storageReference;
    private User user;
    private boolean flag = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        btnUpload = findViewById(R.id.btnUpload);
        btnBack = findViewById(R.id.btnCloseUpload);
        imageView = findViewById(R.id.imgUpload);
        // Nhận đường dẫn của ảnh từ Intent
        String imageUriString = getIntent().getStringExtra("imageUri");
        int facing = getIntent().getIntExtra("len", 0);
        user = (User) getIntent().getParcelableExtra("user");
        // Kiểm tra xem đường dẫn có tồn tại không
        if (imageUriString != null) {
            flag = true;
            // Chuyển đổi đường dẫn từ String sang Uri
            imageUri = Uri.parse(imageUriString);
            // Đặt ảnh vào ImageView
            imageUri = Uri.parse(imageUriString);

            // Xoay ảnh sang phải 90 độ và hiển thị trong ImageView
            try {
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Bitmap rotatedBitmap = rotateBitmap(originalBitmap, 90);
                if(facing == CameraSelector.LENS_FACING_FRONT)
                    rotatedBitmap = rotateBitmap(originalBitmap, 270);
                imageView.setImageBitmap(rotatedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAccountActivity();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void backAccountActivity()
    {
        Intent i = new Intent(UploadActivity.this, MainPageActivity.class);
        i.putExtra("user", user);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        backAccountActivity();
    }

    private void uploadImage() {
        if(flag) {
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fileName = dateFormat.format(currentDate) + "_" + user.getUid();
            storageReference = FirebaseStorage.getInstance().getReference().child("Images");

            storageReference.child(fileName).putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(UploadActivity.this, "Đăng ảnh thành công", Toast.LENGTH_SHORT).show();
                            backAccountActivity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this, "Đăng ảnh thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
            Toast.makeText(UploadActivity.this, "Không tìm thấy ảnh", Toast.LENGTH_SHORT).show();
    }

}
