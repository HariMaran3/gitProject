package responseBuilder;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.Api;
import pojo.MainPojo;

public class Builder {
	
	@Test	
	public static void main(String[] args) throws InterruptedException,MismatchedInputException {
		
		
		System.setProperty("webdriver.chrome.driver", ".\\driver\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		
		WebElement mail = driver.findElement(By.xpath("//input[@type='email']"));
		mail.sendKeys("maranhari7");
		
		driver.findElement(By.xpath("//*[text()='Next']")).click();
		
		Thread.sleep(9000);
		
		WebElement pass = driver.findElement(By.xpath("//input[@type='password']"));
		pass.sendKeys("Lalithamaran97");
		
		driver.findElement(By.xpath("//*[text()='Next']")).click();
		
		Thread.sleep(5000);
		String url = driver.getCurrentUrl();
		
		String[] temp = url.split("code=");
		String[] temp1 = temp[1].split("&");
		String code = temp1[0];
		System.out.println(code);
		
		
		// Builder for request an response
		RequestSpecification request = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/getCourse.php").addQueryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
			.addQueryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
			.addQueryParam("code", code).build();
			
		
	ResponseSpecification response = new ResponseSpecBuilder().expectStatusCode(200).build();
		
	
	//End
	
//		String access = given().spec(request).when().post("https://www.googleapis.com/oauth2/v4/token")
//				.then().spec(response).extract().response().asString();
////	
		String res = given().urlEncodingEnabled(false).queryParam("code",code).queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParam("grant_type", "authorization_code")
				.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.when().post("https://www.googleapis.com/oauth2/v4/token").then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		
		
		System.out.println(res);
		
		JsonPath js = new JsonPath(res);
		String token = js.getString("access_token");
		
		System.out.println(token );

		Response response1 = given().queryParam("access_token", token).expect().defaultParser(Parser.JSON)
				.when().get("https://rahulshettyacademy.com/getCourse.php")
				.then().statusCode(200).extract().response();
		
		MainPojo result = response1.as(MainPojo.class);
		
		List<Api> contentsOfApi= result.getCourses().getApi();
		
		for(int i=0; i<contentsOfApi.size(); i++) {
		

			if(contentsOfApi.get(i).getCourseTitle().equalsIgnoreCase("Rest Assured Automation using Java")) {
				
				int Price = Integer.parseInt(contentsOfApi.get(i).getPrice());
				System.out.println("Total Price : "+Price*100);
			}
	
		
		}
		
		
		System.out.println("success");
	}

}
