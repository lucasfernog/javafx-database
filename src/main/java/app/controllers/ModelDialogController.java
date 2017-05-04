package app.controllers;

import com.jfoenix.controls.JFXButton;
import database.model.Model;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

/**
 * Controller base para dialogs de inclusão/atualização de registros
 * O dialog deve obrigatoriamente possuir:
 * - um botão de salvar, com id mSaveButton
 * <p>
 * Automaticamente realiza o controle de quando o binding view-model deve ser realizado
 * Controla os callbacks de sucesso/erro do save da model
 *
 * @param <T> Tipo da model relacionada ao dialog
 */
public abstract class ModelDialogController<T extends Model> {

    public interface CloseDialogCallback {
        void closeDialog();
    }

    @FXML
    private JFXButton mSaveButton;

    private CloseDialogCallback mCloseDialogCallback;

    T mModel;
    private boolean mCanSave;

    /**
     * Define a model a ser exibida no dialog
     * Por padrão, a inserção/alteração da model é habilitada
     *
     * @param model a model a ser exibida na view
     */
    public void setModel(T model) {
        setModel(model, true);
    }

    public void setCloseDialogCallback(CloseDialogCallback callback) {
        mCloseDialogCallback = callback;
    }

    /**
     * Define a model a ser exibida no dialog
     *
     * @param model   a model a ser exibida na view
     * @param canSave se a model poderá ser inserida/atualizada ou se apenas a visualização é requisitada
     */
    public void setModel(T model, boolean canSave) {
        mModel = model;
        mCanSave = canSave;
        if (model != null) {
            loadModel(canSave);

            /**
             * No modo de visualização, remove o botão de salvar
             */
            if (!canSave)
                ((FlowPane) mSaveButton.getParent()).getChildren().remove(mSaveButton);
        }
    }

    T getModel() {
        return mModel;
    }

    /**
     * Vincular os atributos da view com a model é responsabilidade da classe filha.
     *
     * @return O callback a ser utilizado internamente, ou null se algum erro de validação ocorreu e não se deve prosseguir
     */
    abstract Model.SaveCallback saveModel();

    /**
     * Invocado quando é necessário atualizar a view com os valores contidos na model
     *
     * @param canSave true se a model poderá ser inserida/atualizada; falso caso contrário (visualização apenas)
     */
    void loadModel(boolean canSave) {

    }

    /**
     * Inicializa os atributos anotados com @FXML
     * Método invocado pelo FXMLLoader
     */
    public void initialize() {
        mSaveButton.setOnAction(e -> {
            Model.SaveCallback callback = saveModel();
            if (callback != null)
                callback.onSuccess(() -> {
                    mCloseDialogCallback.closeDialog();
                }).onError(() -> {
                    mCloseDialogCallback.closeDialog();
                });
        });
    }
}
