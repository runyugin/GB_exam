package Animals;

import Animals.Controller.PetsController;
import Animals.UsersInterface.ConsoleMenu;
import Animals.ObjectsPets.*;
import Animals.Servises.*;


public class Programm {
    public static void main(String[] args) throws Exception {

        AnimalsRepository <Pet> myFarm = new PRepository();
        PetsController controller = new PetsController(myFarm);
        new ConsoleMenu (controller).start();
    }

}
