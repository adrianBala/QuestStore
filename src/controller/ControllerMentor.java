package controller;

import view.ViewMentor;
import dao.*;
import iterator.MyIterator;
import model.*;

import java.util.ArrayList;

public class ControllerMentor implements IUserController{
    private ViewMentor viewMentor;
    private Mentor mentor;

    public ControllerMentor(Mentor mentor){
        this.viewMentor = new ViewMentor();
        this.mentor = mentor;
    }

    public void createStudent() {
        
        DaoStudent daoStudent = new DaoStudent();
        String studentName = viewMentor.getInputFromUser("Enter name of new student: ");
        String studentPassword = viewMentor.getInputFromUser("Enter password of new student: ");
        String studentEmail = viewMentor.getInputFromUser( "Enter email of new student: ");

        Student student = daoStudent.createInstance(studentName, studentPassword, studentEmail);
        daoStudent.exportInstance(student);

    }

    public void createTeam() {
//        DaoTeam daoTeam = new DaoTeam();
//
//        String nameRequest = "Enter name of new team: ";
//        String teamName = viewMentor.getInputFromUser(nameRequest);
//
//        daoTeam.createTeam(teamName);
    }

    public void addQuest(){
//        DaoQuest daoQuest = new DaoQuest();
//
//        String nameRequest = "Enter name of new quest: ";
//        String questName = viewMentor.getInputFromUser(nameRequest);
//
//        String valueRequest = "Enter value of new quest: ";
//        int questValue = viewMentor.getIntInputFromUser(valueRequest);
//
//        String descriptionRequest = "Enter description of quest";
//        String questDescription = viewMentor.getInputFromUser(descriptionRequest);
//
//        String questStatus = chooseStatus();
//        String questType = chooseType();
//
//        daoQuest.createQuest(questName, questValue, questDescription, questStatus, questType);
    }

    public void addArtifact() {
//        DaoArtifact daoArtifact = new DaoArtifact();
//
//        String nameRequest = "Enter name of new artifact: ";
//        String artifactName = viewMentor.getInputFromUser(nameRequest);
//
//        String valueRequest = "Enter value of new artifact: ";
//        int artifactValue = viewMentor.getIntInputFromUser(valueRequest);
//
//        String descriptionRequest = "Enter description of new artifact";
//        String artifactDescription = viewMentor.getInputFromUser(descriptionRequest);
//
//        String artifactStatus = chooseStatus();
//        daoArtifact.createArtifact(artifactName, artifactValue, artifactDescription, artifactStatus);
    }

    private String chooseType() {
        String statusRequest = "Choose status:\n1. Individual\n2. Team\nOption: ";
        String status = null;
        boolean choosingStatus = true;
        int option = 0;
        while(choosingStatus) {
            option = viewMentor.getIntInputFromUser(statusRequest);
            switch(option) {
                case 1:
                    status = "individual";
                    choosingStatus = false;
                    break;
                case 2:
                    status = "team";
                    choosingStatus = false;
                    break;
                default:
                    viewMentor.displayText("Wrong option number!");
            }
        }
        return status;
    }

    private String chooseCategory() {
        String typeRequest = "Choose type:\n1. Basic\n2. Extra\nOption: ";
        String type = null;
        boolean choosingType = true;
        int option = 0;
        while(choosingType) {
            option = viewMentor.getIntInputFromUser(typeRequest);
            switch(option) {
                case 1:
                    type = "basic";
                    choosingType = false;
                    break;
                case 2:
                    type = "extra";
                    choosingType = false;
                    break;
                default:
                    viewMentor.displayText("Wrong option number!");
            }
        }
        return type;
    }

    public Quest getQuest(){
        DaoQuest daoQuest = new DaoQuest();

        viewMentor.displayText("Available quests:\n");
        viewMentor.displayList(daoQuest.getAllQuests());

        int questId = viewMentor.getIntInputFromUser("\nEnter id of quest: ");

        return daoQuest.importQuest(questId);
    }

    public void updateQuest(){
        Quest quest = getQuest();

        boolean toContinue = true;
        do{
            viewMentor.displayList(viewMentor.getEditQuestOptions());
            String chosenOption = viewMentor.getInputFromUser("Choose option: ");
            switch(chosenOption){
                case "1": updateQuestName(quest);
                    break;
                case "2": updateQuestDescription(quest);
                    break;
                case "3": updateQuestValue(quest);
                    break;
                case "4": updateQuestType(quest);
                    break;
                case "5": updateQuestCategory(quest);
                    break;
                case "0": toContinue = false;
                    break;
                default: viewMentor.displayText("Wrong option. Try again!");
                    break;
            }
        }while(toContinue);
    }

    public void updateQuestName(Quest quest){
        String name = viewMentor.getInputFromUser("Pass new quest name: ");
        quest.setName(name);
        new DaoQuest().updateQuest(quest);
    }

    public void updateQuestDescription(Quest quest){
        String description = viewMentor.getInputFromUser("Pass new quest description: ");
        quest.setDescription(description);
        new DaoQuest().updateQuest(quest);
    }

    public void updateQuestValue(Quest quest){
        Integer value = viewMentor.getIntInputFromUser("Pass new quest value: ");
        quest.setValue(value);
        new DaoQuest().updateQuest(quest);
    }

    public void updateQuestType(Quest quest){
        String type = chooseType();
        quest.setType(type);
        new DaoQuest().updateQuest(quest);
    }

    public void updateQuestCategory(Quest quest){
        String category = chooseCategory();
        quest.setCategory(category);
        new DaoQuest().updateQuest(quest);
    }

    public void updateArtifact() {
        //seeArtifacts();
        toBeImplemented();
        
    }

    public void toBeImplemented(){
        String text = "Implementation in progress";
        viewMentor.displayText(text);
    }

    public void markQuest() {
        toBeImplemented();
    }

    public void markArtifact() {
        toBeImplemented();
    }

    public void markStudentAchivedQuest() {
        toBeImplemented();
    }

    public void markTeamAchivedQuest() {
        toBeImplemented();
    }

    public void markStudentBoughtArtifact() {
        toBeImplemented();
    }

    public void markTeamBoughtArtifact() {
        toBeImplemented();
    }

    public void seeAllWallets() {
//        int idOfMentorClass = mentor.getClassId();
//        viewMentor.displayText("Wallets of students of class: ");
//
//        DaoStudent daoStudent = new DaoStudent();
//        ArrayList<Student> allStudentsOfClass = daoStudent.getStudentsByClassId(idOfMentorClass);
//        MyIterator <Student> myIterator = new MyIterator<>(allStudentsOfClass);
//        while(myIterator.hasNext()){
//            Student student = myIterator.next();
//            viewMentor.displayText(student.toString() + "/n" + student.getWallet().toString());
//        }
    }
    private ArrayList<Wallet> getWalletsOfStudents(ArrayList<Student> students) {
        ArrayList<Wallet> walletsOfStudents = new ArrayList<>();
        for (Student student : students) {
            walletsOfStudents.add(student.getWallet());
        }
        return walletsOfStudents;
    }

    public void seeQuests() {
//        viewMentor.displayText("Available quests: ");
//        viewMentor.displayList(new DaoArtifact().importData());
    }

    public void seeArtifacts() {
//        viewMentor.displayText("Available artifacts: ");
//        viewMentor.displayList(new DaoArtifact().importData());
    }

    public void runMenu() {
        String mentorOption = "";
        while (!mentorOption.equals("0")) {

            viewMentor.displayText("\nWhat would like to do?");
            viewMentor.displayList(viewMentor.getMentorOptions());

    mentorOption = viewMentor.getInputFromUser("Option: ");
    switch (mentorOption) {
        case "1": createStudent();
                break;
        case "2": createTeam();
                break;
        case "3": addQuest();
                break;
        case "4": addArtifact();
                break;
        case "5": updateQuest();
                break;
        case "6": updateArtifact();
                break;
        case "7": markQuest();
                break;
        case "8": markArtifact();
                break;
        case "9": seeAllWallets();
                break;
        case "0": break;

        default: viewMentor.displayText("Wrong option. Try again!");
                 break;
    }
        }

    }

}
