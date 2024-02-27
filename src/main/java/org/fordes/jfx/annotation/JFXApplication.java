package org.fordes.jfx.annotation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JFXApplication {

    /**
     * fxml文件相对路径
     * {@link FXMLLoader#load()}
     */
    String value() default "";

    /**
     * css 文件，支持http以及相对路径
     * {@link Scene#getStylesheets()}
     */
    String[] css() default {};

    /**
     * 标题 {@link Stage#setTitle(String)}
     */
    String title() default "";

    /**
     * Stage样式 {@link StageStyle}
     */
    StageStyle style() default StageStyle.DECORATED;

    /**
     * 是否启用os主题检测器 {@link com.jthemedetecor.OsThemeDetector}
     */
    boolean osThemeDetector() default false;

    /**
     * 深色模式 StyleClass，当启用{@link JFXApplication#osThemeDetector()}时生效，自动切换
     */
    String darkStyleClass() default "";

    /**
     * 需要加载的字体相对路径，支持http (更推荐使用css引入字体)
     * {@link javafx.scene.text.Font#loadFont(String, double)}
     */
    String[] fonts() default {};

    /**
     * 应用图标，相对路径或http地址
     * {@link Stage#getIcons()}
     */
    String[] icons() default {};

    /**
     * 全屏退出提示消息
     * {@link Stage#setFullScreenExitHint(String)}
     */
    String fullScreenExitHint() default "";

    /**
     * 系统托盘 {@link Tray}
     */
    Tray systemTray() default @Tray;
}
