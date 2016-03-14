package com.ezetap.sdk;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;

public class EzetapDownloadUtils {

	private static final int DOWNLOAD_START = 1;
	private static final int DOWNLOAD_CANCEL = 2;
	private static final int DOWNLOAD_UPDATE = 3;

	private static final int DOWNLOAD_COMPLETED = 4;
	ProgressDialog dialog;
	boolean bCancelled = false;

	String downloadedFilePath = null;
	final String url;
	private Activity currentActivity;

	private String appFileName = null;

	public EzetapDownloadUtils(String url, Activity context) {
		this.url = url;
		this.currentActivity = context;
	}

	public EzetapDownloadUtils(String url, Activity context, String fileName) {
		this.url = url;
		this.currentActivity = context;
		if (fileName == null)
			fileName = "Ezetap";
		this.appFileName = fileName;
	}

	public void start() {
		Message msg = eventHandler.obtainMessage();
		msg.what = DOWNLOAD_START;
		eventHandler.sendMessage(msg);

	}

	public void startUpdate() {
		Message msg = eventHandler.obtainMessage();
		msg.what = DOWNLOAD_START;
		eventHandler.sendMessage(msg);
	}

	private void startDownload() {
		Thread aTh = new Thread() {

			public void run() {
				try {
					URL fileUrl;
					byte[] buf;
					int ByteRead = 0;
					int ByteWritten = 0;
					fileUrl = new URL(url);
					InputStream is = null;
					OutputStream os = null;

					URLConnection URLConn = fileUrl.openConnection();
					URLConn.setUseCaches(false);
					int totalFileSize = URLConn.getContentLength();
					is = URLConn.getInputStream();
					String fileName = "Ezetap.apk";
					if (appFileName != null) {
						fileName = appFileName + ".apk";
					}
					String abs;
					FileOutputStream fos = null;
					String dirPath;
					if (isSDCardAvailable(totalFileSize)) {
						dirPath = Environment.getExternalStorageDirectory() + "/ezetap-download/";
						File f = new File(dirPath);
						f.mkdirs();
						abs = dirPath + fileName;
						f = new File(abs);
						if (f.exists()) {
							f.delete();
							f = new File(abs);
							f.createNewFile();
						} else {
							f = new File(abs);
							f.createNewFile();
						}
						fos = new FileOutputStream(abs);

					} else {
						dirPath = currentActivity.getFilesDir().getPath() + "/";
						if(!isInternalMemoryAvailable(dirPath, totalFileSize))
							throw new IllegalStateException("Insufficient free space on internal/external storage to download application");
						abs = dirPath + fileName;
						File f = new File(abs);
						if (f.exists()) {
							f.delete();
						}
						fos = currentActivity.openFileOutput(fileName, Activity.MODE_WORLD_READABLE | Activity.MODE_WORLD_WRITEABLE);
					}
					downloadedFilePath = abs;

					Log.v("DEBUG", "Downloaded fle path =" + downloadedFilePath);

					os = new BufferedOutputStream(fos);

					buf = new byte[1024];
					/*
					 * This loop reads the bytes and updates a progressdialog
					 */
					while ((ByteRead = is.read(buf)) != -1 && !bCancelled) {

						os.write(buf, 0, ByteRead);
						ByteWritten += ByteRead;

						int tmpWritten = ByteWritten;

						Message msg = eventHandler.obtainMessage();
						msg.what = DOWNLOAD_UPDATE;
						if (totalFileSize < tmpWritten) {
							totalFileSize = tmpWritten;
						}
						if (appFileName == null || appFileName.equals("ezetap_android_service"))
							msg.obj = "Downloading Ezetap Service Application.\n Please wait..." + "(" + tmpWritten + "/" + totalFileSize + ")";
						else
							msg.obj = "Downloading " + appFileName + "\n Please wait..." + "(" + tmpWritten + "/" + totalFileSize + ")";

						eventHandler.sendMessage(msg);
					}
					is.close();
					os.flush();
					os.close();

					Thread.sleep(200);
					if (!bCancelled) {
						Message msg = eventHandler.obtainMessage();
						msg.what = DOWNLOAD_COMPLETED;
						msg.obj = "Downloading completed";
						eventHandler.sendMessage(msg);
					}
				} catch (final IllegalStateException e) {
					e.printStackTrace();
					bCancelled = true;
					if (dialog != null)
						dialog.dismiss();

					currentActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							String message = e.getMessage();
							AlertDialog.Builder errorDialog = new AlertDialog.Builder(currentActivity);
							errorDialog.setTitle("Download failed");
							errorDialog.setMessage(message);
							errorDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {
									notifyStatus(EzeConstants.RESULT_DOWNLOAD_FAILURE);
								}
							});
							errorDialog.show();
						}
					});
				} catch (final Exception e) {
					e.printStackTrace();
					bCancelled = true;
					if (dialog != null)
						dialog.dismiss();

					currentActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							String message = e.getMessage()+".Please try again";
							AlertDialog.Builder errorDialog = new AlertDialog.Builder(currentActivity);
							errorDialog.setTitle("Download failed");
							errorDialog.setMessage(message);
							errorDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {
									notifyStatus(EzeConstants.RESULT_DOWNLOAD_FAILURE);
								}
							});
							errorDialog.show();
						}
					});
				}

			}
		};

		aTh.start();
	}

	public Handler eventHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_START: {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				dialog = new ProgressDialog(currentActivity);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setTitle("Downloading");
				dialog.setMessage("Downloading application.\n Please wait...");
				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				
				dialog.setButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						try {
							bCancelled = true;
							dialog.dismiss();
							notifyStatus(EzeConstants.RESULT_DOWNLOAD_ABORTED);
						} catch (Exception e) {
						}
					}
				});

				dialog.show();
				startDownload();
			}
				break;

			case DOWNLOAD_UPDATE: {
				dialog.setMessage("" + msg.obj);
			}
				break;

			case DOWNLOAD_COMPLETED: {
				dialog.dismiss();
				try {
					String fileName = downloadedFilePath;
					Uri aUri = Uri.fromFile(new File(fileName));
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(aUri, "application/vnd.android.package-archive");
					currentActivity.startActivityForResult(intent, AppConstants.REQ_CODE_INSTALL);
					currentActivity = null;
					notifyStatus(EzeConstants.RESULT_DOWNLOAD_SUCCESS);
				} catch (Exception e) {
				}
			}
				break;

			default:
				break;
			}
		}
	};

	private void notifyStatus(int status) {
		Handler handler = EzetapUtils.getHandler();
		if(handler != null) {
			Message msg = handler.obtainMessage();
			msg.what = status;
			handler.sendMessage(msg);
		}
	}
	
	private boolean isSDCardAvailable(int minSpaceRequired) {
		long available = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		available = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && available > minSpaceRequired;
	}

	
	private boolean isInternalMemoryAvailable(String path, int minSpaceRequired) {
		long available = -1L;
		StatFs stat = new StatFs(path);
		available = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
		return available > minSpaceRequired;
	}
}