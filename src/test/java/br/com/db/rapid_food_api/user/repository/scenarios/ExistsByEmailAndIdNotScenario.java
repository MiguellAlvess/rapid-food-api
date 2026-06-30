package br.com.db.rapid_food_api.user.repository.scenarios;

public enum ExistsByEmailAndIdNotScenario {
    
    EMAIL_BELONGS_TO_ANOTHER_USER(
        "email is already registered",
        "miguel@gmail.com",
        true
    ),

    EMAIL_DOESNT_EXIST(
        "email isn't registered",
        "teste@email.com",
        false
    ),

    SAME_EMAIL_BEING_UPDATED(
        "email already belongs to the updated user",
        "outro@gmail.com",
        false
    );

    public final String description;
    public final String updatedEmail;
    public final boolean expectedResult;
    
    private ExistsByEmailAndIdNotScenario(String description, String updatedEmail, boolean expectedResult) {
        this.description = description;
        this.updatedEmail = updatedEmail;
        this.expectedResult = expectedResult;
    }

    @Override
    public String toString(){
        return description;
    }
}
