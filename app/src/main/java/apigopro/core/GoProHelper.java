package apigopro.core;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import apigopro.core.model.BacPacStatus;
import apigopro.core.model.BackPack;
import apigopro.core.model.CamFields;

public class GoProHelper {

	public static final boolean LOGGING_ENABLED = false;
    public static final int TIMEOUT = 10000;
    public static final int PASO_TIMEOUT = 10;
	private final DefaultHttpClient mClient = newInstance();
	private String mCameraAddress = null;
	private String ipAddress;
	private Integer port;
	private String password;

    public boolean inicializado = false;
    public boolean consultaOK = false;
    public byte[] res = null;


/*******************/
/*  Constructores  */
/*******************/

    public GoProHelper() {
    }

    public GoProHelper(String ipAddress, Integer port, String password) {
        this();
        this.setIpAddress(ipAddress);
        this.setPort(port);
        this.setPassword(password);
        // this.mCamera = paramGoProCamera;
        this.mCameraAddress = ("http://" + ipAddress + ":" + port);
        System.out.println("Helper creado\n");

    }


/*********************/
/*  Metodos locales  */
/*********************/

    private boolean sendCommand(Operations paramString) {
        try {
            sendGET(this.mCameraAddress + paramString.toString() + "?t="
                    + this.getToken());
            return true;
        } catch (Exception localException) {
        }
        return false;
    }

    public boolean sendCommand(Operations paramString, int paramInt) throws Exception {
       // StringBuilder localStringBuilder = new StringBuilder("%");
        //Object[] arrayOfObject = new Object[1];
        //arrayOfObject[0] = Integer.valueOf(paramInt);
        return sendCommand(paramString, "%0" + Integer.valueOf(paramInt));
    }

    public boolean sendCommand(Operations paramString1, String paramString2) throws Exception {
        String param = null;
        //if (!paramString1.toString().startsWith("/")) {
        param = /*"/" +*/ paramString1.toString();
        //}
        sendGET(this.mCameraAddress + param + "?t=" + this.getToken() + "&p="
                + paramString2);
        return true;

    }

    private byte[] sendGET(final String paramString) {

        res = null;
        new AsynConection().execute(paramString);

        for (int i=1; (res == null)  && (i < TIMEOUT); i=(i+PASO_TIMEOUT)){
            Log.i("tag","En espera " + i + " milisegundos\n");
            try {
                Thread.sleep(PASO_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return res;

/*
        try {
        Log.i("tag", "en sendget simple antes de sendget con mClient\n");Thread.sleep(1000);

        //return sendGET(paramString, this.mClient);

        InputStream data = null;
        Log.i("tag","En sendget 1\n");Thread.sleep(1000);
        HttpClient client = new DefaultHttpClient();
        Log.i("tag","En sendget 2\n");Thread.sleep(1000);
        HttpGet get = new HttpGet(paramString);
        Log.i("tag","En sendget 3\n");Thread.sleep(1000);
        try {
            Log.i("tag","En sendget 4\n");Thread.sleep(1000);
            HttpResponse response = client.execute(get);
            Log.i("tag","En sendget 5\n");Thread.sleep(1000);
            data = response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("tag","En sendget 6\n");Thread.sleep(1000);

        return getBytesFromInputStream(data);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
*/

       /* Log.i("tag", paramString);


        //(new Thread(new Runnable() {

            //@Override
            //public void run() {
                try {
                    URL url = new URL(paramString);
                    java.net.URLConnection con = url.openConnection();
                    con.connect();
                    java.io.BufferedReader in =new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

           // }
        //})).start();

*/
      //  return new byte[0];
    }

    public byte[] sendGET(String paramString, DefaultHttpClient paramDefaultHttpClient) throws Exception {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        HttpResponse localHttpResponse;
        Log.i("tag", "En sendget conmpleto antes del try\n");
        try {
            Log.i("tag", "Cmd final :" + paramString);

            System.setProperty("http.keepAlive", "true");
            HttpGet localHttpGet = new HttpGet(paramString);
            Log.i("tag","En sendget 1\n");
            localHttpResponse = paramDefaultHttpClient.execute(localHttpGet);

            int statusCode = localHttpResponse.getStatusLine().getStatusCode();
            Log.i("tag","En sendget 2\n");

            if (statusCode >= 400) {
                localHttpGet.abort();
                Log.i("tag","En sendget 3\n");
                throw new IOException("Fail to send GET - HTTP error code = ["
                        + statusCode + "]");
            }
        } catch (Exception localException) {
            throw localException;
        }
        int j = (int) localHttpResponse.getEntity().getContentLength();
        if (j <= 0)
            j = 128;
        InputStream localInputStream = localHttpResponse.getEntity()
                .getContent();
        byte[] arrayOfByte = new byte[j];
        while (true) {
            if (localInputStream.read(arrayOfByte, 0, arrayOfByte.length) == -1) {
                localByteArrayOutputStream.flush();
                return localByteArrayOutputStream.toByteArray();
            }
            localByteArrayOutputStream
                    .write(arrayOfByte, 0, arrayOfByte.length);
        }
    }



    private class AsynConection extends AsyncTask {

        @Override
        protected void onPreExecute() {
            res = null;
        }

        @Override
        protected void onPostExecute(Object o) {
            //consultaOK = true;
        }

        @Override
        protected Object doInBackground(Object... arg0) {

            try {
                //Log.i("tag", "en sendget simple antes de sendget con mClient: + ( "+ arg0[0] +")\n");
                InputStream data = null;
                long j=0;
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet((String)arg0[0]);
                try {
                    HttpResponse response = client.execute(get);
                    data = response.getEntity().getContent();
                    j = response.getEntity().getContentLength();
                } catch (IOException e) {
                    e.printStackTrace();
                }

/*                if (data == null){
                    Log.i("tag","En sendget 6 data null\n");}
                else{
                    Log.i("tag","En sendget 6 data ok\n");}*/

                   res = getBytesFromInputStream(data, j);

                Log.i("tag", "despues de hacer el doinback: arraylenght\n" + res.length);

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static byte[] getBytesFromInputStream(InputStream is, long j) throws IOException {

/*        // Create the byte array to hold the data
        byte[] bytes = new byte[128];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file ");
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;*/
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrayOfByte = new byte[(int)j];
        while (true) {
            if (is.read(arrayOfByte, 0, arrayOfByte.length) == -1) {
                localByteArrayOutputStream.flush();
                return localByteArrayOutputStream.toByteArray();
            }
            localByteArrayOutputStream.write(arrayOfByte, 0, arrayOfByte.length);

        }
    }

    public DefaultHttpClient newInstance() {
        BasicHttpParams localBasicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(localBasicHttpParams,
                HttpVersion.HTTP_1_1);
        HttpProtocolParams
                .setContentCharset(localBasicHttpParams, "ISO-8859-1");
        HttpProtocolParams.setUseExpectContinue(localBasicHttpParams, true);
        HttpConnectionParams.setStaleCheckingEnabled(localBasicHttpParams,
                false);
        HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, 10000);
        HttpConnectionParams.setSoTimeout(localBasicHttpParams, 10000);
        HttpConnectionParams.setSocketBufferSize(localBasicHttpParams, 8192);
        SchemeRegistry localSchemeRegistry = new SchemeRegistry();
        localSchemeRegistry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        ConnManagerParams.setMaxTotalConnections(localBasicHttpParams, 1);
        return new DefaultHttpClient(new ThreadSafeClientConnManager(
                localBasicHttpParams, localSchemeRegistry),
                localBasicHttpParams);
    }

    private void hexDump(byte[] paramArrayOfByte, String paramString) {	}

    private boolean passFail(byte[] paramArrayOfByte) {
        boolean bool = false;
        if (paramArrayOfByte != null) {
            int i = paramArrayOfByte.length;
            bool = false;
            if (i > 0) {
                int j = paramArrayOfByte[0];
                bool = false;
                if (j == 0)
                    bool = true;
            }
        }
        return bool;
    }

    public int fromBoolean(boolean paramBoolean) {
        if (paramBoolean)
            return 1;
        return 0;
    }

    public boolean toBoolean(int paramInt) {
        return paramInt != 0;
    }



/***************/
/*  Controles  */
/***************/

    public boolean deleteFilesOnSd() {
        return sendCommand(Operations.CAMERA_DA);
    }

    public boolean deleteLastFileOnSd() {
        return sendCommand(Operations.CAMERA_DL);
    }

    public boolean startRecord() throws Exception {
        return sendCommand(Operations.BACPAC_SH, "%01");
    }

    public boolean stopRecord() throws Exception {
        return sendCommand(Operations.BACPAC_SH, "%00");
    }

    public boolean turnOnCamera() throws Exception {
        return sendCommand(Operations.BACPAC_PW, "%01");
    }

    public boolean turnOffCamera() throws Exception {
        return sendCommand(Operations.BACPAC_PW, "%00");
    }

    public boolean changeModeCamera() throws Exception {
        return sendCommand(Operations.BACPAC_PW, "%02");
    }



/*************/
/*  Getters  */
/*************/

    public String getCameraNameCN() {
    String str = this.getIpAddress();
    byte[] arrayOfByte;
    try {
        arrayOfByte = sendGET(this.mCameraAddress + Operations.CAMERA_CN
                + "?t=" + this.getToken());
        if ((arrayOfByte == null) || (arrayOfByte.length == 0)
                || (arrayOfByte[0] == 1))
            return str;
    } catch (Exception localException) {
        return str;
    }
    int i = arrayOfByte[1];
    int j = 0;
    for (int k = 2;; k++) {
        if (j >= i)
            return str;
        if (k < arrayOfByte.length)
            str = str + (char) arrayOfByte[k];
        j++;
    }
}

    public CamFields getCameraInfo() {
        CamFields localCamFields = new CamFields();
        GoProProtocolParser localGoProProtocolParser;
        try {
            byte[] arrayOfByte = sendGET(this.mCameraAddress
                    + Operations.CAMERA_CV + "?t=" + this.getToken());
            localGoProProtocolParser = new GoProProtocolParser(arrayOfByte);
            if (localGoProProtocolParser.extractResultCode() != GoProProtocolParser.RESULT_IS_OK)
                return null;
        } catch (Exception localException) {
            return null;
        }
        localCamFields.setProtocol(localGoProProtocolParser
                .extractUnsignedByte());
        localCamFields.setModel(localGoProProtocolParser.extractUnsignedByte());
        localCamFields.setVersion(localGoProProtocolParser.extractString());
        localCamFields.setCamname(localGoProProtocolParser.extractString());
        return localCamFields;
    }

    public CamFields getCameraSettings() throws Exception {
        try {
            //Log.i("tag", "antes de hacer el sendget\n");Thread.sleep(1000);
            byte[] arrayOfByte = sendGET(this.mCameraAddress + "/camera/se" + "?t=" + this.getToken());
            Thread.sleep(3000);
            if (arrayOfByte == null)
                Log.i("tag", "array nulo\n");Thread.sleep(1000);

            //Log.i("tag", "despues de hacer el sendget: arraylenght\n" + arrayOfByte.length);Thread.sleep(1000);

            return getCameraSettings(new GoProProtocolParser(arrayOfByte));
        } catch (Exception e) {
            //throw new Exception("Fail to get camera settings", localException);
            e.printStackTrace();
            return null;
        }
    }

    public CamFields getCameraSettings(GoProProtocolParser paramGoProProtocolParser) {
        CamFields localCamFields = new CamFields();
        try {
            if (paramGoProProtocolParser.extractResultCode() != GoProProtocolParser.RESULT_IS_OK)
                return null;
            localCamFields.setMode(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setMicrophoneMode(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setOndefault(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setExposure(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setTimeLapse(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setAutopower(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setFieldOfView(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setPhotoResolution(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setVidres(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setAudioinput(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setPlaymode(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setPlaybackPos(paramGoProProtocolParser.extractInteger());
            localCamFields.setBeepSound(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setLedblink(paramGoProProtocolParser.extractUnsignedByte());
            int i = paramGoProProtocolParser.extractByte();
            localCamFields.setPreviewActive(true);
            localCamFields.setBattery(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setUsbMode(paramGoProProtocolParser.extractUnsignedByte());
            localCamFields.setPhotosAvailable(paramGoProProtocolParser.extractShort());
            localCamFields.setPhotosOncard(paramGoProProtocolParser.extractShort());
            localCamFields.setVideoAvailable(paramGoProProtocolParser.extractShort());
            localCamFields.setVideoOncard(paramGoProProtocolParser.extractShort());
            localCamFields.setShutter(paramGoProProtocolParser.extractUnsignedByte());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return localCamFields;
    }

    public CamFields getCameraSettingsExtended() {
        GoProProtocolParser localGoProProtocolParser;
        CamFields localCamFields;
        try {
            byte[] arrayOfByte = sendGET(this.mCameraAddress
                    + Operations.CAMERA_SX + "?t=" + this.getToken());
            localGoProProtocolParser = new GoProProtocolParser(arrayOfByte);
            localCamFields = getCameraSettings(localGoProProtocolParser);
            if (localCamFields == null)
                return localCamFields;
        } catch (Exception localException) {
            return null;
        }
        HashMap localHashMap = new HashMap();
        localCamFields.setSettingsBag(localHashMap);
        localHashMap.put("camera/SS",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/BU",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/CS",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/WB",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/BR",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/PN",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/LO",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/PS",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/BX",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/TS",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("video_loop_counter",
                Long.valueOf(localGoProProtocolParser.extractInteger()));
        localHashMap.put("external_battery",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        int i = localGoProProtocolParser.extractByte();
        localHashMap.put("bombie_attached", Integer.valueOf(i & 0x8));
        localHashMap.put("lcd_attached", Integer.valueOf(i & 0x4));
        localHashMap.put("is_boradcasting", Integer.valueOf(i & 0x2));
        localHashMap.put("is_uploading", Integer.valueOf(i & 0x1));
        localHashMap.put("camera/LV",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/LN",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/LS",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/VV",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        localHashMap.put("camera/FS",
                Short.valueOf(localGoProProtocolParser.extractUnsignedByte()));
        return localCamFields;
    }

    public BackPack getBackPackInfo() throws Exception {
        BackPack localBackPack = new BackPack();
        GoProProtocolParser localGoProProtocolParser;
        try {
            byte[] arrayOfByte = sendGET("http://" + this.getIpAddress()
                    + Operations.BACPAC_CV);
            localGoProProtocolParser = new GoProProtocolParser(arrayOfByte);
            if (localGoProProtocolParser.extractResultCode() != GoProProtocolParser.RESULT_IS_OK) {
                return null;
            }
        } catch (Exception localException) {
            throw new Exception("Fail to get backpack info", localException);
        }
        localBackPack
                .setVersion(localGoProProtocolParser.extractUnsignedByte());
        localBackPack.setModel(localGoProProtocolParser.extractUnsignedByte());
        localBackPack.setId(localGoProProtocolParser
                .extractFixedLengthString(2));
        localBackPack.setBootLoaderMajor(localGoProProtocolParser
                .extractUnsignedByte());
        localBackPack.setBootLoaderMinor(localGoProProtocolParser
                .extractUnsignedByte());
        localBackPack.setBootLoaderBuild(localGoProProtocolParser
                .extractUnsignedByte());
        localBackPack.setRevision(localGoProProtocolParser
                .extractUnsignedByte());
        localBackPack.setMajorversion(localGoProProtocolParser
                .extractUnsignedByte());
        localBackPack.setMinorversion(localGoProProtocolParser
                .extractUnsignedByte());
        localBackPack.setBuildversion(localGoProProtocolParser
                .extractUnsignedByte());
        localBackPack.setWifimac(localGoProProtocolParser
                .extractFixedLengthString(6));
        localBackPack.setSSID(localGoProProtocolParser.extractString());
        return localBackPack;
    }

    public BacPacStatus getBacpacStatus() throws Exception {
        BacPacStatus localBacPacStatus = new BacPacStatus();
        GoProProtocolParser localGoProProtocolParser;
        try {
            byte[] arrayOfByte = sendGET(this.mCameraAddress
                    + Operations.BACPAC_SE + "?t=" + this.getToken());
            hexDump(arrayOfByte, "BacPac SE");
            localGoProProtocolParser = new GoProProtocolParser(arrayOfByte);
            if (localGoProProtocolParser.extractResultCode() != GoProProtocolParser.RESULT_IS_OK)
                return null;
        } catch (Exception localException) {
            throw localException;
        }
        localBacPacStatus.setBacPacBattery(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setWifiMode(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setBlueToothMode(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setRSSI(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setShutterStatus(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setAutoPowerOff(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setBlueToothAudioChannel(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setFileServer(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setCameraPower(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setCameraI2CError(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setCameraReady(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setCameraModel(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setCameraProtocolVersion(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setCameraAttached(localGoProProtocolParser
                .extractUnsignedByte());
        localBacPacStatus.setBOSSReady(localGoProProtocolParser
                .extractUnsignedByte());
        return localBacPacStatus;
    }

    public String getBacPacPassword() {
        try {
            GoProProtocolParser localGoProProtocolParser = new GoProProtocolParser(
                    sendGET(this.mCameraAddress + Operations.BACPAC_SD));
            byte[] arrayOfByte = new byte[1];
            arrayOfByte[0] = localGoProProtocolParser.extractByte();
            boolean bool = passFail(arrayOfByte);
            Object localObject = null;
            if (bool) {
                String str = localGoProProtocolParser.extractString();
                localObject = str;
            }
            return (String) localObject;
        } catch (Exception localException) {
        }
        return null;
    }

    private Integer getPort() {
        return port;
    }

    public String getToken() {
        return getPassword();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getCameraHLSSegment() {
        try {
            byte[] arrayOfByte = sendGET(this.mCameraAddress
                    + Operations.CAMERA_HS2 + "?t=" + this.getToken());
            return new GoProProtocolParser(arrayOfByte).extractUnsignedByte();
        } catch (Exception localException) {
        }
        return -1;
    }

    private String getPassword() {
        return password;
    }



/*************/
/*  Setters  */
/*************/

    public boolean modeCamera() throws Exception {
    return sendCommand(Operations.CAMERA_CM, "%00");
}

    public boolean modePhoto() throws Exception {
        return sendCommand(Operations.CAMERA_CM, "%01");
    }

    public boolean modeBurst() throws Exception {
        return sendCommand(Operations.CAMERA_CM, "%02");
    }

    public boolean setCamVideoResolution(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_VR, paramInt);
    }

    public boolean setCamShutter(boolean paramBoolean) throws Exception {
        return sendCommand(Operations.BACPAC_SH, fromBoolean(paramBoolean));
    }

    public boolean setCamSound(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_BS, paramInt);
    }

    public boolean setCamTimeLapseTI(String paramString) throws Exception {
        return sendCommand(Operations.CAMERA_TI, paramString);
    }

    public boolean setCamUpDown(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_UP, paramInt);
    }

    public boolean setCamLivePreview(boolean paramBoolean) throws Exception {
        if (paramBoolean)
            ;
        for (int i = 2;; i = 0)
            return sendCommand(Operations.CAMERA_PV, i);
    }

    public boolean setCamLocate(boolean paramBoolean) throws Exception {
        return sendCommand(Operations.CAMERA_LL, fromBoolean(paramBoolean));
    }

    public boolean setCamMode(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_CM, paramInt);
    }

    public boolean setCamNtscPal(boolean paramBoolean) throws Exception {
        if (paramBoolean)
            ;
        for (int i = 0;; i = 1)
            return sendCommand(Operations.CAMERA_VM, i);
    }

    public boolean setCamOnScreenDisplay(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_DS, paramInt);
    }

    public boolean setCamPhotoResolution(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_PR, paramInt);
    }

    public boolean setCamAutoPowerOff(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_AO, paramInt);
    }

    public boolean setCamDateTime(String paramString) {
        try {
            boolean bool = passFail(sendGET(this.mCameraAddress
                    + "/camera/TM?t=" + this.getToken() + "&p=" + paramString));
            return bool;
        } catch (Exception localException) {
        }
        return false;
    }

    public boolean setCamDefaultMode(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_DM, paramInt);
    }

    public boolean setCamExposure(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_EX, paramInt);
    }

    public boolean setCamFov(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_FV, paramInt);
    }

    public boolean setCamLEDBlink(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_LB, paramInt);
    }

    public boolean setCameraHLSSegment(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_HS, paramInt);
    }

    public boolean setCameraName(String paramString) {
        if ((paramString.length() > 31) || (paramString.length() == 0))
            return false;
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(paramString.length());
        arrayOfObject[1] = paramString;
        String str = URLEncoder.encode(String.format("%x%s", arrayOfObject)
                .replaceAll("\\s+", "%20"));
        try {
            boolean bool = passFail(sendGET(this.mCameraAddress
                    + Operations.CAMERA_CN + "?t=" + this.getToken() + "&p=%0"
                    + str));
            return bool;
        } catch (Exception localException) {
        }
        return false;
    }

    public boolean setBacPacWifiMode(int paramInt) throws Exception {
        return sendCommand(Operations.BACPAC_WI, paramInt);
    }

    public boolean setBackPackPowerCamera(boolean paramBoolean) throws Exception {
        return sendCommand(Operations.BACPAC_PW, fromBoolean(paramBoolean));
    }

    public boolean setCamProtune(int paramInt) throws Exception {
        return sendCommand(Operations.CAMERA_PT, paramInt);
    }

    private void setPassword(String password) {
        this.password = password;
    }

    private void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    private void setPort(Integer port) {
        this.port = port;
    }


}