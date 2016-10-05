package com.example.ufoma.loginmekashron;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SoapLoader extends AsyncTaskLoader<String> {

    private String TAG = "SoapLoader";

    private String userName;
    private String password;

    public SoapLoader(Context context) {
        super(context);
    }

    public void setContent(String userName, String password) {
        this.userName = userName;
        this.password = password;
        onContentChanged();
    }

    @Override
    public String loadInBackground() {

        String URL = "http://isapi.mekashron.com/StartAJob/General.dll/soap/IGeneral";
        String NAMESPACE = "urn:General.Intf";
        String METHOD = "Login";
        String SOAP_ACTION = "urn:General.Intf-IGeneral#Login";

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD);
        soapObject.addProperty("UserName", userName);
        soapObject.addProperty("Password", password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        try {
            HttpTransportSE transportSE = new HttpTransportSE(URL);
            transportSE.call(SOAP_ACTION, envelope);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException: " + e);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.e(TAG, "XmlPullParserException: " + e);
        }

        SoapObject  result = (SoapObject) envelope.bodyIn;
        String value = "";

        if(result != null) {
            PropertyInfo propertyInfo = new PropertyInfo();
            result.getPropertyInfo(0, propertyInfo);
            value = (String) propertyInfo.getValue();
        }

        return value;
    }
}
