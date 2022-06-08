import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UpdateDelete {
	
	public static void main(String[] args) throws IOException {
	
	RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
	

	//Way 1 –> Updating a Sample String Value
	UpdateRequest updateRequest = new UpdateRequest("empindex", "002");
	updateRequest.doc("department", "Bussiness");
	UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
	System.out.println("updated response id: "+updateResponse.getId());
	

	
	//Way 2–> Updating Id with particular Map values
    Map<String, Object> updateMap = new HashMap<String, Object>();
								updateMap.put("firstname","Sundar");
								updateMap.put("lastname","Pichai");
								updateMap.put("company","Google");
								updateMap.put("sector","IT");
	

    //update way1 							
	UpdateRequest request = new UpdateRequest("empindex", "002").doc(updateMap);
	UpdateResponse updateResponse1= client.update(request, RequestOptions.DEFAULT);
	System.out.println("updated response id: "+updateResponse1.getId());
	
	//update way2 
	IndexRequest request2 = new IndexRequest("empindex");
	request2.id("001");
	request2.source("company","SpaceX");
	IndexResponse indexResponse = client.index(request2, RequestOptions.DEFAULT);
	System.out.println("response id: "+indexResponse.getId());
			System.out.println(indexResponse.getResult().name());
			
	//update way3  
	Map<String, Object> updateMap2 = new HashMap<String, Object>();
	updateMap2.put("firstname","Sundar");
	updateMap2.put("lastname","Pichai");
	updateMap2.put("company","Google");
	updateMap2.put("sector","IT");
	IndexRequest request3 = new IndexRequest("empindex");
	request3.id("002");
	request3.source(updateMap2);
	IndexResponse indexResponseUpdate = client.index(request3, RequestOptions.DEFAULT);
	System.out.println("response id: "+indexResponseUpdate.getId());		
	System.out.println(indexResponseUpdate.getResult().name());
	
	
	
	//Way 3 –> Inserting POJO mappings data
	//Updating to ElasticSearch for particular Uniquid with POJO
    EmployePojo emp = new EmployePojo("Shiva", "Henary",  LocalDate.now() );
	IndexRequest request4 = new IndexRequest("employeeindex");
	request4.id("001");
	request4.source(new ObjectMapper().writeValueAsString(emp), XContentType.JSON);
	IndexResponse indexResponse2 = client.index(request4, RequestOptions.DEFAULT);
	System.out.println("response id:" +indexResponse2.getId());		
	System.out.println(indexResponse2.getResult().name());
	
	
	
	//Using API- UpdateByQueryRequest
    Map<String, Object> updateMap3 = new HashMap<String, Object>();							
	updateMap3.put("firstname","Sundar01");
	updateMap3.put("lastname","Pichai01");
	UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest("empindex");
	updateByQueryRequest.setConflicts("proceed");
	updateByQueryRequest.setQuery(new TermQueryBuilder("_id", "001"));
	Script script = new Script(ScriptType.INLINE, "painless","ctx._source = params",updateMap3);
	updateByQueryRequest.setScript(script); 

	try {
	BulkByScrollResponse bulkResponse = client.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
	long totalDocs = bulkResponse.getTotal();
	System.out.println("updated response id: "+totalDocs);
	} catch (IOException e) {
		e.printStackTrace();

	}
	
//===========================================================================================================================	
	
	//Delete Operations
	
	//Delete particular record
	DeleteRequest deleteRequest = new DeleteRequest("empindex","002");
	DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
    System.out.println("response id: "+deleteResponse.getId());
    
  
    
    //2.Delete entire index

    //Delete the index
   DeleteIndexRequest request5 = new DeleteIndexRequest("employeeindex");
   client.indices().delete(request5, null);
   System.out.println("Delete Done ");
	
	
	}
}