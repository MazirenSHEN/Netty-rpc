//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Test;
//
//import com.sjj.netty.client.ClientRequest;
//import com.sjj.netty.client.TcpClient;
//import com.sjj.netty.util.Response;
//import com.sjj.user.bean.User;
//
//public class TestTcp {
//
//	@Test
//	public void testGetResponse() {
//		ClientRequest request = new ClientRequest();
//		request.setContent("test tcp long connect request");
//		Response resp = TcpClient.send(request);
//		System.out.println(resp.getResult());
//		
//	}
//	
//	@Test
//	public void testSaveUser() {
//		ClientRequest request = new ClientRequest();
//		User u =new User();
//		u.setId(1);
//		u.setName("zhangsan");
//		request.setCommand("com.sjj.user.controller.UserController.saveUser");
//		request.setContent(u);
//		Response resp = TcpClient.send(request);
//		System.out.println(resp.getResult());
//	}
//	
//	@Test
//	public void testSaveUsers() {
//		ClientRequest request = new ClientRequest();
//		List<User> users = new ArrayList<User>();
//		User u =new User();
//		u.setId(1);
//		u.setName("张三");
//		users.add(u);
//		request.setCommand("com.sjj.user.controller.UserController.saveUsers");
//		request.setContent(users);
//		Response resp = TcpClient.send(request);
//		System.out.println(resp.getResult());
//	}
//}
