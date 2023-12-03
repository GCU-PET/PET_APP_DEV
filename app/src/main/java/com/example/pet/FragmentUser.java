package com.example.pet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUser extends Fragment {

    public ImageButton editButton;
    public ImageView imagePet;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // 이곳에 BoardListFragment를 불러옴.
        // 1. FragmentTransaction 시작
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        // 2. 추가할 BoardListFragment 생성
        BoardListFragment boardListFragment = new BoardListFragment();

        // 3. FrameLayout에 BoardListFragment 추가
        transaction.replace(R.id.board_container, boardListFragment);

        // 4. FragmentTransaction을 커밋하여 변경 사항 적용
        transaction.commit();

        editButton = view.findViewById(R.id.userInfo_edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInfoSettingActivity.class);
                startActivity(intent);
            }
        });

        imagePet = view.findViewById(R.id.image_pet);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ImageButton imageButton = getView().findViewById(R.id.userInfo_edit_button);
                imageButton.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
