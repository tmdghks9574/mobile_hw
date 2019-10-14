package com.example.homework1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText name,id, pw, phone, address;
    RadioGroup radioGroup;
    Button confirm, cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        name = (EditText) findViewById(R.id.nameinput);
        id = (EditText) findViewById(R.id.idinput);
        pw = (EditText) findViewById(R.id.pwinput);
        phone = (EditText) findViewById(R.id.phoneinput);
        address = (EditText) findViewById(R.id.addressinput);

        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        radioGroup= (RadioGroup) findViewById(R.id.radiogroup);
    }

    private boolean pwprob(EditText pw){
        String str = pw.getText().toString().trim();
        if(str.length() <= 8){
            Toast.makeText(this,"8자리 이상 입력하세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{9,12}$",
                str)){
            Toast.makeText(this,"영문 특수문자 숫자 조합으로 입력하세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean policyprob(){
        if(radioGroup.getCheckedRadioButtonId() == R.id.radiobutton1){
            return true;
        }
        Toast.makeText(SignUpActivity.this,"동의 하지않으면 가입할수없음.",Toast.LENGTH_SHORT).show();
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm:
                String email = id.getText().toString().trim();
                String password;
                if(pwprob(pw) && policyprob()) {
                    password = pw.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                       Toast.makeText(SignUpActivity.this,"회원가입완료!",Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(SignUpActivity.this,"회원가입실패",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}