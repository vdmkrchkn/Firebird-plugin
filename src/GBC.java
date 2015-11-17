/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;

/**
 * Этот класс упрощает работу с классом GridBagConstraints
 * @author Cay Horstmann
 */
@SuppressWarnings("serial")
public class GBC extends GridBagConstraints {    

    /**
     * Создание объекта GBC, определяя параметры gridx,gridy
     * Остальные параметры принимаются по-умолчанию
     * @param gridx Позиция gridx
     * @param gridy Позиция gridy
     */
    public GBC(int gridx,int gridy)
    {
        this.gridx = gridx;
        this.gridy = gridy;
    }

    /**
     * Создание объекта GBC, определяя параметры gridx,gridy,gridwidth,gridheight
     * Остальные параметры принимаются по-умолчанию
     * @param gridx Позиция gridx
     * @param gridy Позиция gridy
     * @param gridwidth Расширение ячейки в направлении x 
     * @param gridheight Расширение ячейки в направлении y
     */
    public GBC(int gridx,int gridy,int gridwidth,int gridheight)
    {
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
    }
    
    /**
     * Устанавливает параметр anchor
     * @param anchor Значение параметра
     * @return Объект this, пригодный для дальнейшей модификации
     */
    public GBC setAnchor(int anchor)
    {
        this.anchor = anchor;
        return this;
    }
    
    /**
     * Устанавливает параметр Fill
     * @param fill Значение параметра
     * @return Объект this, пригодный для дальнейшей модификации
     */
    public GBC setFill(int fill)
    {
        this.fill = fill;
        return this;
    }
    
    /**
     * Устанавливает веса ячейки
     * @param weightx Вес в направлении x
     * @param weighty Вес в направлении y
     * @return Объект this, пригодный для дальнейшей модификации
     */
    public GBC setWeight(double weightx,double weighty)
    {
        this.weightx = weightx;
        this.weighty = weighty;
        return this;
    }
    
    /**
     * Устанавливает размеры свободного пространства для ячейки
     * @param distance Размеры по всем направлениям
     * @return Объект this, пригодный для дальнейшей модификации
     */
    public GBC setInsets(int distance)
    {
        this.insets = new Insets(distance, distance, distance, distance);
        return this;
    }
    
    /**
     * Устанавливает размеры свободного пространства для ячейки
     * @param top Размер верхней части свободного пространства
     * @param left Размер левой части свободного пространства
     * @param bottom Размер нижней части свободного пространства
     * @param right Размер правой части свободного пространства
     * @return Объект this, пригодный для дальнейшей модификации
     */
    public GBC setInsets(int top,int left,int bottom,int right)
    {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }
    
    /**
     * Устанавливает внутреннее заполнение
     * @param ipadx Внутреннее заполнение в направлении x
     * @param ipady Внутреннее заполнение в направлении y
     * @return Объект this, пригодный для дальнейшей модификации
     */
    public GBC setIpad(int ipadx,int ipady)
    {
        this.ipadx = ipadx;
        this.ipady = ipady;
        return this;
    }
}
