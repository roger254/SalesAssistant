/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.util;

import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author roger
 */
public class SalesAssistantUtil
{
    public static final String ICON_LOCATION = "/resources/icon.png";

    public static void setStageIcon(Stage stage)
    {
        stage.getIcons().add(new Image(ICON_LOCATION));
    }
}
