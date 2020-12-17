package com.example.wireLessApk;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utilities {

    public static final String TAG = "Utilities";
    public static final String RESULT_PASS = "Pass";
    public static final String RESULT_FAIL = "Failed";
    private static final int BUFFER_SIZE = 128;
    private static final int SUB_START = 0;
    private static final int SUB_END = 1;

    public static synchronized String exec(final String cmd) {
        DataOutputStream localDataOutputStream = null;
        InputStream errorStream = null;
        InputStreamReader errorInReader = null;
        BufferedReader errorInBuffer = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process mProcess = runtime.exec("su");
            localDataOutputStream = new DataOutputStream(mProcess.getOutputStream());
            Log.i(TAG, "exec cmd = " + cmd);
            localDataOutputStream.writeBytes(cmd + "\n");
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();

            errorStream = mProcess.getErrorStream();
            errorInReader = new InputStreamReader(errorStream);
            errorInBuffer = new BufferedReader(errorInReader);
            StringBuffer excuteResult = new StringBuffer();
            String errorStr = null;
            while ((errorStr = errorInBuffer.readLine()) != null) {
                excuteResult.append(errorStr + "\n");
            }
            Log.e(TAG, excuteResult.toString());

            int waitFor = mProcess.waitFor();
            Log.i(TAG, "waitFor = " + waitFor);

            int result = mProcess.exitValue();
            Log.i(TAG, "result = " + result);

            return excuteResult.toString();
        } catch (Exception e) {
            Log.e(TAG, "exec cmdList error ", e);
            return null;
        } finally {
            try {
                if (null != errorInBuffer) {
                    errorInBuffer.close();
                }
                if (null != errorInReader) {
                    errorInReader.close();
                }
                if (null != errorStream) {
                    errorStream.close();
                }
                if (null != localDataOutputStream) {
                    localDataOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized String execString(final String cmd) {
       InputStreamReader inReader = null;
        BufferedReader inBuffer = null;
        DataOutputStream localDataOutputStream = null;
        InputStream inStream = null;
        try {
            Log.i(TAG, "execString = " + cmd);
            Process mProcess = Runtime.getRuntime().exec("sh");
            localDataOutputStream = new DataOutputStream(mProcess.getOutputStream());

            //for (String cmd : cmdList) {
            localDataOutputStream.writeBytes(cmd + "\n");
            localDataOutputStream.flush();
            //}
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();

            inStream = mProcess.getInputStream();
            inReader = new InputStreamReader(inStream);
            inBuffer = new BufferedReader(inReader);
            StringBuffer result = new StringBuffer();
            String s;
            while ((s = inBuffer.readLine()) != null) {
                Log.d(TAG, s);
                result.append(s);
            }
            return result.toString();
        } catch (Exception e) {
            Log.e(TAG, "execString error ", e);
            return null;
        } finally {
            try {
                if (null != inReader) {
                    inReader.close();
                }
                if (null != inBuffer) {
                    inBuffer.close();
                }
                if (null != localDataOutputStream) {
                    localDataOutputStream.close();
                }
                if (null != inStream) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] intToBytes(int n){
        byte[] b = new byte[4];
        for(int i = 0;i < 4;i++){
            b[3-i] = (byte)(n >> (24 - i * 8));
        }
        return b;
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ( ((src[offset + 3] & 0xFF) << 24)
                |((src[offset + 2] & 0xFF) << 16)
                |((src[offset + 1] & 0xFF) << 8)
                |(src[offset] & 0xFF));
        return value;
    }
    
    public static String readBuffer(String path) {
        try {
            InputStream in = new FileInputStream(path);
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder co = new StringBuilder();
            String line;
            while((line = localBufferedReader.readLine())!=null) {
                Log.d(TAG,line);
                co.append(line);
                //co.append("\n");
            }
            localBufferedReader.close();
            return co.toString();
        } catch(Exception e) {
            Log.d(TAG,e.getMessage());
            return "";
        }
    }
}