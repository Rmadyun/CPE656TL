package TrainNavigationDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackPointReader {
     
    private static final String PointIdColumn = "point_id";
    private static final String PointNameColumn = "point_name";
    private static final String TypeColumn = "type";
    private static final String XColumn = "x";
    private static final String YColumn = "y";
    private static final String ZColumn = "z";
    private static final String BlockIdColumn = "block_id";
    private static final String TagNameColumn = "tag_name";
    
    public static TrackPoint Read(ResultSet resultSet){
   	 TrackPoint trackPoint = null;
   	 
   	 try {
			if(!resultSet.isClosed() && !resultSet.isBeforeFirst() && !resultSet.isAfterLast()){
		
				int pointId = resultSet.getInt(PointIdColumn);
		        String pointName = resultSet.getString(PointNameColumn);
		        String type = resultSet.getString(TypeColumn);
		        double x = resultSet.getDouble(XColumn);
		        double y = resultSet.getDouble(YColumn);
		        double z = resultSet.getDouble(ZColumn);
		        String blockId = resultSet.getString(BlockIdColumn);
		        String tagName = "";
		        
		        try{
		             tagName = resultSet.getString(TagNameColumn);
		        }
		        catch(Exception exception){
		        	
		        }
		        
		        trackPoint = new TrackPoint(pointName, type,
		        		x, y, z, blockId, tagName);
				
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	 
   	 return trackPoint;
    }
    
    /**
     * Searches for the value for a particular column in a database entry 
     * @param databaseEntry Entry to search in
     * @param columnName Name associated with the column
     * @return value of the column. Returns null if not found.
     */
    private static String findColumnValue(DatabaseEntry databaseEntry, String columnName){
   	String columnValue = null;
   	
   	for(KeyValuePair kvp : databaseEntry.getColumns()){
   		if(kvp.getKey().equals(columnName)){
   			columnValue = kvp.getValue();
   			break;
   		}
   	}
   	
   	return columnValue;
    }
    
    public static TrackPoint Read(DatabaseEntry databaseEntry){
   	 TrackPoint trackPoint = null;
   	 
   	 try {		
				int pointId = Integer.parseInt(findColumnValue(databaseEntry, PointIdColumn));
		        String pointName = findColumnValue(databaseEntry, PointNameColumn);
		        String type = findColumnValue(databaseEntry, TypeColumn);
		        double x = Double.parseDouble(findColumnValue(databaseEntry, XColumn));
		        double y = Double.parseDouble(findColumnValue(databaseEntry, YColumn));
		        double z = Double.parseDouble(findColumnValue(databaseEntry, ZColumn));
		        String blockId = findColumnValue(databaseEntry, BlockIdColumn);
		        String tagName = "";
		        
		        try{
		             tagName = findColumnValue(databaseEntry, TagNameColumn);
		        }
		        catch(Exception exception){
		        	
		        }
		        
		        trackPoint = new TrackPoint(pointName, type,
		        		x, y, z, blockId, tagName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	 
   	 return trackPoint;
    }

}
