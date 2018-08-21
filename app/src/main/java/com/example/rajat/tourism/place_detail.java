package com.example.rajat.tourism;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.rajat.tourism.home.API;

public class place_detail extends Fragment implements MyListener{

    TextView place_id;

    HashMap<String, Object> HM;
    MyListener ml;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_place_detail,null);
        place_id = v.findViewById(R.id.placeid);
     //   place_id.setText(getArguments().getString("placeid"));




        HM = new HashMap<>();
        HM.put("placeid", getArguments().getString("placeid"));
        HM.put("fields","name");
        HM.put("key",API);
        fn_response(getActivity(),HM,0);

        listener();
        return v;
    }




    private void listener(){
        setOnEventListener(this);


    }



    public void setOnEventListener(MyListener ml) {
        this.ml = ml;
    }


    public void fn_response(final Context context,HashMap<String, Object> HM, final int mode){

        api apiInterface = apiclientdetaik.getClient2().create(api.class);
        Call<ResponseBody> call = apiInterface.getJSONdetails("json",HM,"");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                List<HashMap<String, String>> dataList = new ArrayList<>();
                if(response.isSuccessful()) {
                    try {
                        HashMap<String, Object> dataMap = null;
                        String str_testing = response.body().string();
                        dataMap = new HashMap<String, Object>();

                        JSONObject jsonObject = new JSONObject(str_testing);
                        JSONObject data= jsonObject.getJSONObject("result");


                            //  String description = d.getString("description");
                            String title = data.getString("name");

                            HashMap<String,String> samachar = new HashMap<>();

                            samachar.put("name",title);

                            place_id.setText(title);
                            dataList.add(samachar);
                        dataMap.put("result", dataList);
                        switch (mode)
                        {
                            case 0:
                                ml.callback(dataMap, "live", mode);
                                Log.e("Utils123", "response : " + str_testing.toString());
                                break;
                        }


                        Log.e("MainActivity",str_testing);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context,"Something wrong", LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Call",t.toString());

            }
        });
    }


    @Override
    public void callback(HashMap<String, Object> tmpMap, String dataType, int mode) {

    }
}