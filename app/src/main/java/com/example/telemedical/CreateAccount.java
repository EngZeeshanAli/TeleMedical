package com.example.telemedical;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.Formaters.UserFormater;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {
    TextView login;
    Button register;
    private EditText email, mobile, password, insurance, fileNo, name;
    private String sname, semail, smobile, sgender, spassword, sinsurance, sfileNo, profileImg = "";
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private Spinner gender;
    ScrollView view;
    final int MIN_KEYBOARD_HEIGHT_PX = 150;
    int hight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        // Write a message to the database
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        initGui();
        checkPermision();
        getPremitives();
        getEmail();
    }

    void getPremitives() {
        String CountryID = "";
        String CountryZipCode = "";
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                mobile.setText("+" + CountryZipCode);
            }
        }
    }


    void getEmail() {
        AccountManager accountManager = AccountManager.get(this);
        Account account = getAccounts(accountManager);
        if (account == null) {
        } else {
            email.setText(account.name);
        }
    }

    private static Account getAccounts(AccountManager manager) {
        Account[] accounts = manager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
            return account;
        } else {
            return null;
        }
    }


    void checkPermision() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            getEmail();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 100);
        }
    }

    void initGui() {
        login = findViewById(R.id.goSignIn);
        register = findViewById(R.id.register_user);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        name = findViewById(R.id.registername);
        email = findViewById(R.id.registermail);
        mobile = findViewById(R.id.registermob);
        gender = findViewById(R.id.gender);
        password = findViewById(R.id.register_password);
        insurance = findViewById(R.id.insurance);
        fileNo = findViewById(R.id.fileNo);
        mAuth = FirebaseAuth.getInstance();
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sgender = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        view = findViewById(R.id.createAccountsv);
        final View decorView = getWindow().getDecorView();
        // Register global layout listener.
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                // Retrieve visible rectangle inside window.
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();
                // Decide whether keyboard is visible from changing decor view height.
                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        // Calculate current keyboard height (this includes also navigation bar height when in fullscreen mode).
                        int currentKeyboardHeight = decorView.getHeight() - windowVisibleDisplayFrame.bottom;
                        hight = currentKeyboardHeight;
                        view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                        // Notify listener about keyboard being shown.
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        // Notify listener about keyboard being hidden.
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels - hight;
                        view.getLayoutParams().height = height;
                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }

    private boolean getFormData() {
        String message = "necessary".toUpperCase();

        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError(message);
        }

        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError(message);
            return false;
        }

        if (TextUtils.isEmpty(mobile.getText().toString())) {
            mobile.setError(message);
            return false;
        }


        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError(message);
            return false;
        }

        if (password.getText().length() < 8) {
            password.setError("must be 8 characters");
            return false;
        }
        sname = name.getText().toString();
        semail = email.getText().toString().trim();
        smobile = mobile.getText().toString();
        spassword = password.getText().toString().trim();
        sfileNo = fileNo.getText().toString();
        sinsurance = insurance.getText().toString();
        return true;

    }

    void setRegister(final String email, String password) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Registering ! Please Wait.");
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                savingData(uid);
                                            } else {
                                                user.delete();
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Exception exception = task.getException();
                            Toast.makeText(CreateAccount.this, exception.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void savingData(String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child(Constants.SAVE_USER);
        final UserFormater user = new UserFormater(uid, sname, semail, smobile, sgender, spassword, sinsurance, sfileNo, profileImg);
        myRef.child(uid).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                dialog.dismiss();
                updateUI();
            }
        });
    }

    private void updateUI() {
        Intent signIn = new Intent(this, SignIn.class);
        finish();
        startActivity(signIn);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goSignIn:
                Intent signIn = new Intent(this, SignIn.class);
                finish();
                startActivity(signIn);
                break;
            case R.id.register_user:
                if (getFormData() == true) {
                    setRegister(semail, spassword);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateUI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            getEmail();
        }
    }

}
