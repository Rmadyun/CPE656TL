package edu.uah.cpe.traintrax;
import com.google.gson.Gson;

/**
 * JSON implementation of a message deserializer
 * @author Corey Sanders
 *
 * @param <TRepositoryMessage> Type of class to deserialize messages into.
 */
public class JsonRepositoryMessageDeserializer <TRepositoryMessage> implements MessageDeserializerInterface<TRepositoryMessage> {
	
	private Class<TRepositoryMessage> classType;

	/**
	 * Decodes JSON strings of a particular type of class
	 * @param classType class information for the type of class being used.
	 */
	public JsonRepositoryMessageDeserializer(Class<TRepositoryMessage> classType){
		this.classType = classType;
	}
	
	/**
	 * Decodes a JSON string into a repository message
	 * @param jsonString String to decode
	 * @return Deserialized representation of the repository message.
	 */
	public TRepositoryMessage deserialize(String jsonString){
		TRepositoryMessage response = null;
		Gson gson = new Gson();
		response = gson.fromJson(jsonString, classType);
		return response;
	}

}
