/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.login;

/**
 *
 * @author roger
 */
class currentUser {
    
    private String userName;
    private String accessLevel;
    private int userId;

    public currentUser(String userName, String accessLevel, int userId) {
        this.userName = userName;
        this.accessLevel = accessLevel;
        this.userId = userId;
    }
    
    
}
