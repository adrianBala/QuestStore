package terminal.controller;

import system.dao.*;

import system.model.*;
import system.model.Artifact;
import system.model.Student;
import system.model.Level;
import terminal.controller.view.ViewStudent;
import terminal.controller.view.ViewTeam;

import java.util.List;

public class ControllerStudent implements IUserController{

    private ViewStudent viewStudent;
    private Student student;
    private IDaoWallet daoWallet;
    private IDaoStudent daoStudent;
    private IDaoArtifact daoArtifact;
    private IDaoLevel daoLevel;
    private IDaoTeam daoTeam;

    ControllerStudent(Student student, ViewStudent viewStudent,
                             IDaoWallet daoWallet, IDaoStudent daoStudent,
                             IDaoArtifact daoArtifact, IDaoLevel daoLevel,
                             IDaoTeam daoTeam) {
        this.viewStudent = viewStudent;
        this.student = student;
        this.daoWallet = daoWallet;
        this.daoStudent = daoStudent;
        this.daoArtifact = daoArtifact;
        this.daoLevel = daoLevel;
        this.daoTeam = daoTeam;
    }

    private void seeWallet() {
        viewStudent.displayText(student.getWallet().toString());
    }

    private void buyArtifact() {
        Artifact artifact = getArtifact("individual");
        if(artifact != null && artifact instanceof Artifact) {

            int artifactId = artifact.getItemId();
            int studentId = student.getUserId();

            int price = artifact.getValue();
            if (student.hasEnoughCoins(price)) {
                student.subtractCoins(price);
                student.addNewArtifact(artifact);

                daoWallet.updateWallet(student);
                daoWallet.exportStudentArtifact(artifactId, studentId);

            } else {
                viewStudent.displayText("You do not have enough money to buy this artifact!");
            }
        }
        else {
            viewStudent.displayText("\nWrong id of artifact.");
        }
    }

    private Artifact getArtifact(String type) {
        viewStudent.displayText("Available artifacts:\n");
        List<Artifact> allArtifacts = daoArtifact.getArtifacts(type);
        Artifact artifact = null;

        if(allArtifacts.size() != 0) {
            viewStudent.displayList(allArtifacts);
            int artifactId = viewStudent.getIntInputFromUser("\nEnter id of artifact: ");
            for(Artifact choosenArtifact : allArtifacts) {
                if(choosenArtifact.getItemId() == artifactId) {
                    return choosenArtifact;
                }
            }
        }else {
            viewStudent.displayText("\nNo artifacts");
        }

        return artifact;
    }

    private void seeExpLevel() {
        Level level = daoLevel.importLevelByCoins(this.student.getWallet().getAllCoins());
        if (level == null){
            return;
        }
        viewStudent.displayText("Your wallet: ");
        viewStudent.displayText(student.getWallet().toString());
        viewStudent.displayText("Your level: ");
        String levelString = level.toString();
        viewStudent.displayText(levelString);
    }

    private void manageTeam() {
        Team team = daoTeam.getTeamByStudentId(student.getUserId());

        if (team != null) {
            ControllerTeam controllerTeam = new ControllerTeam(team, new ViewTeam(), daoArtifact, daoTeam, daoWallet);
            controllerTeam.runMenu();
            student = daoStudent.importStudent(student.getUserId());
        }
    }

    public void runMenu() {

        String studentOption = "";
        while (!studentOption.equals("0")) {

            viewStudent.displayText("\nWhat would like to do?");
            viewStudent.displayList(viewStudent.getStudentOptions());

            studentOption = viewStudent.getInputFromUser("Option: ");
            switch (studentOption) {
                case "1": seeWallet();
                        break;
                case "2": buyArtifact();
                        break;
                case "3": seeExpLevel();
                        break;
                case "4": manageTeam();
                        break;
                case "0": break;

                default: viewStudent.displayText("Wrong option. Try again!");
                         break;
            }
        }

    }

}
