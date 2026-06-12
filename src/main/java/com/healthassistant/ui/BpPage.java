package com.healthassistant.ui;

import com.healthassistant.logic.BpEvaluator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Map;

public class BpPage {

    private static final Map<String, LevelConfig> LEVEL_CONFIG = Map.of(
            BpEvaluator.LEVEL_NORMAL, new LevelConfig(Styles.COLOR_SUCCESS, Styles.COLOR_SUCCESS_LIGHT, "\u2705"),
            BpEvaluator.LEVEL_HIGH, new LevelConfig(Styles.COLOR_WARNING, Styles.COLOR_WARNING_LIGHT, "\u26A0\uFE0F"),
            BpEvaluator.LEVEL_HYPERTENSION, new LevelConfig(Styles.COLOR_DANGER, Styles.COLOR_DANGER_LIGHT, "\u274C")
    );

    private final StackPane parent;
    private final TextField systolicField;
    private final TextField diastolicField;
    private final VBox resultContainer;

    public BpPage(StackPane parent) {
        this.parent = parent;
        this.systolicField = new TextField();
        this.diastolicField = new TextField();
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

        Label title = new Label("\u2764\uFE0F  血压测评");
        title.setFont(Styles.FONT_TITLE);
        title.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
        title.setPadding(new Insets(Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_XS, Styles.PADDING_LG));

        Label desc = new Label("请输入您的收缩压和舒张压值");
        desc.setFont(Styles.FONT_SUBTITLE);
        desc.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
        desc.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));

        VBox formBox = new VBox(Styles.PADDING_MD);
        formBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        formBox.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_SM, Styles.PADDING_LG));

        HBox bpRow = new HBox(Styles.PADDING_LG);
        bpRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        VBox leftCol = new VBox(Styles.PADDING_XS);
        leftCol.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        Label sysLabel = new Label("收缩压（高压）");
        sysLabel.setFont(Styles.FONT_LABEL);
        sysLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        HBox sysEntryRow = new HBox(Styles.PADDING_SM);
        sysEntryRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        sysEntryRow.setAlignment(Pos.CENTER_LEFT);

        systolicField.setFont(Font.font(Styles.FONT_FAMILY, 16));
        systolicField.setPrefWidth(80);
        systolicField.setStyle("-fx-background-color: " + Styles.COLOR_INPUT_BG + "; -fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + "; -fx-border-color: " + Styles.COLOR_CARD_BORDER + "; -fx-border-radius: 4;");

        Label sysUnit = new Label(" mmHg");
        sysUnit.setFont(Styles.FONT_SMALL);
        sysUnit.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");

        sysEntryRow.getChildren().addAll(systolicField, sysUnit);
        leftCol.getChildren().addAll(sysLabel, sysEntryRow);

        VBox rightCol = new VBox(Styles.PADDING_XS);
        rightCol.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        Label diaLabel = new Label("舒张压（低压）");
        diaLabel.setFont(Styles.FONT_LABEL);
        diaLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        HBox diaEntryRow = new HBox(Styles.PADDING_SM);
        diaEntryRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        diaEntryRow.setAlignment(Pos.CENTER_LEFT);

        diastolicField.setFont(Font.font(Styles.FONT_FAMILY, 16));
        diastolicField.setPrefWidth(80);
        diastolicField.setStyle("-fx-background-color: " + Styles.COLOR_INPUT_BG + "; -fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + "; -fx-border-color: " + Styles.COLOR_CARD_BORDER + "; -fx-border-radius: 4;");

        Label diaUnit = new Label(" mmHg");
        diaUnit.setFont(Styles.FONT_SMALL);
        diaUnit.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");

        diaEntryRow.getChildren().addAll(diastolicField, diaUnit);
        rightCol.getChildren().addAll(diaLabel, diaEntryRow);

        bpRow.getChildren().addAll(leftCol, rightCol);
        formBox.getChildren().add(bpRow);

        Label hint = new Label("有效范围：收缩压 1-300 mmHg，舒张压 1-200 mmHg");
        hint.setFont(Styles.FONT_SMALL);
        hint.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");
        hint.setPadding(new Insets(Styles.PADDING_SM, Styles.PADDING_LG, 0, Styles.PADDING_LG));

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

        card.getChildren().addAll(title, desc, formBox, hint, btnBox);

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

        String sysText = systolicField.getText().trim();
        String diaText = diastolicField.getText().trim();

        if (sysText.isEmpty() || diaText.isEmpty()) {
            showError("请输入收缩压和舒张压");
            return;
        }

        try {
            double systolic = Double.parseDouble(sysText);
            double diastolic = Double.parseDouble(diaText);
            Map<String, Object> result = BpEvaluator.evaluate(systolic, diastolic);
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
