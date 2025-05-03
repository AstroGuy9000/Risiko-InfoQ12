package io.Risiko.CustomWidgets.TextInputWindow;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import io.Risiko.KeyBinds;
import io.Risiko.CustomWidgets.TextFieldCust;
import io.Risiko.CustomWidgets.FileMenu.FileMenuListener;

public class TextInputWindow extends Window{
	
	private Label title;
	private TextFieldCust textField;

	private ArrayList<TextInputWindowListener> listeners;
	
	public TextInputWindow (String titleStr, Skin skin, KeyBinds binds) {
		super("", skin, "dialog");
		
		listeners = new ArrayList<TextInputWindowListener>();
		
		title = new Label(titleStr, skin, "title");
		
		textField = new TextFieldCust("", skin, binds) {
			@Override
			public void acceptPressed() {
				for(TextInputWindowListener i: listeners) {
					i.textAccepted(textField.getText());
				}
			}
			
			@Override
			public void backPressed() {
				for(TextInputWindowListener i: listeners) {
					i.exitInput();
				}
			}
		};
		textField.setFillParent(false);
		
		padTop(0).padLeft(5);
		add(title);
		row();
		add(textField).expandX().fillX();
		padLeft(13);
		setMovable(false);
	}
	
	public void giveKeyFocus(Stage stage) {
		stage.setKeyboardFocus(textField);
	}
	
	public void setText(String text) {
		textField.setText(text);
	}
	
	public String getText() {
		return textField.getText();
	}

	public void addListener(TextInputWindowListener listenerIn) {
		listeners.add(listenerIn);
	}
	
	public void clearListeners() {
		listeners = new ArrayList<TextInputWindowListener>();
	}
}
