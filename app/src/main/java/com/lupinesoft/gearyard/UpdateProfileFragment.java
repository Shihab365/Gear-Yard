package com.lupinesoft.gearyard;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateProfileFragment extends Fragment {
    EditText etName, etEmail, etAddress;
    String sUID, name, email, address, photo;
    CircleImageView imgPrev;
    ImageButton pickPic;
    Button subUpButon;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_GALLERY = 200;
    String file_path;
    Fragment fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_profile, container, false);

        Activity ac = getActivity();
        ac.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etName = v.findViewById(R.id.update_etName_id);
        etEmail = v.findViewById(R.id.update_etEmail_id);
        etAddress = v.findViewById(R.id.update_etAddress_id);
        imgPrev = v.findViewById(R.id.upPro_imagePrev_id);
        pickPic = v.findViewById(R.id.upPro_imageButton_id);
        subUpButon = v.findViewById(R.id.upPro_updateButton_id);

        sUID = getArguments().getString("uid");
        name = getArguments().getString("name");
        email = getArguments().getString("email");
        address = getArguments().getString("address");
        photo = getArguments().getString("photo");

        Picasso.with(getContext()).load(photo).into(imgPrev);
        etName.setText(name);
        etEmail.setText(email);
        etAddress.setText(address);

        pickPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=23){
                    if(checkPermission()){
                        filePicker();
                    }
                    else{
                        requestPermission();
                    }
                }
                else{
                    filePicker();
                }
            }
        });

        subUpButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file_path!=null){
                    UploadFile();
                }
                else{
                    Toast.makeText(getContext(), "Please Select Photo First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    private void UploadFile() {
        UploadUpdate uploadUpdate = new UploadUpdate();
        uploadUpdate.execute(new String[]{file_path});
    }

    private void filePicker(){
        Intent openGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openGallery.addCategory(Intent.CATEGORY_OPENABLE);
        openGallery.setType("image/*");
        startActivityForResult(openGallery, REQUEST_GALLERY);
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(getContext(), "Please Give Permission to Upload File.", Toast.LENGTH_SHORT).show();
        }
        else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext(), "Permission Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_GALLERY && resultCode== Activity.RESULT_OK){
            String filePath = getRealPathFromUri(data.getData());

            Bitmap bitmap= BitmapFactory.decodeFile(filePath);
            imgPrev.setImageBitmap(bitmap);
            this.file_path=filePath;
        }
    }

    private String getRealPathFromUri(Uri uri){
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        String result;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            cursor.moveToFirst();
            String image_id = cursor.getString(0);
            image_id = image_id.substring(image_id.lastIndexOf(":") + 1);
            cursor.close();
            cursor = getActivity().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        }
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return result;
    }

    public class UploadUpdate extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("true")){
                Toast.makeText(getContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "File Uploaded Failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            if(uploadFile(strings[0])){
                return "true";
            }
            else{
                return "failed";
            }
        }

        private boolean uploadFile(String path){
            final String upName = etName.getText().toString();
            final String upEmail = etEmail.getText().toString();
            final String upAddress = etAddress.getText().toString();
            File file=new File(path);
            try{
                RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("files",file.getName(),RequestBody.create(MediaType.parse("image/*"),file))
                        .addFormDataPart("some key","some value")
                        .addFormDataPart("submit","submit")
                        .addFormDataPart("UID", sUID)
                        .addFormDataPart("Name", upName)
                        .addFormDataPart("Email", upEmail)
                        .addFormDataPart("Address", upAddress)
                        .build();
                Request request=new Request.Builder()
                        .url("http://100.72.26.84/GYadminPanel/appDB/pro_update/profile_update.php")
                        .post(requestBody)
                        .build();
                OkHttpClient client=new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            Bundle bundle = new Bundle();
                            bundle.putString("uid",sUID);
                            fragment = new ProfileFragment();
                            fragment.setArguments(bundle);
                            getFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right,
                                            R.anim.enter_right_to_left, R.anim.exit_right_to_left)
                                    .replace(R.id.fragmentId, fragment)
                                    .commit();
                        }
                    }
                });
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
