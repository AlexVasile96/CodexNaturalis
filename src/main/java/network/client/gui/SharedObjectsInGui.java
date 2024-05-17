package network.client.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SharedObjectsInGui {
    private static String pathResourceDeck;
    private static String pathGoldDeck;
    private static ImageView topCardResourceDeckView;
    private static ImageView topCardGoldDeckView;
    private static Image topCardResourceDeck;
    private static Image topCardGoldDeck;
    private static String wellPathOne;
    private static String wellPathSecond;
    private static String wellPathThird;
    private static String wellPathForth;
    private static Image wellCard1;
    private static Image wellCard2;
    private static Image wellCard3;
    private static Image wellCard4;
    private static ImageView wellCard1View;
    private static ImageView wellCard2View;
    private static ImageView wellCard3View;
    private static ImageView wellCard4View;
    private static boolean initialized = false;
    private static String idCard1;
    private static String idCard2;
    private static String idCard3;
    private static String idCard4;

    // Getter e setter per le nuove variabili
    public static String getIdCard1() {
        return idCard1;
    }

    public static void setIdCard1(String idCard1) {
        SharedObjectsInGui.idCard1 = idCard1;
    }

    public static String getIdCard2() {
        return idCard2;
    }

    public static void setIdCard2(String idCard2) {
        SharedObjectsInGui.idCard2 = idCard2;
    }

    public static String getIdCard3() {
        return idCard3;
    }

    public static void setIdCard3(String idCard3) {
        SharedObjectsInGui.idCard3 = idCard3;
    }

    public static String getIdCard4() {
        return idCard4;
    }

    public static void setIdCard4(String idCard4) {
        SharedObjectsInGui.idCard4 = idCard4;
    }

    public static synchronized boolean isInitialized() {
        return initialized;
    }

    public static synchronized void setInitialized(boolean initialized) {
        SharedObjectsInGui.initialized = initialized;
    }
    public static String getPathResourceDeck() {
        return pathResourceDeck;
    }

    public static void setPathResourceDeck(String pathResourceDeck) {
        SharedObjectsInGui.pathResourceDeck = pathResourceDeck;
    }

    public static String getPathGoldDeck() {
        return pathGoldDeck;
    }

    public static void setPathGoldDeck(String pathGoldDeck) {
        SharedObjectsInGui.pathGoldDeck = pathGoldDeck;
    }

    public static ImageView getTopCardResourceDeckView() {
        return topCardResourceDeckView;
    }

    public static void setTopCardResourceDeckView(ImageView topCardResourceDeckView) {
        SharedObjectsInGui.topCardResourceDeckView = topCardResourceDeckView;
    }

    public static ImageView getTopCardGoldDeckView() {
        return topCardGoldDeckView;
    }

    public static void setTopCardGoldDeckView(ImageView topCardGoldDeckView) {
        SharedObjectsInGui.topCardGoldDeckView = topCardGoldDeckView;
    }

    public static Image getTopCardResourceDeck() {
        return topCardResourceDeck;
    }

    public static void setTopCardResourceDeck(Image topCardResourceDeck) {
        SharedObjectsInGui.topCardResourceDeck = topCardResourceDeck;
    }

    public static Image getTopCardGoldDeck() {
        return topCardGoldDeck;
    }

    public static void setTopCardGoldDeck(Image topCardGoldDeck) {
        SharedObjectsInGui.topCardGoldDeck = topCardGoldDeck;
    }

    public static String getWellPathOne() {
        return wellPathOne;
    }

    public static void setWellPathOne(String wellPathOne) {
        SharedObjectsInGui.wellPathOne = wellPathOne;
    }

    public static String getWellPathSecond() {
        return wellPathSecond;
    }

    public static void setWellPathSecond(String wellPathSecond) {
        SharedObjectsInGui.wellPathSecond = wellPathSecond;
    }

    public static String getWellPathThird() {
        return wellPathThird;
    }

    public static void setWellPathThird(String wellPathThird) {
        SharedObjectsInGui.wellPathThird = wellPathThird;
    }

    public static String getWellPathForth() {
        return wellPathForth;
    }

    public static void setWellPathForth(String wellPathForth) {
        SharedObjectsInGui.wellPathForth = wellPathForth;
    }

    public static Image getWellCard1() {
        return wellCard1;
    }

    public static void setWellCard1(Image wellCard1) {
        SharedObjectsInGui.wellCard1 = wellCard1;
    }

    public static Image getWellCard2() {
        return wellCard2;
    }

    public static void setWellCard2(Image wellCard2) {
        SharedObjectsInGui.wellCard2 = wellCard2;
    }

    public static Image getWellCard3() {
        return wellCard3;
    }

    public static void setWellCard3(Image wellCard3) {
        SharedObjectsInGui.wellCard3 = wellCard3;
    }

    public static Image getWellCard4() {
        return wellCard4;
    }

    public static void setWellCard4(Image wellCard4) {
        SharedObjectsInGui.wellCard4 = wellCard4;
    }

    public static ImageView getWellCard1View() {
        return wellCard1View;
    }

    public static void setWellCard1View(ImageView wellCard1View) {
        SharedObjectsInGui.wellCard1View = wellCard1View;
    }

    public static ImageView getWellCard2View() {
        return wellCard2View;
    }

    public static void setWellCard2View(ImageView wellCard2View) {
        SharedObjectsInGui.wellCard2View = wellCard2View;
    }

    public static ImageView getWellCard3View() {
        return wellCard3View;
    }

    public static void setWellCard3View(ImageView wellCard3View) {
        SharedObjectsInGui.wellCard3View = wellCard3View;
    }

    public static ImageView getWellCard4View() {
        return wellCard4View;
    }

    public static void setWellCard4View(ImageView wellCard4View) {
        SharedObjectsInGui.wellCard4View = wellCard4View;
    }
}
