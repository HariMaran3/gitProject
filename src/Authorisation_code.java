import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class Authorisation_code {

	@Test
	public void maintesting() throws InterruptedException {
		
//		System.setProperty("webdriver.gecko.driver", ".\\driver\\geckodriver.exe");
//		FirefoxDriver driver = new FirefoxDriver();
//		
		System.setProperty("webdriver.chrome.driver", ".\\driver\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		
		
		driver.manage().window().maximize();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		
		WebElement Email = driver.findElement(By.xpath("//input[@type='email']"));
		Email.sendKeys("maranhari7@gmail.com");
		
		driver.findElement(By.xpath("//*[text()='Next']")).click();
		

		
		
//		WebDriverWait wait = new WebDriverWait(driver,50);
//		WebElement Pass = driver.findElement(By.xpath("//a[text()='privacy policy']"));
//		
//		wait.until(ExpectedConditions.elementToBeClickable(Pass));
//		
		Thread.sleep(10000);
		WebElement PassWord = driver.findElement(By.xpath("//input[@type='password']"));
		PassWord.sendKeys("Lalithamaran97");
		
		driver.findElement(By.xpath("//*[text()='Next']")).click();
		Thread.sleep(5000);
		
		String current_url = driver.getCurrentUrl();
		
		System.out.println(current_url);
		
		String[] temp = current_url.split("code=");
		String[] temp1 = temp[1].split("&");
		String authorisation_code = temp1[0];
		
		System.out.println(authorisation_code);

		

		
		String access_res = given().urlEncodingEnabled(false)
		.queryParam("code", authorisation_code)
		.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParam("grant_type", "authorization_code")
		.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		.when().log().all().post("https://www.googleapis.com/oauth2/v4/token")
		.asString();
		
		JsonPath js = new JsonPath(access_res);
		String access_token = js.getString("access_token");
		System.out.println(access_token);
		
		String result = given().queryParam("access_token", access_token).when()
				.get("https://rahulshettyacademy.com/getCourse.php").asString();
		
		System.out.println(result);
		
	}

}
