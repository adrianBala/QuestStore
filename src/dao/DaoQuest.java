package dao;

import model.*;
import java.util.ArrayList;

public class DaoQuest{

    public Quest createQuest(String name, int value, String description, String type, String category){
        return new Quest(name, value, description, type, category);
    }

    public Quest createQuest(int itemId, String name, int value, String description, String type, String category){
        return new Quest(itemId,name, value, description, type, category);
    }

    
    
}