package flashcards;

public class Flashcard {
    private int cardId;
    private int userId;
    private String frontText;
    private String backText;
    private String languageDirection; // "EN_HI" or "HI_EN"

    public Flashcard(int cardId, int userId, String frontText, String backText, String languageDirection) {
        this.cardId = cardId;
        this.userId = userId;
        this.frontText = frontText;
        this.backText = backText;
        this.languageDirection = languageDirection;
    }

    public int getCardId() { return cardId; }
    public int getUserId() { return userId; }
    public String getFrontText() { return frontText; }
    public String getBackText() { return backText; }
    public String getLanguageDirection() { return languageDirection; }
}