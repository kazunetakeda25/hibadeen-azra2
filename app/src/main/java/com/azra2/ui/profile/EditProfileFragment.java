package com.azra2.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;
import com.azra2.utils.Utils;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {
    View rootView;
    YMUser me;
    Uri profileUri;

    ImageView ivProfile;
    EditText etFirstName, etLastName, etUserName, etBio;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        me = AGApplication.the().getMe();

        ivProfile = view.findViewById(R.id.ivProfile);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etUserName = view.findViewById(R.id.etUserName);
        etBio = view.findViewById(R.id.etBio);

        String url = me.getProfileImage();
        if(!Utils.isNullOrEmpty(url)){
            Picasso.Builder builder = new Picasso.Builder(getContext());
            builder.downloader(new OkHttp3Downloader(getContext()));
            builder.build().load(url)
                    .placeholder((R.mipmap.ic_launcher_round))
                    .error(R.mipmap.ic_launcher_round)
                    .into(this.ivProfile);
        }
        etFirstName.setText(me.getFirstName());
        etFirstName.setEnabled(false);
        etLastName.setText(me.getLastName());
        etLastName.setEnabled(false);
        etUserName.setText(me.getNickName());
        etUserName.setEnabled(false);
        etBio.setText(me.getBio());

        view.findViewById(R.id.ivBackBtn).setOnClickListener(v -> {
        });

        view.findViewById(R.id.ivDoneBtn).setOnClickListener(v -> {
            updateProfile();
        });

        view.findViewById(R.id.clProfileImage).setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setFixAspectRatio(true)
                    .getIntent(getContext());
            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        });

        view.findViewById(R.id.ivAvatar_1).setOnClickListener(v -> {
            setProfileImageFromAvatar(1);
        });
        view.findViewById(R.id.ivAvatar_2).setOnClickListener(v -> {
            setProfileImageFromAvatar(2);
        });
        view.findViewById(R.id.ivAvatar_3).setOnClickListener(v -> {
            setProfileImageFromAvatar(3);
        });

        view.findViewById(R.id.ivAvatar_4).setOnClickListener(v -> {
            setProfileImageFromAvatar(4);
        });
        view.findViewById(R.id.ivAvatar_5).setOnClickListener(v -> {
            setProfileImageFromAvatar(5);
        });
        view.findViewById(R.id.ivAvatar_6).setOnClickListener(v -> {
            setProfileImageFromAvatar(6);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    ivProfile.setImageBitmap(bitmap);
                    profileUri = resultUri;


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateProfile(){
        String userId = String.valueOf(me.getId());
        String firstName = etFirstName.getText().toString().trim();
        if (Utils.isNullOrEmpty(firstName)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.message_first_name), Toast.LENGTH_SHORT).show();
            etFirstName.requestFocus();
            return;
        }

        String lastName = etLastName.getText().toString().trim();

        String nickName = etUserName.getText().toString().trim();
        if (Utils.isNullOrEmpty(nickName)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.message_nickname), Toast.LENGTH_SHORT).show();
            etUserName.requestFocus();
            return;
        }

        String bio = etBio.getText().toString().trim();

        String appToken = me.getAppToken();

        MultipartBody.Part profileImage = null;
        Uri uri = profileUri;

        if (uri != null) {
            File file = new File(profileUri.getPath());
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            profileImage = MultipartBody.Part.createFormData("profile_image", file.getName(), reqFile);
        }

        RequestBody rbAgentId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody rbFirstName = RequestBody.create(MediaType.parse("text/plain"), firstName);
        RequestBody rbLastName = RequestBody.create(MediaType.parse("text/plain"), lastName);
        RequestBody rbNickName = RequestBody.create(MediaType.parse("text/plain"), nickName);
        RequestBody rbBio = RequestBody.create(MediaType.parse("text/plain"), bio);
        RequestBody rbAppToken = RequestBody.create(MediaType.parse("text/plain"), appToken);

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.message_wait));
        progressDialog.show();

        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.userUpdateProfile(rbAgentId, rbFirstName, rbLastName, rbNickName, rbBio, rbAppToken, profileImage);

        call.enqueue(new Callback<YMUserResponse>() {
            @Override
            public void onResponse(Call<YMUserResponse> call, Response<YMUserResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {
                    YMUserResponse response1 = response.body();
                    if (response1.getResult().equalsIgnoreCase("false")) {
                        switch (response.body().getMessage()){
                            case YMConst.MSG_INVALID_TOKEN:
                                Toast.makeText(getContext(), getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return;
                    }
                    else {
                        YMUser user = response1.getData();
                        if(user != null){
                            AGApplication.the().setMe(user);
                            NavController navController = Navigation.findNavController(rootView);
                            NavDirections directions = EditProfileFragmentDirections.actionEditProfileFragmentToNavProfile();
                            navController.navigate(directions);
                        }
                    }
                }
                else{
                    Log.d("888888888888888888:",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<YMUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed to connect to server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProfileImageFromAvatar(int number){
//        if(true) return;
        int resId = 0;
        switch(number){
            case 1:
                resId = R.drawable.avatar_1;
                break;
            case 2:
                resId = R.drawable.avatar_2;
                break;
            case 3:
                resId = R.drawable.avatar_3;
                break;
            case 4:
                resId = R.drawable.avatar_4;
                break;
            case 5:
                resId = R.drawable.avatar_5;
                break;
            case 6:
                resId = R.drawable.avatar_6;
                break;
            default:
                resId = R.drawable.avatar;
                break;
        }
        ivProfile.setImageDrawable(getResources().getDrawable(resId));

        /*profileUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + getResources().getResourcePackageName(resId) + '/'
                + getResources().getResourceTypeName(resId) + '/'
                + getResources().getResourceEntryName(resId)  );*/

        /*******************************************************************************************
         * First, make bitmap from resource and save it to file of storage
         * Next, get Uri from file
        * */
        Bitmap bm = BitmapFactory.decodeResource(getResources(),resId);
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "drawable");

        boolean doSave = true;
        if (!dir.exists()) {
            doSave = dir.mkdirs();
        }

        if (doSave) {
            String fileName = "avatar.png";
            saveBitmapToFile(dir, fileName, bm, Bitmap.CompressFormat.PNG,100);
            String path = Environment.getExternalStorageDirectory() + File.separator + "drawable" + "/" + fileName;
            File imageFile = new File(path);
            profileUri = Uri.fromFile(imageFile);
        }
        else {
            Log.e("app","Couldn't create target directory.");
        }
        /******************************************************************************************/

    }

    /**
     * @param dir you can get from many places like Environment.getExternalStorageDirectory() or mContext.getFilesDir() depending on where you want to save the image.
     * @param fileName The file name.
     * @param bm The Bitmap you want to save.
     * @param format Bitmap.CompressFormat can be PNG,JPEG or WEBP.
     * @param quality quality goes from 1 to 100. (Percentage).
     * @return true if the Bitmap was saved successfully, false otherwise.
     */
    boolean saveBitmapToFile(File dir, String fileName, Bitmap bm,
                             Bitmap.CompressFormat format, int quality) {

        File imageFile = new File(dir,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bm.compress(format,quality,fos);

            fos.close();

            return true;
        }
        catch (IOException e) {
            Log.e("app",e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

}