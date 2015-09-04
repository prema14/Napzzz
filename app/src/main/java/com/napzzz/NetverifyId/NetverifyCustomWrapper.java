package com.napzzz.NetverifyId;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jumio.netverify.sdk.NetverifyDocumentData;
import com.jumio.netverify.sdk.NetverifyMrzData;
import com.jumio.netverify.sdk.NetverifySDK;
import com.jumio.netverify.sdk.custom.NetverifyCountry;
import com.jumio.netverify.sdk.custom.NetverifyCustomConfirmationView;
import com.jumio.netverify.sdk.custom.NetverifyCustomSDKController;
import com.jumio.netverify.sdk.custom.NetverifyCustomSDKInterface;
import com.jumio.netverify.sdk.custom.NetverifyCustomScanView;
import com.jumio.netverify.sdk.custom.NetverifyCustomScanViewController;
import com.jumio.netverify.sdk.custom.NetverifyCustomScanViewInterface;
import com.jumio.netverify.sdk.custom.NetverifyDocumentPart;
import com.jumio.netverify.sdk.custom.SDKNotConfiguredException;
import com.jumio.netverify.sdk.enums.NVDocumentType;
import com.jumio.netverify.sdk.enums.NVDocumentVariant;
import com.jumio.netverify.sdk.enums.NVScanSide;
import com.napzzz.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NetverifyCustomWrapper implements NetverifyCustomSDKInterface, NetverifyCustomScanViewInterface {
	private LinearLayout countryDocumentLayout;
	private LinearLayout partTypeLayout;
	private LinearLayout inlineCallbackLog;
	private RelativeLayout inlineScanLayout;
	private RelativeLayout inlineConfirmLayout;
	private RelativeLayout noUSAddressLayout;
	private NetverifyCustomScanView customScanView;
	private NetverifyCustomConfirmationView customConfirmationView;
	private ProgressBar loadingIndicator;
	private Spinner customCountrySpinner;
	private Spinner customDocumentSpinner;
	private Spinner customVariantSpinner;
	private SeekBar ratioSeekBar;
	private TextView ratioTextView;
	private Button setCountryAndDocumentTypeButton;
	private Button frontSideButton;
	private Button backSideButton;
	private Button faceButton;
	private Button stopScan;
	private Switch extraction;
	private Button startFallback;
	private Button switchCamera;
	private Button takePicture;
	private Button toggleFlash;
	private Button retryScan;
	private Button confirmScan;
	private Button retakeAddress;
	private Button confirmAddress;
	private Button finishButton;
	private ImageView frontSideImageView;
	private ImageView backSideImageView;
	private ImageView faceImageView;

	private NetverifyCustomSDKController customSDKController;
	private NetverifyCustomScanViewController customScanViewController;

	private CustomCountryAdapter customCountryAdapter;
	private CustomDocumentAdapter customDocumentAdapter;
	private CustomVariantAdapter customVariantAdapter;


	private HashMap<NVScanSide, NetverifyDocumentPart> documentParts;

	private Drawable successDrawable;
	private Drawable errorDrawable;

	private SampleActivity activity;
	private NetverifySDK sdk;

	public NetverifyCustomWrapper(SampleActivity activity, NetverifySDK sdk) {
		this.activity = activity;
		this.sdk = sdk;
	}

	public void start(View rootView) {
		initGUI(rootView);
		showView(false, loadingIndicator);
		inlineCallbackLog.removeAllViews();
		customSDKController = sdk.start(NetverifyCustomWrapper.this);
	}

	public void stop() {
		if(isSDKControllerValid()){
			hideView(false, countryDocumentLayout, partTypeLayout, inlineScanLayout, inlineConfirmLayout, noUSAddressLayout, finishButton, loadingIndicator);
			inlineCallbackLog.removeAllViews();
			customSDKController.clearSDK();
			sdk.destroy();
			customSDKController = null;
		}
	}

	public void notifyConfigurationChange() {
		initScanView();
	}

	private View.OnClickListener customClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			v.setEnabled(false);
			boolean keepDisabled = false;
			if(v == setCountryAndDocumentTypeButton && isSDKControllerValid()) {
				NetverifyCountry country = customCountryAdapter.getCountryObject(customCountrySpinner.getSelectedItemPosition());
				NVDocumentType documentType = customDocumentAdapter.getDocumentType(customDocumentSpinner.getSelectedItemPosition());
				NVDocumentVariant documentVariant = customVariantAdapter.getDocumentVariant(customVariantSpinner.getSelectedItemPosition());
				frontSideButton.setVisibility(View.GONE);
				frontSideButton.setCompoundDrawablesWithIntrinsicBounds(errorDrawable, null, null, null);
				frontSideButton.setEnabled(false);
				frontSideImageView.setVisibility(View.GONE);
				backSideButton.setVisibility(View.GONE);
				backSideButton.setCompoundDrawablesWithIntrinsicBounds(errorDrawable, null, null, null);
				backSideButton.setEnabled(false);
				backSideImageView.setVisibility(View.GONE);
				faceButton.setVisibility(View.GONE);
				faceButton.setCompoundDrawablesWithIntrinsicBounds(errorDrawable, null, null, null);
				faceButton.setEnabled(false);
				faceButton.setVisibility(View.GONE);

				List<NetverifyDocumentPart> parts = null;
				try {
					parts = customSDKController.setDocumentConfiguration(country, documentType, documentVariant);
					documentParts = new HashMap<NVScanSide, NetverifyDocumentPart>(parts.size());

					for(NetverifyDocumentPart part: parts){
						documentParts.put(part.getScanSide(), part);
						switch (part.getScanSide()) {
							case FRONT:
								frontSideButton.setVisibility(View.VISIBLE);
								break;
							case BACK:
								backSideButton.setVisibility(View.VISIBLE);
								break;
							case FACE:
								faceButton.setVisibility(View.VISIBLE);
								break;
						}
					}
					showView(true, partTypeLayout);
				} catch (SDKNotConfiguredException e) {
					e.printStackTrace();
				}
			} else if((v == frontSideButton || v == backSideButton || v == faceButton) && isSDKControllerValid()) {
				try {
					NVScanSide scanSide = NVScanSide.FRONT;
					if(v == backSideButton)
						scanSide = NVScanSide.BACK;
					else if(v == faceButton)
						scanSide = NVScanSide.FACE;
					switchCamera.setEnabled(false);
					takePicture.setEnabled(false);
					toggleFlash.setEnabled(false);
					startFallback.setEnabled(false);
					extraction.setChecked(true);
					showView(true, inlineScanLayout);
					customScanViewController = customSDKController.startScanForPart(documentParts.get(scanSide), customScanView, customConfirmationView, NetverifyCustomWrapper.this);
					startFallback.setEnabled(customScanViewController.isFallbackAvailable());

				} catch (SDKNotConfiguredException e) {
					addToCallbackLog(e.getMessage());
				}
			} else if(v == stopScan && isScanViewControllerValid()) {
				customScanViewController.stopScan();
				hideView(false, inlineScanLayout);
				customScanViewController.destroy();
			} else if(v == extraction && isScanViewControllerValid()) {
				if(extraction.isChecked())
					customScanViewController.resumeExtraction();
				else
					customScanViewController.pauseExtraction();
			} else if(v == startFallback && isScanViewControllerValid()) {
				if(customScanViewController.isFallbackAvailable()) {
					customScanViewController.startFallback();
					keepDisabled = true;
				}
			} else if(v == switchCamera && isScanViewControllerValid()) {
				if (customScanViewController.hasMultipleCameras())
					customScanViewController.switchCamera();
			} else if(v == takePicture && isScanViewControllerValid()) {
				if(customScanViewController.showShutterButton())
					customScanViewController.takePicture();
			} else if(v == toggleFlash && isScanViewControllerValid()) {
				if(customScanViewController.hasFlash())
					customScanViewController.toggleFlash();
			} else if(v == retryScan && isScanViewControllerValid()) {
				hideView(false, inlineConfirmLayout);
				showView(false, inlineScanLayout);
				try {
					customSDKController.retry();
				} catch (SDKNotConfiguredException e) {
					addToCallbackLog(e.getMessage());
				}
			} else if(v == confirmScan && isScanViewControllerValid()) {
				hideView(true, inlineConfirmLayout);
				customScanViewController.confirmScan();
			} else if(v == retakeAddress && isScanViewControllerValid()) {
				hideView(false, noUSAddressLayout);
				customScanViewController.startUSAddressFallback();
			} else if(v == confirmAddress && isScanViewControllerValid()) {
				hideView(true, noUSAddressLayout);
				customScanViewController.confirmScan();
			} else if(v == finishButton && isSDKControllerValid()) {
				try {
					showView(false, loadingIndicator);
					customSDKController.finish();
					keepDisabled = true;
				} catch (SDKNotConfiguredException e) {
					addToCallbackLog(e.getMessage());
				}
			}

			if(!keepDisabled)
				v.setEnabled(true);
		}
	};

	private boolean isSDKControllerValid() {
		return customSDKController != null;
	}

	private boolean isScanViewControllerValid() {
		return customScanViewController != null;
	}

	private void initGUI(View rootView) {
		countryDocumentLayout = (LinearLayout) rootView.findViewById(R.id.countryDocumentLayout);
		partTypeLayout = (LinearLayout) rootView.findViewById(R.id.partTypeLayout);
		inlineCallbackLog = (LinearLayout) rootView.findViewById(R.id.callbackLog);
		inlineScanLayout = (RelativeLayout) rootView.findViewById(R.id.netverifyCustomScanLayout);
		inlineConfirmLayout = (RelativeLayout) rootView.findViewById(R.id.netverifyCustomConfirmLayout);
		noUSAddressLayout = (RelativeLayout) rootView.findViewById(R.id.noUSAddressLayout);
		customScanView = (NetverifyCustomScanView) rootView.findViewById(R.id.netverifyCustomScanView);
		customConfirmationView = (NetverifyCustomConfirmationView) rootView.findViewById(R.id.netverifyCustomConfirmationView);
		customCountrySpinner = (Spinner) rootView.findViewById(R.id.customCountrySpinner);
		customDocumentSpinner = (Spinner) rootView.findViewById(R.id.customDocumentSpinner);
		customVariantSpinner = (Spinner) rootView.findViewById(R.id.customVariantSpinner);
		loadingIndicator = (ProgressBar) rootView.findViewById(R.id.loadingIndicator);
		ratioSeekBar = (SeekBar) rootView.findViewById(R.id.ratioSeekBar);
		ratioTextView = (TextView) rootView.findViewById(R.id.ratioTextView);

		setCountryAndDocumentTypeButton = (Button) rootView.findViewById(R.id.setDocumentConfiguration);
		setCountryAndDocumentTypeButton.setOnClickListener(customClickListener);
		frontSideButton = (Button) rootView.findViewById(R.id.frontSideButton);
		frontSideButton.setOnClickListener(customClickListener);
		backSideButton = (Button) rootView.findViewById(R.id.backSideButton);
		backSideButton.setOnClickListener(customClickListener);
		faceButton = (Button) rootView.findViewById(R.id.faceButton);
		faceButton.setOnClickListener(customClickListener);
		stopScan = (Button) rootView.findViewById(R.id.stopScan);
		stopScan.setOnClickListener(customClickListener);
		extraction = (Switch) rootView.findViewById(R.id.extraction);
		extraction.setOnClickListener(customClickListener);
		startFallback = (Button) rootView.findViewById(R.id.startFallback);
		startFallback.setOnClickListener(customClickListener);
		switchCamera = (Button) rootView.findViewById(R.id.switchCamera);
		switchCamera.setOnClickListener(customClickListener);
		takePicture = (Button) rootView.findViewById(R.id.takePicture);
		takePicture.setOnClickListener(customClickListener);
		toggleFlash = (Button) rootView.findViewById(R.id.toggleFlash);
		toggleFlash.setOnClickListener(customClickListener);
		retryScan = (Button) rootView.findViewById(R.id.retryScan);
		retryScan.setOnClickListener(customClickListener);
		confirmScan = (Button) rootView.findViewById(R.id.confirmScan);
		confirmScan.setOnClickListener(customClickListener);
		retakeAddress = (Button) rootView.findViewById(R.id.retakeAddress);
		retakeAddress.setOnClickListener(customClickListener);
		confirmAddress = (Button) rootView.findViewById(R.id.confirmAddress);
		confirmAddress.setOnClickListener(customClickListener);
		finishButton = (Button) rootView.findViewById(R.id.finishButton);
		finishButton.setOnClickListener(customClickListener);
		frontSideImageView = (ImageView) rootView.findViewById(R.id.frontSideImageView);
		backSideImageView = (ImageView) rootView.findViewById(R.id.backSideImageView);
		faceImageView = (ImageView) rootView.findViewById(R.id.faceImageView);

		successDrawable = new BitmapDrawable(activity.getResources(), BitmapFactory.decodeResource(activity.getResources(), R.drawable.success));
		errorDrawable = new BitmapDrawable(activity.getResources(), BitmapFactory.decodeResource(activity.getResources(), R.drawable.error));

		initScanView();
	}

	private void initScanView() {
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		float screenRatio = (float)size.x/size.y;
		final boolean isPortrait = size.y > size.x;

		int dp300 = (int) TypedValue.applyDimension(1, (float) 300, activity.getResources().getDisplayMetrics());

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(isPortrait? FrameLayout.LayoutParams.MATCH_PARENT: FrameLayout.LayoutParams.WRAP_CONTENT,isPortrait? FrameLayout.LayoutParams.WRAP_CONTENT: dp300);
		customScanView.setLayoutParams(params);
		if(isPortrait) {
			ratioSeekBar.setMax((int) ((1.33f - screenRatio) * 100));
		} else {
			ratioSeekBar.setMax((int) ((screenRatio - 1.33f) * 100));
		}
		ratioSeekBar.setProgress(0);
		customScanView.setRatio(1.33f);
		ratioTextView.setText(Float.toString(customScanView.getRatio()));

		ratioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (isPortrait)
					customScanView.setRatio(1.33f - (float) progress / 100);
				else
					customScanView.setRatio(1.33f + (float) progress / 100);

				ratioTextView.setText(Float.toString(customScanView.getRatio()));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}


	//Custom SDK Interface
	@Override
	public void onNetverifyCountriesReceived(HashMap<String, NetverifyCountry> countryList, String userCountryCode) {
		addToCallbackLog("onNetverifyCountriesReceived");
		showView(true, countryDocumentLayout);
		customCountryAdapter = new CustomCountryAdapter(activity, countryList);
		customCountrySpinner.setAdapter(customCountryAdapter);
		customCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				NetverifyCountry country = customCountryAdapter.getCountryObject(position);
				customDocumentAdapter = new CustomDocumentAdapter(activity, country.getDocumentTypes());
				customDocumentSpinner.setAdapter(customDocumentAdapter);
				customDocumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						hideView(false, partTypeLayout, inlineScanLayout, inlineConfirmLayout, finishButton, loadingIndicator);
						NetverifyCountry country = customCountryAdapter.getCountryObject(customCountrySpinner.getSelectedItemPosition());
						NVDocumentType documentType = customDocumentAdapter.getDocumentType(position);
						customVariantAdapter = new CustomVariantAdapter(activity, country.getDocumentVariants(documentType));
						customVariantSpinner.setAdapter(customVariantAdapter);
						customVariantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
								hideView(false, partTypeLayout, inlineScanLayout, inlineConfirmLayout, finishButton, loadingIndicator);
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {

							}
						});
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	public void onNetverifyResourcesLoaded() {
		addToCallbackLog("onNetverifyResourcesLoaded");
		frontSideButton.setEnabled(true);
		backSideButton.setEnabled(true);
		faceButton.setEnabled(true);
	}

	@Override
	public void onNetverifyScanForPartFinished(NetverifyDocumentPart documentPart, String image, boolean allPartsScanned) {
		customScanViewController.destroy();
		addToCallbackLog("onNetverifyScanForPartFinished");
		if(inlineScanLayout.getVisibility() == View.VISIBLE)
			hideView(false, inlineScanLayout);
		if(inlineConfirmLayout.getVisibility() == View.VISIBLE)
			hideView(false, inlineConfirmLayout);
		hideView(false, loadingIndicator);
		ImageView imageView = null;
		switch (documentPart.getScanSide()) {
			case FRONT:
				frontSideButton.setCompoundDrawablesWithIntrinsicBounds(successDrawable, null, null, null);
				imageView = frontSideImageView;
				break;
			case BACK:
				backSideButton.setCompoundDrawablesWithIntrinsicBounds(successDrawable, null, null, null);
				imageView = backSideImageView;
				break;
			case FACE:
				faceButton.setCompoundDrawablesWithIntrinsicBounds(successDrawable, null, null, null);
				imageView = faceImageView;
				break;
		}
		if(image != null) {
			Bitmap bitmap = loadShrinked(image, 300, 300);
			imageView.setImageBitmap(bitmap);
			imageView.setVisibility(View.VISIBLE);
		}
		if(allPartsScanned) {
			finishButton.setEnabled(true);
			showView(true, finishButton);
		}
	}

	private Bitmap loadShrinked(String file, int width, int height) {
		BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
		factoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file, factoryOptions);
		int heightRatio = (int) Math.ceil(factoryOptions.outHeight / (float) height);
		int widthRatio = (int) Math.ceil(factoryOptions.outWidth / (float) width);
		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				factoryOptions.inSampleSize = heightRatio;
			} else {
				factoryOptions.inSampleSize = widthRatio;
			}
		}
		factoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, factoryOptions);

		return bitmap;
	}

	@Override
	public void onNetverifyFinished(NetverifyDocumentData documentData, String scanReference) {
		addToCallbackLog("onNetverifyFinished");
		hideView(false, countryDocumentLayout, partTypeLayout, finishButton, loadingIndicator);

		appendKeyValue("Scan reference", scanReference);

		if (documentData != null) {
			//Dont change the key strings - they are needed for the qa automation
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
			appendKeyValue("Selected country", documentData.getSelectedCountry());
			appendKeyValue("Selected document type", documentData.getSelectedDocumentType() == null ? "" : documentData.getSelectedDocumentType().name());
			appendKeyValue("ID number", documentData.getIdNumber());
			appendKeyValue("Personal number", documentData.getPersonalNumber());
			appendKeyValue("OptData1", documentData.getOptionalData1());
			appendKeyValue("OptData2", documentData.getOptionalData2());
			appendKeyValue("Issue date", documentData.getIssuingDate() == null ? null : dateFormat.format(documentData.getIssuingDate()));
			appendKeyValue("Expiry date", documentData.getExpiryDate() == null ? null : dateFormat.format(documentData.getExpiryDate()));
			appendKeyValue("Issuing country", documentData.getIssuingCountry());
			appendKeyValue("Last name", documentData.getLastName());
			appendKeyValue("First name", documentData.getFirstName());
			appendKeyValue("Middle name", documentData.getMiddleName());
			appendKeyValue("Date of birth", documentData.getDob() == null ? null : dateFormat.format(documentData.getDob()));
			appendKeyValue("Gender", documentData.getGender() == null ? null : documentData.getGender().toString());
			appendKeyValue("Originating country", documentData.getOriginatingCountry());
			appendKeyValue("Street", documentData.getStreet());
			appendKeyValue("City", documentData.getCity());
			appendKeyValue("State", documentData.getState());
			appendKeyValue("Postal code", documentData.getPostalCode());

			NetverifyMrzData mrz = documentData.getMrzData();
			if (mrz == null)
				appendKeyValue("MRZ Validation: ", "null");
			else {
				appendKeyValue("MRZ Validation: ", "");
				appendKeyValue("   MRZ Type: ", mrz.getFormat().toString());
				appendKeyValue("   Line1", mrz.getMrzLine1());
				appendKeyValue("   Line2", mrz.getMrzLine2());
				if (mrz.getMrzLine3() != null) {
					appendKeyValue("   Line3", mrz.getMrzLine3());
				}
				appendKeyValue("   idNumberValid()", "" + mrz.idNumberValid());
				appendKeyValue("   dobValid()", "" + mrz.dobValid());
				appendKeyValue("   personalNumberValid()", "" + mrz.personalNumberValid());
				appendKeyValue("   expiryDateValid()", "" + mrz.expiryDateValid());
				appendKeyValue("   compositeValid()", "" + mrz.compositeValid());

			}

//			appendKeyValue("Name", documentData.getName());
			appendKeyValue("Name match", documentData.isNameMatch() ? "true" : "false");
			appendKeyValue("Name distance", String.valueOf(documentData.getNameDistance()));
			appendKeyValue("Liveness detected", String.valueOf(documentData.getLivenessDetected()));
		}
	}

	@Override
	public void onNetverifyError(int errorCode, String errorMessage, boolean retryPossible, String scanReference) {
		hideView(false, loadingIndicator);
		addToCallbackLog(String.format("onNetverifyError: %d, %s, %d, %s", errorCode, errorMessage, retryPossible ? 0 : 1, scanReference != null ? scanReference : "null"));
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setTitle("Scan error");
		alertDialogBuilder.setMessage(errorMessage);
		if (retryPossible) {
			alertDialogBuilder.setPositiveButton("retry", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						customSDKController.retry();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});
		}
		alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.stopNetverifyCustom();
			}
		});
		alertDialogBuilder.show();
	}


	//Custom ScanView Interface
	@Override
	public void onNetverifyCameraAvailable() {
		addToCallbackLog("onNetverifyCameraAvailable");
		switchCamera.setEnabled(customScanViewController.hasMultipleCameras());
		takePicture.setEnabled(customScanViewController.showShutterButton());
		toggleFlash.setEnabled(customScanViewController.hasFlash());
		stopScan.setEnabled(true);
		extraction.setEnabled(true);
	}

	@Override
	public void onNetverifyExtractionStarted() {
		addToCallbackLog("onNetverifyExtractionStarted");
		hideView(true, inlineScanLayout);
	}

	@Override
	public void onNetverifyPresentConfirmationView() {
		addToCallbackLog("onNetverifyPresentConfirmationView");
		showView(true, inlineConfirmLayout);
	}

	@Override
	public void onNetverifyNoUSAddressFound() {
		addToCallbackLog("onNetverifyNoUsAddressFound");
		showView(true, noUSAddressLayout);
	}

	private void showView(boolean hideLoading, View... views) {
		if(hideLoading)
			loadingIndicator.setVisibility(View.GONE);
		for(View view: views)
			view.setVisibility(View.VISIBLE);
	}
	private void hideView(boolean showLoading, View... views) {
		for(View view: views)
			view.setVisibility(View.GONE);
		if(showLoading)
			loadingIndicator.setVisibility(View.VISIBLE);
	}

	private void addToCallbackLog(String message) {
		Log.d("NetverifyCustom", message);
		TextView logline = new TextView(activity);
		logline.setText(message);
		inlineCallbackLog.addView(logline, 0);
		if (inlineCallbackLog.getChildCount() > 40)
			inlineCallbackLog.removeViewAt(inlineCallbackLog.getChildCount() - 1);
	}

	private void appendKeyValue(String key, CharSequence value) {
		addToCallbackLog(String.format("%s: %s", key, value));
	}

	private class CustomCountryAdapter extends ArrayAdapter<String> {

		private HashMap<String, NetverifyCountry> countryList;

		public CustomCountryAdapter(Context context, HashMap<String, NetverifyCountry> countryList) {
			super(context, android.R.layout.simple_spinner_item);

			this.countryList = countryList;
			ArrayList<String> sortedCountryList = new ArrayList<String>(countryList.keySet());
			Collections.sort(sortedCountryList);
			addAll(sortedCountryList);
		}

		public NetverifyCountry getCountryObject(int position) {
			return countryList.get(getItem(position));
		}
	}

	private class CustomDocumentAdapter extends ArrayAdapter<String> {

		private NVDocumentType[] documentTypes;

		public CustomDocumentAdapter(Context context, Set<NVDocumentType> documentTypeSet) {
			super(context, android.R.layout.simple_spinner_item);

			documentTypes = documentTypeSet.toArray(new NVDocumentType[documentTypeSet.size()]);
			for(NVDocumentType documentType: documentTypes) {
				add(documentType.name());
			}
		}

		public NVDocumentType getDocumentType(int position) {
			return documentTypes[position];
		}
	}

	private class CustomVariantAdapter extends ArrayAdapter<String> {

		private NVDocumentVariant[] documentVariants;

		public CustomVariantAdapter(Context context, Set<NVDocumentVariant> documentVariantSet) {
			super(context, android.R.layout.simple_spinner_item);

			this.documentVariants = documentVariantSet.toArray(new NVDocumentVariant[documentVariantSet.size()]);
			for(NVDocumentVariant documentVariant: documentVariants) {
				add(documentVariant.name());
			}
		}

		public NVDocumentVariant getDocumentVariant(int position) {
			return documentVariants[position];
		}
	}

}
