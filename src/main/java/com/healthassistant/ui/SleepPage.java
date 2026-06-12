package com.healthassistant.ui;

import com.healthassistant.logic.SleepEvaluator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Map;

public class SleepPage {

    private static final Map<String, LevelConfig> LEVEL_CONFIG = Map.of(
            SleepEvaluator.LEVEL_SUFFICIENT, new LevelConfig(Styles.COLOR_SUCCESS, Styles.COLOR_SUCCESS_LIGHT, "\u2705"),
            SleepEvaluator.LEVEL_NORMAL, new LevelConfig(Styles.COLOR_WARNING, Styles.COLOR_WARNING_LIGHT, "\u26A0\uFE0F"),
            SleepEvaluator.LEVEL_INSUFFICIENT, new LevelConfig(Styles.COLOR_DANGER, Styles.COLOR_DANGER_LIGHT, "\u274C")
    );

    private final StackPane parent;
    private final Spinner<Integer> sleepHourSpinner;
    private final Spinner<Integer> sleepMinSpinner;
    private final Spinner<Integer> wakeHourSpinner;
    private final Spinner<Integer> wakeMinSpinner;
    private final Slider scoreSlider;
    private final Label scoreValueLabel;
    private final VBox resultContainer;

    public SleepPage(StackPane parent) {
        this.parent = parent;
        this.sleepHourSpinner = new Spinner<>(0, 23, 22);
        this.sleepMinSpinner = new Spinner<>(0, 59, 0);
        this.wakeHourSpinner = new Spinner<>(0, 23, 6);
        this.wakeMinSpinner = new Spinner<>(0, 59, 30);
        this.scoreSlider = new Slider(1, 5, 3);
        this.scoreValueLabel = new Label("3");
        this.resultContainer = new VBox(Styles.PADDING_MD);
        this.resultContainer.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");
        build();
    }

    private void build() {
        VBox frame = new VBox(Styles.PADDING_MD);
        frame.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");
        frame.setFillWidth(true);

        VBox card = createCard();
        card.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("\uD83D\uDCA4  睡眠评估");
        title.setFont(Styles.FONT_TITLE);
        title.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
        title.setPadding(new Insets(Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_XS, Styles.PADDING_LG));

        Label desc = new Label("请填写您的睡眠时间和质量自评");
        desc.setFont(Styles.FONT_SUBTITLE);
        desc.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
        desc.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));

        VBox formBox = new VBox(Styles.PADDING_MD);
        formBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        formBox.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));

        HBox timeRow = new HBox(Styles.PADDING_XL);
        timeRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        VBox leftCol = new VBox(Styles.PADDING_XS);
        leftCol.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        Label sleepLabel = new Label("\uD83C\uDF19  入睡时间");
        sleepLabel.setFont(Styles.FONT_LABEL);
        sleepLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        HBox sleepRow = new HBox(Styles.PADDING_SM);
        sleepRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        sleepRow.setAlignment(Pos.CENTER_LEFT);

        sleepHourSpinner.setEditable(true);
        sleepHourSpinner.setPrefWidth(80);
        sleepMinSpinner.setEditable(true);
        sleepMinSpinner.setPrefWidth(80);

        Label hourLabel1 = new Label(" 时 ");
        hourLabel1.setFont(Styles.FONT_LABEL);
        hourLabel1.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        Label minLabel1 = new Label(" 分");
        minLabel1.setFont(Styles.FONT_SMALL);
        minLabel1.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");

        sleepRow.getChildren().addAll(sleepHourSpinner, hourLabel1, sleepMinSpinner, minLabel1);
        leftCol.getChildren().addAll(sleepLabel, sleepRow);

        VBox rightCol = new VBox(Styles.PADDING_XS);
        rightCol.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        Label wakeLabel = new Label("\u2600\uFE0F  起床时间");
        wakeLabel.setFont(Styles.FONT_LABEL);
        wakeLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        HBox wakeRow = new HBox(Styles.PADDING_SM);
        wakeRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        wakeRow.setAlignment(Pos.CENTER_LEFT);

        wakeHourSpinner.setEditable(true);
        wakeHourSpinner.setPrefWidth(80);
        wakeMinSpinner.setEditable(true);
        wakeMinSpinner.setPrefWidth(80);

        Label hourLabel2 = new Label(" 时 ");
        hourLabel2.setFont(Styles.FONT_LABEL);
        hourLabel2.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        Label minLabel2 = new Label(" 分");
        minLabel2.setFont(Styles.FONT_SMALL);
        minLabel2.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");

        wakeRow.getChildren().addAll(wakeHourSpinner, hourLabel2, wakeMinSpinner, minLabel2);
        rightCol.getChildren().addAll(wakeLabel, wakeRow);

        timeRow.getChildren().addAll(leftCol, rightCol);

        VBox scoreBox = new VBox(Styles.PADDING_XS);
        scoreBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        scoreBox.setPadding(new Insets(0, 0, Styles.PADDING_SM, 0));

        Label scoreLabel = new Label("\u2B50  睡眠质量自评");
        scoreLabel.setFont(Styles.FONT_LABEL);
        scoreLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        HBox scoreRow = new HBox(Styles.PADDING_MD);
        scoreRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        scoreRow.setAlignment(Pos.CENTER_LEFT);

        scoreSlider.setPrefWidth(240);
        scoreSlider.setMajorTickUnit(1);
        scoreSlider.setMinorTickCount(0);
        scoreSlider.setSnapToTicks(true);
        scoreSlider.setShowTickMarks(true);
        scoreSlider.setShowTickLabels(true);
        scoreSlider.valueProperty().addListener((obs, old, val) ->
                scoreValueLabel.setText(Integer.toString(val.intValue())));

        scoreValueLabel.setFont(Font.font(Styles.FONT_FAMILY, 16));
        scoreValueLabel.setStyle("-fx-text-fill: " + Styles.COLOR_PRIMARY + "; -fx-font-weight: bold;");
        scoreValueLabel.setPadding(new Insets(0, 0, 0, Styles.PADDING_MD));

        VBox scoreLabels = new VBox(Styles.PADDING_XS);
        scoreLabels.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        Label lowLabel = new Label("1 \uD83D\uDE1E");
        lowLabel.setFont(Font.font(Styles.FONT_FAMILY, 10));
        lowLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");
        Label highLabel = new Label("5 \uD83D\uDE0A");
        highLabel.setFont(Font.font(Styles.FONT_FAMILY, 10));
        highLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");
        scoreLabels.getChildren().addAll(lowLabel, highLabel);

        scoreRow.getChildren().addAll(scoreSlider, scoreValueLabel, scoreLabels);
        scoreBox.getChildren().addAll(scoreLabel, scoreRow);

        formBox.getChildren().addAll(timeRow, scoreBox);

        Button evalBtn = new Button("  \uD83D\uDD0D  开始评估");
        evalBtn.setFont(Styles.FONT_BUTTON);
        evalBtn.setStyle("-fx-background-color: " + Styles.COLOR_PRIMARY + "; -fx-text-fill: white; -fx-background-radius: 4;");
        evalBtn.setPrefWidth(160);
        evalBtn.setPadding(new Insets(Styles.PADDING_SM + 2, Styles.PADDING_LG, Styles.PADDING_SM + 2, Styles.PADDING_LG));
        evalBtn.setOnAction(e -> evaluate());

        HBox btnBox = new HBox(evalBtn);
        btnBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));

        card.getChildren().addAll(title, desc, formBox, btnBox);

        ScrollPane scrollPane = new ScrollPane(resultContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + "; -fx-background: " + Styles.COLOR_CONTENT_BG + ";");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        frame.getChildren().addAll(card, scrollPane);
        parent.getChildren().add(frame);
    }

    private VBox createCard() {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + "; -fx-border-color: " + Styles.COLOR_CARD_BORDER + "; -fx-border-radius: " + Styles.RADIUS_LG + "; -fx-background-radius: " + Styles.RADIUS_LG + ";");
        return card;
    }

    private void evaluate() {
        resultContainer.getChildren().clear();

        try {
            int sh = sleepHourSpinner.getValue();
            int sm = sleepMinSpinner.getValue();
            int wh = wakeHourSpinner.getValue();
            int wm = wakeMinSpinner.getValue();
            int score = (int) scoreSlider.getValue();

            Map<String, Object> result = SleepEvaluator.evaluate(sh, sm, wh, wm, score);
            int hours = (int) result.get("durationHours");
            int minutes = (int) result.get("durationMinutes");
            String level = (String) result.get("level");
            String suggestion = (String) result.get("suggestion");
            LevelConfig config = LEVEL_CONFIG.getOrDefault(level,
                    new LevelConfig(Styles.COLOR_SUCCESS, Styles.COLOR_SUCCESS_LIGHT, "\u2705"));

            showResult(hours + " 小时 " + minutes + " 分钟", level, suggestion, config);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void showResult(String duration, String level, String suggestion, LevelConfig config) {
        VBox card = createCard();

        HBox header = new HBox();
        header.setStyle("-fx-background-color: " + config.bgColor + ";");
        header.setPadding(new Insets(Styles.PADDING_MD, Styles.PADDING_LG, Styles.PADDING_XS, Styles.PADDING_LG));
        header.setAlignment(Pos.CENTER_LEFT);

        Label levelLabel = new Label(config.emoji + "  睡眠质量：" + level);
        levelLabel.setFont(Styles.FONT_BADGE);
        levelLabel.setStyle("-fx-text-fill: " + config.fgColor + ";");
        header.getChildren().add(levelLabel);

        VBox body = new VBox(Styles.PADDING_SM);
        body.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        body.setPadding(new Insets(Styles.PADDING_MD, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));

        Label durationLabel = new Label("\uD83D\uDD70\uFE0F  睡眠时长：" + duration);
        durationLabel.setFont(Styles.FONT_LABEL);
        durationLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
        durationLabel.setPadding(new Insets(0, 0, Styles.PADDING_SM, 0));

        Label suggestionLabel = new Label("\uD83D\uDCAC  " + suggestion);
        suggestionLabel.setFont(Styles.FONT_RESULT);
        suggestionLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
        suggestionLabel.setWrapText(true);
        suggestionLabel.setMaxWidth(500);

        body.getChildren().addAll(durationLabel, suggestionLabel);
        card.getChildren().addAll(header, body);

        resultContainer.getChildren().add(card);
    }

    private void showError(String msg) {
        VBox card = createCard();
        Label errorLabel = new Label("\u26A0\uFE0F  " + msg);
        errorLabel.setFont(Styles.FONT_RESULT);
        errorLabel.setStyle("-fx-text-fill: " + Styles.COLOR_DANGER + ";");
        errorLabel.setPadding(new Insets(Styles.PADDING_MD, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));
        card.getChildren().add(errorLabel);
        resultContainer.getChildren().add(card);
    }

    private record LevelConfig(String fgColor, String bgColor, String emoji) {}
}
