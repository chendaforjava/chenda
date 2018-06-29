package com.vein.dao;

public interface RedisCacheDao {
	
	public void saveObj2Map(final String mainKey,final String key,final byte[] b);
	
	public Object getObjfromReds(final String mainKey,final String key);
	
	public void saveObj2Set(final String key);
	
	public Object getObjFromSet(final String key);
	
	public Object getAllObjFromMap(final String key);
	
	
	public void saveObj2List(final String mainKey,final byte[] b);
	
	public void delKey(final String key);
	
	public Object getObjeFromList(final String key);
	
	public void delMapKey(final String mainKey,final String key);
	
	public Object getListLength(final String mainKey);
	
	public Object lpopList(final String mainKey);
}
