package com.napzzz.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;


import com.napzzz.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class YourSpace_AddPhotos extends Activity implements OnClickListener {

	ImageView cameraImage,doneImage,galleryImage,imagePreview;
	boolean isImageAttached = false;
	private static final String TAG = YourSpace_AddPhotos.class.getSimpleName();
	// Camera activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int IMAGE_PICK_GALLERY = 300;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	String imgDecodableString;
	private Button btn_next_photo;
	private Uri fileUri; // file url to store image/video
	List<String> imagesArray;

	//the images to display
	Integer[] imageIDs = {
			R.drawable.pic1,
			R.drawable.pic2,
			R.drawable.pic3,
			R.drawable.pic4,
			R.drawable.pic5,


	};

	//int[] imageIDs;


	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_your_space__add_photos);

   /* getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_color)));
      getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
      getActionBar().setDisplayShowHomeEnabled(true) ;
      getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Napzzz </font>"));*/

		imagePreview=(ImageView) findViewById(R.id.imagePreview);
		cameraImage=(ImageView) findViewById(R.id.cameraImage);
		doneImage=(ImageView) findViewById(R.id.doneImage);
		galleryImage=(ImageView) findViewById(R.id.galleryImage);
		btn_next_photo=(Button)findViewById(R.id.btn_next_photo);

		imagesArray = new ArrayList<String>();
		// Note that Gallery view is deprecated in Android 4.1---
		Gallery gallery = (Gallery) findViewById(R.id.gallery1);
		//gallery.setSpacing(1);
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				//Toast.makeText(getBaseContext(),"pic" + (position + 1) + " selected",
				//    Toast.LENGTH_SHORT).show();
				imagePreview.setImageResource(imageIDs[position]);

			}
		});
		cameraImage.setOnClickListener(this);
		galleryImage.setOnClickListener(this);
		btn_next_photo.setOnClickListener(this);

	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private int itemBackground;
		public ImageAdapter(Context c)
		{
			context = c;
			// sets a grey background; wraps around the images
			TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
			itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
			a.recycle();
		}
		// returns the number of images
		public int getCount() {
			return imageIDs.length;
		}
		// returns the ID of an item
		public Object getItem(int position) {
			return position;
		}
		// returns the ID of an item
		public long getItemId(int position) {
			return position;
		}
		// returns an ImageView view
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			imageView.setImageResource(imageIDs[position]);
			imageView.setLayoutParams(new Gallery.LayoutParams(300, 180));
			imageView.setBackgroundResource(itemBackground);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			return imageView;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){

			case R.id.cameraImage:
				showPickImageAlertCamera();

				break;

			case R.id.galleryImage:
				showPickImageAlertGallery();
				break;
			case R.id.btn_next_photo:

				Intent i =new Intent(YourSpace_AddPhotos.this,YourSpace_Details.class);
				startActivity(i);
				break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.your_space__add_photos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_next) {



			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showPickImageAlertCamera(){

		final CharSequence[] options = { "Take Photo","Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(YourSpace_AddPhotos.this);
		builder.setTitle("Add Photo!");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Take Photo"))
				{
					if (!isDeviceSupportCamera()) {
						Toast.makeText(getApplicationContext(),
								"Sorry! Your device doesn't support camera",
								Toast.LENGTH_LONG).show();
						// will close the app if the device does't have camera
						// finish();
					}
					else{
						//takePicture();
						captureImage();
					}
				}

				else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private void showPickImageAlertGallery(){

		final CharSequence[] options = {"Choose from Gallery","Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(YourSpace_AddPhotos.this);
		builder.setTitle("Add Photo!");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Choose from Gallery"))
				{
					openGallery();
				}
				else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private void openGallery() {

		// Create intent to Open Image applications like Gallery, Google Photos
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY);
	}

	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				// successfully captured the image
				// launching upload activity
				launchUploadActivity(true);


			} else if (resultCode == RESULT_CANCELED) {

				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();

			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}

		}
		if (requestCode == IMAGE_PICK_GALLERY && resultCode == RESULT_OK
				&& null != data) {
			// Get the Image from data

			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			// Get the cursor
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			// Move to first row
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			imgDecodableString = cursor.getString(columnIndex);
			cursor.close();

			imagePreview.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

			if (imgDecodableString != null) {
				imagesArray.add(imgDecodableString);

			}


		}
	}

	private void launchUploadActivity(boolean isImage){

		// bimatp factory
		BitmapFactory.Options options = new BitmapFactory.Options();
		// down sizing image as it throws OutOfMemory Exception for larger images
		options.inSampleSize = 8;
		final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
		imagePreview.setImageBitmap(bitmap);
		isImageAttached = true;
	}


	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {

		File mediaStorageDir = Environment.getExternalStorageDirectory();

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(TAG, "Oops! Failed create "
						+ "Android File Upload" + " directory");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + "Deepti" + ".jpg");
		}  else {
			return null;
		}

		return mediaFile;
	}

	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}
}