import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sjj.consumer.client.annonation.RemoteInvoke;
import com.sjj.consumer.client.param.Response;
import com.sjj.user.model.User;
import com.sjj.user.remote.UserRemote;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=RemoteInvokeTest.class)
@ComponentScan("\\")
public class RemoteInvokeTest {
	
	@RemoteInvoke
	public UserRemote userremote;
	
	
	@Test
	public void testSaveUser(){
		
//		for(int i=0;i<100;i++){
			User user = new User();
			user.setId(100);
			user.setName("张三");
			Response response = userremote.saveUser(user);
			System.out.println("成功了");
//		}
		
//		System.out.println("100个请求完毕");
		
		
//		Response response = NettyClient.send(clientRequest);
		System.out.println(response.getResult());
	}
}
