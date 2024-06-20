package jenkins;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class JenkinsTaskCreator {
    public static void main(String[] args) throws InterruptedException {
        // 设置Chrome浏览器驱动路径
        System.setProperty("webdriver.chrome.driver", "/Users/chenjujun/java-projects/Java-Test/src/main/resources/chromedriver");

        // 创建Chrome浏览器驱动
        WebDriver driver = new ChromeDriver();

        // 访问Jenkins登录页
        driver.get("url");

        // 查找并填写用户名和密码表单，点击提交按钮
        WebElement usernameInput = driver.findElement(By.id("j_username"));
        WebElement passwordInput = driver.findElement(By.name("j_password"));
        WebElement submitButton = driver.findElement(By.name("Submit"));
        usernameInput.sendKeys("user");
        passwordInput.sendKeys("password");
        submitButton.click();

        // 等待Jenkins页面加载完成
        Thread.sleep(5000);

        // 访问Jenkins创建任务页面
        driver.get("url/newJob");

        // 查找并填写任务名称和描述表单，选择项目类型和源码管理方式，点击保存按钮
        WebElement nameInput = driver.findElement(By.name("name"));
        WebElement fromInput = driver.findElement(By.name("from"));
//        WebElement descriptionInput = driver.findElement(By.name("description"));
//        WebElement typeSelect = driver.findElement(By.name("mode"));
//        WebElement scmSelect = driver.findElement(By.name("scm"));
        WebElement saveButton = driver.findElement(new By.ById("ok-button"));
        nameInput.sendKeys("phantom-admin-test");
        fromInput.sendKeys("uat-paas-phantom-admin");
//        descriptionInput.sendKeys("A new task created by Selenium");
//        typeSelect.sendKeys("freestyle");
//        scmSelect.sendKeys("git");
        saveButton.click();

        // 等待创建任务完成
        Thread.sleep(5000);

        // 关闭浏览器
        driver.quit();
    }
}
