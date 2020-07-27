package naver2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dao.UserDAOImpl;
import dto.DataDTO;

//네이버 검색 API 예제 - blog 검색
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ApiExamSearchBlog {

 public static void main(String[] args) {
     String clientId = ""; //애플리케이션 클라이언트 아이디값"
     String clientSecret = ""; //애플리케이션 클라이언트 시크릿값"

     String text = null;
     try {
         text = URLEncoder.encode("부산 날씨", "UTF-8");
     } catch (UnsupportedEncodingException e) {
         throw new RuntimeException("검색어 인코딩 실패",e);
     }

     String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text;    // json 결과
     //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과

     Map<String, String> requestHeaders = new HashMap<>();
     requestHeaders.put("X-Naver-Client-Id", clientId);
     requestHeaders.put("X-Naver-Client-Secret", clientSecret);
     String responseBody = get(apiURL,requestHeaders);

     String jsonData = responseBody;
     
     UserDAOImpl userDao = new UserDAOImpl();
     DataDTO dto = null;
         
     try {
    	 
         
         JSONParser jsonParse = new JSONParser();
         
         //JSONParse에 json데이터를 넣어 파싱한 다음 JSONObject로 변환한다.
         JSONObject jsonObj = (JSONObject) jsonParse.parse(jsonData);
         
         //JSONObject에서 PersonsArray를 get하여 JSONArray에 저장한다.
         JSONArray personArray = (JSONArray) jsonObj.get("items");
         
         for(int i=0; i < personArray.size(); i++) {
        	 dto = new DataDTO();
             JSONObject personObject = (JSONObject) personArray.get(i);
             
             dto.setTitle(personObject.get("title"));
             dto.setOriginallink(personObject.get("originallink"));
             dto.setLink(personObject.get("link"));
             dto.setDescription(personObject.get("description"));
             dto.setPubDate(personObject.get("pubDate"));
                             
             userDao.insert(dto);
         }

     } catch (ParseException e) {
         e.printStackTrace();
     }
     
 }
 
 


 private static String get(String apiUrl, Map<String, String> requestHeaders){
     HttpURLConnection con = connect(apiUrl);
     try {
         con.setRequestMethod("GET");
         for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
             con.setRequestProperty(header.getKey(), header.getValue());
         }

         int responseCode = con.getResponseCode();
         if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
             return readBody(con.getInputStream());
         } else { // 에러 발생
             return readBody(con.getErrorStream());
         }
     } catch (IOException e) {
         throw new RuntimeException("API 요청과 응답 실패", e);
     } finally {
         con.disconnect();
     }
 }

 private static HttpURLConnection connect(String apiUrl){
     try {
         URL url = new URL(apiUrl);
         return (HttpURLConnection)url.openConnection();
     } catch (MalformedURLException e) {
         throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
     } catch (IOException e) {
         throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
     }
 }

 private static String readBody(InputStream body){
     InputStreamReader streamReader = new InputStreamReader(body);

     try (BufferedReader lineReader = new BufferedReader(streamReader)) {
         StringBuilder responseBody = new StringBuilder();

         String line;
         while ((line = lineReader.readLine()) != null) {
             responseBody.append(line);
         }

         return responseBody.toString();
     } catch (IOException e) {
         throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
     }
 }
}