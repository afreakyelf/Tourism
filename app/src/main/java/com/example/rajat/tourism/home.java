package com.example.rajat.tourism;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

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
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class home extends Fragment implements MyListener , SwipeRefreshLayout.OnRefreshListener{

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    static String curlat,curlang;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    private RecyclerView recyclerView,hrv;
    private homeAdapter adapter;
    MyListener ml;
    static home news;
    final int home= 0;
    ArrayList<String> hrecycleview;
    String RESULT = "result";
    ArrayList<HashMap<String, Object>> al_details;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    final long period = 5000;
    String price,high,low,name;
    static String API = "AIzaSyBTYpsEx0f8FyKSRScuIFOL5_MOfQrKPRQ";
    horizontalAdapter hradapter;
    int i;

    HashMap<String,Object> HM;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity());

    }

    public static home newInstance(){
        home fragment = new home();
        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.layout,container,false);

        recyclerView =  rootview.findViewById(R.id.rv);
        hrv = rootview.findViewById(R.id.horizontalrv);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        hrecycleview = new ArrayList<>();
        hrecycleview.add("All");
        hrecycleview.add("cafe");
        hrecycleview.add("school");
        hrecycleview.add("atm");
        hradapter = new horizontalAdapter(hrecycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        hrv.setLayoutManager(linearLayoutManager);
        hrv.setAdapter(hradapter);


        progressBar = rootview.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        swipeRefreshLayout = rootview.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadJSON(HM,0);
            }
        });



        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        locationTrack = new LocationTrack(getContext());


        if (locationTrack.canGetLocation()) {


            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            curlang = Double.toString(longitude);
            curlat = Double.toString(latitude);
     /*       Toast.makeText(getActivity(), curlat+","+curlang, Toast.LENGTH_SHORT).show();*/
        } else {

            locationTrack.showSettingsAlert();
        }



        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HM = new HashMap<>();
        HM.put("location",curlat+","+curlang);
        HM.put("rankby","distance");
        HM.put("type","");

        HM.put("key",API);

        loadJSON(HM,0);


        listener();

    }

    public class horizontalAdapter extends RecyclerView.Adapter<horizontalAdapter.MyViewHolder>{

            private List<String> horizontalList;

            public class MyViewHolder extends RecyclerView.ViewHolder {
                public TextView txtView;
                RelativeLayout rv;

                public MyViewHolder(View view) {
                    super(view);
                    rv = view.findViewById(R.id.filterrv);
                    txtView = view.findViewById(R.id.text);

                }
            }
            public horizontalAdapter(List<String> horizontalList) {
                this.horizontalList = horizontalList;
            }

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.filter_layout, parent, false);

                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(final MyViewHolder holder, final int position) {
                holder.txtView.setText(horizontalList.get(position));

                holder.txtView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),holder.txtView.getText().toString(),Toast.LENGTH_SHORT).show();


                    }
                });
                holder.rv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), horizontalList.get(position), Toast.LENGTH_SHORT).show();
                        String filter = horizontalList.get(position);

                        HM = new HashMap<>();
                        HM.put("location",curlat+","+curlang);
                        HM.put("rankby","distance");
                        HM.put("type",filter);
                        HM.put("key",API);
                        loadJSON(HM,0);

                    }
                });


            }

            @Override
            public int getItemCount() {
                return horizontalList.size();
            }
        }

    private void listener() {
        setOnEventListener(this);
    }

    public void setOnEventListener(MyListener ml) {
        this.ml = ml;
    }

    public void loadJSON(HashMap<String,Object> HM,final int mode){
            progressBar.setVisibility(View.VISIBLE);

        final api apiInterface = apiclient.getClient().create(api.class);
        Call<ResponseBody> call = apiInterface.getJSON("json",HM,"");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBar.setVisibility(View.GONE);
                ArrayList<HashMap<String, String>> dataList = new ArrayList<>();


                if(response.isSuccessful()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    try {
                        HashMap<String, Object> dataMap = null;
                        String str_testing = response.body().string();
                        dataMap = new HashMap<String, Object>();
                        JSONObject jsonObject = new JSONObject(str_testing);



                        JSONArray data= jsonObject.getJSONArray("results");
                        for(int i=0;i<data.length();i++){
                            JSONObject d = data.getJSONObject(i);
                            //  String description = d.getString("description");
                            String title = d.getString("name");

                            String des = d.getString("vicinity");
                            String place_id = d.getString("place_id");



                            //tmp hashmap for single contact
                            HashMap<String,String> samachar = new HashMap<>();

                            if(d.has("photos")) {
                                JSONArray jsonArray = d.getJSONArray("photos");
                                JSONObject dd = jsonArray.getJSONObject(0);
                                String image = dd.getString("photo_reference");
                                samachar.put("photo_reference", image);
                            }else  {

                                samachar.put("photo_reference", "mRaAAAAnwZJKden3NA59DlCUUjbCMZGKwvyf2WKe6LRua11k70Vgrxk1qgcwhk_byNyZ9Ah2vPN9rqT3WBJHDbO_vExzSdGSn2Mk46lay51Sxqusy9QpyqdLhMFLE4YdvAvQB4KEhDMU8vcnhe60vpywqI9v8jIGhQ6bp5HmHs5UplD1sYt2ItTz6r1lw");
                            }
                            samachar.put("name",title);
                            samachar.put("place_id",place_id);
                            samachar.put("vicinity",des);

                            dataList.add(samachar);

                        }




                    Log.e("json"+"162", "jsonObject1: " + dataList);
                        dataMap.put(RESULT, dataList);

                        switch (mode)
                        {
                            case home:
                                ml.callback(dataMap, "live", mode);
                                Log.e("Utils123", "response : " + str_testing.toString());
                                break;
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    Toast.makeText(getActivity(),"Something wrong", LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Call",t.toString());
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),"Check your Internet Connection",Toast.LENGTH_SHORT);
            }
        });


    }



    @Override
    public void callback(HashMap<String, Object> tmpMap, String dataType, int mode) {
        if(mode == home){
            al_details = (ArrayList<HashMap<String, Object>>) tmpMap.get(RESULT);
            if (al_details.size()!=0){
                adapter = new homeAdapter(al_details,news,getContext());
                recyclerView.setAdapter(adapter);

            }
        }
    }

    @Override
    public void onRefresh() {
        loadJSON(HM,0);
        swipeRefreshLayout.setRefreshing(true);
        progressBar.setVisibility(View.GONE);
    }



    //LATLONG
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }
}



