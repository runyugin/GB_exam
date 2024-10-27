package Animals.UsersInterface;

import java.util.Scanner;

import Animals.Controller.CounterPets;
import Animals.Controller.PetsController;
import Animals.Exceptions.UncorrectDataException;
import Animals.ObjectsPets.TypeOfPet;

public class ConsoleMenu {
    PetsController petController;

    public ConsoleMenu(PetsController controller) {
        this.petController = controller;
    }

    public void start() {

        System.out.print("\033[H\033[J");
        try (Scanner sc = new Scanner(System.in, "ibm866"); CounterPets count = new CounterPets()) {

            boolean flag = true;
            int id;
            while (flag) {

                System.out.println(
                        "\n1 - Список всех животных\n2 - Завести новое животное\n3 - Изменить данные о животном\n4 - Что умеет животное\n5 - Дрессировка\n6 - Удалить запись\n0 - Выход");
                String key = sc.next();
                switch (key) {
                    case "1":
                        petController.getAllPet();
                        break;
                    case "2":
                        TypeOfPet type = menuChoice(sc);
                        if (type != null) {
                            try {
                                petController.createPet(type);
                                count.add();
                                System.out.println("ОК");
                            } catch (UncorrectDataException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        break;
                    case "3":
                        while (true) {
                            id = menuChoicePet(sc);
                            if (id != 0)
                                try {
                                    petController.updatePet(id);
                                } catch (UncorrectDataException e) {
                                    System.out.println(e.getMessage());
                                }
                            else
                                break;
                        }
                        break;
                    case "4":
                        while (true) {
                            id = menuChoicePet(sc);
                            if (id != 0)
                                petController.getCommands(id);
                            else
                                break;
                        }
                        break;
                    case "5":
                        id = menuChoicePet(sc);
                        if (id != 0)
                            menuTrainPet(id, sc);
                        break;
                    case "6":
                        id = menuChoicePet(sc);
                        if (id != 0)
                            petController.delete(id);
                        break;
                    case "0":
                        flag = false;
                        break;
                    default:
                        System.out.println("Такой цифры нет в меню");
                        break;
                }
            }
        }
    }

    private TypeOfPet menuChoice(Scanner sc) {
        System.out.println("Какое животное добавить:\n1 - Кошка\n2 - Собака\n3 - Хомяк\n0 - Возврат в основное меню");

        while (true) {
            String key = sc.next();
            switch (key) {
                case "1":
                    return TypeOfPet.Cat;
                case "2":
                    return TypeOfPet.Dog;
                case "3":
                    return TypeOfPet.Hamster;
                case "0":
                    return null;
                default:
                    System.out.println("Такой цифры нет в меню");
                    break;
            }
        }
    }

    private int menuChoicePet(Scanner sc) {
        System.out.println("\nВведите номер животного, 0 для возврата в основное меню: ");
        while (true) {
            int id = sc.nextInt();
            sc.nextLine();
            if (id == 0)
                return id;
            if (petController.getById(id) == null) {
                System.out.println("Животного с таким номером нет, попробуйте еще раз, 0 для возврата в основное меню:");
            } else
                return id;

        }
    }

    private void menuTrainPet(int petID, Scanner sc) {
        Scanner s = sc;
        while (true) {
            System.out.println("Введите новую команду, 0 для возврата в основное меню: ");
            String command = s.nextLine();
            if (command.length() == 1 && command.equals("0"))
                return;
            if (petController.trainPet(petID, command))
                System.out.println("Команда изучена");
        }
    }
}
