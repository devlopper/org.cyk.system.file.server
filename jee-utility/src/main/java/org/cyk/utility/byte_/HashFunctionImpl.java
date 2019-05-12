package org.cyk.utility.byte_;

import java.io.Serializable;
import java.security.MessageDigest;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.cyk.utility.function.AbstractFunctionWithPropertiesAsInputImpl;

public class HashFunctionImpl extends AbstractFunctionWithPropertiesAsInputImpl<String> implements HashFunction,Serializable {
	private static final long serialVersionUID = 1L;

	private String algorithm;
	private byte[] bytes;
	
	@Override
	protected String __execute__() throws Exception {
		byte[] bytes = __injectValueHelper__().returnOrThrowIfBlank("hash function bytes", getBytes());
		String algorithm = __injectValueHelper__().returnOrThrowIfBlank("hash function algorithm", getAlgorithm());
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		messageDigest.update(bytes);
		return new HexBinaryAdapter().marshal(messageDigest.digest());
		
		/*
	    try (InputStream input = new FileInputStream(file)) {

	        byte[] bytes = new byte[8192];
	        int numberOfBytesRead = input.read(bytes);

	        while (numberOfBytesRead != -1) {
	        	messageDigest.update(bytes, 0, numberOfBytesRead);
	            numberOfBytesRead = input.read(bytes);
	        }

	        return new HexBinaryAdapter().marshal(messageDigest.digest());
	    }
		return super.__execute__();
		*/
	}
	
	@Override
	public String getAlgorithm() {
		return algorithm;
	}

	@Override
	public HashFunction setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
		return this;
	}
	
	@Override
	public byte[] getBytes() {
		return bytes;
	}
	
	@Override
	public HashFunction setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}

}