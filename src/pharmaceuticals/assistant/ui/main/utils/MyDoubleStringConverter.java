/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.main.utils;

import javafx.util.converter.DoubleStringConverter;

/**
 * @author roger
 */
public class MyDoubleStringConverter extends DoubleStringConverter
{
    @Override
    public Double fromString(final String value)
    {
        return value.isEmpty() || !isNumber(value) ? null : super.fromString(value);
    }

    public boolean isNumber(String value)
    {
        int size = value.length();
        for (int i = 0; i < size; i++)
        {
            if (!Character.isDigit(value.charAt(i)))
                return false;
        }
        return size > 0;
    }
}
