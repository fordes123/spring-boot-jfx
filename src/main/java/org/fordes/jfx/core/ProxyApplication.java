package org.fordes.jfx.core;

import com.jthemedetecor.OsThemeDetector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.fordes.jfx.annotation.JFXApplication;
import org.fordes.jfx.annotation.Tray;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Collection;

/**
 * 代理主类
 *
 * @author fordes on 2022/8/5
 */
@JFXApplication
public abstract class ProxyApplication {

    public static final FXMLLoader loader = new FXMLLoader();

    @Resource
    public ConfigurableApplicationContext context;

    /**
     * 监听器，当spring容器就绪时执行，开始初始化javafx程序
     *
     * @param event {@link StageReadyEvent}
     */
    @EventListener
    public void handleEvent(StageReadyEvent event) throws IOException, AWTException {
        Stage stage = event.getStage();
        JFXApplication property = this.getClass().getAnnotation(JFXApplication.class);

        //执行前置方法
        this.loadFXMLBefore(stage, property);

        //加载fxml
        Parent root;
        if (isNotEmpty(property.value())) {
            //加载根布局
            loader.setControllerFactory(context::getBean);
            loader.setLocation(context.getResource(property.value()).getURL());
            root = loader.load();
        } else root = new StackPane();

        //加载字体
        if (isNotEmpty(property.fonts())) {
            for (String path : property.fonts()) {
                Font.loadFont(context.getResource(path).getInputStream(), 0);
            }
        }

        //设置程序图标
        if (isNotEmpty(property.icons())) {
            for (String icon : property.icons()) {
                stage.getIcons().add(new Image(context.getResource(icon).getInputStream()));
            }
        }

        //设置样式
        Scene scene = new Scene(root);
        if (isNotEmpty(property.css())) {
            scene.getStylesheets().addAll(property.css());
        }
        if (isNotEmpty(property.style())) {
            stage.initStyle(property.style());
        }
        if (isNotEmpty(property.title())) {
            stage.setTitle(property.title());
        }
        if (isNotEmpty(property.fullScreenExitHint())) {
            stage.setFullScreenExitHint(property.fullScreenExitHint());
        }
        stage.setScene(scene);

        //注册OS主题检测器
        if (property.osThemeDetector()) {
            OsThemeDetector detector = OsThemeDetector.getDetector();
            this.registerOsThemeDetector(detector, stage, property);
        }

        //注册系统托盘
        Tray tray = property.systemTray();
        if (tray != null && tray.value()) {
            System.setProperty("java.awt.headless", String.valueOf(tray.headless()));
            if (SystemTray.isSupported() && tray.image() != null && !tray.image().equals("")) {
                TrayIcon trayIcon = new TrayIcon(ImageIO.read(context.getResource(tray.image()).getInputStream()));
                trayIcon.setToolTip(tray.toolTip());
                trayIcon.setImageAutoSize(tray.imageAutoSize());
                trayIcon.setActionCommand(tray.actionCommand());
                this.initSystemTray(trayIcon, stage, tray);
                SystemTray.getSystemTray().add(trayIcon);
            } else {
                throw new RuntimeException("SystemTray is not support!");
            }
        }

        initAfter(stage);
        event.getStage().show();
    }

    /**
     * 加载fxml之前执行，可覆盖此方法，执行一些前置操作
     *
     * @param stage    {@link Stage}
     * @param property {@link JFXApplication}
     */
    public void loadFXMLBefore(Stage stage, JFXApplication property) {
    }

    /**
     * 初始化全部完成后执行，可扩展此方法，执行一些后置操作
     * @param stage {@link Stage}
     */
    public void initAfter(Stage stage) {}

    /**
     * 注册主题检测器  使用默认策略，可覆盖此类进行自定义
     * 注意：只有在{@link JFXApplication#osThemeDetector}中开启，才会执行此方法
     *
     * @param detector {@link OsThemeDetector}
     * @param stage    {@link Stage}
     * @param property {@link JFXApplication}
     */
    public void registerOsThemeDetector(OsThemeDetector detector, Stage stage, JFXApplication property) {
        if (isNotEmpty(property.darkStyleClass())) {
            //默认监听器，深色模式时为root添加 darkStyleClass
            detector.registerListener(isDark -> {
                if (isDark) {
                    if (!stage.getScene().getRoot().getStyleClass().contains(property.darkStyleClass())) {
                        stage.getScene().getRoot().getStyleClass().add(property.darkStyleClass());
                    }
                } else {
                    stage.getScene().getRoot().getStyleClass().remove(property.darkStyleClass());
                }
            });
        }
    }

    /**
     * 设置托盘图标，可覆盖此方法，对托盘图标进行自定义
     * 注意：需要{@link JFXApplication#systemTray}进行配置并设置了托盘图标{@link Tray#image()}才会执行此方法
     *
     * @param trayIcon {@link TrayIcon}
     * @param stage    {@link Stage}
     * @param property {@link Tray}
     * @throws RuntimeException() SystemTray is not support
     */
    public void initSystemTray(TrayIcon trayIcon, Stage stage, Tray property) {
    }

    /**
     * 简单判断是否不为空
     *
     * @param value value
     * @param <T>   type
     * @return 结果
     */
    static <T> boolean isNotEmpty(T value) {
        if (value == null) return false;
        if (value instanceof String) return !((String) value).isEmpty();
        if (value instanceof Object[]) return ((Object[]) value).length != 0;
        if (value instanceof Collection) return !((Collection<?>) value).isEmpty();
        return true;
    }
}
