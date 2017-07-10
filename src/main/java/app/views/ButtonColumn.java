package app.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class ButtonColumn<S> extends JFXTreeTableColumn<S, Boolean> {

    private final ButtonCell mCell = new ButtonCell();
    private OnButtonClickListener mOnButtonClickListener;

    public interface OnButtonClickListener {
        void onClick(int rowIndex);
    }

    public ButtonColumn() {
        setCellFactory(column -> new ButtonCell());
        setSortable(false);
        setMaxWidth(60);
        setMinWidth(60);
        getStyleClass().add("column-button");
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mOnButtonClickListener = listener;
    }

    public class ButtonCell extends TreeTableCell<S, Boolean> {

        final JFXButton mButton = new JFXButton();
        final StackPane mButtonContainer = new StackPane();

        ButtonCell() {
            mButtonContainer.getChildren().add(mButton);
            mButtonContainer.getStyleClass().add("btn-column-container");

            mButton.getStyleClass().add("btn-table");
            mButton.setOnAction(e -> {
                if (mOnButtonClickListener != null)
                    mOnButtonClickListener.onClick(getIndex());
            });
        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(empty ? null : mButtonContainer);
        }

        public JFXButton getButton() {
            return mButton;
        }
    }
}
