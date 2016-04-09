package com.traintrax.navigation.service.rest.client;
import com.google.gson.Gson;

/**
 * JSON implementation of a message deserializer
 * @author Corey Sanders
 *
 * @param <TMessage> Type of class to deserialize messages into.
 */
public class JsonMessageDeserializer <TMessage> implements MessageDeserializerInterface<TMessage> {
	
	private Class<TMessage> classType;

	/**
	 * Decodes JSON strings of a particular type of class
	 * @param classType class information for the type of class being used.
	 */
	public JsonMessageDeserializer(Class<TMessage> classType){
		this.classType = classType;
	}
	
	/**
	 * Decodes a JSON string into a repository message
	 * @param jsonString String to decode
	 * @return Deserialized representation of the repository message.
	 */
	public TMessage deserialize(String jsonString){
		TMessage response = null;
		Gson gson = new Gson();
		response = gson.fromJson(jsonString, classType);
		return response;
	}

}
