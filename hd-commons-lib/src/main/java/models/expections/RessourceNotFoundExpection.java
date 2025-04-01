package models.expections;

public class RessourceNotFoundExpection extends RuntimeException{
    public RessourceNotFoundExpection(String message) {
        super(message);
    }
}
