package com.example.telemedical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.accessibilityservice.AccessibilityService;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaSync;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telemedical.ConstantsUsage.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.IDN;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    private TextView createAccount, forgetPass;
    private Button signIn;
    private EditText mail, password;
    String smail, spassword;
    private FirebaseAuth mAuth;
    ScrollView layout;
    ScrollView view;
    final int MIN_KEYBOARD_HEIGHT_PX = 150;
    int hight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initGui();
    }


    void initGui() {
        createAccount = findViewById(R.id.create_account);
        createAccount.setOnClickListener(this);
        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);
        mail = findViewById(R.id.sign_in_mail);
        password = findViewById(R.id.sign_in_password);
        layout = findViewById(R.id.login_view);
        forgetPass = findViewById(R.id.forget_pass);
        forgetPass.setOnClickListener(this);
        // view = findViewById(R.id.scrollView2);


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
                        layout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                        // Notify listener about keyboard being shown.
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        // Notify listener about keyboard being hidden.
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels - hight;
                        layout.getLayoutParams().height = height;
                    }
                }
                // Save current decor view height for the next call.
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });


    }


    private Boolean getFormData() {
        String message = "necessary".toUpperCase();
        if (TextUtils.isEmpty(mail.getText().toString())) {
            mail.setError(message);
            return false;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError(message);
            return false;
        }
        smail = mail.getText().toString().trim();
        spassword = password.getText().toString().trim();
        return true;
    }


    void signIn(String mail, String pass) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Sign In Processing!");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E4E4E4")));
        dialog.show();

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if (mAuth.getCurrentUser().isEmailVerified()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            }else {
                                Toast.makeText(SignIn.this, "Verify Your Mail.", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Exception e = task.getException();
                            Toast.makeText(SignIn.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            dialog.dismiss();
                        }

                        // ...
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent home = new Intent(this, DashBoard.class);
            finish();
            startActivity(home);
        }
    }

    void setRememberMe() {
        SharedPreferences save = getSharedPreferences(Constants.remember, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = save.edit();
        editor.putString(Constants.remember, Constants.remember);
        editor.apply();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_account:
                Intent intent = new Intent(this, CreateAccount.class);
                finish();
                startActivity(intent);
                break;

            case R.id.signIn:
                if (getFormData() == true) {
                    signIn(smail, spassword);
                    setRememberMe();
                }

                break;
            case R.id.forget_pass:
                FirebaseAuth auth = FirebaseAuth.getInstance();

                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.forget_password);
                EditText text=dialog.findViewById(R.id.forgetMail);
                Button button=dialog.findViewById(R.id.cancellMail);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button sendMail=dialog.findViewById(R.id.sendMail);
                sendMail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(text.getText())){

                            auth.sendPasswordResetEmail(text.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignIn.this, "Email Has Send. Check Inbox.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else{
                            text.setError("Required");
                        }
                    }
                });
                dialog.setCancelable(false);
                dialog.show();




        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences save = getSharedPreferences(Constants.remember, Context.MODE_PRIVATE);
        String check = save.getString(Constants.remember, "");
        if (check.equals(Constants.remember)) {
            FirebaseAuth auth;
            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            this.overridePendingTransition(0, 0);
            updateUI(currentUser);
        }
    }
}
