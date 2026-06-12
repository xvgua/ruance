package com.healthassistant.ui;

import com.healthassistant.logic.ReminderManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainWindow extends Application {

    private static final Map<String, String> PAGE_TITLES = new LinkedHashMap<>();
    private static final Map<String, String> PAGE_ICONS = new LinkedHashMap<>();

    static {
        PAGE_TITLES.put("bp", "血压评估");
        PAGE_TITLES.put("bg", "血糖评估");
        PAGE_TITLES.put("reminder", "用药提醒");
        PAGE_TITLES.put("exercise", "运动建议");
        PAGE_TITLES.put("sleep", "睡眠评估");
        PAGE_TITLES.put("ai", "AI健康助手");

        PAGE_ICONS.put("bp", "\u2764\uFE0F");
        PAGE_ICONS.put("bg", "\uD83E\uDE7A");
        PAGE_ICONS.put("reminder", "\uD83D\uDC8A");
        PAGE_ICONS.put("exercise", "\uD83C\uDFC3");
        PAGE_ICONS.put("sleep", "\uD83D\uDCA4");
        PAGE_ICONS.put("ai", "\uD83E\uDD16");
    }

    private final Map<String, StackPane> pages = new LinkedHashMap<>();
    private final Map<String, HBox> navItems = new LinkedHashMap<>();
    private final Map<String, Region> navIndicators = new LinkedHashMap<>();
    private String selectedKey;
    private Label pageIconLabel;
    private Label pageTitleLabel;
    private StackPane pageContainer;
    private ReminderManager reminderManager;

    private BpPage bpPage;
    private BgPage bgPage;
    private ReminderPage reminderPage;
    private ExercisePage exercisePage;
    private SleepPage sleepPage;
    private AIPage aiPage;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("老年人健康管理助手");

        reminderManager = new ReminderManager();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + Styles.COLOR_BG + ";");

        root.setLeft(buildNav());
        root.setCenter(buildContent());

        Scene scene = new Scene(root, 1060, 720);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(640);
        primaryStage.show();

        switchPage("bp");
    }

    private VBox buildNav() {
        VBox nav = new VBox();
        nav.setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
        nav.setPrefWidth(230);

        VBox brandBox = new VBox(Styles.PADDING_XS);
        brandBox.setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
        brandBox.setPadding(new Insets(Styles.PADDING_XL, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));

        HBox brandRow = new HBox(Styles.PADDING_SM);
        brandRow.setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
        brandRow.setAlignment(Pos.CENTER_LEFT);

        Label star = new Label("\u2728");
        star.setFont(Font.font(26));
        star.setStyle("-fx-text-fill: " + Styles.COLOR_NAV_TEXT_ACTIVE + ";");

        Label brandName = new Label("健康助手");
        brandName.setFont(Styles.FONT_HERO);
        brandName.setStyle("-fx-text-fill: " + Styles.COLOR_NAV_TEXT_ACTIVE + ";");

        brandRow.getChildren().addAll(star, brandName);
        brandBox.getChildren().add(brandRow);

        Label subtitle = new Label("老年人健康管理助手");
        subtitle.setFont(Font.font(Styles.FONT_FAMILY, 9));
        subtitle.setStyle("-fx-text-fill: " + Styles.COLOR_NAV_TEXT + ";");
        brandBox.getChildren().add(subtitle);

        nav.getChildren().add(brandBox);

        Region separator = new Region();
        separator.setPrefHeight(1);
        separator.setMaxHeight(1);
        separator.setStyle("-fx-background-color: " + Styles.COLOR_NAV_HOVER + ";");
        nav.getChildren().add(separator);

        VBox navItemsContainer = new VBox(2);
        navItemsContainer.setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
        navItemsContainer.setPadding(new Insets(0, Styles.PADDING_SM, 0, Styles.PADDING_SM));
        navItemsContainer.setFillWidth(true);

        String[][] pageConfig = {
                {"bp", "血压评估"},
                {"bg", "血糖评估"},
                {"reminder", "用药提醒"},
                {"exercise", "运动建议"},
                {"sleep", "睡眠评估"},
                {"ai", "AI健康助手"},
        };

        for (String[] config : pageConfig) {
            String key = config[0];
            String label = config[1];

            HBox itemRow = new HBox();
            itemRow.setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
            itemRow.setAlignment(Pos.CENTER_LEFT);
            itemRow.setCursor(javafx.scene.Cursor.HAND);

            Region indicator = new Region();
            indicator.setPrefWidth(4);
            indicator.setMinWidth(4);
            indicator.setMaxWidth(4);
            indicator.setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
            navIndicators.put(key, indicator);

            String icon = PAGE_ICONS.getOrDefault(key, "");
            Label navLabel = new Label("  " + icon + "  " + label);
            navLabel.setFont(Styles.FONT_NAV);
            navLabel.setStyle("-fx-text-fill: " + Styles.COLOR_NAV_TEXT + "; -fx-background-color: " + Styles.COLOR_NAV_BG + ";");
            navLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
            navLabel.setMaxWidth(Double.MAX_VALUE);
            navLabel.setAlignment(Pos.CENTER_LEFT);
            navLabel.setPadding(new Insets(Styles.PADDING_SM + 2, Styles.PADDING_MD, Styles.PADDING_SM + 2, Styles.PADDING_MD));
            HBox.setHgrow(navLabel, Priority.ALWAYS);

            itemRow.getChildren().addAll(indicator, navLabel);
            itemRow.setHgrow(navLabel, Priority.ALWAYS);

            String k = key;
            itemRow.setOnMouseEntered(e -> {
                if (!k.equals(selectedKey)) {
                    itemRow.setStyle("-fx-background-color: " + Styles.COLOR_NAV_HOVER + ";");
                    navLabel.setStyle("-fx-text-fill: " + Styles.COLOR_NAV_TEXT + "; -fx-background-color: " + Styles.COLOR_NAV_HOVER + ";");
                }
            });
            itemRow.setOnMouseExited(e -> {
                if (!k.equals(selectedKey)) {
                    itemRow.setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
                    navLabel.setStyle("-fx-text-fill: " + Styles.COLOR_NAV_TEXT + "; -fx-background-color: " + Styles.COLOR_NAV_BG + ";");
                }
            });
            itemRow.setOnMouseClicked(e -> switchPage(k));

            navItems.put(key, itemRow);
            navItemsContainer.getChildren().add(itemRow);
        }

        nav.getChildren().add(navItemsContainer);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        spacer.setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
        nav.getChildren().add(spacer);

        Label version = new Label("v1.0");
        version.setFont(Font.font(Styles.FONT_FAMILY, 9));
        version.setStyle("-fx-text-fill: " + Styles.COLOR_NAV_TEXT + "; -fx-background-color: " + Styles.COLOR_NAV_BG + ";");
        version.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));

        HBox footerBox = new HBox();
        footerBox.setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
        footerBox.setAlignment(Pos.CENTER_RIGHT);
        footerBox.getChildren().add(version);
        nav.getChildren().add(footerBox);

        return nav;
    }

    private BorderPane buildContent() {
        BorderPane content = new BorderPane();
        content.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");

        HBox header = new HBox(Styles.PADDING_SM);
        header.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");
        header.setPrefHeight(64);
        header.setMinHeight(64);
        header.setMaxHeight(64);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, Styles.PADDING_XL, 0, Styles.PADDING_XL));

        pageIconLabel = new Label();
        pageIconLabel.setFont(Font.font(22));
        pageIconLabel.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");

        pageTitleLabel = new Label();
        pageTitleLabel.setFont(Styles.FONT_TITLE);
        pageTitleLabel.setStyle("-fx-text-fill: " + Styles.COLOR_PRIMARY + "; -fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");

        header.getChildren().addAll(pageIconLabel, pageTitleLabel);

        Region headerSep = new Region();
        headerSep.setPrefHeight(1);
        headerSep.setMaxHeight(1);
        headerSep.setMinHeight(1);
        headerSep.setStyle("-fx-background-color: " + Styles.COLOR_DIVIDER + ";");

        VBox headerArea = new VBox();
        headerArea.getChildren().addAll(header, headerSep);

        content.setTop(headerArea);

        pageContainer = new StackPane();
        pageContainer.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");
        pageContainer.setPadding(new Insets(Styles.PADDING_MD, Styles.PADDING_XL, Styles.PADDING_MD, Styles.PADDING_XL));

        for (String key : PAGE_TITLES.keySet()) {
            StackPane page = new StackPane();
            page.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");
            pages.put(key, page);
            pageContainer.getChildren().add(page);
            page.setVisible(false);
        }

        content.setCenter(pageContainer);

        bpPage = new BpPage(getPage("bp"));
        bgPage = new BgPage(getPage("bg"));
        reminderPage = new ReminderPage(getPage("reminder"), reminderManager);
        exercisePage = new ExercisePage(getPage("exercise"));
        sleepPage = new SleepPage(getPage("sleep"));
        aiPage = new AIPage(getPage("ai"));

        return content;
    }

    public void switchPage(String key) {
        if (selectedKey != null && navItems.containsKey(selectedKey)) {
            HBox prevItem = navItems.get(selectedKey);
            prevItem.setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
            for (javafx.scene.Node child : prevItem.getChildren()) {
                if (child instanceof Label) {
                    child.setStyle("-fx-text-fill: " + Styles.COLOR_NAV_TEXT + "; -fx-background-color: " + Styles.COLOR_NAV_BG + ";");
                }
            }
            navIndicators.get(selectedKey).setStyle("-fx-background-color: " + Styles.COLOR_NAV_BG + ";");
        }

        if (navItems.containsKey(key)) {
            HBox currItem = navItems.get(key);
            currItem.setStyle("-fx-background-color: " + Styles.COLOR_NAV_ACTIVE + ";");
            for (javafx.scene.Node child : currItem.getChildren()) {
                if (child instanceof Label) {
                    child.setStyle("-fx-text-fill: " + Styles.COLOR_NAV_TEXT_ACTIVE + "; -fx-background-color: " + Styles.COLOR_NAV_ACTIVE + ";");
                }
            }
            navIndicators.get(key).setStyle("-fx-background-color: " + Styles.COLOR_NAV_TEXT_ACTIVE + ";");
        }

        selectedKey = key;

        pageIconLabel.setText(PAGE_ICONS.getOrDefault(key, ""));
        pageTitleLabel.setText(PAGE_TITLES.getOrDefault(key, ""));

        for (Map.Entry<String, StackPane> entry : pages.entrySet()) {
            entry.getValue().setVisible(entry.getKey().equals(key));
        }
    }

    public StackPane getPage(String key) {
        return pages.get(key);
    }

    public ReminderManager getReminderManager() {
        return reminderManager;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
