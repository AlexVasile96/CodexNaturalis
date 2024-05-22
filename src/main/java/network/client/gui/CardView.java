package network.client.gui;

import javafx.scene.image.ImageView;

import java.util.Objects;

public class CardView {
    private ImageView imageView;
    private String cardId;
    private String position; // TL, TR, BL, BR, TC, LC, CC, RC, BC

    public CardView(ImageView imageView, String cardId, String position) {
        this.imageView = imageView;
        this.cardId = cardId;
        this.position = position;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getCardId() {
        return cardId;
    }

    public String getPosition() {
        return position;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardView cardView = (CardView) o;
        return Objects.equals(imageView, cardView.imageView) &&
                Objects.equals(cardId, cardView.cardId) &&
                Objects.equals(position, cardView.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageView, cardId, position);
    }

}
