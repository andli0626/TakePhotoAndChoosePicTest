package com.example.choosepictest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;

	private Button takePhoto;		// 拍照
	private Button chooseFromAlbum;	// 相册

	private ImageView 	picture;	//图片预览
	private Uri 		imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		takePhoto 		= (Button) findViewById(R.id.take_photo);
		chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
		
		picture = (ImageView) findViewById(R.id.picture);
		
		takePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				File outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKE_PHOTO);
			}
		});
		
		chooseFromAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				File outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent("android.intent.action.GET_CONTENT");
				intent.setType("image/*");
				intent.putExtra("crop", true);
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 拍照回调
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		// 相册回调
		case CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
					picture.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}

}
