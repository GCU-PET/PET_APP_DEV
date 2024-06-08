package com.example.pet;

import static android.app.Activity.RESULT_OK;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentUserInfoSetting extends Fragment {
    //private CircleImageView petProfile;
    private static final int REQUEST_CODE = 1;

    private String profile_path;

    //private Button submitBtn;
    private TextView userID;
    private TextView userPassword;
    //private CircleImageView profile;

    private EditText petName;
    private EditText userName;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_setting, container, false);

        //submitBtn = view.findViewById(R.id.userInfo_submit);

        userName = view.findViewById(R.id.user_name);
        petName = view.findViewById(R.id.pet_name);
        userID = view.findViewById(R.id.user_id);
        userPassword = view.findViewById(R.id.user_password);

        //profile = view.findViewById(R.id.info_setting_profile);

//        String imagePath = getImagePath();
//        if (imagePath != null) {
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            profile.setImageBitmap(bitmap);
//        }
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickImage();
//            }
//        });

        // 유저 정보 GET
        getUserInfo();
        
        // 수정된 반려동물 이름이나 이미지를 업데이트
//        submitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String pw = userPassword.getText().toString();
//                //String id = userID.getText().toString();
//                String pet_name = petName.getText().toString();
//                String user_name = userName.getText().toString();
//
//                //UpdateUserRequest request = new UpdateUserRequest(pw, id, pet_name);
//
//                //회원정보 업데이트
//                RetrofitClient.postUpdateUserInfo(getContext(), pw, user_name, pet_name);
//
//                getUserInfo();
//
//            }
//        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
//            if (data != null && data.getData() != null) {
//                imageUri = data.getData();  // 선택한 이미지의 URI를 가져옵니다.
//
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
//                    profile.setImageBitmap(bitmap);  // CircleImageView에 이미지 설정
//                    saveImageToInternalStorage(bitmap); // 이미지를 내부 저장소에 저장
//                    // 저장된 이미지의 파일 경로를 사용할 수 있습니다. 이를 활용하여 필요한 곳에서 이미지를 불러올 수 있습니다.
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getContext(), "이미지를 로드하는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        try {
            // 내부 저장소에 파일 이름을 지정합니다.
            File directory = getActivity().getFilesDir(); // 앱의 파일 디렉터리를 가져옵니다.
            File file = new File(directory, "profileImage.jpg");

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // 비트맵을 JPEG 형식으로 압축
            fos.close();

            // SharedPreferences에 이미지 경로 저장
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("profileImagePath", file.getAbsolutePath());
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("saveImageStorage","내부 저장소 이미지 저장에 실패했습니다.");
        }
    }

    private String getImagePath() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("profileImagePath", null);  // 기본값으로 null 설정
    }


    private void getUserInfo() {
        UserServiceApi service = RetrofitClient.getUserServiceApi(getContext());
        Call<UserInfoResponse> call = service.getUserInfo();

        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserInfoResponse userInfoResponse = response.body();
                    // userInfo를 이용하여 필요한 작업 수행
                    if (userInfoResponse.isResult()) {
                        UserInfoResponse.ResponseData userInfo = userInfoResponse.getResponse();

                        userID.setText(userInfo.getID());
                        userPassword.setText(userInfo.getPW());
                        userName.setText(userInfo.getUserName());
                        petName.setText(userInfo.getPetName());

                        Log.i("USERINFO","유저 정보를 불러오는데 성공했습니다.");
                    }
                    else {
                        Log.e(TAG, "Response Error :: Result is false");
                        Toast.makeText(getContext(), "유저 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 응답은 받았지만 성공적이지 않은 경우 처리
                    Log.e(TAG, "Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                // 호출 실패 처리
                Toast.makeText(getContext(), "유저 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}