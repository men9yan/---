package com.wukong.logisticsproject.utils;

import com.wukong.logisticsproject.model.Staff;
import com.wukong.logisticsproject.model.Waybill;
import com.wukong.logisticsproject.service.impl.WaybillServiceImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MD5Utils {

    public static String text2md5(String text) {

        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] bs = digest.digest(text.getBytes());
            String hexString = "";
            for (byte b : bs) {
                int temp = b & 255;
                if (temp < 16 && temp >= 0) {
                    hexString = hexString + "0" + Integer.toHexString(temp);
                } else {
                    hexString = hexString + Integer.toHexString(temp);
                }
            }
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static void main(String[] args) {
       /* DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //通过随机数生成运单编号
        String num = DateTimeFormatter.ofPattern("yyMMddHHss").format(LocalDateTime.now());
        String waybillId =num;
        //String admin123 = MD5Utils.text2md5("123456");
       // System.out.println(num);
        WaybillServiceImpl waybillService = new WaybillServiceImpl();
        Waybill waybill = new Waybill();
        Staff staffInfo = new Staff();
        waybill.setDestinationBranch("七台河市新兴分网点");
        waybill.setNextBranch("哈尔滨市呼兰区分网点");
        staffInfo.setBranch("哈尔滨市呼兰区分网点");
        System.out.println(waybillService.getFindSendScanData(waybill, staffInfo).getNextBranch());*/



        for (int i = 0; i < 10; i++) {
            System.out.println(String.valueOf((int)(Math.random() * 36 + 35)));
        }
    }

}
