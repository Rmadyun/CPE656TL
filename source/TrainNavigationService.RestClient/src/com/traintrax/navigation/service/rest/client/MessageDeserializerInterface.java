package com.traintrax.navigation.service.rest.client;
/**
 * Interface responsible for deserializing strings into class
 * objects
 * @author Corey Sanders
 *
 */
public interface MessageDeserializerInterface<TMessage> {

	/**
	 * Decodes a JSON string into a repository message
	 * @param jsonString String to decode
	 * @return Deserialized representation of the repository message.
	 */
	public TMessage deserialize(String jsonString);
	
}
