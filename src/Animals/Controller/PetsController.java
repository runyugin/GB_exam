package Animals.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import Animals.ObjectsPets.*;
import Animals.Servises.*;
import Animals.UsersInterface.*;


public class PetsController {
    private AnimalsRepository <Pet> petRepository;
    private Creator petCreator;
    private final View <Pet> view;
    private Validator validator;

    public PetsController(AnimalsRepository <Pet> petRepository) {
        this.petRepository = petRepository;
        this.petCreator = new CreatorPet();
        this.view = new ConsoleView();
        this.validator = new Validator();
    }

    public void createPet(TypeOfPet type) {

        String[] data = new String[] { view.getName(), view.getBirthday() };
        validator.validate(data);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthday = LocalDate.parse(data[1], formatter);
        try {
            int result = petRepository.create(petCreator.createPet(type, data[0], birthday));
            view.showMessage(String.format("%d запись добавлена", result));
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }

    }

    public void updatePet(int id) {

        Pet pet = getById(id);
        String[] data = new String[] { view.getName(), view.getBirthday() };

        validator.validate(data);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthday = LocalDate.parse(data[1], formatter);
        pet.setName(data[0]);
        pet.setBirthday(birthday);
        try {
            int result = petRepository.update(pet);
            view.showMessage(String.format("%d запись изменена \n", result));
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }

    }

    public void getAllPet() {
        try {
            view.printAll(petRepository.getAll(), Pet.class);
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }
    }

    public boolean trainPet(int id, String command) {
        try {

            if (((PRepository) petRepository).getCommandsById(id, 1).contains(command))
                view.showMessage("Это мы уже умеем");
            else {
                if (!((PRepository) petRepository).getCommandsById(id, 2).contains(command))
                    view.showMessage("Невыполнимая команда");
                else {
                    ((PRepository) petRepository).train(id, command);
                    view.showMessage("Команда разучена\n");
                    return true;
                }
            }
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }
        return false;
    }

    public Pet getById(int id) {
        try {
            return petRepository.getById(id);
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }
        return null;
    }

    public void delete(int id) {
        try {
            petRepository.delete(id);
            view.showMessage("Запись удалена");
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }
    }

    public void getCommands(int id) {
        try {
            view.printAll(((PRepository) petRepository).getCommandsById(id, 1), String.class);
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }
    }
}
