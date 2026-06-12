package com.healthassistant.ui;

import com.healthassistant.logic.ExerciseAdvisor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExercisePage {

    private static final Map<String, String> ITEM_ICONS = new LinkedHashMap<>();

    static {
        ITEM_ICONS.put("运动类型", "\uD83C\uDFC3");
        ITEM_ICONS.put("运动时长", "\u23F1\uFE0F");
        ITEM_ICONS.put("运动频率", "\uD83D\uDCC5");
        ITEM_ICONS.put("注意事项", "\u2139\uFE0F");
    }

    private final StackPane parent;
    private final TextField ageField;
    private final ToggleGroup freqGroup;
    private final VBox resultContainer;

    public ExercisePage(StackPane parent) {
        this.parent = parent;
        this.ageField = new TextField();
        this.freqGroup = new ToggleGroup();
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

        Label title = new Label("\uD83C\uDFC3  运动建议");
        title.setFont(Styles.FONT_TITLE);
        title.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
        title.setPadding(new Insets(Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_XS, Styles.PADDING_LG));

        Label desc = new Label("请填写您的基本情况，获取个性化运动方案");
        desc.setFont(Styles.FONT_SUBTITLE);
        desc.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
        desc.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));

        VBox formBox = new VBox(Styles.PADDING_MD);
        formBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        formBox.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));

        HBox cols = new HBox(Styles.PADDING_XL);
        cols.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        VBox leftCol = new VBox(Styles.PADDING_XS);
        leftCol.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        Label ageLabel = new Label("年龄");
        ageLabel.setFont(Styles.FONT_LABEL);
        ageLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        HBox entryRow = new HBox(Styles.PADDING_SM);
        entryRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        entryRow.setAlignment(Pos.CENTER_LEFT);

        ageField.setFont(Font.font(Styles.FONT_FAMILY, 16));
        ageField.setPrefWidth(80);
        ageField.setStyle("-fx-background-color: " + Styles.COLOR_INPUT_BG + "; -fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + "; -fx-border-color: " + Styles.COLOR_CARD_BORDER + "; -fx-border-radius: 4;");

        Label ageUnit = new Label(" 岁");
        ageUnit.setFont(Styles.FONT_SMALL);
        ageUnit.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");

        entryRow.getChildren().addAll(ageField, ageUnit);

        Label ageHint = new Label("有效范围：1-120 岁");
        ageHint.setFont(Styles.FONT_SMALL);
        ageHint.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");
        ageHint.setPadding(new Insets(Styles.PADDING_SM, 0, 0, 0));

        leftCol.getChildren().addAll(ageLabel, entryRow, ageHint);

        VBox rightCol = new VBox(Styles.PADDING_XS);
        rightCol.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        Label freqLabel = new Label("运动频率");
        freqLabel.setFont(Styles.FONT_LABEL);
        freqLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        HBox radioRow = new HBox(Styles.PADDING_SM);
        radioRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        RadioButton sedentary = new RadioButton(ExerciseAdvisor.getFrequencyLabel(ExerciseAdvisor.FREQ_SEDENTARY));
        sedentary.setToggleGroup(freqGroup);
        sedentary.setUserData(ExerciseAdvisor.FREQ_SEDENTARY);
        sedentary.setFont(Styles.FONT_LABEL);
        sedentary.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        RadioButton occasional = new RadioButton(ExerciseAdvisor.getFrequencyLabel(ExerciseAdvisor.FREQ_OCCASIONAL));
        occasional.setToggleGroup(freqGroup);
        occasional.setUserData(ExerciseAdvisor.FREQ_OCCASIONAL);
        occasional.setSelected(true);
        occasional.setFont(Styles.FONT_LABEL);
        occasional.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        RadioButton frequent = new RadioButton(ExerciseAdvisor.getFrequencyLabel(ExerciseAdvisor.FREQ_FREQUENT));
        frequent.setToggleGroup(freqGroup);
        frequent.setUserData(ExerciseAdvisor.FREQ_FREQUENT);
        frequent.setFont(Styles.FONT_LABEL);
        frequent.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        radioRow.getChildren().addAll(sedentary, occasional, frequent);
        rightCol.getChildren().addAll(freqLabel, radioRow);

        cols.getChildren().addAll(leftCol, rightCol);
        formBox.getChildren().add(cols);

        Button evalBtn = new Button("  \u2728  生成建议");
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

        String ageText = ageField.getText().trim();
        if (ageText.isEmpty()) {
            showError("请输入年龄");
            return;
        }

        RadioButton selected = (RadioButton) freqGroup.getSelectedToggle();
        String frequency = selected != null ? (String) selected.getUserData() : ExerciseAdvisor.FREQ_OCCASIONAL;

        try {
            int age = Integer.parseInt(ageText);
            Map<String, String> plan = ExerciseAdvisor.getExerciseAdvice(age, frequency);
            String freqLabel = ExerciseAdvisor.getFrequencyLabel(frequency);
            showResult(plan, freqLabel);
        } catch (NumberFormatException e) {
            showError("请输入有效的整数");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void showResult(Map<String, String> plan, String freqLabel) {
        VBox card = createCard();

        HBox header = new HBox();
        header.setStyle("-fx-background-color: " + Styles.COLOR_PRIMARY_LIGHT + ";");
        header.setPadding(new Insets(Styles.PADDING_MD, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));
        header.setAlignment(Pos.CENTER_LEFT);

        Label headerLabel = new Label("\u2728  您的运动方案（" + freqLabel + "）");
        headerLabel.setFont(Styles.FONT_BADGE);
        headerLabel.setStyle("-fx-text-fill: " + Styles.COLOR_PRIMARY + ";");
        header.getChildren().add(headerLabel);

        VBox body = new VBox(Styles.PADDING_XS);
        body.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        body.setPadding(new Insets(Styles.PADDING_MD, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));

        String[][] items = {
                {"运动类型", plan.get("type")},
                {"运动时长", plan.get("duration")},
                {"运动频率", plan.get("frequency")},
                {"注意事项", plan.get("note")},
        };

        for (String[] item : items) {
            HBox row = new HBox(Styles.PADDING_MD);
            row.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
            row.setAlignment(Pos.CENTER_LEFT);

            String icon = ITEM_ICONS.getOrDefault(item[0], "\u25B8");
            Label itemLabel = new Label(" " + icon + "  " + item[0]);
            itemLabel.setFont(Styles.FONT_LABEL);
            itemLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
            itemLabel.setPrefWidth(120);
            itemLabel.setMinWidth(120);

            Label valueLabel = new Label(item[1]);
            valueLabel.setFont(Styles.FONT_RESULT);
            valueLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
            valueLabel.setWrapText(true);
            valueLabel.setMaxWidth(400);

            row.getChildren().addAll(itemLabel, valueLabel);
            body.getChildren().add(row);
        }

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
}
