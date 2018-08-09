/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.main.utils;

import javafx.util.converter.DateStringConverter;

import java.sql.Date;

/**
 * @author roger
 */
public class MyDateStringConverter extends DateStringConverter
{

    public MyDateStringConverter(final String pattern)
    {
        super(pattern);
    }

    @Override
    public Date fromString(String value)
    {
        //catches the Runtimexception thrown by
        //DateStringConverter.fromString()
        try
        {
            return (Date) super.fromString(value);
        } catch (RuntimeException ex)
        {
            return null;
        }
    }
}
