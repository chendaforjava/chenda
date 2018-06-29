package cn.jsfund.wechat.module.activity.common;

import java.sql.Blob;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





public class VeinMatchUtils {

private static final Logger log = LoggerFactory.getLogger(VeinMatchUtils.class);
	
	static
	{
		try {
			String SOPath = Thread.currentThread().getContextClassLoader().getResource("/").getPath()+"libJVeinMatchJni.so";
			System.load(SOPath);
		} catch (Throwable e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public byte[] blob2ByteArr(Blob blob) throws Exception {
	     
        byte[] b = null;
        try {	
            if (blob != null) {
                long in = 1;
                b = blob.getBytes(in, (int) (blob.length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("fault");
        }
 
        return b;
    }
	
	public byte[] String2ByteArr(String blob) throws Exception {
	     
        byte[] b = null;
        try {
            if (blob != null) {
                b = blob.getBytes();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("fault");
        }
 
        return b;
    }
	
	/*
	 * 楠岃瘉瀵规瘮
	 */
	public int MatchTemplates(Blob blob1, Blob blob2)
	{
		int ret = -1;
		try {
			
			ret = MatchTemplatesJni(blob2ByteArr(blob1), blob2ByteArr(blob2));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	/*
	 * 楂樼骇楠岃瘉瀵规瘮 锛屾柊鐗堟祴璇�
	 */
	public int MatchTemplatesAIToString(String str1, String  str2,String str3)
	{
		int ret = -1;
		try {
	//		System.out.println("str-->"+StringTool.byteToString(Base64.decodeBase64(str1)));
	//		System.out.println("str2--->"+StringTool.byteToString(Base64.decodeBase64(str2)));
			ret = MatchTemplatesAIJni(Base64.decodeBase64(str1), Base64.decodeBase64(str2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	
	/*
	 * 楂樼骇楠岃瘉瀵规瘮 锛屾柊鐗堟祴璇�
	 */
	public int MatchTemplatesAIEx(String str1, String  str2,byte RegCnt ,byte flag,byte securityLevel,int[] diff,byte[] AIDataBuf , int[] AIDataLen)
	{
		int ret = -1;
		try {
	//		System.out.println("str-->"+StringTool.byteToString(Base64.decodeBase64(str1)));
	//		System.out.println("str2--->"+StringTool.byteToString(Base64.decodeBase64(str2)));
	//		ret = MatchTemplatesAIJni(Base64.decodeBase64(str1), Base64.decodeBase64(str2));
			
	//byte[] i_aBlob2,byte RegCnt, byte flag,
	//		byte securityLevel, int[] diff, byte[] AIDataBuf, int[] AIDataLen	
			byte[] str1byte = Base64.decodeBase64(str1) ;
			if(str1byte.length != 512){
				return ret ;
			}
 			ret = FvmMatchFeature(str1byte, Base64.decodeBase64(str2),RegCnt,  flag,
					securityLevel,  diff,  AIDataBuf,  AIDataLen);
		//	logger.info("return ret="+ret+"  diff="+diff[0]+" AIDataBuf-->"+StringTool.bytesToHexString(AIDataBuf));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ret ;
		}
		
		return ret;
		
	}
	
	public int MatchTemplatesToString(String str1, String  str2)
	{
		int ret = -1;
		try {
		//	System.out.println("str-->"+StringTool.byteToString(Base64.decodeBase64(str1)));
		//	System.out.println("str2--->"+StringTool.byteToString(Base64.decodeBase64(str2)));
			ret = MatchTemplatesAIJni(Base64.decodeBase64(str1), Base64.decodeBase64(str2));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
		
	}
	

	public native int MatchTemplatesJni(byte[] i_aBlob1, byte[] i_aBlob2);
	
	public native int MatchTemplatesAIJni(byte[] i_aBlob1, byte[] i_aBlob2);
	
	public native int FvmMatchFeature(byte[] i_aBlob1, byte[] i_aBlob2,byte RegCnt, byte flag,
			byte securityLevel, int[] diff, byte[] AIDataBuf, int[] AIDataLen);
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.library.path"));
	}
	
	
}
