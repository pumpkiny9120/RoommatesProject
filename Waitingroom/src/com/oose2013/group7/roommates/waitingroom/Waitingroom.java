package com.oose2013.group7.roommates.waitingroom;

import java.io.File;

import com.example.trygridview.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Gallery;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Main activity for waiting room. The application must run with the server running.
 * It loads files from the server first, then stores them in the sdcard, finally displays it on the screen.
 * If we upload a new photo, the whole activity will be reloaded.
 * @author lbowen5
 */

@SuppressWarnings("deprecation")
public class Waitingroom extends Activity {

       private String[] FilePathStrings;
       private String[] FileNameStrings;
       private File[] listFile;
       Gallery gallerry;
       GridViewAdapter adapter;
       File file;
       private OutImageService web=new OutImageService();
       String username="lbwdruid";
       private String nowFile; 
       
       @Override
       public void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_main);
              
              new InImageService();
              
              SystemClock.sleep(5000);
              // Check for SD Card
              if (!Environment.getExternalStorageState().equals(
                           Environment.MEDIA_MOUNTED)) {
                     Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                                  .show();
              } else {
                     file = new File(Environment.getExternalStorageDirectory()
                                  + File.separator + "Roommates/WaitingRoom");
                     file.mkdirs();
              }

              if (file.isDirectory()) {
                     listFile = file.listFiles();
                     FilePathStrings = new String[listFile.length];
                     FileNameStrings = new String[listFile.length];

                     for (int i = 0; i < listFile.length; i++) {
                           FilePathStrings[i] = listFile[i].getAbsolutePath();
                           FileNameStrings[i] = listFile[i].getName();
                     }
              }

              gallerry = (Gallery) findViewById(R.id.gridview);
              adapter = new GridViewAdapter(this, FilePathStrings, FileNameStrings);
              gallerry.setSpacing(2);
              gallerry.setAdapter(adapter);

              gallerry.setOnItemClickListener(new OnItemClickListener() {

                     @Override
                     public void onItemClick(AdapterView<?> parent, View view,
                                  int position, long id) {

                           zoomImage(position);
                     }
              });
              
             
       }
       
       /**
        * This method calls up a new dialog to show the photo, shows the uploader of it,
        * and deals with "Follow".
        * @param position Photo index
        */
       private void zoomImage(int position) {
              final Dialog dialog = new Dialog(Waitingroom.this);
              dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
              dialog.getWindow().setBackgroundDrawableResource(
                           android.R.color.darker_gray);
              dialog.setContentView(R.layout.image_zoomdialog);
              ImageView imageview = (ImageView) dialog.findViewById(R.id.imageView1);
              Bitmap bmp = BitmapFactory.decodeFile(FilePathStrings[position]);
              nowFile=FilePathStrings[position];
              int i, j;
        	  for (i=0; i<nowFile.length(); i++)
        		  if (nowFile.charAt(i)=='~') break;
        	  for (j=i; j>=0; j--)
        		  if (nowFile.charAt(j)=='/') break;
        	  String uploader=nowFile.substring(j+1, i);
              imageview.setImageBitmap(bmp);
              dialog.show();
              Toast.makeText(getApplicationContext(), "Uploader: "+uploader, Toast.LENGTH_SHORT).show();
              Button button = (Button) dialog.findViewById(R.id.button1);
              button.setOnClickListener(new View.OnClickListener() {
                  public void onClick(View v) {
                	  int i, j;
                	  for (i=0; i<nowFile.length(); i++)
                		  if (nowFile.charAt(i)=='~') break;
                	  for (j=i; j>=0; j--)
                		  if (nowFile.charAt(j)=='/') break;
                	  String uploader=nowFile.substring(j+1, i);
                	  if (uploader.equals(username))
                		  Toast.makeText(getApplicationContext(), "You cannot follow yourself's photo.", Toast.LENGTH_SHORT).show();
                	  else
                		  Toast.makeText(getApplicationContext(), "Successfully made friends with "+uploader+"!", Toast.LENGTH_SHORT).show();
                  }
              });
       }
       
       /**
        * This method calls the android gallery for users to choose new photo.
        * It will reload the activity once the user uploads.
        * @param view
        */
       public void uploadPhoto(View view) {
    	   Intent gallery = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
           startActivityForResult(gallery, 1);
       }
       
       /**
        * Method needed by android system gallery.
        * Reloads the activity one second later.
        */
       @Override
       protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	   super.onActivityResult(requestCode, resultCode, data);
    	   if(resultCode==RESULT_OK && requestCode==1){
    		   Uri selectedImage=data.getData();
    		   String path=getPath(selectedImage);
    		   web.sendImage(path, username);
    		   Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();
    		   SystemClock.sleep(1000);
               Intent intent = new Intent(this, Waitingroom.class);
               startActivity(intent);
               finish();
    	   }
       }
       
       /**
        * Resolve the path for a certain picture.
        * @param uri
        * @return absolute path of the photo
        */
       public String getPath(Uri uri){
    	   String[] filePathColumn={MediaStore.Images.Media.DATA};
    	   Cursor cursor=getContentResolver().query(uri, filePathColumn, null, null, null);
    	   cursor.moveToFirst();
    	   int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
    	   return cursor.getString(columnIndex);
       }
}