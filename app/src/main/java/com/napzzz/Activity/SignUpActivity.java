package com.napzzz.Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.napzzz.R;

public class SignUpActivity extends Activity implements OnClickListener {

	EditText emailEditText,passwordEditText,nameEditText,confirmPasswordEditText;
	TextView dobTextView;
	Button signupBtn;
	Spinner genderSpinner;
	ConnectionDetector cDetector;
	AlertDialogManager alert;
	//GCM PROJECT ID
    static final String SENDER_ID = "822012508209";
    Context mContext;
    String email,password,confirm_password,name; 
    private ProgressDialog pDialog;
    private String deviceID="", registrationId="", version="";
    SharedPreferences login_pref;
	public static final String LOGIN_PREF = "LoginPrefs" ;
	String gender;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		nameEditText=(EditText) findViewById(R.id.nameEditText);
		emailEditText=(EditText) findViewById(R.id.emailEditText);
		passwordEditText=(EditText) findViewById(R.id.passwordEditText);
		confirmPasswordEditText=(EditText) findViewById(R.id.confirmPasswordEditText);
		genderSpinner=(Spinner) findViewById(R.id.genderSpinner);
		dobTextView=(TextView) findViewById(R.id.dobTextView);
		signupBtn=(Button) findViewById(R.id.signupBtn);
		
		 mContext=(Context) getApplicationContext();
		 cDetector=new ConnectionDetector(getApplicationContext());
		 alert=new AlertDialogManager();
		 login_pref = this.getApplicationContext().getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE);
			
		 GCMRegistrar.checkDevice(getApplicationContext());
         GCMRegistrar.checkManifest(getApplicationContext());
		    registerGCMId();
		 	// Get GCM registration id
		    registrationId = GCMRegistrar.getRegistrationId(getApplicationContext());
		    if(registrationId.equals("") || registrationId.equals(null))
		 		 registerGCMId();
		 		 generateDeviceID();
			 
		    String zoneArr[]=new String[]{"Male","Female"};
		    ArrayAdapter<String> zoneAdp=new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_dropdown_layout,zoneArr);
		 	genderSpinner.setAdapter(zoneAdp);
		 		
		 		 
		signupBtn.setOnClickListener(this);
		dobTextView.setOnClickListener(this);
		
		 genderSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
             @Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					gender=parent.getItemAtPosition(position).toString();
					((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
			        ((TextView) parent.getChildAt(0)).setTextSize(16);
					
					}

				@Override
				public void onNothingSelected(AdapterView<?> adapter) {
					// TODO Auto-generated method stub

				}
			});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        switch(v.getId()){
		case R.id.signupBtn:
			
			loadSignUp();
			
			break;  
			
		case R.id.dobTextView:
			
			showDatePicker();
			
	break;
		} 
		return;
	}
	
	
	private void loadSignUp(){
		email=emailEditText.getText().toString();
		password=passwordEditText.getText().toString();
		confirm_password = confirmPasswordEditText.getText().toString();
		name = nameEditText.getText().toString();

		if(!cDetector.isConnectingToInternet())
			alert.showAlertDialog(SignUpActivity.this, "Network Connection", "Enable Your Internet Connection", false);
     	
		else if(name.equals("") || name==null)
		{
      	    nameEditText.setError("Please Enter Name");
		}
		else if(!LoginDetailActivity.isValidEmail(email))
		{
			emailEditText.setError("Invalid Email");
		}
		
		else if(password.equals("") || password==null)
		{
      	    passwordEditText.setError("Please Fill Password");
		}
		else if(confirm_password.equals("") || confirm_password==null){

			confirmPasswordEditText.setError("Please Enter Password");
		}
		else if(!confirm_password.equals(password))  // Change is here
        {
			confirmPasswordEditText.setError("Password Not Matched");
        }
		
		else{
			   
			//new userSignUp().execute();
		
		}
			
	}
	
/*	private class userSignUp extends AsyncTask<String, Void, Void> {

		boolean isNewCategoryCreated = false,err=false;
		String response,status,verifyCode;
		public JSONArray JsonArray ;
		 String user_id,full_name,user_email,dob,phone_number,gender;
		   
		
		 @Override
		 protected void onPreExecute() {
			 super.onPreExecute();
			 pDialog = new ProgressDialog(SignUpActivity.this);
			 pDialog.setTitle("Login");
			 pDialog.setMessage("Please Wait...");
			 pDialog.setCancelable(false);
			 pDialog.show();

			 if(registrationId.equals("")|| registrationId.equals(null)){
				 registrationId = GCMRegistrar.getRegistrationId(SignUpActivity.this);
				 registerGCMId();
			 }
		 }
		 @Override
		 protected Void doInBackground(String... arg) {

			 ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			 
			 params.add(new BasicNameValuePair("action", "register"));
			 params.add(new BasicNameValuePair("useremail", email)); // test@gmail.com
			 params.add(new BasicNameValuePair("your_name", name));
			 params.add(new BasicNameValuePair("password", password)); // 123456
		     params.add(new BasicNameValuePair("gender", gender));
			 params.add(new BasicNameValuePair("dateofbirth", "01-07-91"));
			 params.add(new BasicNameValuePair("device_id", deviceID));
			 params.add(new BasicNameValuePair("gcm_id", registrationId));
			 
			 ServiceHandler serviceClient = new ServiceHandler();
			 try{
				 String json = serviceClient.makeServiceCall(ServiceHandler.URL_USER_SIGNUP,
						 ServiceHandler.POST, params);
				
				  if(!json.equals("error")){

					  JSONObject obj=new JSONObject(json);
					  response=obj.getString("msg");
					  status=obj.getString("status");
				
				 if(response.equals("1")){
					 JSONObject json2 =obj.getJSONObject("data");
					 user_id=json2.getString("user_id");
				//	 full_name=json2.getString("full_name");
					 user_email=json2.getString("user_email");
				//	 user_pwd=json2.getString("user_pwd");
				//	 isVerified=json2.getString("isVerified");
					 verifyCode=json2.getString("verifyCode");
				//	 user_status=json2.getString("status");
				//	 dob=json2.getString("dob");
				//	 phone_number=json2.getString("phone_number");
				//	 language=json2.getString("language");
				//	 gender=json2.getString("gender");
				//	 completeaddress=json2.getString("completeaddress");
				//	 image=json2.getString("image");
				      }
				
				  }
				 else
					 err=true;
			 }
			 
			 catch(Exception e){
				 e.printStackTrace();
				 pDialog.dismiss();
			//	 Toast.makeText(mContext, "Something wrong with network, Please try later", Toast.LENGTH_SHORT).show();
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
							   
						        ConnectionDetector.showToast(getApplicationContext(), "Signup Successfully ");
						     // for saving values in shared prefrences....
						  		Editor editor1 = login_pref.edit();
						  		editor1.putString("has_login", "1");
						  		editor1.putString("user_id", user_id);
						  		editor1.putString("user_email", user_email);
						  		
						  		editor1.commit();
						  	}
						else if(response.equals("0")){
							    ConnectionDetector.showToast(getApplicationContext(), "User already exists, Please Login");
						}
						else{
							    ConnectionDetector.showToast(getApplicationContext(), "Please Fill Correct Credentials");	
						}
					
				      }
				        else
					            Toast.makeText(mContext, "Something wrong", Toast.LENGTH_SHORT).show();
			 }
		 }
   }*/

/*public void forgotPasswordDialog() {
		
		LayoutInflater li = LayoutInflater.from(SignUpActivity.this);
		LinearLayout voice = (LinearLayout)li.inflate(R.layout.activate_now_dialog, null);    
	    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
	  	    	 
	    		 // forgotEmailEditText.setError("Invalid Email");
	  		}
	    	   else{
	    		   new ForgotPassword().execute();
	    	   
	    	   }
	    	// Toast.makeText(getApplicationContext(), "Activate", Toast.LENGTH_SHORT).show();
	    	 }
	      }); 
	      builder.setNegativeButton("Cancel",
	        new DialogInterface.OnClickListener() {
	       public void onClick(DialogInterface dialog, int whichButton) {
	     
	    	
	       }
	      }); 

	      final AlertDialog dialog = builder.create();
	      dialog.show();

      
}*/
	
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
	
	@SuppressLint("NewApi")
	public void showDatePicker() {
		// how to convert dd-MMM-yyyy to yyyy-MM-dd in android
	    // Initializiation
	    LayoutInflater inflater = (LayoutInflater)SignUpActivity.this.getLayoutInflater();
	    final AlertDialog.Builder dialogBuilder = 
	    new AlertDialog.Builder(SignUpActivity.this);
	    View customView = inflater.inflate(R.layout.custom_view, null);
	    dialogBuilder.setView(customView);
	    final Calendar now = Calendar.getInstance();
	    final DatePicker datePicker = (DatePicker) customView.findViewById(R.id.dialog_datepicker);
	    /*  final TextView dateTextView = 
	        (TextView) customView.findViewById(R.id.dialog_dateview);*/
	    /*  final SimpleDateFormat dateViewFormatter = 
	        new SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.ENGLISH);
	        final SimpleDateFormat formatter = 
	        new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);*/
	    
	    final SimpleDateFormat dateViewFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);//yyyy-MM-dd
	    final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
	   
	    Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df2.format(c.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("HH : mm : ss");
	    String strTime = sdf.format(new Date());
	    
	    // Minimum date
	    Calendar minDate = Calendar.getInstance();
	    // Minimum date
	    Calendar maxDate = Calendar.getInstance();
	    try {
	        minDate.setTime(formatter.parse("12.12.1970"));
	        maxDate.setTime(formatter.parse(formattedDate));
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    datePicker.setMinDate(minDate.getTimeInMillis());
	    datePicker.setMaxDate(maxDate.getTimeInMillis());
	 //  ConnectionDetector.showToast(getActivity(), "Selected: "+ minDate);
	    // View settings
	 //   dialogBuilder.setTitle("Select a date");
	    Calendar choosenDate = Calendar.getInstance();
	    int year = choosenDate.get(Calendar.YEAR);
	    int month = choosenDate.get(Calendar.MONTH);
	    int day = choosenDate.get(Calendar.DAY_OF_MONTH);
	    try {
	        Date choosenDateFromUI = formatter.parse(
	        		dobTextView.getText().toString()
	        );
	        choosenDate.setTime(choosenDateFromUI);
	        year = choosenDate.get(Calendar.YEAR);
	        month = choosenDate.get(Calendar.MONTH);
	        day = choosenDate.get(Calendar.DAY_OF_MONTH);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    Calendar dateToDisplay = Calendar.getInstance();
	   /* dateToDisplay.set(year, month, day);*/
	  // comments by pramod     ------------------------------------- dateToDisplay.set(0,month);
	    /*dateTextView.setText(
	        dateViewFormatter.format(dateToDisplay.getTime())
	    );*/
	    // Buttons
	    dialogBuilder.setNegativeButton(
	        "Cancel", 
	        new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	//selectDateTextview.setText(
	               //     formatter.format(now.getTime())
	              //  );
	                dialog.dismiss();
	            }
	        }
	    );
	    dialogBuilder.setPositiveButton(
	        "Ok", 
	        new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                Calendar choosen = Calendar.getInstance();
	                choosen.set(
	                    datePicker.getYear(), 
	                    datePicker.getMonth(), 
	                    datePicker.getDayOfMonth()
	                );
	               
	                dobTextView.setText(
	                    dateViewFormatter.format(choosen.getTime())
	                );
	             /*  finalDate = dateViewFormatter.format(choosen.getTime());
	        
	               convertDate(finalDate);*/
	              
	                dialog.dismiss();
	            }
	        }
	    );
	    final AlertDialog dialog = dialogBuilder.create();
	    // Initialize datepicker in dialog atepicker
	    datePicker.init(
	        year, 
	        month, 
	        day, 
	        new DatePicker.OnDateChangedListener() {
	            public void onDateChanged(DatePicker view, int year, 
	                int monthOfYear, int dayOfMonth) {
	                Calendar choosenDate = Calendar.getInstance();
	                choosenDate.set(year, monthOfYear, dayOfMonth);
	            
	            }
	        }
	    );
	    // Finish
	    dialog.show();
	}

}
