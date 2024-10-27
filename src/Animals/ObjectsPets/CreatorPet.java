package Animals.ObjectsPets;

public class CreatorPet extends Creator{
    @Override
    protected Pet createNewPet (TypeOfPet type) {

        switch (type) {
            case Cat:
                return new Cat();
            case Dog:
                return new Dog();
            case Hamster:
                return new Hamster();
        }
        return null;
    }
}
