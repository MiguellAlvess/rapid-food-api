package br.com.db.rapid_food_api.user.repository.scenarios;

public enum ExistsByEmailScenario {

    EMAIL_EXISTS(
        "email is already registered",
        "miguel@email.com",
        "miguel@email.com",
        true
    );

    public final String description;
    public final String persistEmail;
    public final String queryEmail;
    public final boolean expectedResult;
    
    private ExistsByEmailScenario(String description, String persistEmail, String queryEmail, boolean expectedResult) {
        this.description = description;
        this.persistEmail = persistEmail;
        this.queryEmail = queryEmail;
        this.expectedResult = expectedResult;
    }

    @Override
    public String toString(){
        return description;
    }
}
