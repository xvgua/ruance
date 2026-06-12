package com.healthassistant.ui;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public final class Styles {

    private Styles() {}

    public static final String FONT_FAMILY = "Microsoft YaHei";

    public static final Font FONT_HERO = Font.font(FONT_FAMILY, FontWeight.BOLD, 28);
    public static final Font FONT_TITLE = Font.font(FONT_FAMILY, FontWeight.BOLD, 22);
    public static final Font FONT_SUBTITLE = Font.font(FONT_FAMILY, 16);
    public static final Font FONT_LABEL = Font.font(FONT_FAMILY, 15);
    public static final Font FONT_BUTTON = Font.font(FONT_FAMILY, FontWeight.BOLD, 15);
    public static final Font FONT_BUTTON_SM = Font.font(FONT_FAMILY, FontWeight.BOLD, 12);
    public static final Font FONT_RESULT = Font.font(FONT_FAMILY, 14);
    public static final Font FONT_BADGE = Font.font(FONT_FAMILY, FontWeight.BOLD, 20);
    public static final Font FONT_SMALL = Font.font(FONT_FAMILY, 12);
    public static final Font FONT_NAV = Font.font(FONT_FAMILY, FontWeight.BOLD, 14);

    public static final String COLOR_BG = "#F0EDE8";
    public static final String COLOR_NAV_BG = "#1E2D3D";
    public static final String COLOR_NAV_HOVER = "#2A3F56";
    public static final String COLOR_NAV_ACTIVE = "#3B82A4";
    public static final String COLOR_NAV_TEXT = "#B0BEC5";
    public static final String COLOR_NAV_TEXT_ACTIVE = "#FFFFFF";
    public static final String COLOR_CONTENT_BG = "#F7F5F2";

    public static final String COLOR_PRIMARY = "#3B82A4";
    public static final String COLOR_PRIMARY_LIGHT = "#E8F4F9";

    public static final String COLOR_SUCCESS = "#43A047";
    public static final String COLOR_SUCCESS_LIGHT = "#E8F5E9";
    public static final String COLOR_WARNING = "#EF6C00";
    public static final String COLOR_WARNING_LIGHT = "#FFF3E0";
    public static final String COLOR_DANGER = "#E53935";
    public static final String COLOR_DANGER_LIGHT = "#FFEBEE";

    public static final String COLOR_CARD_BG = "#FFFFFF";
    public static final String COLOR_CARD_BORDER = "#E0DCD5";
    public static final String COLOR_TEXT_PRIMARY = "#263238";
    public static final String COLOR_TEXT_SECONDARY = "#607D8B";
    public static final String COLOR_TEXT_MUTED = "#90A4AE";
    public static final String COLOR_DIVIDER = "#ECE8E1";
    public static final String COLOR_INPUT_BG = "#FFFFFF";

    public static final double PADDING_XL = 32;
    public static final double PADDING_LG = 24;
    public static final double PADDING_MD = 16;
    public static final double PADDING_SM = 10;
    public static final double PADDING_XS = 6;

    public static final double RADIUS_LG = 12;
    public static final double RADIUS_MD = 8;
}
