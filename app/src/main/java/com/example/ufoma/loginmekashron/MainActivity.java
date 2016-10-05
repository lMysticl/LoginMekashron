package com.example.ufoma.loginmekashron;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private final String TAG = "testmekashron";

    private EditText mLogin;
    private EditText mPassword;
    private TextView mTextMsg;
    private Button mBtnOk;

    private final String MSG_TEXT = "msgText";
    private final String MSG_COLOR = "msgColor";
    private final String BTN_ENABLE = "btnEnable";

    private String userName;
    private String password;

    private SoapLoader mSoapLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogin = (EditText) findViewById(R.id.login);
        mPassword = (EditText) findViewById(R.id.password);
        mTextMsg = (TextView) findViewById(R.id.msg);
        mBtnOk = (Button) findViewById(R.id.btnOk);

        if (savedInstanceState != null) {
            CharSequence msgText = savedInstanceState.getCharSequence(MSG_TEXT);
            int msgColor = savedInstanceState.getInt(MSG_COLOR);
            boolean enable = savedInstanceState.getBoolean(BTN_ENABLE);

            mTextMsg.setText(msgText);
            mTextMsg.setTextColor(msgColor);
            mBtnOk.setEnabled(enable);
        }

        mSoapLoader = (SoapLoader) getSupportLoaderManager().initLoader(0, null, this);

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = mLogin.getText().toString();
                password = mPassword.getText().toString();
                mBtnOk.setEnabled(false);
                mSoapLoader.setContent(userName, password);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(MSG_TEXT, mTextMsg.getText());
        outState.putInt(MSG_COLOR, mTextMsg.getTextColors().getDefaultColor());
        outState.putBoolean(BTN_ENABLE, mBtnOk.isEnabled());
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        mSoapLoader = new SoapLoader(this);
        return mSoapLoader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if(data.contains("\"ResultCode\":-1") || data.isEmpty()) {
            mTextMsg.setText(getResources().getText(R.string.error));
            mTextMsg.setTextColor(Color.RED);
        } else {
            mTextMsg.setText(getResources().getText(R.string.successfull));
            mTextMsg.setTextColor(Color.GRAY);
        }

        mBtnOk.setEnabled(true);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }
}
