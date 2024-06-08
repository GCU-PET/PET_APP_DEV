package com.example.pet;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;

public class FragmentCamera extends Fragment {

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_camera, container, false);

        WebView webView = view.findViewById(R.id.camera_webView);
        WebSettings webSettings = webView.getSettings();

        // URL 컨텍스트 내에서 자바스크립트 접근가능여부
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setAllowFileAccess(true);
        // 파일 기반 XSS 취약성 문제 해결 (구글지적)
        // true -> false 변경
        //1. WebView에 위험한 설정이 포함되지 않도록 설정
        webSettings.setAllowFileAccessFromFileURLs(true);
        // 다른 도메인의경우에도 허용하는가
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        // 네트워크를 통한 이미지 리소스 로딩여부 결정 (false -> 이미지로딩 금지)

        // 웹뷰에서 웹페이지의 카메라 및 마이크 접근을 허용 *****
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        // 뷰 가속 - 가속하지 않으면 영상실행 안됨, 소리만 나온다
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.setWebChromeClient(new WebChromeClient(){
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                //super.onPermissionRequest(request);
                request.grant(request.getResources());
            }
        });

        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // SSL 인증서 오류를 무시하고 로드하도록 합니다.
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                // 권한 요청을 수락
                getActivity().runOnUiThread(() -> request.grant(request.getResources()));
            }
        });

        webView.loadUrl("https://6e3b-210-119-237-46.ngrok-free.app/home"); //링크 삽입.
        //webView.setWebChromeClient(new WebChromeClient());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1);
        }
    }
}
