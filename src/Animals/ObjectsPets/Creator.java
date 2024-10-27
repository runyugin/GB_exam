package Animals.ObjectsPets;

import java.time.LocalDate;

public abstract class Creator {
    protected abstract Pet createNewPet(TypeOfPet type);

    public Pet createPet(TypeOfPet type, String petName, LocalDate date) {

        Pet pet = createNewPet(type);
        pet.setName(petName);
        pet.setBirthday(date);
        return pet;
    }
}
