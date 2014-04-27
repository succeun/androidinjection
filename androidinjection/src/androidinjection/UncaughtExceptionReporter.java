package androidinjection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * SD-Card에 파일을 기록하고 싶으면, 
 * 반드시 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> 퍼미션을
 * 추가해주어야 한다.
 * @author Eun Jeong-Ho
 *
 */
public class UncaughtExceptionReporter implements Thread.UncaughtExceptionHandler {
	public static final String DEFAULT_ENCODING = "euc-kr";
	public static void reportException(Throwable e) {
		UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        if ( (handler instanceof UncaughtExceptionReporter) ) {
        	((UncaughtExceptionReporter) handler).uncaughtException(Thread.currentThread(), e);
        } else {
        	Log.w(UncaughtExceptionReporter.class.getSimpleName(), "UncaughtExceptionReporter가 DefaultUncaughtExceptionHandler으로 아직 정의되지 않았습니다.");
        }
	}
	
	private String versionName;
	private String packageName;
	private String FilePath;
	private String phoneModel;
	private String androidVersion;
	private String board;
	private String brand;
	private String CPU_ABI;
	private String device;
	private String display;
	private String fingerPrint;
	private String host;
	private String id;
	private String manufacturer;
	private String model;
	private String product;
	private String tags;
	private long time;
	private String type;
	private String user;
	private Thread.UncaughtExceptionHandler previousHandler;
	private Context context;
	
	private boolean isWriteFile = true;
	private String serverUrl = null;
	private String encoding = DEFAULT_ENCODING;
	
	public UncaughtExceptionReporter(Context context, boolean isWriteFile, String serverUrl, String encoding) {
		previousHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		this.context = context;
		this.isWriteFile = isWriteFile;
		this.serverUrl = serverUrl;
		this.encoding = encoding;
	}
	
	public UncaughtExceptionReporter(Context context, boolean isWriteFile, String serverUrl) {
		this(context, isWriteFile, serverUrl, DEFAULT_ENCODING);
	}

	public UncaughtExceptionReporter(Context context) {
		this(context, true, "", DEFAULT_ENCODING);
	}
	
	public UncaughtExceptionReporter(Context context, boolean isWriteFile) {
		this(context, isWriteFile, "", DEFAULT_ENCODING);
	}

	private long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	private long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	private void getInformations(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			// Version
			versionName = pi.versionName;
			// Package name
			packageName = pi.packageName;
			// Files dir for storing the stack traces
			FilePath = context.getFilesDir().getAbsolutePath();
			// Device model
			phoneModel = android.os.Build.MODEL;
			// Android version
			androidVersion = android.os.Build.VERSION.RELEASE;
			board = android.os.Build.BOARD;
			brand = android.os.Build.BRAND;
			CPU_ABI = android.os.Build.CPU_ABI;
			device = android.os.Build.DEVICE;
			display = android.os.Build.DISPLAY;
			fingerPrint = android.os.Build.FINGERPRINT;
			host = android.os.Build.HOST;
			id = android.os.Build.ID;
			manufacturer = android.os.Build.MANUFACTURER;
			model = android.os.Build.MODEL;
			product = android.os.Build.PRODUCT;
			tags = android.os.Build.TAGS;
			time = android.os.Build.TIME;
			type = android.os.Build.TYPE;
			user = android.os.Build.USER;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String toInformationString() {
		String value = "";
		value += "Version : " + versionName;
		value += "\n";
		value += "Package : " + packageName;
		value += "\n";
		value += "FilePath : " + FilePath;
		value += "\n";
		value += "Phone Model" + phoneModel;
		value += "\n";
		value += "Android Version : " + androidVersion;
		value += "\n";
		value += "Board : " + board;
		value += "\n";
		value += "Brand : " + brand;
		value += "\n";
		value += "CPU_ABI : " + CPU_ABI;
		value += "\n";
		value += "Device : " + device;
		value += "\n";
		value += "Display : " + display;
		value += "\n";
		value += "Finger Print : " + fingerPrint;
		value += "\n";
		value += "Host : " + host;
		value += "\n";
		value += "ID : " + id;
		value += "\n";
		value += "Manufacturer : " + manufacturer;
		value += "\n";
		value += "Model : " + model;
		value += "\n";
		value += "Product : " + product;
		value += "\n";
		value += "Tags : " + tags;
		value += "\n";
		value += "Time : " + time;
		value += "\n";
		value += "Type : " + type;
		value += "\n";
		value += "User : " + user;
		value += "\n";
		value += "Total Internal memory : " + getTotalInternalMemorySize();
		value += "\n";
		value += "Available Internal memory : "
				+ getAvailableInternalMemorySize();
		value += "\n";
		return value;
	}

	public void uncaughtException(Thread t, Throwable e) {
		try {
			getInformations(context);
			String reportContent = "";
			Date now = new Date();
			reportContent += "Error Report collected on : " + now.toString();
			reportContent += "\n";
			reportContent += "\n";
			reportContent += "Informations :";
			reportContent += "\n";
			reportContent += "==============";
			reportContent += "\n";
			reportContent += "\n";
			reportContent += toInformationString();
			reportContent += "\n\n";
			reportContent += "Stack : \n";
			reportContent += "======= \n";
			final Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			String stacktrace = result.toString();
			reportContent += stacktrace;
			reportContent += "\n";
			reportContent += "Cause : \n";
			reportContent += "======= \n";
			// If the exception was thrown in a background thread inside
			// AsyncTask, then the actual exception can be found with getCause
			Throwable cause = e.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				reportContent += result.toString();
				cause = cause.getCause();
			}
			printWriter.close();
			reportContent += "****  End of current Report ***";
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			
			String fileName = "stack-" + dateFormat.format(now) + ".stacktrace";
			if (isWriteFile) {
				saveAsFile(fileName, reportContent);
			}
			
			if(serverUrl != null && serverUrl.length() > 0) {
				sendToServer(fileName, reportContent);
			}
		} finally {
			previousHandler.uncaughtException(t, e);
		}
	}

	private void sendToServer(String fileName, String errorContent) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(serverUrl);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("filename", fileName));
        nvps.add(new BasicNameValuePair("stacktrace", errorContent));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
            httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void saveAsFile(String fileName, String errorContent) {
		try {
			String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
			int result = context.checkCallingOrSelfPermission(permission);
			if (result == PackageManager.PERMISSION_GRANTED) {
				File file = Environment.getExternalStorageDirectory();
				fileName = getClass().getPackage().getName() + "." + fileName;
				FileOutputStream trace = new FileOutputStream(new File(file, fileName));
				trace.write(errorContent.getBytes());
				trace.close();
			} else {	// PackageManager.PERMISSION_DENIED
				FileOutputStream trace = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				trace.write(errorContent.getBytes());
				trace.close();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}