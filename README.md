# Simple SpringBoot JavaFX Launcher

这是一个简单的脚手架，有助于快速创建集成 `SpringBoot` 的 `JavaFX` 应用程序

## 基本示例

```java

@JFXApplication
@SpringBootApplication
public class ExampleApplication extends ProxyApplication {

  public static void main(String[] args) {
    ProxyLauncher.run(ExampleApplication.class, args);
  }
}
```

## 入门

### 1、 创建一个典型的 `Spring Boot` 项目
### 2、 引入 `springboot-jfx` 依赖
### 3、 改写启动类

- 添加注解
  - 添加 `@JFXApplication` 注解到类上，该注解用于设置初始化属性，其属性如下：
    |属性|类型|默认值|描述|
    |--|--|--|--|
    |`vlaue`|`String`||fxml文件路径|
    |`css`|`String[]`|`[]`|css样式表路径|
    |`title`|`String`||窗体标题|
    |`style`|`StageStyle`|`StageStyle.DECORATED`|Stage样式|
    |`osThemeDetector`|`boolean`|`false`|是否启用os主题检测器|
    |`darkStyleClass`|`String`||深色模式 StyleClass|
    |`fonts`|`String[]`|`[]`|待加载的字体文件|
    |`icons`|`String[]`|`[]`|待加载的图标|
    |`fullScreenExitHint`|`String[]`||全屏退出提示消息|
    |`systemTray`|`Tray`||系统托盘|

- 继承 `ProxyApplication`
  - 该抽象类用于在启动时进行初始化，可根据需求扩展其中方法，以实现定制化

- 替换启动入口
  - 将 `main` 函数中 `SpringApplication.run()` 更改为 `ProxyLauncher.run()`

- 启动程序

## 使用的库

- [Spring Boot](https://github.com/spring-projects/spring-boot)
- [OpenJFX](https://github.com/openjdk/jfx)
- [jSystemThemeDetector](https://github.com/Dansoftowner/jSystemThemeDetector)

## 开源许可

- [MIT license](https://opensource.org/licenses/MIT)