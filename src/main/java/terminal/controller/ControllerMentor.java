package terminal.controller;

import terminal.controller.view.ViewMentor;
import system.dao.*;
import system.model.*;

import java.util.List;

public class ControllerMentor implements IUserController{
    private ViewMentor viewMentor;
    private Mentor mentor;
    private IDaoWallet daoWallet;
    private IDaoTeam daoTeam;
    private IDaoQuest daoQuest;
    private IDaoArtifact daoArtifact;
    private IDaoStudent daoStudent;
    private IDaoClass daoClass;

    public ControllerMentor(Mentor mentor, ViewMentor viewMentor,
                            IDaoStudent daoStudent, IDaoClass daoClass,
                            IDaoArtifact daoArtifact, IDaoQuest daoQuest,
                            IDaoTeam daoTeam, IDaoWallet daoWallet){
        this.viewMentor = viewMentor;
        this.mentor = mentor;
        this.daoStudent = daoStudent;
        this.daoClass = daoClass;
        this.daoArtifact = daoArtifact;
        this.daoQuest = daoQuest;
        this.daoTeam = daoTeam;
        this.daoWallet = daoWallet;
    }

    private void createStudent() {
        String studentName = viewMentor.getInputFromUser("Enter name of new student: ");
        String studentPassword = viewMentor.getInputFromUser("Enter password of new student: ");
        String studentEmail = viewMentor.getInputFromUser( "Enter email of new student: ");

        Student student = daoStudent.createStudent(studentName, studentPassword, studentEmail);

        if(student.getUserId() !=0) {
            Student studentWithId = daoStudent.importNewStudent(studentEmail);

            Wallet wallet = daoWallet.createWallet(student);
            studentWithId.setWallet(wallet);

            CodecoolClass codecoolClass = getCodecoolClass();
            daoClass.assignStudentToClass(studentWithId.getUserId(), codecoolClass.getGroupId());
        }
        else{
            viewMentor.displayText("Creation student failed");
        }

    }

    private void createTeam() {
        String teamName = viewMentor.getInputFromUser("Enter name of new team: ");

        if(daoTeam.createTeam(teamName).getGroupId() != 0){
            viewMentor.displayText("Creation team successful");
        }else{
            viewMentor.displayText("Creation team failed");
        }
    }

    public Student getStudent(){
        Student student = null;

        viewMentor.displayText("Available students:\n");
        List<Student> allStudents = daoStudent.getAllStudents();
        if(allStudents.size() != 0) {
            viewMentor.displayList(allStudents);
            Integer studentId = viewMentor.getIntInputFromUser("\nEnter id of student: ");
            student = daoStudent.importStudent(studentId);
        }else{
            viewMentor.displayText("No students");
        }

        return student;
    }

    private CodecoolClass getCodecoolClass(){
        CodecoolClass chosenClass = null;
        while(isClassNotChosen(chosenClass)) {
            viewMentor.displayText("Available classes: ");
            List <CodecoolClass> allClasses = daoClass.getAllClasses();
            if(allClasses.size() != 0) {
                for (CodecoolClass codecoolClass : allClasses) {
                    viewMentor.displayText(codecoolClass.getBasicInfo());
                }

                Integer classId = viewMentor.getIntInputFromUser("\nEnter id of chosen class: ");
                chosenClass = daoClass.importClass(classId);
            }else{
                viewMentor.displayText("No classes");
            }

            if (isClassNotChosen(chosenClass)) {
                viewMentor.displayText("Wrong id of class. Try again.");
            }
        }
        return chosenClass;
    }

    private boolean isClassNotChosen(CodecoolClass codecoolClass) {
        return codecoolClass == null;
    }

    public Team getTeam(){
        Team chosenTeam = null;

        viewMentor.displayText("Available teams:\n");
        List<Team> teams = daoTeam.getAllTeams();
        if(teams.size() != 0) {
            for (Team team : teams) {
                viewMentor.displayText(team.getBasicInfo());
            }

            int teamId = viewMentor.getIntInputFromUser("\nEnter id of team: ");
            chosenTeam = daoTeam.importTeam(teamId);
        }else {
            viewMentor.displayText("No teams");
        }

        return chosenTeam;
    }

    private Quest getTeamQuest() {
        Quest chosenQuest = null;

        viewMentor.displayText("Available team quests:\n");
        List<Quest> allQuests = daoQuest.getTeamQuests();

        if(allQuests.size() != 0) {
            viewMentor.displayList(allQuests);
            Integer questId = viewMentor.getIntInputFromUser("Enter id of chosen quest");
            chosenQuest = daoQuest.importQuest(questId);
        }else {
            viewMentor.displayText("No team quests");
        }
        return chosenQuest;
    }

    private Quest getIndividualQuest(){
        Quest chosenQuest = null;

        List<Quest> allQuests = daoQuest.getIndividualQuests();
        viewMentor.displayText("Available individual quests:\n");

        if(allQuests.size() != 0) {
            viewMentor.displayList(allQuests);
            Integer questId = viewMentor.getIntInputFromUser("Enter id of chosen quest");
            chosenQuest = daoQuest.importQuest(questId);
        }else {
            viewMentor.displayText("No individual quests");
        }
        return chosenQuest;
    }

    private void addQuest(){
        String questName = viewMentor.getInputFromUser("Enter name of new quest: ");
        int questValue = viewMentor.getIntInputFromUser( "Enter value of new quest: ");
        String questDescription = viewMentor.getInputFromUser("Enter description of quest: ");
        String questType = chooseType();
        String questCategory = chooseCategory();

        if(daoQuest.createQuest(questName, questValue, questDescription, questType, questCategory).getItemId() != 0){
            viewMentor.displayText("Creation quest successful");
        }else {
            viewMentor.displayText("Creation quest failed");
        }
    }

    private void addArtifact() {
        String artifactName = viewMentor.getInputFromUser("Enter name of new artifact: ");
        int artifactValue = viewMentor.getIntInputFromUser("Enter value of new artifact: ");
        String artifactDescription = viewMentor.getInputFromUser("Enter description of new artifact");
        String artifactStatus = chooseType();

        if(daoArtifact.createArtifact(artifactName, artifactValue, artifactDescription, artifactStatus).getItemId() != 0){
            viewMentor.displayText("Creation artifact successful");
        }else {
            viewMentor.displayText("Creation artifact failed");
        }
    }

    private String chooseType() {

        String statusRequest = "Choose type:\n1. Individual\n2. Team\nOption: ";
        String status = null;
        boolean choosingStatus = true;
        int option;
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
        String typeRequest = "Choose category:\n1. Basic\n2. Extra\nOption: ";
        String type = null;
        boolean choosingType = true;
        int option ;
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
        Quest chosenQuest = null;

        viewMentor.displayText("Available quests:\n");
        List<Quest> allQuests = daoQuest.getAllQuests();

        if(allQuests.size() != 0) {
            viewMentor.displayList(allQuests);
            int questId = viewMentor.getIntInputFromUser("\nEnter id of quest: ");
            chosenQuest = daoQuest.importQuest(questId);
        }else {
            viewMentor.displayText("No quests");
        }
        return chosenQuest;
    }

    private void updateQuest(){
        Quest quest = getQuest();
        if(quest == null){
            return;
        }

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

    private void updateQuestName(Quest quest){
        String name = viewMentor.getInputFromUser("Pass new quest name: ");
        quest.setName(name);
        boolean isUpdate = daoQuest.updateQuest(quest);
        updateInfo(isUpdate);
    }

    private void updateQuestDescription(Quest quest){
        String description = viewMentor.getInputFromUser("Pass new quest description: ");
        quest.setDescription(description);
        boolean isUpdate = daoQuest.updateQuest(quest);
        updateInfo(isUpdate);
    }

    private void updateQuestValue(Quest quest){
        Integer value = viewMentor.getIntInputFromUser("Pass new quest value: ");
        quest.setValue(value);
        boolean isUpdate = daoQuest.updateQuest(quest);
        updateInfo(isUpdate);
    }

    private void updateQuestType(Quest quest){
        String type = chooseType();
        quest.setType(type);
        boolean isUpdate = daoQuest.updateQuest(quest);
        updateInfo(isUpdate);
    }

    private void updateQuestCategory(Quest quest){
        String category = chooseCategory();
        quest.setCategory(category);
        boolean isUpdate = daoQuest.updateQuest(quest);
        updateInfo(isUpdate);
    }

    private void updateInfo(boolean isInsert){
        if(isInsert){
            viewMentor.displayText("Update was succesful");
        }else {
            viewMentor.displayText("Update failed");
        }
    }

    private void updateArtifact() {
        Artifact artifact = getArtifact();
        if(artifact == null){
            return;
        }

        boolean toContinue = true;
        do{
            viewMentor.displayList(viewMentor.getUpdateArtifactsOptions());
            String chosenOption = viewMentor.getInputFromUser("Choose option: ");
            switch(chosenOption){
                case "1": updateArtifactName(artifact);
                    break;
                case "2": updateArtifactDescription(artifact);
                    break;
                case "3": updateArtifactValue(artifact);
                    break;
                case "4": updateArtifactType(artifact);
                    break;
                case "0": toContinue = false;
                    break;
                default: viewMentor.displayText("Wrong option. Try again!");
                    break;
            }
        }while(toContinue);

    }

    private void updateArtifactName(Artifact artifact){
        String name = viewMentor.getInputFromUser("Choose new name: ");
        artifact.setName(name);
        boolean isUpdate = daoArtifact.updateArtifact(artifact);
        updateInfo(isUpdate);
    }
    private void updateArtifactValue(Artifact artifact){
        Integer value = viewMentor.getIntInputFromUser("Choose new value: ");
        artifact.setValue(value);
        boolean isUpdate = daoArtifact.updateArtifact(artifact);
        updateInfo(isUpdate);
    }

    private void updateArtifactDescription(Artifact artifact){
        String description = viewMentor.getInputFromUser("Choose new description: ");
        artifact.setDescription(description);
        boolean isUpdate = daoArtifact.updateArtifact(artifact);
        updateInfo(isUpdate);
    }
    private void updateArtifactType(Artifact artifact){
        String type = null;
        boolean toContinue = true;
        do{
            viewMentor.displayList(viewMentor.getUpdateArtifactTypeOptions());
            String userChoice = viewMentor.getInputFromUser("Choose type: ");
            switch (userChoice){
                case "1": type = "individual";
                    toContinue = false;
                    break;
                case "2": type = "team";
                    toContinue = false;
                    break;
                case "0": toContinue = false;
                    break;
                default: viewMentor.displayText("Wrong option. Try again!");
            }
        }while (toContinue);

        if(type != null){
            artifact.setType(type);
            boolean isUpdate = daoArtifact.updateArtifact(artifact);
            updateInfo(isUpdate);
        }
    }

    private boolean seeAllArtifacts() {
        List<Artifact> artifactList = daoArtifact.getAllArtifacts();
        boolean isListNotEmpty = false;

        viewMentor.displayText("List of artifacts:");
        if(artifactList.size() != 0) {
            viewMentor.displayList(artifactList);
            isListNotEmpty = true;
        }else {
            viewMentor.displayText("No artifacts");
        }
        return isListNotEmpty;
    }

    private Artifact getArtifact() {
        Artifact artifact = null;
        if (seeAllArtifacts()) {
            int artifactId = viewMentor.getIntInputFromUser("\nEnter id of artifact: ");
            artifact = daoArtifact.importArtifact(artifactId);
        }
        return artifact;
    }


    private void markQuest() {
        String mentorOption = "";
        while (!mentorOption.equals("0")) {

            viewMentor.displayText("\nWhat would like to do?");
            viewMentor.displayList(viewMentor.getChooseTeamOrStudent());

            mentorOption = viewMentor.getInputFromUser("Option: ");
            switch (mentorOption) {
                case "1": markTeamAchivedQuest();
                    break;
                case "2":markStudentAchivedQuest();
                    break;
                case "0": break;
                default: viewMentor.displayText("Wrong option. Try again!");
                    break;
            }
        }
    }


    private void markBoughtArtifact(){
        Student student = getStudent();
        if(student == null){
            return;
        }

        viewMentor.displayText("Student new artifacts:\n");
        List<Artifact> allNewArtifacts = student.getAllNewArtifacts();  //nie wszytskie, tylko indywidualne (bez teamowych)
        Artifact artifactToBeBougth = null;

        if(allNewArtifacts.size() != 0) {
            viewMentor.displayList(allNewArtifacts);
            Integer artifactId = viewMentor.getIntInputFromUser("Choose id artifact to be marked as used: ");

            artifactToBeBougth = daoArtifact.importArtifact(artifactId);
            student.markArtifactAsBougth(artifactToBeBougth);

            daoWallet.updateStudentsArtifact(artifactToBeBougth.getItemId(), student.getUserId());
        }
    }

    private void markStudentAchivedQuest() {
        Student student = getStudent();
        if(student == null){
            return;
        }

        Quest quest = getIndividualQuest();
        if(quest == null){
            return;
        }

        int coins = quest.getValue();
        student.addCoins(coins);
        daoWallet.updateWallet(student);

    }

    private void markTeamAchivedQuest() {
        Team team = getTeam();
        if(team == null){
            return;
        }

        Quest quest = getTeamQuest();
        if(quest == null){
            return;
        }

        int coins = quest.getValue();
        team.addCoins(coins);
        daoTeam.updateTeamData(team);

    }

    private void seeAllWallets() {
        CodecoolClass mentorsClass = daoClass.getMentorsClass(mentor.getUserId());
        if(mentorsClass == null){
            viewMentor.displayText("Mentor is not assigned to any class");
            return;
        }

        viewMentor.displayText("Wallets of students of class: \n");
        for (Student student : mentorsClass.getStudents()) {
            Wallet wallet = student.getWallet();
            viewMentor.displayText(student.toString());
            viewMentor.displayText(wallet.toString());
        }
    }

    private void assignStudentsToTeam(){
        Team team = getTeam();
        if(team == null){
            return;
        }

        boolean toContinue = true;
        do{
            viewMentor.displayList(viewMentor.getAssignStudentToTeamOptions());
            String chosenOption = viewMentor.getInputFromUser("Choose option: ");
            switch(chosenOption){
                case "1": assignStudentToTeam(team);
                    break;
                case "0": toContinue = false;
                    break;
                default: viewMentor.displayText("Wrong option. Try again!");
                    break;
            }
        }while(toContinue);
    }

    private void assignStudentToTeam(Team team){
        Student student = getStudent();
        if(student != null) {
            daoTeam.assignStudentToTeam(student.getUserId(), team.getGroupId());
        }
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
                case "8": markBoughtArtifact();
                        break;
                case "9": seeAllWallets();
                        break;
                case "10": assignStudentsToTeam();
                        break;
                case "0": break;

                default: viewMentor.displayText("Wrong option. Try again!");
                         break;
            }
        }

    }

}
