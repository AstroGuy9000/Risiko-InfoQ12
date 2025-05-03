package io.Risiko.CustomWidgets.FileMenu;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.Risiko.KeyBinds;
import io.Risiko.CustomWidgets.ListCust;

public class FileMenu extends Window{

	private VerticalGroup group;
	private Label title;
	private ListCust<String> list;
	private FileHandle[] files;
	
	private ArrayList<FileMenuListener> listeners;
	
	public FileMenu (String titleStr, Skin skin, FileHandle dir, String extension, KeyBinds binds) {
		super("", skin, "dialog");
		
		listeners = new ArrayList<FileMenuListener>();
		
		group = new VerticalGroup();
		group.setFillParent(true);
		group.center();
		
		title = new Label(titleStr, skin, "title");
		group.padTop(7).addActor(title);
		
		FileHandle[] unfilteredFiles = dir.list();
		ArrayList<FileHandle> filesList = new ArrayList<FileHandle>();
		for(FileHandle i: unfilteredFiles) {
			if(extension.equals(i.extension())) {
				filesList.add(i);
			}
		}
		
		files = new FileHandle[filesList.size()];
		for(int i = 0; i < filesList.size(); i++) {
			files[i] = filesList.get(i);
		}
		String[] fileNames = new String[files.length];
		for(int i = 0; i < files.length; i++) {
			fileNames[i] = files[i].nameWithoutExtension() + "  ";
		}
		
		list = new ListCust<String>(skin, "dimmed", binds) {
			@Override
			public boolean acceptPressed() {
				for(FileMenuListener i: listeners) {
					i.selectionConfirmed(getSelectedFile());
				}
				return true;
			}
		};
		list.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				for(FileMenuListener i: listeners) {
					i.movedSelectionToFile(getSelectedFile());
				}
			}});
		
		list.setFillParent(false);
		list.setItems(fileNames);
		
		group.top().center();
		group.addActor(list);
		
		padTop(0).padLeft(5);
		add(group);
		setMovable(false);
	}
	
	public FileHandle getSelectedFile() {
		int i = list.getSelectedIndex();
		if(i >= 0 && i < files.length) {
			return files[i];
		}
		return null;
	}
	
	public void setKeyboardFocus(Stage stageUI) {
		stageUI.setKeyboardFocus(list);
	}
	
	public void addListener(FileMenuListener listenerIn) {
		listeners.add(listenerIn);
	}
	
	public void clearListeners() {
		listeners = new ArrayList<FileMenuListener>();
	}
	
	private ListCust<String> getList() {
		return list;
	}
}