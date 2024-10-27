package Animals.ObjectsPets;

public enum TypeOfPet {
    Dog,
    Cat,
    Hamster;

    public static TypeOfPet getType(int typeID){
        switch (typeID) {
            case 1:
                return TypeOfPet.Dog;
            case 2:
                return TypeOfPet.Cat;
            case 3:
                return TypeOfPet.Hamster;
            default:
                return null;
        }
    }
}
