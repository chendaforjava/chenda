package com.vein.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import com.vein.bean.User;
import com.vein.bean.VeinFinger;
import com.vein.util.SerializeUtil;



@SuppressWarnings("unchecked")
@Repository
public class RedisCacheDaoImpl implements RedisCacheDao{
	
	@Autowired(required=false)
	RedisTemplate<Object,Object> redisTemplate;
	
	
	//保存为hash
	@Override
	public void saveObj2Map(final String mainKey,final String key,final byte[] b) {
		redisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				 RedisSerializer<String> serializer = getRedisSerializer();
				 byte[] mainKeyByte = serializer.serialize(mainKey);
				 byte[] keyByte = serializer.serialize(key);
				 connection.hSet(mainKeyByte, keyByte, b);
				return 1L;
			}
			
		});
	}
	

	public Object getObjfromReds(final String mainKey,final String key){
		Object value = redisTemplate.execute(new RedisCallback(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] mainKeyByte = serializer.serialize(mainKey);
				byte[] keyByte = serializer.serialize(key);
				byte[] valueByte = connection.hGet(mainKeyByte, keyByte);
				Object value = redisTemplate.getStringSerializer().deserialize(valueByte);
				return value;
			}
		});
		return value;
	}
	
	

	protected RedisSerializer<String> getRedisSerializer() { 
	    return redisTemplate.getStringSerializer(); 
	}


	@Override
	public void saveObj2Set(final String key) {
		Object value = redisTemplate.execute(new RedisCallback(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] keyByte = serializer.serialize(key);
				connection.set(keyByte, keyByte);
				connection.expire(keyByte, 180);
				return 1L;
			}
		});
	}


	@Override
	public Object getObjFromSet(final String key) {
		Object value = redisTemplate.execute(new RedisCallback(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] mainKeyByte = serializer.serialize(key);
				byte[] valueByte = connection.get(mainKeyByte);
				if(valueByte!=null && valueByte.length>0){
					Object value = redisTemplate.getStringSerializer().deserialize(valueByte);
					return value;
				}else{
					return null;
				}
			}
		});
		return value;
	}


	@Override
	public Object getAllObjFromMap(final String key) {
		Object value = redisTemplate.execute(new RedisCallback(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				Map<byte[],byte[]> map = new HashMap<byte[],byte[]>();
				Map<String,VeinFinger> veinMap = new HashMap<String,VeinFinger>();
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] mainKeyByte = serializer.serialize(key);
				map = connection.hGetAll(mainKeyByte);
				if(map.size()>0){
					Set<Entry<byte[],byte[]>> entrySet = map.entrySet();
					for(Entry<byte[],byte[]> entry:entrySet){
						String veinId = (String)redisTemplate.getHashKeySerializer().deserialize(entry.getKey());
						VeinFinger veinFinger = (VeinFinger)SerializeUtil.unserialize(entry.getValue());
						veinMap.put(veinId, veinFinger);
					}
					return veinMap;
				}else{
					return null;
				}
			}
		});
		return value;
	}


	@Override
	public void saveObj2List(final String mainKey, final byte[] b) {
		Object value = redisTemplate.execute(new RedisCallback(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] keyByte = serializer.serialize(mainKey);
				return connection.lPush(keyByte, b);
			}
		});
	}


	@Override
	public void delKey(final String key) {
		Object value = redisTemplate.execute(new RedisCallback(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] keyByte = serializer.serialize(key);
				return connection.del(keyByte);
			}
		});
	}


	@Override
	public Object getObjeFromList(final String key) {
		Object value = redisTemplate.execute(new RedisCallback(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				List<byte[]> list = new ArrayList<byte[]>();
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] keyByte = serializer.serialize(key);
				list = connection.lRange(keyByte, 0, -1);
				return list;
			}
		});
		return value;
	}


	@Override
	public void delMapKey(final String mainKey,final String key) {
		Object value = redisTemplate.execute(new RedisCallback(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] mainkeyByte = serializer.serialize(mainKey);
				byte[] keybyte = serializer.serialize(key);
				return connection.hDel(mainkeyByte, keybyte);
			}
		});
		
	}
	
	public Object getListLength(final String mainKey){
		Object value = redisTemplate.execute(new RedisCallback(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] mainkeyByte = serializer.serialize(mainKey);
				return connection.lLen(mainkeyByte);
			}
		});
		
		return value;
	}
	
	
	public Object lpopList(final String mainKey){
		Object value = redisTemplate.execute(new RedisCallback(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] mainkeyByte = serializer.serialize(mainKey);
				return connection.lPop(mainkeyByte);
			}
		});
		return value;
	}
	
}
