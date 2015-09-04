package com.napzzz.Activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.napzzz.R;

public class LoginDetailActivity extends Activity implements OnClickListener {

	EditText emailEditText,passwordEditText;
	TextView forgotPasswordTextView;
	Button loginBtn,signUpBtn;
	ConnectionDetector cDetector;
	AlertDialogManager alert;
	//GCM PROJECT ID
    static final String SENDER_ID = "822012508209"; 
    Context mContext;
    String email,password; 
    private ProgressDialog pDialog;
    private String deviceID="", registrationId="", version="";
    boolean hasLoggedIn;
	SharedPreferences sharedPreference;
	SharedPreferences login_pref;
	EditText forgotEmailEditText;	
	String forgot_pass_email;
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String LOGIN_PREF = "LoginPrefs" ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext=(Context) getApplicationContext();
		cDetector=new ConnectionDetector(getApplicationContext());
		alert=new AlertDialogManager();
		sharedPreference = getSharedPreferences(LoginDetailActivity.PREFS_NAME, 0);
		
		login_pref = this.getApplicationContext().getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE);
		
		hasLoggedIn = sharedPreference.getBoolean("hasLoggedIn", false);
		 		
		if(hasLoggedIn)
		 {
		    	  //Go directly to main activity.
		          Intent myIntent = new Intent(LoginDetailActivity.this,MainActivity.class);
		          myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	              finish();
	              startActivity(myIntent);
	      } 	
	    else{
			setContentView(R.layout.activity_login_detail);
		}
		  /*{
				setContentView(R.layout.activity_login_detail);
				
				emailEditText=(EditText) findViewById(R.id.emailEditText);
				passwordEditText=(EditText) findViewById(R.id.passwordEditText);
				forgotPasswordTextView=(TextView ) findViewById(R.id.forgotPasswordTextView);
				loginBtn=(Button) findViewById(R.id.loginBtn);
				signUpBtn=(Button) findViewById(R.id.signUpBtn);
			 
			    GCMRegistrar.checkDevice(getApplicationContext());
                GCMRegistrar.checkManifest(getApplicationContext());
			    registerGCMId();
			 	// Get GCM registration id
			    registrationId = GCMRegistrar.getRegistrationId(getApplicationContext());
			    if(registrationId.equals("") || registrationId.equals(null))
			 		 registerGCMId();
			 		 generateDeviceID();
			 		
			 		 loginBtn.setOnClickListener(this);
			 		 signUpBtn.setOnClickListener(this);
			 		 forgotPasswordTextView.setOnClickListener(this);
		      }*/
		 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        switch(v.getId()){
		case R.id.loginBtn:
			loadLogin();
			break;
			
        case R.id.forgotPasswordTextView:
			forgotPasswordDialog();
			break;
			
        case R.id.signUpBtn:
			
    		Intent intt =new Intent(LoginDetailActivity.this,SignUpActivity.class);
    	    startActivity(intt);
    		//finish();
    	 
    	    break;
		}
		return;
	}
	
	private void registerGCMId(){
		GCMRegistrar.register(getApplicationContext(), SENDER_ID);
		GCMRegistrar.setRegisteredOnServer(getApplicationContext(), true);
	}
	
	private void generateDeviceID(){
		TelephonyManager    telephonyManager;                                             
	    
	    telephonyManager  =  
	         ( TelephonyManager )getSystemService( Context.TELEPHONY_SERVICE ); // for device id....
	   //  * getDeviceId() function Returns the unique device ID.
	   //  * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.  
	         deviceID = telephonyManager.getDeviceId();
	   //    Toast.makeText(getApplicationContext(), deviceID, Toast.LENGTH_LONG).show(); // showing device id.....
	 }
	
	private void loadLogin(){
		email=emailEditText.getText().toString();
		password=passwordEditText.getText().toString();

		if(!cDetector.isConnectingToInternet())
			alert.showAlertDialog(LoginDetailActivity.this, "Network Connection", "Enable Your Internet Connection", false);
     	
		else if(!LoginDetailActivity.isValidEmail(email))
		{
			emailEditText.setError("Invalid Email");
		}
		
		else if(password.equals("") || password==null)
		{
      	    passwordEditText.setError("Please Fill Password");
		}
		else{
			   
			//new MasterLogin().execute();
		
		}
			
	}
/*
	private class MasterLogin extends AsyncTask<String, Void, Void> {

		boolean isNewCategoryCreated = false,err=false;
		String response,status;
		public JSONArray JsonArray ;
	    String user_id,full_name,user_email,dob,phone_number,gender;
	    
		 @Override
		 protected void onPreExecute() {
			 super.onPreExecute();
			 pDialog = new ProgressDialog(LoginDetailActivity.this);
			 pDialog.setTitle("Login");
			 pDialog.setMessage("Please Wait...");
			 pDialog.setCancelable(false);
			 pDialog.show();
				
			 if(registrationId.equals("")|| registrationId.equals(null)){
				 registrationId = GCMRegistrar.getRegistrationId(LoginDetailActivity.this);
				 registerGCMId();
			 }
		 }
		 @Override
		 protected Void doInBackground(String... arg) {

			 ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			 
			 params.add(new BasicNameValuePair("action", "login"));
			 params.add(new BasicNameValuePair("username", email)); // test@gmail.com
			 params.add(new BasicNameValuePair("password", password)); // 123456
		     params.add(new BasicNameValuePair("device_id", deviceID));
			 params.add(new BasicNameValuePair("gcm_id", registrationId));
			
			 ServiceHandler serviceClient = new ServiceHandler();
			 try{
				 String json = serviceClient.makeServiceCall(ServiceHandler.URL_NEW_USER_LOGIN,
						 ServiceHandler.POST, params);
				
				  if(!json.equals("error")){

					  JSONObject obj=new JSONObject(json);
					  response=obj.getString("msg");
					  status=obj.getString("status");
                	
				  if(response.equals("1")){
					 JSONObject json2 =obj.getJSONObject("data");
					 user_id=json2.getString("u_id");
					 full_name=json2.getString("full_name");
					 user_email=json2.getString("user_email");
					 dob=json2.getString("dob");
					 phone_number=json2.getString("phone_number");
					 gender=json2.getString("gender");
				      }
				
				  }
				 else
					 err=true;
			 }
			 
			 catch(Exception e){
				 e.printStackTrace();
				 pDialog.dismiss();
				 err=true;
			 }
			 return null;
		 }

		 @Override 
		 protected void onPostExecute(Void result) {
			 super.onPostExecute(result);
			 if (pDialog.isShowing()){
				 pDialog.dismiss();
				 if(!err){
					 
					 if(response.equals("1")){
						 
						    Intent in =new Intent(LoginDetailActivity.this,MainActivity.class);
							startActivity(in);
							in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							finish();
							  
							Editor edit=sharedPreference.edit();
							edit.putBoolean("hasLoggedIn", true);
						    edit.commit();
						       
						    ConnectionDetector.showToast(getApplicationContext(), "Login Successfully ");
						     // for saving values in shared prefrences....
						  		Editor editor1 = login_pref.edit();
						  		editor1.putString("has_login", "1");
						  		editor1.putString("user_id", user_id);
						  		editor1.putString("full_name", full_name);
						  		editor1.putString("user_email", user_email);
						  		editor1.putString("dob", dob);
						  		editor1.putString("phone_number", phone_number);
						  		editor1.putString("gender", gender);

						  		editor1.commit();
						  	}
						else if(response.equals("0")){
							    ConnectionDetector.showToast(getApplicationContext(), status);
						}
						else if(response.equals("3")){
							    ConnectionDetector.showToast(getApplicationContext(), "Please Fill Correct Credentials");	
						}
					
				      }
				        else
					            Toast.makeText(mContext, "Something wrong", Toast.LENGTH_SHORT).show();
			 }
		 }
   }*/

public void forgotPasswordDialog() {
		
		LayoutInflater li = LayoutInflater.from(LoginDetailActivity.this);
		LinearLayout voice = (LinearLayout)li.inflate(R.layout.activate_now_dialog, null);    
	      AlertDialog.Builder builder = new AlertDialog.Builder(LoginDetailActivity.this);
	    //  builder.setTitle("Recorder");
	      
	      builder.setView(voice);
	      builder.setCancelable(false);

	      forgotEmailEditText = (EditText) voice.findViewById(R.id.forgotEmailEditText);
	      
	      builder.setPositiveButton("Activate",
	        new DialogInterface.OnClickListener() {
	       public void onClick(DialogInterface dialog, int whichButton) {
	    	   
	    	   forgot_pass_email = forgotEmailEditText.getText().toString();
	    	  
	    		 
	    	  if(forgot_pass_email.equals("") || forgot_pass_email==null){
	    		   Toast.makeText(getApplicationContext(), "Please enter email id", Toast.LENGTH_SHORT).show();
	    	   }
	    	  else if(!LoginDetailActivity.isValidEmail(forgot_pass_email))
	  		{
	    		   Toast.makeText(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_SHORT).show();
	  	    	 
	    	}
	    	   else{
	    		  // new ForgotPassword().execute();
	    	   
	    	   }
	    	 }
	      }); 
	      builder.setNegativeButton("Cancel",
	        new DialogInterface.OnClickListener() {
	       public void onClick(DialogInterface dialog, int whichButton) {
	     
	    	
	       }
	      }); 

	      final AlertDialog dialog = builder.create();
	      dialog.show();

      
}
	
/*private class ForgotPassword extends AsyncTask<String, Void, Void> {

	 boolean isNewCategoryCreated = false,err=false;
	String response;
	 @Override
	 protected void onPreExecute() {
		 super.onPreExecute();
		 pDialog = new ProgressDialog(LoginDetailActivity.this);
		 pDialog.setTitle("Sending Request");
		 pDialog.setMessage("Please Wait...");
		 pDialog.setCancelable(false);
		 pDialog.show();

	 }

	 @Override
	 protected Void doInBackground(String... arg) {
	
		 ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		 params.add(new BasicNameValuePair("action", "forgot"));
		 params.add(new BasicNameValuePair("username", forgot_pass_email));
		
		 ServiceHandler serviceClient = new ServiceHandler();
		 try{
			 String json = serviceClient.makeServiceCall(ServiceHandler.URL_FORGOT_PASSWORD,
					 ServiceHandler.POST, params);
			
			  if(!json.equals("error")){
                 JSONObject obj=new JSONObject(json);
			     response=obj.getString("msg");
                       
			 }
			 else
				 err=true;
		 }
		
		 catch(Exception e){
			 e.printStackTrace();
			 pDialog.dismiss();
			 err=true;
		 }
		 return null;
	 }

	 @Override 
	 protected void onPostExecute(Void result) {
		 super.onPostExecute(result);
		 if (pDialog.isShowing()){
			 pDialog.dismiss();
			 if(!err){
				 
				 if(response.equals("1")){
						finish();
						ConnectionDetector.showToast(getApplicationContext(), "Password Change Request Sent Successfully, Please check your mail.");
					}
					else if(response.equals("0")){
						ConnectionDetector.showToast(getApplicationContext(), "Please Enter valid email id");	
					}
				
			 }
			 else
				 Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
		 }
	 }

}*/

	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}	
}
