package com.azra2.ui.welcome;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.PreferencesHelper;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;
import com.azra2.ui.MainActivity;
import com.azra2.utils.TinyDB;
import com.azra2.utils.Utils;
import com.hbb20.CountryCodePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupActivity extends AuthActivity {

    String TAG = getClass().getSimpleName();
    private ProgressDialog progressDialog;
    
    AppCompatEditText etFirstName, etLastName, etUsername, etEmail, etPassword, etPhone, etBirthday;
    CardView cvSignup;
    CircleImageView ivProfile;
    CountryCodePicker countryCodePicker;

    LinearLayout llProfile;

    RadioButton rbMale;

    final int REQUEST_CAMERA    = 101;
    final int REQUEST_GALLERY   = 102;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.message_wait));

        llProfile = findViewById(R.id.llProfile);
        ivProfile = findViewById(R.id.ivProfile);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        etPhone = findViewById(R.id.etPhone);
        etBirthday = findViewById(R.id.etBirthday);

        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.setCountryForPhoneCode(966); //Saudi

        rbMale = findViewById(R.id.radioMale);
        cvSignup = findViewById(R.id.cvSignup);

        etBirthday.setOnClickListener(view -> showDatePicker());

        cvSignup.setOnClickListener(view->signup());

        findViewById(R.id.tvLogin).setOnClickListener(view->login());

        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 && etFirstName.getText().length() > 0  && etUsername.getText().length() > 0 && etEmail.getText().length() > 0) {
                    cvSignup.setCardBackgroundColor(getResources().getColor(R.color.btnEnableColor));
                } else {
                    cvSignup.setCardBackgroundColor(getResources().getColor(R.color.btnDisableColor));
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    ivProfile.setImageBitmap(bitmap);
                    imageUri = resultUri;
                    ivProfile.setVisibility(View.VISIBLE);
                    llProfile.setVisibility(View.GONE);

//                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//                    byte [] pngByteArray= Utils.getPNGByteArray(bitmap);
//                    mPFPhoto = new ParseFile("custom_profile.png", pngByteArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUEST_CAMERA) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCamera();
//            } else {
//                Log.d("SignupActivity", "camera permission denied");
//            }
//        } else if (requestCode == REQUEST_GALLERY) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openGallery();
//            } else {
//                Log.d("SignupActivity", "gallery permission denied");
//            }
//        }
//    }
//    private void showPhotoDialog() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getResources().getString(R.string.select_avatar));
//
//        final View customLayout = getLayoutInflater().inflate(R.layout.view_photo_dialog, null);
//        builder.setView(customLayout);
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        customLayout.findViewById(R.id.tvCamera).setOnClickListener(view -> {
//            dialog.dismiss();
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                    == PackageManager.PERMISSION_GRANTED) {
//                openCamera();
//            } else {
//                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);
//            }
//        });
//
//        customLayout.findViewById(R.id.tvGallery).setOnClickListener(view -> {
//            dialog.dismiss();
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                openGallery();
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_GALLERY);
//            }
//        });
//    }
//    private void openCamera() {
//        Toast.makeText(this, "camera", Toast.LENGTH_SHORT).show();
//    }
//    private void openGallery() {
//        Toast.makeText(this, "gallery", Toast.LENGTH_SHORT).show();
//    }

    public void onPickProfile(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .start(this);
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String strDate = formatter.format(calendar.getTime());
                etBirthday.setText(strDate);
            }
        }, 2000, 0, 1);
        datePickerDialog.show();
    }

    private void showFreeCoinDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getResources().getString(R.string.select_avatar));

        final View customLayout = getLayoutInflater().inflate(R.layout.view_free_coin_dialog, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        customLayout.findViewById(R.id.ivClose).setOnClickListener(view -> {
            dialog.dismiss();
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            SignupActivity.this.finish();
        });

        customLayout.findViewById(R.id.cvShare).setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();
        });

        customLayout.findViewById(R.id.llCopyLink).setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Copy link", Toast.LENGTH_SHORT).show();
        });
    }

    private void signup() {

        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        if (Utils.isNullOrEmpty(firstName)) {
            Toast.makeText(this, getResources().getString(R.string.message_first_name), Toast.LENGTH_SHORT).show();
            etFirstName.requestFocus();
            return;
        }

        String nickName = etUsername.getText().toString().trim();
        if (Utils.isNullOrEmpty(nickName)) {
            Toast.makeText(this, getResources().getString(R.string.message_nickname), Toast.LENGTH_SHORT).show();
            etUsername.requestFocus();
            return;
        }

        String phone = etPhone.getText().toString().trim();
        if (Utils.isNullOrEmpty(phone)) {
            Toast.makeText(this, getResources().getString(R.string.message_phone), Toast.LENGTH_SHORT).show();
            etPhone.requestFocus();
            return;
        }

        String email = etEmail.getText().toString().trim();
        if (Utils.isNullOrEmpty(email)) {
            Toast.makeText(this, getResources().getString(R.string.message_email), Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }

        if (!Utils.isValidEmail(email)) {
            Toast.makeText(this, getResources().getString(R.string.message_email_invalid), Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }

        String password = etPassword.getText().toString();
        if (Utils.isNullOrEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.message_password), Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        String age = etBirthday.getText().toString();
        if (Utils.isNullOrEmpty(age)) {
            Toast.makeText(this, getResources().getString(R.string.message_age), Toast.LENGTH_SHORT).show();
            showDatePicker();
            etBirthday.requestFocus();
            return;
        }

        String gender = "Female";
        if (rbMale.isChecked()) {
            gender = "Male";
        }

        String countryCode = countryCodePicker.getSelectedCountryCode();

        if (!Utils.getInstance(this).isOnline()) {
            Toast.makeText(getApplicationContext(), "Please check network state.", Toast.LENGTH_SHORT).show();
            return;
        }

        MultipartBody.Part profileImage = null;

        if (imageUri != null) {
            File file = new File(imageUri.getPath());
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            profileImage = MultipartBody.Part.createFormData("profile_image", file.getName(), reqFile);
        }

        RequestBody rbFirstName = RequestBody.create(MediaType.parse("text/plain"), firstName);
        RequestBody rbLastName = RequestBody.create(MediaType.parse("text/plain"), lastName);
        RequestBody rbNickName = RequestBody.create(MediaType.parse("text/plain"), nickName);
        RequestBody rbPhone = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody rbEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody rbCountry = RequestBody.create(MediaType.parse("text/plain"), countryCode);
        RequestBody rbPassword = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody rbGender = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody rbAge = RequestBody.create(MediaType.parse("text/plain"), age);
        RequestBody rbDevice = RequestBody.create(MediaType.parse("text/plain"), deviceToken);

        progressDialog.show();

        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.userSignup(rbFirstName, rbLastName, rbNickName, rbPhone, rbEmail, rbAge, rbGender, rbCountry, rbPassword, rbDevice, profileImage);

        call.enqueue(new Callback<YMUserResponse>() {
            @Override
            public void onResponse(Call<YMUserResponse> call, Response<YMUserResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {
                    YMUserResponse response1 = response.body();
                    if (response1.getResult().equalsIgnoreCase("false")) {
                        Toast.makeText(getApplicationContext(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        YMUser user = response1.getData();
                        if(user != null){
                            //save user info to TinyDB for Auto Login(to storage)
                            TinyDB tinyDB = new TinyDB(getApplicationContext());
                            tinyDB.putObject(YMConst.KEY_CURRENT_USER, user);

                            //save user info to memory
                            AGApplication.the().setMe(user);

                            //save login info to sharedPreference(to storage)
                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_STATE, YMConst.LOGGED_IN);
//                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_EMAIL, email);
//                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_PASS, password);
//                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_TOKEN, deviceToken);

                            goMain();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<YMUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, t.getMessage());
            }
        });

    }

    private void login() {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        SignupActivity.this.finish();
    }

    private void goMain() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        SignupActivity.this.finish();
    }
}
