package com.napzzz.NetverifyId;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.jumio.mobile.sdk.PlatformNotSupportedException;
import com.jumio.mobile.sdk.ResourceNotFoundException;
import com.jumio.mobile.sdk.enums.JumioDataCenter;
import com.jumio.netswipe.sdk.NetswipeCardInformation;
import com.jumio.netswipe.sdk.NetswipeCustomScanInterface;
import com.jumio.netswipe.sdk.NetswipeCustomScanPresenter;
import com.jumio.netswipe.sdk.NetswipeCustomScanView;
import com.jumio.netswipe.sdk.NetswipeSDK;
import com.jumio.netverify.barcode.JumioBarcodeScanner;
import com.jumio.netverify.barcode.JumioBarcodeScannerInterface;
import com.jumio.netverify.barcode.enums.BarcodeFormat;
import com.jumio.netverify.sdk.NetverifyDocumentData;
import com.jumio.netverify.sdk.NetverifyMrzData;
import com.jumio.netverify.sdk.NetverifySDK;
import com.napzzz.R;

import java.util.ArrayList;

/**
 * sample activity using the JumioSDK
 */
public class SampleActivity extends Activity implements NetswipeCustomScanInterface {

    /* PUT YOUR NETVERIFY API TOKEN AND SECRET HERE */
    private static String NETVERIFY_API_TOKEN = "";
    private static String NETVERIFY_API_SECRET = "";

    /* PUT YOUR NETSWIPE API TOKEN AND SECRET HERE */
    private static String NETSWIPE_API_TOKEN = "";
    private static String NETSWIPE_API_SECRET = "";

    private final static String TAG = "JumioMobileSDKSample";

    private Button startNetswipeButton;
    private Button startNetswipeCustomButton;
    private Button stopNetswipeCustomButton;
    private Button startNetverifyButton;
	private Button startNetverifyCustomButton;
	private Button stopNetverifyCustomButton;
    private Button startBarcodeScannerButton;

    private ImageView switchCameraImageView;
    private ImageView toggleFlashImageView;
    private RelativeLayout netswipeCustomContainer;
	private ScrollView netverifyCustomContainer;


    private NetverifySDK netverifySDK;
    private NetswipeSDK netswipeSDK;
    private JumioBarcodeScanner barcodeScanner;
    private NetswipeCustomScanPresenter customScanPresenter;
	private NetverifyCustomWrapper netverifyCustomWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.netverify_main);

        startNetswipeButton = (Button) findViewById(R.id.startNetswipeButton);
        startNetswipeButton.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View view) {

		        //since the NetswipeSDK is a singleton internally, a new instance is not
		        //created here

		        initializeNetswipeSDK();
		        netswipeSDK.start();
	        }
        });

        startNetswipeCustomButton = (Button) findViewById(R.id.startNetswipeCustomButton);
        startNetswipeCustomButton.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View view) {

		        //since the NetswipeSDK is a singleton internally, a new instance is not
		        //created here

		        startNetswipeButton.setVisibility(View.GONE);
		        startNetswipeCustomButton.setVisibility(View.GONE);
		        startNetverifyButton.setVisibility(View.GONE);
		        startNetverifyCustomButton.setVisibility(View.GONE);
		        startBarcodeScannerButton.setVisibility(View.GONE);
		        netswipeCustomContainer.setVisibility(View.VISIBLE);

		        initializeNetswipeSDK();

		        customScanPresenter = netswipeSDK.start(SampleActivity.this, (NetswipeCustomScanView) findViewById(R.id.netswipeCustomScanView));
	        }
        });

        stopNetswipeCustomButton = (Button) findViewById(R.id.stopNetswipeCustomButton);
        stopNetswipeCustomButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //do not just re-instantiate the SDK here because fast subsequent taps on the button
                //can cause two SDK instances to be created, which will result in undefined (and
                //most likely incorrect) behaviour. A suitable place for the re-instantiation of the SDK
                //would be onCreate()

                customScanPresenter.stopScan();
                customScanPresenter.clearSDK();
                netswipeCustomContainer.setVisibility(View.GONE);

                startNetswipeButton.setVisibility(View.VISIBLE);
                startNetswipeCustomButton.setVisibility(View.VISIBLE);
                startNetverifyButton.setVisibility(View.VISIBLE);
	            startNetverifyCustomButton.setVisibility(View.VISIBLE);
                startBarcodeScannerButton.setVisibility(View.VISIBLE);

            }
        });

        startNetverifyButton = (Button) findViewById(R.id.startNetverifyButton);
        startNetverifyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //since the NetverifySDK is a singleton internally, a new instance is not
                //created here
                initializeNetverifySDK();
                netverifySDK.start();
            }
        });

	    startNetverifyCustomButton = (Button) findViewById(R.id.startNetverifyCustomButton);
	    startNetverifyCustomButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View view) {

			    //since the NetverifySDK is a singleton internally, a new instance is not
			    //created here


			    startNetswipeButton.setVisibility(View.GONE);
			    startNetswipeCustomButton.setVisibility(View.GONE);
			    startNetverifyButton.setVisibility(View.GONE);
			    startNetverifyCustomButton.setVisibility(View.GONE);
			    startBarcodeScannerButton.setVisibility(View.GONE);
			    netverifyCustomContainer.setVisibility(View.VISIBLE);

			    initializeNetverifySDK();
			    netverifyCustomWrapper = new NetverifyCustomWrapper(SampleActivity.this, netverifySDK);
			    netverifyCustomWrapper.start(netverifyCustomContainer);
		    }
	    });



	    stopNetverifyCustomButton = (Button) findViewById(R.id.stopNetverifyCustomButton);
	    stopNetverifyCustomButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    stopNetverifyCustom();
		    }
	    });

        startBarcodeScannerButton = (Button) findViewById(R.id.startBarcodeScannerButton);
        startBarcodeScannerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //since the BarcodeScanner is a singleton internally, a new instance is
                //not created here
                initializeBarcodeScanner();
                barcodeScanner.start();
            }
        });

        netswipeCustomContainer = (RelativeLayout) findViewById(R.id.netswipeCustomContainer);
	    netverifyCustomContainer = (ScrollView) findViewById(R.id.netverifyCustomContainer);

        switchCameraImageView = (ImageView) findViewById(R.id.switchCameraImageView);
        switchCameraImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                if (customScanPresenter != null && customScanPresenter.hasMultipleCameras())
                    customScanPresenter.switchCamera();

            }
        });

        toggleFlashImageView = (ImageView) findViewById(R.id.toggleFlashImageView);
        toggleFlashImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                if (customScanPresenter != null && customScanPresenter.hasFlash()) {
                    customScanPresenter.toggleFlash();
                    v.setEnabled(true);
                }
                toggleFlashImageView.setImageResource(customScanPresenter.isFlashOn() ? R.drawable.ic_flash_off : R.drawable.ic_flash_on);
            }
        });

        initializeNetswipeSDK();
        initializeNetverifySDK();
        initializeBarcodeScanner();

    }

    @Override
    public void onPause() {
        if (customScanPresenter != null)
            customScanPresenter.onActivityPause();
        super.onPause();
    }

	public void stopNetverifyCustom() {
		netverifyCustomWrapper.stop();

		startNetswipeButton.setVisibility(View.VISIBLE);
		startNetswipeCustomButton.setVisibility(View.VISIBLE);
		startNetverifyButton.setVisibility(View.VISIBLE);
		startNetverifyCustomButton.setVisibility(View.VISIBLE);
		startBarcodeScannerButton.setVisibility(View.VISIBLE);
		netverifyCustomContainer.setVisibility(View.GONE);
	}

    private void initializeNetswipeSDK() {
        try {
            // You can get the current SDK version using the method below.
            // NetswipeSDK.getSDKVersion();

            // Call the method isSupportedPlatform to check if the device is supported (camera available,
            // ARMv7 processor with Neon, Android 4.0 or higher).
            // NetswipeSDK.isSupportedPlatform(this);

            // Applications implementing the SDK shall not run on rooted devices. Use either the below
            // method or a self-devised check to prevent usage of SDK scanning functionality on rooted
            // devices.
            if (NetswipeSDK.isRooted())
                Log.w(TAG, "Device is rooted");

            // To create an instance of the SDK, perform the following call as soon as your activity is initialized.
            // Make sure that your merchant API token and API secret are correct, specify an instance
            // of your activity and provide a reference to identify the scans in your reports (max. 100
            // characters or null). If your merchant account is created in the EU data center, use
	        // JumioDataCenter.EU instead.
            netswipeSDK = NetswipeSDK.create(SampleActivity.this, NETSWIPE_API_TOKEN, NETSWIPE_API_SECRET, "YOURREPORTINGCRITERIA", JumioDataCenter.US);

            // Overwrite your specified reporting criteria to identify each scan attempt in your reports (max. 100 characters).
            // netswipeSDK.setMerchantReportingCriteria("YOURREPORTINGCRITERIA");

            // To restrict supported card types, pass an ArrayList of CreditCardTypes to the setSupportedCreditCardTypes method.
            // ArrayList<CreditCardType> creditCardTypes = new ArrayList<CreditCardType>();
            // creditCardTypes.add(CreditCardType.VISA);
            // creditCardTypes.add(CreditCardType.MASTER_CARD);
            // creditCardTypes.add(CreditCardType.AMERICAN_EXPRESS);
            // creditCardTypes.add(CreditCardType.DINERS_CLUB);
            // creditCardTypes.add(CreditCardType.DISCOVER);
            // creditCardTypes.add(CreditCardType.CHINA_UNIONPAY);
            // creditCardTypes.add(CreditCardType.JCB);
            // creditCardTypes.add(CreditCardType.PRIVATE_LABEL);
            // netswipeSDK.setSupportedCreditCardTypes(creditCardTypes);

            // You can enable the recognition of card holder name, sort code and account number.
            // Manual entry, expiry recognition and CVV entry are enabled by default and can be disabled.
	        // The user can edit the recognized expiry date if setExpiryEditable is enabled.
            // netswipeSDK.setCardHolderNameRequired(true);
            // netswipeSDK.setSortCodeAndAccountNumberRequired(true);
            // netswipeSDK.setManualEntryEnabled(false);
            // netswipeSDK.setExpiryRequired(false);
            // netswipeSDK.setCvvRequired(false);
	        // netswipeSDK.setExpiryEditable(true);

            // Use setName to pass first and last name for name match if card holder recognition is enabled.
            // The user can edit the recognized card holder name if setCardHolderNameEditable is enabled.
            // netswipeSDK.setName("FIRSTNAME LASTNAME");
            // netswipeSDK.setCardHolderNameEditable(true);

            // You can set a short vibration and sound effect to notify the user that the card has been detected.
            // netswipeSDK.setVibrationEffectEnabled(true);
            // netswipeSDK.setSoundEffect(R.raw.shutter_sound);

            // To show the unmasked card number during the user journey, disable the following setting.
            // netswipeSDK.setCardNumberMaskingEnabled(false);

	        // Set the default camera position
	        // netswipeSDK.setCameraPosition(JumioCameraPosition.FRONT);

	        // Automatically enable flash when scan is started
	        // netswipeSDK.setEnableFlashOnScanStart(true);

            // You can add additional fields to "Manual entry" and the confirmation page.(keyboard entry or predefined values).
            // netswipeSDK.addCustomField("zipCodeId", getString(R.string.zip_code), getString(R.string.zip_code_hint), InputType.TYPE_CLASS_NUMBER, "[0-9]{5,}");
            // ArrayList<String> states = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.state_selection_values)));
            // netswipeSDK.addCustomField("stateId", getString(R.string.state), getString(R.string.state_hint), states, false, getString(R.string.state_reset_value));

        } catch (PlatformNotSupportedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "This platform is not supported", Toast.LENGTH_LONG).show();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Resource files are missing", Toast.LENGTH_LONG).show();
        }
    }

    private void initializeNetverifySDK() {
        try {
            // You can get the current SDK version using the method below.
            // NetverifySDK.getSDKVersion();

            // Call the method isSupportedPlatform to check if the device is supported (camera available,
            // ARMv7 processor with Neon, Android 4.0 or higher).
            // NetverifySDK.isSupportedPlatform(this);

            // To create an instance of the SDK, perform the following call as soon as your activity is initialized.
            // Make sure that your merchant API token and API secret are correct and specify an instance
            // of your activity. If your merchant account is created in the EU data center, use
	        // JumioDataCenter.EU instead.
            netverifySDK = NetverifySDK.create(SampleActivity.this, NETVERIFY_API_TOKEN, NETVERIFY_API_SECRET, JumioDataCenter.US);

            // You can specify issuing country (ISO 3166-1 alpha-3 country code) and/or ID type and/or document variant to skip
            // their selection during the scanning process.
            // netverifySDK.setPreselectedCountry("AUT");
            // netverifySDK.setPreselectedDocumentType(NVDocumentType.PASSPORT);
	        // netverifySDK.setPreselectedDocumentVariant(NVDocumentVariant.PLASTIC);

			// Enable data extraction from visa documents
			// Note: Only possible for accounts configured as Fastfill.
	        // netverifySDK.setEnableVisa(true);

	        // To hide the country flag on the info bar, disable the following setting.
	        // netverifySDK.setShowFlagOnInfoBar(false);

            // The merchant scan reference allows you to identify the scan (max. 100 characters).
            // Note: Must not contain sensitive data like PII (Personally Identifiable Information) or account login.
            // netverifySDK.setMerchantScanReference("YOURSCANREFERENCE");

            // Use the following property to identify the scan in your reports (max. 100 characters).
            // netverifySDK.setMerchantReportingCriteria("YOURREPORTINGCRITERIA");

            // You can also set a customer identifier (max. 100 characters).
            // Note: The customer ID should not contain sensitive data like PII (Personally Identifiable Information) or account login.
            // netverifySDK.setCustomerId("CUSTOMERID");

            // Use the following method to pass first and last name to Fastfill for name match.
            // netverifySDK.setName("FIRSTNAME LASTNAME");

            // Enable ID verification to receive a verification status and verified data positions (see Callback chapter).
            // Note: Not possible for accounts configured as Fastfill only.
            // netverifySDK.setRequireVerification(true);

            // You can enable face match during the ID verification for a specific transaction. This setting overrides your default Jumio merchant settings.
            // netverifySDK.setRequireFaceMatch(true);

            // Use the following method to pass first and last name to the ID verification for name match (each max. 100 characters).
            // netverifySDK.setFirstAndLastName("FIRSTNAME", "LASTNAME");

	        // Set the default camera position
	        // netverifySDK.setCameraPosition(JumioCameraPosition.FRONT);

	        // Callback URL for the confirmation after the verification is completed. This setting overrides your Jumio merchant settings.
	        // netverifySDK.setCallbackUrl("YOURCALLBACKURL");

        } catch (PlatformNotSupportedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "This platform is not supported", Toast.LENGTH_LONG).show();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Resource files are missing", Toast.LENGTH_LONG).show();
        }
    }

    private void initializeBarcodeScanner() {
        try {
            // You can get the current SDK version using the method below.
            // JumioBarcodeScanner.getSDKVersion();

            // Call the method isSupportedPlatform to check if the device is supported (camera available,
            // ARMv7 processor with Neon, Android 4.0 or higher).
            // JumioBarcodeScanner.isSupportedPlatform(this);

            // Make sure that your API credentials are correct, specify an instance of your activity and
            // provide an instance of your class which implements the JumioBarcodeScannerInterface to
            // receive the initial, successful onLoadingComplete notification (can be null).  If your merchant account
            // is created in the EU data center, use JumioDataCenter.EU instead.
            JumioBarcodeScannerInterface scannerInterface = null;
            barcodeScanner = JumioBarcodeScanner.create(SampleActivity.this, NETVERIFY_API_TOKEN, NETVERIFY_API_SECRET, scannerInterface, JumioDataCenter.US);

            // To check if the bar code scanner has been loaded previously, you can use the below method.
            // barcodeScanner.hasLoaded();

            // Set one of the following bar code formats: CODE39, CODE128, EAN8, EAN13, ITF, QR, PDF417, UPCA or UPCE.
            barcodeScanner.setBarcodeFormat(BarcodeFormat.PDF417);

        } catch (PlatformNotSupportedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "This platform is not supported", Toast.LENGTH_LONG).show();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Resource files are missing", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NetverifySDK.REQUEST_CODE) {
            if (data == null)
                return;
            if (resultCode == NetverifySDK.RESULT_CODE_SUCCESS || resultCode == NetverifySDK.RESULT_CODE_BACK_WITH_SUCCESS) {
                String scanReference = (data == null) ? "" : data.getStringExtra(NetverifySDK.RESULT_DATA_SCAN_REFERENCE);
                NetverifyDocumentData documentData = (data == null) ? null : (NetverifyDocumentData) data.getParcelableExtra(NetverifySDK.RESULT_DATA_SCAN_DATA);
                NetverifyMrzData mrzData = documentData != null ? documentData.getMrzData() : null;
            } else if (resultCode == NetverifySDK.RESULT_CODE_CANCEL) {
                String errorMessage = data.getStringExtra(NetverifySDK.RESULT_DATA_ERROR_MESSAGE);
                int errorCode = data.getIntExtra(NetverifySDK.RESULT_DATA_ERROR_CODE, 0);
            }

            //at this point, the SDK is not needed anymore. It is highly advisable to call destroy(), so that
            //internal resources can be freed
            if (netverifySDK != null) {
                netverifySDK.destroy();
                netverifySDK = null;
            }

        } else if (requestCode == NetswipeSDK.REQUEST_CODE) {
            if (data == null)
                return;
            ArrayList<String> scanAttempts = data.getStringArrayListExtra(NetswipeSDK.EXTRA_SCAN_ATTEMPTS);

            if (resultCode == Activity.RESULT_OK) {
                NetswipeCardInformation cardInformation = data.getParcelableExtra(NetswipeSDK.EXTRA_CARD_INFORMATION);

                cardInformation.clear();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                String errorMessage = data.getStringExtra(NetswipeSDK.EXTRA_ERROR_MESSAGE);
                int errorCode = data.getIntExtra(NetswipeSDK.EXTRA_ERROR_CODE, 0);
            }

            //at this point, the SDK is not needed anymore. It is highly advisable to call destroy(), so that
            //internal resources can be freed
            if (netswipeSDK != null) {
                netswipeSDK.destroy();
                netswipeSDK = null;
            }
        } else if (requestCode == JumioBarcodeScanner.REQUEST_CODE) {
            if (data == null)
                return;
            if (resultCode == JumioBarcodeScanner.RESULT_CODE_SUCCESS) {
                BarcodeFormat barcodeFormat = (BarcodeFormat) data.getSerializableExtra(JumioBarcodeScanner.RESULT_BARCODE_FORMAT);
                String rawData = data.getStringExtra(JumioBarcodeScanner.RESULT_BARCODE_DATA);
            } else if (resultCode == NetverifySDK.RESULT_CODE_CANCEL) {
                String errorMessage = data.getStringExtra(NetverifySDK.RESULT_DATA_ERROR_MESSAGE);
                int errorCode = data.getIntExtra(NetverifySDK.RESULT_DATA_ERROR_CODE, 0);
            }

            //at this point, the SDK is not needed anymore. It is highly advisable to call destroy(), so that
            //internal resources can be freed
            if (barcodeScanner != null) {
                barcodeScanner.destroy();
                barcodeScanner = null;
            }
        }
    }

    /*
    Called as soon as the camera is available for the custom scan. It is safe to check for flash and additional cameras here
     */
    @Override
    public void onNetswipeCameraAvailable() {
        Log.d("NetswipeCustomScan", "camera available");
        switchCameraImageView.setVisibility(customScanPresenter.hasMultipleCameras() ? View.VISIBLE : View.INVISIBLE);
        switchCameraImageView.setImageResource(customScanPresenter.isCameraFrontFacing() ? R.drawable.ic_camera_rear : R.drawable.ic_camera_front);
        toggleFlashImageView.setVisibility(customScanPresenter.hasFlash() ? View.VISIBLE : View.INVISIBLE);
	    toggleFlashImageView.setImageResource(customScanPresenter.isFlashOn() ? R.drawable.ic_flash_off : R.drawable.ic_flash_on);
    }

    @Override
    public void onNetswipeError(int errorCode, String errorMessage, boolean retryPossible, ArrayList<String> scanAttempts) {
        Log.d("NetswipeCustomScan", "error occured");
	    //Do not show error dialog when it's a error because of background execution not supported
	    if(errorCode == 310)
		    return;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Scan error");
        alertDialogBuilder.setMessage(errorMessage);
        if (retryPossible) {
            alertDialogBuilder.setPositiveButton("retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        customScanPresenter.retryScan();
                    } catch (UnsupportedOperationException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopNetswipeCustomButton.performClick();
            }
        });
        alertDialogBuilder.show();

    }

    /*
    When extraction is started, the preview screen will be paused. A loading indicator can be displayed within this callback
     */
    @Override
    public void onNetswipeExtractionStarted() {
        Log.d("NetswipeCustomScan", "extraction started");
    }

    /*
     */
    @Override
    public void onNetswipeExtractionFinished(NetswipeCardInformation netswipeCardInformation, ArrayList<String> scanAttempts) {
        Log.d("NetswipeCustomScan", "extraction finished");
        netswipeCardInformation.clear();
    }
}
