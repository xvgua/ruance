package com.healthassistant.ui;

import com.healthassistant.logic.AIAdvisorService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.List;

public class AIPage {

    private final StackPane parent;
    private final TextArea questionArea;
    private final VBox resultContainer;
    private final VBox suggestionContainer;

    public AIPage(StackPane parent) {
        this.parent = parent;
        this.questionArea = new TextArea();
        this.resultContainer = new VBox(Styles.PADDING_MD);
        this.resultContainer.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");
        this.suggestionContainer = new VBox(Styles.PADDING_SM);
        this.suggestionContainer.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        build();
    }

    private void build() {
        VBox frame = new VBox(Styles.PADDING_MD);
        frame.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");
        frame.setFillWidth(true);

        VBox card = createCard();
        card.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("\uD83E\uDD16  AI 健康助手");
        title.setFont(Styles.FONT_TITLE);
        title.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
        title.setPadding(new Insets(Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_XS, Styles.PADDING_LG));

        Label desc = new Label("\uD83D\uDCAC  您可以向我询问血压、血糖、睡眠、运动、用药、饮食等方面的健康知识");
        desc.setFont(Styles.FONT_SUBTITLE);
        desc.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
        desc.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_SM, Styles.PADDING_LG));

        VBox formBox = new VBox(Styles.PADDING_MD);
        formBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        formBox.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_SM, Styles.PADDING_LG));

        questionArea.setFont(Font.font(Styles.FONT_FAMILY, 15));
        questionArea.setPrefRowCount(3);
        questionArea.setPrefHeight(80);
        questionArea.setWrapText(true);
        questionArea.setPromptText("请输入您的健康问题...");
        questionArea.setStyle("-fx-background-color: " + Styles.COLOR_INPUT_BG + "; -fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + "; -fx-border-color: " + Styles.COLOR_CARD_BORDER + "; -fx-border-radius: 6; -fx-background-radius: 6;");
        questionArea.setMaxWidth(Double.MAX_VALUE);
        formBox.getChildren().add(questionArea);

        buildSuggestions(formBox);

        Button askBtn = new Button("  \uD83D\uDCAC  提 问");
        askBtn.setFont(Styles.FONT_BUTTON);
        askBtn.setStyle("-fx-background-color: " + Styles.COLOR_PRIMARY + "; -fx-text-fill: white; -fx-background-radius: 4;");
        askBtn.setPrefWidth(140);
        askBtn.setPadding(new Insets(Styles.PADDING_SM + 2, Styles.PADDING_LG, Styles.PADDING_SM + 2, Styles.PADDING_LG));
        askBtn.setOnAction(e -> askQuestion());

        HBox btnBox = new HBox(askBtn);
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

    private void buildSuggestions(VBox formBox) {
        Label sugTitle = new Label("\uD83D\uDC49 试试这样问我：");
        sugTitle.setFont(Styles.FONT_SMALL);
        sugTitle.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");
        sugTitle.setPadding(new Insets(Styles.PADDING_MD, 0, Styles.PADDING_XS, 0));

        suggestionContainer.getChildren().clear();
        suggestionContainer.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        List<String> suggestions = Arrays.asList(
                "\uD69D  血压多少算正常？",
                "\uD83E\uDE7A  血糖正常值是多少？",
                "\uD83D\uDCA4  怎么改善睡眠？",
                "\uD83C\uDFC3  老年人适合什么运动？",
                "\uD83D\uDC8A  忘记吃药怎么办？",
                "\uD83C\uDF4E  老年人怎么补钙？"
        );

        HBox row = null;
        for (int i = 0; i < suggestions.size(); i++) {
            if (i % 2 == 0) {
                row = new HBox(Styles.PADDING_SM);
                row.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
                suggestionContainer.getChildren().add(row);
            }
            String q = suggestions.get(i);
            Button chip = new Button(q);
            chip.setFont(Styles.FONT_SMALL);
            chip.setStyle("-fx-background-color: " + Styles.COLOR_PRIMARY_LIGHT + "; -fx-text-fill: " + Styles.COLOR_PRIMARY + "; -fx-background-radius: 16; -fx-border-color: " + Styles.COLOR_PRIMARY + "; -fx-border-radius: 16; -fx-cursor: hand;");
            chip.setPadding(new Insets(Styles.PADDING_XS, Styles.PADDING_MD, Styles.PADDING_XS, Styles.PADDING_MD));
            chip.setOnAction(e -> {
                String text = chip.getText().replaceAll("^[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]\\s*", "").trim();
                questionArea.setText(text);
                askQuestion();
            });
            row.getChildren().add(chip);
        }

        formBox.getChildren().addAll(sugTitle, suggestionContainer);
    }

    private VBox createCard() {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + "; -fx-border-color: " + Styles.COLOR_CARD_BORDER + "; -fx-border-radius: " + Styles.RADIUS_LG + "; -fx-background-radius: " + Styles.RADIUS_LG + ";");
        return card;
    }

    private void askQuestion() {
        resultContainer.getChildren().clear();

        String question = questionArea.getText().trim();
        if (question.isEmpty()) {
            showError("请输入您的问题");
            return;
        }

        String answer = AIAdvisorService.answer(question);
        showResult(question, answer);
    }

    private void showResult(String question, String answer) {
        VBox card = createCard();

        HBox header = new HBox();
        header.setStyle("-fx-background-color: " + Styles.COLOR_PRIMARY_LIGHT + ";");
        header.setPadding(new Insets(Styles.PADDING_MD, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));
        header.setAlignment(Pos.CENTER_LEFT);

        Label qLabel = new Label("\u2753  您的问题");
        qLabel.setFont(Styles.FONT_LABEL);
        qLabel.setStyle("-fx-text-fill: " + Styles.COLOR_PRIMARY + ";");
        header.getChildren().add(qLabel);

        VBox body = new VBox(Styles.PADDING_SM);
        body.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        body.setPadding(new Insets(Styles.PADDING_MD, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));

        Label questionText = new Label("\uD83D\uDC64 " + question);
        questionText.setFont(Styles.FONT_RESULT);
        questionText.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
        questionText.setWrapText(true);
        questionText.setMaxWidth(550);
        body.getChildren().add(questionText);

        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setMaxHeight(1);
        divider.setStyle("-fx-background-color: " + Styles.COLOR_DIVIDER + ";");
        VBox.setMargin(divider, new Insets(Styles.PADDING_SM, 0, Styles.PADDING_SM, 0));
        body.getChildren().add(divider);

        Label answerHeaderLabel = new Label("\uD83E\uDD16  回答");
        answerHeaderLabel.setFont(Styles.FONT_LABEL);
        answerHeaderLabel.setStyle("-fx-text-fill: " + Styles.COLOR_PRIMARY + ";");
        body.getChildren().add(answerHeaderLabel);

        Label answerText = new Label(answer);
        answerText.setFont(Styles.FONT_RESULT);
        answerText.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
        answerText.setWrapText(true);
        answerText.setMaxWidth(550);
        answerText.setLineSpacing(4);
        body.getChildren().add(answerText);

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
