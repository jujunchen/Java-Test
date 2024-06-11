//package jsoup;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URLDecoder;
//
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//public class jsoupTest {
//	public static void main(String[] args) throws IOException {
////		Connection conn = Jsoup.connect("https://live.douyin.com/314264155008?sem_keyword=douyinzhiboban");
////		byte[] body = conn.execute().bodyAsBytes();
////		InputStream in = new ByteArrayInputStream(body);
////		Document document = Jsoup.parse(in, "GBK", "https://live.douyin.com/314264155008?sem_keyword=douyinzhiboban");
//		//被抖音屏蔽了
//		Document document = Jsoup.connect("https://live.douyin.com/314264155008?sem_keyword=douyinzhiboban").userAgent("Mozilla").get();
//		Element element = document.getElementById("RENDER_DATA");
//		String text =  element.text();
//		String decode =  URLDecoder.decode(text, "UTF-8");
//		System.out.println(decode);
//	}
//}
