package com.healthassistant.ui;

import com.healthassistant.logic.BgEvaluator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Map;

public class BgPage {

    private static final Map<String, LevelConfig> LEVEL_CONFIG = Map.of(
            BgEvaluator.LEVEL_NORMAL, new LevelConfig(Styles.COLOR_SUCCESS, Styles.COLOR_SUCCESS_LIGHT, "\u2705"),
            BgEvaluator.LEVEL_HIGH, new LevelConfig(Styles.COLOR_WARNING, Styles.COLOR_WARNING_LIGHT, "\u26A0\uFE0F"),
            BgEvaluator.LEVEL_DIABETES, new LevelConfig(Styles.COLOR_DANGER, Styles.COLOR_DANGER_LIGHT, "\u274C")
    );

    private final StackPane parent;
    private final ToggleGroup periodGroup;
    private final TextField valueField;
    private final VBox resultContainer;

    public BgPage(StackPane parent) {
        this.parent = parent;
        this.periodGroup = new ToggleGroup();
        this.valueField = new TextField();
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

        Label title = new Label("\uD83E\uDE7A  血糖测评");
        title.setFont(Styles.FONT_TITLE);
        title.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
        title.setPadding(new Insets(Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_XS, Styles.PADDING_LG));

        Label desc = new Label("请输入您的血糖值以及测量时段");
        desc.setFont(Styles.FONT_SUBTITLE);
        desc.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
        desc.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));

        VBox formBox = new VBox(Styles.PADDING_SM);
        formBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        formBox.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));

        Label valueLabel = new Label("血糖值");
        valueLabel.setFont(Styles.FONT_LABEL);
        valueLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        HBox entryRow = new HBox(Styles.PADDING_SM);
        entryRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        entryRow.setAlignment(Pos.CENTER_LEFT);

        valueField.setFont(Font.font(Styles.FONT_FAMILY, 16));
        valueField.setPrefWidth(120);
        valueField.setStyle("-fx-background-color: " + Styles.COLOR_INPUT_BG + "; -fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + "; -fx-border-color: " + Styles.COLOR_CARD_BORDER + "; -fx-border-radius: 4;");

        Label unit = new Label("  mmol/L");
        unit.setFont(Styles.FONT_SMALL);
        unit.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");

        entryRow.getChildren().addAll(valueField, unit);

        Label hint = new Label("有效范围：0.1-35 mmol/L");
        hint.setFont(Styles.FONT_SMALL);
        hint.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");
        hint.setPadding(new Insets(Styles.PADDING_SM, 0, 0, 0));

        formBox.getChildren().addAll(valueLabel, entryRow, hint);

        VBox periodBox = new VBox(Styles.PADDING_XS);
        periodBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        periodBox.setPadding(new Insets(0, Styles.PADDING_LG, 0, Styles.PADDING_LG));

        Label periodLabel = new Label("测量时段");
        periodLabel.setFont(Styles.FONT_LABEL);
        periodLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        HBox radioRow = new HBox(Styles.PADDING_LG);
        radioRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        RadioButton fastingRb = new RadioButton(BgEvaluator.PERIOD_FASTING);
        fastingRb.setToggleGroup(periodGroup);
        fastingRb.setSelected(true);
        fastingRb.setFont(Styles.FONT_LABEL);
        fastingRb.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        RadioButton postMealRb = new RadioButton(BgEvaluator.PERIOD_POST_MEAL);
        postMealRb.setToggleGroup(periodGroup);
        postMealRb.setFont(Styles.FONT_LABEL);
        postMealRb.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        radioRow.getChildren().addAll(fastingRb, postMealRb);
        periodBox.getChildren().addAll(periodLabel, radioRow);

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

        card.getChildren().addAll(title, desc, formBox, periodBox, btnBox);

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

        String valueText = valueField.getText().trim();
        if (valueText.isEmpty()) {
            showError("请输入血糖值");
            return;
        }

        RadioButton selected = (RadioButton) periodGroup.getSelectedToggle();
        String period = selected != null ? selected.getText() : BgEvaluator.PERIOD_FASTING;

        try {
            double value = Double.parseDouble(valueText);
            Map<String, Object> result = BgEvaluator.evaluate(value, period);
            String level = (String) result.get("level");
            String suggestion = (String) result.get("suggestion");
            LevelConfig config = LEVEL_CONFIG.getOrDefault(level,
                    new LevelConfig(Styles.COLOR_SUCCESS, Styles.COLOR_SUCCESS_LIGHT, "\u2705"));
            showResult(level, suggestion, config);
        } catch (NumberFormatException e) {
            showError("请输入有效的数字");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void showResult(String level, String suggestion, LevelConfig config) {
        VBox card = createCard();

        HBox header = new HBox();
        header.setStyle("-fx-background-color: " + config.bgColor + ";");
        header.setPadding(new Insets(Styles.PADDING_MD, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));
        header.setAlignment(Pos.CENTER_LEFT);

        Label levelLabel = new Label(config.emoji + "  " + level);
        levelLabel.setFont(Styles.FONT_BADGE);
        levelLabel.setStyle("-fx-text-fill: " + config.fgColor + ";");
        header.getChildren().add(levelLabel);

        VBox body = new VBox(Styles.PADDING_SM);
        body.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        body.setPadding(new Insets(Styles.PADDING_MD, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));

        HBox suggestionRow = new HBox(Styles.PADDING_SM);
        suggestionRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        suggestionRow.setAlignment(Pos.TOP_LEFT);

        Label bubble = new Label("\uD83D\uDCAC");
        bubble.setFont(Font.font(14));
        bubble.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");

        Label suggestionText = new Label(suggestion);
        suggestionText.setFont(Styles.FONT_RESULT);
        suggestionText.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
        suggestionText.setWrapText(true);
        suggestionText.setMaxWidth(500);

        suggestionRow.getChildren().addAll(bubble, suggestionText);
        body.getChildren().add(suggestionRow);

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
