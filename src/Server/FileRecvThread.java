package Server;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class FileRecvThread implements Runnable{

    private Socket mSocket;
    private FileRecvCallback mCallback;

    static final byte RECV_FAILED_DATA_ERROR = 1;
    static final byte RECV_FAILED_INCREDIBLE = 2;

    FileRecvThread(FileRecvCallback callback, Socket socket){
        mCallback = callback;
        mSocket = socket;
    }

    @Override
    public void run() {
        /*
        * finish the method for receiving file
        * */
        try {
            DataInputStream dis = new DataInputStream(mSocket.getInputStream());

            String username = dis.readUTF();
            String filename = dis.readUTF();
            Long fileLength = dis.readLong();

            if (!mCallback.isCredible(username)){
                mCallback.recvFileFailed(RECV_FAILED_INCREDIBLE, username);
                dis.close();
                mSocket.close();
                return;
            }

            // todo: change the directory, load from the configure file
            File dir = new File("/home/alvis/download/fileTP/");
            File file = new File(dir, filename);
            if (file.exists()){
                file = new File(dir, filename +"_"+ new Date().getTime());
            }
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int length;
            while ((length = dis.read(buf)) != -1){
                fos.write(buf, 0, length);
                fos.flush();
            }

            mCallback.gotFile(file.getAbsolutePath());

            dis.close();
            fos.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
