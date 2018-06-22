package pplr.gui.application.tools;

import java.io.File;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

/**
 * Create a tree view file system. The tree view represented the containing files to the set path.
 * @author CM
 *
 */
public class FileTreeView {

	/**
	 * Create the tree view items.
	 * @param dir Directory of the file system
	 * @param parent item of the file
	 * @return tree view representation of the directory
	 */
	public TreeItem<File> createTreeView(File dir, TreeItem<File> parent) {

		TreeItem<File> root = new TreeItem<>(dir);
		root.setExpanded(true);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				createTreeView(file, root);
			} else {
				root.getChildren().add(new TreeItem<>(file));
			}
		}
		if (parent == null) {
			return root;
		} else {
			parent.getChildren().add(root);
		}
		return null;
	}

	/**
	 * Event handling for the tree view.
	 * @param locationTreeView directory of the local file system the tree view should represent
	 */
	public void setCellFactory(TreeView<File> locationTreeView) {
		locationTreeView.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {

			public TreeCell<File> call(TreeView<File> tv) {
				return new TreeCell<File>() {

					@Override
					protected void updateItem(File item, boolean empty) {
						super.updateItem(item, empty);

						setText((empty || item == null) ? "" : item.getName());
					}
				};
			}
		});
	}
}
