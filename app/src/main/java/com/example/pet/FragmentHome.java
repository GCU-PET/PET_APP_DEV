package com.example.pet;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import com.squareup.picasso.Picasso;

import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.util.Calendar;

public class FragmentHome extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    //private ImageView imageViewPet;
    private ActivityResultLauncher<String> imagePickerLauncher;

    private TextView dateTextView; // 선택된 날짜를 표시할 TextView
    private int selectedYear, selectedMonth, selectedDayOfMonth; // 사용자가 선택한 날짜의 년, 월, 일
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ImageButton btnAddImage = rootView.findViewById(R.id.btn_add_image);

        // LinearLayout 참조 가져오기
        LinearLayout containerLayout = rootView.findViewById(R.id.container_layout);

        // 사용자가 선택한 날짜를 표시할 TextView 찾기
        dateTextView = rootView.findViewById(R.id.date);

        // 'Date' 버튼 클릭 시 DatePickerDialog를 열도록 리스너 추가
        Button dateButton = rootView.findViewById(R.id.date);
        dateButton.setOnClickListener(v -> openDatePicker());

        // 고정된 너비와 높이 값 설정
        int fixedWidth = 480; // 원하는 너비
        int fixedHeight = 240; // 원하는 높이

        // 이미지 url 24개를 받아왔다고 치고(백엔드 서버에서)
        String[] imageUrls = new String[24];
        for (int i = 0; i < imageUrls.length; i++) {
            imageUrls[i] = "https://flexible.img.hani.co.kr/flexible/normal/960/960/imgdb/resize/2019/0121/00501111_20190121.JPG";
        }

        // 반복문을 사용하여 TextView와 View를 동적으로 생성 및 추가
        for (int i = 0; i < 24; i++) {
            // "00:00"부터 "24:00"까지
            String time = String.format("%02d:00", i);

            // TextView 생성
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(time);
            textView.setTextAppearance(requireContext(), R.style.time_line);
            containerLayout.addView(textView);

//            // View 생성 -> 이미지가 표시될 곳을 테스트로 영역을 띄워봄.
//            View view = new View(getContext());
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    fixedWidth, // 고정된 너비
//                    fixedHeight); // 고정된 높이
//            layoutParams.gravity = Gravity.CENTER;
//            layoutParams.setMargins(0, 0, 10, 0); // 원하는 마진 설정
//            view.setLayoutParams(layoutParams);
//            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black)); // 배경색 설정
//            containerLayout.addView(view);

            // 백엔드에서 Image url을 가져오면 실제로 띄우는 코드.
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    fixedWidth,
                    fixedHeight);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(0, 0, 10, 0);
            imageView.setLayoutParams(layoutParams);

            //Picasso라는 라이브러리를 이용해서 image url을 image로 띄움.
            loadImageFromUrl(imageView, imageUrls[i]);
            containerLayout.addView(imageView);


        }


        // Set up ActivityResultLauncher for image picker
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        btnAddImage.setImageURI(result); // Set selected image to imageViewPet
                    }
                });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

// Set up ActivityResultLauncher for image picker
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        // 이미지를 원형으로 자르기
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(result);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            inputStream.close();

                            // 이미지를 원형으로 자르기
                            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                            Paint paint = new Paint();
                            paint.setShader(shader);
                            paint.setAntiAlias(true);
                            Canvas canvas = new Canvas(circleBitmap);
                            float radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2f;
                            canvas.drawCircle(bitmap.getWidth() / 2f, bitmap.getHeight() / 2f, radius, paint);

                            // 원형으로 잘린 이미지를 ImageButton에 설정
                            btnAddImage.setImageBitmap(circleBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return rootView;
    }

    // DatePickerDialog 열기
    private void openDatePicker() {
        // 현재 날짜로 DatePickerDialog 초기화
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePickerDialog 생성 및 날짜 선택 리스너 설정
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, monthOfYear, dayOfMonth1) -> {
            // 사용자가 선택한 날짜 저장
            selectedYear = year1;
            selectedMonth = monthOfYear;
            selectedDayOfMonth = dayOfMonth1;

            // 선택된 날짜를 TextView에 표시
            displaySelectedDate();
        }, year, month, dayOfMonth);

        // DatePickerDialog 보이기
        datePickerDialog.show();
    }

    // 선택된 날짜를 TextView에 표시
    private void displaySelectedDate() {
        // 선택된 날짜를 TextView에 표시
        dateTextView.setText(String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth));
    }

    // FragmentHome 클래스 내부에서
    private void loadImageFromUrl(ImageView imageView, String imageUrl) {
        Picasso.get().load(imageUrl).into(imageView);
    }

    private void openFileChooser() {
        imagePickerLauncher.launch("image/*");
    }

}


