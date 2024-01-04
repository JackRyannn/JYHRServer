package com.yhdyy.jyhrserver.services;

import com.google.gson.Gson;
import com.yhdyy.jyhrserver.bos.HzjbxxBo;
import com.yhdyy.jyhrserver.bos.JybgBo;
import com.yhdyy.jyhrserver.bos.JybgxqBo;
import com.yhdyy.jyhrserver.bos.MsgRequestBody;
import com.yhdyy.jyhrserver.client.NetService;
import com.yhdyy.jyhrserver.daos.chisdb.ChisDao;
import com.yhdyy.jyhrserver.daos.clabdb.ClabDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
@Service
public class JyhrService {
    @Resource
    private ChisDao chisDao;
    @Resource
    private ClabDao clabDao;
    @Resource
    private NetService netService;

    private static final String METHOD_HZJBXX = "BatchSavePatient";
    private static final String METHOD_JYBG = "BatchSavePatientReport";
    private static final String METHOD_JYBGXQ = "BatchSavePatientReportDetail";

    /**
     * RSA最大加密明文大小`
     * 1024位的证书，加密时最大支持117个字节，解密时为128；
     * 2048位的证书，加密时最大支持245个字节，解密时为256。
     * RSA最大解密密文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 245;
    private static final int MAX_DECRYPT_BLOCK = 256;
    private static final String ALGORITHM_NAME = "RSA";
    //固定的加密公钥
    public static final String COMMON_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAumr+6z+h767HP++6dLnNsvnJSaINNOYWnZESafFMsb5xUA334w9fKmLXLI6ROUV9d/kWMh0eGKNGAnFWMJb+/QUrC984dviwwZI4JExE3yLWZWv2gKStTYBNvLafiOrLzZrFzFkOpN6+PzNV2m8W2Ltdn3xNP7RD0eocEvbHJ6TlPjAhUAe09Of5mNc4UH3yjr8B6PQcsL3tgf/ZMaOAEVNLSXTzGUZ4Qjbs85AkythsWigymvQFQrQ/w/BaypgAlf995jVCn8V2g3s9XVBFAmz0cXs2ahitSnMruGRlwZ4SLWjSXmVVa/gaESRszUNv/FTPKAV2uGi0MQy5ktQkvQIDAQAB";


    private static final String HOS_PRIVATEKEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDPCT5lri4EC93SNBVb+nYhbIFDsb5ORginLYrjU077oHu+8A4AVSbEKB1UQKtSeRfFHYYq7dDi64uGGYzM82wpw2z8FKUqkGolMsHMPvOQw/zj3UrQqFZYM2KmMrtxGfvourp3wZoPFl52ngE8X46RBG8Gi+kJOrushJmE8oM16L477xjsTcXvYhpQsHWY1D74PWWrf2/rI0H1JrJhnhRyg70++xL6O834SJx+MYBzRudmINU2XfbFdtYZgSObcftaFYkj+uOKu9uXpNn8tYjpzs1efXD1uVIu+S52QKxq4FBnyL0EfjOxLeMcotZ9gWgJQVMLC5W//9IdR5AFIeffAgMBAAECggEAd70B4G7HYEG4rcy/ma2WSqhlblBLHxvz/zQgwm65HGBpkTlm75xPylzk8YhhTVaJf9Aee4+FIuhVjl4Sh6o1IUZMLtKF38McEJ11Zfn2hgBDr0mZ7mmdXm+Bm1t1+uirP6NXGtHWluk6AuJJFrCxVH+68o9SXKsiRZl+7FwRtMf3bT7zmPkUZaWwu5LQR0DGq5YM1dCpA8Qm0I4BPwnDeLC+DQHHpgmU3jH6xOAe2Ch6DJF+1m3m/JE4hlyBROsg1LBE95VVlfNCwOY8kuASccD0N2v207VhZS+EyRADfDhgG/FSq9HcBDja3lUeUadP+s7mDMhNSfQAqFUCOGkVqQKBgQDs+O/tkTGcouzFMAdXPMdbiITaQsw+FjUJJaegOJq2apqVnpWwtCpiZRZiyKsE17EAs/bwPsjrsxcY2y1AnyEJOcfpw+3N95pWfj1dNgKCuJ95GLrohKlyz9v8l6AhADcjkL/Dc3VbP0R5xARcqFd9qLF2gQe7qu22NydH2Jz7/QKBgQDfqPQueFSRpPMhqpvsD7xQD1QYUgC/BeINy7ceHxPsxMRYxxdgZbWZXoI1H8EYvxG/1uOAJ/vUsQTsV66ruIlKQTOg4Cdc/0q3Dt/sSsVi6dGMiA6HOmZZ4wPeABFlFezQqHnrU/XmwZ4Ym0nnVNaxt2StRPqsL8SFTQbc0lmkCwKBgDcMPO/64oN4dXf5CDuDquvgcrwg+EET944AaUgW3hmx+eb3x94cfBwPT3vzEYc2c6+uHpcAfrwCBkOH5qM0VABIo9/x27QU6I1fFfIpMbwcFy+SRxjseFBraLbsog4IdsaUQXetwn1H3ShMGJdNo8VVAqR8pMc7lazWcxGFiDIhAoGBAMFQYgWMYeQkwzTVJsGpdCVqqk//CMAS0KkF6/dv+yPJisAFDNJ39rdiNX6/PIIsG6ZJkQdjB8p1WoEA5GtuK9PveTVSqRM7YRPKGoc8tVDiMgmaS5xcQLzud5g5TM+nXLGY/RAG2OKjdQdlqUH2VT7z+WO93EBZZS2mPwk8EtizAoGAMAM1B+77Mf1vFRvWhrnlBln0KanMvsrVilTcZ4Q4T/4Vqff8ANGazVD/nyyHpg/R1DTAnFX3RXXObrWyQiaoDbtKvQQT74qtegpJGZDZ8p0B6aTtFSziCEnw0I+mj/DePLEV0HmfKALcsHYj2KbwAFdD0eSvP/fex4hC0aPQL4g=";
    private static final String HOSPITAL_ID = "ytyhd_001";//对接时分配的医院编码
//    static String idcardno = "370631195805066546";

    Gson gson = new Gson();

    public void hzjbxx(){
        log.info("hzjbxx start");
        List<HzjbxxBo> list = chisDao.getHzjbxx();
        String dataList = gson.toJson(list);
        send(dataList,METHOD_HZJBXX);
    }

    public void jybg(){
        log.info("jybg start");
        List<JybgBo> list = clabDao.getJybgBo();
        String dataList = gson.toJson(list);
        send(dataList,METHOD_JYBG);
    }

    public void jybgxq(){
        log.info("jybgxq start");
        List<JybgxqBo> list = clabDao.getJybgxqBo();
        String dataList = gson.toJson(list);
        send(dataList,METHOD_JYBGXQ);
    }

    public void send(String dataList,String methodName) {

//        log.info("json dataList:" + dataList);

        Map<String, String> params = new TreeMap<>();
        long l = System.currentTimeMillis();
        params.put("timestamp", String.valueOf(l));
        params.put("dataList", dataList);

        String content = mapToString(params);
        String sign = null;//对接时分配的私钥
        String encryptStr = null;
        try {
            //2.md5生成签名
            sign = generateSign(content, HOS_PRIVATEKEY);
            //3.rsa加密
            encryptStr = encrypt(content + "&sign=" + sign, COMMON_PUBLIC_KEY);//文档中提供的固定公钥
        } catch (Exception e) {
            log.error("encrypt error",e);
        }
        String param_info = URLEncoder.encode(encryptStr);

        //4.构建requestBody
        MsgRequestBody requestBody = new MsgRequestBody();
        requestBody.setId("1");
        requestBody.setJsonrpc("2.0");
        requestBody.setMethod(methodName);

        List<MsgRequestBody.Param> paramList = new ArrayList<>();
        MsgRequestBody.Param param = new MsgRequestBody.Param();
        param.setParam_info(param_info);
        param.setHospital_id(HOSPITAL_ID);
        paramList.add(param);
        requestBody.setParams(paramList);

        String jsonBody = gson.toJson(requestBody);
        log.info("jsonBody:" + jsonBody);
        //5.post请求
        String ret = netService.batchSavePatient(jsonBody);
        log.info("ret:" + ret);
    }


    //参数转成key=value&key=value,value为空的key和value不拼接
    public static String mapToString(Map<String, String> map) {
        StringJoiner sj = new StringJoiner("&");
        map.forEach((key, value) -> {
            if (!Strings.isBlank(value)) {
                sj.add(key + "=" + value);
            }
        });
        return sj.toString();
    }

    public static String generateSign(String preStr, String privateKeyPKCS8)
            throws Exception {
        String suite = "SHA256WithRSA";

        //初始化算法SHA256
        Signature signature = Signature.getInstance(suite);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyPKCS8));
        //初始化私钥+RSA
        KeyFactory keyFac = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFac.generatePrivate(keySpec);
        signature.initSign(privateKey);

        //待签名字符串转byte数组使用UTF8
        byte[] msgBuf = preStr.getBytes("UTF8");
        signature.update(msgBuf);
        byte[] byteSign = signature.sign();
        //签名值byte数组转字符串用BASE64
        String strSign = Base64.getEncoder().encodeToString(byteSign);
        return strSign;
    }

    public static PublicKey getPublicKey(String publicKey) throws Exception {
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        byte[] decodedKey = base64.decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static String encrypt(String data, String publicKeyStr) throws Exception {
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.getEncoder().encodeToString(encryptedData));
    }
}
