package com.example.pet;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentUserInfoSetting extends Fragment {
    private CircleImageView petProfile;
    private static final int REQUEST_CODE = 1;

    private Button submitBtn;
    TextView userID;
    TextView userPassword;

    EditText petName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_setting, container, false);

        submitBtn = view.findViewById(R.id.userInfo_submit);

        petName = view.findViewById(R.id.pet_name);
        userID = view.findViewById(R.id.user_id);
        userPassword = view.findViewById(R.id.user_password);

        // 서버에서 받아온 유저 정보 입력

        petProfile = view.findViewById(R.id.info_setting_profile);
        petProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pickImage 메소드
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
            }
        });
        
        // 수정된 반려동물 이름이나 이미지를 업데이트
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                        petProfile.setImageBitmap(bitmap);
                        petProfile.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}