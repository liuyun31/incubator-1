package com.example.incubatorui;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.incubatorui.GetJPEG.MergeByte;
import com.example.incubatorui.sendMessage.CloseAllOldMNLed;
import com.example.incubatorui.utils.ImageUtils.ByteToHex;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ApplicationUtil extends Application {
    //App文件的根目录
    private static final String DNAME ="/incubator";
    //图片路径
    private static final String PICTURE ="/picture";
    private Socket socket;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private Boolean socketIs=false;
    private Bitmap allBitmap;
    private PrintWriter out;
    MainActivity mainActivity = null;
    FullModeActivity fullModeActivity = null;
    //存储本次进入app打开RGB灯的状态
    private int[][][] rgb = new int[4][6][3];
    //存储本次进入app打开紫外灯的状态
    private int[] UV1 = new int[16];
    private int[] UV2 = new int[16];
    //存储白灯状态
    private int[] LED = new int[21];
    //数据是否需要更新
    boolean upDataCTHP = false;
    //标识，是否初始化
    int zeroIs = 0;
    //传感器数据
    String CO2 = "100",T1 = "100",T2 = "100",H = "100",P = "100";
    //点击坐标数据
    int X_int_total=0;
    int Y_int_total=0;
    int Z_int_total=0;
    int m = -1;
    int n = -1;
    int oldM = -1;
    int oldN = -1;
    //滤光片位置
    int filter = 0;
    //电机位置圈数和步数
    String XTotal = "0,0",YTotal = "0,0",ZTotal = "0,0";
    //下部状态
    String S = "Part";
    //机器序列号
    String ID = "123456789";
    //摄像机状态
    boolean camera = false;
    //步进倍数
    int multiple = 1;
    //判断是否可以发送数据的状态值
    boolean tcpSendIs = false;
    //op数组存储图片的开头角标和截至角标，也就是ffd8ff和ffd9出现的位置,后面两位存储 反馈数据的位置
    int[] op = {-1,-1,-1,-1};
    byte[] imgByte;
    Bitmap bitmap1 = null;
    boolean RD = false;//用于控制读数据线程是否执行

    //是否实时缩放
    Boolean video = true;


    public void init(String ip, String port) throws IOException, Exception {
        if(ip==null || port==null || ip.equals("") || port.equals("")){
            System.out.println("空值！");
            socketIs = false;
            return;
        }
        final String ADDRESS =ip;
        final int PORT = Integer.parseInt(port) ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //与服务器建立连接
                try {
                    socket = new Socket(ADDRESS, PORT);
                    dos = new DataOutputStream(socket.getOutputStream());
                    dis = new DataInputStream(socket.getInputStream());
                    socketIs = true;
                    tcpSendIs = true;
                    System.out.println("连接成功！！！！！！！！！！！");
                } catch (Exception e) {
                    socketIs = false;
                    System.out.println("连接失败？？？？？？？？？？？");
                    e.printStackTrace();
                }
            }
        }).start();
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    socket.sendUrgentData(0xff);
                    socketIs = true;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        for(int i =1;i<=15;i++){
            UV1[i] = 0;
        }
        for(int i =1;i<=15;i++){
            UV2[i] = 0;
        }
        for(int i =1;i<=20;i++){
            LED[i] = 0;
        }
        //初始化
        for(int i=0;i<4;i++){
            for(int j=0;j<6;j++){
                for(int k=0;k<3;k++){
                    rgb[i][j][k] = 0;
                }
            }
        }
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public Boolean getSocketIs()
    {
        return socketIs;
    }

    //将所有灯关闭后进行所有灯状态数组置0
    public void closeAlled(){
        for(int i =1;i<=15;i++){
            UV1[i] = 0;
        }
        for(int i =1;i<=15;i++){
            UV2[i] = 0;
        }
        for(int i =1;i<=20;i++){
            LED[i] = 0;
        }
        //初始化
        for(int i=0;i<4;i++){
            for(int j=0;j<6;j++){
                for(int k=0;k<3;k++){
                    rgb[i][j][k] = 0;
                }
            }
        }
    }
    public void setSocket(Socket socket) {
        try {
            this.socket.close();
            dos=null;
            dis=null;
            this.socket = socket;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setRD(boolean RD) {
        this.RD = RD;
        if(RD){
            ThreadReadData t = new ThreadReadData();
            t.start();
        }

    }

    public Socket getSocket() {
        return socket;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public int[] getUV2() {
        return UV2;
    }

    public void setUV2(int i,int UV2) {
        this.UV2[i] = UV2;
    }

    public int[] getLED() {
        return LED;
    }

    public void setLED(int i,int value) {
        this.LED[i] = value;
    }

    public int[] getUV1() {
        return UV1;
    }

    public void setUV1(int i,int value) {
        this.UV1[i] = value;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public Bitmap getAllBitmap() {
        return allBitmap;
    }

    public int getZeroIs() {
        return zeroIs;
    }

    public void setZeroIs(int zeroIs) {
        this.zeroIs = zeroIs;
    }

    public String getCO2() {
        return CO2;
    }

    public String getT1() {
        return T1;
    }

    public String getT2() {
        return T2;
    }

    public String getH() {
        return H;
    }

    public String getP() {
        return P;
    }

    public String getXTotal() {
        return XTotal;
    }

    public String getYTotal() {
        return YTotal;
    }

    public String getZTotal() {
        return ZTotal;
    }

    public int getX_int_total() {
        return X_int_total;
    }

    public void setX_int_total(int x_int_total) {
        X_int_total = x_int_total;
        int x =X_int_total;
        if(x >= 3430){ n=1;
            if(x>=6751){ n=2;
                if(x>=9713){ n=3;
                    if(x>=12854){ n=4;
                        if(x>=15996){ n=5;
                            if(x>=19137){ n=6;
                                if(x > 22280){n=-1;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(n != oldN){
            CloseAllOldMNLed c = new CloseAllOldMNLed(this,oldM,oldN);
            c.start();
            oldN = n;
        }
        XTotal = X_int_total/6400 + "," + X_int_total%6400;
    }

    public int getY_int_total() {
        return Y_int_total;
    }

    public void setY_int_total(int y_int_total) {
        Y_int_total = y_int_total;
        int y = Y_int_total;
        if(y >= 4272){ m=4;
            if(y >= 15754){ m=3;
                if(y >= 27237){ m=2;
                    if(y >= 38719){ m=1;
                        if(y > 50202){m=-1;
                        }
                    }
                }
            }
        }
        if(m != oldM){
            CloseAllOldMNLed c = new CloseAllOldMNLed(this,oldM,oldN);
            c.start();
            oldM = m;
        }
        YTotal = Y_int_total/1536 + "," + Y_int_total%1536;
    }
    public void setX_Y_int_total(int x_int_total,int y_int_total) {
        X_int_total = x_int_total;
        int x =X_int_total;
        if(x >= 3430){ n=1;
            if(x>=6751){ n=2;
                if(x>=9713){ n=3;
                    if(x>=12854){ n=4;
                        if(x>=15996){ n=5;
                            if(x>=19137){ n=6;
                                if(x > 22280){n=-1;
                                }
                            }
                        }
                    }
                }
            }
        }
        Y_int_total = y_int_total;
        int y = Y_int_total;
        if(y >= 4272){ m=4;
            if(y >= 15754){ m=3;
                if(y >= 27237){ m=2;
                    if(y >= 38719){ m=1;
                        if(y > 50202){m=-1;
                        }
                    }
                }
            }
        }
        if(m != oldM || n !=oldN){
            CloseAllOldMNLed c = new CloseAllOldMNLed(this,oldM,oldN);
            c.start();
            oldM = m;
            oldN = n;
        }
        XTotal = X_int_total/6400 + "," + X_int_total%6400;
        YTotal = Y_int_total/1536 + "," + Y_int_total%1536;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int getZ_int_total() {
        return Z_int_total;
    }

    public void setZ_int_total(int z_int_total) {
        Z_int_total = z_int_total;
        ZTotal = Z_int_total/640 + "," + z_int_total%640;
    }

    public Bitmap getBitmap1() {
        return bitmap1;
    }

    public void setBitmap1(Bitmap bitmap1) {
        this.bitmap1 = bitmap1;
    }

    public String getS() {
        return S;
    }

    public String getID() {
        return ID;
    }

    public int[][][] getRgb() {
        return rgb;
    }

    public void setRgb(int[][][] rgb) {
        this.rgb = rgb;
    }



    public boolean isCamera() {
        return camera;
    }

    public void setCamera(boolean camera) {
        this.camera = camera;
    }

    public void setRgbIndex(int m,int n,int num,int value){
        rgb[m][n][num] = value;
    }

    public boolean getTcpSendIs() {
        return tcpSendIs;
    }

    public void setAllBitmap(Bitmap allBitmap) {
        this.allBitmap = allBitmap;
    }

    public static String getDNAME() {
        return DNAME;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        RD = true;
        ThreadReadData t = new ThreadReadData();
        t.start();

    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }



    public void setFullModeActivity(FullModeActivity fullModeActivity) {
        this.fullModeActivity = fullModeActivity;
        RD = true;
        ThreadReadData t1  = new ThreadReadData();
        t1.start();
    }
    public FullModeActivity getFullModeActivity() {
        return fullModeActivity;
    }

    public void stopUpdateMainImg(){
        RD = false;
        mainActivity = null;
    }

    public void stopUpdateFullImg(boolean RDis){
        RD = RDis;
        fullModeActivity = null;
    }

    /**
     * 发送指令方法
     * @param message 指令
     */
    public boolean sendMessageF(String message){
        System.out.println("发送指令方法：tcpsendis:" + tcpSendIs);
        if(tcpSendIs){
            SendMessage s = new SendMessage(message);
            s.start();
            tcpSendIs = false;
            return true;
        }else {
            return false;
        }
    }

    /**
     * 发送指令
     */
    class  SendMessage extends Thread{
        String message;
        public SendMessage(String m){
            this.message = m;
        }
        @Override
        public void run() {
            if(!getSocketIs()){
                return;
            }
            System.out.println("Application:发送指令");
            if(dos != null){
                try {
                    byte[] m = message.getBytes();
                    dos.write(m);

                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 指令响应流程
     * 1.从数据流截取有效反馈数据（即ffd8开头，ffd9结尾的数据）
     * 2.将截取数据交给数据解析函数
     * 3.函数反馈此数据有效还是无效（true有效，false无效）将反馈状态赋值给tcpSendIs,如果无效则目前不可发送消息，等待有效状态。
     * 4.发送指令后立即调用（判断本次指令响应的最终结果）decideSendOr()
     * 5.调用Resonse线程，线程结束时不管是通过哪种方式都会对isGo赋值，随即while（isGo == 0）；循环结束，判断isGo是等于1还是2，反馈成功或失败。
     */

    /**
     * 分析数据
     * @param data
     * @return
     */
    public boolean analysisData(byte[] data){
        //去掉标识头和标识尾
        byte[] s = new byte[data.length-4];
        System.arraycopy(data,2,s,0,data.length-4);
        ByteToHex.printHexString(s);
        String sData = new String(s);
        System.out.println("过来的消息是：" + sData + "长度是：" + s.length);

        if("OK".equals(sData)){
            System.out.println("收到ok返回return true！！！");
            return true;
        }else {
            switch (sData.charAt(0)){
                case 'S':
                    if("S=".equals(sData.substring(0,2))){
                        S = sData.substring(2);
                        upDataCTHP = true;
                        return true;
                    }
                    break;
                case 'I':
                    if("ID=".equals(sData.substring(0,3))){
                        ID = sData.substring(3);
                        upDataCTHP = true;
                        return true;
                    }
                    break;
                case 'X':
                    if("XTotal=".equals(sData.substring(0,7))){//截取完剩余字符
                        XTotal = sData.substring(7);
                        upDataCTHP = true;
                        return true;
                    }
                    break;
                case 'Y':
                    if("YTotal=".equals(sData.substring(0,7))){
                        YTotal = sData.substring(7);
                        upDataCTHP = true;
                        return true;
                    }
                    break;
                case 'Z':
                    if("ZTotal=".equals(sData.substring(0,7))){
                        ZTotal = sData.substring(7);
                        upDataCTHP = true;
                        return true;
                    }
                    break;
                case 'C':
                    if("CO2=".equals(sData.substring(0,4))){
                        CO2 = sData.substring(4);
                        upDataCTHP = true;
                        return true;
                    }
                    break;
                case 'T':
                    if("T1=".equals(sData.substring(0,3))){
                        T1 = sData.substring(3);
                        upDataCTHP = true;
                        return true;
                    }else {
                        if("T2=".equals(sData.substring(0,3))){
                            T2 = sData.substring(3);
                            upDataCTHP = true;
                            return true;
                        }
                    }
                    break;
                case 'H':
                    if("H=".equals(sData.substring(0,2))){
                        H = sData.substring(2);
                        upDataCTHP = true;
                        return true;
                    }
                    break;
                case 'P':
                    if("P=".equals(sData.substring(0,2))){
                        P = sData.substring(2);
                        upDataCTHP = true;
                        return true;
                    }
                    break;
                default:break;
            }
        }
        return false;
    }

    //用线程执行读取服务器发来的数据
    class ThreadReadData extends Thread{
        @Override
        public void run() {
            //根据RD变量的值判断是否执行读数据
            while (RD) {
                try {
                    //定义一个字节集，存放输入的数据，缓存区大小为2048字节
                    byte[] ReadBuffer = new byte[1024];
                    //用于存放数据量
                    final int ReadBufferLengh;
                    //从输入流获取服务器发来的数据和数据宽度
                    //ReadBuffer为参考变量，在这里会改变为数据
                    //输入流的返回值是服务器发来的数据宽度
                    //获取缓冲区的数据，返回获取的数量
                    ReadBufferLengh = dis.read(ReadBuffer);
                    //ByteToHex.printHexString(ReadBuffer);
                    //判断获取数据的长度，当不为零的时候就进行数据的合成，因为张图片的数据不止1024个字节将多个字节数组进行合成
                    if(ReadBufferLengh != -1){
                        imgByte = MergeByte.mergeByte(imgByte , ReadBuffer,ReadBufferLengh);
                    }
                    int i=0;
                    //判断有没有图片开头的地址，如果有就直接从当前位置开始，没有就从零扫描
                    if(op[0] >= 0) i=op[0];
                    try {
                        //扫描数据中是否存在JPEG格式的图片，判断ffd8
                        if(imgByte != null){
                            for(;i<imgByte.length-1;i++){
                                //上面不能减2，因为减2就判断不到最后的ffd9了，有可能ffd9就在最后两个
                                if(i+2 < imgByte.length){
                                    //判断FF D8 FF，存在则记录下脚标
                                    if(imgByte[i]==(byte)255 && imgByte[i+1]==(byte)216 && imgByte[i+2]==(byte)255){
                                        op[0]=i;
                                    }else{
                                        if(imgByte[i]==(byte)255 && imgByte[i+1]==(byte)216){
                                            op[2] = i;
                                        }
                                    }
                                }
                                //判断FF D9，存在则记录下脚标
                                if(imgByte[i]==(byte)255 && imgByte[i+1]==(byte)217){
                                    //如果反馈数据头存在，也就是op[2]不等于-1，就判定这个ffd9是数据反馈结尾的下角标,否则这个角标是图片的结束标志
                                    if(op[2] != -1 && i-op[2] < 50){
                                        op[3]=i+1;
                                    }else {
                                        op[1]=i+1;
                                    }
                                }
                                //判断图片合法性，当两个都存在则都不为负数
                                if (op[0] != -1 &&  op[1] !=-1){
                                    //当图片尾部角标大于图片头部角标，认定为是一张图片，否则认定图片有误
                                    if(op[1] - (op[0]+2) > 0){
                                        break;
                                    }else {
                                        op[0] = -1;
                                        op[1]  =-1;
                                        break;
                                    }
                                }
                                //判断反馈消息合法性，以及进行消息处理操作
                                if(op[2] != -1 && op[3] != -1){
                                    System.out.println("有反馈消息");
                                    if(op[3] > op[2]){
                                        //将数据获取出去
                                        //存在图片则新建一个字节数组将图片数据复制过来
                                        byte[] getData = new byte[op[3]-op[2]+1];
                                        //参数对应数据：目标源，目标源的起始位置，目标，目标的起始位置，复制长度
                                        System.arraycopy(imgByte,op[2],getData,0,op[3]-op[2]+1);
                                        /*System.out.println("收到的字节数组是：");
                                        ByteToHex.printHexString(getData);
                                        System.out.println("数据长度：" + getData.length + "   数据是：" + (new String(getData)));*/
                                        //将数据传过去分析
                                        tcpSendIs = analysisData(getData);//分析数据
                                        System.out.println("分析完成，现在tcpSendIs是" + tcpSendIs);
                                        //将数据从imgByte删除
                                        byte[] newImgByte = new byte[imgByte.length - (op[3] - op[2]+1)];
                                        System.arraycopy(imgByte,0,newImgByte,0,op[2]);
                                        System.arraycopy(imgByte,op[3]+1,newImgByte,op[2],imgByte.length-op[3]-1);
                                        imgByte = newImgByte;
                                    }
                                    op[2] = -1;
                                    op[3] = -1;
                                }
                            }
                        }

                        //判断图片头部与尾部是否存在
                        if(op[0] !=-1 && op[1] != -1)
                        {
                            //存在图片则新建一个字节数组将图片数据复制过来
                            byte[] get = new byte[op[1]-op[0]+2];
                            //参数对应数据：目标源，目标源的起始位置，目标，目标的起始位置，复制长度
                            System.arraycopy(imgByte,op[0],get,0,op[1]-op[0]+1);
                            //将字节数组转化成bitmap对象
                            bitmap1 = BitmapFactory.decodeByteArray(get,0,get.length);

                            //将图片数据从imgByte中裁剪掉,因为不裁剪会一直累加
                            if(op[0] == 0  ||  op[0]>100000){
                                byte[] test = new byte[imgByte.length - (op[1])+1];
                                System.arraycopy(imgByte,op[1]+1,test,0,imgByte.length - (op[1])-1);
                                imgByte = test;

                            }else {
                                byte[] test = new byte[imgByte.length - (op[1] - op[0]+1)];
                                System.arraycopy(imgByte,0,test,0,op[0]);
                                System.arraycopy(imgByte,op[1]+1,test,op[0],imgByte.length-op[1]-1);
                                imgByte = test;
                            }
                            //初始化角标数据
                            op[0]=-1;
                            op[1]=-1;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        return;
                    }

                    //验证数据宽度，如果为-1则已经断开了连接
                    if (ReadBufferLengh == -1) {
                        //重新归位到初始状态
                        RD = false;
                        socket.close();
                        socket = null;
                    } else {
                        //判断图片是否存在
                        if(bitmap1!=null)
                        {
                            if(mainActivity != null){
                                mainActivity.updateImage(bitmap1);
                            }
                            if(fullModeActivity != null){
                                fullModeActivity.updateImage(bitmap1,video);
                            }
                            bitmap1=null;//使用后再次初始化为空
                        }
                    }
                } catch (Exception e) {
                    socketIs = false;
                    RD = false;
                    e.printStackTrace();
                }
            }
        }
    }
}
