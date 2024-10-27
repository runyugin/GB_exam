package Animals.ObjectsPets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Pet {
    protected int petID;
    protected String petName;
    protected LocalDate birthday;

    public void setPetId(int petID) {
        this.petID = petID;
    }

    public int getPetID() {
        return petID;
    }

    public void setName(String petName) {
        this.petName = petName;
    }

    public String getName() {
        return petName;
    }

    public void setBirthday(LocalDate date) {
        this.birthday = date;
    }

    public LocalDate getBirthdayDate(){
        return birthday;
    }

    public String getBirthday() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return formatter.format(birthday);
    }

    @Override
    public String toString() {
        return String.format("%d. %s: имя: %s, дата рождения: %s ", getPetID(), getClass().getSimpleName(), petName, getBirthday());
    }
}
