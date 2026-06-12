package com.healthassistant.ui;

import com.healthassistant.logic.Reminder;
import com.healthassistant.logic.ReminderManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderPage {

    private final StackPane parent;
    private final ReminderManager manager;
    private final TextField nameField;
    private final TextField dosageField;
    private final Spinner<Integer> hourSpinner;
    private final Spinner<Integer> minuteSpinner;
    private final Label errorLabel;
    private final VBox listBox;
    private Timer checkTimer;

    public ReminderPage(StackPane parent, ReminderManager manager) {
        this.parent = parent;
        this.manager = manager;
        this.nameField = new TextField();
        this.dosageField = new TextField();
        this.hourSpinner = new Spinner<>(0, 23, 8);
        this.minuteSpinner = new Spinner<>(0, 59, 0);
        this.errorLabel = new Label();
        this.listBox = new VBox(Styles.PADDING_MD);
        this.listBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        build();
        startChecker();
    }

    private void build() {
        VBox frame = new VBox(Styles.PADDING_MD);
        frame.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + ";");
        frame.setFillWidth(true);

        VBox card = createCard();
        card.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("\uD83D\uDC8A  添加用药提醒");
        title.setFont(Styles.FONT_TITLE);
        title.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
        title.setPadding(new Insets(Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_XS, Styles.PADDING_LG));

        Label desc = new Label("设置药品信息和提醒时间");
        desc.setFont(Styles.FONT_SUBTITLE);
        desc.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
        desc.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));

        VBox formBox = new VBox(Styles.PADDING_MD);
        formBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        formBox.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_MD, Styles.PADDING_LG));

        HBox row1 = new HBox(Styles.PADDING_LG);
        row1.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        VBox leftCol = new VBox(Styles.PADDING_XS);
        leftCol.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        Label nameLabel = new Label("药品名称");
        nameLabel.setFont(Styles.FONT_LABEL);
        nameLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        nameField.setFont(Font.font(Styles.FONT_FAMILY, 14));
        nameField.setPrefWidth(180);
        nameField.setStyle("-fx-background-color: " + Styles.COLOR_INPUT_BG + "; -fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + "; -fx-border-color: " + Styles.COLOR_CARD_BORDER + "; -fx-border-radius: 4;");
        nameField.setPadding(new Insets(Styles.PADDING_XS, Styles.PADDING_SM, Styles.PADDING_XS, Styles.PADDING_SM));

        leftCol.getChildren().addAll(nameLabel, nameField);

        VBox rightCol = new VBox(Styles.PADDING_XS);
        rightCol.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        Label dosageLabel = new Label("剂量");
        dosageLabel.setFont(Styles.FONT_LABEL);
        dosageLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        dosageField.setFont(Font.font(Styles.FONT_FAMILY, 14));
        dosageField.setPrefWidth(120);
        dosageField.setStyle("-fx-background-color: " + Styles.COLOR_INPUT_BG + "; -fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + "; -fx-border-color: " + Styles.COLOR_CARD_BORDER + "; -fx-border-radius: 4;");
        dosageField.setPadding(new Insets(Styles.PADDING_XS, Styles.PADDING_SM, Styles.PADDING_XS, Styles.PADDING_SM));

        rightCol.getChildren().addAll(dosageLabel, dosageField);

        row1.getChildren().addAll(leftCol, rightCol);

        VBox timeBox = new VBox(Styles.PADDING_XS);
        timeBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");

        Label timeLabel = new Label("\u23F0  提醒时间");
        timeLabel.setFont(Styles.FONT_LABEL);
        timeLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        HBox timeRow = new HBox(Styles.PADDING_SM);
        timeRow.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        timeRow.setAlignment(Pos.CENTER_LEFT);

        hourSpinner.setEditable(true);
        hourSpinner.setPrefWidth(80);
        minuteSpinner.setEditable(true);
        minuteSpinner.setPrefWidth(80);

        Label hLabel = new Label(" 时 ");
        hLabel.setFont(Styles.FONT_LABEL);
        hLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");

        Label mLabel = new Label(" 分");
        mLabel.setFont(Styles.FONT_SMALL);
        mLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");

        timeRow.getChildren().addAll(hourSpinner, hLabel, minuteSpinner, mLabel);
        timeBox.getChildren().addAll(timeLabel, timeRow);

        errorLabel.setFont(Styles.FONT_SMALL);
        errorLabel.setStyle("-fx-text-fill: " + Styles.COLOR_DANGER + ";");

        formBox.getChildren().addAll(row1, timeBox, errorLabel);

        Button addBtn = new Button("  \u2795  添加提醒");
        addBtn.setFont(Styles.FONT_BUTTON);
        addBtn.setStyle("-fx-background-color: " + Styles.COLOR_PRIMARY + "; -fx-text-fill: white; -fx-background-radius: 4;");
        addBtn.setPrefWidth(160);
        addBtn.setPadding(new Insets(Styles.PADDING_SM + 2, Styles.PADDING_LG, Styles.PADDING_SM + 2, Styles.PADDING_LG));
        addBtn.setOnAction(e -> addReminder());

        HBox btnBox = new HBox(addBtn);
        btnBox.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(0, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));

        card.getChildren().addAll(title, desc, formBox, btnBox);

        VBox listCard = createCard();
        listCard.setMaxWidth(Double.MAX_VALUE);

        Label listTitle = new Label("\uD83D\uDCCB  已设置提醒");
        listTitle.setFont(Styles.FONT_TITLE);
        listTitle.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
        listTitle.setPadding(new Insets(Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_SM, Styles.PADDING_LG));

        Region sep = new Region();
        sep.setPrefHeight(1);
        sep.setMaxHeight(1);
        sep.setStyle("-fx-background-color: " + Styles.COLOR_DIVIDER + ";");

        listCard.getChildren().addAll(listTitle, sep, listBox);
        VBox.setVgrow(listCard, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(listCard);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + Styles.COLOR_CONTENT_BG + "; -fx-background: " + Styles.COLOR_CONTENT_BG + ";");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        frame.getChildren().addAll(card, scrollPane);
        parent.getChildren().add(frame);

        refreshList();
    }

    private VBox createCard() {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + "; -fx-border-color: " + Styles.COLOR_CARD_BORDER + "; -fx-border-radius: " + Styles.RADIUS_LG + "; -fx-background-radius: " + Styles.RADIUS_LG + ";");
        return card;
    }

    private void addReminder() {
        String name = nameField.getText().trim();
        String dosage = dosageField.getText().trim();
        errorLabel.setText("");

        try {
            int hour = hourSpinner.getValue();
            int minute = minuteSpinner.getValue();
            manager.add(name, dosage, hour, minute);
            nameField.clear();
            dosageField.clear();
            refreshList();
        } catch (IllegalArgumentException e) {
            errorLabel.setText("\u26A0\uFE0F  " + e.getMessage());
        }
    }

    private void removeReminder(int id) {
        manager.remove(id);
        refreshList();
    }

    private void refreshList() {
        listBox.getChildren().clear();

        java.util.List<Reminder> reminders = manager.getAll();
        if (reminders.isEmpty()) {
            VBox emptyCard = createCard();
            emptyCard.setMaxWidth(Double.MAX_VALUE);
            Label emptyLabel = new Label("\uD83D\uDEB6 暂无提醒，请在上方添加用药提醒");
            emptyLabel.setFont(Styles.FONT_RESULT);
            emptyLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_MUTED + ";");
            emptyLabel.setPadding(new Insets(Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG, Styles.PADDING_LG));
            emptyCard.getChildren().add(emptyLabel);
            listBox.getChildren().add(emptyCard);
            return;
        }

        for (Reminder r : reminders) {
            HBox row = new HBox(Styles.PADDING_MD);
            row.setStyle("-fx-background-color: " + Styles.COLOR_CARD_BG + ";");
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(Styles.PADDING_XS, 0, Styles.PADDING_XS, 0));

            Region dot = new Region();
            dot.setPrefWidth(3);
            dot.setMinWidth(3);
            dot.setMaxWidth(3);
            dot.setStyle("-fx-background-color: " + Styles.COLOR_PRIMARY + ";");

            Label timeLabel = new Label("\uD83D\uDD55 " + r.getTimeStr());
            timeLabel.setFont(Styles.FONT_LABEL);
            timeLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_PRIMARY + ";");
            timeLabel.setPrefWidth(100);
            timeLabel.setMinWidth(100);

            Label infoLabel = new Label(r.getName() + "  |  " + r.getDosage());
            infoLabel.setFont(Styles.FONT_RESULT);
            infoLabel.setStyle("-fx-text-fill: " + Styles.COLOR_TEXT_SECONDARY + ";");
            HBox.setHgrow(infoLabel, Priority.ALWAYS);
            infoLabel.setMaxWidth(Double.MAX_VALUE);

            Button delBtn = new Button("删除");
            delBtn.setFont(Styles.FONT_BUTTON_SM);
            delBtn.setStyle("-fx-background-color: " + Styles.COLOR_DANGER + "; -fx-text-fill: white; -fx-background-radius: 4;");
            delBtn.setPadding(new Insets(2, Styles.PADDING_MD, 2, Styles.PADDING_MD));
            int rid = r.getId();
            delBtn.setOnAction(e -> removeReminder(rid));

            row.getChildren().addAll(dot, timeLabel, infoLabel, delBtn);
            row.setHgrow(infoLabel, Priority.ALWAYS);

            listBox.getChildren().add(row);
        }
    }

    private void startChecker() {
        checkTimer = new Timer(true);
        checkTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<Reminder> due = manager.checkNow();
                if (!due.isEmpty()) {
                    Platform.runLater(() -> {
                        for (Reminder r : due) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("\u23F0 用药提醒");
                            alert.setHeaderText("该吃药啦！");
                            alert.setContentText("\uD83D\uDC8A " + r.getName() + "\n\uD83D\uDCCF " + r.getDosage());
                            alert.show();
                        }
                    });
                }
            }
        }, 1000, 30000);
    }

    public void stopChecker() {
        if (checkTimer != null) {
            checkTimer.cancel();
        }
    }
}
