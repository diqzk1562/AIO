package com.example.aio;

import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// 고객센터 화면 액티비티
public class CustomerServiceActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView cemail, cnum, caddr1, caddr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        cemail = (TextView) findViewById(R.id.service_cemail);
        cnum = (TextView) findViewById(R.id.service_cnum);
        caddr1 = (TextView) findViewById(R.id.service_caddr1);
        caddr2 = (TextView) findViewById(R.id.service_caddr2);

        Company_information company_information = new Company_information("AIO@gmail.com",
                new Address("경상북도 경산시 대학로 280", "영남대학교"),
                "053-810-2114");

        // 정보 출력
        cemail.setText(company_information.getCompany_name());
        Address address = company_information.getCompany_address();
        caddr1.setText(address.getStreet_name());
        caddr2.setText(address.getDetail_address());
        cnum.setText(company_information.getCompany_num());

        // 구글맵을 이용한 지도.
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.cMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng YU = new LatLng(35.830035, 128.7614061);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(YU);
        markerOptions.title("영남대학교");
        markerOptions.snippet("경산의 자랑");
        googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(YU, 17));
    }

}
