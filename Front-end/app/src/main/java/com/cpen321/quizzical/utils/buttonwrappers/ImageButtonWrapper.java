package com.cpen321.quizzical.utils.buttonwrappers;

import android.widget.ImageButton;

import com.cpen321.quizzical.utils.buttonwrappers.ButtonTypes;
import com.cpen321.quizzical.utils.buttonwrappers.IButtons;

public class ImageButtonWrapper implements IButtons {

    private ImageButton imageButton;

    public ImageButtonWrapper(ImageButton imageButton) {
        this.imageButton = imageButton;
    }

    @Override
    public ButtonTypes getButtonType() {
        return ButtonTypes.Image;
    }

    @Override
    public IButtons getButton() {
        return this;
    }

    @Override
    public void myButtonSetBackGroundColor(int color) {
        imageButton.setBackgroundColor(color);
    }

    public ImageButton getButtonAsImageButton() {
        return imageButton;
    }
}