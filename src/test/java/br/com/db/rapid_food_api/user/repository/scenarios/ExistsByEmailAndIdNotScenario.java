package br.com.db.rapid_food_api.user.repository.scenarios;

public enum ExistsByEmailAndIdNotScenario {
    
    EMAIL_EXISTS(
        "email is already registered",
        "miguel@gmail.com",
        true
    ),

    EMAIL_DOESNT_EXIST(
        "email isn't registered",
        "teste@email.com",
        false
    ),

    UPPERCASE_EMAIL(
        "same email in uppercase returns false(case sensitive)",
        "MIGUEL@EMAIL.COM",
        false
    ),

    MIXED_CASE_EMAIL(
        "same email in mixed case returns false (case sensitive)",
        "Miguel@Email.Com",
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
